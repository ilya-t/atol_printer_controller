package com.atolprinterhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.atol.drivers.fptr.IFptr;
import com.atol.drivers.fptr.settings.SettingsActivity;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Printer {
    /**Гашение контрольной ленты*/
    public static final int REPORT_TYPE_TAPE_DAMPING = 0;
    /**Суточный отчет с гашением*/
    public static final int REPORT_TYPE_DAILY_DAMPING = 1;
    /**Суточный отчет без гашения*/
    public static final int REPORT_TYPE_DAILY = 2;
    /**Отчет по секциям*/
    public static final int REPORT_TYPE_SECTIONS = 7;

    protected static final int TEXT_ALIGNMENT_LEFT = 0;
    protected static final int TEXT_ALIGNMENT_CENTER = 1;
    protected static final int TEXT_ALIGNMENT_RIGHT = 2;

    protected static final int TEXT_WRAP_DISABLED = 0;
    protected static final int TEXT_WRAP_WORD = 1;
    protected static final int TEXT_WRAP_LINE = 2;

    /**Режим выбора*/
    public static final int MODE_CHOICE = 0;
    /**Режим регистрации*/
    public static final int MODE_REGISTRATION = 1;
    /**X-отчет*/
    public static final int MODE_XREPORT = 2;
    /**Z-отчет*/
    public static final int MODE_ZREPORT = 3;

    /** Чек продажи*/
    public static final int CHECK_TYPE_SALE = 1;
    /** Чек возврата*/
    public static final int CHECK_TYPE_REFUND = 2;
    /** Чек аннулирования*/
    public static final int CHECK_TYPE_ANNULATE = 3;
    /** Чек покупки*/
    public static final int CHECK_TYPE_PURCHASE = 4;
    /** Чек возврата покупки*/
    public static final int CHECK_TYPE_PURCHASE_REFUND = 5;
    /** Чек аннулирования покупки*/
    public static final int CHECK_TYPE_PURCHASE_ANNULATE = 6;

    /**Чек закрыт*/
    public static final int CHECK_STATE_CLOSED = 0;
    /**Чек продажи*/
    public static final int CHECK_STATE_SALE = 1;
    /**Чек возврата*/
    public static final int CHECK_STATE_REFUND = 2;
    /**Чек аннулирования*/
    public static final int CHECK_STATE_ANNULATE = 3;
    /**Чек покупки*/
    public static final int CHECK_STATE_PURCHASE = 4;
    /**Чек возврата покупки*/
    public static final int CHECK_STATE_PURCHASE_REFUND = 5;
    /**Чек аннулирования покупки.*/
    public static final int CHECK_STATE_PURCHASE_ANNULATE = 6;

    //default payment types
    public static final int PAYMENT_TYPE_CASH = 0;
    public static final int PAYMENT_TYPE_CREDIT = 1;
    public static final int PAYMENT_TYPE_PACKAGE = 2;
    public static final int PAYMENT_TYPE_CARD = 3;
    //custom payment types
    public static final int PAYMENT_TYPE_4 = 4;
    public static final int PAYMENT_TYPE_5 = 5;
    public static final int PAYMENT_TYPE_6 = 6;
    public static final int PAYMENT_TYPE_7 = 7;
    public static final int PAYMENT_TYPE_8 = 8;
    public static final int PAYMENT_TYPE_9 = 9;
    public static final int PAYMENT_TYPE_10 = 10;

    private static Printer instance;

    private static final int REQUEST_CODE = 38921;
    final Context context;
    private IFptr driver;


    private SettingsContainer settingsContainer;
    private DeviceSettings connectionSettings;

    public synchronized static Printer getInstance(Context context) throws IllegalAccessException{
        if (instance == null){
            instance = new Printer(context);
        }

        return instance;
    }

    protected Printer(Context context) throws IllegalAccessException{
        this.context = context;
        settingsContainer = (this instanceof SettingsContainer)
                                ?(SettingsContainer)this
                                :new DefaultSettingsContainer(context);
        if (driver == null){
            initDriver();
        }
    }

    private void initDriver() throws IllegalAccessException {
        if (driver != null){
            driver.destroy();
            driver = null;
        }
        driver = new IFptr();
        try {
            driver.create(context);
        } catch (NullPointerException e) {
            e.printStackTrace();
            driver = null;
            throw new IllegalAccessException("unable to access printer driver: "+
                    (e.getMessage() != null ? e.getMessage() : e.toString()));
        }

        driver.put_DeviceEnabled(false);
    }


    /** launches Settings activity inside printer service app */
    public static void configure(Activity activity) {
        Intent intent = new Intent(activity, SettingsActivity.class);
//        intent.putExtra(SettingsActivity.DEVICE_SETTINGS, printer.get_DeviceSettings());
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

    public boolean isConnected() {
        return driver != null && driver.get_DeviceEnabled();
    }


    public PrintError connectDevice() throws IllegalAccessException {
        String settingsConfig = settingsContainer.getSettingsConfig();

        PrintError error = tryConnect(settingsConfig);

        if (error.isClear() && !isConnected()){
            initDriver();

            error = tryConnect(settingsConfig);


            if (error.isClear() && !isConnected()){
                error = DefaultPrintError.DEVICE_CONNECTION.get();
            }
        }

        if (!error.isClear()){
            driver.put_DeviceEnabled(false);
        }

        return error;
    }

    private PrintError tryConnect(String settingsConfig){
        if (driver.put_DeviceSettings(settingsConfig) != 0){
            return getLastError();
        }

        return driver.put_DeviceEnabled(true) != 0 ? getLastError() : DefaultPrintError.SUCCESS.get();
    }

    public PrintError setMode(final int mode){
        driver.put_UserPassword(getConnectionSettings().getUserPassword());
        driver.put_Mode(mode);
        if (driver.SetMode() != 0){
            return getLastError();
        }
        return DefaultPrintError.SUCCESS.get();
    }

    public PrintError resetMode(){
        return  driver.ResetMode() != 0 ? getLastError() : DefaultPrintError.SUCCESS.get();
    }

    public PrintError cancelCheck(){
        return  driver.CancelCheck() != 0 ? getLastError() : DefaultPrintError.SUCCESS.get();
    }

    public PrintError printCheck(final CashCheck<? extends CheckItem> cashCheck, final int checkType){
        PrintError error = cashCheck.verify();
        if (!error.isClear()) {
            return error;
        }

        driver.put_CheckType(checkType);
        if (driver.OpenCheck() != 0){
            return getLastError();
        }

        float commonDiscount = cashCheck.getItemList().get(0).getDiscount();

        for (CheckItem checkItem : cashCheck.getItemList()) {
            if (commonDiscount != checkItem.getDiscount()) {
                commonDiscount = 0f;
                break;
            }
        }

        error = printLines(cashCheck.getHeaders());
        if (!error.isClear()) {
            return error;
        }

        for (CheckItem checkItem : cashCheck.getItemList()) {
            error = printLines(checkItem.getHeaders());
            if (!error.isClear()) {
                return error;
            }

            driver.put_Name(checkItem.getTitle());
            driver.put_Quantity(checkItem.getQuantity());
            driver.put_Price(checkItem.getPrice());
            driver.put_Department(checkItem.getDepartment());

            driver.put_TextWrap(TEXT_WRAP_WORD);
            int errorCode = 0;
            switch (checkType) {
                case CHECK_TYPE_SALE: {
                    errorCode = driver.Registration();
                }break;
                case CHECK_TYPE_REFUND: {
                    errorCode = driver.Return();
                }break;
                case CHECK_TYPE_ANNULATE: {
                    errorCode = driver.Annulate();
                }break;
                case CHECK_TYPE_PURCHASE: {
                    errorCode = driver.Buy();
                }break;
                case CHECK_TYPE_PURCHASE_REFUND: {
                    errorCode = driver.BuyReturn();
                }break;
                case CHECK_TYPE_PURCHASE_ANNULATE: {
                    errorCode = driver.BuyAnnulate();
                }break;}

            if (errorCode != 0){
                return getLastError();
            }

            if (checkItem.getDiscount() > 0f && Float.compare(commonDiscount, 0f) == 0) {
                error = printString("( " + context.getString(R.string.check_item_discount) + " " + String.valueOf(checkItem.getDiscount()) + "%)");
                if (!error.isClear()){
                    return error;
                }
            }
        }


        if (commonDiscount > 0f) {
            error = printString(
                    context.getString(R.string.check_discount) + " " + String.valueOf(commonDiscount) + "%");

            if (!error.isClear()){
                return error;
            }
        }

        if (driver.GetStatus() != 0){
            driver.CancelCheck();
            return getLastError();
        }

        long checkTime = getPrinterTimeInMillis();
        int checkId = driver.get_CheckNumber();
        long timeStart = Calendar.getInstance().getTimeInMillis();

        driver.put_TypeClose(cashCheck.getPaymentType());
        if (driver.CloseCheck() != 0){
            return getLastError();
        }

        long timeEnd = Calendar.getInstance().getTimeInMillis();
        checkTime = checkTime + (timeEnd-timeStart);

        cashCheck.setCheckTime(checkTime);
        cashCheck.setCheckNumber(checkId);

/*
        // checking that driver memory is ok (if check was printed in less than 0.1 second)
        if (timeStart < timeEnd && timeEnd - timeStart < 100) {
            return DefaultPrintError.OUT_OF_MEMORY.get();
        }
*/
        return DefaultPrintError.SUCCESS.get();
    }

    private PrintError printLines(List<String> lines) {
        if (lines != null){
            for (String line : lines){
                PrintError error = printString(line);

                if (!error.isClear()){
                    return error;
                }
            }
        }

        return DefaultPrintError.SUCCESS.get();
    }

    PrintError getLastError() {
        if (driver != null){
            PrintError lastError = new PrintError(driver.get_ResultCode(), driver.get_ResultDescription());
            if (
                    lastError.getErrorCode() == DefaultPrintError.DEVICE_DISCONNECT.code ||
                    lastError.getErrorCode() == DefaultPrintError.DEVICE_CONNECTION.code){
                driver.put_DeviceEnabled(false);
            }
            return lastError;
        }
        return DefaultPrintError.FAIL.get();
    }

    public PrintError printString(final String line) {
        driver.put_TextWrap(TEXT_WRAP_WORD);
        driver.put_Caption(line);
        driver.put_Alignment(TEXT_ALIGNMENT_LEFT);

        int errorCode = driver.PrintString();
        return errorCode != 0 ? getLastError() : DefaultPrintError.SUCCESS.get();
    }

    public PrintError report(final int reportType){
        driver.put_ReportType(reportType);
        return driver.Report() != 0 ? getLastError() : DefaultPrintError.SUCCESS.get();
    }

    /** disconnects from printer device */
    public void disconnectDevice() {
        driver.put_DeviceEnabled(false);
    }

    public void terminateInstance(){
        driver.destroy();
        instance = null;
    }

    public PrintError applyDeviceSettings(final String deviceSettings) {
        return driver.put_DeviceSettings(deviceSettings) != 0 ? getLastError() : DefaultPrintError.SUCCESS.get();
    }

    public DeviceSettings getDeviceSettings(){
        return DeviceSettings.getInstance(this, true);
    }

    public int getMode(){
        return driver.get_Mode();
    }

    public long getPrinterTimeInMillis(){
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(driver.get_Date().getTime());

        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(driver.get_Time().getTime());

        date.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        date.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
        date.set(Calendar.SECOND, time.get(Calendar.SECOND));
        return date.getTimeInMillis();
    }

    public boolean isSessionOpened() {
        return driver.get_SessionOpened();
    }

    /** @return divider line that fits check max width*/
    public String getDividerLine(final char divider) {
        int length = driver.get_CharLineLength();
        char[] array = new char[length];
        Arrays.fill(array, divider);
        return new String(array);
    }

    private DeviceSettings getConnectionSettings() {
        if (connectionSettings == null){
            connectionSettings = DeviceSettings.getInstance(settingsContainer.getSettingsConfig());
        }
        return connectionSettings;
    }

    public boolean isConfigured() {
        return getConnectionSettings().isDeviceConfigured();
    }

    protected IFptr getDriver() {
        return driver;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE){
            if (data != null && data.getExtras() != null && data.getExtras().containsKey(SettingsActivity.DEVICE_SETTINGS)){
                String settings = data.getExtras().getString(SettingsActivity.DEVICE_SETTINGS);

                DeviceSettings deviceSettings = DeviceSettings.getInstance(settings);
                if (deviceSettings.getError().isClear() && deviceSettings.isDeviceConfigured()){
                    settingsContainer.saveDeviceSettings(deviceSettings);
                }
            }
        }
    }
}
