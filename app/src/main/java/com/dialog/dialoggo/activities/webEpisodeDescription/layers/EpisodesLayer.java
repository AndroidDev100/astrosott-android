package com.dialog.dialoggo.activities.webEpisodeDescription.layers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonImages;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonUrls;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.AssetContent;
import com.dialog.dialoggo.utils.helpers.MediaTypeConstant;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EpisodesLayer {

    private String seriesId;
    private List<Response<ListResponse<Asset>>> responseList;
    private static EpisodesLayer webEpisodeDescriptionRepository;

    public static EpisodesLayer getInstance() {
        if (webEpisodeDescriptionRepository == null) {
            webEpisodeDescriptionRepository = new EpisodesLayer();
        }

        return webEpisodeDescriptionRepository;
    }

    private AssetCommonBean assetCommonBean;
    private List<AssetCommonBean> assetCommonList;

    public LiveData<List<AssetCommonBean>> getEpisodesList(Context context, Map<String, MultilingualStringValueArray> map,
                                                           int assetType, int counter, List<Integer> seasonNumberList, int seasonCounter, int layoutType) {

        responseList = new ArrayList<>();
        assetCommonList = new ArrayList<>();
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();
        assetCommonBean = new AssetCommonBean();
        seriesId = AssetContent.getSeriesId(map);
        KsServices ksServices = new KsServices(context);
        ksServices.callSeasonEpisodes(counter, seriesId, assetType, seasonNumberList, seasonCounter, (status, commonResponse) -> {

            if (status) {
                assetCommonBean.setStatus(true);
                parseEpisodesAssests(context, commonResponse.getAssetList(), seasonNumberList, layoutType, assetType, seasonCounter);
                connection.postValue(assetCommonList);
            } else {
                assetCommonBean.setStatus(false);
                assetCommonList.add(assetCommonBean);
                connection.postValue(assetCommonList);
            }
        });
        return connection;
    }

    private void parseEpisodesAssests(Context context, Response<ListResponse<Asset>> list, List<Integer> seasonNumberList, int layoutType, int assetType, int i) {

        if(list == null){
            return;
        }
        responseList.add(list);
        assetCommonBean.setRailType(layoutType);
        assetCommonBean.setMoreType(AppLevelConstants.WEB_EPISODE);
        if (assetType == MediaTypeConstant.getWebEpisode(context)) {
            assetCommonBean.setMoreAssetType(assetType);
        } else {
            if (assetType == MediaTypeConstant.getDrama(context)) {
                assetCommonBean.setMoreType(AppLevelConstants.WEB_EPISODE);
            }
        }
        long id = 1;
        assetCommonBean.setID(id);

        assetCommonBean.setMoreSeriesID(seriesId);
        assetCommonBean.setMoreID(seasonNumberList.get(i));
        int seriesNumber2 = AssetContent.getSeriesNumber(list.results.getObjects().get(0).getMetas());
        int seriesNumber;
        if (seriesNumber2 == -1) {
            seriesNumber = i + 1;
        } else {
            seriesNumber = seriesNumber2;
        }

        assetCommonBean.setTitle(context.getResources().getString(R.string.season) + " " + seriesNumber);
        setRailData(context, responseList, assetCommonBean, i);
    }

    private void setRailData(Context context, List<Response<ListResponse<Asset>>> list,
                             AssetCommonBean assetCommonBean, int position) {
        if(!(list.get(position).results.getTotalCount() >= 0)){
            return;
        }
        int totalCount = list.get(position).results.getTotalCount();
        if (totalCount != 0) {
            List<RailCommonData> railList = new ArrayList<>();
            try{
                for (int j = 0; j < list.get(position).results.getObjects().size(); j++) {
                    RailCommonData railCommonData = new RailCommonData();
                    //  railCommonData.setCatchUpBuffer(list.get(position).results.getObjects().get(j).getEnableCatchUp());
                    railCommonData.setType(list.get(position).results.getObjects().get(j).getType());
                    railCommonData.setName(list.get(position).results.getObjects().get(j).getName());
                    railCommonData.setId(list.get(position).results.getObjects().get(j).getId());
                    railCommonData.setObject(list.get(position).results.getObjects().get(j));

                    List<AssetCommonImages> imagesList = new ArrayList<>();
                    for (int k = 0; k < list.get(position).results.getObjects().get(j).getImages().size(); k++) {

                        AssetCommonImages assetCommonImages = new AssetCommonImages();

                        AppCommonMethods.getImageList(context, AppLevelConstants.TYPE3, position, j, k, list, assetCommonImages, imagesList);
                    }

                    List<AssetCommonUrls> urlList = new ArrayList<>();
                    for (int k = 0; k < list.get(position).results.getObjects().get(j).getMediaFiles().size(); k++) {
                        AssetCommonUrls assetCommonUrls = new AssetCommonUrls();
                        assetCommonUrls.setUrl(list.get(position).results.getObjects().get(j).getMediaFiles().get(k).getUrl());
                        assetCommonUrls.setUrlType(list.get(position).results.getObjects().get(j).getMediaFiles().get(k).getType());
                        assetCommonUrls.setDuration(AppCommonMethods.getDuration(list, position, j, k));

                        urlList.add(assetCommonUrls);
                    }
                    railCommonData.setImages(imagesList);
                    railCommonData.setUrls(urlList);
                    railList.add(railCommonData);

                }

            }catch (Exception e){
                PrintLogging.printLog("Exception", e.toString());
            }
            assetCommonBean.setRailAssetList(railList);
            assetCommonBean.setTotalCount(list.get(position).results.getTotalCount());
            assetCommonList.add(assetCommonBean);
        }
    }
}
