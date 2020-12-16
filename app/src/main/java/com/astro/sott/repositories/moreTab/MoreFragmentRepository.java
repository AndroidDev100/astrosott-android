package com.astro.sott.repositories.moreTab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.content.res.Resources;

import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.R;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.networking.ksServices.KsServices;

import java.util.Arrays;
import java.util.List;

public class MoreFragmentRepository {


    public MoreFragmentRepository getInstance() {

        return new MoreFragmentRepository();
    }


    public LiveData<List<String>> createMoreList(Context context) {
        MutableLiveData<List<String>> sectionDataModelMutableLiveData = new MutableLiveData<>();
        //
        //    boolean login=SharedPrefHelper.getInstance(context.getApplicationContext()).getBoolean("LOGINSTATUS",false);

        boolean login = KsPreferenceKey.getInstance(context).getUserActive();
        String[] labels;
        if (login) {
            labels = context.getResources().getStringArray(R.array.more_list);

        } else {
            if (new KsPreferenceKey(context).getAppLangName().equalsIgnoreCase("en")) {
                labels = context.getResources().getStringArray(R.array.more_list_with_login);

            } else {
                labels = context.getResources().getStringArray(R.array.more_list_with_login_lang);

            }

        }
        try {
            List<String> mList = Arrays.asList(labels);
            sectionDataModelMutableLiveData.postValue(mList);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return sectionDataModelMutableLiveData;
    }

    public LiveData<CommonResponse> callLogoutApi(Context context) {
        final MutableLiveData<CommonResponse> mutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);
        ksServices.callLogoutApi((status, message) -> {
            if (status) {
                CommonResponse response = new CommonResponse();
                response.setStatus(true);
                mutableLiveData.postValue(response);
            }
        });
        return mutableLiveData;
    }

}
