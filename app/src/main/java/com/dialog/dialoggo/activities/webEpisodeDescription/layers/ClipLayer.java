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
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClipLayer {

    private List<AssetCommonBean> assetCommonList;
    private  List<Response<ListResponse<Asset>>> responseList;
    private static ClipLayer clipLayer;

    public static ClipLayer getInstance() {
        if (clipLayer == null) {
            clipLayer = new ClipLayer();
        }

        return clipLayer;
    }

    private int layoutType;

    public LiveData<List<AssetCommonBean>> loadData(final Context context,
                                                    final int assetId,
                                                    int counter,
                                                    final int assetType,
                                                    Map<String, MultilingualStringValueArray> map,
                                                    final int layout,
                                                    final int seriesMediaTyp) {

        layoutType = layout;
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();
        if (assetType == MediaTypeConstant.getClip()) {
            String refIdData = AssetContent.getParentRefIdData(map);
            getSeriesData(context, refIdData, connection);
        }
        return connection;

    }

    private void getSeriesData(Context context, String refIdData, MutableLiveData<List<AssetCommonBean>> connection) {
        final KsServices ksServices = new KsServices(context);
        ksServices.getSeriesFromClip(refIdData, asset -> getClipData(context, asset, ksServices, connection));
    }

    private String getRefIdFromSeries(Asset asset) {
        Map<String, MultilingualStringValueArray> map = asset.getTags();
        return AssetContent.getRefIdStrData(map);
    }

    private AssetCommonBean assetCommonBean;

    private void getClipData(Context context, final Asset asset, final KsServices ksServices, MutableLiveData<List<AssetCommonBean>> connection) {
        responseList = new ArrayList<>();
        assetCommonList = new ArrayList<>();
        assetCommonBean = new AssetCommonBean();
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {

                ksServices.getClipData(getRefIdFromSeries(asset), (status1, result) -> {
                    if (status1) {
                        assetCommonBean.setStatus(true);
                        parseClipAssests(context, result);
                        connection.postValue(assetCommonList);
                    } else {
                        assetCommonBean.setStatus(false);
                        assetCommonList.add(assetCommonBean);
                        connection.postValue(assetCommonList);
                    }

                });
            }
        });
    }

    private void parseClipAssests(Context context, Response<ListResponse<Asset>> list) {
        responseList.add(list);
        AssetCommonBean assetCommonBean = new AssetCommonBean();
        assetCommonBean.setStatus(true);
        assetCommonBean.setRailType(layoutType);
        assetCommonBean.setMoreID(1);
        assetCommonBean.setTitle(context.getResources().getString(R.string.clips));

        setRailData(context, responseList, assetCommonBean);

    }

    private void setRailData(Context context, List<Response<ListResponse<Asset>>> list, AssetCommonBean assetCommonBean) {

//        if (1 == 0) {
//
//        } else {
        int totalCount = list.get(0).results.getTotalCount();
        if (totalCount != 0) {
            List<RailCommonData> railList = new ArrayList<>();
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

                    AppCommonMethods.getImageList(context, AppLevelConstants.TYPE5, 0, j, k, list, assetCommonImages, imagesList);
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
            assetCommonBean.setRailAssetList(railList);
            assetCommonList.add(assetCommonBean);
        }
//        }
    }


}
