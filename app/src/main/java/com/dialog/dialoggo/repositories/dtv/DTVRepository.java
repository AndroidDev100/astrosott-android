package com.dialog.dialoggo.repositories.dtv;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.DtvMbbHbbModel;
import com.dialog.dialoggo.callBacks.commonCallBacks.DTVCallBack;
import com.dialog.dialoggo.callBacks.commonCallBacks.DtvListCallBack;
import com.dialog.dialoggo.callBacks.commonCallBacks.HungamaResponse;
import com.dialog.dialoggo.callBacks.commonCallBacks.MBBAccountCallBack;
import com.dialog.dialoggo.callBacks.otpCallbacks.DTVAccountCallback;
import com.dialog.dialoggo.modelClasses.DTVContactInfoModel;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;

import java.util.List;

import retrofit2.Response;

public class DTVRepository {

    private static DTVRepository dtvRepository;

    private DTVRepository() {

    }

    public static DTVRepository getInstance() {
        if (dtvRepository == null) {
            dtvRepository = new DTVRepository();
        }

        return dtvRepository;
    }

    public LiveData<String> saveDTVAccount(Context context, String dtvAccount, String fragmentType) {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

        KsServices ksServices = new KsServices(context);

        switch (fragmentType){

            case AppLevelConstants.DTV_TYPE_FRAGMENT:
                ksServices.saveDTVAccount(dtvAccount, new DTVCallBack() {
                    @Override
                    public void response(String value) {
                        mutableLiveData.postValue(value);
                    }

                    @Override
                    public void failure() {
                        mutableLiveData.postValue(null);
                    }
                });
                return mutableLiveData;

            case AppLevelConstants.MBB_TYPE_FRAGMENT:

                ksServices.saveMBBAccount(dtvAccount, new MBBAccountCallBack() {
                    @Override
                    public void response(String value) {
                        mutableLiveData.postValue(value);
                    }

                    @Override
                    public void failure() {
                        mutableLiveData.postValue(null);
                    }
                });
                return mutableLiveData;

            default:
                return null;

        }
    }
    public LiveData<String> saveDTVAccountData(Context context, String dtvAccount) {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

        KsServices ksServices = new KsServices(context);
        ksServices.saveDTVAccountData(dtvAccount, new DTVCallBack() {
            @Override
            public void response(String value) {
                mutableLiveData.postValue(value);
            }

            @Override
            public void failure() {
                mutableLiveData.postValue(null);
            }
        });
        return mutableLiveData;
    }

    public LiveData<String> getDtvAccountList(Context context)
    {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);

        ksServices.getDtvAccountList(context,new DtvListCallBack() {
            @Override
            public void response(String value) {
                if(value!="") {
                    mutableLiveData.postValue(value);
                }else {
                    mutableLiveData.postValue("");
                }
            }

            @Override
            public void failure() {
                mutableLiveData.postValue(null);
            }
        });
        return mutableLiveData;
    }

    public LiveData<String> updateDtvMbbHbbAccount(Context context, List<DtvMbbHbbModel> dtvMbbHbbModels) {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);

        ksServices.updateDtvMbbHbbAccount(context, dtvMbbHbbModels, new DTVCallBack() {
            @Override
            public void response(String value) {
                mutableLiveData.postValue(value);
            }

            @Override
            public void failure() {
            mutableLiveData.postValue(null);
            }
        });

        return mutableLiveData;
    }

    public LiveData<String> getHungamaUrl(Context context, String providerExternalContentId) {
        MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
       ksServices.getHungamaUrl(providerExternalContentId,context, new HungamaResponse() {
           @Override
           public void onSuccess(String url) {
                if(!url.equalsIgnoreCase("") && !url.equalsIgnoreCase("NA")){
                    mutableLiveData.postValue(url);
                }else {
                    mutableLiveData.postValue(null);
                }
           }

           @Override
           public void onFailureFailure() {
                mutableLiveData.postValue(null);
           }

           @Override
           public void onError(Throwable ex) {
                mutableLiveData.postValue(null);
           }
       });
        return mutableLiveData;
    }
}
