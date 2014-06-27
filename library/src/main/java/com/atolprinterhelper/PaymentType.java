package com.atolprinterhelper;

public enum PaymentType {
    CASH(Printer.PAYMENT_TYPE_CASH),
    CREDIT(Printer.PAYMENT_TYPE_CREDIT),
    PACKAGE(Printer.PAYMENT_TYPE_PACKAGE),
    CARD(Printer.PAYMENT_TYPE_CARD);
    private final int type;

    PaymentType(int type) {
        this.type = type;
    }

    int getTypeId() {
        return type;
    }
}
