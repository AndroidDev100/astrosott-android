package com.astro.sott.utils.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.astro.sott.utils.helpers.security.CryptUtil;

import java.util.HashMap;
import java.util.Map;

public class SharedPrefHelper {
    private static final String PREF_FILE = "Session";
    private static Map<Context, SharedPrefHelper> instances = new HashMap<>();
    private static SharedPrefHelper mInstance;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private CryptUtil cryptUtil;

    private SharedPrefHelper(Context context) {
        if (settings == null) {
            settings = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
            editor = settings.edit();
        }
        if (cryptUtil == null) {
            cryptUtil = CryptUtil.getInstance();
        }
    }

    public static SharedPrefHelper getInstance(Context context) {
        if (mInstance == null)
            mInstance = new SharedPrefHelper(context);
        return mInstance;
    }

    public void clear() {
        settings.edit();
        editor.clear();
        editor.commit();
    }

    public String getString(String key, String defValue) {
        String decryptedValue = cryptUtil.decrypt(settings.getString(key, defValue), AppLevelConstants.MY_DIALOG_ENCRYPTION_KEY);
        if (decryptedValue == null || decryptedValue.equalsIgnoreCase("") || key.equalsIgnoreCase("DMS_Response")) {
            decryptedValue = settings.getString(key, defValue);
        }
        return decryptedValue;


    }

    public void setString(String key, String value) {
        String encryptedValue;
        encryptedValue = cryptUtil.encrypt(value, AppLevelConstants.MY_DIALOG_ENCRYPTION_KEY);
        if (key.equalsIgnoreCase("DMS_Response") || value == null || value.equalsIgnoreCase("")) {
            editor.putString(key, value);
        } else {
            editor.putString(key, encryptedValue);

        }
        editor.commit();

    }

    public int getInt(String key, int defValue) {
        return settings.getInt(key, defValue);
    }

    public void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return settings.getBoolean(key, defValue);
    }

    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }
}
