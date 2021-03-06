package com.astro.sott.repositories.liveChannel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.kalturaCallBacks.GetSeriesCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.kaltura.client.types.Asset;

import java.util.List;

public class LinearProgramDataLayer {


    public static LiveData<Asset> getProgramFromLinear(Context context, String channelId) {
        MutableLiveData<Asset> assetMutableLiveData = new MutableLiveData<>();
        new KsServices(context).getProgramFromLinear(channelId, new GetSeriesCallBack() {
            @Override
            public void onSuccess(List<Asset> asset) {
                if (asset.get(0) != null) {
                    assetMutableLiveData.postValue(asset.get(0));
                } else {
                    assetMutableLiveData.postValue(null);

                }
            }

            @Override
            public void onFailure() {
                assetMutableLiveData.postValue(null);

            }
        });
        return assetMutableLiveData;
    }

    public static LiveData<RailCommonData> getLinearFromProgram(Context context, String linearId) {
        MutableLiveData<RailCommonData> railCommonDataMutableLiveData = new MutableLiveData<>();
        new KsServices(context).callSpecificAsset(linearId, (status, asset) -> {
            RailCommonData railCommonData = new RailCommonData();
            if (status) {
                railCommonData.setStatus(true);
                railCommonData.setObject(asset);
                railCommonDataMutableLiveData.postValue(railCommonData);
            } else {
                railCommonData.setStatus(false);
                railCommonDataMutableLiveData.postValue(railCommonData);
            }
        });
        return railCommonDataMutableLiveData;
    }

}
