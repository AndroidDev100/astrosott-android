package com.astro.sott.repositories.trailerFragment;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.callBacks.kalturaCallBacks.GetSeriesCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.kaltura.client.types.Asset;

import java.util.List;

public class TrailerHighlightsDataLayer {


    public static LiveData<Asset> geAssetFromTrailer(Context context, String parentRefId) {
        MutableLiveData<Asset> movieAsset = new MutableLiveData<>();
        new KsServices(context).getAssetFromTrailer(parentRefId, new GetSeriesCallBack() {
            @Override
            public void onSuccess(List<Asset> asset) {
                if (asset.get(0) != null) {
                    movieAsset.postValue(asset.get(0));
                } else {
                    movieAsset.postValue(null);
                }
            }

            @Override
            public void onFailure() {
                movieAsset.postValue(null);

            }
        });
        return movieAsset;

    }
}
