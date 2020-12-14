package com.astro.sott.activities.appSettings.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.astro.sott.activities.SelectAccount.UI.SelectDtvAccountActivity;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.notificationSetting.ui.NotificationSettingActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.activities.contentPreference.ui.ContentPreferenceActivity;
import com.astro.sott.databinding.AppSettingsBinding;

public class AppSettingsActivity extends BaseBindingActivity<AppSettingsBinding> implements View.OnClickListener {

    @Override
    public AppSettingsBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return AppSettingsBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        new ToolBarHandler(this).setAppSettingAction(this, getBinding().toolbar);


        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_settings));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

//        setTextViewDrawableColor(getBinding().tvNotificationSettings, this);

//        getBinding().tvVideoQuality.setOnClickListener(this);
        getBinding().tvNotificationSettings.setOnClickListener(this);
        getBinding().tvContentPreferences.setOnClickListener(this);
        getBinding().language.setOnClickListener(this);
        if (BuildConfig.FLAVOR.equalsIgnoreCase("prod"))
            getBinding().flavorTxt.setText(getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
        else
            getBinding().flavorTxt.setText(BuildConfig.FLAVOR + " " + BuildConfig.VERSION_CODE + " (" + BuildConfig.VERSION_NAME + ")");

        if (!KsPreferenceKey.getInstance(this).getUserActive())
            getBinding().tvNotificationSettings.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.tv_video_quality:
//              //  Intent intentVideo = new Intent(AppSettingsActivity.this, VideoQualityActivity.class);
//             //   startActivity(intentVideo);
//                break;
            case R.id.tv_notification_settings:
                Intent intentNotification = new Intent(AppSettingsActivity.this, NotificationSettingActivity.class);
                startActivity(intentNotification);
                break;
            case R.id.tv_content_preferences:
                Intent intent = new Intent(AppSettingsActivity.this, ContentPreferenceActivity.class);
                startActivity(intent);

                break;

            case R.id.language:
                changeLanguage();
                break;
            default:

                break;
        }

    }

    private void changeLanguage() {
        try {
            String selectedLanguage = new KsPreferenceKey(AppSettingsActivity.this).getAppLangName();

            if (selectedLanguage.equalsIgnoreCase("en")||selectedLanguage.equalsIgnoreCase("")) {
                AppCommonMethods.updateLanguage("ms", this);
            } else {
                AppCommonMethods.updateLanguage("en", this);
            }
            new ActivityLauncher(AppSettingsActivity.this).homeScreen(AppSettingsActivity.this, HomeActivity.class);

            finish();
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

//    private void setTextViewDrawableColor(TextView textView, Context context) {
//        for (Drawable drawable : textView.getCompoundDrawables()) {
//            if (drawable != null) {
//                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.more_item_title), PorterDuff.Mode.SRC_IN));
//
////                DrawableCompat.setTint(myImageView.getDrawable(), ContextCompat.getColor(context, R.color.another_nice_color));
//            }
//        }
//    }
}
