package com.astro.sott.activities.subtitle.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivitySubtitleLanguageBinding;

public class SubtitleLanguageActivity extends BaseBindingActivity<ActivitySubtitleLanguageBinding> {

    @Override
    protected ActivitySubtitleLanguageBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySubtitleLanguageBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }
}