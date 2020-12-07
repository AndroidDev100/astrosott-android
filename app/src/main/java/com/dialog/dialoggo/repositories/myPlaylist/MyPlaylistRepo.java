package com.dialog.dialoggo.repositories.myPlaylist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.dialog.dialoggo.activities.login.ui.StartSessionLogin;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonImages;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonUrls;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.networking.errorCallBack.ErrorCallBack;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;

public class MyPlaylistRepo {


    private static MyPlaylistRepo myWatchlistRepository;
    private  ArrayList<RailCommonData> railList = new ArrayList<>();

    private MyPlaylistRepo() {

    }

    public static MyPlaylistRepo getInstance() {
       /* if (myWatchlistRepository == null) {
            myWatchlistRepository = new MyPlaylistRepo();
        }*/

        return new MyPlaylistRepo();
    }


    public LiveData<List<RailCommonData>> getAllWatchlist(final Context context, String ksql) {
        railList.clear();
        final KsServices ksServices = new KsServices(context);
        final MutableLiveData<List<RailCommonData>> connection = new MutableLiveData<>();
        callWatchList(context, ksql, ksServices, connection);
        return connection;
    }

    private void callWatchList(final Context context, final String ksql, final KsServices ksServices,
                               final MutableLiveData<List<RailCommonData>> connection) {
        ksServices.getWatchlist(ksql, (status, errorCode, message, responseListt) -> {
            if (status) {
                if (responseListt != null) {
                    if (responseListt.size() > 0) {
                        int size = responseListt.get(0).results.getObjects().size();
                        if (size > 0) {
                            callDynamicData(context, responseListt);
                            connection.postValue(railList);
                        }
                    } else {
                        errorHandling();
                        connection.postValue(railList);
                    }
                } else {
                    errorHandling();
                    connection.postValue(railList);
                }

            } else {
                if (errorCode.equalsIgnoreCase("")) {
                    errorHandling();
                    connection.postValue(railList);
                } else if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                    new StartSessionLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status1, apiType, list) -> {
                        if (status1) {
                            callWatchList(context, ksql, ksServices, connection);
                        }
                    });
                } else {
                    errorHandling();
                    connection.postValue(railList);
                }

            }
        });

    }

    private void errorHandling() {
        railList = new ArrayList<>();
        RailCommonData assetCommonBean = new RailCommonData();
        assetCommonBean.setStatus(false);
        railList.add(assetCommonBean);

    }

    private void callDynamicData(Context context, List<Response<ListResponse<Asset>>> list) {

        Log.e("","callDynamicData"+list.get(0).results.getObjects().size());
        for (int j = 0; j < list.get(0).results.getObjects().size(); j++) {
            RailCommonData railCommonData = new RailCommonData();
            railCommonData.setStatus(true);
            railCommonData.setTotalCount(list.get(0).results.getTotalCount());
        //    railCommonData.setCatchUpBuffer(list.get(0).results.getObjects().get(j).getEnableCatchUp());
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
    }

    public LiveData<CommonResponse> getWatchListData(Context context, int counter, String partnerId) {
        final MutableLiveData<CommonResponse> listMutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        callWatchlistData(partnerId,counter, listMutableLiveData, ksServices, context);
        return listMutableLiveData;
    }

    private void callWatchlistData(String partnerId,int counter, final MutableLiveData<CommonResponse> listMutableLiveData,
                                   final KsServices ksServices, final Context context) {
        ksServices.compareWatchlist(partnerId,counter, (status, errorCode, result) -> {
            CommonResponse commonResponse = new CommonResponse();

            if (status && result.results != null) {
                // personalList = result.results.getObjects();
                commonResponse.setPersonalLists(result.results.getObjects());
                commonResponse.setTotalCount(result.results.getTotalCount());
                listMutableLiveData.postValue(commonResponse);
                // compareLists(personalList,responseListt);
            } else {
                if(result.results != null && result.results.getObjects() != null ){
                    commonResponse.setPersonalLists(result.results.getObjects());
                    commonResponse.setTotalCount(result.results.getTotalCount());
                    listMutableLiveData.postValue(commonResponse);
                }else {
                    listMutableLiveData.postValue(null);
                }

//                if (errorCode.equalsIgnoreCase("")) {
//                    commonResponse.setPersonalLists(result.results.getObjects());
//                    commonResponse.setTotalCount(result.results.getTotalCount());
//                    listMutableLiveData.postValue(commonResponse);
//                }
//
//                else if (errorCode.equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
//                    new StartSessionLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status1, apiType, list) -> {
//                        if (status1) {
//                            callWatchlistData(partnerId,counter, listMutableLiveData, ksServices, context);
//                        }
//                    });
//                }

            }
        });

    }


    public LiveData<CommonResponse> deleteFromWatchlist(String assetId, Context mcontext) {
        final MutableLiveData<CommonResponse> booleanMutableLiveData = new MutableLiveData<>();
        final CommonResponse commonResponse = new CommonResponse();
        KsServices ksServices = new KsServices(mcontext);

        ksServices.deleteFromWatchlistList(String.valueOf(assetId), (status, errorCode, message) -> {
            if (status) {

                commonResponse.setStatus(true);
            } else {
                commonResponse.setStatus(false);
                commonResponse.setErrorCode(errorCode);
                commonResponse.setMessage(new ErrorCallBack().ErrorMessage(errorCode,message));
            }
            booleanMutableLiveData.postValue(commonResponse);

        });
        return booleanMutableLiveData;
    }
}
