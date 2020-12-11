package com.astro.sott.activities.moreListing.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.astro.sott.repositories.moreListing.ContinueWatchlistingRepository;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;

public class ContinueWatchingViewModel extends AndroidViewModel {
    public ContinueWatchingViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<AssetCommonBean> getContinueWatching() {
        return ContinueWatchlistingRepository.getInstance().callContinueWatching(getApplication());
    }
}
