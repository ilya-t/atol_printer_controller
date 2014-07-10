package com.atolprinterhelper;

import com.atol.services.ecrservice.ParcelableDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CashCheck {
    private static final double MAX_TOTAL = 40*1000*1000;

    private PaymentType paymentType;
    private List<CheckItem> itemList = new ArrayList<>();
    private int checkNumber;
    private Date checkTime;

    public CashCheck(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public List<CheckItem> getItemList() {
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
            return new PrintError("Некорректная итоговая сумма - "+ String.valueOf(totalSum));
        }

        return new PrintError(DefaultPrintError.SUCCESS);
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public int getCheckNumber() {
        return checkNumber;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    void setCheckTime(ParcelableDate checkTime) {
        this.checkTime = checkTime;
    }
}
