package com.dialog.dialoggo.repositories.billPaymentRepo;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dialog.dialoggo.activities.subscription.model.BillPaymentModel;
import com.dialog.dialoggo.activities.subscription.model.PurchaseResponse;
import com.dialog.dialoggo.activities.subscription.viewmodel.InvokeModel;
import com.dialog.dialoggo.callBacks.commonCallBacks.BillPaymentCallBack;
import com.dialog.dialoggo.callBacks.commonCallBacks.InvokeApiCallBack;
import com.dialog.dialoggo.callBacks.commonCallBacks.PurchaseSubscriptionCallBack;
import com.dialog.dialoggo.networking.errorCallBack.ErrorCallBack;
import com.dialog.dialoggo.networking.ksServices.KsServices;

import java.util.List;

public class BillPaymentRepositories {
    private static BillPaymentRepositories billPaymentRepositories;

    private BillPaymentRepositories(){

    }

    public static  BillPaymentRepositories getInstance() {
        if (billPaymentRepositories == null) {
            billPaymentRepositories = new BillPaymentRepositories();
        }
        return billPaymentRepositories;
    }

    public LiveData<List<BillPaymentModel>> getBillPAymentAccounts(Context context) {
        final MutableLiveData<List<BillPaymentModel>> mutableLiveData = new MutableLiveData<>();

        KsServices ksServices = new KsServices(context);
        ksServices.getBillPaymentsAccount(new BillPaymentCallBack() {
            @Override
            public void response(List<BillPaymentModel> billPaymentModels) {
                if(billPaymentModels!=null && billPaymentModels.size()>0){
                    mutableLiveData.postValue(billPaymentModels);
                }
            }

            @Override
            public void failure() {
                mutableLiveData.postValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<PurchaseResponse> callPurchaseapi(String paymentMethodId, String productId, String currency, String price, Context applicationContext) {
        final MutableLiveData<PurchaseResponse> mutableLiveData = new MutableLiveData<>();
        PurchaseResponse railCommonData = new PurchaseResponse();
        KsServices ksServices = new KsServices(applicationContext);
        ksServices.callPurchaseApi(paymentMethodId, applicationContext,productId,currency,price, new PurchaseSubscriptionCallBack() {
            @Override
            public void response(boolean status, String errorCode, String message, String paymentGatewayReferenceId) {
                if (status) {
                    railCommonData.setStatus(true);
                    railCommonData.setPaymentGatewayReferenceId(paymentGatewayReferenceId);
                    mutableLiveData.postValue(railCommonData);
                }else {
                    railCommonData.setStatus(false);
                    railCommonData.setMessage(new ErrorCallBack().ErrorMessage(errorCode,message));
                    mutableLiveData.postValue(railCommonData);
                }
            }
        });
        return mutableLiveData;
    }

    public LiveData<InvokeModel> callInvokeApi(String accountType, String accountNumber, Context applicationContext) {
        final MutableLiveData<InvokeModel> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(applicationContext);
        InvokeModel invokeModel = new InvokeModel();
        ksServices.callInvokeApi(accountType, accountNumber, applicationContext, new InvokeApiCallBack() {
            @Override
            public void result(boolean status,String errorCode, String message) {
                if (status){
                    invokeModel.setStatus(true);
                    mutableLiveData.postValue(invokeModel);
                }else {
                    invokeModel.setStatus(false);
                    invokeModel.setError(new ErrorCallBack().ErrorMessage(errorCode,message));
                    mutableLiveData.postValue(invokeModel);
                }

            }
        });
        return mutableLiveData;
    }
}
