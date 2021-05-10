package com.astro.sott.activities.appSettings.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import com.astro.sott.activities.audio.ui.AudioLanguageActivity;
import com.astro.sott.activities.language.ui.ChangeLanguageActivity;
import com.astro.sott.activities.notificationSetting.ui.NotificationSettingActivity;
import com.astro.sott.activities.subtitle.ui.SubtitleLanguageActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.activities.contentPreference.ui.ContentPreferenceActivity;
import com.astro.sott.databinding.AppSettingsBinding;

import static com.astro.sott.R.id.audio_language;
import static com.astro.sott.R.id.language;
import static com.astro.sott.R.id.subtitle_language;
import static com.astro.sott.R.id.tv_content_preferences;
import static com.astro.sott.R.id.tv_notification_settings;


public class AppSettingsActivity extends BaseBindingActivity<AppSettingsBinding> implements View.OnClickListener {
    private String oldLang, newLang;

    @Override
    public AppSettingsBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return AppSettingsBinding.inflate(inflater);
    }

    @Override
    protected void onResume() {
        super.onResume();

        newLang = new KsPreferenceKey(this).getAppLangName();
        if (!oldLang.equals(newLang)) {
            oldLang = newLang;
           /* if (newLang.equalsIgnoreCase("ms")) {
                getSupportActionBar().setTitle("Tetapan aplikasi");

            } else {*/
                getSupportActionBar().setTitle(getResources().getString(R.string.app_settings));

           // }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        oldLang = new KsPreferenceKey(this).getAppLangName();
//        new ToolBarHandler(this).setAppSettingAction(this, getBinding().toolbar);
        if (!new KsPreferenceKey(this).getDownloadOverWifi()) {
            getBinding().switchTheme.setChecked(false);
        } else {
            getBinding().switchTheme.setChecked(true);
        }

        setSupportActionBar(getBinding().include.toolbar);
       // String c = Resources.getConfiguration().locale.getLanguage();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_settings));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setClicks();
//        setTextViewDrawableColor(getBinding().tvNotificationSettings, this);

//        getBinding().tvVideoQuality.setOnClickListener(this);
        getBinding().tvNotificationSettings.setOnClickListener(this);
        getBinding().tvContentPreferences.setOnClickListener(this);
        getBinding().language.setOnClickListener(this);
        getBinding().audioLanguage.setOnClickListener(this);
        getBinding().subtitleLanguage.setOnClickListener(this);
        if (BuildConfig.FLAVOR.equalsIgnoreCase("prod"))
            getBinding().flavorTxt.setText(getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
        else
            getBinding().flavorTxt.setText(BuildConfig.FLAVOR + " " + BuildConfig.VERSION_CODE + " (" + BuildConfig.VERSION_NAME + ")");

        if (!KsPreferenceKey.getInstance(this).getUserActive())
            getBinding().tvNotificationSettings.setVisibility(View.GONE);
    }

    private void setClicks() {
        getBinding().switchTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (getBinding().switchTheme.isChecked()) {
                    new KsPreferenceKey(getApplicationContext()).setDownloadOverWifi(true);
                } else {
                    new KsPreferenceKey(getApplicationContext()).setDownloadOverWifi(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.tv_video_quality:
//              //  Intent intentVideo = new Intent(AppSettingsActivity.this, VideoQualityActivity.class);
//             //   startActivity(intentVideo);
//                break;
            case tv_notification_settings:
                Intent intentNotification = new Intent(AppSettingsActivity.this, NotificationSettingActivity.class);
                startActivity(intentNotification);
                break;
            case tv_content_preferences:
                Intent intent = new Intent(AppSettingsActivity.this, ContentPreferenceActivity.class);
                startActivity(intent);

                break;

            case language:
                Intent intent1 = new Intent(this, ChangeLanguageActivity.class);
                startActivity(intent1);
                break;
            case audio_language:
                Intent audioIntent = new Intent(this, AudioLanguageActivity.class);
                startActivity(audioIntent);
                break;
            case subtitle_language:
                Intent subtitleIntent = new Intent(this, SubtitleLanguageActivity.class);
                startActivity(subtitleIntent);
                break;
            default:

                break;
        }

    }

    private void changeLanguage() {
        try {
            String selectedLanguage = new KsPreferenceKey(AppSettingsActivity.this).getAppLangName();

            if (selectedLanguage.equalsIgnoreCase("en") || selectedLanguage.equalsIgnoreCase("")) {
                AppCommonMethods.updateLanguage("ms", this);
                ToastHandler.show("Language changed to Malay", getApplication().getApplicationContext());
            } else {
                AppCommonMethods.updateLanguage("en", this);
                ToastHandler.show("Language changed to English", getApplication().getApplicationContext());
            }
            // new ActivityLauncher(AppSettingsActivity.this).homeScreen(AppSettingsActivity.this, HomeActivity.class);

            //  finish();
        } catch (Exception exc) {
        }
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
