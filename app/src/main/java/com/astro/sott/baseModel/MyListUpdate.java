package com.astro.sott.baseModel;

import android.app.Activity;
import android.content.Context;

import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.kalturaCallBacks.ContinuewWatchingList;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;

public class MyListUpdate {
    private ContinuewWatchingList callBack;

    private List<AssetCommonBean> assetCommonList;

    public void updateCall(Context context, List<VIUChannel> dtChannelsList, int position, ContinuewWatchingList callBck) {
        this.callBack = callBck;
        new KsServices(context).getWatchlistRails(dtChannelsList, (status, errorCode, message, listResponseResponse) -> {
            assetCommonList = new ArrayList<>();
            if (status) {
                AssetCommonBean assetCommonBean = new AssetCommonBean();
                assetCommonBean.setStatus(true);
                assetCommonBean.setRailDetail(dtChannelsList.get(position));
                assetCommonBean.setTitle(dtChannelsList.get(position).getName());
                assetCommonBean.setID(dtChannelsList.get(position).getId());
                setRailData(context, dtChannelsList.get(position), listResponseResponse, 1, assetCommonBean, position, AppConstants.TYPE5);
                assetCommonBean.setRailType(AppConstants.Rail5);

            } else {
                callBck.response(false, assetCommonList);
            }
        });
    }


    private void setRailData(Context context, VIUChannel channelList, List<Response<ListResponse<Asset>>> list, int type,
                             AssetCommonBean assetCommonBean, int position, String tileType) {
        try {
            List<RailCommonData> railList = new ArrayList<RailCommonData>();
            for (int j = 0; j < list.get(0).results.getObjects().size(); j++) {

                RailCommonData railCommonData = new RailCommonData();
//                            railCommonData.setCatchUpBuffer(list.get(position).results.getObjects().get(j).getEnableCatchUp());
                railCommonData.setType(list.get(0).results.getObjects().get(j).getType());
                railCommonData.setName(list.get(0).results.getObjects().get(j).getName());
                railCommonData.setId(list.get(0).results.getObjects().get(j).getId());
                railCommonData.setObject(list.get(0).results.getObjects().get(j));
                railCommonData.setRailDetail(channelList);
                if (tileType.equals(AppConstants.TYPE6)) {
                    PrintLogging.printLog("", "valueeInLoop" + AssetContent.getVideoProgress(context, j, list.get(0).results.getObjects().get(j).getId().intValue()));
                    railCommonData.setPosition(AssetContent.getVideoProgress(context, j, list.get(0).results.getObjects().get(j).getId().intValue()));
                    railCommonData.setProgress(AssetContent.getVideoPosition(context, j, list.get(0).results.getObjects().get(j).getId().intValue()));
                }
                if (tileType.equals(AppConstants.TYPE7)) {
                    railCommonData.setType(AppConstants.Rail7);
                }
                int ugcCreator = list.get(0).results.getObjects().get(j).getType();
                List<AssetCommonImages> imagesList = new ArrayList<AssetCommonImages>();
                if (ugcCreator == MediaTypeConstant.getUGCCreator(context)) {
                    PrintLogging.printLog("", "creatorNameInrail" + list.get(0).results.getObjects().get(j).getName());

                    railCommonData.setCreatorName(AppCommonMethods.getCteatorName(list.get(0).results.getObjects().get(j).getName().trim()));
                } else {
                    for (int k = 0; k < list.get(0).results.getObjects().get(j).getImages().size(); k++) {
                        AssetCommonImages assetCommonImages = new AssetCommonImages();
                        AppCommonMethods.getCategoryImageList(context, tileType, 0, j, k, list, assetCommonImages, imagesList, channelList);
                    }
                }


                List<AssetCommonUrls> urlList = new ArrayList<AssetCommonUrls>();
                if (list.get(0).results.getObjects().get(j).getMediaFiles() != null) {
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
            callBack.response(true, assetCommonList);
        } catch (Exception e) {
            PrintLogging.printLog("", "categoryCatch-->>" + e.toString());
        }

    }
}
