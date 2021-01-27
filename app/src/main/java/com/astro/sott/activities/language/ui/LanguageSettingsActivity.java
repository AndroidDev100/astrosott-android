package com.astro.sott.activities.language.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.activities.audio.ui.AudioLanguageActivity;
import com.astro.sott.activities.subtitle.ui.SubtitleLanguageActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivityLanguageSettingsBinding;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;

public class LanguageSettingsActivity extends BaseBindingActivity<ActivityLanguageSettingsBinding> {
    private String oldLang, newLang;

    @Override
    protected ActivityLanguageSettingsBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityLanguageSettingsBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oldLang = new KsPreferenceKey(LanguageSettingsActivity.this).getAppLangName();
        setClicks();

    }

    private void setClicks() {
        getBinding().rlAppLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LanguageSettingsActivity.this, ChangeLanguageActivity.class);
                startActivity(intent1);
            }
        });
        getBinding().rlAudioLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LanguageSettingsActivity.this, AudioLanguageActivity.class);
                startActivity(intent1);
            }
        });
        getBinding().rlSubtitleLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LanguageSettingsActivity.this, SubtitleLanguageActivity.class);
                startActivity(intent1);
            }
        });
        getBinding().backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void updateLang() {
        newLang = new KsPreferenceKey(LanguageSettingsActivity.this).getAppLangName();
        if (!oldLang.equals(newLang)) {
            oldLang = newLang;
            if (newLang.equalsIgnoreCase("ms")) {
                getBinding().title.setText("Pemilihan Bahasa");
                getBinding().tvAppLanguage.setText("Bahasa Aplikasi");
                getBinding().tvAudioLanguage.setText("Bahasa Audio");
                getBinding().tvSubtitleLanguage.setText("Bahasa Sari kata");
            } else {
                getBinding().title.setText("Language Selection");
                getBinding().tvAppLanguage.setText("App Language");
                getBinding().tvAudioLanguage.setText("Audio Language");
                getBinding().tvSubtitleLanguage.setText("Subtitle Language");

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLang();
    }
}