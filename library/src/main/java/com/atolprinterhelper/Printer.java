package com.atolprinterhelper;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.RemoteException;

import com.atol.services.ecrservice.IEcr;

import java.util.List;

public class Printer {
    public static final int REPORT_TYPE_TAPE_DAMPING = 0;//Гашение контрольной ленты
    public static final int REPORT_TYPE_DAILY_DAMPING = 1;//Суточный отчет с гашением
    public static final int REPORT_TYPE_DAILY = 2;//Суточный отчет без гашения
    public static final int REPORT_TYPE_SECTIONS = 7;//Отчет по секциям

    private static final int TEXT_ALIGNMENT_LEFT = 0;
    private static final int TEXT_ALIGNMENT_CENTER = 1;
    private static final int TEXT_ALIGNMENT_RIGHT = 2;

    private static final int TEXT_WRAP_DISABLED = 0;
    private static final int TEXT_WRAP_LINE = 1;
    private static final int TEXT_WRAP_WORD = 2;

    public static final int MODE_CHOICE = 0;//Режим выбора
    public static final int MODE_REGISTRATION = 1;//Режим регистрации
    public static final int MODE_XREPORT = 2;//X-отчет
    public static final int MODE_ZREPORT = 3;//Z-отчет

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
    private static final String PREFERENCES_FILE = "atol_device_settings";
    private static final String PREFS_DEVICE_SETTINGS = "deviceSettings";
    private final static String DRIVER_PACKAGE_NAME = "com.atol.services.ecrservice";
    final Context context;

    private SharedPreferences preferences;
    private static boolean isConfiguring;

    public Printer(Context context) {
        this.context = context;
        sc = PrinterServiceController.newInstance(this);

        if (!sc.isConnected()){
            sc.startService();
        }

        preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    public static boolean configure(Activity activity) {
        if (isDriverInstalled(activity)){
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(DRIVER_PACKAGE_NAME, "com.atol.services.ecrservice.settings.SettingsActivity"));
            activity.startActivityForResult(intent, REQUEST_CODE);
            isConfiguring = true;
            return true;
        }else{
            return false;
        }
    }

    public boolean isServiceConnected(){
        return sc.isConnected();
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
                        isDeviceConfigured(printer)
                                ? DefaultPrintError.SUCCESS
                                : DefaultPrintError.FAIL);
            }
        }).isClear();
    }

    private boolean isDeviceConfigured(IEcr printer) throws RemoteException {
        return !printer.deviceSetting("deviceName").equals("") && !printer.deviceSetting("deviceAddress").equals("");
    }

    public PrintError connect() {
        return perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                return new PrintError(printer.enableDevice(true));
            }
        });
    }

    private String getStoredSettings() {
        return preferences.getString(PREFS_DEVICE_SETTINGS, "");
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

                float commonDiscount = cashCheck.getItemList().get(0).getDiscount();

                for (CheckItem checkItem : cashCheck.getItemList()){
                    if (commonDiscount != checkItem.getDiscount()){
                        commonDiscount = 0f;
                        break;
                    }
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

                    if (checkItem.getDiscount() > 0f && Float.compare(commonDiscount, 0f) == 0){
                        errorCode = printer.printString("( "+context.getString(R.string.check_item_discount)+" "+String.valueOf(checkItem.getDiscount())+"%)",
                                                        TEXT_WRAP_WORD,TEXT_ALIGNMENT_RIGHT);
                        if (errorCode != DefaultPrintError.SUCCESS.code){
                            return new PrintError(errorCode);
                        }
                    }
                }


                if (commonDiscount > 0f){
                    errorCode = printer.printString(
                            context.getString(R.string.check_discount)+" "+
                                    String.valueOf(commonDiscount)+"%",TEXT_WRAP_WORD, TEXT_ALIGNMENT_LEFT);
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

    PrintError perform(PrinterAction action){
        if (!sc.isConnected()){
            sc.startService();
            return new PrintError(DefaultPrintError.SERVICE_CONNECTION);
        }

        if (sc.getPrinterInterface() == null){
            return new PrintError(DefaultPrintError.EMPTY_INTERFACE);
        }

        if (isConfiguring){
            isConfiguring = false;
            saveDeviceSettings();
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

    public PrintError report(final int reportType){
        return perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                return new PrintError(printer.report(
                        reportType,
                        0/*unused*/,
                        0,
                        false));
            }
        });
    }

    public PrintError disconnect() {
        return perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                return new PrintError(printer.enableDevice(false));
            }
        });
    }

    public void disconnectService(){
        sc.unbindService();
    }

    protected void  onServiceConnected() {
        if (getStoredSettings().equals("")){
            saveDeviceSettings();
        }else{
            perform(new PrinterAction() {
                @Override
                public PrintError run(IEcr printer) throws RemoteException {
                    if (!isDeviceConfigured(printer)){
                        printer.setDeviceSettings(getStoredSettings());
                    }
                    return null;
                }
            });
        }
    }

    private void saveDeviceSettings() {
        perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(PREFS_DEVICE_SETTINGS, printer.deviceSettings());
                editor.apply();
                return new PrintError(DefaultPrintError.SUCCESS);
            }
        });
    }


    public DeviceSettings getDeviceSettings(){
        return DeviceSettings.getInstance(this);
    }

    public int getMode() {
        final int[] mode = new int[1];
        if (
        perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                mode[0] = printer.mode();
                return new PrintError(DefaultPrintError.SUCCESS);
            }
        }).isClear()){
            return mode[0];
        }
        return -1;
    }

    public static boolean isDriverInstalled(Context context){
        if (context != null && context.getPackageManager() != null){
            List<PackageInfo> packageList = context.getPackageManager().getInstalledPackages(0);

            for (PackageInfo packageInfo : packageList){
                if (packageInfo.packageName.equals(DRIVER_PACKAGE_NAME)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSessionOpened() {
        final boolean[] result = new boolean[1];
        perform(
                new PrinterAction() {
                    @Override
                    public PrintError run(IEcr printer) throws RemoteException {
                        result[0] = printer.isSessionOpened();
                        return new PrintError(DefaultPrintError.SUCCESS);
                    }
                });
        
        return result[0];
    }
}
