package com.dialog.dialoggo.activities.moreListing.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.repositories.moreListing.ContinueWatchlistingRepository;

public class ContinueWatchingViewModel extends AndroidViewModel {
    public ContinueWatchingViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<AssetCommonBean> getContinueWatching() {
        return ContinueWatchlistingRepository.getInstance().callContinueWatching(getApplication());
    }
}
