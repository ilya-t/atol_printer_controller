package com.printerhelper.atol;

import android.content.Context;
import android.content.SharedPreferences;

import com.printerhelper.common.SettingsContainer;

public class DefaultSettingsContainer implements SettingsContainer {
    private SharedPreferences preferences;
    private static final String PREFERENCES_FILE = "atol_device_settings";
    private static final String PREFS_DEVICE_SETTINGS = "deviceSettings";

    public DefaultSettingsContainer(Context context){
        preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public void saveDeviceSettings(DeviceSettings deviceSettings) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_DEVICE_SETTINGS, deviceSettings.getSettingsConfig());
        editor.apply();
    }

    @Override
    public String getSettingsConfig() {
        return preferences.getString(PREFS_DEVICE_SETTINGS, "");
    }
}
