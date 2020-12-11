package com.astro.sott.repositories.homeTab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.baseModel.CategoryRails;
import com.astro.sott.utils.helpers.carousel.model.Slide;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.PrintLogging;
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;
import com.enveu.BaseCollection.BaseCategoryServices.BaseCategoryServices;
import com.enveu.CallBacks.EnveuCallBacks;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;


public class HomeFragmentRepository {
    private List<AssetCommonBean> assetCommonList;
    private ArrayList<Slide> slides;
    private int apiCount = 0;
    private static HomeFragmentRepository projectRepository;
    private boolean flag = false;

    public static HomeFragmentRepository getInstance() {
        if (projectRepository == null) {
            projectRepository = new HomeFragmentRepository();
        }
        return new HomeFragmentRepository();
    }

    public static void resetObject() {
        projectRepository = null;
    }

/*
    public LiveData<List<AssetCommonBean>> loadData(Context context, long channelID, List<VIUChannel> list, int counter, int swipeRefresh, boolean isCraousalInjected) {
        apiCount = counter;
        flag=isCraousalInjected;
        Log.e("Craousal Injected", String.valueOf(isCraousalInjected));
        final KsServices ksServices = new KsServices(context);
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {
                ksServices.callChannelRail(0, channelID, list, counter, (status1, listResponseResponse, channelList) -> {
                    if (status1) {
                        apiCount++;
                        callDynamicData(context, listResponseResponse, channelList, counter);
                        connection.postValue(assetCommonList);
                    } else {
                        errorHandling();
                        connection.postValue(assetCommonList);
                    }
                });
            } else {
                connection.postValue(null);
            }

        });
        return connection;
    }
*/

    int loopCount=1;
    public LiveData<List<AssetCommonBean>> loadData(Context context, long channelID, List<VIUChannel> list, int counter, int swipeRefresh, List<AssetCommonBean> loadedList, int i) {
        PrintLogging.printLog("", "swipeRefresh--++" + counter+"  "+channelID);
       /* if (swipeRefresh==2){
            apiCount=0;
        }*/

        apiCount = counter;
        loopCount = 1;
        final KsServices ksServices = new KsServices(context);
        final MutableLiveData<List<AssetCommonBean>> connection = new MutableLiveData<>();
        AppCommonMethods.checkDMS(context, status -> {
            if (status) {
                ksServices.callChannelRail(1, channelID, list, counter, (status1, listResponseResponse, channelList) -> {
                    if (status1) {
                        apiCount++;
                        callDynamicData(context,listResponseResponse, channelList, counter, loadedList);
                        connection.postValue(assetCommonList);
                    } else {
                        apiCount--;
                        errorHandling();
                        connection.postValue(assetCommonList);
                    }

                });
            }


        });
        return connection;
    }

    private void callDynamicData(Context context,final List<Response<ListResponse<Asset>>> list, final List<VIUChannel> channelList, int counter, List<AssetCommonBean> loadedList) {
        try {

            assetCommonList = new ArrayList<>();
            PrintLogging.printLog("", "getDescription" + channelList.get(counter).getDescription().trim());
            if (loadedList.size() > 0) {
                new CategoryRails().setRails(context,list, channelList, 2, slides, assetCommonList, counter);
            } else {
                new CategoryRails().setRails(context,list, channelList, loopCount, slides, assetCommonList, counter);
            }
        }catch (Exception e)
        {}
    }



    private void errorHandling() {
        assetCommonList = new ArrayList<>();
        AssetCommonBean assetCommonBean = new AssetCommonBean();
        assetCommonBean.setStatus(false);
        assetCommonList.add(assetCommonBean);
    }

/*
    private void callDynamicData(Context context, final List<Response<ListResponse<Asset>>> list, final List<VIUChannel> channelList, int counter) {
        assetCommonList = new ArrayList<>();
        if (flag) {
            new CategoryRails().setRails(context, list, channelList, 2, slides, assetCommonList, counter);
        } else {
            new CategoryRails().setRails(context, list, channelList, 1, slides, assetCommonList, counter);
        }

    }
*/

    public LiveData<List<BaseCategory>> getCategories(String screenId) {
        MutableLiveData<List<BaseCategory>> liveData = new MutableLiveData<>();
        BaseCategoryServices.Companion.getInstance().categoryService(screenId, new EnveuCallBacks() {
            @Override
            public void success(boolean status, List<BaseCategory> categoryList) {
                if (status) {
                    liveData.postValue(categoryList);
                }
            }

            @Override
            public void failure(boolean status, int errorCode, String errorMessage) {
                liveData.postValue(new ArrayList<>());
            }
        });
        return liveData;
    }


}
