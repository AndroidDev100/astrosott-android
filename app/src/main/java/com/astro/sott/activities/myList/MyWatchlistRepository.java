package com.astro.sott.activities.myList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.activities.login.ui.StartSessionLogin;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonUrls;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;
public class MyWatchlistRepository {
    private static MyWatchlistRepository myWatchlistRepository;
    private ArrayList<RailCommonData> railList = new ArrayList<RailCommonData>();
    private MyWatchlistRepository() {

    }

    public static MyWatchlistRepository getInstance() {
        if (myWatchlistRepository == null) {
            myWatchlistRepository = new MyWatchlistRepository();
        }

        return myWatchlistRepository;
    }


    public LiveData<List<RailCommonData>> getAllWatchlist(final Context context, String ksql) {
        railList.clear();
        final KsServices ksServices =  new KsServices(context);
        final MutableLiveData<List<RailCommonData>> connection = new MutableLiveData<>();
        callWatchList(context,ksql,ksServices,connection);
        return connection;
    }

    private void callWatchList(final Context context, final String ksql, final KsServices ksServices,
                               final MutableLiveData<List<RailCommonData>> connection) {
        ksServices.getWatchlist(ksql, (status, errorCode, message, responseListt) -> {
            PrintLogging.printLog("","valueWatchlistKs"+errorCode+"--"+message+" "+status);
            if (status) {
                if (responseListt!=null){
                    if (responseListt.size()>0){
                        int size=responseListt.get(0).results.getObjects().size();
                        if (size>0){
                            callDynamicData(AppConstants.TYPE5, responseListt,context);
                            connection.postValue(railList);
                        }
                    } else {
                        errorHandling();
                        connection.postValue(railList);
                    }
                }
                else {
                    errorHandling();
                    connection.postValue(railList);
                }

            } else {
                if (errorCode.equalsIgnoreCase("")){
                    errorHandling();
                    connection.postValue(railList);
                }else if (errorCode.equalsIgnoreCase(AppConstants.KS_EXPIRE)){
                    new StartSessionLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status1, apiType, list) -> {
                        if (status1) {
                            PrintLogging.printLog("", "kalturaLogin" + status1);
                            callWatchList(context, ksql, ksServices, connection);
                        }
                    });
                }else {
                    errorHandling();
                    connection.postValue(railList);
                }

            }
        });

    }

    private void errorHandling() {
        railList=new ArrayList<>();
        RailCommonData assetCommonBean=new RailCommonData();
        assetCommonBean.setStatus(false);
        railList.add(assetCommonBean);

    }

    private void callDynamicData(String layout, List<Response<ListResponse<Asset>>> list,Context context) {

        for (int j = 0; j < list.get(0).results.getObjects().size(); j++) {
            RailCommonData railCommonData = new RailCommonData();
            railCommonData.setStatus(true);
//            railCommonData.setCatchUpBuffer(list.get(0).results.getObjects().get(j).getEnableCatchUp());
            railCommonData.setType(list.get(0).results.getObjects().get(j).getType());
            railCommonData.setName(list.get(0).results.getObjects().get(j).getName());
            railCommonData.setId(list.get(0).results.getObjects().get(j).getId());
            railCommonData.setObject(list.get(0).results.getObjects().get(j));

            List<AssetCommonImages> imagesList = new ArrayList<AssetCommonImages>();
            for (int k = 0; k < list.get(0).results.getObjects().get(j).getImages().size(); k++) {

                AssetCommonImages assetCommonImages = new AssetCommonImages();
                //imageLogic(tileType,position,j,k,list,assetCommonImages,imagesList);

                AppCommonMethods.getImageList(context, AppLevelConstants.TYPE5, 0, j, k, list, assetCommonImages, imagesList);
            }

            List<AssetCommonUrls> urlList = new ArrayList<AssetCommonUrls>();
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
            railCommonData.setImages(imagesList);
            railCommonData.setUrls(urlList);
            railList.add(railCommonData);

        }
    }

    public LiveData<CommonResponse> getWatchListData(int counter, Context context) {
        final MutableLiveData<CommonResponse> listMutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        callWatchlistData(counter,listMutableLiveData,ksServices,context);
        return  listMutableLiveData;
    }

    private void callWatchlistData(int counter,final MutableLiveData<CommonResponse> listMutableLiveData,
                                   final KsServices ksServices,final Context context) {
        ksServices.compareWatchlist(counter,(status, errorCode, result) -> {
            CommonResponse commonResponse = new CommonResponse();
            if (status){
                commonResponse.setPersonalLists(result.results.getObjects());
                commonResponse.setTotalCount(result.results.getTotalCount());
                listMutableLiveData.postValue(commonResponse);
                // compareLists(personalList,responseListt);
            }
            else {
                if (errorCode.equalsIgnoreCase("")){
                    commonResponse.setPersonalLists(result.results.getObjects());
                    commonResponse.setTotalCount(result.results.getTotalCount());
                    listMutableLiveData.postValue(commonResponse);
                }else if (errorCode.equalsIgnoreCase(AppConstants.KS_EXPIRE)){
                    new StartSessionLogin(context).callUserLogin(KsPreferenceKey.getInstance(context).getUser().getUsername(), "", (status1, apiType, list) -> {
                        if (status1) {
                            PrintLogging.printLog("", "kalturaLogin" + status1);
                            callWatchlistData(counter,listMutableLiveData, ksServices, context);
                        }
                    });
                }

            }
        });

    }


    public LiveData<CommonResponse> deleteFromWatchlist(String assetId, Context mcontext) {
        final MutableLiveData<CommonResponse> booleanMutableLiveData = new MutableLiveData<>();
        final CommonResponse commonResponse=new CommonResponse();
        KsServices ksServices = new KsServices(mcontext);

        ksServices.deleteFromWatchlistList(String.valueOf(assetId), (status, errorCode, message) -> {
            if (status){

                PrintLogging.printLog("", "deleteId" + status);
                commonResponse.setStatus(true);
            }else {
                commonResponse.setStatus(false);
                commonResponse.setErrorCode(errorCode);
                commonResponse.setMessage(message);
            }
            booleanMutableLiveData.postValue(commonResponse);

        });
        return booleanMutableLiveData;
    }
}