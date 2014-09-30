package com.atolprinterhelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

public class DeviceSettings {
    public final static int CONNECTION_BASIC = 1;//обычное подключение
    public final static int CONNECTION_UNSAFE = 2;//небезопасное подключение
    public final static int CONNECTION_OTHER = 3;//альтернативное подключение

    private String transport;
    private boolean autoEnableBluetooth;
    private boolean autoDisableBluetooth;
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

    static DeviceSettings getInstance(String settingsConfig){
        final DeviceSettings ds = new DeviceSettings();
        ds.settingsConfig = settingsConfig;
        XmlPullParser parser = null;

        try {
            // получаем фабрику
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // включаем поддержку namespace (по умолчанию выключена)
            factory.setNamespaceAware(true);
            // создаем парсер
            parser = factory.newPullParser();
            // даем парсеру на вход Reader
            parser.setInput(new StringReader(ds.settingsConfig));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        if (parser != null){
            String ELEMENT_SETTINGS = "settings";
            String TAG_VALUE = "value";
            String ATTRIBUTE_NAME = "name";


            String tagName = "", settingName = "", settingValue = "";
            try {
                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    switch (parser.getEventType()) {
                        case XmlPullParser.START_TAG:
                            tagName = parser.getName();
                            if (tagName.equals(TAG_VALUE)){
                                for (int i = 0; i < parser.getAttributeCount(); i++) {
                                    if (parser.getAttributeName(i).equals(ATTRIBUTE_NAME)){
                                        settingName = parser.getAttributeValue(i);
                                    }
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (tagName.equals(TAG_VALUE)){
                                switch (settingName){
                                    case "DeviceName":{
                                        ds.deviceName = settingValue;
                                    }break;
                                    case "Port":{
                                        ds.transport = settingValue;
                                    }break;
                                    case "Model":{
                                        ds.model = settingValue;
                                    }break;
                                    case "MACAddress":{
                                        ds.deviceAddress = settingValue;
                                    }break;
                                    case "AutoDisableBluetooth":{
                                        ds.autoDisableBluetooth = settingValue.equals("1");
                                    }break;
                                    case "connectionType":{
                                        try {
                                            ds.connectionType = Integer.parseInt(settingValue);
                                        } catch (NumberFormatException e) {
                                            e.printStackTrace();
                                        }
                                    }break;
                                    case "UserPassword":{
                                        ds.userPassword = settingValue;
                                    }break;
                                    case "AutoEnableBluetooth":{
                                        ds.autoEnableBluetooth = settingValue.equals("1");
                                    }break;
                                    case "AccessPassword":{
                                        ds.accessPassword = settingValue;
                                    }break;
                                }
                            }

                            tagName = settingName = settingValue = "";
                            break;
                        case XmlPullParser.TEXT:
                            if (tagName.equals(TAG_VALUE) && !settingName.equals("")){
                                settingValue = parser.getText();
                            }
                            break;
                    }
                    parser.next();
                }
            } catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        }

        return ds;
    }

    public static DeviceSettings getInstance(Printer printer, final boolean includeDeviceInfo){
        final DeviceSettings deviceSettings = getInstance(printer.getDriver().get_DeviceSettings());


        if (includeDeviceInfo && printer.getDriver().get_DeviceEnabled()) {
            deviceSettings.serialNumber = printer.getDriver().get_SerialNumber();

            if (deviceSettings.serialNumber == null) {
                PrintError printError = printer.setMode(Printer.MODE_CHOICE);

                if (!printError.isClear()){
                    deviceSettings.error = printError;
                }
                deviceSettings.serialNumber = printer.getDriver().get_SerialNumber();
            }


            deviceSettings.dateTime = printer.getDriver().get_Time();
        }

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