package com.dialog.dialoggo.repositories.moreTab;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.res.Resources;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;

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
            labels = context.getResources().getStringArray(R.array.more_list_with_login
            );

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
