package com.astro.sott.activities.parentalControl.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.callBacks.commonCallBacks.CommonResponseCallBack;
import com.astro.sott.callBacks.commonCallBacks.UserPrefrencesCallBack;
import com.astro.sott.callBacks.kalturaCallBacks.OttUserDetailsCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.kaltura.client.types.APIException;

public class ParentalControlRepository {
    private static ParentalControlRepository parentalControlRepository;
    private ParentalControlRepository(){
    }
    public static  ParentalControlRepository getInstance(){
        if(parentalControlRepository == null)
            parentalControlRepository = new ParentalControlRepository();
        return parentalControlRepository;
    }

    public LiveData<CommonResponse> getOttUserDetails(Context context) {
        MutableLiveData<CommonResponse> commonResponseLiveData= new MutableLiveData<>();
        new KsServices(context).getOttUserDetails(new OttUserDetailsCallBack() {
            @Override
            public void onSuccess(String userPreferenceForParental) {
                CommonResponse commonResponse = new CommonResponse();
                commonResponse.setStatus(true);
                commonResponse.setMessage(userPreferenceForParental);
                commonResponseLiveData.postValue(commonResponse);
            }

            @Override
            public void onFailure(APIException error) {
                CommonResponse commonResponse = new CommonResponse();
                commonResponse.setStatus(false);
                commonResponse.setErrorCode(error.getCode());
                commonResponse.setMessage(error.getMessage());
                commonResponseLiveData.postValue(commonResponse);
            }

            @Override
            public void onUserParentalDetailsNotFound() {
                CommonResponse commonResponse = new CommonResponse();
                commonResponse.setStatus(false);
                commonResponseLiveData.postValue(commonResponse);
            }
        });
        return commonResponseLiveData;
    }

    public LiveData<String> updateParentalControl(Context context,String parentalStatus) {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

        KsServices ksServices = new KsServices(context);
        ksServices.updateParentalControl(parentalStatus,new UserPrefrencesCallBack() {
            @Override
            public void response(String value) {
                mutableLiveData.postValue(value);
            }

            @Override
            public void failure() {
                mutableLiveData.postValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<CommonResponse> validatePin(Context context, String pinText) {
        final MutableLiveData<CommonResponse> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        CommonResponse commonResponse = new CommonResponse();
        ksServices.validatePin(pinText,new CommonResponseCallBack(){
            @Override
            public void onSuccess() {
                commonResponse.setStatus(true);
                mutableLiveData.postValue(commonResponse);

            }

            @Override
            public void onFailure(APIException error) {
                commonResponse.setStatus(false);
                commonResponse.setErrorCode(error.getCode());
                commonResponse.setMessage(error.getMessage());
                mutableLiveData.postValue(commonResponse);

            }
        });
        return mutableLiveData;
    }

    public LiveData<CommonResponse> setPin(Context context, String pinText) {
        final  MutableLiveData<CommonResponse> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        CommonResponse commonResponse = new CommonResponse();
        ksServices.setPin(pinText,new CommonResponseCallBack(){
            @Override
            public void onSuccess() {
                commonResponse.setStatus(true);
                mutableLiveData.postValue(commonResponse);
            }
            @Override
            public void onFailure(APIException error) {
                commonResponse.setStatus(false);
                commonResponse.setErrorCode(error.getCode());
                commonResponse.setMessage(error.getMessage());
                mutableLiveData.postValue(commonResponse);
            }
        });

        return mutableLiveData;
    }

    public LiveData<CommonResponse>  enableParental(Context context) {
        final MutableLiveData<CommonResponse> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        CommonResponse commonResponse = new CommonResponse();
        ksServices.enableParental(new CommonResponseCallBack() {
            @Override
            public void onSuccess() {
                commonResponse.setStatus(true);
                mutableLiveData.postValue(commonResponse);
            }

            @Override
            public void onFailure(APIException error) {
                commonResponse.setStatus(false);
                commonResponse.setErrorCode(error.getCode());
                commonResponse.setMessage(error.getMessage());
                mutableLiveData.postValue(commonResponse);
            }
        });
        return mutableLiveData;
    }

    public LiveData<CommonResponse>  disableParental(Context context) {
        final MutableLiveData<CommonResponse> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        CommonResponse commonResponse = new CommonResponse();
        ksServices.disableParental(new CommonResponseCallBack() {
            @Override
            public void onSuccess() {
                commonResponse.setStatus(true);
                mutableLiveData.postValue(commonResponse);
            }

            @Override
            public void onFailure(APIException error) {
                commonResponse.setStatus(false);
                commonResponse.setErrorCode(error.getCode());
                commonResponse.setMessage(error.getMessage());
                mutableLiveData.postValue(commonResponse);
            }
        });
        return mutableLiveData;
    }
}
