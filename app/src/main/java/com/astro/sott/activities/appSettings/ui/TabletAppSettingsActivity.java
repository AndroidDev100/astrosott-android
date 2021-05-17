package com.astro.sott.activities.appSettings.ui;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.astro.sott.activities.contentPreference.ui.ContentPreferenceActivity;
import com.astro.sott.activities.notificationSetting.ui.NotificationSettingActivity;
import com.astro.sott.activities.videoQuality.adapter.VideoQualityAdapter;
import com.astro.sott.activities.videoQuality.viewModel.VideoQualityViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.databinding.AppSettingsBinding;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.player.adapter.TrackItem;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.kaltura.client.types.InboxMessage;

import java.util.ArrayList;
import java.util.List;

import static com.astro.sott.R.id.tv_content_preferences;
import static com.astro.sott.R.id.tv_notification_settings;

public class TabletAppSettingsActivity extends BaseBindingActivity<AppSettingsBinding> implements View.OnClickListener, AlertDialogSingleButtonFragment.AlertDialogListener {
    private KsServices ksServices;

    private VideoQualityViewModel viewModel;
    private VideoQualityAdapter notificationAdapter;
    private List<InboxMessage> list;
    private ArrayList<TrackItem> arrayList;

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
        ksServices = new KsServices(this);
        callModel();
        connectionObserver();
        getBinding().tvContentPreferences.setOnClickListener(this);
        if (BuildConfig.FLAVOR.equalsIgnoreCase("prod"))
            getBinding().flavorTxt.setText(getResources().getString(R.string.version) + " " + BuildConfig.VERSION_NAME);
        else
            getBinding().flavorTxt.setText(BuildConfig.FLAVOR + " " + BuildConfig.VERSION_CODE + " (" + BuildConfig.VERSION_NAME + ")");
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().connection.rlConnection.setVisibility(View.GONE);
            arrayList = viewModel.getQualityList();
            UIinitialization();
            setAdapter();
            // modelCall();


        } else {
            noConnectionLayout();
        }


    }

    private void callModel() {
        viewModel = ViewModelProviders.of(this).get(VideoQualityViewModel.class);
    }

    private void UIinitialization() {
        if (KsPreferenceKey.getInstance(this).getUserActive()) {
            getBinding().notificationView.setVisibility(View.VISIBLE);
            getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
            ksServices.getNotificationSetting((status, response) -> runOnUiThread(() -> {

                if (status) {

                    if (response.isNotification()) {
                        getBinding().switchPush.setChecked(true);

                    } else {
                        getBinding().switchPush.setChecked(false);

                    }
                } else {
                    showDialog(response.getMessage());

                }
            }));
        } else {
            getBinding().notificationView.setVisibility(View.GONE);

        }
        getBinding().recyclerview.hasFixedSize();
        getBinding().recyclerview.setNestedScrollingEnabled(false);
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        getBinding().recyclerview.addItemDecoration(itemDecor);

        getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);

        getBinding().switchPush.setOnCheckedChangeListener((compoundButton, b) -> {

            getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
            ksServices.getNotificationSettingUpdate(getBinding().switchPush.isChecked(), (status, message) -> runOnUiThread(() -> {

                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                if (status) {

                    if (getBinding().switchPush.isChecked()) {
                        getBinding().switchPush.setChecked(true);
                    } else {
                        getBinding().switchPush.setChecked(false);

                    }

                } else {

                    if (getBinding().switchPush.isChecked()) {
                        getBinding().switchPush.setChecked(false);
                    } else {
                        getBinding().switchPush.setChecked(true);

                    }
                    showDialog(message);
                }
            }));

        });

    }

    private void setAdapter() {
        if (TextUtils.isEmpty(KsPreferenceKey.getInstance(this).getQualityName())) {
            arrayList.get(0).setSelected(true);
            KsPreferenceKey.getInstance(this).setQualityName(arrayList.get(0).getTrackName());
            KsPreferenceKey.getInstance(this).setQualityPosition(0);
        } else {
            arrayList.get(KsPreferenceKey.getInstance(this).getQualityPosition()).setSelected(true);
        }
        notificationAdapter = new VideoQualityAdapter(this, arrayList);
        getBinding().recyclerview.setAdapter(notificationAdapter);
    }

    private void noConnectionLayout() {
        getBinding().connection.rlConnection.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.tv_video_quality:
////                Intent intentVideo = new Intent(TabletAppSettingsActivity.this, VideoQualityActivity.class);
////                startActivity(intentVideo);
//
//                break;
            case tv_notification_settings:

                Intent intentNotification = new Intent(TabletAppSettingsActivity.this, NotificationSettingActivity.class);
                startActivity(intentNotification);

                break;
            case tv_content_preferences:

                Intent intent = new Intent(TabletAppSettingsActivity.this, ContentPreferenceActivity.class);
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
    private void showDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    @Override
    public void onFinishDialog() {

    }
}
