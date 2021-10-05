package com.astro.sott.baseModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;

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

    private void callDynamicData(Context context,String layout, List<Response<ListResponse<Asset>>> list) {
        try {

            if (list != null) {

                for (int j = 0; j < list.get(0).results.getObjects().size(); j++) {
                    RailCommonData railCommonData = new RailCommonData();
                    railCommonData.setTotalCount(list.get(0).results.getTotalCount());
                    railCommonData.setStatus(true);
//            railCommonData.setCatchUpBuffer(list.get(0).results.getObjects().get(j).getEnableCatchUp());
                    railCommonData.setType(list.get(0).results.getObjects().get(j).getType());
                    railCommonData.setName(list.get(0).results.getObjects().get(j).getName());
                    railCommonData.setId(list.get(0).results.getObjects().get(j).getId());
                    railCommonData.setObject(list.get(0).results.getObjects().get(j));
                    int ugcCreator = list.get(0).results.getObjects().get(j).getType();

                    List<AssetCommonImages> imagesList = new ArrayList<AssetCommonImages>();

                    if (ugcCreator == MediaTypeConstant.getUGCCreator(context)) {
                        railCommonData.setCreatorName(AppCommonMethods.getCteatorName(list.get(0).results.getObjects().get(j).getName()));
                        // AppCommonMethods.getImageList(tileType,position,j,k,list,assetCommonImages,imagesList);
                    } else {
                        for (int k = 0; k < list.get(0).results.getObjects().get(j).getImages().size(); k++) {

                            AssetCommonImages assetCommonImages = new AssetCommonImages();
                            //imageLogic(tileType,position,j,k,list,assetCommonImages,imagesList);
                           /* if (ugcCreator == MediaTypeConstant.getUGCCreator()){
                                railCommonData.setCreatorName(AppCommonMethods.getCteatorName(list.get(position).results.getObjects().get(j).getName()));
                                // AppCommonMethods.getImageList(tileType,position,j,k,list,assetCommonImages,imagesList);
                            }else {

                            }*/
                            AppCommonMethods.getImageList(context,layout, 0, j, k, list, assetCommonImages, imagesList);
                            //  AppCommonMethods.getImageList(tileType,position,j,k,list,assetCommonImages,imagesList);
                        }
                    }

           /* for (int k = 0; k < list.get(position).results.getObjects().get(j).getImages().size(); k++) {

                AssetCommonImages assetCommonImages = new AssetCommonImages();
                //imageLogic(tileType,position,j,k,list,assetCommonImages,imagesList);

                if (ugcCreator == MediaTypeConstant.getUGCCreator()){
                    railCommonData.setCreatorName(AppCommonMethods.getCteatorName(list.get(position).results.getObjects().get(j).getName()));
                    // AppCommonMethods.getImageList(tileType,position,j,k,list,assetCommonImages,imagesList);
                }else {
                    AppCommonMethods.getImageList(layout, position, j, k, list, assetCommonImages, imagesList);
                }


            }*/

                    List<AssetCommonUrls> urlList = new ArrayList<AssetCommonUrls>();
                    if (list.get(0).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context)) {

                    } else {


                        for (int k = 0; k < list.get(0).results.getObjects().get(j).getMediaFiles().size(); k++) {
                            AssetCommonUrls assetCommonUrls = new AssetCommonUrls();
                            assetCommonUrls.setUrl(list.get(0).results.getObjects().get(j).getMediaFiles().get(k).getUrl());
                            assetCommonUrls.setUrlType(list.get(0).results.getObjects().get(j).getMediaFiles().get(k).getType());
                            assetCommonUrls.setDuration(AppCommonMethods.getDuration(list, 0, j, k));
                       /* if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("16:9")){
                            Log.w("sliderLoop",list.get(position).results.getObjects().get(j).getImages().get(k).getUrl());
                            assetCommonImages.setImage16By9(list.get(position).results.getObjects().get(j).getImages().get(k).getUrl());
                        }
                        else if (list.get(position).results.getObjects().get(j).getImages().get(k).getRatio().equals("2:3")){
                            Log.w("sliderLoop",list.get(position).results.getObjects().get(j).getImages().get(k).getUrl());
                            assetCommonImages.setImage2by3(list.get(position).results.getObjects().get(j).getImages().get(k).getUrl());
                        }*/
                            urlList.add(assetCommonUrls);
                        }
                    }
                    railCommonData.setImages(imagesList);
                    railCommonData.setUrls(urlList);
                    railList.add(railCommonData);

                }
            }
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }
    }



    public LiveData<List<RailCommonData>> loadSearchedData(Context context, int assetId,String filterValue, List<String> genreList, int counter, final String layout, boolean isScrolling,int pageSize) {
        if (!isScrolling) {
            railList.clear();
        }

        final KsServices ksServices = new KsServices(context);
        final MutableLiveData<List<RailCommonData>> connection = new MutableLiveData<>();
        List<VIUChannel> list = new ArrayList<>();
        VIUChannel channel = new VIUChannel();
        channel.setId((long) assetId);

        ksql =AppCommonMethods.getDeepSearchKsql("",null,1,context);
       /* if(genreList.size()>0) {

            ksql = AssetContent.getGenredata(genreList);
        }else {
            ksql = "Genre = ''";
        }*/
        // DTChannel channel = new DTChannel((long) assetId,"","");
        list.add(channel);
        ksServices.callDeepSearchAssetListing(context,(long) 1233, list,ksql, filterValue, counter,pageSize, (status, listResponseResponse, channelList) -> {
            if (status) {
                // PrintLogging.printLog("totalCount");
                callDynamicData(context,layout, listResponseResponse);
                connection.postValue(railList);
            } else {
                errorHandling();
                connection.postValue(railList);
            }
        });
        return connection;
    }
}
