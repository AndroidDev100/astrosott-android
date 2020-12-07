package com.dialog.dialoggo.utils.helpers;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;


public class UDID {

    public static String getDeviceId(Context context,ContentResolver contentResolver) {

        try {
            if (KsPreferenceKey.getInstance(context).getUser() != null && KsPreferenceKey.getInstance(context).getUser().getUsername() != null && !TextUtils.isEmpty(KsPreferenceKey.getInstance(context).getUser().getUsername())) {

                String android_id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);

                StringBuilderHolder.getInstance().clear();
                StringBuilderHolder.getInstance().append(AppLevelConstants.MOBILE);
                StringBuilderHolder.getInstance().append("_");
                StringBuilderHolder.getInstance().append(KsPreferenceKey.getInstance(context).getUser().getUsername());
                StringBuilderHolder.getInstance().append("_");
                StringBuilderHolder.getInstance().append(android_id);

                PrintLogging.printLog("UDID", "", "UDIDOFDEVICE--" + android_id);
            } else {
                String android_id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
                StringBuilderHolder.getInstance().clear();
                StringBuilderHolder.getInstance().append(android_id);
                PrintLogging.printLog("User Name", "", "user name is blank");
            }
        }catch (Exception e){

        }

//        return android_id;

        return StringBuilderHolder.getInstance().getText().toString();
    }

    public static String getDeviceIdForHungama(Context context,ContentResolver contentResolver) {
        String android_id = "";
        try {
             android_id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
        }catch (Exception e){

        }

//        return android_id;

        return android_id;
    }
}
