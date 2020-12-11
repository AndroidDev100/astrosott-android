package com.astro.sott.fragments.nowPlaying.layers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;


import java.util.ArrayList;
import java.util.List;

public class LiveNowPrograms {

    private Context context;
    String tileType;
    private int assetType;
    private AssetCommonBean assetCommonBean;
    private List<AssetCommonBean> assetCommonList;
    private List<Response<ListResponse<Asset>>> responseList;

    private static LiveNowPrograms movieDescriptionRepository;
    public static LiveNowPrograms getInstance() {
        if (movieDescriptionRepository == null) {
            movieDescriptionRepository = new LiveNowPrograms();
        }
        return movieDescriptionRepository;
    }

    public static void resetObject() {
        movieDescriptionRepository=null;
    }

    public LiveData<List<AssetCommonBean>> loadData(final Asset asset, Context ctx) {
        context=ctx;
        assetType=AppLevelConstants.Rail5;
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();
        KsServices ksServices = new KsServices(ctx);
        assetCommonBean=new AssetCommonBean();
        assetCommonList=new ArrayList<>();
        responseList = new ArrayList<>();
        ksServices.callLiveNowRail(1, (status, result) -> {
            if (status) {
                assetCommonBean.setStatus(true);
                parseLiveNowAssests(result);
                connection.postValue(assetCommonList);
            }else {
                assetCommonBean.setStatus(false);
                assetCommonList.add(assetCommonBean);
                connection.postValue(assetCommonList);
            }
        });
        return connection;
    }

    public LiveData<List<AssetCommonBean>> loadAllChannelsData(int pageCount,final Asset asset, Context ctx) {
        context=ctx;
        assetType=AppLevelConstants.Rail5;
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();
        KsServices ksServices = new KsServices(ctx);
        assetCommonBean=new AssetCommonBean();
        assetCommonList=new ArrayList<>();
        responseList = new ArrayList<>();
        ksServices.callLiveNowRail(pageCount, (status, result) -> {
            if (status) {
                assetCommonBean.setStatus(true);
                parseLiveNowAssests(result);
                connection.postValue(assetCommonList);
            }else {
                assetCommonBean.setStatus(false);
                assetCommonList.add(assetCommonBean);
                connection.postValue(assetCommonList);
            }
        });
        return connection;
    }

    private void parseLiveNowAssests(Response<ListResponse<Asset>> assetList) {
        responseList.add(assetList);
        assetCommonBean.setStatus(true);
        long id=1;
        assetCommonBean.setID(id);
        assetCommonBean.setMoreType(AppLevelConstants.LIVE_CHANNEL_LIST);
        assetCommonBean.setRailType(assetType);
        assetCommonBean.setTitle(context.getResources().getString(R.string.live));

        setRailData(responseList, assetCommonBean, AppLevelConstants.TYPE5);

    }

    private void setRailData(List<Response<ListResponse<Asset>>> list,
                             AssetCommonBean assetCommonBean, String tileType) {

        int totalCount = responseList.get(0).results.getTotalCount();
        if (totalCount == 0) {

        } else {
            List<RailCommonData> railList = new ArrayList<RailCommonData>();
            for (int j = 0; j < list.get(0).results.getObjects().size(); j++) {
                RailCommonData railCommonData = new RailCommonData();
               // railCommonData.setCatchUpBuffer(list.get(0).results.getObjects().get(j).getEnableCatchUp());
                railCommonData.setType(list.get(0).results.getObjects().get(j).getType());
                railCommonData.setName(list.get(0).results.getObjects().get(j).getName());
                railCommonData.setId(list.get(0).results.getObjects().get(j).getId());
                railCommonData.setObject(list.get(0).results.getObjects().get(j));

                List<AssetCommonImages> imagesList = new ArrayList<AssetCommonImages>();
                for (int k = 0; k < list.get(0).results.getObjects().get(j).getImages().size(); k++) {

                    AssetCommonImages assetCommonImages = new AssetCommonImages();
                    //imageLogic(tileType,position,j,k,list,assetCommonImages,imagesList);

                    AppCommonMethods.getImageList(context,tileType, 0, j, k, list, assetCommonImages, imagesList);
                }

                List<AssetCommonUrls> urlList = new ArrayList<AssetCommonUrls>();
                if (list.get(0).results.getObjects().get(j).getMediaFiles()!=null){
                    for (int k = 0; k < list.get(0).results.getObjects().get(j).getMediaFiles().size(); k++) {
                        AssetCommonUrls assetCommonUrls = new AssetCommonUrls();
                        assetCommonUrls.setUrl(list.get(0).results.getObjects().get(j).getMediaFiles().get(k).getUrl());
                        assetCommonUrls.setUrlType(list.get(0).results.getObjects().get(j).getMediaFiles().get(k).getType());
                        assetCommonUrls.setDuration(AppCommonMethods.getDuration(list, 0, j, k));
                        urlList.add(assetCommonUrls);
                    }
                }

                railCommonData.setImages(imagesList);
                railCommonData.setUrls(urlList);
                railList.add(railCommonData);
                assetCommonBean.setTotalCount(list.get(0).results.getTotalCount());
            }
            assetCommonBean.setRailAssetList(railList);
            assetCommonList.add(assetCommonBean);
        }
    }

}
