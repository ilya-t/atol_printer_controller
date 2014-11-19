package com.printerhelper.common;

import com.printerhelper.atol.DefaultPrintError;
import com.printerhelper.atol.PrintError;

import java.util.ArrayList;
import java.util.List;

public class BaseCashCheck<T extends CheckItem> {
    protected final static int CHECK_NUMBER_UNKNOWN = -1;
    private static final int ERROR_CODE_WRONG_COUNT = 19;
    private String paymentType;
    private List<T> itemList = new ArrayList<>();
    private List<String> headers;
    private int checkNumber = CHECK_NUMBER_UNKNOWN;
    private long checkTime;

    public BaseCashCheck(String paymentType) {
        this.paymentType = paymentType;
    }

    public List<T> getItemList() {
        return itemList;
    }

    public void setCheckNumber(int checkId) {
        this.checkNumber = checkId;
    }

    protected BasePrintError verify() {
        if (itemList.size() == 0){
            return new PrintError(ERROR_CODE_WRONG_COUNT, "В чеке отсутствуют позиции");
        }

        return DefaultPrintError.SUCCESS.get();
    }

    public String getPaymentType() {
        return paymentType;
    }

    public int getCheckNumber() {
        return checkNumber;
    }

    public long getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(long checkTimeInMillis) {
        this.checkTime = checkTimeInMillis/1000;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<String> getHeaders() {
        return headers;
    }
}
