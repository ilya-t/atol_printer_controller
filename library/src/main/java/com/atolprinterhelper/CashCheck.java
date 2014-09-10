package com.atolprinterhelper;

import com.atol.services.ecrservice.ParcelableDate;

import java.util.ArrayList;
import java.util.List;

public class CashCheck<T extends CheckItem> {
    protected final static int CHECK_NUMBER_UNKNOWN = -1;
    private static final double MAX_TOTAL = 40*1000*1000;
    private static final int ERROR_CODE_WRONG_SUM = 18;

    private int paymentType;
    private List<T> itemList = new ArrayList<>();
    private List<String> headers;
    private int checkNumber = CHECK_NUMBER_UNKNOWN;
    private long checkTime;

    public CashCheck(int paymentType) {
        this.paymentType = paymentType;
    }

    public List<T> getItemList() {
        return itemList;
    }

    void setCheckNumber(int checkId) {
        this.checkNumber = checkId;
    }

    PrintError verify() {
        double totalSum = 0;
        for (CheckItem checkItem : itemList){
            totalSum += checkItem.getPrice() * checkItem.getQuantity();
        }

        if (totalSum > MAX_TOTAL){
            return new PrintError(ERROR_CODE_WRONG_SUM, "Некорректная итоговая сумма - "+ String.valueOf(totalSum));
        }

        return DefaultPrintError.SUCCESS.getError();
    }

    public int getPaymentType() {
        return paymentType;
    }

    public int getCheckNumber() {
        return checkNumber;
    }

    public long getCheckTime() {
        return checkTime;
    }

    protected void setCheckTime(ParcelableDate checkTime) {
        this.checkTime = checkTime.getTime()/1000;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    List<String> getHeaders() {
        return headers;
    }
}
