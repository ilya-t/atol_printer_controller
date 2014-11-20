package com.printerhelper.common;

public interface SettingsContainer {
    void saveDeviceSettings(BaseDeviceSettings deviceSettings);
    String getConnectSettings();
}
