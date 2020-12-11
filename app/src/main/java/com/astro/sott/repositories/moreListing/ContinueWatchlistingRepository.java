package com.astro.sott.repositories.moreListing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.callBacks.kalturaCallBacks.ContinueWatchingCallBack;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;

public class ContinueWatchlistingRepository {
    private static ContinueWatchlistingRepository projectRepository;
    private int continueWatchingCount = -1;
    private int continueWatchingRailCount = 0;

    public static ContinueWatchlistingRepository getInstance() {
        if (projectRepository == null) {
            projectRepository = new ContinueWatchlistingRepository();
        }
        return projectRepository;
    }

    public LiveData<AssetCommonBean> callContinueWatching(Context context) {
        KsServices ksServices = new KsServices(context);
        AssetCommonBean assetCommonBean = new AssetCommonBean();
        MutableLiveData<AssetCommonBean> mutableLiveData = new MutableLiveData<>();
        ksServices.callContinueWatchingForListing(new ContinueWatchingCallBack() {
            @Override
            public void response(boolean status, Response<ListResponse<Asset>> listResponseResponse) {
                if (status == true) {
                    List<RailCommonData> railList = new ArrayList<>();

                    List<Response<ListResponse<Asset>>> list = new ArrayList<>();
                    list.add(listResponseResponse);
                    for (int j = 0; j < listResponseResponse.results.getObjects().size(); j++) {
                        RailCommonData railCommonData = new RailCommonData();
                        railCommonData.setType(listResponseResponse.results.getObjects().get(j).getType());
                        railCommonData.setName(listResponseResponse.results.getObjects().get(j).getName());
                        railCommonData.setId(listResponseResponse.results.getObjects().get(j).getId());
                        railCommonData.setObject(listResponseResponse.results.getObjects().get(j));
                        railCommonData.setPosition(AssetContent.getVideoProgress(context, j, listResponseResponse.results.getObjects().get(j).getId().intValue()));
                        List<AssetCommonImages> imagesList = new ArrayList<>();
                        for (int k = 0; k < listResponseResponse.results.getObjects().get(j).getImages().size(); k++) {
                            AssetCommonImages assetCommonImages = new AssetCommonImages();
                            AppCommonMethods.getImageList(context, AppLevelConstants.TYPE6, 0, j, k, list, assetCommonImages, imagesList);
                        }
                        List<AssetCommonUrls> urlList = new ArrayList<>();
                        if (listResponseResponse.results.getObjects().get(j).getMediaFiles() != null) {
                            for (int k = 0; k < listResponseResponse.results.getObjects().get(j).getMediaFiles().size(); k++) {
                                AssetCommonUrls assetCommonUrls = new AssetCommonUrls();
                                assetCommonUrls.setUrl(listResponseResponse.results.getObjects().get(j).getMediaFiles().get(k).getUrl());
                                assetCommonUrls.setUrlType(listResponseResponse.results.getObjects().get(j).getMediaFiles().get(k).getType());
                                assetCommonUrls.setDuration(AppCommonMethods.getDuration(list, 0, j, k));
                                urlList.add(assetCommonUrls);
                            }
                        }
                        railCommonData.setImages(imagesList);
                        railCommonData.setUrls(urlList);

                        if (listResponseResponse.results.getObjects().get(j).getType() != MediaTypeConstant.getLinear(context) &&
                                listResponseResponse.results.getObjects().get(j).getType() != MediaTypeConstant.getTrailer(context) &&
                                listResponseResponse.results.getObjects().get(j).getType() != MediaTypeConstant.getClip()) {
                            continueWatchingRailCount++;
                            continueWatchingCount = 1;
                            railList.add(railCommonData);
                            assetCommonBean.setTotalCount(continueWatchingRailCount);
                        }


                    }
                    assetCommonBean.setRailAssetList(railList);
                    mutableLiveData.postValue(assetCommonBean);
                }
            }
        });
        return mutableLiveData;
    }


}
