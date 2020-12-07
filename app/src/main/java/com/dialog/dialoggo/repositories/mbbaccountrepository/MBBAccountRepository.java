package com.dialog.dialoggo.repositories.mbbaccountrepository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dialog.dialoggo.callBacks.commonCallBacks.DTVCallBack;
import com.dialog.dialoggo.callBacks.commonCallBacks.DtvListCallBack;
import com.dialog.dialoggo.callBacks.commonCallBacks.MBBAccountListCallBack;
import com.dialog.dialoggo.networking.ksServices.KsServices;

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
