package com.atolprinterhelper;

import android.os.RemoteException;

import com.atol.services.ecrservice.IEcr;

import java.util.Date;

public class DeviceSettings {
    public final static int CONNECTION_BASIC = 1;//обычное подключение
    public final static int CONNECTION_UNSAFE = 2;//небезопасное подключение
    public final static int CONNECTION_OTHER = 3;//альтернативное подключение

    private String transport;
    private boolean autoEnableBluetooth;
    private String deviceAddress;
    private String deviceName;
    private String model;
    private String userPassword;
    private String accessPassword;
    private int connectionType;

    private String settingsConfig;

    private String serialNumber;
    private Date dateTime;
    private PrintError error;

    static DeviceSettings getInstance(Printer printer){
        final DeviceSettings deviceSettings = new DeviceSettings();
        PrintError error = printer.perform(new PrinterAction() {
            @Override
            public PrintError run(IEcr printer) throws RemoteException {
                deviceSettings.transport = printer.deviceSetting("transport");
                deviceSettings.deviceAddress = printer.deviceSetting("deviceAddress");
                deviceSettings.deviceName = printer.deviceSetting("deviceName");
                deviceSettings.model = printer.deviceSetting("model");
                deviceSettings.userPassword = printer.deviceSetting("userPassword");
                deviceSettings.accessPassword = printer.deviceSetting("accessPassword");
                try {
                    deviceSettings.autoEnableBluetooth = Boolean.parseBoolean(printer.deviceSetting("autoEnableBluetooth"));
                    deviceSettings.connectionType = Integer.parseInt(printer.deviceSetting("connectionType"));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                deviceSettings.settingsConfig = printer.deviceSettings();

                if (printer.isDeviceEnabled()){
                    deviceSettings.serialNumber = printer.serialNumber();

                    if (deviceSettings.serialNumber == null){
                        int errorCode;
                        errorCode = printer.setMode(Printer.MODE_CHOICE);

                        if (errorCode != DefaultPrintError.SUCCESS.code) {
                            return new PrintError(errorCode);
                        }

                        errorCode = printer.updateStatus();
                        if (errorCode != DefaultPrintError.SUCCESS.code) {
                            return new PrintError(errorCode);
                        }
                        deviceSettings.serialNumber = printer.serialNumber();
                    }


                    deviceSettings.dateTime = printer.dateTime();
                }

                return DefaultPrintError.SUCCESS.getError();
            }
        });

        deviceSettings.setError(error);

        return deviceSettings;

    }

    private DeviceSettings(){

    }

    public String getTransport() {
        return transport;
    }

    public boolean isAutoEnableBluetooth() {
        return autoEnableBluetooth;
    }

    /** @return MAC-address of device in format "AA-BB-CC-DD- EE-FF" */
    public String getDeviceAddress() {
        return deviceAddress;
    }

    /** @return bluetooth device name */
    public String getDeviceName() {
        return deviceName;
    }

    public String getModel() {
        return model;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getAccessPassword() {
        return accessPassword;
    }

    public int getConnectionType() {
        return connectionType;
    }

    /** @return printer serial number if device is connected*/
    public String getSerialNumber() {
        return serialNumber;
    }

    /** @return printer datetime if device is connected*/
    public Date getDateTime() {
        return dateTime;
    }

    public void setError(PrintError error) {
        this.error = error;
    }

    public PrintError getError() {
        return error;
    }

    public String getSettingsConfig() {
        return settingsConfig;
    }

    public boolean isDeviceConfigured() {
        return error.isClear() &&
               deviceName != null && !deviceName.equals("") &&
               deviceAddress != null && !deviceAddress.equals("");
    }
}