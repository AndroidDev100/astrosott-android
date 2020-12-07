package com.dialog.dialoggo.fragments.livetv.viewModel;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.baseModel.ChannelLayer;
import com.dialog.dialoggo.baseModel.HomeBaseViewModel;
import com.dialog.dialoggo.beanModel.VIUChannel;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.repositories.homeTab.HomeFragmentRepository;
import com.dialog.dialoggo.repositories.liveTvTab.LiveTabRepository;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;

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
