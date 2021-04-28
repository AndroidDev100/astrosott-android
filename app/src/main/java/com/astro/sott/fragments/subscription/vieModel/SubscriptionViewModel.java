package com.astro.sott.fragments.subscription.vieModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.astro.sott.repositories.loginRepository.AstrLoginRepository;
import com.astro.sott.repositories.mysubscriptionplan.MySubscriptionPlanRepository;
import com.astro.sott.repositories.subscription.SubscriptionRepository;
import com.astro.sott.usermanagment.modelClasses.addSubscripton.AddSubscriptionResponse;
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.GetActiveResponse;
import com.astro.sott.usermanagment.modelClasses.changePassword.ChangePasswordResponse;
import com.astro.sott.usermanagment.modelClasses.checkCredential.CheckCredentialResponse;
import com.astro.sott.usermanagment.modelClasses.createOtp.CreateOtpResponse;
import com.astro.sott.usermanagment.modelClasses.invoice.InvoiceResponse;
import com.astro.sott.usermanagment.modelClasses.lastSubscription.LastSubscriptionResponse;
import com.astro.sott.usermanagment.modelClasses.removeSubscription.RemoveSubscriptionResponse;
import com.astro.sott.usermanagment.modelClasses.updateProfile.UpdateProfileResponse;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.google.gson.JsonArray;
import com.kaltura.client.types.Subscription;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class SubscriptionViewModel extends AndroidViewModel {
    public SubscriptionViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<EvergentCommonResponse> getProduct() {
        return MySubscriptionPlanRepository.getInstance().getProducts(getApplication());
    }

    public LiveData<EvergentCommonResponse> getProductForLogin(String accessToken, JsonArray subscriptionId) {
        return MySubscriptionPlanRepository.getInstance().getProductsForLogin(getApplication(), subscriptionId, accessToken);
    }

    public LiveData<EvergentCommonResponse> getPaymentV2(String acessToken) {
        return MySubscriptionPlanRepository.getInstance().getPaymentV2(getApplication(), acessToken);
    }

    public LiveData<List<Subscription>> getSubscriptionPackageList(String assetId) {
        return SubscriptionRepository.getInstance().getSubscriptionPackageList(getApplication().getApplicationContext(), assetId);
    }

    public LiveData<EvergentCommonResponse<GetActiveResponse>> getActiveSubscription(String acessToken,String profile) {
        return MySubscriptionPlanRepository.getInstance().getActiveSubscription(getApplication(), acessToken,profile);
    }

    public LiveData<EvergentCommonResponse<ChangePasswordResponse>> changePassword(String acessToken, String oldPassword, String newPassword) {
        return MySubscriptionPlanRepository.getInstance().changePassword(getApplication(), acessToken, oldPassword, newPassword);
    }

    public LiveData<EvergentCommonResponse<LastSubscriptionResponse>> getLastSubscription(String acessToken) {
        return MySubscriptionPlanRepository.getInstance().getLastSubscription(getApplication(), acessToken);
    }

    public LiveData<EvergentCommonResponse<UpdateProfileResponse>> updateProfile(String type, String emailMobile, String accessToken) {
        return MySubscriptionPlanRepository.getInstance().updateProfile(getApplication(), type, emailMobile, accessToken);
    }

    public LiveData<EvergentCommonResponse<CheckCredentialResponse>> checkCredential(String password, String emailMobile, String type) {
        return MySubscriptionPlanRepository.getInstance().checkCredential(getApplication(), password, emailMobile,type);
    }

    public LiveData<EvergentCommonResponse<CreateOtpResponse>> createOtp(String type, String emailMobile) {
        return AstrLoginRepository.getInstance().createOtp(getApplication(), type, emailMobile);
    }

    public LiveData<EvergentCommonResponse<RemoveSubscriptionResponse>> removeSubscription(String acessToken, String productId) {
        return MySubscriptionPlanRepository.getInstance().removeSubscription(getApplication(), acessToken, productId);
    }

    public LiveData<EvergentCommonResponse<InvoiceResponse>> getInvoice(String acessToken, String transactionId) {
        return MySubscriptionPlanRepository.getInstance().getInvoice(getApplication(), acessToken, transactionId);
    }

    public LiveData<EvergentCommonResponse<AddSubscriptionResponse>> addSubscription(String acessToken, String productId, String token, String orderId) {
        return MySubscriptionPlanRepository.getInstance().addSubscription(getApplication(), acessToken, productId, token, orderId);
    }

}
