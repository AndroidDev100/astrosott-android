package com.astro.sott.activities.changePaymentMethod.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.astro.sott.activities.changePaymentMethod.model.ChangePaymentMethodModel;
import com.astro.sott.activities.changePaymentMethod.model.UpdatePaymentMethodModel;
import com.astro.sott.repositories.mysubscriptionplan.MySubscriptionPlanRepository;
import com.astro.sott.repositories.changepaymentmethod.ChangePaymentMethodRepository;
import com.kaltura.client.types.Entitlement;
import com.kaltura.client.types.HouseholdPaymentMethod;

import java.util.List;

public class ChangePaymentMethodViewModel extends AndroidViewModel {
    public ChangePaymentMethodViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<HouseholdPaymentMethod>> getHouseholdPaymentMethodList() {
        return  ChangePaymentMethodRepository.getInstance().getHouseholdPaymentMethod(getApplication().getApplicationContext());
    }

    public LiveData<ChangePaymentMethodModel> callRemoveApi(int id) {
        return ChangePaymentMethodRepository.getInstance().callRemoveApi(id,getApplication().getApplicationContext());
    }

    public LiveData<List<Entitlement>> getEntitlementList() {
        return  MySubscriptionPlanRepository.getInstance().getEntitlementList(getApplication().getApplicationContext());
    }

    public LiveData<UpdatePaymentMethodModel> updatePaymentMethod(int id, int newPaymentMethodId) {
        return ChangePaymentMethodRepository.getInstance().updatePaymentMethod(id,newPaymentMethodId,getApplication().getApplicationContext());
    }
}
