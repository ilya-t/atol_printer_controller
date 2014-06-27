package com.atolprinterhelper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.atol.services.ecrservice.IEcr;

public class Printer {
    private static final int TEXT_ALIGNMENT_LEFT = 0;
    private static final int TEXT_ALIGNMENT_CENTER = 1;
    private static final int TEXT_ALIGNMENT_RIGHT = 2;

    private static final int TEXT_WRAP_DISABLED = 0;
    private static final int TEXT_WRAP_LINE = 1;
    private static final int TEXT_WRAP_WORD = 2;

    public static final int MODE_CHOICE = 0;
    public static final int MODE_REGISTRATION = 1;
    public static final int MODE_XREPORT = 2;
    public static final int MODE_ZREPORT = 2;

    private static final int CHECK_TYPE_CLOSED = 0;//Чек закрыт
    private static final int CHECK_TYPE_SALE = 1;// Чек продажи
    private static final int CHECK_TYPE_REFUND = 2;// Чек возврата
    private static final int CHECK_TYPE_CANCEL = 3;// Чек аннулирования
    private static final int CHECK_TYPE_PURCHASE = 4;// Чек покупки
    private static final int CHECK_TYPE_PURCHASE_REFUND = 5;// Чек возврата покупки
    private static final int CHECK_TYPE_PURCHASE_CANCEL = 6;// Чек аннулирования покупки

    static final int PAYMENT_TYPE_CASH = 0;
    static final int PAYMENT_TYPE_CREDIT = 1;
    static final int PAYMENT_TYPE_PACKAGE = 2;
    static final int PAYMENT_TYPE_CARD = 3;


    private final PrinterServiceController sc;
    private static final int REQUEST_CODE = 38921;

    public Printer(Context context) {
        sc = PrinterServiceController.newInstance(context);

        if (!sc.isConnected()){
            sc.startService();
        }
    }

    public void configure(Activity activity) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.atol.services.ecrservice", "com.atol.services.ecrservice.settings.SettingsActivity"));
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public boolean isConnected() {
        return perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                return new PrintError(
                                        printer.isDeviceEnabled()
                                        ? DefaultPrintError.SUCCESS
                                        : DefaultPrintError.FAIL);
            }
        }).isClear();
    }

    public boolean isConfigured() {
        return perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                return new PrintError(
                        !printer.deviceSetting("deviceName").equals("") && !printer.deviceSetting("deviceAddress").equals("")
                                ? DefaultPrintError.SUCCESS
                                : DefaultPrintError.FAIL);
            }
        }).isClear();
    }

    public PrintError connect() {
        return perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                return new PrintError(printer.enableDevice(true));
            }
        });
    }

    public PrintError setMode(final int mode){
        return perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                return new PrintError(printer.setMode(mode));
            }
        });
    }

    public PrintError resetMode(){
        return perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                return new PrintError(printer.resetMode());
            }
        });
    }

    public PrintError cancelCheck(){
        return perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                return new PrintError(printer.cancelCheck());
            }
        });
    }

    public PrintError printCheck(final CashCheck cashCheck){
        return perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                PrintError error = cashCheck.verify();
                if (!error.isClear()){
                    return error;
                }

                int errorCode = printer.openCheck(CHECK_TYPE_SALE);

                if (errorCode != DefaultPrintError.SUCCESS.code){
                    return new PrintError(errorCode);
                }


                for (CheckItem checkItem : cashCheck.getItemList()){
                    errorCode = printer.registration(
                                        checkItem.getTitle(),
                                        TEXT_WRAP_WORD,
                                        TEXT_ALIGNMENT_LEFT,
                                        checkItem.getQuantity(),
                                        checkItem.getPrice(),
                                        checkItem.getDepartment());

                    if (errorCode != DefaultPrintError.SUCCESS.code){
                        return new PrintError(errorCode);
                    }
                }

                cashCheck.setCheckNumber(printer.checkNumber());

                errorCode = printer.closeCheck(cashCheck.getPaymentType().getTypeId());
                if (errorCode != DefaultPrintError.SUCCESS.code){
                    return new PrintError(errorCode);
                }
                return new PrintError(DefaultPrintError.SUCCESS);
            }
        });
    }

    private PrintError perform(PrinterAction action){
        if (!sc.isConnected()){
            return new PrintError(DefaultPrintError.SERVICE_CONNECTION);
        }

        if (sc.getPrinterInterface() == null){
            return new PrintError(DefaultPrintError.EMPTY_INTERFACE);
        }

        try {
            return action.run(sc.getPrinterInterface());
        } catch (RemoteException e) {
            e.printStackTrace();
            return new PrintError(e.toString());
        }
    }

    public PrintError printString(final String line) {
        return perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                return new PrintError(printer.printString(line, TEXT_WRAP_WORD, TEXT_ALIGNMENT_LEFT));
            }
        });
    }
}
