package com.astro.sott.repositories.loginRepository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.usermanagment.EvergentServices.EvergentServices;
import com.astro.sott.usermanagment.callBacks.EvergentConfirmOtpCallBack;
import com.astro.sott.usermanagment.callBacks.EvergentCreateOtpCallBack;
import com.astro.sott.usermanagment.callBacks.EvergentCreateUserCallback;
import com.astro.sott.usermanagment.callBacks.EvergentResetPasswordCallBack;
import com.astro.sott.usermanagment.callBacks.EvergentSearchAccountCallBack;
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse;
import com.astro.sott.usermanagment.modelClasses.confirmOtp.ConfirmOtpResponse;
import com.astro.sott.usermanagment.modelClasses.createOtp.CreateOtpResponse;
import com.astro.sott.usermanagment.modelClasses.createUser.CreateUserResponse;
import com.astro.sott.usermanagment.modelClasses.resetPassword.ResetPasswordResponse;
import com.astro.sott.usermanagment.modelClasses.searchAccountv2.SearchAccountv2Response;

import org.jetbrains.annotations.NotNull;

public class AstrLoginRepository {
    private static AstrLoginRepository astrLoginRepository;

    public static AstrLoginRepository getInstance() {
        if (astrLoginRepository == null) {
            astrLoginRepository = new AstrLoginRepository();
        }
        return astrLoginRepository;
    }

    public LiveData<EvergentCommonResponse> searchAccountV2(Context context) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().searchAccountv2(context, new EvergentSearchAccountCallBack() {
            @Override
            public void onSuccess(@NotNull SearchAccountv2Response searchAccountv2Response) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setSearchAccountv2Response(searchAccountv2Response);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onFailure(@NotNull String errorMessage) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                mutableLiveData.postValue(evergentCommonResponse);

            }
        });
        return mutableLiveData;
    }


    public LiveData<EvergentCommonResponse> createOtp(Context context) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().createOtp(context, new EvergentCreateOtpCallBack() {


            @Override
            public void onFailure(@NotNull String errorMessage) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull CreateOtpResponse createOtpResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setCreateOtpResponse(createOtpResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }

    public LiveData<EvergentCommonResponse> confirmOtp(Context context) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().confirmOtp(context, new EvergentConfirmOtpCallBack() {


            @Override
            public void onFailure(@NotNull String errorMessage) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull ConfirmOtpResponse confirmOtpResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setConfirmOtpResponse(confirmOtpResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }


    public LiveData<EvergentCommonResponse> resetPassword(Context context, String token) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().resetPassword(token, context, new EvergentResetPasswordCallBack() {


            @Override
            public void onFailure(@NotNull String errorMessage) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull ResetPasswordResponse resetPasswordResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setResetPasswordResponse(resetPasswordResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }


    public LiveData<EvergentCommonResponse> createUser(Context context) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().createUser(context, new EvergentCreateUserCallback() {


            @Override
            public void onFailure(@NotNull String errorMessage) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull CreateUserResponse createUserResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setCreateUserResponse(createUserResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }


}
