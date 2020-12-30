package com.astro.sott.activities.subtitle.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;

import com.astro.sott.R;
import com.astro.sott.activities.audio.adapter.AudioAdapter;
import com.astro.sott.activities.subtitle.adapter.SubtitleAdapter;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivitySubtitleLanguageBinding;
import com.astro.sott.modelClasses.dmsResponse.AudioLanguages;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.modelClasses.dmsResponse.SubtitleLanguages;
import com.astro.sott.utils.commonMethods.AppCommonMethods;

import java.util.ArrayList;

public class SubtitleLanguageActivity extends BaseBindingActivity<ActivitySubtitleLanguageBinding> {
    private ResponseDmsModel responseDmsModel;
    private ArrayList<SubtitleLanguages> subtitleList;
    @Override
    protected ActivitySubtitleLanguageBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySubtitleLanguageBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setSupportActionBar(getBinding().include.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Subtitle Language");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        responseDmsModel = AppCommonMethods.callpreference(this);
        subtitleList = responseDmsModel.getSubtitleLanguageList();
        setRecyclerProperties(subtitleList);
    }
    private void setRecyclerProperties(ArrayList<SubtitleLanguages> subtitleLanguages) {
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        SubtitleAdapter audioAdapter = new SubtitleAdapter(subtitleLanguages,this);
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