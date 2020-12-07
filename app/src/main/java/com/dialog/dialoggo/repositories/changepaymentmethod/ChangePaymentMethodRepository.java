package com.dialog.dialoggo.repositories.changepaymentmethod;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dialog.dialoggo.activities.changePaymentMethod.model.ChangePaymentMethodModel;
import com.dialog.dialoggo.activities.changePaymentMethod.model.UpdatePaymentMethodModel;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.callBacks.commonCallBacks.RemovePaymentCallBack;
import com.dialog.dialoggo.callBacks.commonCallBacks.UpdatePaymentMethodCallBack;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.Entitlement;
import com.kaltura.client.types.HouseholdPaymentMethod;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.Subscription;
import com.kaltura.client.utils.response.base.Response;

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
