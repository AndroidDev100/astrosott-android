package com.astro.sott.repositories.applicationRepo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.astro.sott.R;

import java.util.Arrays;
import java.util.List;

public class ApplicationRepository {


    public LiveData<List<String>> createMoreList(Context context)

    {
        MutableLiveData<List<String>> sectionDataModelMutableLiveData = new MutableLiveData<>();


        String[] labels = context.getResources().getStringArray(R.array.application_setting_options);
        List<String> mList = Arrays.asList(labels);
        sectionDataModelMutableLiveData.postValue(mList);

        return sectionDataModelMutableLiveData;
    }
}
