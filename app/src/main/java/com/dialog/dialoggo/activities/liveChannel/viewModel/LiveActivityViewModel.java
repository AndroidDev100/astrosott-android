package com.dialog.dialoggo.activities.liveChannel.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.repositories.splash.SplashRepository;

public class LiveActivityViewModel extends AndroidViewModel {
    public LiveActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<RailCommonData> getSpecificAsset(String assetId) {
        return new SplashRepository().getSpecificAsset(getApplication().getApplicationContext(), assetId);
    }
}
