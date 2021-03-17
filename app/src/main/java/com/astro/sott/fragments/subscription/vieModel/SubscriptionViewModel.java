package com.astro.sott.fragments.subscription.vieModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.astro.sott.repositories.mysubscriptionplan.MySubscriptionPlanRepository;
import com.astro.sott.usermanagment.modelClasses.addSubscripton.AddSubscriptionResponse;
import com.astro.sott.usermanagment.modelClasses.EvergentCommonResponse;
import com.astro.sott.usermanagment.modelClasses.activeSubscription.GetActiveResponse;

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
