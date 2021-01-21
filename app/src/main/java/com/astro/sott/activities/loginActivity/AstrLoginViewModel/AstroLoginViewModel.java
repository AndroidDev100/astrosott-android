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
}
