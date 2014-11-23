package com.printerhelper.common;

import android.content.Context;
import android.content.SharedPreferences;

public class DefaultSettingsContainer implements SettingsContainer {
    private SharedPreferences preferences;
    private static final String PREFERENCES_FILE = "printerhelper_device_settings";
    private static final String PREFS_DEVICE_SETTINGS = "deviceSettings";

    public DefaultSettingsContainer(Context context){
        preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public void saveDeviceSettings(BaseDeviceSettings deviceSettings) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREFS_DEVICE_SETTINGS, deviceSettings.getDeviceConfig());
        editor.apply();
    }

    @Override
    public String getConnectSettings() {
        return preferences.getString(PREFS_DEVICE_SETTINGS, "");
    }
}
