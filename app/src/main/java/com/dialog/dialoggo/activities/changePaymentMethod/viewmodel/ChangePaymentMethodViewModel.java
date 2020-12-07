package com.dialog.dialoggo.activities.changePaymentMethod.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dialog.dialoggo.activities.changePaymentMethod.model.ChangePaymentMethodModel;
import com.dialog.dialoggo.activities.changePaymentMethod.model.UpdatePaymentMethodModel;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.repositories.changepaymentmethod.ChangePaymentMethodRepository;
import com.dialog.dialoggo.repositories.mysubscriptionplan.MySubscriptionPlanRepository;
import com.dialog.dialoggo.repositories.subscription.SubscriptionRepository;
import com.kaltura.client.types.Entitlement;
import com.kaltura.client.types.HouseholdPaymentMethod;
import com.kaltura.client.types.Subscription;

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
