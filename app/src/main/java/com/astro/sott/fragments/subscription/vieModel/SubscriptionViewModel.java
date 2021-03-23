package com.astro.sott.fragments.subscription.vieModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.astro.sott.repositories.mysubscriptionplan.MySubscriptionPlanRepository;
import com.astro.sott.usermanagment.modelClasses.addSubscripton.AddSubscriptionResponse;
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.GetActiveResponse;
import com.astro.sott.usermanagment.modelClasses.changePassword.ChangePasswordResponse;
import com.astro.sott.usermanagment.modelClasses.lastSubscription.LastSubscriptionResponse;
import com.astro.sott.usermanagment.modelClasses.removeSubscription.RemoveSubscriptionResponse;
import com.astro.sott.utils.commonMethods.AppCommonMethods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SubscriptionViewModel extends AndroidViewModel {
    public SubscriptionViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<EvergentCommonResponse> getProduct() {
        return MySubscriptionPlanRepository.getInstance().getProducts(getApplication());
    }

    public LiveData<EvergentCommonResponse> getPaymentV2(String acessToken) {
        return MySubscriptionPlanRepository.getInstance().getPaymentV2(getApplication(), acessToken);
    }

    public LiveData<EvergentCommonResponse<GetActiveResponse>> getActiveSubscription(String acessToken) {
        return MySubscriptionPlanRepository.getInstance().getActiveSubscription(getApplication(), acessToken);
    }

    public LiveData<EvergentCommonResponse<ChangePasswordResponse>> changePassword(String acessToken, String oldPassword, String newPassword) {
        return MySubscriptionPlanRepository.getInstance().changePassword(getApplication(), acessToken, oldPassword, newPassword);
    }

    public LiveData<EvergentCommonResponse<LastSubscriptionResponse>> getLastSubscription(String acessToken) {
        return MySubscriptionPlanRepository.getInstance().getLastSubscription(getApplication(), acessToken);
    }

    public LiveData<EvergentCommonResponse<RemoveSubscriptionResponse>> removeSubscription(String acessToken, String productId) {
        return MySubscriptionPlanRepository.getInstance().removeSubscription(getApplication(), acessToken, productId);
    }

    public LiveData<EvergentCommonResponse<AddSubscriptionResponse>> addSubscription(String acessToken, String productId, String token, String orderId) {
        return MySubscriptionPlanRepository.getInstance().addSubscription(getApplication(), acessToken, productId, token, orderId);
    }

}
