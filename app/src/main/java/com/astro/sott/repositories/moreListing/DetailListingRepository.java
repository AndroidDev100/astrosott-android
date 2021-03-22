package com.astro.sott.repositories.moreListing;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.callBacks.kalturaCallBacks.SimilarMovieCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;

public class DetailListingRepository {
    private static DetailListingRepository projectRepository;
    List assetList;
    private ArrayList<RailCommonData> railList = new ArrayList<>();
    private ArrayList<RailCommonData> mainList = new ArrayList<>();

    private String genreskl;
    private List<Response<ListResponse<Asset>>> liveChannelList;
    private List<Response<ListResponse<Asset>>> similarChannelList;

    private DetailListingRepository() {

    }

    public static DetailListingRepository getInstance() {
        if (projectRepository == null) {
            projectRepository = new DetailListingRepository();
        }
        return projectRepository;
    }

    public LiveData<List<RailCommonData>> loadData(Context context, int assetId, int counter, final String layout, boolean isScrolling) {
        if (isScrolling == false) {
            railList.clear();
        }

        final KsServices ksServices = new KsServices(context);
        final MutableLiveData<List<RailCommonData>> connection = new MutableLiveData<>();
        List<VIUChannel> list = new ArrayList<>();
        VIUChannel channel = new VIUChannel();
        channel.setId((long) assetId);
        list.add(channel);
        ksServices.callAssetListing((long) 1233, list, counter, (status, listResponseResponse, channelList) -> {
            if (status == true) {
                // PrintLogging.printLog("totalCount");
                callDynamicData(context, layout, listResponseResponse, 0);
                mainList=railList;
                connection.postValue(mainList);
            } else {
                errorHandling();
                connection.postValue(mainList);
            }
        });
        return connection;
    }

    private void errorHandling() {
        railList = new ArrayList<>();
        RailCommonData assetCommonBean = new RailCommonData();
        assetCommonBean.setStatus(false);
        railList.add(assetCommonBean);

    }

    private void callDynamicData(Context context, String layout, List<Response<ListResponse<Asset>>> list, int position) {
        if (list.get(position).results.getObjects() != null) {

            for (int j = 0; j < list.get(position).results.getObjects().size(); j++) {
                RailCommonData railCommonData = new RailCommonData();
                railCommonData.setTotalCount(list.get(position).results.getTotalCount());
                railCommonData.setStatus(true);
                // railCommonData.setCatchUpBuffer(list.get(position).results.getObjects().get(j).getEnableCatchUp());
                railCommonData.setType(list.get(position).results.getObjects().get(j).getType());
                railCommonData.setName(list.get(position).results.getObjects().get(j).getName());
                railCommonData.setId(list.get(position).results.getObjects().get(j).getId());
                railCommonData.setObject(list.get(position).results.getObjects().get(j));
                int ugcCreator = list.get(position).results.getObjects().get(j).getType();

                List<AssetCommonImages> imagesList = new ArrayList<>();

           /* if (ugcCreator == MediaTypeConstant.getUGCCreator()){
                railCommonData.setCreatorName(AppCommonMethods.getCteatorName(list.get(position).results.getObjects().get(j).getName()));
                // AppCommonMethods.getImageList(tileType,position,j,k,list,assetCommonImages,imagesList);
            }else {*/
                for (int k = 0; k < list.get(position).results.getObjects().get(j).getImages().size(); k++) {

                    AssetCommonImages assetCommonImages = new AssetCommonImages();
                    //imageLogic(tileType,position,j,k,list,assetCommonImages,imagesList);
                           /* if (ugcCreator == MediaTypeConstant.getUGCCreator()){
                                railCommonData.setCreatorName(AppCommonMethods.getCteatorName(list.get(position).results.getObjects().get(j).getName()));
                                // AppCommonMethods.getImageList(tileType,position,j,k,list,assetCommonImages,imagesList);
                            }else {

                            }*/
                    AppCommonMethods.getImageList(context, layout, position, j, k, list, assetCommonImages, imagesList);
                    //  AppCommonMethods.getImageList(tileType,position,j,k,list,assetCommonImages,imagesList);
                }
                /* }*/

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

                List<AssetCommonUrls> urlList = new ArrayList<>();
                if (list.get(position).results.getObjects().get(j).getType() == MediaTypeConstant.getProgram(context)) {

                } else {


                    for (int k = 0; k < list.get(position).results.getObjects().get(j).getMediaFiles().size(); k++) {
                        AssetCommonUrls assetCommonUrls = new AssetCommonUrls();
                        assetCommonUrls.setUrl(list.get(position).results.getObjects().get(j).getMediaFiles().get(k).getUrl());
                        assetCommonUrls.setUrlType(list.get(position).results.getObjects().get(j).getMediaFiles().get(k).getType());
                        assetCommonUrls.setDuration(AppCommonMethods.getDuration(list, position, j, k));
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
    }

    public LiveData<List<RailCommonData>> loadSimillarMovieData(Context context, AssetCommonBean assetCommonBean, Context applicationContext,
                                                                Asset asset, final String layout, int listingType, boolean isScrolling, int counter) {
        if (isScrolling == false) {
            railList.clear();
        }

        final MutableLiveData<List<RailCommonData>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(applicationContext);
        if (listingType == 3) {
            List<Integer> seasonNumberList = new ArrayList<>();
            seasonNumberList.add(assetCommonBean.getMoreID());
            ksServices.callSeasonEpisodes(counter, assetCommonBean.getMoreSeriesID(), MediaTypeConstant.getWebEpisode(context), seasonNumberList, 0,"", new SimilarMovieCallBack() {
                @Override
                public void response(boolean status, CommonResponse commonResponse) {
                    if (commonResponse.getStatus()) {
                        // PrintLogging.printLog("totalCount");
                        assetList = new ArrayList<>();
                        assetList.add(commonResponse.getAssetList());
                        callDynamicData(context, layout, assetList, 0);
                        connection.postValue(railList);
                    } else {
                        errorHandling();
                        connection.postValue(railList);
                    }
                }
            });
        } else if (listingType == 7) {
            liveChannelList = new ArrayList<>();
            ksServices.callLiveNowRail(counter, (status, result) -> {
                if (status) {
                    liveChannelList.add(result);
                    callDynamicData(context, layout, liveChannelList, 0);
                    connection.postValue(railList);
                } else {
                    errorHandling();
                    connection.postValue(railList);
                }
            });


        } else if (listingType == 8) {
            similarChannelList = new ArrayList<>();
            ksServices.getSimilarChannel(counter, assetCommonBean.getMoreAssetType(), assetCommonBean.getMoreSeriesID(), (status, result) -> {
                if (status) {
                    similarChannelList.add(result);
                    callDynamicData(context, layout, similarChannelList, 0);
                    connection.postValue(railList);
                } else {
                    errorHandling();
                    connection.postValue(railList);
                }
            });

        } else {
            genreskl = AppLevelConstants.KSQL_GENRE;
            List<MultilingualStringValue> genreskl_values = new ArrayList<>();
            MultilingualStringValueArray genreskl_list = asset.getTags().get(AppLevelConstants.KEY_GENRE);

            if (genreskl_list != null)
                genreskl_values.addAll(genreskl_list.getObjects());

            genreSkl(genreskl_values);
            ksServices.similarMovieListing(assetCommonBean.getMoreGenre(), assetCommonBean.getMoreAssetType(), assetCommonBean.getMoreID(), counter, listingType, (status, listResponseResponse, channelList) -> {
                if (status) {
                    callDynamicData(context, layout, listResponseResponse, 0);
                    connection.postValue(railList);
                } else {
                    errorHandling();
                    connection.postValue(railList);
                }

            });
        }

        return connection;

    }

    private String genreSkl(List<MultilingualStringValue> genre_values) {
        for (int i = 0; i <= genre_values.size() - 1; i++) {
            if (i != genre_values.size() - 1)
                genreskl = genreskl + genre_values.get(i).getValue() + AppLevelConstants.KSQL_GENRE_END + "";
            else
                genreskl = genreskl + genre_values.get(i).getValue() + "'" + "";
        }

        return genreskl;
    }
}



