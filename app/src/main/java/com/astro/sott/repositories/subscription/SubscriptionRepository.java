package com.astro.sott.repositories.subscription;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.callBacks.commonCallBacks.SubscriptionAssetListResponse;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionRepository {

    private static SubscriptionRepository resultRepository;
    private List<AssetCommonBean> assetCommonList;
    private AssetCommonBean assetCommonBean;
    private List<Response<ListResponse<Asset>>> responseList;
    private SubscriptionRepository(){

    }

    public static synchronized SubscriptionRepository getInstance() {
        if (resultRepository == null) {
            resultRepository = new SubscriptionRepository();
        }
        return resultRepository;
    }

    public MutableLiveData<List<com.kaltura.client.types.Subscription>> getSubscriptionPackageList(Context context, String id) {

        final MutableLiveData<List<com.kaltura.client.types.Subscription>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        ksServices.callSubscriptionPackageListApi(id,(status1,errorCode,message,subscriptionListRespone)->{
            if(status1){
                if(subscriptionListRespone != null){
                    if(subscriptionListRespone.size() > 0){

                        connection.postValue(subscriptionListRespone);
                    }else{
                        connection.postValue(subscriptionListRespone);
                    }
                }else{
                    connection.postValue(null);
                }
            }else{
                connection.postValue(null);
            }
        });

        return connection;
    }

    public LiveData<List<AssetCommonBean>> getAllChannelList(String id, int counter, Context context) {
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();

        KsServices ksServices = new KsServices(context);
        assetCommonBean = new AssetCommonBean();
        responseList = new ArrayList<>();
        assetCommonList = new ArrayList<>();
        ksServices.callViewChannelApi(id,counter, (status, commonResponse) -> {
            if (status) {

                assetCommonBean.setStatus(true);
                parseAllChannelList(context, commonResponse.getAssetList());
                connection.postValue(assetCommonList);
            } else {
                assetCommonBean.setStatus(false);
                assetCommonList.add(assetCommonBean);
                connection.postValue(null);
            }

        });
        return connection;
    }

    private void parseAllChannelList(Context context, Response<ListResponse<Asset>> assetList) {
        responseList.add(assetList);
        AssetCommonBean assetCommonBean = new AssetCommonBean();
        assetCommonBean.setStatus(true);

        setRailData(context, responseList, assetCommonBean);

    }

    private void setRailData(Context context, List<Response<ListResponse<Asset>>> list, AssetCommonBean assetCommonBean) {
        String tileType = AppLevelConstants.TYPE3;
        int totalCount = responseList.get(0).results.getTotalCount();
        if (totalCount != 0) {
            List<RailCommonData> railList = new ArrayList<>();

            if(list.get(0).results.getObjects() == null) {
                return;
            }

            for (int j = 0; j < list.get(0).results.getObjects().size(); j++) {
                RailCommonData railCommonData = new RailCommonData();
                // railCommonData.setCatchUpBuffer(list.get(0).results.getObjects().get(j).getEnableCatchUp());
                railCommonData.setType(list.get(0).results.getObjects().get(j).getType());
                railCommonData.setName(list.get(0).results.getObjects().get(j).getName());
                railCommonData.setId(list.get(0).results.getObjects().get(j).getId());
                railCommonData.setObject(list.get(0).results.getObjects().get(j));

                List<AssetCommonImages> imagesList = new ArrayList<>();
                for (int k = 0; k < list.get(0).results.getObjects().get(j).getImages().size(); k++) {

                    AssetCommonImages assetCommonImages = new AssetCommonImages();
                    //imageLogic(tileType,position,j,k,list,assetCommonImages,imagesList);

                    AppCommonMethods.getImageList(context, tileType, 0, j, k, list, assetCommonImages, imagesList);
                }

                List<AssetCommonUrls> urlList = new ArrayList<>();
                for (int k = 0; k < list.get(0).results.getObjects().get(j).getMediaFiles().size(); k++) {
                    AssetCommonUrls assetCommonUrls = new AssetCommonUrls();
                    assetCommonUrls.setUrl(list.get(0).results.getObjects().get(j).getMediaFiles().get(k).getUrl());
                    assetCommonUrls.setUrlType(list.get(0).results.getObjects().get(j).getMediaFiles().get(k).getType());
                    assetCommonUrls.setDuration(AppCommonMethods.getDuration(list, 0, j, k));
                    urlList.add(assetCommonUrls);
                }
                railCommonData.setImages(imagesList);
                railCommonData.setUrls(urlList);
                railList.add(railCommonData);

            }
            assetCommonBean.setTotalCount(list.get(0).results.getTotalCount());
            assetCommonBean.setRailAssetList(railList);
            assetCommonList.add(assetCommonBean);
        }
    }


    public LiveData<List<Asset>> getAssetList(String subscriptionOffer, Context context) {
        final MutableLiveData<List<Asset>> connection = new MutableLiveData<>();

        KsServices ksServices = new KsServices(context);
        ksServices.getAssetListForSubscription(subscriptionOffer, new SubscriptionAssetListResponse() {
            @Override
            public void response(boolean status, String message, List<Asset> listResponseResponse) {
                if (status){
                    connection.postValue(listResponseResponse);
                }else {
                    connection.postValue(null);
                }
            }
        });
        return connection;
    }
}
