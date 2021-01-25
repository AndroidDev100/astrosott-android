package com.astro.sott.activities.loginActivity.AstrLoginViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.repositories.loginRepository.AstrLoginRepository;
import com.astro.sott.repositories.loginRepository.LoginRepository;
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse;

public class AstroLoginViewModel extends AndroidViewModel {
    public AstroLoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<EvergentCommonResponse> searchAccountV2() {
        return AstrLoginRepository.getInstance().searchAccountV2(getApplication());
    }

    public LiveData<EvergentCommonResponse> createOtp() {
        return AstrLoginRepository.getInstance().createOtp(getApplication());
    }


    public LiveData<EvergentCommonResponse> confirmOtp() {
        return AstrLoginRepository.getInstance().confirmOtp(getApplication());
    }

    public LiveData<EvergentCommonResponse> resetPassword(String token) {
        return AstrLoginRepository.getInstance().resetPassword(getApplication(),token);
    }

    public LiveData<EvergentCommonResponse> createUser() {
        return AstrLoginRepository.getInstance().createUser(getApplication());
    }

}
