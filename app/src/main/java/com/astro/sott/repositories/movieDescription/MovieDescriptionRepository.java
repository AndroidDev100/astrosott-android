package com.astro.sott.repositories.movieDescription;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.baseModel.CategoryRails;
import com.astro.sott.baseModel.Watchlist;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.R;
import com.astro.sott.activities.login.ui.StartSessionLogin;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.networking.errorCallBack.ErrorCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovieDescriptionRepository {
    private static MovieDescriptionRepository movieDescriptionRepository;
    private List<AssetCommonBean> assetCommonList;
    private String genreskl;
    private List<Response<ListResponse<Asset>>> responseList;

    private MovieDescriptionRepository() {

    }

    public static MovieDescriptionRepository getInstance() {
        if (movieDescriptionRepository == null) {
            movieDescriptionRepository = new MovieDescriptionRepository();
        }

        return movieDescriptionRepository;
    }


    private int assetID = 0;
    private int assetTYPE = 0;

    public static void resetObject() {
        movieDescriptionRepository = null;
    }

    public LiveData<List<AssetCommonBean>> loadData(final Context context,
                                                    final int assetId,
                                                    int counter,
                                                    int assetType,
                                                    Map<String, MultilingualStringValueArray> map,
                                                    final int layoutType, final int screen_id, Asset asset) {
        assetID = assetId;
        assetTYPE = assetType;
        responseList = new ArrayList<>();
        genreskl = AppLevelConstants.KSQL_GENRE;
        final KsServices ksServices = new KsServices(context);
        ArrayList<MultilingualStringValue> genreskl_values = new ArrayList<>();
        MultilingualStringValueArray genreskl_list = map.get(AppLevelConstants.KEY_GENRE);

        if (genreskl_list != null)
            genreskl_values.addAll(genreskl_list.getObjects());
//            for (MultilingualStringValue value : genreskl_list.getObjects()) {
//                genreskl_values.add(value);
//            }

        genreSkl(genreskl_values);
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();

        ksServices.movieAssetListing(genreskl, assetType, assetId, counter, (status, listResponseResponse, channelList) -> {
            if (status) {
                responseList = listResponseResponse;
                callBelowData(context, connection, layoutType, screen_id);

            } else {
                callBelowData(context, connection, layoutType, screen_id);
            }

        });
        return connection;
    }

    private void genreSkl(List<MultilingualStringValue> genre_values) {
        StringBuilder stringBuilder = new StringBuilder(genreskl);
        for (int i = 0; i <= genre_values.size() - 1; i++) {
            if (i != genre_values.size() - 1) {
                stringBuilder.append(genre_values.get(i).getValue());
                stringBuilder.append(AppLevelConstants.KSQL_GENRE_END);
            } else {
                stringBuilder.append(genre_values.get(i).getValue());
                stringBuilder.append("'");
            }
        }
        genreskl = stringBuilder.toString();
    }

    private void callDynamicData(Context context, final List<Response<ListResponse<Asset>>> list, final List<VIUChannel> channelList, int layoutType) {
        assetCommonList = new ArrayList<>();
        for (int i = 0; i < responseList.size(); i++) {
            if (i == 0) {
                AssetCommonBean assetCommonBean = new AssetCommonBean();
                assetCommonBean.setStatus(true);
                assetCommonBean.setRailType(AppLevelConstants.Rail3);
                assetCommonBean.setMoreType(AppLevelConstants.YOU_MAY_LIKE);
                assetCommonBean.setMoreID(assetID);
                assetCommonBean.setMoreGenre(genreskl);
                assetCommonBean.setMoreAssetType(assetTYPE);
                if (channelList != null && channelList.size() > 0)
                    assetCommonBean.setID(channelList.get(i).getId());
                assetCommonBean.setTitle(context.getResources().getString(R.string.you_may_like));
                setRailData(context, responseList, assetCommonBean, i);
            } else {
                AssetCommonBean assetCommonBean = new AssetCommonBean();
                assetCommonBean.setStatus(true);
                assetCommonBean.setRailType(AppLevelConstants.Rail3);
                assetCommonBean.setMoreType(AppLevelConstants.SIMILAR_MOVIES);
                if (channelList != null && channelList.size() > 0)
                    assetCommonBean.setID(channelList.get(i).getId());
                assetCommonBean.setMoreID(assetID);
                assetCommonBean.setMoreGenre(genreskl);
                assetCommonBean.setMoreAssetType(assetTYPE);
                assetCommonBean.setTitle(context.getResources().getString(R.string.similar_movie));


                setRailData(context, responseList, assetCommonBean, i);

            }

        }
        if (channelList != null && channelList.size() > 0)
            new CategoryRails().setDescriptionRails(context, list, channelList, assetCommonList, 0);

    }


    private void setRailData(Context context, List<Response<ListResponse<Asset>>> list,
                             AssetCommonBean assetCommonBean, int position) {

//        if (1 == 0) {
//
//        } else {
        int totalCount = list.get(position).results.getTotalCount();
        if (totalCount != 0) {
            List<RailCommonData> railList = new ArrayList<>();
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
                    //imageLogic(tileType,position,j,k,list,assetCommonImages,imagesList);

                    AppCommonMethods.getImageList(context, AppLevelConstants.TYPE3, position, j, k, list, assetCommonImages, imagesList);
                }

                List<AssetCommonUrls> urlList = new ArrayList<>();
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
                railCommonData.setImages(imagesList);
                railCommonData.setUrls(urlList);
                railList.add(railCommonData);

            }
            assetCommonBean.setRailAssetList(railList);
            assetCommonList.add(assetCommonBean);
        }
//        }
    }


    private void callBelowData(Context context, final MutableLiveData<List<AssetCommonBean>> mutableLiveData, final int layoutType, final int screen_id) {

        final KsServices ksServices = new KsServices(context);
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {
                ksServices.callHomeChannels(context,0, screen_id, (status1, listResponseResponse, channelList) -> {
                    if (status1) {
                        callDynamicData(context, listResponseResponse, channelList, layoutType);
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

    public LiveData<String> getTrailorURL(Context applicationContext,
                                          String ref_id, int assetType) {
        final MutableLiveData<String> connection = new MutableLiveData<>();
        KsServices ksServices = new KsServices(applicationContext);
        ksServices.getTrailorURL(ref_id, assetType, (status, trailorURL) -> {
            if (status) {
                connection.postValue("");
            } else {
                connection.postValue(trailorURL);
            }

        });
        return connection;
    }

    public LiveData<Asset> getAssetFromTrailor(Context applicationContext, String ref_id) {
        final MutableLiveData<Asset> connection = new MutableLiveData<>();
        KsServices ksServices = new KsServices(applicationContext);
        ksServices.getAssetFromTrailor(ref_id, connection::postValue);
        return connection;
    }

    public LiveData<CommonResponse> compareWatchlist(final String assetId, final Context context) {
        final MutableLiveData<CommonResponse> commonMutableLiveData = new MutableLiveData<>();
        new Watchlist().listWatchlist(assetId, context, commonMutableLiveData);

        return commonMutableLiveData;
    }


    public LiveData<CommonResponse> addToWatchlist(String id, String titleName, Context context,int playlistidtype) {
        final MutableLiveData<CommonResponse> stringMutableLiveData = new MutableLiveData<>();
        new Watchlist().addToWatchList(id, titleName, context, stringMutableLiveData,playlistidtype);

        return stringMutableLiveData;
    }

    public LiveData<CommonResponse> deleteFromWatchlist(String assetId, Context mcontext) {
        final MutableLiveData<CommonResponse> booleanMutableLiveData = new MutableLiveData<>();
        final CommonResponse commonResponse = new CommonResponse();
        KsServices ksServices = new KsServices(mcontext);
        deleteWatchListItem(assetId, mcontext, booleanMutableLiveData, commonResponse, ksServices);
        return booleanMutableLiveData;
    }

    private void deleteWatchListItem(final String assetId, final Context mcontext,
                                     final MutableLiveData<CommonResponse> booleanMutableLiveData,
                                     final CommonResponse commonResponse, final KsServices ksServices) {
        ksServices.deleteFromWatchlist(assetId, (status, errorCode, message) -> {
            if (status) {
                commonResponse.setStatus(true);
                commonResponse.setErrorCode(errorCode);
                commonResponse.setMessage(message);
                booleanMutableLiveData.postValue(commonResponse);
            } else if (errorCode.equals(AppLevelConstants.KS_EXPIRE)) {
                new StartSessionLogin(mcontext).callUserLogin(KsPreferenceKey.getInstance(mcontext).getUser().getUsername(), "", (status1, apiType, list) -> {
                    if (status1) {
//                                PrintLogging.printLog(this.getClass(), "", "kalturaLogin" + status);
                        deleteWatchListItem(assetId, mcontext, booleanMutableLiveData, commonResponse, ksServices);
                    }
                });
            } else {
                commonResponse.setStatus(false);
                commonResponse.setErrorCode(errorCode);
                commonResponse.setMessage(new ErrorCallBack().ErrorMessage(errorCode,message));
                booleanMutableLiveData.postValue(commonResponse);
            }


        });


    }
}

