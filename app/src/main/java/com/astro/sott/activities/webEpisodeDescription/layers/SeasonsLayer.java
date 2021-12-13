package com.astro.sott.activities.webEpisodeDescription.layers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.Value;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeasonsLayer {

    private static SeasonsLayer seasonsLayer;
    private String seriesId;
    private int assetType;
    private List<Response<ListResponse<Asset>>> responseList;
    private AssetCommonBean assetCommonBean;
    private List<AssetCommonBean> assetCommonList;

    public static SeasonsLayer getInstance() {
        if (seasonsLayer == null) {
            seasonsLayer = new SeasonsLayer();
        }

        return seasonsLayer;
    }

    public LiveData<List<Asset>> loadData(final Context context,
                                            final int assetId,
                                            int counter,
                                            final int assetTyp,
                                            Map<String, Value> map,
                                            final int layoutType,
                                            final String seriesId) {

        assetType = assetTyp;
        final MutableLiveData<List<Asset>> connection = new MutableLiveData<>();
       // Response<ListResponse<Asset>> emptyList = new ArrayList<>();
        final KsServices ksServices = new KsServices(context);
        if (!seriesId.equals("")) {
            AppCommonMethods.checkDMS(context, status -> {
                if (status) {
                    ksServices.callSeasons(0, seriesId, assetType, (status1, result) -> {
                        if (status1) {
                           // TabsData.getInstance().setSeasonData(result.results.getObjects());
//                            connection.postValue(AssetContent.getSeasonNumber(result));
                            connection.postValue(result.results.getObjects());
                        } else {
                            connection.postValue(null);

                        }
                    });
                }
            });
        } else {
           // connection.postValue(emptyList);

        }
        return connection;
    }


    public LiveData<List<Asset>> loadData1(final Context context,
                                          final int assetId,
                                          int counter,
                                          final int assetTyp,
                                          Map<String, Value> map,
                                          final int layoutType,
                                          final String seriesId) {

        assetType = assetTyp;
        final MutableLiveData<List<Asset>> connection = new MutableLiveData<>();
        // Response<ListResponse<Asset>> emptyList = new ArrayList<>();
        final KsServices ksServices = new KsServices(context);
        if (!seriesId.equals("")) {
            AppCommonMethods.checkDMS(context, status -> {
                if (status) {
                    ksServices.callSeasons1(0, seriesId, assetType, (status1, result) -> {
                        if (status1) {
                            // TabsData.getInstance().setSeasonData(result.results.getObjects());
//                            connection.postValue(AssetContent.getSeasonNumber(result));
                            connection.postValue(result.results.getObjects());
                        } else {
                            connection.postValue(null);

                        }
                    });
                }
            });
        } else {
            // connection.postValue(emptyList);

        }
        return connection;
    }



}

