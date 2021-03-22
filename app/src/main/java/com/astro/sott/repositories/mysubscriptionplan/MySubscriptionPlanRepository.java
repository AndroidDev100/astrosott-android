package com.astro.sott.repositories.mysubscriptionplan;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.networking.errorCallBack.ErrorCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.usermanagment.EvergentServices.EvergentServices;
import com.astro.sott.usermanagment.modelClasses.addSubscripton.AddSubscriptionResponse;
import com.astro.sott.usermanagment.callBacks.EvergentGetProductsCallBack;
import com.astro.sott.usermanagment.callBacks.EvergentPaymentV2Callback;
import com.astro.sott.usermanagment.callBacks.EvergentResponseCallBack;
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.GetActiveResponse;
import com.astro.sott.usermanagment.modelClasses.getPaymentV2.PaymentV2Response;
import com.astro.sott.usermanagment.modelClasses.getProducts.GetProductResponse;
import com.astro.sott.usermanagment.modelClasses.removeSubscription.RemoveSubscriptionResponse;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.Entitlement;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.Subscription;
import com.kaltura.client.utils.response.base.Response;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MySubscriptionPlanRepository {

    private static MySubscriptionPlanRepository resultRepository;
    private List<AssetCommonBean> assetCommonList;
    private AssetCommonBean assetCommonBean;
    private List<Response<ListResponse<Asset>>> responseList;

    private MySubscriptionPlanRepository() {

    }

    public static synchronized MySubscriptionPlanRepository getInstance() {
        if (resultRepository == null) {
            resultRepository = new MySubscriptionPlanRepository();
        }
        return resultRepository;
    }


    public LiveData<EvergentCommonResponse> getProducts(Context context) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().getProduct(context, new EvergentGetProductsCallBack() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull GetProductResponse getDevicesResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setGetProductResponse(getDevicesResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }

    public LiveData<EvergentCommonResponse> getPaymentV2(Context context, String acessToken) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().getPaymentV2(context, acessToken, new EvergentPaymentV2Callback() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull PaymentV2Response getDevicesResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setPaymentV2Response(getDevicesResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }


    public LiveData<EvergentCommonResponse<GetActiveResponse>> getActiveSubscription(Context context, String acessToken) {
        MutableLiveData<EvergentCommonResponse<GetActiveResponse>> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().getActiveSubscripton(context, acessToken, new EvergentResponseCallBack<GetActiveResponse>() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull GetActiveResponse getDevicesResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setResponse(getDevicesResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }


    public LiveData<EvergentCommonResponse<AddSubscriptionResponse>> addSubscription(Context context, String acessToken, String productId, String token,String orderId) {
        MutableLiveData<EvergentCommonResponse<AddSubscriptionResponse>> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().addSubscription(context, productId, token, acessToken,orderId, new EvergentResponseCallBack<AddSubscriptionResponse>() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull AddSubscriptionResponse getDevicesResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setResponse(getDevicesResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }

    public LiveData<EvergentCommonResponse<RemoveSubscriptionResponse>> removeSubscription(Context context, String acessToken, String productId) {
        MutableLiveData<EvergentCommonResponse<RemoveSubscriptionResponse>> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().removeSubscription(context, productId, acessToken, new EvergentResponseCallBack<RemoveSubscriptionResponse>() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull RemoveSubscriptionResponse getDevicesResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setResponse(getDevicesResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }

    public MutableLiveData<List<Entitlement>> getEntitlementList(Context context) {

        final MutableLiveData<List<Entitlement>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        ksServices.callEntitlementListApi((status1, errorCode, message, entitlementListRespone) -> {
            if (status1) {
                if (entitlementListRespone != null) {
                    if (entitlementListRespone.size() > 0) {

                        connection.postValue(entitlementListRespone);
                    } else {
                        connection.postValue(entitlementListRespone);
                    }
                } else {
                    connection.postValue(null);
                }
            } else {
                connection.postValue(null);
            }
        });

        return connection;
    }


    public MutableLiveData<List<Subscription>> getMySubscriptionList(Context context, String id) {

        final MutableLiveData<List<Subscription>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);

        ksServices.callMySubcriptionListApi(id, (status1, errorCode, message, subscriptionListRespone) -> {
            if (status1) {
                if (subscriptionListRespone != null) {
                    if (subscriptionListRespone.size() > 0) {

                        connection.postValue(subscriptionListRespone);
                    } else {
                        connection.postValue(subscriptionListRespone);
                    }
                } else {
                    connection.postValue(null);
                }
            } else {
                connection.postValue(null);
            }
        });

        return connection;
    }

    public MutableLiveData<CommonResponse> callCancelSubcriptionApi(String id, Context context) {

        final MutableLiveData<CommonResponse> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        final CommonResponse commonResponse = new CommonResponse();
        ksServices.callCancelSubscriptionApi(id, (status1, errorCode, message) -> {
            if (status1) {
                commonResponse.setStatus(true);
                commonResponse.setErrorCode(errorCode);
                commonResponse.setMessage(message);
                connection.postValue(commonResponse);
            } else {
                commonResponse.setStatus(false);
                commonResponse.setErrorCode(errorCode);
                commonResponse.setMessage(new ErrorCallBack().ErrorMessage(errorCode, message));
                connection.postValue(commonResponse);
            }
        });

        return connection;
    }


}
