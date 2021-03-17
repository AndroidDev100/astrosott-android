package com.astro.sott.fragments.subscription.vieModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.astro.sott.repositories.mysubscriptionplan.MySubscriptionPlanRepository;
import com.astro.sott.usermanagment.modelClasses.addSubscripton.AddSubscriptionResponse;
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.GetActiveResponse;
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



    public LiveData<EvergentCommonResponse<AddSubscriptionResponse>> addSubscription(String acessToken, String productId, String token) {
        return MySubscriptionPlanRepository.getInstance().addSubscription(getApplication(), acessToken, productId, token);
    }

}
