package com.astro.sott.repositories.deviceManagement;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.R;

import java.util.Arrays;
import java.util.List;

public class DeviceManagementRepository {

    public DeviceManagementRepository getInstance() {
        return new DeviceManagementRepository();
    }

    public LiveData<List<String>> createMoreList(Context context)

    {
        MutableLiveData<List<String>> sectionDataModelMutableLiveData = new MutableLiveData<>();



        String[] labels = context.getResources().getStringArray(R.array.application_setting_options);
        List<String> mList = Arrays.asList(labels);
        sectionDataModelMutableLiveData.postValue(mList);

        return sectionDataModelMutableLiveData;
    }
}
