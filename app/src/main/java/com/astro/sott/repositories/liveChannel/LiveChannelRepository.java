package com.astro.sott.repositories.liveChannel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.baseModel.CategoryRails;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.R;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;

public class LiveChannelRepository {

    private List<RailCommonData> channelList;
    private List<Response<ListResponse<Asset>>> liveChannelList;
    private List<Response<ListResponse<Asset>>> similarList;
    private List<AssetCommonBean> assetCommonList;
    private Context context;
    private String tileType;
    private String liveGenre;
    private int assetType;

    private LiveChannelRepository() {

    }

    public static LiveChannelRepository getInstance() {
        return new LiveChannelRepository();
    }

    public LiveData<List<RailCommonData>> loadChannelsData(Context applicationContext,
                                                           String externalId, final String startDate, String endDate, int type,int counter) {
        context = applicationContext;
        final MutableLiveData<List<RailCommonData>> connection = new MutableLiveData<>();
        KsServices ksServices = new KsServices(applicationContext);
        channelList = new ArrayList<>();
        ksServices.callLiveEPGDayWise(externalId, startDate, endDate, type,counter, (status, result) -> {
            if (status == true) {
                setData(connection, result);
            } else {
                connection.postValue(channelList);
            }
        });

        return connection;
    }


    public LiveData<List<RailCommonData>> loadCatchupData(Context applicationContext,
                                                          String externalId, final String startDate, int type) {
        context = applicationContext;
        final MutableLiveData<List<RailCommonData>> connection = new MutableLiveData<>();
        KsServices ksServices = new KsServices(applicationContext);
        channelList = new ArrayList<>();
        ksServices.callCatchupData(externalId, startDate,type,(status, result) -> {
            if (status == true) {
                setData(connection, result);
            } else {
                connection.postValue(channelList);
            }
        });

        return connection;
    }

    public LiveData<List<RailCommonData>> liveCatchupData(Context applicationContext,
                                                          String externalId) {
        context = applicationContext;
        final MutableLiveData<List<RailCommonData>> connection = new MutableLiveData<>();
        KsServices ksServices = new KsServices(applicationContext);
        channelList = new ArrayList<>();
        ksServices.liveCatchupData(externalId,(status, result) -> {
            if (status == true) {
                setData(connection, result);
            } else {
                connection.postValue(channelList);
            }
        });

        return connection;
    }


    private void setData(MutableLiveData<List<RailCommonData>> connection,
                         Response<ListResponse<Asset>> result) {
        for (int i = 0; i < result.results.getObjects().size(); i++) {
            RailCommonData assetCommonBean = new RailCommonData();
            assetCommonBean.setStatus(true);
            assetCommonBean.setObject(result.results.getObjects().get(i));
            assetCommonBean.setTotalCount(result.results.getTotalCount());
            channelList.add(assetCommonBean);
        }
        connection.postValue(channelList);
    }

    public LiveData<List<AssetCommonBean>> loadData(final Asset asset, Context ctx) {
        context = ctx;
        assetType = asset.getType();
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();
        liveChannelList = new ArrayList<>();
        similarList = new ArrayList<>();
        KsServices ksServices = new KsServices(ctx);
        liveGenre = AssetContent.getLiveGenredata(asset.getTags());
        ksServices.callLiveNowRail(1, (status, result) -> {
            if (status == true) {
                liveChannelList.add(result);
                callSimilarChannels(assetType, connection, liveGenre);
            } else {
                callSimilarChannels(assetType, connection, liveGenre);

            }
        });
        return connection;
    }

    private void callSimilarChannels(Integer type, final MutableLiveData<List<AssetCommonBean>> connection, String genre) {
        KsServices ksServices = new KsServices(context);
        ksServices.getSimilarChannel(1, type, genre, (status, result) -> {

            if (status == true) {
                similarList.add(result);
                callBelowData(connection, AppLevelConstants.Rail5);
            } else {
                callBelowData(connection, AppLevelConstants.Rail5);
            }
        });
    }

    private void callBelowData(final MutableLiveData<List<AssetCommonBean>> mutableLiveData, final int layoutType) {
        final KsServices ksServices = new KsServices(context);
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {
                ksServices.callHomeChannels(context,0, AppLevelConstants.TAB_LIVETV_DETAIL, (status1, listResponseResponse, channelList) -> {
                    if (status1) {
                        callDynamicData(listResponseResponse, channelList, layoutType);
                        mutableLiveData.postValue(assetCommonList);
                    } else {
                        errorHandling();
                    }

                });
            }
        });
    }

    private void errorHandling() {
        assetCommonList = new ArrayList<>();
        AssetCommonBean assetCommonBean = new AssetCommonBean();
        assetCommonBean.setStatus(false);
        assetCommonList.add(assetCommonBean);
    }

    private void callDynamicData(final List<Response<ListResponse<Asset>>> list, final List<VIUChannel> channelList, int layoutType) {
        assetCommonList = new ArrayList<>();
        for (int i = 0; i < liveChannelList.size(); i++) {

            AssetCommonBean assetCommonBean = new AssetCommonBean();
            assetCommonBean.setStatus(true);
            if (channelList != null && channelList.size() > 0) {
                assetCommonBean.setID(channelList.get(i).getId());
            } else {
                long id = 1;
                assetCommonBean.setID(id);
            }

            assetCommonBean.setMoreType(AppLevelConstants.LIVE_CHANNEL_LIST);
            assetCommonBean.setRailType(layoutType);
            assetCommonBean.setTitle(context.getResources().getString(R.string.live));

            setRailData(liveChannelList, assetCommonBean, i, calculateLatout(layoutType));

        }
        for (int i = 0; i < similarList.size(); i++) {

            AssetCommonBean assetCommonBean = new AssetCommonBean();
            assetCommonBean.setStatus(true);
            assetCommonBean.setMoreType(AppLevelConstants.SIMILLAR_CHANNEL_LIST);
            assetCommonBean.setMoreAssetType(assetType);
            assetCommonBean.setMoreSeriesID(liveGenre);
            if (channelList != null && channelList.size() > 0) {
                assetCommonBean.setID(channelList.get(i).getId());
            } else {
                long id = 1;
                assetCommonBean.setID(id);
            }
            assetCommonBean.setRailType(AppLevelConstants.Rail2);
            assetCommonBean.setTitle(context.getResources().getString(R.string.similar_channel));

            setRailData(similarList, assetCommonBean, i, calculateLatout(AppLevelConstants.Rail2));

        }
        if (channelList != null && channelList.size() > 0)
            new CategoryRails().setDescriptionRails(context,list, channelList, assetCommonList, 0);
    }

    private String calculateLatout(int layoutType) {
        if (layoutType == 1) {
            tileType = AppLevelConstants.TYPE2;
        } else if (layoutType == 2) {
            tileType = AppLevelConstants.TYPE3;
        } else if (layoutType == 3) {
            tileType = AppLevelConstants.TYPE4;
        } else if (layoutType == 4) {
            tileType = AppLevelConstants.TYPE5;
        }
        return tileType;
    }

    private void setRailData(List<Response<ListResponse<Asset>>> list,
                             AssetCommonBean assetCommonBean, int position, String tileType) {

        if (1 == 0) {

        } else {
            int totalCount = list.get(position).results.getTotalCount();
            if (totalCount == 0) {

            } else {
                List<RailCommonData> railList = new ArrayList<RailCommonData>();
                for (int j = 0; j < list.get(position).results.getObjects().size(); j++) {
                    RailCommonData railCommonData = new RailCommonData();
                   // railCommonData.setCatchUpBuffer(list.get(position).results.getObjects().get(j).getEnableCatchUp());
                    railCommonData.setType(list.get(position).results.getObjects().get(j).getType());
                    railCommonData.setName(list.get(position).results.getObjects().get(j).getName());
                    railCommonData.setId(list.get(position).results.getObjects().get(j).getId());
                    railCommonData.setObject(list.get(position).results.getObjects().get(j));

                    List<AssetCommonImages> imagesList = new ArrayList<AssetCommonImages>();
                    for (int k = 0; k < list.get(position).results.getObjects().get(j).getImages().size(); k++) {

                        AssetCommonImages assetCommonImages = new AssetCommonImages();
                        //imageLogic(tileType,position,j,k,list,assetCommonImages,imagesList);

                        AppCommonMethods.getImageList(context,tileType, position, j, k, list, assetCommonImages, imagesList);
                    }

                    List<AssetCommonUrls> urlList = new ArrayList<AssetCommonUrls>();
                    if (list.get(position).results.getObjects().get(j).getMediaFiles() != null) {
                        int size = list.get(position).results.getObjects().get(j).getMediaFiles().size();
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
                    railList.add(railCommonData);

                    assetCommonBean.setTotalCount(list.get(position).results.getTotalCount());

                }
                assetCommonBean.setRailAssetList(railList);
                assetCommonList.add(assetCommonBean);
            }
        }
    }


    public LiveData<List<AssetCommonBean>> callCategoryData(Asset asset,final int screen) {
        assetCommonList = new ArrayList<>();
        final MutableLiveData<List<AssetCommonBean>> mutableLiveData = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        AppCommonMethods.checkDMS(context,status -> {
            if (status) {
                ksServices.callHomeChannels(context,0, screen, (status1, listResponseResponse, channelList) -> {
                    if (status1) {
                        new CategoryRails().setDescriptionRails(context,listResponseResponse, channelList, assetCommonList, 0);
                        mutableLiveData.postValue(assetCommonList);
                    } else {
                        errorHandling();
                    }

                });
            }
        });
        return mutableLiveData;
    }
}
