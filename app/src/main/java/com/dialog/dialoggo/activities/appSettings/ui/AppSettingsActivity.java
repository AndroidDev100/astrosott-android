package com.dialog.dialoggo.activities.appSettings.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.dialog.dialoggo.BuildConfig;
import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.contentPreference.ui.ContentPreferenceActivity;
import com.dialog.dialoggo.activities.notificationSetting.ui.NotificationSettingActivity;
import com.dialog.dialoggo.activities.videoQuality.ui.VideoQualityActivity;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.databinding.AppSettingsBinding;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;

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
            default:

                break;
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
