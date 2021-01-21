package com.astro.sott.repositories.loginRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.usermanagment.EvergentServices.EvergentServices;
import com.astro.sott.usermanagment.callBacks.EvergentSearchAccountCallBack;
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse;
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

    public LiveData<EvergentCommonResponse> searchAccountV2() {
        MutableLiveData<EvergentCommonResponse> mutableLiveData = new MutableLiveData<>();
        EvergentCommonResponse evergentCommonResponse = new EvergentCommonResponse();
        EvergentServices.Companion.getInstance().searchAccountv2(new EvergentSearchAccountCallBack() {
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

}
