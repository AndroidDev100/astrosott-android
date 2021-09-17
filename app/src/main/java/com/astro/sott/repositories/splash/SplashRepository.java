package com.astro.sott.repositories.splash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.util.Log;

import com.astro.sott.activities.splash.ui.SplashActivity;
import com.astro.sott.callBacks.commonCallBacks.VersionValidator;
import com.astro.sott.callBacks.kalturaCallBacks.KsAnonymousLoginCallBack;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.modelClasses.appVersion.AppVersionStatus;
import com.astro.sott.networking.ForceUpdateNetworkCall;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.SharedPrefHelper;
import com.astro.sott.BuildConfig;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.UserPrefrencesCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.PrintLogging;
import com.clevertap.android.sdk.Logger;
import com.kaltura.client.types.LoginSession;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SplashRepository implements AlertDialogSingleButtonFragment.AlertDialogListener {
    private static SplashRepository splashRepository;
    private List<AppVersionStatus> list;
    private String flavor;

    public static SplashRepository getInstance() {
        if (splashRepository == null) {
            splashRepository = new SplashRepository();
        }
        return splashRepository;
    }

    public LiveData<String> launchHomeScreen(final Context context) {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        boolean _date = verifyDmsDate(SharedPrefHelper.getInstance(context).getString("DMS_Date", "mDate"));
        Log.e("oncreate: ", "in7" + _date);
        if (_date) {
            ksServices.hitApiDMS(status -> {
                if (status) {
                    callAnonymousLogin(context, mutableLiveData);
                } else {
                    mutableLiveData.postValue(null);
                }
            });
        } else {
            callAnonymousLogin(context, mutableLiveData);
        }
        return mutableLiveData;
    }

    private void getCategories(Context context, MutableLiveData<String> stringMutableLiveData) {
        final KsServices ksServices = new KsServices(context);
        stringMutableLiveData.postValue(AppLevelConstants.TRUE);
       /* ksServices.getSubCategories(context, new SubCategoryCallBack() {
            @Override
            public void subCategorySuccess() {
                stringMutableLiveData.postValue(AppLevelConstants.TRUE);
            }
            @Override
            public void subCategoryFailure() {
              stringMutableLiveData.postValue(null);
            }
        });*/

    }

    public LiveData<Boolean> pushToken(Context context, String fcmRefreshToken) {

        final MutableLiveData<Boolean> mutableLiveData = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        ksServices.notificationPushToken(fcmRefreshToken, (status, result) -> {
            if (status) {
                mutableLiveData.postValue(result.results);
            } else {
                mutableLiveData.postValue(false);
            }
        });
        return mutableLiveData;
    }

    private void callAnonymousLogin(final Context context, final MutableLiveData<String> stringMutableLiveData) {
        final KsServices ksServices = new KsServices(context);
        Log.e("oncreate: ", "inann");
        ksServices.callanonymousLogin(SharedPrefHelper.getInstance(context), new KsAnonymousLoginCallBack() {
            @Override
            public void success(boolean sucess, com.kaltura.client.utils.response.base.Response<LoginSession> result) {
                Log.e("oncreate: ", "inannsuc");
                getCategories(context, stringMutableLiveData);
            }

            @Override
            public void failure(boolean failure, com.kaltura.client.utils.response.base.Response<LoginSession> result) {
                Log.e("oncreate: ", "inannfail");
                stringMutableLiveData.postValue(null);
            }
        });
    }

    public LiveData<RailCommonData> getSpecificAsset(Context context, String assetId) {
        final MutableLiveData<RailCommonData> mutableLiveData = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        ksServices.callSpecificAsset(assetId, (status, asset) -> {
            RailCommonData railCommonData = new RailCommonData();
            if (status) {
                railCommonData.setStatus(true);
                railCommonData.setObject(asset);
                mutableLiveData.postValue(railCommonData);
            } else {
                railCommonData.setStatus(false);
                mutableLiveData.postValue(railCommonData);
            }
        });
        return mutableLiveData;
    }

    private boolean verifyDmsDate(String storedDate) {
        boolean verifyDms;
        String temp ="";
        if (storedDate == null || storedDate.equalsIgnoreCase(AppLevelConstants.MDATE)) {
            return true;
        }
     try {
         String currentDate = getDateTimeStamp(System.currentTimeMillis());
         temp = getDateTimeStamp(Long.parseLong(storedDate));
         verifyDms = !currentDate.equalsIgnoreCase(temp);

     }catch (NumberFormatException e){

     }
        return true;


    }

    private String getDateTimeStamp(Long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        return formatter.format(timeStamp);
    }


    public LiveData<List<AppVersionStatus>> checkVersion(Context applicationContext) {
        final MutableLiveData<List<AppVersionStatus>> mutableLiveData = new MutableLiveData<>();
        new ForceUpdateNetworkCall(applicationContext).checkCurrentVersion(AppCommonMethods.getVersion(applicationContext), new VersionValidator() {
            @Override
            public void version(boolean status, int currentVersion, int playStoreVersion) {

//                PrintLogging.printLog(this.getClass(), "", currentVersion + "------" + playStoreVersion);
//                if (currentVersion == playStoreVersion) {
//                    createList(true, currentVersion, playStoreVersion);
//                    mutableLiveData.postValue(list);
//                } else {
//                    createList(false, currentVersion, playStoreVersion);
//                    mutableLiveData.postValue(list);
//                }

                PrintLogging.printLog("", "versionV>>" + status + " " + currentVersion + " " + playStoreVersion);
                if (status) {
                    createList(true, currentVersion, playStoreVersion);
                    mutableLiveData.postValue(list);
                } else {
                    if (currentVersion < playStoreVersion) {
                        createList(false, currentVersion, playStoreVersion);
                        mutableLiveData.postValue(list);
                    } else {
                        createList(true, currentVersion, playStoreVersion);
                        mutableLiveData.postValue(list);
                    }
                }
            }
        });

        return mutableLiveData;
    }


    private void createList(boolean status, int currentVersion, int playStoreVersion) {
        list = new ArrayList<>();
        getFlavors();
        AppVersionStatus versionStatus = new AppVersionStatus();
        versionStatus.setStatus(status);
        versionStatus.setCurrentVersion(currentVersion);
        versionStatus.setPlayStoreVersion(playStoreVersion);
        versionStatus.setFlavor(flavor);
        list.add(versionStatus);
    }

    private void getFlavors() {
        flavor = BuildConfig.FLAVOR;
    }

    public LiveData<RailCommonData> getLiveSpecificAsset(Context context, String assetId) {
        final MutableLiveData<RailCommonData> mutableLiveData = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        ksServices.DeeplinkingLivecallSpecificAsset(assetId, (status, asset) -> {
            RailCommonData railCommonData = new RailCommonData();
            if (status) {
                railCommonData.setStatus(true);
                railCommonData.setObject(asset);
                mutableLiveData.postValue(railCommonData);
            } else {
                railCommonData.setStatus(false);
                mutableLiveData.postValue(railCommonData);
            }

        });
        return mutableLiveData;
    }

    public LiveData<String> checkUserPreferences(Context ctx) {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

        KsServices ksServices = new KsServices(ctx);
        ksServices.checkUserPreferences(ctx, new UserPrefrencesCallBack() {
            @Override
            public void response(String value) {
                mutableLiveData.postValue(value);
            }

            @Override
            public void failure() {
                mutableLiveData.postValue(null);
            }
        });
        return mutableLiveData;
    }

    @Override
    public void onFinishDialog() {

    }

    public LiveData<RailCommonData> getProgramAsset(SplashActivity context, String assetId) {
        final MutableLiveData<RailCommonData> mutableLiveData = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        ksServices.callProgramAsset(assetId, (status, asset) -> {
            RailCommonData railCommonData = new RailCommonData();
            if (status) {
                railCommonData.setStatus(true);
                railCommonData.setObject(asset);
                mutableLiveData.postValue(railCommonData);
            } else {
                railCommonData.setStatus(false);
                mutableLiveData.postValue(railCommonData);
            }

        });
        return mutableLiveData;
    }
}
