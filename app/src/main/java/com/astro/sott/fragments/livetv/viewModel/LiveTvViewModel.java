package com.astro.sott.fragments.livetv.viewModel;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.astro.sott.baseModel.ChannelLayer;
import com.astro.sott.baseModel.HomeBaseViewModel;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.repositories.homeTab.HomeFragmentRepository;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.repositories.liveTvTab.LiveTabRepository;

import java.util.List;

public class LiveTvViewModel extends HomeBaseViewModel {
    public LiveTvViewModel(@NonNull Application application) {
        super(application);
    }

    /*@Override
    public LiveData<List<AssetCommonBean>> getListLiveData(long channelID, List<VIUChannel> list, int counter, int swipeToRefresh) {
        return HomeFragmentRepository.getInstance().loadData(getApplication().getApplicationContext(), channelID, list, counter, swipeToRefresh,false);
    }

    @Override
    public LiveData<List<AssetCommonBean>> getListLiveData(long channelID, List<VIUChannel> list, int counter, int swipeToRefresh, boolean isCrousalInjected) {
        return HomeFragmentRepository.getInstance().loadData(getApplication().getApplicationContext(), channelID, list, counter, swipeToRefresh,isCrousalInjected);
    }*/

    public LiveData<List<AssetCommonBean>> getListLiveData(long channelID, List<VIUChannel> list, int counter, int swipeToRefresh, List<AssetCommonBean> loadedList) {
        return HomeFragmentRepository.getInstance().loadData(getApplication().getApplicationContext(), channelID, list, counter, swipeToRefresh, loadedList,1);
    }

    @Override
    public LiveData<AssetCommonBean> getChannelList() {
        return ChannelLayer.getInstance().getChannelList(getApplication().getApplicationContext(), AppLevelConstants.TAB_LIVE);
    }

    @Override
    public void resetObject() {
        LiveTabRepository.resetObject();
    }
}
