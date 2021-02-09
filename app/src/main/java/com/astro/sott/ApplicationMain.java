package com.astro.sott;

import android.os.AsyncTask;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.SharedPrefHelper;
import com.astro.sott.R;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.UDID;
import com.enveu.BaseClient.BaseClient;
import com.enveu.BaseClient.BaseConfiguration;
import com.enveu.BaseClient.BaseDeviceType;
import com.enveu.BaseClient.BaseGateway;
import com.enveu.BaseClient.BasePlatform;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kaltura.playkit.player.PKHttpClientManager;

import java.util.Map;

import io.branch.referral.Branch;


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
        super.onCreate();

        mInstance = this;
//        ApplicationMain.context = getApplicationContext();
        MultiDex.install(this);
        // Branch logging for debugging
        //Branch.enableLogging();
        //Branch.getAutoInstance(this, true);

        // Branch object initialization
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                connectionWarmup();
                //   AdSettings.setIntegrationErrorMode(INTEGRATION_ERROR_CRASH_DEBUG_MODE);

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

        setupBaseClient();

    }

    private void connectionWarmup() {

        PKHttpClientManager.setHttpProvider("okhttp");
        PKHttpClientManager.warmUp(
                "https://restv4-as.ott.kaltura.com/crossdomain.xml",
                "https://rest-as.ott.kaltura.com/crossdomain.xml",
                "https://cdnapisec.kaltura.com/favicon.ico",
                "https://livelinearsddash2.akamaized.net",
                "https://cfvod.kaltura.com/favicon.ico"
        );
    }

    private void setupBaseClient() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        String API_KEY = "";
        String DEVICE_TYPE = "";
        String OVP_API_KEY = "";
        if (isTablet) {
            API_KEY = AppConstants.API_KEY_TAB;
            OVP_API_KEY = AppConstants.API_KEY_TAB;
            DEVICE_TYPE = BaseDeviceType.tablet.name();
        } else {
            API_KEY = AppConstants.API_KEY_MOB;
            OVP_API_KEY = AppConstants.API_KEY_MOB;
            DEVICE_TYPE = BaseDeviceType.mobile.name();
        }

        //   BaseClient client = new BaseClient(BaseGateway.ENVEU, AppConstants.BASE_URL, AppConstants.SUBSCRIPTION_BASE_URL,AppConstants.OVP_API_KEY,API_KEY, DEVICE_TYPE, BasePlatform.android.name(), isTablet, AppCommonMethods.getDeviceId(getContentResolver()));
        BaseClient client = new BaseClient(BaseGateway.ENVEU, AppConstants.BASE_URL, AppConstants.SUBSCRIPTION_BASE_URL, OVP_API_KEY, API_KEY, DEVICE_TYPE, BasePlatform.android.name(), isTablet, UDID.getDeviceId(this, this.getContentResolver()));

        BaseConfiguration.Companion.getInstance().clientSetup(client);
    }

}
