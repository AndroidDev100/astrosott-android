package com.dialog.dialoggo.repositories.mysubscriptionplan;


import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.networking.errorCallBack.ErrorCallBack;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.Entitlement;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.Subscription;
import com.kaltura.client.types.SubscriptionEntitlement;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MySubscriptionPlanRepository {

    private static MySubscriptionPlanRepository resultRepository;
    private List<AssetCommonBean> assetCommonList;
    private AssetCommonBean assetCommonBean;
    private List<Response<ListResponse<Asset>>> responseList;
    private MySubscriptionPlanRepository(){

    }

    public static synchronized MySubscriptionPlanRepository getInstance() {
        if (resultRepository == null) {
            resultRepository = new MySubscriptionPlanRepository();
        }
        return resultRepository;
    }

    public MutableLiveData<List<Entitlement>> getEntitlementList(Context context) {

        final MutableLiveData<List<Entitlement>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        ksServices.callEntitlementListApi((status1,errorCode,message,entitlementListRespone)->{
            if(status1){
                if(entitlementListRespone != null){
                    if(entitlementListRespone.size() > 0){

                        connection.postValue(entitlementListRespone);
                    }else{
                        connection.postValue(entitlementListRespone);
                    }
                }else{
                    connection.postValue(null);
                }
            }else{
                connection.postValue(null);
            }
        });

        return connection;
    }


    public MutableLiveData<List<Subscription>> getMySubscriptionList(Context context, String id) {

        final MutableLiveData<List<Subscription>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);

        ksServices.callMySubcriptionListApi(id,(status1,errorCode,message,subscriptionListRespone)->{
            if(status1){
                if(subscriptionListRespone != null){
                    if(subscriptionListRespone.size() > 0){

                        connection.postValue(subscriptionListRespone);
                    }else{
                        connection.postValue(subscriptionListRespone);
                    }
                }else{
                    connection.postValue(null);
                }
            }else{
                connection.postValue(null);
            }
        });

        return connection;
    }

    public MutableLiveData<CommonResponse> callCancelSubcriptionApi(String id,Context context) {

        final MutableLiveData<CommonResponse> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        final CommonResponse commonResponse = new CommonResponse();
        ksServices.callCancelSubscriptionApi(id,(status1,errorCode,message)->{
            if(status1){
                commonResponse.setStatus(true);
                commonResponse.setErrorCode(errorCode);
                commonResponse.setMessage(message);
                connection.postValue(commonResponse);
            }else{
                commonResponse.setStatus(false);
                commonResponse.setErrorCode(errorCode);
                commonResponse.setMessage(new ErrorCallBack().ErrorMessage(errorCode,message));
                connection.postValue(commonResponse);
            }
        });

        return connection;
    }




}
