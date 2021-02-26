package com.astro.sott.repositories.webSeriesDescription;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.callBacks.kalturaCallBacks.GetSeriesCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.kaltura.client.types.Asset;

import java.util.List;

public class SeriesDataLayer {

    public static LiveData<Asset> getSeries(Context context, int mediaType, String externalId) {
        MutableLiveData<Asset> assetMutableLiveData = new MutableLiveData<>();
        new KsServices(context).callSeriesData(mediaType, externalId, new GetSeriesCallBack() {
            @Override
            public void onSuccess(List<Asset> asset) {
                assetMutableLiveData.postValue(asset.get(0));
            }

            @Override
            public void onFailure() {
                assetMutableLiveData.postValue(null);
            }
        });
        return assetMutableLiveData;
    }
}
