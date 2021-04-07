package com.astro.sott.repositories.trailerFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;

import com.astro.sott.callBacks.kalturaCallBacks.GetSeriesCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.callBacks.kalturaCallBacks.TrailerAssetCallBack;
import com.kaltura.client.types.Asset;

import java.util.List;

public class TrailerFragmentRepository {
    private static TrailerFragmentRepository trailerFragmentRepository;


    public static TrailerFragmentRepository getInstance() {
        if (trailerFragmentRepository == null) {
            trailerFragmentRepository = new TrailerFragmentRepository();
        }
        return trailerFragmentRepository;
    }

    public LiveData<List<Asset>> getTrailerAsset(Context context, String refId, int assetType) {
        MutableLiveData<List<Asset>> trailerAsset = new MutableLiveData<>();
        new KsServices(context).getTrailorAsset(refId, assetType, new TrailerAssetCallBack() {
            @Override
            public void getTrailorAsset(boolean status, List<Asset> assetList) {
                if (status) {
                    trailerAsset.postValue(assetList);
                } else {
                    trailerAsset.postValue(null);
                }
            }
        });
        return trailerAsset;
    }

    public LiveData<List<Asset>> getShowFromBoxSet(Context context, String refId, int assetType) {
        MutableLiveData<List<Asset>> trailerAsset = new MutableLiveData<>();
        new KsServices(context).getBoxSetShows(refId, assetType, new TrailerAssetCallBack() {
            @Override
            public void getTrailorAsset(boolean status, List<Asset> assetList) {
                if (status) {
                    trailerAsset.postValue(assetList);
                } else {
                    trailerAsset.postValue(null);
                }
            }
        });
        return trailerAsset;
    }


    public LiveData<List<Asset>> getHighlightAsset(Context context, String refId, int assetType) {
        MutableLiveData<List<Asset>> trailerAsset = new MutableLiveData<>();
        new KsServices(context).getHighLightAsset(refId, assetType, new TrailerAssetCallBack() {
            @Override
            public void getTrailorAsset(boolean status, List<Asset> assetList) {
                if (status) {
                    trailerAsset.postValue(assetList);
                } else {
                    trailerAsset.postValue(null);
                }
            }
        });
        return trailerAsset;
    }

}
