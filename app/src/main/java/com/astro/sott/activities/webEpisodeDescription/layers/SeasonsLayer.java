package com.astro.sott.activities.webEpisodeDescription.layers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;

import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeasonsLayer {

    private static SeasonsLayer seasonsLayer;
    private String seriesId;
    private int assetType;

    public static SeasonsLayer getInstance() {
        if (seasonsLayer == null) {
            seasonsLayer = new SeasonsLayer();
        }

        return seasonsLayer;
    }

    public LiveData<List<Integer>> loadData(final Context context,
                                            final int assetId,
                                            int counter,
                                            final int assetTyp,
                                            Map<String, Value> map,
                                            final int layoutType,
                                            final String seriesId) {

        assetType = assetTyp;
        final MutableLiveData<List<Integer>> connection = new MutableLiveData<>();
        ArrayList<Integer> emptyList = new ArrayList<>();
        final KsServices ksServices = new KsServices(context);
        if (!seriesId.equals("")) {
            AppCommonMethods.checkDMS(context, status -> {
                if (status) {
                    ksServices.callSeasons(0, seriesId, assetType, (status1, result) -> {
                        if (status1) {

                            connection.postValue(AssetContent.getSeasonNumber(result));
                        } else {
                            connection.postValue(null);

                        }
                    });
                }
            });
        } else {
            connection.postValue(emptyList);

        }
        return connection;
    }


}

