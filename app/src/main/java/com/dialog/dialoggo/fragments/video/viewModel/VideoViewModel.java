package com.dialog.dialoggo.fragments.video.viewModel;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.baseModel.ChannelLayer;
import com.dialog.dialoggo.baseModel.HomeBaseViewModel;
import com.dialog.dialoggo.beanModel.VIUChannel;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.repositories.homeTab.HomeFragmentRepository;
import com.dialog.dialoggo.repositories.videoTab.VideoFragmentRepository;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;

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
