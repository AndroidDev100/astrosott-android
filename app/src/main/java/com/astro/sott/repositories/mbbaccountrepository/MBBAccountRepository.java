package com.astro.sott.repositories.mbbaccountrepository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.callBacks.commonCallBacks.MBBAccountListCallBack;
import com.astro.sott.callBacks.commonCallBacks.DTVCallBack;
import com.astro.sott.networking.ksServices.KsServices;

public class MBBAccountRepository {

    private static MBBAccountRepository mBBAccountRepository;

    private MBBAccountRepository() {

    }

    public static MBBAccountRepository getInstance() {
        if (mBBAccountRepository == null) {
            mBBAccountRepository = new MBBAccountRepository();
        }

        return mBBAccountRepository;
    }

    public LiveData<String> saveDTVAccount(Context context, String dtvAccount) {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

        KsServices ksServices = new KsServices(context);
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
    }

    public LiveData<String> getMBBAccountList(Context context)
    {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);

        ksServices.getMBBAccountList(new MBBAccountListCallBack() {
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
}
