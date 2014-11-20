package com.printerhelper.common;

import android.app.Activity;
import android.content.Intent;

public interface BasePrinter{
    public enum CheckType{
        SALE, REFUND, ANNULATE, PURCHASE, PURCHASE_REFUND, PURCHASE_ANNULATE
    }

    void configure(Activity activity);
    boolean isConnected();
    BasePrintError connectDevice();
    BasePrintError cancelCheck();
    BasePrintError printCheck(final BaseCashCheck<? extends CheckItem> cashCheck, final CheckType checkType);
    BasePrintError printString(final String line);
    BasePrintError reportX();
    BasePrintError reportZ();
    void disconnectDevice();
    void terminateInstance();

    BasePrintError applyDeviceInfo(final String deviceInfo);
    BaseDeviceSettings getDeviceInfo();

    long getPrinterTimeInMillis();
    boolean isConfigured();
    void onActivityResult(int requestCode, int resultCode, Intent data);
}

