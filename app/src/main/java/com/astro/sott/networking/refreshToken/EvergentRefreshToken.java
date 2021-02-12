package com.astro.sott.networking.refreshToken;

import android.content.Context;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.usermanagment.EvergentServices.EvergentServices;
import com.astro.sott.usermanagment.callBacks.EvergentRefreshTokenCallBack;
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse;
import com.astro.sott.usermanagment.modelClasses.refreshToken.RefreshTokenResponse;
import com.astro.sott.utils.userInfo.UserInfo;

import org.jetbrains.annotations.NotNull;

public class EvergentRefreshToken {

    public static LiveData<EvergentCommonResponse> refreshToken(Context context, String refreshToken) {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().refreshToken(context, refreshToken, new EvergentRefreshTokenCallBack() {


            @Override
            public void onFailure(@NotNull String errorMessage, @NotNull String errorCode) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setErrorMessage(errorMessage);
                evergentCommonResponse.setErrorCode(errorCode);
                mutableLiveData.postValue(evergentCommonResponse);
            }

            @Override
            public void onSuccess(@NotNull RefreshTokenResponse createUserResponse) {
                evergentCommonResponse.setStatus(false);
                evergentCommonResponse.setRefreshTokenResponse(createUserResponse);
                UserInfo.getInstance(context).setAccessToken(createUserResponse.getRefreshTokenResponseMessage().getAccessToken());
                mutableLiveData.postValue(evergentCommonResponse);

            }
        });
        return mutableLiveData;
    }
}
