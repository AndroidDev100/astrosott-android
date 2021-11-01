
package com.astro.sott.activities.splash.ui;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.astro.sott.activities.liveChannel.ui.LiveChannel;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.activities.splash.viewModel.SplashViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.modelClasses.appVersion.AppVersionStatus;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.networking.refreshToken.EvergentRefreshToken;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.usermanagment.EvergentBaseClient.EvergentBaseClient;
import com.astro.sott.usermanagment.EvergentBaseClient.EvergentBaseConfiguration;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.AccountServiceMessageItem;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.thirdParty.conViva.ConvivaManager;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.SharedPrefHelper;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.liveChannel.liveChannelManager.LiveChannelManager;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.ActivitySplashBinding;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.DialogHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.UDID;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;
import com.enveu.BaseClient.BaseClient;
import com.enveu.BaseClient.BaseConfiguration;
import com.enveu.BaseClient.BaseDeviceType;
import com.enveu.BaseClient.BaseGateway;
import com.enveu.BaseClient.BasePlatform;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.firebase.crashlytics.internal.common.CommonUtils;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaltura.client.types.Asset;
import com.kaltura.playkit.PKDrmParams;
import com.kaltura.playkit.player.MediaSupport;
import com.kaltura.playkit.player.PKDeviceCapabilitiesInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class SplashActivity extends BaseBindingActivity<ActivitySplashBinding> implements AlertDialogSingleButtonFragment.AlertDialogListener, DisplayManager.DisplayListener {

    private static final String TAG = "SplashActivity";
    String screenName = "";
    String assetId = "";
    private String dms_response = "";
    private SplashViewModel myViewModel;
    private String token;
    private String screen_name = "";
    private Long Id;
    private String name = "";
    private Long assetIdFire;
    private String via = "";
    private String description = "";
    private RailCommonData railCommonData;
    private boolean isFirstTimeUser;
    private boolean isDmsFailed;
    private boolean isDmsApiHit = false;
    private int counterDmsCall = 0;
    private Long value;
    private String programScreenValue = null;
    private JSONObject branchObject;
    //  Branch.BranchReferralInitListener branchReferralInitListener;


    @Override
    public ActivitySplashBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {

        return ActivitySplashBinding.inflate(inflater);
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            printHashKey();
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }

    }

    private String keyHash = "";

    private void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.astro.sott",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                PrintLogging.printLog("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            PrintLogging.printLog("Exception", "" + e);
        } catch (NoSuchAlgorithmException e) {
            PrintLogging.printLog("Exception", "" + e);
        }
    }


    private void connectionValidation(Boolean aBoolean) {
        /*BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        String deviceName = Settings.Global.getString(getContentResolver(), Settings.Global.DEVICE_NAME);

        Log.e(TAG, "oncreate: " + "indevice_name3" + deviceName);*/
        if (aBoolean) {
            if (!CommonUtils.isRooted(this)) {
                Log.e(TAG, "oncreate: " + "in4");
                isFirstTimeUser = SharedPrefHelper.getInstance(getApplication()).getBoolean("isFirstTime", false);
                if (!isFirstTimeUser) {
                    SharedPrefHelper.getInstance(getApplication()).setString("DMS_Date", "mDate");
                    SharedPrefHelper.getInstance(getApplication()).setBoolean("isFirstTime", true);
                }

                updateLanguage();
                initDrm();
                DMSCall();
                ConvivaManager.initConvivaAnalytics(this);
            } else {
                showFailureDialog(getString(R.string.rooted_failure_msg));
            }

            // versionStatus();
        } else {
            setConnectionLayout();
        }
    }

    public static boolean isStoreVersion(Context context) {
        boolean result = false;

        try {
            String installer = context.getPackageManager()
                    .getInstallerPackageName(context.getPackageName());
            result = !TextUtils.isEmpty(installer) && installer.equalsIgnoreCase("com.android.vending");
        } catch (Throwable e) {
        }
        Toast.makeText(context, result + "", Toast.LENGTH_SHORT).show();
        return result;
    }

    private void updateLanguage() {
        String selectedLanguage = new KsPreferenceKey(this).getAppLangName();
        if (selectedLanguage.equalsIgnoreCase("ms")) {
            AppCommonMethods.updateLanguage("ms", this);
        } else {
            AppCommonMethods.updateLanguage("en", this);

        }

    }

    private void callViewModel() {
        Log.e(TAG, "oncreate: " + "in2");
        myViewModel = ViewModelProviders.of(this).get(SplashViewModel.class);
        connectionObserver();
    }

    private void versionStatus() {
        Log.e(TAG, "oncreate: " + "in2");
        myViewModel.getVersion(this).observe(this, new Observer<List<AppVersionStatus>>() {
            @Override
            public void onChanged(@Nullable List<AppVersionStatus> appVersionStatuses) {
                Log.e(TAG, "oncreate: " + "in3");
                if (appVersionStatuses != null && appVersionStatuses.get(0) != null && appVersionStatuses.get(0).getStatus()) {
                    pushToken();

                } else {
                    showUpdateDialog();
                }

            }
        });
        //   DMSCall();

    }

    private void parseNotificationData(String response) {
        try {


            JSONObject object = new JSONObject(response);
            if (object != null) {
                if (object.has("screenname")) {

                    screenName = object.getString("screenname");


                    if (screenName.equalsIgnoreCase("null") && via.equalsIgnoreCase(AppLevelConstants.FIREBASE_SCREEN)) {

                        callSpecficAssetApi(String.valueOf(Id), "");

                    } else {
                        if (screenName.equalsIgnoreCase("Program")) {

                            if (object.has("assetid")) {
                                assetId = object.getString("assetid");
                                PrintLogging.printLog("", "assetIdsFromNoti" + assetId);
                                if (screenName.equalsIgnoreCase("Program")) {
                                    myViewModel.getProgramAsset(SplashActivity.this, assetId).observe(this, asset -> {

                                        if (asset.getStatus()) {

                                            PrintLogging.printLog("", "assetIdsFromNoti Success" + assetId);
                                            PrintLogging.printLog("", "BranchHomeRedirection b");
                                            checkCurrentProgram(asset.getObject());
                                        } else {
                                            PrintLogging.printLog("", "assetIdsFromNoti else" + assetId);
                                            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                                        }
                                    });
                                } else {
                                    new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                                }

                            } else {
                                new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                            }
                        } else {
                            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                        }
                    }
                }
            }

        } catch (Exception e) {

        }
    }

    private void callSpecficAssetApi(String value, String subMediaType) {

        myViewModel.getSpecificAsset(SplashActivity.this, value).observe((LifecycleOwner) SplashActivity.this, asset -> {

            if (asset != null && asset.getStatus()) {

                PrintLogging.printLog("MediaTypeIs", "", "MediaTypeIs--" + asset.getObject().getType());
                redirectionOnMediaType(asset, asset.getObject().getType().toString(), subMediaType);
            } else {
                new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
            }
        });
    }

    private void checkCurrentProgram(final Asset itemValue) {

        // handleProgressDialog();
        PrintLogging.printLog("", "BranchHomeRedirection c");
        new LiveChannelManager().getLiveProgram(SplashActivity.this, itemValue, asset -> {
            //   handleProgressDialog();
            PrintLogging.printLog("", "BranchHomeRedirection d");
            if (asset != null) {
                if (asset.getStatus()) {
                    if (asset.getLivePrograme()) {
                        PrintLogging.printLog("", "BranchHomeRedirection e");
                        PrintLogging.printLog("", "Live Program" + asset.getCurrentProgram().getName());
                        getProgramRailCommonData(itemValue, "liveChannelCall-->>" + asset.getStatus());
                        new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                        // new ActivityLauncher(SplashActivity.this).liveChannelActivity(SplashActivity.this, LiveChannel.class, railCommonData);
                        new ActivityLauncher(SplashActivity.this).liveChannelActivity(SplashActivity.this, LiveChannel.class, railCommonData);
                    } else {

                        PrintLogging.printLog("", "BranchHomeRedirection f");
                        getProgramRailCommonData(asset.getCurrentProgram(), "liveChannelCall-->>" + asset.getStatus() + "--" + asset.getProgramTime());
                        if (asset.getProgramTime() == 1) {
                            getProgramRailCommonData(itemValue, "Program VideoItemClicked");
                            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                            //  new ActivityLauncher(SplashActivity.this).catchUpActivity(SplashActivity.this, CatchupActivity.class, railCommonData);
                        } else {
                            PrintLogging.printLog("", "forwardedEPG else" + itemValue.getName());
                            getProgramRailCommonData(itemValue, "asdas");
                            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                            //  new ActivityLauncher(SplashActivity.this).forwardeEPGActivity(SplashActivity.this, ForwardedEPGActivity.class, railCommonData);
                        }
                    }
                } else {
                    PrintLogging.printLog("", "Live Programelse" + asset.getCurrentProgram().getName());
                }
            } else {
                runOnUiThread(() -> ToastHandler.show("Asset Not Found", SplashActivity.this));
            }
        });

    }

    private void DMSCall() {
        Log.e(TAG, "oncreate: " + "in5");
        myViewModel.DMSCall(this).observe(this, s -> {
            dms_response = s;
            Log.e(TAG, "oncreate: " + "in6" + dms_response);
            if (dms_response != null && !TextUtils.isEmpty(dms_response)) {

                if (TextUtils.isEmpty(KsPreferenceKey.getInstance(this).getQualityName())) {
                    KsPreferenceKey.getInstance(this).setQualityName("Auto");
                    KsPreferenceKey.getInstance(this).setQualityPosition(0);
                }
                isDmsApiHit = true;
                boolean _date = verifyDmsDate(SharedPrefHelper.getInstance(SplashActivity.this).getString("DMS_Date", "mDate"));
                if (_date)
                    getActiveSubscription();
                versionStatus();

            } else {
                isDmsFailed = true;
                if (counterDmsCall > 0) {
                    DialogHelper.showAlertDialog(this, getString(R.string.something_went_wrong_try_again), getString(R.string.ok), this);
                } else {
                    counterDmsCall++;
                    connectionObserver();
                }

            }
        });
    }

    private boolean verifyDmsDate(String storedDate) {
        boolean verifyDms;
        if (storedDate == null || storedDate.equalsIgnoreCase(AppLevelConstants.MDATE)) {
            return true;
        }

        String currentDate = getDateTimeStamp(System.currentTimeMillis());
        String temp = getDateTimeStamp(Long.parseLong(storedDate));
        verifyDms = !currentDate.equalsIgnoreCase(temp);

        return verifyDms;
    }

    private String getDateTimeStamp(Long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return formatter.format(timeStamp);
    }

    private void initDrm() {
        MediaSupport.initializeDrm(this, new MediaSupport.DrmInitCallback() {
            @Override
            public void onDrmInitComplete(PKDeviceCapabilitiesInfo pkDeviceCapabilitiesInfo, Exception provisionError) {
                if (pkDeviceCapabilitiesInfo.isProvisionPerformed()) {
                    if (provisionError != null) {
                        PrintLogging.printLog(SplashActivity.class, "", "DRM Provisioning failed-->>" + provisionError);
                    } else {
                        PrintLogging.printLog(SplashActivity.class, "", "DRM Provisioning succeeded-->>");
                    }
                }

                if (pkDeviceCapabilitiesInfo.getSupportedDrmSchemes().contains(PKDrmParams.Scheme.WidevineCENC)) {
                    PrintLogging.printLog(SplashActivity.class, "", "Widevinesupported:-->>");
                } else {
                    PrintLogging.printLog(SplashActivity.class, "", "Widevinenotsupported-->>");
                }
            }
        });
    }

    private void pushToken() {
        setupBaseClient();
        token = SharedPrefHelper.getInstance(this).getString(AppLevelConstants.FCM_TOKEN, "");
        if (token == null || token.equals("")) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
                token = instanceIdResult.getToken();
                SharedPrefHelper.getInstance(getApplicationContext()).setString(AppLevelConstants.FCM_TOKEN, token);
                Log.e(TAG, "pushToken: " + token);
                setBranchInit();
              /*  myViewModel.pushToken(this, token).observe(this, aBoolean -> {

                    //setBranchInit();
                    getBinding().noConnectionLayout.setVisibility(View.GONE);

                    try {
                        String response = KsPreferenceKey.getInstance(SplashActivity.this).getNotificationResponse();
                        if (response.equalsIgnoreCase("")) {
                            PrintLogging.printLog("", "d a");
                            setBranchInit();

                            // checkUserPreferences();
                        } else {


                            KsPreferenceKey.getInstance(SplashActivity.this).setNotificationResponse("");
                            parseNotificationData(response);
                            PrintLogging.printLog("", "notificationRespose-->>" + response);
                        }

                    } catch (Exception e) {

                    }


                });*/
            })
                    .addOnFailureListener(e -> {
                        // DialogHelper.showAlertDialog(this, getString(R.string.something_went_wrong_try_again), getString(R.string.ok), this);
                        getBinding().noConnectionLayout.setVisibility(View.GONE);
                        setBranchInit();
/*                        try {
                            String response = KsPreferenceKey.getInstance(SplashActivity.this).getNotificationResponse();
                            if (response.equalsIgnoreCase("")) {
                                PrintLogging.printLog("", "d a");
                                setBranchInit();
                                // checkUserPreferences();
                            } else {


                                KsPreferenceKey.getInstance(SplashActivity.this).setNotificationResponse("");
                                parseNotificationData(response);
                                PrintLogging.printLog("", "notificationRespose-->>" + response);
                            }

                        } catch (Exception ex) {

                        }*/
                    });
        } else {
            setBranchInit();
            /*myViewModel.pushToken(this, token).observe(this, aBoolean -> {

                //setBranchInit();
                getBinding().noConnectionLayout.setVisibility(View.GONE);

                try {
                    String response = KsPreferenceKey.getInstance(SplashActivity.this).getNotificationResponse();
                    if (response.equalsIgnoreCase("")) {
                        PrintLogging.printLog("", "d a");

                        // checkUserPreferences();
                    } else {


                        KsPreferenceKey.getInstance(SplashActivity.this).setNotificationResponse("");
                        parseNotificationData(response);
                        PrintLogging.printLog("", "notificationRespose-->>" + response);
                    }

                } catch (Exception e) {

                }

            });*/
        }
    }

    private String displayName = "";

    private void getActiveSubscription() {

        myViewModel.getActiveSubscription(UserInfo.getInstance(this).getAccessToken(), "").observe(this, evergentCommonResponse -> {
            if (evergentCommonResponse.isStatus()) {
                if (evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage() != null) {
                    if (evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage() != null && evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage().size() > 0) {
                        for (AccountServiceMessageItem accountServiceMessageItem : evergentCommonResponse.getResponse().getGetActiveSubscriptionsResponseMessage().getAccountServiceMessage()) {
                            if (accountServiceMessageItem.getStatus().equalsIgnoreCase("ACTIVE") && !accountServiceMessageItem.isFreemium()) {
                                if (accountServiceMessageItem.getDisplayName() != null)
                                    displayName = accountServiceMessageItem.getDisplayName();
                            }
                        }
                        if (!displayName.equalsIgnoreCase("")) {
                            UserInfo.getInstance(this).setVip(true);
                        } else {
                            UserInfo.getInstance(this).setVip(false);
                        }
                    } else {
                        UserInfo.getInstance(this).setVip(false);
                    }
                } else {
                }
            } else {

            }
        });
    }

    private void setupBaseClient() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        String API_KEY = "";
        String DEVICE_TYPE = "";
        String OVP_API_KEY = "";
        String EXPERIENCE_MANAGER_URL = "";
        if (isTablet) {
            API_KEY = AppConstants.API_KEY_TAB;
            OVP_API_KEY = AppConstants.API_KEY_TAB;
            DEVICE_TYPE = BaseDeviceType.tablet.name();
        } else {
            API_KEY = AppConstants.API_KEY_MOB;
            OVP_API_KEY = AppConstants.API_KEY_MOB;
            DEVICE_TYPE = BaseDeviceType.mobile.name();
        }
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(this);


        if (responseDmsModel != null && responseDmsModel.getParams() != null && responseDmsModel.getParams().getApiProxyUrlExpManager() != null) {
            EXPERIENCE_MANAGER_URL = responseDmsModel.getParams().getApiProxyUrlExpManager();
            BaseClient client = new BaseClient(BaseGateway.ENVEU, EXPERIENCE_MANAGER_URL, AppConstants.SUBSCRIPTION_BASE_URL, OVP_API_KEY, API_KEY, DEVICE_TYPE, BasePlatform.android.name(), isTablet, UDID.getDeviceId(this, this.getContentResolver()));
            BaseConfiguration.Companion.getInstance().clientSetup(client);
        }

        if (responseDmsModel != null && responseDmsModel.getParams() != null && responseDmsModel.getParams().getApiProxyUrlEvergent() != null) {
            EvergentBaseClient evergentBaseClient = new EvergentBaseClient(responseDmsModel.getParams().getApiProxyUrlEvergent());
            EvergentBaseConfiguration.Companion.getInstance().clientSetup(evergentBaseClient);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        Branch.sessionBuilder(this).withCallback(branchReferralInitListener).withData(getIntent() != null ? getIntent().getData() : null).init();

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);

        processIntent(intent);
        //Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit();

    }

//

    private void processIntent(Intent intent) {

        if (intent != null) {
            try {
                via = intent.getStringExtra("via");

                if (via.equalsIgnoreCase("firebase_screen")) {

                    //  name = intent.getStringExtra(AppLevelConstants.Title);
                    // description = intent.getStringExtra(AppLevelConstants.DESCRIPTION);
                    Id = intent.getLongExtra(AppLevelConstants.ID, 0);
                    screen_name = intent.getStringExtra(AppLevelConstants.SCREEN_NAME);

                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty(AppLevelConstants.SCREEN_NAME, screen_name);
                    jsonObject.addProperty("assetid", Id);

                    KsPreferenceKey.getInstance(this).setNotificationResponse(jsonObject + "");


                } else {

                    name = intent.getStringExtra(AppLevelConstants.Title);
                    description = intent.getStringExtra(AppLevelConstants.DESCRIPTION);
                    Id = intent.getLongExtra(AppLevelConstants.ID, 0);
                    screen_name = intent.getStringExtra(AppLevelConstants.SCREEN_NAME);
                    Log.w("reminderDetails", screen_name);


                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty(AppLevelConstants.SCREEN_NAME, screen_name);
                    jsonObject.addProperty("assetid", Id);

                    if (screen_name != null) {

                        KsPreferenceKey.getInstance(this).setNotificationResponse(jsonObject + "");
                    }

                }


            } catch (Exception e) {

            }
        }
    }


    // Branch init
    private void setBranchInit() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
//            }
//        },6000);

        checkSocialRedirection();


//            branchReferralInitListener = new Branch.BranchReferralInitListener() {
//                @Override
//                public void onInitFinished(JSONObject referringParams, BranchError error) {
//                    // do stuff with deep link data (nav to page, display content, etc)
//
//                    if (error == null) {
//
//                        if (referringParams.has("assetId")) {
//                            Log.d("asasasasasasa", new Gson().toJson(referringParams));
//
//                            redirectionCondition(referringParams);
//                        } else {
//                            PrintLogging.printLog("", "c a");
//                            new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
//                        }
//                    } else {
//                        DialogHelper.showAlertDialog(getApplicationContext(), getString(R.string.something_went_wrong_try_again), getString(R.string.ok), SplashActivity.this);
//                    }
//
//
//                }
//            };
    }

    private void checkSocialRedirection() {
        try {
            if (getIntent().getData() != null && getIntent().getData().toString().contains("mediaType")) {
                Uri deepLink = Uri.parse(getIntent().getData().toString());

                if (deepLink.getQuery() != null && deepLink.getQuery().contains("id")) {
                    String mediatype = (String) deepLink.getQueryParameter("mediaType");
                    if (Integer.parseInt(mediatype) == MediaTypeConstant.getProgram(SplashActivity.this)) {
                        myViewModel.getLiveSpecificAsset(this, deepLink.getQueryParameter("id")).observe(this, railCommonData -> {
                            if (railCommonData != null && railCommonData.getStatus()) {
                                //liveManger(railCommonData);
                                new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                                new ActivityLauncher(SplashActivity.this).checkCurrentProgram(railCommonData.getObject());
                            } else {
                                new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
                                // DialogHelper.showAlertDialog(this, getString(R.string.asset_not_found), getString(R.string.ok), this);
                            }
                        });
                    } else {
                        callSpecficAssetApi(String.valueOf(deepLink.getQueryParameter("id")), deepLink.getQueryParameter("subMediaType"));
                    }
                }

            } else {
                FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(pendingDynamicLinkData -> {
                    Log.w("deepLink", "in" + pendingDynamicLinkData);
                    try {
                        if (pendingDynamicLinkData != null) {
                            Log.w("deepLink", "in2" + pendingDynamicLinkData.getLink());
                            Uri deepLink = pendingDynamicLinkData.getLink();
                            Log.w("deepLink", "in2" + pendingDynamicLinkData.getLink() + " " + deepLink.getQuery());
                            if (deepLink != null) {
                                if (deepLink.getQuery() != null && deepLink.getQuery().contains("link=")) {
                                    String arr[] = deepLink.getQuery().toString().split("link=");
                                    String url = arr[1];
                                    Log.w("deepLink", "first" + url);
                                    Uri newU = Uri.parse(url);
                                    Log.w("deepLink", "second" + newU.toString());
                                    Log.w("deepLink", "third" + newU.getQueryParameter("id"));
                                    Log.w("deepLink", "in2---" + newU.getQueryParameter("mediaType"));
                                    Log.w("deepLink", "in2---" + newU.getQueryParameter("subMediaType"));
                                    // Log.w("deepLink",deepLink.getQuery().getQueryParameter("id"));
                                    // Log.w("deepLink",deepLink.getQueryParameter("mediaType"));
                                    // Log.w("deepLink",deepLink.getQueryParameter("name"));
                                    if (newU.getQueryParameter("mediaType") != null) {
                                        if (Integer.parseInt(newU.getQueryParameter("mediaType")) == MediaTypeConstant.getProgram(SplashActivity.this)) {
                                            myViewModel.getLiveSpecificAsset(this, newU.getQueryParameter("id")).observe(this, railCommonData -> {
                                                if (railCommonData != null && railCommonData.getStatus()) {
                                                    //liveManger(railCommonData);
                                                    new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                                                    new ActivityLauncher(SplashActivity.this).checkCurrentProgram(railCommonData.getObject());

                                                } else {

                                                    new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);

                                                    // DialogHelper.showAlertDialog(this, getString(R.string.asset_not_found), getString(R.string.ok), this);
                                                }
                                            });
                                        } else {
                                            callSpecficAssetApi(String.valueOf(newU.getQueryParameter("id")), newU.getQueryParameter("subMediaType"));
                                        }
                                    }


                                } else {
                                    if (pendingDynamicLinkData.getLink() != null && pendingDynamicLinkData.getLink().getQueryParameter("id") != null) {
                                        if (pendingDynamicLinkData.getLink().getQueryParameter("mediaType") != null) {
                                            if (Integer.parseInt(pendingDynamicLinkData.getLink().getQueryParameter("mediaType")) == MediaTypeConstant.getProgram(SplashActivity.this)) {
                                                myViewModel.getLiveSpecificAsset(this, pendingDynamicLinkData.getLink().getQueryParameter("id")).observe(this, railCommonData -> {
                                                    if (railCommonData != null && railCommonData.getStatus()) {
                                                        //liveManger(railCommonData);
                                                        new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                                                        new ActivityLauncher(SplashActivity.this).checkCurrentProgram(railCommonData.getObject());
                                                    } else {
                                                        new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
                                                        // DialogHelper.showAlertDialog(this, getString(R.string.asset_not_found), getString(R.string.ok), this);
                                                    }
                                                });
                                            } else {
                                                callSpecficAssetApi(String.valueOf(pendingDynamicLinkData.getLink().getQueryParameter("id")), pendingDynamicLinkData.getLink().getQueryParameter("subMediaType"));
                                            }
                                        }
                                    } else {
                                        new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
                                    }
                                }

                            } else {
                                if (branchObject != null) {
                                    if (branchObject.has("assetId")) {
                                        redirectionCondition(branchObject);
                                    } else {
                                        new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
                                    }
                                } else {
                                    new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
                                }
                                Log.d("deepLink", "getDynamicLink: no link found");
                            }
                        } else {
                            if (branchObject != null) {
                                if (branchObject.has("assetId")) {
                                    redirectionCondition(branchObject);
                                } else {
                                    new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
                                }
                            } else {
                                if (Id != null) {
                                    if (Id != 0) {
                                        reminderRedirections(Id);
                                    } else {
                                        new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
                                    }
                                } else {
                                    new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
                                }
                                //new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
                            }
                        }
                    } catch (Exception e) {
                        new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
                    }

                });

            }
        } catch (Exception e) {
            new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
        }

    }

    private void reminderRedirections(Long id) {
        Log.w("reminderDetails", id + "");
        if (screen_name != null && screen_name.equalsIgnoreCase(AppLevelConstants.PROGRAM)) {
            myViewModel.getLiveSpecificAsset(this, String.valueOf(id)).observe(this, railCommonData -> {
                if (railCommonData != null && railCommonData.getStatus()) {
                    new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                    new ActivityLauncher(SplashActivity.this).checkCurrentProgram(railCommonData.getObject());

                } else {
                    new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
                }
            });

        } else {
            Log.w("reminderDetails", "else");
            callSpecficAssetApi(id + "", "");
        }
    }


//        Branch.getInstance().initSession((referringParams, error) -> {
//            if (error == null) {
//                if (referringParams.has("assetId")) {
//
//
//
//
//                    redirectionCondition(referringParams);
//                } else {
//                    PrintLogging.printLog("", "c a");
//                    new ActivityLauncher(SplashActivity.this).homeActivity(SplashActivity.this, HomeActivity.class);
//                }
//            } else {
//                DialogHelper.showAlertDialog(this, getString(R.string.something_went_wrong_try_again), getString(R.string.ok), this);
//            }
//        }, this.getIntent().getData(), this);


    //redirection of pages after deeplink
    private void redirectionCondition(JSONObject referringParams) {
        try {
            if (referringParams.has("assetId")) {
                String assetId = referringParams.getString("assetId");
                final String mediaType = referringParams.getString("mediaType");
                if (!mediaType.equals("")) {
                    if (Integer.parseInt(mediaType) == MediaTypeConstant.getProgram(SplashActivity.this)) {
                        myViewModel.getLiveSpecificAsset(this, assetId).observe(this, railCommonData -> {
                            if (railCommonData != null && railCommonData.getStatus()) {
                                liveManger(railCommonData);
                            } else {
                                DialogHelper.showAlertDialog(this, getString(R.string.asset_not_found), getString(R.string.ok), this);
                            }
                        });
                    } else {
                        myViewModel.getSpecificAsset(this, assetId).observe(this, asset -> {
                            if (asset != null && asset.getStatus()) {
                                redirectionOnMediaType(asset, mediaType, "");
                            } else {
                                DialogHelper.showAlertDialog(this, getString(R.string.asset_not_found), getString(R.string.ok), this);
                            }
                        });
                    }
                } else {
                    DialogHelper.showAlertDialog(this, getString(R.string.something_went_wrong_try_again), getString(R.string.ok), this);
                }
            } else {
                DialogHelper.showAlertDialog(this, getString(R.string.something_went_wrong_try_again), getString(R.string.ok), this);
            }
        } catch (Exception e) {
            DialogHelper.showAlertDialog(this, getString(R.string.something_went_wrong_try_again), getString(R.string.ok), this);
        }

    }

    private void liveManger(final RailCommonData railCommonData) {
        if (railCommonData != null) {
            new LiveChannelManager().getLiveProgram(SplashActivity.this, railCommonData.getObject(), commonResponse -> {
                Log.w("deepLink", "in2---" + commonResponse.getStatus());
                if (commonResponse.getStatus()) {
                    if (commonResponse.getLivePrograme()) {
                        getProgramRailCommonData(commonResponse.getCurrentProgram(), "liveChannelCall-->>" + commonResponse.getStatus());
                        new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                        new ActivityLauncher(SplashActivity.this).liveChannelActivity(SplashActivity.this, LiveChannel.class, railCommonData);
                    } else {
                        getProgramRailCommonData(commonResponse.getCurrentProgram(), "liveChannelCall-->>" + commonResponse.getStatus() + "--" + commonResponse.getProgramTime());
                        if (commonResponse.getProgramTime() == 1) {
                            getProgramRailCommonData(commonResponse.getCurrentProgram(), "Program VideoItemClicked");
                            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);

                            // new ActivityLauncher(SplashActivity.this).catchUpActivity(SplashActivity.this, CatchupActivity.class, railCommonData);
                        } else {
                            Log.w("deepLink", "in2---2" + commonResponse.getLivePrograme());
                            new ActivityLauncher(SplashActivity.this).liveChannelActivity(SplashActivity.this, LiveChannel.class, railCommonData);
                            //   new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                            //  new ActivityLauncher(SplashActivity.this).forwardeEPGActivity(SplashActivity.this, ForwardedEPGActivity.class, railCommonData);
                        }
                    }
                } else {
                    new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
                }
            });
        }

    }

    private void redirectionOnMediaType(RailCommonData asset, String mediaType, String subMediaType) {
        PrintLogging.printLog(this.getClass(), "", "mediaTypeDeepLink" + mediaType);
        if (Integer.parseInt(mediaType) == MediaTypeConstant.getMovie(SplashActivity.this)) {
            // finish();
            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
            new ActivityLauncher(SplashActivity.this).detailActivity(SplashActivity.this, MovieDescriptionActivity.class, asset, AppLevelConstants.Rail3);
        } else if (Integer.parseInt(mediaType) == MediaTypeConstant.getShortFilm(SplashActivity.this)) {
            //  finish();
            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
            new ActivityLauncher(SplashActivity.this).detailActivity(SplashActivity.this, MovieDescriptionActivity.class, asset, AppLevelConstants.Rail5);
        } else if (Integer.parseInt(mediaType) == MediaTypeConstant.getEpisode(SplashActivity.this)) {
            //finish();
            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
            new ActivityLauncher(SplashActivity.this).webDetailRedirection(asset, AppLevelConstants.Rail5);

            // new ActivityLauncher(SplashActivity.this).webEpisodeActivity(SplashActivity.this, WebEpisodeDescriptionActivity.class, asset, AppLevelConstants.Rail5);
        } else if (Integer.parseInt(mediaType) == MediaTypeConstant.getTrailer(SplashActivity.this)) {
            //  finish();
            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
            new ActivityLauncher(SplashActivity.this).detailActivity(SplashActivity.this, MovieDescriptionActivity.class, asset, AppLevelConstants.Rail5);
        } else if (Integer.parseInt(mediaType) == MediaTypeConstant.getSeries(SplashActivity.this)) {
            // finish();
            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
            new ActivityLauncher(SplashActivity.this).webSeriesActivity(SplashActivity.this, WebSeriesDescriptionActivity.class, asset, AppLevelConstants.Rail5);
        } else if (Integer.parseInt(mediaType) == MediaTypeConstant.getLinear(SplashActivity.this)) {
            //  finish();
            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
            new ActivityLauncher(SplashActivity.this).liveChannelActivity(SplashActivity.this, LiveChannel.class, asset);
        } else if (Integer.parseInt(mediaType) == MediaTypeConstant.getCollection(SplashActivity.this)) {
            //  finish();
            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
            new ActivityLauncher(SplashActivity.this).boxSetDetailActivity(SplashActivity.this, asset, AppLevelConstants.Rail3);
        } else {
            new ActivityLauncher(SplashActivity.this).homeScreen(SplashActivity.this, HomeActivity.class);
        }

    }

    private void getProgramRailCommonData(Asset currentProgram, String program_videoItemClicked) {
        railCommonData = new RailCommonData();
        railCommonData.setObject(currentProgram);
    }

    private void showAnimation() {
        Animation slideUP = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        //    getBinding().splashImage.startAnimation(slideUP);
        slideUP.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, R.style.AlertDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_custom_title, null);
        // TextView title = (TextView)view.findViewById(R.id.myTitle);
        // title.setText("Title I want to show");
        builder.setCustomTitle(view);


        // builder.setTitle(getApplicationContext().getResources().getString(R.string.new_version));
        if (SplashActivity.this != null) {
            builder.setMessage(getApplicationContext().getResources().getString(R.string.update_app_msg))
                    .setCancelable(false)
                    .setPositiveButton(getApplicationContext().getString(R.string.force_update), (dialog, id) -> {
                        if (BuildConfig.FLAVOR.equalsIgnoreCase("QA")) {
                            final String appPackageName = getPackageName();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }

                        } else if (isDmsFailed) {
                            isDmsFailed = false;
                            showFailureDialog(getString(R.string.something_went_wrong_try_again));
                        } else {
                            final String appPackageName = getPackageName();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }

                        }
                    });
            //  .setNegativeButton(getApplicationContext().getString(R.string.no), (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.setCanceledOnTouchOutside(false);
            alert.show();
            //  alert.setCancelable(false);
//            Button bn = alert.getButton(android.content.DialogInterface.BUTTON_NEGATIVE);
//            bn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            Button bp = alert.getButton(android.content.DialogInterface.BUTTON_POSITIVE);
            bp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.aqua_marine));

        }


    }

    private void showFailureDialog(String string) {
        DialogHelper.showAlertDialog(this, string, getString(R.string.ok), this);
    }

    private void setConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
//       myDevice.enable();
//       myDevice.startDiscovery();
//       Handler handler = new Handler();
//        Runnable r = new Runnable() {
//            public void run() {
//
//                devicename= BluetoothAdapter.getDefaultAdapter().getName();
//                Log.d("DEVICEusername",devicename+"");
//
//            }
//        };
//        handler.postDelayed(r, 1000);
//
//        String deviceModel = Build.PRODUCT;
//        Log.d("DEVICEProduct",deviceModel+"");
//        String deviceName1=  Settings.Global.getString(getContentResolver(), Settings.Global.DEVICE_NAME);
////                Settings.Secure.getString(getContentResolver(), "bluetooth_name");
//        Log.d("DEVICEname",deviceName1+"");


//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }


        FirebaseEventManager.getFirebaseInstance(this).trackScreenName("Splash Screen");

        Bundle bundle = getIntent().getExtras();


        if (bundle != null) {

            try {
                for (String key : bundle.keySet()) {

                    if (key.equalsIgnoreCase("type")) {
                        Object typeValue = bundle.get(key);
                        programScreenValue = String.valueOf(typeValue);
                        via = "firebase_screen";
                    } else if (key.equalsIgnoreCase("Id")) {
                        Object idValue = bundle.get(key);
                        value = Long.valueOf(String.valueOf(idValue));
                        via = "firebase_screen";
                    }

                }


                try {

                    if (via.equalsIgnoreCase("firebase_screen")) {
                        // name = bundle.getString(AppLevelConstants.Title);
                        //  description = bundle.getString(AppLevelConstants.DESCRIPTION);
                        Id = value;
                        screen_name = programScreenValue;
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty(AppLevelConstants.SCREEN_NAME, screen_name);
                        jsonObject.addProperty("assetid", Id);
                        if (KsPreferenceKey.getInstance(SplashActivity.this).getNotificationResponse().equalsIgnoreCase(""))
                            KsPreferenceKey.getInstance(this).setNotificationResponse(jsonObject + "");


                    }


                } catch (Exception e) {

                }


            } catch (Exception e) {

            }
        }
        Log.e(TAG, "oncreate: " + "in");
        AppCommonMethods.isAdsEnable = true;
        callViewModel();
        showAnimation();

        if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            installTls();
        }

        try {
            Window window = this.getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.grape_purple));
        } catch (Exception ignored) {

        }
        processIntent(getIntent());
    }

    private void installTls() {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Prompt the user to install/update/enable Google Play services.
            Toast.makeText(this, "Install or Update Google Play services", Toast.LENGTH_SHORT).show();
            GoogleApiAvailability.getInstance()
                    .showErrorNotification(this, e.getConnectionStatusCode());
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates a non-recoverable error: let the user know.
            Toast.makeText(this, "App may not work properly for your mobile", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFinishDialog() {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDmsApiHit)
            versionStatus();
    }

    @Override
    public void onDisplayAdded(int displayId) {
        Log.e("DISPLAY", "ADDED");
    }

    @Override
    public void onDisplayRemoved(int displayId) {
        Log.e("DISPLAY", "REMOVED");

    }

    @Override
    public void onDisplayChanged(int displayId) {
        Log.e("DISPLAY", "CHANGED");

    }
}
