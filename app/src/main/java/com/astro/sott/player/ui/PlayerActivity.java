package com.astro.sott.player.ui;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.WindowFocusCallback;
import com.astro.sott.thirdParty.conViva.ConvivaManager;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.R;
import com.astro.sott.databinding.PlayerActivityBinding;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.shimmer.Constants;
import com.kaltura.client.types.Asset;

public class PlayerActivity extends BaseBindingActivity<PlayerActivityBinding> {

    private DTPlayer fragment;
    private RailCommonData railData;
    private String assetURL, programName = "";
    private WindowFocusCallback windowFocusListner;
    private Boolean isLivePlayer = false;

    @Override
    public PlayerActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return PlayerActivityBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        connectionObserver();
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
            setPlayerFragment();
        }
    }


    //initialize player fragment
    private void setPlayerFragment() {
        FragmentManager manager = getSupportFragmentManager();
        fragment = (DTPlayer) manager.findFragmentById(R.id.player_fragment);
        if (fragment != null) {
            windowFocusListner = fragment;
            fragment.getBinding().ivCancel.setOnClickListener(view -> onBackPressed());
            isLivePlayer = getIntent().getBooleanExtra("isLivePlayer", false);
            programName = getIntent().getStringExtra(AppLevelConstants.PROGRAM_NAME);
            if (getIntent().getExtras() != null) {
                railData = getIntent().getExtras().getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
                if (railData != null) {
                    Asset asset = railData.getObject();
                    getUrlToPlay(asset);
                }
            }
        }
    }

    private void getUrlToPlay(final Asset asset) {
        getURL(asset);
    }

    private void getURL(Asset asset) {
        AssetContent.getVideoResolution(asset.getTags()).observe(this, videoResolution -> {
            if (asset.getMediaFiles() != null) {
                for (int i = 0; i < asset.getMediaFiles().size(); i++) {
                    String assetUrl = asset.getMediaFiles().get(i).getType();
                    if (videoResolution.equals(AppConstants.HD)) {
                        if (assetUrl.equals(AppConstants.Mobile_Dash_HD)) {
                            Constants.urlToplay = asset.getMediaFiles().get(i).getUrl();
                            Constants.assetUrl = asset.getMediaFiles().get(i).getUrl();
                            assetURL = asset.getMediaFiles().get(i).getUrl();
                            break;
                        }
                    } else {
                        if (assetUrl.equals(AppConstants.Mobile_Dash_SD)) {
                            Constants.urlToplay = asset.getMediaFiles().get(i).getUrl();
                            Constants.assetUrl = asset.getMediaFiles().get(i).getUrl();
                            assetURL = asset.getMediaFiles().get(i).getUrl();
                            break;
                        }
                    }
                }
            } else {
                assetURL = "";
            }
            if (isLivePlayer) {
                fragment.getUrl(Constants.assetUrl, asset, railData.getProgress(), isLivePlayer, programName);

            } else {
                fragment.getUrl(Constants.assetUrl, asset, railData.getProgress(), isLivePlayer, "");

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("CONFIG", String.valueOf(newConfig.orientation));

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ViewGroup.LayoutParams params2 = getBinding().playerLayout.getLayoutParams();
            params2.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params2.height = ViewGroup.LayoutParams.MATCH_PARENT;
            getBinding().playerLayout.requestLayout();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ViewGroup.LayoutParams params2 = getBinding().playerLayout.getLayoutParams();
            params2.width = 0;
            params2.height = 0;
            getBinding().playerLayout.requestLayout();

        }

    }

    private void checkAutoRotation() {
        if (android.provider.Settings.System.getInt(getApplicationContext().getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (windowFocusListner != null)
            windowFocusListner.windowFocusChange(hasFocus);
        checkAutoRotation();

    }

    @Override
    protected void onStop() {
        super.onStop();
        ConvivaManager.convivaPlayerAppBackgrounded(this);
    }
}
