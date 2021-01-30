package com.astro.sott.repositories.webEpisodeDescription;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.baseModel.CategoryRails;
import com.astro.sott.baseModel.Watchlist;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.networking.errorCallBack.ErrorCallBack;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.R;
import com.astro.sott.activities.login.ui.StartSessionLogin;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.FollowTvSeries;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebEpisodeDescriptionRepository {
    private static WebEpisodeDescriptionRepository webEpisodeDescriptionRepository;
    private List<AssetCommonBean> assetCommonList;
    private List<Response<ListResponse<Asset>>> responseList;
    private List<Response<ListResponse<Asset>>> clipList;
    private String seriesId;
    private List<Integer> seasonNumberList;
    private int seriesNumber = 1;
    private String tileType;
    private String results = "";
    private int assetType;
    private int seriesMediaType;

    private WebEpisodeDescriptionRepository() {

    }

    public static WebEpisodeDescriptionRepository getInstance() {
        if (webEpisodeDescriptionRepository == null) {
            webEpisodeDescriptionRepository = new WebEpisodeDescriptionRepository();
        }

        return webEpisodeDescriptionRepository;
    }


    public LiveData<List<AssetCommonBean>> loadData(final Context context,
                                                    final int assetId,
                                                    int counter,
                                                    final int assetType,
                                                    Map<String, MultilingualStringValueArray> map, final int layoutType, final Asset asset) {

        seriesMediaType = assetType;
        //PrintLogging.printLog("","valueAssetTye"+seriesMediaType);
        seriesNumber = 1;
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();
        seriesId = AssetContent.getSeriesId(asset.getMetas());
        final KsServices ksServices = new KsServices(context);
        responseList = new ArrayList<>();
        clipList = new ArrayList<>();
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {
                ksServices.callSeasons(0, seriesId, assetType, (status1, result) -> {
                    // boolean cStatus = checkStatus(result);

                    if (status1) {
                        seasonNumberList = AssetContent.getSeasonNumber(result);
                        callSeasonEpisodes(context, seasonNumberList, ksServices, connection, layoutType, assetType);
                    } else {
                        callBelowData(context, connection, layoutType, assetType);
                    }
                });
            }


        });

        //callBelowData(connection,layoutType);

        return connection;
    }

    private void callSeasonEpisodes(Context context, List<Integer> seasonNumberList,
                                    KsServices ksServices,
                                    final MutableLiveData<List<AssetCommonBean>> connection,
                                    final int layoutType, final int assetType) {

        ksServices.callSesionEpisode(1, seriesId, seriesMediaType, seasonNumberList, (status, listResponseResponse, channelList) -> {
            responseList = listResponseResponse;
            callBelowData(context, connection, layoutType, assetType);
        });
    }


//    private boolean checkStatus(Response<ListResponse<Asset>> result) {
//        if (result != null) {
//            if (result.isSuccess()) {
//                if (result.results != null) {
//                    return true;
//                }
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//        return false;
//    }


    private void callDynamicData(Context context, final List<Response<ListResponse<Asset>>> list, final List<VIUChannel> channelList, int layoutType) {
        assetCommonList = new ArrayList<>();

        for (int i = 0; i < responseList.size(); i++) {
            AssetCommonBean assetCommonBean = new AssetCommonBean();
            assetCommonBean.setStatus(true);
            assetCommonBean.setRailType(AppLevelConstants.Rail5);
            if (seriesMediaType == MediaTypeConstant.getDrama(context)) {
                assetCommonBean.setMoreType(AppLevelConstants.WEB_EPISODE);
            } else {
                assetCommonBean.setMoreType(AppLevelConstants.SPOTLIGHT_EPISODE);
            }

            assetCommonBean.setMoreAssetType(seriesMediaType);
            assetCommonBean.setMoreSeriesID(seriesId);
            assetCommonBean.setMoreID(seasonNumberList.get(i));
            if (channelList != null && channelList.size() > 0) {

                assetCommonBean.setID(channelList.get(i).getId());
            }
            int seriesNumber2 = AssetContent.getSeriesNumber(responseList.get(i).results.getObjects().get(0).getMetas());
            if (seriesNumber2 == -1) {
                seriesNumber = i + 1;
            } else {
                seriesNumber = seriesNumber2;
            }
            // responseList.get(i).results.get
            // seriesNumber = i + 1;

            assetCommonBean.setTitle(context.getResources().getString(R.string.season) + " " + seriesNumber);

            setRailData(context, responseList, assetCommonBean, i);

        }

        if (clipList.size() > 0) {
            for (int i = 0; i < clipList.size(); i++) {
                AssetCommonBean assetCommonBean = new AssetCommonBean();
                assetCommonBean.setStatus(true);
                assetCommonBean.setRailType(AppLevelConstants.Rail5);
                assetCommonBean.setMoreType(AppLevelConstants.CATEGORY);
                seriesNumber = seriesNumber + i;
                assetCommonBean.setTitle(context.getResources().getString(R.string.clips));

                setRailData(context, clipList, assetCommonBean, i);

            }

        }
        if (channelList != null && channelList.size() > 0)
            new CategoryRails().setDescriptionRails(context, list, channelList, assetCommonList, 0);

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

    private void setRailData(Context context, List<Response<ListResponse<Asset>>> list, AssetCommonBean assetCommonBean, int position) {

//        if (1 == 0) {
//
//        } else {
        if (list.get(position).results != null) {
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
                    railCommonData.setSeriesType(seriesMediaType);

                    List<AssetCommonImages> imagesList = new ArrayList<>();
                    for (int k = 0; k < list.get(position).results.getObjects().get(j).getImages().size(); k++) {

                        AssetCommonImages assetCommonImages = new AssetCommonImages();
                        //imageLogic(tileType,position,j,k,list,assetCommonImages,imagesList);

                        AppCommonMethods.getImageList(context, AppLevelConstants.TYPE5, position, j, k, list, assetCommonImages, imagesList);
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
                    assetCommonBean.setTotalCount(list.get(position).results.getTotalCount());
                }
                assetCommonBean.setRailAssetList(railList);
                assetCommonList.add(assetCommonBean);
            }
        }


//        }
    }


    private void callBelowData(Context context, final MutableLiveData<List<AssetCommonBean>> mutableLiveData,
                               final int layoutType, final int assetType) {

        final KsServices ksServices = new KsServices(context);
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {
                if (assetType == MediaTypeConstant.getDrama(context)) {
                    ksServices.callHomeChannels(context,0, AppLevelConstants.TAB_DRAMA_DETAILS, (status1, listResponseResponse, channelList) -> {
                        if (status1) {
                            try {
                                callDynamicData(context, listResponseResponse, channelList, layoutType);
                                mutableLiveData.postValue(assetCommonList);
                            } catch (Exception e) {
                                PrintLogging.printLog("Exception", "", "" + e);
                            }

                        } else {

                            errorHandling();
                        }

                    });
                }

            }
        });
    }

    private void errorHandling() {
        //  Log.e(this.getClass().getSimpleName(), "ERROR HANDLING");
        assetCommonList = new ArrayList<>();
        AssetCommonBean assetCommonBean = new AssetCommonBean();
        assetCommonBean.setStatus(false);
        assetCommonList.add(assetCommonBean);
    }


//    public LiveData<RailCommonData> getClipData(Context applicationContext, final String ref_id) {
//        final KsServices ksServices = new KsServices(context);
//        MutableLiveData<RailCommonData> liveData = new MutableLiveData<>();
//        AppCommonMethods.checkDMS(context, status -> {
//            if (status) {
//
//                ksServices.getClipData(ref_id, new SeasonCallBack() {
//                    @Override
//                    public void result(boolean status, Response<ListResponse<Asset>> result) {
//                        clipList.add(result);
//                    }
//                });
//            }
//        });
//
//        return liveData;
//
//    }

//    public LiveData<String> seriesFollowList(final Context application, final long assetID) {
//        context = application;
//        final MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
//        KsServices ksServices = new KsServices(context);
//        ksServices.checkSeriesList((status, errorCode, message, result) -> {
//            if (status) {
//                checkAssetAdded(status, result, stringMutableLiveData, assetID);
//            } else {
//                new KalturaLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status1, apiType, list) -> {
//                    if (status1) {
////                                PrintLogging.printLog(this.getClass(), "", "kalturaLogin" + status);
//                        seriesFollowList(application, assetID);
//
//                    }
//                });
//            }
//
//
//        });
//
//        return stringMutableLiveData;
//    }

    private void checkAssetAdded(Boolean status,
                                 Response<ListResponse<FollowTvSeries>> result,
                                 MutableLiveData<String> stringMutableLiveData, long asset_id) {

        String matched_string = "";
        if (status) {
            if (result.results.getTotalCount() > 0) {
                for (int i = 0; i < result.results.getObjects().size(); i++) {
                    long assetID = result.results.getObjects().get(i).getAssetId();
                    if (String.valueOf(assetID).equals(String.valueOf(asset_id))) {
                        matched_string = String.valueOf(assetID);
                    }
                }
                stringMutableLiveData.postValue(matched_string);
            } else {
                stringMutableLiveData.postValue("");
            }
        } else {
            stringMutableLiveData.postValue("");
        }
    }

    public LiveData<CommonResponse> addToFollowlist(long assetId, Context context) {
        final MutableLiveData<CommonResponse> stringMutableLiveData = new MutableLiveData<>();
        final CommonResponse commonResponse = new CommonResponse();
        KsServices ksServices = new KsServices(context);
        followSeries(context, assetId, stringMutableLiveData, commonResponse, ksServices);

        return stringMutableLiveData;
    }

    private void followSeries(Context context, final long assetId,
                              final MutableLiveData<CommonResponse> stringMutableLiveData,
                              final CommonResponse commonResponse, final KsServices ksServices) {
        ksServices.addToFollowlist(String.valueOf(assetId), (id, errorCode, message) -> {
            if (errorCode.equals(AppLevelConstants.KS_EXPIRE)) {
                new StartSessionLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status, apiType, list) -> {
                    if (status) {
                        followSeries(context, assetId, stringMutableLiveData, commonResponse, ksServices);
                    }
                });
            } else if (id.equals("")) {
                commonResponse.setStatus(false);
                commonResponse.setErrorCode(errorCode);
                commonResponse.setMessage(message);
                stringMutableLiveData.postValue(commonResponse);
            } else {
                commonResponse.setStatus(true);
                commonResponse.setAssetID(id);
                stringMutableLiveData.postValue(commonResponse);
            }

        });

    }

    public LiveData<CommonResponse> deleteFromWatchlist(long assetId, Context mcontext) {
        final MutableLiveData<CommonResponse> booleanMutableLiveData = new MutableLiveData<>();
        final CommonResponse commonResponse = new CommonResponse();
        KsServices ksServices = new KsServices(mcontext);
        deleteSeriesData(assetId, mcontext, commonResponse, booleanMutableLiveData, ksServices);
        return booleanMutableLiveData;
    }

    private void deleteSeriesData(final long assetId, final Context context, final CommonResponse commonResponse,
                                  final MutableLiveData<CommonResponse> booleanMutableLiveData,
                                  final KsServices ksServices) {
        ksServices.deleteSeriesAsset(String.valueOf(assetId), (status, errorCode, message) -> {
            if (status) {
                commonResponse.setStatus(true);
                commonResponse.setErrorCode(errorCode);
                commonResponse.setMessage(message);
                booleanMutableLiveData.postValue(commonResponse);
            } else {
                if (errorCode.equalsIgnoreCase("")) {
                    commonResponse.setStatus(false);
                    commonResponse.setErrorCode(errorCode);
                    commonResponse.setMessage(message);
                    booleanMutableLiveData.postValue(commonResponse);
                } else if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                    new StartSessionLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status1, apiType, list) -> {
                        if (status1) {
                            deleteSeriesData(assetId, context, commonResponse, booleanMutableLiveData, ksServices);
                        }
                    });
                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setErrorCode(errorCode);
                    commonResponse.setMessage(message);
                    booleanMutableLiveData.postValue(commonResponse);
                }


            }


        });

    }

    public LiveData<CommonResponse> getNumberOfEpisode(Asset asset, Context context) {
        final MutableLiveData<CommonResponse> mutableLiveData = new MutableLiveData<>();
        final CommonResponse commonResponse = new CommonResponse();
        KsServices ksServices = new KsServices(context);
        int mediaType = asset.getType();
        if (mediaType == MediaTypeConstant.getDrama(context)) {
            mediaType = MediaTypeConstant.getWebEpisode(context);
        }
        String seriesId = AssetContent.getSeriesId(asset.getMetas());
        if (!seriesId.equalsIgnoreCase("")) {
            ksServices.callNumberOfEpisodes(seriesId, mediaType, (status, errorCode, message, assetList) -> {
                if (status) {
                    if (assetList.results.getTotalCount() > 0) {
                        commonResponse.setStatus(true);
                        commonResponse.setTotalEpisodes(assetList.results.getTotalCount());
                        mutableLiveData.postValue(commonResponse);
                    } else {
                        commonResponse.setStatus(false);
                        mutableLiveData.postValue(commonResponse);
                    }

                } else {
                    commonResponse.setStatus(false);
                    mutableLiveData.postValue(commonResponse);
                }
            });
        }


        return mutableLiveData;
    }

    public LiveData<Asset> getAssetFromClip(Context applicationContext, String ref_id) {
        final MutableLiveData<Asset> connection = new MutableLiveData<>();
        KsServices ksServices = new KsServices(applicationContext);
        ksServices.getAssetFromTrailor(ref_id, connection::postValue);
        return connection;
    }

    public LiveData<CommonResponse> compareWatchlist(final String assetId, final Context context) {
        final MutableLiveData<CommonResponse> commonMutableLiveData = new MutableLiveData<>();
        new Watchlist().checkWatchlist(assetId, context, commonMutableLiveData);

        return commonMutableLiveData;
    }

    public LiveData<CommonResponse> addToWatchlist(String id, String titleName, Context context, int playlistId) {
        final MutableLiveData<CommonResponse> stringMutableLiveData = new MutableLiveData<>();
        new Watchlist().addToWatchList(id, titleName, context, stringMutableLiveData,playlistId);

        return stringMutableLiveData;
    }

    public LiveData<CommonResponse> deleteFromWatchlist(String assetId, Context context) {
        final MutableLiveData<CommonResponse> booleanMutableLiveData = new MutableLiveData<>();
        final CommonResponse commonResponse = new CommonResponse();
        KsServices ksServices = new KsServices(context);
        deleteWatchListItem(assetId, context, booleanMutableLiveData, commonResponse, ksServices);
        return booleanMutableLiveData;
    }

    private void deleteWatchListItem(final String assetId, final Context context,
                                     final MutableLiveData<CommonResponse> booleanMutableLiveData,
                                     final CommonResponse commonResponse, final KsServices ksServices) {
        ksServices.deleteFromWatchlist(assetId, (status, errorCode, message) -> {
            if (status) {
                commonResponse.setStatus(true);
                commonResponse.setErrorCode(errorCode);
                commonResponse.setMessage(message);
                booleanMutableLiveData.postValue(commonResponse);
            } else if (errorCode.equals(AppLevelConstants.KS_EXPIRE)) {
                new StartSessionLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status1, apiType, list) -> {
                    if (status1) {
//                                PrintLogging.printLog(this.getClass(), "", "kalturaLogin" + status);
                        deleteWatchListItem(assetId, context, booleanMutableLiveData, commonResponse, ksServices);
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