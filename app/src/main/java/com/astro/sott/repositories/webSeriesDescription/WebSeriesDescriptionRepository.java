package com.astro.sott.repositories.webSeriesDescription;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.util.Log;

import com.astro.sott.baseModel.CategoryRails;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.R;
import com.astro.sott.activities.login.ui.KalturaLogin;
import com.astro.sott.activities.login.ui.StartSessionLogin;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.networking.errorCallBack.ErrorCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.gson.Gson;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.FollowTvSeries;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebSeriesDescriptionRepository {
    private static WebSeriesDescriptionRepository webSeriesDescriptionRepository;
    private List<AssetCommonBean> assetCommonList;
    private String seriesId;
    private String tileType;
    private int seriesNumber = 1;
    private String matched_string = "";
    private int seriesMediaType = 0;
    private List<Integer> seasonNumberList;
    private List<Response<ListResponse<Asset>>> responseList;
    private List<Response<ListResponse<Asset>>> clipList;

    private WebSeriesDescriptionRepository() {

    }

    public static WebSeriesDescriptionRepository getInstance() {
        if (webSeriesDescriptionRepository == null) {
            webSeriesDescriptionRepository = new WebSeriesDescriptionRepository();
        }

        return webSeriesDescriptionRepository;
    }


    public LiveData<List<AssetCommonBean>> loadData(final Context context,
                                                    final int assetId,
                                                    int counter,
                                                    final int assetType,
                                                    Map<String, MultilingualStringValueArray> map, final int layoutType, final Asset asset) {
        seriesMediaType = assetType;
        seriesNumber = 1;
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();
        seriesId = AssetContent.getSeriesId(asset.getMetas());
        final KsServices ksServices = new KsServices(context);
        responseList = new ArrayList<>();
        clipList = new ArrayList<>();
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {
                ksServices.callSeasons(0, seriesId, assetType, (status1, result) -> {
                    if (status1) {
                        Log.e("STATUS", String.valueOf(status1));
                        Gson gson = new Gson();
                        Log.e("SEASONS", gson.toJson(seasonNumberList));
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
            Log.e("STATUS", String.valueOf(status));
            responseList = listResponseResponse;
            callBelowData(context, connection, layoutType, assetType);
        });
    }

    private void callDynamicData(Context context, final List<Response<ListResponse<Asset>>> list, final List<VIUChannel> channelList, int layoutType) {
        assetCommonList = new ArrayList<>();

        for (int i = 0; i < responseList.size(); i++) {
            AssetCommonBean assetCommonBean = new AssetCommonBean();
            assetCommonBean.setStatus(true);
            assetCommonBean.setRailType(AppLevelConstants.Rail5);
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

    private void setRailData(Context context, List<Response<ListResponse<Asset>>> list,
                             AssetCommonBean assetCommonBean, int position) {

        List<RailCommonData> railList = new ArrayList<>();
        for (int j = 0; j < list.get(position).results.getObjects().size(); j++) {
            RailCommonData railCommonData = new RailCommonData();
            // railCommonData.setCatchUpBuffer(list.get(position).results.getObjects().get(j).getEnableCatchUp());
            railCommonData.setType(list.get(position).results.getObjects().get(j).getType());
            railCommonData.setName(list.get(position).results.getObjects().get(j).getName());
            railCommonData.setId(list.get(position).results.getObjects().get(j).getId());
            railCommonData.setObject(list.get(position).results.getObjects().get(j));
            //PrintLogging.printLog("","seriesMediaType"+seriesMediaType);
            railCommonData.setSeriesType(seriesMediaType);

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
        assetCommonBean.setRailAssetList(railList);
        assetCommonList.add(assetCommonBean);
    }


    private void callBelowData(Context context, final MutableLiveData<List<AssetCommonBean>> mutableLiveData,
                               final int layoutType, final int assetType) {

        final KsServices ksServices = new KsServices(context);
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {
                if (assetType == MediaTypeConstant.getDrama(context)) {
                    ksServices.callHomeChannels(context, 0, AppLevelConstants.TAB_DRAMA_DETAILS, (status12, listResponseResponse, channelList) -> {
                        if (status12) {
                            callDynamicData(context, listResponseResponse, channelList, layoutType);
                            mutableLiveData.postValue(assetCommonList);
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


    public LiveData<RailCommonData> getClipData(Context context, final String ref_id) {
        final KsServices ksServices = new KsServices(context);
        MutableLiveData<RailCommonData> liveData = new MutableLiveData<>();
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {

                ksServices.getClipData(ref_id, (status1, result) -> clipList.add(result));
            }
        });

        return liveData;

    }

    public LiveData<String> seriesFollowList(final Context context, final long assetID) {
        final MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        ksServices.checkSeriesList((status, errorCode, message, result) -> {
                    if (status) {
                        checkAssetAdded(result, stringMutableLiveData, assetID);
                    } else {
                        if (KsPreferenceKey.getInstance(context).getUser().getUsername() != null) {
                            new KalturaLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status1, apiType, list) -> {
                                if (status1) {
//                                PrintLogging.printLog(this.getClass(), "", "kalturaLogin" + status);
                                    seriesFollowList(context, assetID);
                                }
                            });
                        }
                    }


                }
        );
        return stringMutableLiveData;
    }

    public LiveData<String> getEpisodeToPlay(final Context context, final long assetID) {
        final MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        ksServices.getEpisodeToPlay(assetID, status -> {
            if (status) {

            }

        });
        return stringMutableLiveData;
    }

    private void checkAssetAdded(
            Response<ListResponse<FollowTvSeries>> result,
            MutableLiveData<String> stringMutableLiveData, long asset_id) {

        String matched_string = "";
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
//                                PrintLogging.printLog(this.getClass(), "", "kalturaLogin" + status);
                                followSeries(context, assetId, stringMutableLiveData, commonResponse, ksServices);
                            }
                        });
                    } else if (id.equals("")) {
                        commonResponse.setStatus(false);
                        commonResponse.setErrorCode(errorCode);
                        commonResponse.setMessage(new ErrorCallBack().ErrorMessage(errorCode, message));
                        stringMutableLiveData.postValue(commonResponse);
                    } else {
                        commonResponse.setStatus(true);
                        commonResponse.setAssetID(id);
                        stringMutableLiveData.postValue(commonResponse);
                    }

                }
        );

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
                    new KalturaLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status1, apiType, list) -> {
                        if (status1) {
//                                PrintLogging.printLog(this.getClass(), "", "kalturaLogin" + status);
                            deleteSeriesData(assetId, context, commonResponse, booleanMutableLiveData, ksServices);
                        }
                    });

                } else {
                    commonResponse.setStatus(false);
                    commonResponse.setErrorCode(new ErrorCallBack().ErrorMessage(errorCode, message));
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
        PrintLogging.printLog(this.getClass(), "", "forNumberOfEpisode" + seriesId + " " + mediaType);
        if (seriesId.equalsIgnoreCase("")) {
            commonResponse.setStatus(true);
            commonResponse.setTotalEpisodes(0);
            mutableLiveData.postValue(commonResponse);
        } else {
            ksServices.callNumberOfEpisodes(seriesId, mediaType, (status, errorCode, message, assetList) -> {
                Log.e("TOTAL EPISODES", String.valueOf(assetList.results.getTotalCount()));
                if (status) {
                    if (assetList.results.getTotalCount() >= 0) {
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
}
