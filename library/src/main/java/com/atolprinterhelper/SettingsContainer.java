package com.atolprinterhelper;

public interface SettingsContainer {
    void saveDeviceSettings(DeviceSettings deviceSettings);
    String getSettingsConfig();
}
