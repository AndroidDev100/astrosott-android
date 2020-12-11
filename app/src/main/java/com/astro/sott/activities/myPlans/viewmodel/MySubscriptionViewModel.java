package com.astro.sott.activities.myPlans.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.astro.sott.repositories.mysubscriptionplan.MySubscriptionPlanRepository;
import com.astro.sott.repositories.subscription.SubscriptionRepository;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.login.CommonResponse;
import com.kaltura.client.types.Entitlement;
import com.kaltura.client.types.Subscription;

import java.util.List;

public class MySubscriptionViewModel extends AndroidViewModel {
    public MySubscriptionViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Entitlement>> getEntitlementList() {
        return  MySubscriptionPlanRepository.getInstance().getEntitlementList(getApplication().getApplicationContext());
    }

    public LiveData<List<Subscription>> getMySubscriptionPackageList(String assetId) {
        return  MySubscriptionPlanRepository.getInstance().getMySubscriptionList(getApplication().getApplicationContext(), assetId);
    }

    public LiveData<List<AssetCommonBean>> getAllChannelList(String assetId, int counter) {
        return  SubscriptionRepository.getInstance().getAllChannelList(assetId,counter,getApplication().getApplicationContext());
    }

    public LiveData<CommonResponse> callCancelSubscription(String id) {
        return  MySubscriptionPlanRepository.getInstance().callCancelSubcriptionApi(id,getApplication().getApplicationContext());
    }
}
