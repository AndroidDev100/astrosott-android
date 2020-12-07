package com.dialog.dialoggo.baseModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.beanModel.VIUChannel;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;

import java.util.List;


public abstract class HomeBaseViewModel extends AndroidViewModel{
    protected HomeBaseViewModel(@NonNull Application application) {
        super(application);
    }


   // public abstract LiveData<List<AssetCommonBean>> getListLiveData(long channelID, List<VIUChannel> list, int counter, int swipeToRefresh);
   // public abstract LiveData<List<AssetCommonBean>> getListLiveData(long channelID, List<VIUChannel> list, int counter, int swipeToRefresh,boolean isCrousalInjected);
    public abstract LiveData<List<AssetCommonBean>> getListLiveData(long channelID, List<VIUChannel> list,int counter,int swipeToRefresh,List<AssetCommonBean> loadedList);
    public abstract LiveData<AssetCommonBean> getChannelList();

    public abstract void resetObject();

}
