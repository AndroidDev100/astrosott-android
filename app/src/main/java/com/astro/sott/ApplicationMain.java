package com.astro.sott;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.appsflyer.AppsFlyerConversionListener;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.SharedPrefHelper;

import com.clevertap.android.sdk.ActivityLifecycleCallback;

import com.clevertap.android.sdk.CleverTapAPI;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kaltura.playkit.player.PKHttpClientManager;

import java.util.Map;

public class ApplicationMain extends MultiDexApplication {

    private static ApplicationMain mInstance;

    public static ApplicationMain getInstance() {
        return mInstance;
    }

    private AppsFlyerConversionListener conversionDataListener =
            new AppsFlyerConversionListener() {
                @Override
                public void onConversionDataSuccess(Map<String, Object> map) {

                }

                @Override
                public void onConversionDataFail(String s) {

                }

                @Override
                public void onAppOpenAttribution(Map<String, String> map) {

                }

                @Override
                public void onAttributionFailure(String s) {

                }
            };
//    private static Context context;

//    public static Context getAppContext() {
//        return ApplicationMain.context;
//    }

    @Override
    public void onCreate() {
        ActivityLifecycleCallback.register(this);
        super.onCreate();
        CleverTapAPI.createNotificationChannel(this, "sooka-channel", "test-channel", "test-channel", NotificationManager.IMPORTANCE_MAX, true);
        mInstance = this;
//        ApplicationMain.context = getApplicationContext();
        MultiDex.install(this);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                connectionWarmup();

                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                    String token = instanceIdResult.getToken();
                    Log.e("ApplicationMain", "run: " + token);
//                    PrintLogging.printLog(ApplicationMain.class, "", token + "fcmtokentoken");
                    SharedPrefHelper.getInstance(getApplicationContext()).setString(AppLevelConstants.FCM_TOKEN, token);
                });
                // AudienceNetworkAds.initialize(getApplicationContext());
                //AppsFlyerLib.getInstance().init(AppConstants.AF_DEV_KEY, conversionDataListener, getApplicationContext());
                //AppsFlyerLib.getInstance().startTracking(ApplicationMain.this, AppConstants.AF_DEV_KEY);

                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                    String token = instanceIdResult.getToken();
                    SharedPrefHelper.getInstance(getApplicationContext()).setString(AppLevelConstants.FCM_TOKEN, token);
                });
            }
        });

      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setupBaseClient();
            }
        }, 2000);
*/
    }

    private void connectionWarmup() {
        if (BuildConfig.FLAVOR.equalsIgnoreCase("prod")) {
            PKHttpClientManager.warmUp(
                    "https://linear01-playback.sooka.my/",
                    "https://linear02-playback.sooka.my/",
                    "https://vod-playback.sooka.my/favicon.ico",
                    "https://rest-as.ott.kaltura.com/crossdomain.xml",
                    "https://cdnapisec.kaltura.com/favicon.ico"

            );
        }
       /* PKHttpClientManager.setHttpProvider("okhttp");
        PKHttpClientManager.warmUp(
                "https://restv4-as.ott.kaltura.com/crossdomain.xml",
                "https://rest-as.ott.kaltura.com/crossdomain.xml",
                "https://cdnapisec.kaltura.com/favicon.ico",
                "https://livelinearsddash2.akamaized.net",
                "https://cfvod.kaltura.com/favicon.ico"
        );*/
    }


}
