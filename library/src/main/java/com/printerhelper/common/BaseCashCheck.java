package com.printerhelper.common;

import java.util.ArrayList;
import java.util.List;

public class BaseCashCheck<T extends CheckItem> {
    protected final static int CHECK_NUMBER_UNKNOWN = -1;
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
