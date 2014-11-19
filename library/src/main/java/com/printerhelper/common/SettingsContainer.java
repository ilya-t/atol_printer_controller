package com.printerhelper.common;

import com.printerhelper.atol.DeviceSettings;

public interface SettingsContainer {
    void saveDeviceSettings(DeviceSettings deviceSettings);
    String getSettingsConfig();
}
