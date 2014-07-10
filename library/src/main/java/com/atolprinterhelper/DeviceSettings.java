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

    private String serialNumber;
    private Date dateTime;

    static DeviceSettings getInstance(Printer printer){
        final DeviceSettings deviceSettings = new DeviceSettings();
        printer.perform(new PrinterAction() {
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
                deviceSettings.serialNumber = printer.serialNumber();
                deviceSettings.dateTime = printer.dateTime();

                return new PrintError(DefaultPrintError.SUCCESS);
            }
        });

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

    public String getSerialNumber() {
        return serialNumber;
    }

    public Date getDateTime() {
        return dateTime;
    }
}