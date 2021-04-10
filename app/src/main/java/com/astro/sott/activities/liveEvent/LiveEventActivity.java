package com.astro.sott.activities.liveEvent;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.movieDescription.viewModel.MovieDescriptionViewModel;
import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.activities.subscriptionActivity.ui.SubscriptionDetailActivity;
import com.astro.sott.callBacks.kalturaCallBacks.ProductPriceStatusCallBack;
import com.astro.sott.databinding.ActivityLiveEventBinding;
import com.astro.sott.fragments.dialog.PlaylistDialogFragment;
import com.astro.sott.player.entitlementCheckManager.EntitlementCheck;
import com.astro.sott.thirdParty.conViva.ConvivaManager;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.userInfo.UserInfo;
import com.conviva.sdk.ConvivaSdkConstants;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.LoginActivity;
import com.astro.sott.activities.parentalControl.viewmodels.ParentalControlViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.baseModel.RailBaseFragment;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.ParentalDialogCallbacks;
import com.astro.sott.callBacks.commonCallBacks.PlaylistCallback;
import com.astro.sott.databinding.MovieScreenBinding;
import com.astro.sott.fragments.detailRailFragment.DetailRailFragment;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.modelClasses.dmsResponse.ParentalLevels;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.networking.refreshToken.RefreshKS;
import com.astro.sott.player.geoBlockingManager.GeoBlockingCheck;
import com.astro.sott.player.houseHoldCheckManager.HouseHoldCheck;
import com.astro.sott.player.ui.PlayerActivity;
import com.astro.sott.repositories.player.PlayerRepository;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.CommonPlaylistDialog;
import com.astro.sott.utils.helpers.DialogHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.StringBuilderHolder;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.gson.Gson;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.PersonalList;
import com.kaltura.client.types.StringValue;
import com.kaltura.client.types.UserAssetRule;
import com.kaltura.client.types.Value;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LiveEventActivity extends BaseBindingActivity<ActivityLiveEventBinding> implements DetailRailClick, AlertDialogSingleButtonFragment.AlertDialogListener {
    ArrayList<ParentalLevels> parentalLevels;
    private RailCommonData railData;
    private Asset asset;
    private String vodType;

    private int layoutType, playlistId = 1;
    private DoubleValue doubleValue;
    private boolean xofferWindowValue = false, playbackControlValue = false;
    private MovieDescriptionViewModel viewModel;
    private Map<String, MultilingualStringValueArray> map;
    private Map<String, Value> yearMap;
    private FragmentManager manager;
    private long assetId;
    private int assetType;
    private List<PersonalList> playlist = new ArrayList<>();
    private List<PersonalList> personalLists = new ArrayList<>();
    private RailBaseFragment baseRailFragment;
    private String trailor_url = "";
    private List<Integer> list;
    private String name, titleName, idfromAssetWatchlist;
    private boolean isActive, isAdded;
    private boolean iconClicked = false;
    private String image_url = "";
    private long lastClickTime = 0;
    private int errorCode = -1;
    private boolean playerChecksCompleted = false;
    private int assetRuleErrorCode = -1;
    private boolean isParentalLocked = false;
    private String defaultParentalRating = "";
    private String userSelectedParentalRating = "";
    private int userSelectedParentalPriority;
    private int priorityLevel;
    private int assetRestrictionLevel;
    private boolean assetKey = false;
    private boolean isDtvAdded = false;


    @Override
    public ActivityLiveEventBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityLiveEventBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentalLevels = new ArrayList<>();
        connectionObserver();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getMediaType(asset, railData);
    }

    private void intentValues() {
        layoutType = getIntent().getIntExtra(AppLevelConstants.LAYOUT_TYPE, 0);
        if (getIntent().getExtras() != null) {
            railData = getIntent().getExtras().getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
            if (railData != null) {
                Gson gson = new Gson();
                asset = railData.getObject();
                getDataFromBack(railData, layoutType);
            }
        }
    }

    private void getDataFromBack(RailCommonData commonRailData, int layout) {
        getBinding().watchButton.setVisibility(View.GONE);
        railData = commonRailData;
        asset = railData.getObject();
        layoutType = layout;
        assetId = asset.getId();
        name = asset.getName();
        titleName = name;
        isActive = UserInfo.getInstance(this).isActive();
        map = asset.getTags();
        setPlayerFragment();
        getMediaType(asset, railData);
        if (playbackControlValue)
            checkEntitleMent(railData);


    }


    private void showAlertDialog(String msg) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance("", msg, getString(R.string.ok));
        alertDialog.setAlertDialogCallBack(alertDialog::dismiss);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void setPlayerFragment() {

        manager = getSupportFragmentManager();
        //getBinding().playButton.setClickable(true);
        getBinding().watchButton.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            if (vodType.equalsIgnoreCase(EntitlementCheck.FREE)) {
                playerChecks(railData);
            } else if (vodType.equalsIgnoreCase(EntitlementCheck.SVOD)) {
                if (UserInfo.getInstance(this).isActive()) {
                    fileId = AppCommonMethods.getFileIdOfAssest(railData.getObject());
                    if (!fileId.equalsIgnoreCase("")) {
                        Intent intent = new Intent(this, SubscriptionDetailActivity.class);
                        intent.putExtra(AppLevelConstants.FILE_ID_KEY, fileId);
                        startActivity(intent);
                    }
                } else {
                    new ActivityLauncher(LiveEventActivity.this).astrLoginActivity(LiveEventActivity.this, AstrLoginActivity.class);
                }

            }


        });

    }


    private void checkErrors() {
        if (playerChecksCompleted) {
            if (assetRuleErrorCode == AppLevelConstants.GEO_LOCATION_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeforGeoLocation(1, LiveEventActivity.this));
            } else if (errorCode == AppLevelConstants.USER_ACTIVE_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(LiveEventActivity.this));
            } else if (errorCode == AppLevelConstants.NO_ERROR) {
                if (KsPreferenceKey.getInstance(this).getUserActive()) {
                    parentalCheck(railData);
                } else {
                    startPlayer();
                }
            }
        } else {
            DialogHelper.showAlertDialog(this, getString(R.string.play_check_message), getString(R.string.ok), this);
        }
    }

    private void parentalCheck(RailCommonData railData) {
        if (KsPreferenceKey.getInstance(this).getUserActive()) {
            if (KsPreferenceKey.getInstance(this).getParentalActive()) {
                ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(getApplicationContext());
                defaultParentalRating = responseDmsModel.getParams().getDefaultParentalLevel();
                userSelectedParentalRating = KsPreferenceKey.getInstance(getApplicationContext()).getUserSelectedRating();
                if (!userSelectedParentalRating.equalsIgnoreCase("")) {
                    assetKey = AssetContent.getAssetKey(asset.getTags(), userSelectedParentalRating, getApplicationContext());
                    if (assetKey) {
                        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                        checkOnlyDevice(railData);
                    } else {
                        validateParentalPin(railData);
                    }

                } else {
                    assetKey = AssetContent.getAssetKey(asset.getTags(), defaultParentalRating, getApplicationContext());
                    if (assetKey) {
                        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                        checkOnlyDevice(railData);
                    } else {
                        validateParentalPin(railData);
                    }
                }
            } else {
                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                checkOnlyDevice(railData);
            }
        }
    }

    private void validateParentalPin(RailCommonData railData) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.showValidatePinDialog(LiveEventActivity.this, null, "MOVIE", new ParentalDialogCallbacks() {
                    @Override
                    public void onPositiveClick(String pinText) {
                        ParentalControlViewModel parentalViewModel = ViewModelProviders.of(LiveEventActivity.this).get(ParentalControlViewModel.class);

                        parentalViewModel.validatePin(LiveEventActivity.this, pinText).observe(LiveEventActivity.this, commonResponse -> {
                            if (commonResponse.getStatus()) {
                                DialogHelper.hideValidatePinDialog();
                                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                                playerChecksCompleted = true;
                                //checkErrors();
                                checkOnlyDevice(railData);
                            } else {
                                Toast.makeText(LiveEventActivity.this, getString(R.string.incorrect_parental_pin), Toast.LENGTH_LONG).show();
                                assetRuleErrorCode = AppLevelConstants.PARENTAL_BLOCK;

                            }
                        });
                    }

                    @Override
                    public void onNegativeClick() {
                        DialogHelper.hideValidatePinDialog();
                    }
                });
            }
        });


    }

    private void checkOnlyDevice(RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(LiveEventActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> {
                        startPlayer();
                    });
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(LiveEventActivity.this).refreshKS(response -> checkDevice(railData));
                    } else {
                        showDialog(commonResponse.getMessage());
                    }
                }
            }

        });
    }


    private void startPlayer() {
        try {

            //  ConvivaManager.getConvivaAdAnalytics(this);
            Intent intent = new Intent(LiveEventActivity.this, PlayerActivity.class);
            intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
            startActivity(intent);

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }


    private void playerChecks(final RailCommonData railData) {
        new GeoBlockingCheck().aseetAvailableOrNot(LiveEventActivity.this, railData.getObject(), (status, response, totalCount, errorcode, message) -> {
            if (status) {
                if (totalCount != 0) {
                    checkBlockingErrors(response);
                } else {
                    playerChecksCompleted = true;
                    checkErrors();
                }
            } else {
                showDialog(message);
            }
        });
    }

    private void checkBlockingErrors(Response<ListResponse<UserAssetRule>> response) {
        if (response != null && response.results != null && response.results.getObjects() != null) {
            for (UserAssetRule userAssetRule :
                    response.results.getObjects()) {
                switch (userAssetRule.getRuleType()) {
                    case GEO:
                        assetRuleErrorCode = AppLevelConstants.GEO_LOCATION_ERROR;
                        playerChecksCompleted = true;
                        checkErrors();
                        return;

                    default:
                        playerChecksCompleted = true;
                        checkErrors();
                        break;
                }
            }
        }
    }

    private String fileId = "";

    private void checkEntitleMent(final RailCommonData railCommonData) {


        fileId = AppCommonMethods.getFileIdOfAssest(railData.getObject());

        new EntitlementCheck().checkAssetPurchaseStatus(LiveEventActivity.this, fileId, (apiStatus, purchasedStatus, vodType, purchaseKey, errorCode, message) -> {
            this.errorCode = AppLevelConstants.NO_ERROR;
            if (apiStatus) {
                if (purchasedStatus) {
                    runOnUiThread(() -> {
                        getBinding().watchButton.setBackground(getResources().getDrawable(R.drawable.gradient_free));
                        getBinding().playText.setText(getResources().getString(R.string.watch_now));
                        getBinding().watchButton.setVisibility(View.VISIBLE);
                        getBinding().starIcon.setVisibility(View.GONE);
                        getBinding().playText.setTextColor(getResources().getColor(R.color.black));


                    });
                    this.vodType = EntitlementCheck.FREE;

                } else {
                    if (vodType.equalsIgnoreCase(EntitlementCheck.SVOD)) {
                        if (xofferWindowValue) {
                            runOnUiThread(() -> {
                                getBinding().watchButton.setBackground(getResources().getDrawable(R.drawable.gradient_svod));
                                getBinding().playText.setText(getResources().getString(R.string.become_vip));
                                getBinding().watchButton.setVisibility(View.VISIBLE);
                                getBinding().starIcon.setVisibility(View.VISIBLE);
                            });
                        }
                        this.vodType = EntitlementCheck.SVOD;

                    } else if (vodType.equalsIgnoreCase(EntitlementCheck.TVOD)) {
                        if (xofferWindowValue) {
                            runOnUiThread(() -> {
                                getBinding().watchButton.setBackground(getResources().getDrawable(R.drawable.gradient_svod));
                                getBinding().playText.setText(getResources().getString(R.string.rent_movie));
                                getBinding().watchButton.setVisibility(View.VISIBLE);
                                getBinding().starIcon.setVisibility(View.GONE);

                            });
                        }

                        this.vodType = EntitlementCheck.TVOD;


                    }
                }

            } else {

            }
        });

    }

    private void checkDevice(final RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(LiveEventActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> checkEntitleMent(railData));
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(LiveEventActivity.this).refreshKS(response -> checkDevice(railData));
                    } else {
                        showDialog(commonResponse.getMessage());
                    }
                }
            }
        });
    }

    private void getMediaType(Asset asset, RailCommonData railCommonData) {
        setMovieMetaData(asset, 0);
    }


    private void getRefId(final int type, Asset asset) {
      /*  if (asset.getExternalId() != null && !asset.getExternalId().equalsIgnoreCase("")) {
            callTrailorAPI(asset.getExternalId(), type);
        }*/
    }


    private StringBuilder stringBuilder;

    private void setMetas() {
        //  getDuration();
        ///  getMovieYear();
        try {
            getBinding().programTitle.setText(asset.getName());
            getBinding().descriptionText.setText(asset.getDescription());
            stringBuilder = new StringBuilder();
            stringBuilder.append(viewModel.getStartDate(asset.getStartDate()) + " - " + AppCommonMethods.getEndTime(asset.getEndDate()) + " | ");
            getImage();
            getGenre();
            getXofferWindow();
            getPlayBackControl();
        } catch (Exception e) {

        }

    }

    private void getGenre() {


        ///"EEE, d MMM yyyy HH:mm:ss Z"


        viewModel.getGenreLivedata(asset.getTags()).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                if (!TextUtils.isEmpty(s)) {
                    stringBuilder.append(s + " | ");
                }
                getParentalRating();
            }
        });


    }

    private void getParentalRating() {
        if (!TextUtils.isEmpty(AssetContent.getParentalRating(asset.getTags()))) {

            stringBuilder.append(AssetContent.getParentalRating(asset.getTags()));

        }
        String value = stringBuilder.toString();
       /* if (value.length() > 0) {
            value = StringBuilderHolder.getInstance().getText().substring(0, value.length() - 2);
        }*/
        getBinding().metas.setText(value);
        StringBuilderHolder.getInstance().clear();

    }

    private void getImage() {
        if (asset.getImages().size() > 0) {
            for (int i = 0; i < asset.getImages().size(); i++) {
                if (asset.getImages().get(i).getRatio().equals("16x9")) {
                    String image_url = asset.getImages().get(i).getUrl();
                    String final_url = image_url + AppLevelConstants.WIDTH + (int) getResources().getDimension(R.dimen.detail_image_width) + AppLevelConstants.HEIGHT + (int) getResources().getDimension(R.dimen.carousel_image_height) + AppLevelConstants.QUALITY;
                    ImageHelper.getInstance(getBinding().playerImage.getContext()).loadImageToPotrait(getBinding().playerImage, final_url, R.drawable.square1);
                }
            }
        }

    }

    private void callTrailorAPI(String ref_id, final int type) {
        if (type == 0) {
            viewModel.getTrailorURL(ref_id, asset.getType()).observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String _url) {
                    if (!TextUtils.isEmpty(_url)) {
                        trailor_url = _url;
                    }

                }
            });
        } else {
            viewModel.getAssetFromTrailor(ref_id, asset.getType()).observe(this, asset -> {
                if (asset != null) {
                    AssetContent.getVideoResolution(asset.getTags()).observe(this, new Observer<String>() {
                        @Override
                        public void onChanged(@Nullable String videoResolution) {
                            AssetContent.getUrl(asset, videoResolution);
                            //    setMovieMetaData(asset, type);
                        }
                    });

                }
            });
        }

    }


    private void setMovieMetaData(Asset asset, int type) {
        //PrintLogging.printLog(this.getClass(),"", "valuessId" +);
        setMetaDataValues(asset, type);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        getBinding().share.setOnClickListener(view -> {

            /*if (SystemClock.elapsedRealtime() - lastClickTime < AppLevelConstants.SHARE_DIALOG_DELAY) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            openShareDialouge();*/

        });
        // setRailFragment();
        setRailBaseFragment();
    }


    private void setRailBaseFragment() {
        FragmentManager fm = getSupportFragmentManager();
        DetailRailFragment detailRailFragment = new DetailRailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        detailRailFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.rail_fragment, detailRailFragment).commitNow();
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(MovieDescriptionViewModel.class);

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

            modelCall();
            intentValues();
            manager = getSupportFragmentManager();

        } else {
            setConnectionLayout();
        }
    }


    private void setMetaDataValues(Asset asset, int type) {
        map = asset.getTags();
        yearMap = asset.getMetas();

        PrintLogging.printLog(this.getClass(), "", "YearMapIS" + map.get("SubtitleLanguage"));

        StringBuilderHolder.getInstance().clear();

        setMetas();

        if (type == 1) {

            PrintLogging.printLog(this.getClass(), "type 1", "");
        } else {
            getRefId(0, asset);
        }

        assetId = asset.getId();
        assetType = asset.getType();


    }


    private void getPlayBackControl() {
        if (yearMap != null) {
            playbackControlValue = viewModel.getPlayBackControl(yearMap);
        } else {
            playbackControlValue = true;
        }
    }


    private void getXofferWindow() {
        StringValue stringValue = null;
        String xofferValue = "";
        if (yearMap != null) {
            stringValue = (StringValue) yearMap.get(AppLevelConstants.XOFFERWINDOW);
        }
        if (stringValue != null) {
            xofferValue = stringValue.getValue();
        }
        if (!xofferValue.equalsIgnoreCase("")) {
            xofferWindowValue = viewModel.isXofferWindow(xofferValue);
        } else {
            xofferWindowValue = true;
        }
    }


    private void getMovieRating() {


        if (!TextUtils.isEmpty(AssetContent.getParentalRating(map))) {

            StringBuilderHolder.getInstance().append(AssetContent.getParentalRating(map));
            StringBuilderHolder.getInstance().append(" | ");

        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (KsPreferenceKey.getInstance(this).getUserActive()) {
                if (NetworkConnectivity.isOnline(this)) {
                    this.titleName = name;
                    isActive = true;
                }
            }
        } catch (IllegalStateException e) {
            PrintLogging.printLog("ExceptionIs", e.getMessage());
        }
    }

    private void setConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

    }


    private void openShareDialouge() {
        AppCommonMethods.openShareDialog(this, asset, getApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserInfo.getInstance(this).isActive()) {
            if (NetworkConnectivity.isOnline(this)) {
                titleName = name;
                isActive = true;
                // getDataFromBack(railData, layoutType);

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PlayerRepository.getInstance().releasePlayer();

    }


    private void setBannerImage(Asset asset) {

        StringBuilderHolder.getInstance().clear();
        for (int i = 0; i < asset.getImages().size(); i++) {
            if (asset.getImages().get(i).getRatio().equals("1:1")) {


                StringBuilderHolder.getInstance().append(asset.getImages().get(i).getUrl());
                StringBuilderHolder.getInstance().append(AppLevelConstants.WIDTH);
                StringBuilderHolder.getInstance().append("" + (int) getResources().getDimension(R.dimen.carousel_image_width));
                StringBuilderHolder.getInstance().append(AppLevelConstants.HEIGHT);
                StringBuilderHolder.getInstance().append("" + (int) getResources().getDimension(R.dimen.carousel_image_height));
                StringBuilderHolder.getInstance().append(AppLevelConstants.QUALITY);
                Log.d("ImageUrlIs", asset.getImages().get(i).getUrl());
                break;
            }
        }
        getBinding().backImg.setOnClickListener(view -> onBackPressed());


    }

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


    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {
        getDataFromBack(commonData, layoutType);
        isActive = UserInfo.getInstance(this).isActive();
    }


}

