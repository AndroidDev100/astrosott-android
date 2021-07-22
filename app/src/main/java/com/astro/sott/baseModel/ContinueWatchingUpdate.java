package com.astro.sott.baseModel;

import android.content.Context;

import com.astro.sott.ApplicationMain;
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
import com.enveu.Enum.PredefinePlaylistType;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.AssetHistory;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;

public class ContinueWatchingUpdate {

    private ContinuewWatchingList callBack;
    private int continueWatchingCount = -1;
    private int continueWatchingRailCount = 0;
    private List<AssetCommonBean> assetCommonList;

    public void updateCall(Context context, List<VIUChannel> dtChannelsList, int counter, int swipeToRefresh, List<AssetCommonBean> list, ContinuewWatchingList callBck) {
        callBack = callBck;
        List<Response<ListResponse<Asset>>> responseList = new ArrayList<Response<ListResponse<Asset>>>();
        PrintLogging.printLog("", "sizeOfOf  " + counter);
        new KsServices(context).callContinueWatchingAPI(responseList, dtChannelsList, (status, listResponseResponse, channelList) -> {
            assetCommonList = new ArrayList<>();
            if (status) {
                if (listResponseResponse != null) {
                    // callBack.response(true,responseList,channelList);
                    try {
                        AssetCommonBean assetCommonBean = new AssetCommonBean();
                        setRailType(assetCommonBean, AppConstants.Rail6, dtChannelsList.get(counter).getWidgetType());
                        assetCommonBean.setRailType(AppConstants.Rail6);
                        assetCommonBean.setRailDetail(dtChannelsList.get(counter));
                        assetCommonBean.setTitle(dtChannelsList.get(counter).getName());
                        assetCommonBean.setMoreType(AppConstants.CONTINUE_WATCHING);
                        assetCommonBean.setID(dtChannelsList.get(counter).getId());
                        List<RailCommonData> railCommonData = new ArrayList<>();
                        sortContinueWatchingRail(context, listResponseResponse, 1, assetCommonBean, 0, AppConstants.TYPE6, railCommonData, dtChannelsList.get(counter));
                    } catch (Exception e) {

                    }
                    PrintLogging.printLog("", "sizeOfContinue " + assetCommonList.size() + "");
                }

            } else {
                callBck.response(false, assetCommonList);
            }
        });
    }

    public void setRailType(AssetCommonBean commonBean, int railType, int widgetType) {
        //   String description=setDescription(railType);
        VIUChannel channel = new VIUChannel();
        channel.setWidgetType(widgetType);
        channel.setShowHeader(true);
        //  channel.setDescription(description);
        channel.setPredefPlaylistType(PredefinePlaylistType.CON_W.name());
        commonBean.setRailDetail(channel);
    }



   /* private String setDescription(int railType) {
        String description=HORIZONTAL_LANDSCAPE;
        if (railType==AppConstants.Rail3){
            description=HORIZONTAL_POTRAIT;
        }else if (railType==AppConstants.Rail4){
            description=HORIZONTAL_SQUARE;
        }
        return description;
    }*/


    public void sortContinueWatchingRail(Context context, List<Response<ListResponse<Asset>>> list, int type,
                                         AssetCommonBean assetCommonBean, int position, String tileType, List<RailCommonData> railList, VIUChannel channelList) {

        List<Long> longList = AppCommonMethods.getContinueWatchingIDsPreferences(context);
        List<AssetHistory> continueWatchingList = AppCommonMethods.getContinueWatchingPreferences(context);
        if (longList != null) {
            if (longList.size() > 0) {
                for (int y = 0; y < longList.size(); y++) {
                    long con_id = longList.get(y);
                    for (int j = 0; j < list.get(position).results.getObjects().size(); j++) {
                        if (con_id == list.get(position).results.getObjects().get(j).getId()) {
                            PrintLogging.printLog("", "indexessss-->>" + list.get(position).results.getObjects().get(j).getId());
                            RailCommonData railCommonData = new RailCommonData();
//                            railCommonData.setCatchUpBuffer(list.get(position).results.getObjects().get(j).getEnableCatchUp());
                            railCommonData.setType(list.get(position).results.getObjects().get(j).getType());
                            railCommonData.setName(list.get(position).results.getObjects().get(j).getName());
                            railCommonData.setId(list.get(position).results.getObjects().get(j).getId());
                            railCommonData.setRailDetail(channelList);
                            railCommonData.setObject(list.get(position).results.getObjects().get(j));
                            if (tileType.equals(AppConstants.TYPE6)) {
                                PrintLogging.printLog("", "valueeInLoop" + AssetContent.getVideoProgress(context, j, list.get(position).results.getObjects().get(j).getId().intValue()));
                                railCommonData.setPosition(AssetContent.getVideoProgress(context, j, list.get(position).results.getObjects().get(j).getId().intValue()));
                                railCommonData.setProgress(AssetContent.getVideoPosition(context, j, list.get(position).results.getObjects().get(j).getId().intValue()));
                            }
                            if (tileType.equals(AppConstants.TYPE7)) {
                                railCommonData.setType(AppConstants.Rail7);
                            }
                            int ugcCreator = list.get(position).results.getObjects().get(j).getType();
                            List<AssetCommonImages> imagesList = new ArrayList<AssetCommonImages>();
                            if (ugcCreator == MediaTypeConstant.getUGCCreator(context)) {
                                railCommonData.setCreatorName(AppCommonMethods.getCteatorName(list.get(position).results.getObjects().get(j).getName()));
                            } else {
                                for (int k = 0; k < list.get(position).results.getObjects().get(j).getImages().size(); k++) {
                                    AssetCommonImages assetCommonImages = new AssetCommonImages();
                                    AppCommonMethods.getCategoryImageList(context, tileType, position, j, k, list, assetCommonImages, imagesList, channelList);
                                }
                            }


                            List<AssetCommonUrls> urlList = new ArrayList<AssetCommonUrls>();
                            if (list.get(position).results.getObjects().get(j).getMediaFiles() != null) {
                                for (int k = 0; k < list.get(position).results.getObjects().get(j).getMediaFiles().size(); k++) {
                                    AssetCommonUrls assetCommonUrls = new AssetCommonUrls();
                                    assetCommonUrls.setUrl(list.get(position).results.getObjects().get(j).getMediaFiles().get(k).getUrl());
                                    assetCommonUrls.setUrlType(list.get(position).results.getObjects().get(j).getMediaFiles().get(k).getType());
                                    assetCommonUrls.setDuration(AppCommonMethods.getDuration(list, position, j, k));
                                    urlList.add(assetCommonUrls);
                                }
                            }

                            railCommonData.setImages(imagesList);
                            railCommonData.setUrls(urlList);
                            if (tileType.equals(AppConstants.TYPE6)) {

                                if (list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getUGCVideo(context) || list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context) ||
                                        list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getLinear(context)) {
                                } else {
                                    boolean included = AppCommonMethods.shouldItemIncluded(continueWatchingList, list.get(position).results.getObjects().get(j).getId() + "");
                                    if (included) {
                                        continueWatchingRailCount++;
                                        continueWatchingCount = 1;
                                        railList.add(railCommonData);
                                        assetCommonBean.setTotalCount(continueWatchingRailCount);
                                    }
                                }
                            } else {
                                assetCommonBean.setTotalCount(list.get(position).results.getTotalCount());
                                railList.add(railCommonData);

                            }
                        }


                    }

                }

            }

            PrintLogging.printLog("", "continueWatchingCount==" + continueWatchingCount);
            assetCommonBean.setRailAssetList(railList);
            if (tileType.equals(AppConstants.TYPE6)) {
                if (continueWatchingCount == 1) {
                    assetCommonList.add(assetCommonBean);
                    callBack.response(true, assetCommonList);
                    PrintLogging.printLog("", "continueWatchingRailCount-->>" + continueWatchingRailCount);
                } else {
                    assetCommonBean.setStatus(false);
                    assetCommonList.add(assetCommonBean);
                    callBack.response(false, assetCommonList);

                }
            } else {
                assetCommonList.add(assetCommonBean);
            }
        }


    }


}
