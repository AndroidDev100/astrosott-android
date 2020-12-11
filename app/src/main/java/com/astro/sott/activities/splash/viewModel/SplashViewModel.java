package com.astro.sott.activities.splash.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.astro.sott.modelClasses.appVersion.AppVersionStatus;
import com.astro.sott.activities.splash.ui.SplashActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.repositories.splash.SplashRepository;

import java.util.List;

public class SplashViewModel extends AndroidViewModel {

    public SplashViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<List<AppVersionStatus>> getVersion(Context context) {
        return new SplashRepository().checkVersion(context);
    }
    public LiveData<Boolean> pushToken(Context context,String fcmRefreshToken){
        return new SplashRepository().pushToken(context,fcmRefreshToken);
    }

    public LiveData<String> DMSCall(Context context) {
        return new SplashRepository().launchHomeScreen(context);
    }
    public LiveData<RailCommonData> getSpecificAsset(Context context,String assetId){
        return new SplashRepository().getSpecificAsset(context,assetId);
    }
    public LiveData<RailCommonData> getLiveSpecificAsset(Context context,String assetId){
        return new SplashRepository().getLiveSpecificAsset(context,assetId);
    }

    public LiveData<RailCommonData> getProgramAsset(SplashActivity context, String assetId){
        return new SplashRepository().getProgramAsset(context,assetId);
    }
}
