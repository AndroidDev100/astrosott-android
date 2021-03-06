package com.astro.sott.baseModel;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.activities.search.constants.SearchFilterEnum;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeepSearchLayer {
    String ksql = "";
    private static DeepSearchLayer deepSearchLayer;
    private ArrayList<RailCommonData> railList = new ArrayList<RailCommonData>();
    private String genreskl;
    private List<Response<ListResponse<Asset>>> sillilarList;
    private List<Response<ListResponse<Asset>>> sillilarCreatorList;
    private List<Response<ListResponse<Asset>>> liveChannelList;
    private List<Response<ListResponse<Asset>>> similarChannelList;

    private DeepSearchLayer() {

    }

    public static DeepSearchLayer getInstance() {
        if (deepSearchLayer == null) {
            deepSearchLayer = new DeepSearchLayer();
        }
        return deepSearchLayer;
    }


    private void errorHandling() {
        railList = new ArrayList<>();
        RailCommonData assetCommonBean = new RailCommonData();
        assetCommonBean.setStatus(false);
        railList.add(assetCommonBean);

    }

    private void callDynamicData(Context context, String layout, List<Response<ListResponse<Asset>>> list) {
        try {

            if (list != null) {

                for (int j = 0; j < list.get(0).results.getObjects().size(); j++) {
                    RailCommonData railCommonData = new RailCommonData();
                    railCommonData.setTotalCount(list.get(0).results.getTotalCount());
                    railCommonData.setStatus(true);
                    railCommonData.setType(list.get(0).results.getObjects().get(j).getType());
                    railCommonData.setName(list.get(0).results.getObjects().get(j).getName());
                    railCommonData.setId(list.get(0).results.getObjects().get(j).getId());
                    railCommonData.setObject(list.get(0).results.getObjects().get(j));
                    int ugcCreator = list.get(0).results.getObjects().get(j).getType();

                    List<AssetCommonImages> imagesList = new ArrayList<AssetCommonImages>();

                    if (ugcCreator == MediaTypeConstant.getUGCCreator(context)) {
                        railCommonData.setCreatorName(AppCommonMethods.getCteatorName(list.get(0).results.getObjects().get(j).getName()));
                    } else {
                        for (int k = 0; k < list.get(0).results.getObjects().get(j).getImages().size(); k++) {
                            AssetCommonImages assetCommonImages = new AssetCommonImages();
                            AppCommonMethods.getImageList(context, layout, 0, j, k, list, assetCommonImages, imagesList);
                        }
                    }

                    List<AssetCommonUrls> urlList = new ArrayList<AssetCommonUrls>();
                    if (list.get(0).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context)) {

                    } else {
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
                    boolean value = applyFreePaidChannelFilter(list.get(0).results.getObjects().get(j), railList, context, railCommonData);

                }
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    private boolean applyFreePaidChannelFilter(Asset results, ArrayList<RailCommonData> railList, Context context, RailCommonData railCommonData) {
        try {
            if (!KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase("")) {

                Map<String, MultilingualStringValueArray> tagMap = results.getTags();
                if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.ALL.name())) {
                    railList.add(railCommonData);
                } else if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.FREE.name())) {
                    MultilingualStringValueArray language_list = tagMap.get(AppLevelConstants.KEY_BILLING_ID);
                    if (results.getType() == MediaTypeConstant.getSeries(context) || results.getType() == MediaTypeConstant.getCollection(context) || results.getType() == MediaTypeConstant.getLinear(context)) {
                        if (!AssetContent.getBillingIdForSeries(tagMap)) {
                            railList.add(railCommonData);
                        }
                    } else if (language_list != null) {
                        if (language_list.getObjects() != null && language_list.getObjects().size() > 0 && language_list.getObjects().get(0).getValue() != null
                                && !language_list.getObjects().get(0).getValue().equalsIgnoreCase("")) {

                        } else {
                            railList.add(railCommonData);
                        }
                    } else {
                        railList.add(railCommonData);
                    }
                } else if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.PAID.name())) {
                    MultilingualStringValueArray language_list = tagMap.get(AppLevelConstants.KEY_BILLING_ID);
                    if (results.getType() == MediaTypeConstant.getSeries(context) || results.getType() == MediaTypeConstant.getCollection(context) || results.getType() == MediaTypeConstant.getLinear(context)) {
                        if (AssetContent.getBillingIdForSeries(tagMap)) {
                            railList.add(railCommonData);
                        }
                    } else if (language_list != null) {
                        if (language_list.getObjects() != null && language_list.getObjects().size() > 0 && language_list.getObjects().get(0).getValue() != null
                                && !language_list.getObjects().get(0).getValue().equalsIgnoreCase("")) {
                            railList.add(railCommonData);
                        }
                    }
                }
            } else {
                railList.add(railCommonData);
            }

        } catch (Exception ignored) {
            railList.add(railCommonData);
        }
        return false;
    }


    public LiveData<List<RailCommonData>> loadSearchedData(Context context, int assetId, String filterValue, List<String> genreList, int counter, final String layout, boolean isScrolling, int pageSize) {
        if (!isScrolling) {
            railList.clear();
        }

        final KsServices ksServices = new KsServices(context);
        final MutableLiveData<List<RailCommonData>> connection = new MutableLiveData<>();
        List<VIUChannel> list = new ArrayList<>();
        VIUChannel channel = new VIUChannel();
        channel.setId((long) assetId);

        ksql = AppCommonMethods.getDeepSearchKsql("", null, 1, context);
       /* if(genreList.size()>0) {

            ksql = AssetContent.getGenredata(genreList);
        }else {
            ksql = "Genre = ''";
        }*/
        // DTChannel channel = new DTChannel((long) assetId,"","");
        list.add(channel);
        ksServices.callDeepSearchAssetListing(context, (long) 1233, list, ksql, filterValue, counter, pageSize, (status, listResponseResponse, channelList) -> {
            if (status) {
                // PrintLogging.printLog("totalCount");
                callDynamicData(context, layout, listResponseResponse);
                connection.postValue(railList);
            } else {
                errorHandling();
                connection.postValue(railList);
            }
        });
        return connection;
    }
}
