package com.astro.sott.activities.audio.ui;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.astro.sott.activities.audio.adapter.AudioAdapter;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityAudioLanguageBinding;
import com.astro.sott.modelClasses.dmsResponse.AudioLanguages;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;

import java.util.ArrayList;

public class AudioLanguageActivity extends BaseBindingActivity<ActivityAudioLanguageBinding> {
    private ResponseDmsModel responseDmsModel;
    private ArrayList<AudioLanguages> audioLanguageList;

    @Override
    protected ActivityAudioLanguageBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityAudioLanguageBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(getBinding().include.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Audio Language");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        responseDmsModel = AppCommonMethods.callpreference(this);
        audioLanguageList = responseDmsModel.getAudioLanguageList();
        setRecyclerProperties(audioLanguageList);


    }

    private void setRecyclerProperties(ArrayList<AudioLanguages> audioLanguageList) {
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        AudioAdapter audioAdapter = new AudioAdapter(audioLanguageList,this);
        getBinding().recyclerview.setAdapter(audioAdapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}