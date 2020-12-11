package com.astro.sott.baseModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;

public class CategoryRailLayer {

    private List<AssetCommonBean> assetCommonList;
    private static CategoryRailLayer categoryRailLayer;
    private int apiCount = 0;

    public static CategoryRailLayer getInstance() {
        if (categoryRailLayer == null) {
            categoryRailLayer = new CategoryRailLayer();
        }
        return categoryRailLayer;
    }

    public static void resetObject() {
        categoryRailLayer = null;
    }

    public LiveData<List<AssetCommonBean>> loadData(Context context, long channelID, List<VIUChannel> list, int counter, int swipeRefresh) {
        if (swipeRefresh == 2) {
            apiCount = 0;
        }
        int loopCount = 1;
        final KsServices ksServices = new KsServices(context);
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {
                ksServices.callChannelRail(0, channelID, list, counter, (status1, listResponseResponse, channelList) -> {
                    if (status1) {
                        apiCount++;
                        callDynamicData(context, listResponseResponse, channelList, counter);
                        connection.postValue(assetCommonList);
                    } else {
                        errorHandling();
                        connection.postValue(assetCommonList);
                    }

                });
            }


        });
        return connection;
    }

    private void errorHandling() {
        assetCommonList = new ArrayList<>();
        AssetCommonBean assetCommonBean = new AssetCommonBean();
        assetCommonBean.setStatus(false);
        assetCommonList.add(assetCommonBean);

    }

    private void callDynamicData(Context context, final List<Response<ListResponse<Asset>>> list, final List<VIUChannel> channelList, int counter) {
        assetCommonList = new ArrayList<>();
        if (channelList != null && channelList.size() > 0)
            new CategoryRails().setDescriptionRails(context, list, channelList, assetCommonList, counter);

    }


}
