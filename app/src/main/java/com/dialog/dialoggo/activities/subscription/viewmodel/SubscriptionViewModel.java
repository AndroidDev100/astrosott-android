package com.dialog.dialoggo.activities.subscription.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.repositories.subscription.SubscriptionRepository;
import com.dialog.dialoggo.utils.helpers.AssetContent;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.utils.response.base.Response;

import java.util.List;
import java.util.Map;

public class SubscriptionViewModel extends AndroidViewModel {
    public SubscriptionViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<com.kaltura.client.types.Subscription>> getSubscriptionPackageList(String assetId) {
        return  SubscriptionRepository.getInstance().getSubscriptionPackageList(getApplication().getApplicationContext(), assetId);
    }

    public LiveData<List<AssetCommonBean>> getAllChannelList(String assetId,int counter) {
        return  SubscriptionRepository.getInstance().getAllChannelList(assetId,counter,getApplication().getApplicationContext());
    }

    public LiveData<String> getGenreLivedata(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getGenredata(map);
    }

    public LiveData<List<Asset>> getAssetList(String subscriptionOffer) {
        return SubscriptionRepository.getInstance().getAssetList(subscriptionOffer,getApplication().getApplicationContext());
    }
}
