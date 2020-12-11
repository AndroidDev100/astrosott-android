package com.astro.sott.repositories.liveTvTab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.baseModel.CategoryRails;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.utils.helpers.carousel.model.Slide;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;


public class LiveTabRepository {

    List<RailCommonData> channelList;
    List<Response<ListResponse<Asset>>> liveChannelList;
    List<Response<ListResponse<Asset>>> similarList;
    private List<AssetCommonBean> assetCommonList;
    private static LiveTabRepository liveTabRepository;

    public static LiveTabRepository getInstance() {
        liveTabRepository = new LiveTabRepository();
        return liveTabRepository;
    }

    public static void resetObject() {
        liveTabRepository = null;
    }

    private int loopCount = 1;
    private int apiCount = 0;

    public LiveData<List<AssetCommonBean>> loadData(Context context, long channelID, List<VIUChannel> list, int counter, int swipeRefresh) {
        apiCount = counter;
        loopCount = 1;
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

    private ArrayList<Slide> slides;

    private void callDynamicData(Context context, final List<Response<ListResponse<Asset>>> list, final List<VIUChannel> channelList, int counter) {
        assetCommonList = new ArrayList<>();
        if (apiCount > 1) {
            new CategoryRails().setRails(context, list, channelList, 2, slides, assetCommonList, counter);
        } else {
            new CategoryRails().setRails(context, list, channelList, loopCount, slides, assetCommonList, counter);
        }
    }
}
