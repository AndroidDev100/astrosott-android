package com.astro.sott.repositories.loginRepository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.usermanagment.EvergentServices.EvergentServices;
import com.astro.sott.usermanagment.callBacks.EvergentConfirmOtpCallBack;
import com.astro.sott.usermanagment.callBacks.EvergentCreateOtpCallBack;
import com.astro.sott.usermanagment.callBacks.EvergentCreateUserCallback;
import com.astro.sott.usermanagment.callBacks.EvergentGetContactCallback;
import com.astro.sott.usermanagment.callBacks.EvergentGetDeviceCallback;
import com.astro.sott.usermanagment.callBacks.EvergentGetProductsCallBack;
import com.astro.sott.usermanagment.callBacks.EvergentLoginUserCallback;
import com.astro.sott.usermanagment.callBacks.EvergentRefreshTokenCallBack;
import com.astro.sott.usermanagment.callBacks.EvergentRemoveDevice;
import com.astro.sott.usermanagment.callBacks.EvergentResetPasswordCallBack;
import com.astro.sott.usermanagment.callBacks.EvergentSearchAccountCallBack;
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse;
import com.astro.sott.usermanagment.modelClasses.confirmOtp.ConfirmOtpResponse;
import com.astro.sott.usermanagment.modelClasses.createOtp.CreateOtpResponse;
import com.astro.sott.usermanagment.modelClasses.createUser.CreateUserResponse;
import com.astro.sott.usermanagment.modelClasses.getContact.GetContactResponse;
import com.astro.sott.usermanagment.modelClasses.getDevice.GetDevicesResponse;
import com.astro.sott.usermanagment.modelClasses.getProducts.GetProductResponse;
import com.astro.sott.usermanagment.modelClasses.login.LoginResponse;
import com.astro.sott.usermanagment.modelClasses.refreshToken.RefreshTokenResponse;
import com.astro.sott.usermanagment.modelClasses.removeDevice.RemoveDeviceResponse;
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

    public LiveData<EvergentCommonResponse> searchAccountV2(Context context, String type, String emailMobile) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().searchAccountv2(context, type, emailMobile, new EvergentSearchAccountCallBack() {
            @Override
            public void onSuccess(@NotNull SearchAccountv2Response searchAccountv2Response) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setSearchAccountv2Response(searchAccountv2Response);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                mutableLiveData.postValue(evergentCommonResponse);
                evergentCommonResponse.setErrorCode(errorCode);


            }
        });
        return mutableLiveData;
    }

    public void addToken(Context context, String ks) {
        new KsServices(context).kalturaAddToken(ks);
    }

    public LiveData<EvergentCommonResponse<CreateOtpResponse>> createOtp(Context context, String type, String emailMobile) {
        MutableLiveData<EvergentCommonResponse<CreateOtpResponse>> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().createOtp(context, type, emailMobile, new EvergentCreateOtpCallBack() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);

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

    public LiveData<EvergentCommonResponse> confirmOtp(Context context, String loginType, String emailMobile, String otp) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().confirmOtp(context, loginType, emailMobile, otp, new EvergentConfirmOtpCallBack() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);
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


    public LiveData<EvergentCommonResponse> resetPassword(Context context, String token, String password) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().resetPassword(token, context, password, new EvergentResetPasswordCallBack() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);

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


    public LiveData<EvergentCommonResponse> createUser(Context context, String type, String emailMobile, String password) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().createUser(context, type, emailMobile, password, new EvergentCreateUserCallback() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);
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

    public LiveData<EvergentCommonResponse> loginUser(Context context, String type, String emailMobile, String password) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().loginUser(context, type, emailMobile, password, new EvergentLoginUserCallback() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull LoginResponse createUserResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setLoginResponse(createUserResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }

    public LiveData<EvergentCommonResponse> getContact(Context context, String accessToken) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().getContact(context, accessToken, new EvergentGetContactCallback() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull GetContactResponse createUserResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setGetContactResponse(createUserResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }


    public LiveData<EvergentCommonResponse> getDevice(Context context, String accessToken) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().getDevice(context, accessToken, new EvergentGetDeviceCallback() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull GetDevicesResponse getDevicesResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setGetDevicesResponse(getDevicesResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }

    public LiveData<EvergentCommonResponse> removeDevice(Context context, String accessToken, String serial) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().removeDevice(context, accessToken, serial, new EvergentRemoveDevice() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull RemoveDeviceResponse getDevicesResponse) {
                evergentCommonResponse.setStatus(true);
                evergentCommonResponse.setRemoveDeviceResponse(getDevicesResponse);
                mutableLiveData.postValue(evergentCommonResponse);
            }
        });
        return mutableLiveData;
    }






}
