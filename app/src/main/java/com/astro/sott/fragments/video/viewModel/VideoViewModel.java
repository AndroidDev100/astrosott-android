package com.astro.sott.fragments.video.viewModel;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.astro.sott.baseModel.ChannelLayer;
import com.astro.sott.baseModel.HomeBaseViewModel;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.repositories.homeTab.HomeFragmentRepository;
import com.astro.sott.utils.helpers.AppLevelConstants;

import java.util.List;

public class VideoViewModel extends HomeBaseViewModel {
    public VideoViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<List<AssetCommonBean>> getListLiveData(long channelID, List<VIUChannel> list, int counter, int swipeToRefresh, List<AssetCommonBean> loadedList) {
        return HomeFragmentRepository.getInstance().loadData(getApplication().getApplicationContext(), channelID, list, counter, swipeToRefresh, loadedList,1);
    }

    public LiveData<AssetCommonBean> getChannelList() {
        return ChannelLayer.getInstance().getChannelList(getApplication().getApplicationContext(), AppLevelConstants.TAB_HOME);
    }

    public void resetObject() {
        HomeFragmentRepository.resetObject();
    }

    public void resetFragment() {

    }
}
