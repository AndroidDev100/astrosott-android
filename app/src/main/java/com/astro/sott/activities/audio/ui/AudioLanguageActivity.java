package com.astro.sott.activities.audio.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityAudioLanguageBinding;

public class AudioLanguageActivity extends BaseBindingActivity<ActivityAudioLanguageBinding> {

    @Override
    protected ActivityAudioLanguageBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityAudioLanguageBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
}