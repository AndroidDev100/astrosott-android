package com.astro.sott.activities.loginActivity.AstrLoginViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.astro.sott.repositories.loginRepository.AstrLoginRepository;
import com.astro.sott.repositories.mysubscriptionplan.MySubscriptionPlanRepository;
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.GetActiveResponse;
import com.astro.sott.usermanagment.modelClasses.changePassword.ChangePasswordResponse;
import com.astro.sott.usermanagment.modelClasses.createOtp.CreateOtpResponse;
import com.astro.sott.usermanagment.modelClasses.updateProfile.UpdateProfileResponse;

public class AstroLoginViewModel extends AndroidViewModel {
    public AstroLoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<EvergentCommonResponse> searchAccountV2(String type, String emailMobile) {
        return AstrLoginRepository.getInstance().searchAccountV2(getApplication(), type, emailMobile);
    }
    public LiveData<EvergentCommonResponse<UpdateProfileResponse>> updateProfile(String type, String emailMobile, String accessToken) {
        return MySubscriptionPlanRepository.getInstance().updateProfile(getApplication(), type, emailMobile, accessToken);
    }
    public LiveData<EvergentCommonResponse<CreateOtpResponse>> createOtp(String type, String emailMobile) {
        return AstrLoginRepository.getInstance().createOtp(getApplication(), type, emailMobile);
    }

    public LiveData<EvergentCommonResponse<ChangePasswordResponse>> changePassword(String acessToken, String oldPassword, String newPassword) {
        return MySubscriptionPlanRepository.getInstance().changePassword(getApplication(), acessToken, oldPassword, newPassword);
    }

    public LiveData<EvergentCommonResponse> confirmOtp(String loginType, String emailMobile, String otp) {
        return AstrLoginRepository.getInstance().confirmOtp(getApplication(), loginType, emailMobile, otp);
    }

    public LiveData<EvergentCommonResponse> resetPassword(String token, String password) {
        return AstrLoginRepository.getInstance().resetPassword(getApplication(), token, password);
    }

    public LiveData<EvergentCommonResponse> createUser(String type, String emailMobile, String password, String name,boolean isTablet) {
        return AstrLoginRepository.getInstance().createUser(getApplication(), type, emailMobile, password,name,isTablet);
    }

    public LiveData<EvergentCommonResponse> loginUser(String type, String emailMobile, String password,boolean isTablet) {
        return AstrLoginRepository.getInstance().loginUser(getApplication(), type, emailMobile, password,isTablet);
    }
    public LiveData<EvergentCommonResponse<GetActiveResponse>> getActiveSubscription(String acessToken, String profile) {
        return MySubscriptionPlanRepository.getInstance().getActiveSubscription(getApplication(), acessToken, profile);
    }
    public LiveData<EvergentCommonResponse> getContact(String acessToken) {
        return AstrLoginRepository.getInstance().getContact(getApplication(), acessToken);
    }

    public LiveData<EvergentCommonResponse> getDevice(String acessToken) {
        return AstrLoginRepository.getInstance().getDevice(getApplication(), acessToken);
    }

    public LiveData<EvergentCommonResponse> removeDevice(String acessToken, String serial) {
        return AstrLoginRepository.getInstance().removeDevice(getApplication(), acessToken, serial);
    }


    public void addToken(String ks) {
        AstrLoginRepository.getInstance().addToken(getApplication(), ks);
    }

}
