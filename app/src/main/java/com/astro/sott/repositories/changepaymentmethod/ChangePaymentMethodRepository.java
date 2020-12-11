package com.astro.sott.repositories.changepaymentmethod;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.activities.changePaymentMethod.model.ChangePaymentMethodModel;
import com.astro.sott.activities.changePaymentMethod.model.UpdatePaymentMethodModel;
import com.astro.sott.callBacks.commonCallBacks.RemovePaymentCallBack;
import com.astro.sott.callBacks.commonCallBacks.UpdatePaymentMethodCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.kaltura.client.types.HouseholdPaymentMethod;

import java.util.List;

public class ChangePaymentMethodRepository {

    private static ChangePaymentMethodRepository resultRepository;

    private ChangePaymentMethodRepository(){

    }

    public static synchronized ChangePaymentMethodRepository getInstance() {
        if (resultRepository == null) {
            resultRepository = new ChangePaymentMethodRepository();
        }
        return resultRepository;
    }

    public MutableLiveData<List<HouseholdPaymentMethod>> getHouseholdPaymentMethod(Context context) {

        final MutableLiveData<List<HouseholdPaymentMethod>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        ksServices.callHouseholdpaymentmethod((status1,errorCode,message,householdPaymentMethodList)->{
            if(status1){
                if(householdPaymentMethodList != null){
                    if(householdPaymentMethodList.size() > 0){

                        connection.postValue(householdPaymentMethodList);
                    }else{
                        connection.postValue(householdPaymentMethodList);
                    }
                }else{
                    connection.postValue(householdPaymentMethodList);
                }
            }else{
                connection.postValue(householdPaymentMethodList);
            }
        });

        return connection;
    }

    public LiveData<ChangePaymentMethodModel> callRemoveApi(int id, Context context) {
        final MutableLiveData<ChangePaymentMethodModel> mutableLiveData = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        ChangePaymentMethodModel changePaymentMethodModel = new ChangePaymentMethodModel();
        ksServices.callRemoveApi(id, context, new RemovePaymentCallBack() {
            @Override
            public void response(boolean status, String message, String code) {
                if (status){
                    changePaymentMethodModel.setStatus(true);
                    mutableLiveData.postValue(changePaymentMethodModel);
                }else {
                    changePaymentMethodModel.setStatus(false);
                    changePaymentMethodModel.setErrorCode(code);
                    changePaymentMethodModel.setMessage(message);
                    mutableLiveData.postValue(changePaymentMethodModel);
                }
            }
        });
        return mutableLiveData;
    }

    public LiveData<UpdatePaymentMethodModel> updatePaymentMethod(int id, int newPaymentMethodId, Context applicationContext) {
        final MutableLiveData<UpdatePaymentMethodModel> updatePaymentMethodModelMutableLiveData = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(applicationContext);
        UpdatePaymentMethodModel updatePaymentMethodModel = new UpdatePaymentMethodModel();
        ksServices.updatePaymentMethod(id, newPaymentMethodId, applicationContext, new UpdatePaymentMethodCallBack() {
            @Override
            public void response(boolean status, String message, String code) {
                if (status){
                    updatePaymentMethodModel.setStatus(true);
                    updatePaymentMethodModelMutableLiveData.postValue(updatePaymentMethodModel);
                }else {
                    updatePaymentMethodModel.setStatus(false);
                    updatePaymentMethodModel.setMessage(message);
                    updatePaymentMethodModelMutableLiveData.postValue(updatePaymentMethodModel);
                }
            }
        });
        return updatePaymentMethodModelMutableLiveData;
    }
}
