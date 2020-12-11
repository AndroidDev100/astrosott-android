package com.astro.sott.repositories.dtv;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.activities.SelectAccount.SelectAccountModel.DtvMbbHbbModel;
import com.astro.sott.callBacks.commonCallBacks.DtvListCallBack;
import com.astro.sott.callBacks.commonCallBacks.HungamaResponse;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.callBacks.commonCallBacks.DTVCallBack;
import com.astro.sott.callBacks.commonCallBacks.MBBAccountCallBack;
import com.astro.sott.networking.ksServices.KsServices;

import java.util.List;

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
