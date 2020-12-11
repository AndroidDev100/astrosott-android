package com.astro.sott.repositories.videoTab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.baseModel.CategoryRails;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.utils.helpers.carousel.model.Slide;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;


public class VideoFragmentRepository {
    private List<AssetCommonBean> assetCommonList;
    private ArrayList<Slide> slides;
    private int apiCount = 0;
    private static VideoFragmentRepository projectRepository;
    private boolean flag=false;

    public static VideoFragmentRepository getInstance() {
        if (projectRepository == null) {
            projectRepository = new VideoFragmentRepository();
        }
        return projectRepository;
    }

    public static void resetObject() {
        projectRepository = null;
    }

    public LiveData<List<AssetCommonBean>> loadData(Context context, long channelID, List<VIUChannel> list, int counter, int swipeRefresh, boolean craousalInjected) {
        apiCount = counter;
        flag=craousalInjected;
        final KsServices ksServices = new KsServices(context);
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {
                ksServices.callChannelRail(0, channelID, list, counter, (status1, listResponseResponse, channelList) -> {
                    if (status1) {
                        apiCount++;
                        callDynamicData(context,listResponseResponse, channelList, counter);
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
        if (flag) {
            new CategoryRails().setRails(context, list, channelList, 2, slides, assetCommonList, counter);
        } else {
            new CategoryRails().setRails(context, list, channelList, 1, slides, assetCommonList, counter);
        }

    }

}
