package com.dialog.dialoggo.activities.subscription.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dialog.dialoggo.activities.subscription.model.BillPaymentModel;
import com.dialog.dialoggo.activities.subscription.model.PurchaseResponse;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.repositories.billPaymentRepo.BillPaymentRepositories;
import com.dialog.dialoggo.repositories.changepaymentmethod.ChangePaymentMethodRepository;
import com.dialog.dialoggo.utils.helpers.AssetContent;
import com.kaltura.client.types.HouseholdPaymentMethod;

import java.util.List;

public class BillPaymentViewModel extends AndroidViewModel {
    public BillPaymentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<BillPaymentModel>> getBillPaymentAccounts() {
        return BillPaymentRepositories.getInstance().getBillPAymentAccounts(getApplication().getApplicationContext());
    }

    public LiveData<List<HouseholdPaymentMethod>> callHouseHoldList() {
       return ChangePaymentMethodRepository.getInstance().getHouseholdPaymentMethod(getApplication().getApplicationContext());
    }

    public LiveData<PurchaseResponse> callPurchaseApi(String paymentMethodId, String productId, String currency, String price) {
        return BillPaymentRepositories.getInstance().callPurchaseapi(paymentMethodId,productId,currency,price,getApplication().getApplicationContext());
    }

    public LiveData<InvokeModel>callInvokeApi(String accountType, String accountNumber) {
      return  BillPaymentRepositories.getInstance().callInvokeApi(accountType,accountNumber,getApplication().getApplicationContext());
    }

    public LiveData<List<BillPaymentModel>> getMbbAccountDetails() {
        return AssetContent.getMbbAccountDetails(getApplication().getApplicationContext());
    }
}
