package com.printerhelper.atol;

import android.text.TextUtils;

import com.atol.drivers.fptr.settings.DeviceSettings;
import com.printerhelper.common.BaseDeviceSettings;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

public class AtolDeviceSettings implements BaseDeviceSettings {
    /** обычное подключение */
    public final static int CONNECTION_BASIC = 1;
    /** небезопасное подключение */
    public final static int CONNECTION_UNSAFE = 2;
    /** альтернативное подключение */
    public final static int CONNECTION_OTHER = 3;

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
    private long dateTime;//timestamp
    private PrintError error;

    static AtolDeviceSettings getInstance(String settingsConfig){
        final AtolDeviceSettings ds = new AtolDeviceSettings();
        ds.settingsConfig = settingsConfig;
        XmlPullParser parser = null;

        if (settingsConfig != null){
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                parser = factory.newPullParser();
                parser.setInput(new StringReader(ds.settingsConfig));
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        boolean hasData = false;
        if (parser != null){
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
                                hasData = true;
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

        ds.error = hasData?DefaultPrintError.SUCCESS.get():DefaultPrintError.FAIL.get();

        return ds;
    }

    public static AtolDeviceSettings getInstance(AtolPrinter atolPrinter, final boolean includeDeviceInfo){
        final AtolDeviceSettings deviceSettings = getInstance(atolPrinter.getDriver().get_DeviceSettings());


        if (includeDeviceInfo) {
            if (!atolPrinter.isConnected()){
                PrintError error = atolPrinter.connectDevice();

                if (!error.isClear()){
                    deviceSettings.error = error;
                    return deviceSettings;
                }
            }

            if (atolPrinter.getDriver().GetStatus() != 0){
                deviceSettings.error = atolPrinter.getLastError();
                return deviceSettings;
            }

            deviceSettings.serialNumber = atolPrinter.getDriver().get_SerialNumber();

            if (deviceSettings.serialNumber == null || deviceSettings.serialNumber.equals("")) {
                PrintError printError = atolPrinter.setMode(AtolPrinter.MODE_CHOICE);

                if (!printError.isClear()){
                    deviceSettings.error = printError;
                }
                deviceSettings.serialNumber = atolPrinter.getDriver().get_SerialNumber();
            }


            deviceSettings.dateTime = atolPrinter.getPrinterTimeInMillis()/1000;
        }
        return deviceSettings;
    }

    private AtolDeviceSettings(){

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
    @Override
    public String getSerialNumber() {
        return serialNumber;
    }

    @Override
    public boolean isConfigured() {
        return deviceName != null && !deviceName.equals("") &&
                deviceAddress != null && !deviceAddress.equals("");
    }

    /** @return printer timestamp (since last status update) if device is connected*/
    @Override
    public long getDateTime() {
        return dateTime;
    }

    public void setError(PrintError error) {
        this.error = error;
    }

    public PrintError getError() {
        return error;
    }

    @Override
    public String getDeviceConfig() {
        if (TextUtils.isEmpty(settingsConfig)) {
            return createEmptyDeviceSettings();
        }
        return settingsConfig;
    }

    /**
     * Workaround for 9.9.1 driver version. 'ConnectionType' value must be set
     * in order to be used later at {@link com.atol.drivers.fptr.settings.BluetoothSearchActivity}.
     */
    private static String createEmptyDeviceSettings() {
        DeviceSettings deviceSettings = new DeviceSettings();
        deviceSettings.add("ConnectionType", "1");
        return deviceSettings.toXML();
    }
}