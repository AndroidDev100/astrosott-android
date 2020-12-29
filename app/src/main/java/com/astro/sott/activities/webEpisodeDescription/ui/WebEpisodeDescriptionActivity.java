package com.astro.sott.activities.webEpisodeDescription.ui;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.astro.sott.activities.parentalControl.viewmodels.ParentalControlViewModel;
import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.callBacks.commonCallBacks.ParentalDialogCallbacks;
import com.astro.sott.fragments.dialog.PlaylistDialogFragment;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.player.entitlementCheckManager.EntitlementCheck;
import com.astro.sott.player.geoBlockingManager.GeoBlockingCheck;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.CommonPlaylistDialog;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.LoginActivity;
import com.astro.sott.activities.webEpisodeDescription.adapter.WebEpisodeDescriptionCommonAdapter;
import com.astro.sott.activities.webEpisodeDescription.viewModel.WebEpisodeDescriptionViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.PlaylistCallback;
import com.astro.sott.databinding.ActivityWebEpisodeDescriptionBinding;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.modelClasses.dmsResponse.ParentalLevels;
import com.astro.sott.networking.refreshToken.RefreshKS;
import com.astro.sott.player.houseHoldCheckManager.HouseHoldCheck;
import com.astro.sott.player.ui.PlayerActivity;
import com.astro.sott.repositories.player.PlayerRepository;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.DialogHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.StringBuilderHolder;
import com.astro.sott.utils.helpers.shimmer.Constants;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.PersonalList;
import com.kaltura.client.types.UserAssetRule;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class WebEpisodeDescriptionActivity extends BaseBindingActivity<ActivityWebEpisodeDescriptionBinding> implements DetailRailClick, AlertDialogSingleButtonFragment.AlertDialogListener, PlaylistDialogFragment.EditDialogListener, PlaylistCallback {
    private final List<AssetCommonBean> loadedList = new ArrayList<>();
    private final Handler mHandler = new Handler();
    private WebEpisodeDescriptionCommonAdapter adapter;
    private int tempCount = 0;
    private List<Integer> seriesNumberList;
    private List<AssetCommonBean> clipList;
    private List<VIUChannel> channelList;
    private List<VIUChannel> dtChannelsList;
    private int seasonCounter = 0;
    private List<Integer> list;

    private int layoutType, playlistId = 1;

    private int counter = 0;
    private int loopend = 0;
    private List<PersonalList> playlist = new ArrayList<>();
    private RailCommonData railData;
    private Asset asset;
    private WebEpisodeDescriptionViewModel viewModel;
    private Map<String, MultilingualStringValueArray> map;
    private long assetId;
    private FragmentManager manager;
    private String idofasset, name, titleName, id, concatedTitleName, idfromAssetWatchlist;
    private boolean isActive, isAdded;
    private boolean iconClicked = false;
    private List<PersonalList> personalLists = new ArrayList<>();

    private int seriesMediaType = 0;
    private String image_url = "";
    private long lastClickTime;
    private int errorCode = -1;
    private boolean playerChecksCompleted = false;
    private int assetRuleErrorCode = -1;
    private boolean isParentalLocked = false;
    private String defaultParentalRating = "";
    private String userSelectedParentalRating = "";
    private int userSelectedParentalPriority;
    private int priorityLevel;
    private int assetRestrictionLevel;
    ArrayList<ParentalLevels> parentalLevels;
    private boolean assetKey = false;
    private boolean isDtvAdded = false;

    @Override
    public ActivityWebEpisodeDescriptionBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityWebEpisodeDescriptionBinding.inflate(inflater);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentalLevels = new ArrayList<>();
        connectionObserver();
        getBinding().myRecyclerView.hasFixedSize();
        getBinding().myRecyclerView.setNestedScrollingEnabled(false);
        getBinding().myRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
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
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            modelCall();
            intentValues();
        } else {
            noConnectionLayout();
        }
    }

    private void getDuration() {
        String duraton = AppCommonMethods.getURLDuration(asset);
        if (duraton.length() > 0) {
//            StringBuilderHolder.getInstance().clear();
            StringBuilderHolder.getInstance().append(duraton);
            StringBuilderHolder.getInstance().append(" | ");

//            getBinding().duration.setText(StringBuilderHolder.getInstance().getText());
        }
//        getBinding().duration.setText("| " + duraton);
    }

    private void intentValues() {
        layoutType = getIntent().getIntExtra(AppLevelConstants.LAYOUT_TYPE, 0);
        if (getIntent().getExtras() != null) {
            railData = getIntent().getExtras().getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
            if (railData != null) {
                asset = railData.getObject();
                setMetaData(railData, layoutType);
            }
        }
    }

    private void setMetaData(RailCommonData commonRailData, int layout) {
        railData = commonRailData;
        //  AllChannelManager.getInstance().setRailCommonData(railData);
        layoutType = layout;
        asset = railData.getObject();
        getBinding().setMovieAssestModel(asset);
        getBinding().setMovieAssestModel(asset);
        map = asset.getTags();
        assetId = asset.getId();
        name = asset.getName();
//        setBannerImage(asset);
        getMovieCasts();
        getMovieCrews();
//        getMovieGenres();
        callSpecificAsset(assetId);


        StringBuilderHolder.getInstance().clear();

        setMetas();

        Constants.id = asset.getId();
        Constants.assetType = asset.getType();
        Constants.assetId = (int) Constants.id;

        seriesMediaType = railData.getSeriesType();
        if (seriesMediaType == 0) {
            seriesMediaType = MediaTypeConstant.getDrama(WebEpisodeDescriptionActivity.this);
        }

        setHungamaTag(asset);


        setExpandable();
        getBinding().shareWith.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < AppLevelConstants.SHARE_DIALOG_DELAY) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            openShareDialouge();
        });

        getBinding().watchList.setOnClickListener(view -> {
            boolean isActive = KsPreferenceKey.getInstance(getApplicationContext()).getUserActive();
            iconClicked = true;
            playlist.clear();

            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            if (NetworkConnectivity.isOnline(getApplication())) {
                if (KsPreferenceKey.getInstance(WebEpisodeDescriptionActivity.this).getUserActive()) {
                    callwatchlistApi();
                } else {
                    new ActivityLauncher(WebEpisodeDescriptionActivity.this).loginActivity(WebEpisodeDescriptionActivity.this, LoginActivity.class, 0, "");
                }
            } else {
                ToastHandler.show(getResources().getString(R.string.no_internet_connection), WebEpisodeDescriptionActivity.this);

            }
        });

        loadDataFromModel();
        manager = getSupportFragmentManager();

        setPlayerFragment();
    }

    private void setHungamaTag(Asset asset) {
        boolean isProviderAvailable = AssetContent.getHungamaTag(asset.getTags());
        if (isProviderAvailable) {
            getBinding().hungama.setVisibility(View.VISIBLE);
        } else {
            getBinding().hungama.setVisibility(View.GONE);
        }
    }

    private void callSpecificAsset(long assetId) {
        AppCommonMethods.setImages(railData, getApplicationContext(), getBinding().webseriesimage);
    }


    private void showAlertDialog(String msg) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance("", msg, getString(R.string.ok));
        alertDialog.setAlertDialogCallBack(alertDialog::dismiss);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void setPlayerFragment() {
        manager = getSupportFragmentManager();

        getBinding().ivPlayIcon.setClickable(true);
        getBinding().ivPlayIcon.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            getBinding().includeProgressbar.progressBar.setOnClickListener(view1 -> {

            });
//            runOnUiThread(() -> DialogHelper.openDialougeForSubscription(WebEpisodeDescriptionActivity.this));

            callProgressBar();
            playerChecks(railData);

        });
    }


    private void checkErrors() {
        if (playerChecksCompleted) {
            if (assetRuleErrorCode == AppLevelConstants.GEO_LOCATION_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeforGeoLocation(1, WebEpisodeDescriptionActivity.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.FOR_PURCHASED_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(WebEpisodeDescriptionActivity.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.USER_ACTIVE_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(WebEpisodeDescriptionActivity.this));
                callProgressBar();
            }
//            else if (assetRuleErrorCode == AppLevelConstants.PARENTAL_BLOCK) {
//                isParentalLocked = true;
//                if (KsPreferenceKey.getInstance(this).getUserActive())
//                    validateParentalPin();
//                else
//                    startPlayer();
//            }
//            else if (errorCode == AppLevelConstants.NO_ERROR && (assetRuleErrorCode == AppLevelConstants.NO_ERROR || assetRuleErrorCode == -1)) {
//                if (KsPreferenceKey.getInstance(this).getUserActive())
//                    checkOnlyDevice(railData);
//                else {
//                    startPlayer();
//                }
//            }
            else if (errorCode == AppLevelConstants.NO_ERROR) {
                if (KsPreferenceKey.getInstance(this).getUserActive()) {
                    parentalCheck(railData);
                } else {
                    startPlayer();
                }
            }
        } else {
            callProgressBar();
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

    private void startPlayer() {
        callProgressBar();
        Intent intent = new Intent(WebEpisodeDescriptionActivity.this, PlayerActivity.class);
        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        startActivity(intent);
    }

    private void validateParentalPin(RailCommonData railData) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.showValidatePinDialog(WebEpisodeDescriptionActivity.this, null, "WEBEPISODE", new ParentalDialogCallbacks() {
                    @Override
                    public void onPositiveClick(String pinText) {
                        ParentalControlViewModel parentalViewModel = ViewModelProviders.of(WebEpisodeDescriptionActivity.this).get(ParentalControlViewModel.class);

                        parentalViewModel.validatePin(WebEpisodeDescriptionActivity.this, pinText).observe(WebEpisodeDescriptionActivity.this, commonResponse -> {
                            if (commonResponse.getStatus()) {
                                DialogHelper.hideValidatePinDialog();
                                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                                playerChecksCompleted = true;
                                //  checkErrors();
                                checkOnlyDevice(railData);
                            } else {
                                Toast.makeText(WebEpisodeDescriptionActivity.this, getString(R.string.incorrect_parental_pin), Toast.LENGTH_LONG).show();
                                assetRuleErrorCode = AppLevelConstants.PARENTAL_BLOCK;
                            }
                        });
                    }

                    @Override
                    public void onNegativeClick() {
                        DialogHelper.hideValidatePinDialog();
                        callProgressBar();
                    }
                });
            }
        });
    }

    private void checkOnlyDevice(RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(WebEpisodeDescriptionActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> {
                        startPlayer();
                    });

                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(WebEpisodeDescriptionActivity.this).refreshKS(response -> checkDevice(railData));
                    } else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }
                }


            }

        });
    }

    private void playerChecks(final RailCommonData railData) {
        new GeoBlockingCheck().aseetAvailableOrNot(WebEpisodeDescriptionActivity.this, railData.getObject(), (status, response, totalCount, errorcode, message) -> {
            if (status) {
                if (totalCount != 0) {
                    checkBlockingErrors(response);
                } else {
                    checkEntitleMent(railData);
                }
            } else {
                callProgressBar();
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
//                    case PARENTAL:
//                        assetRuleErrorCode = AppLevelConstants.PARENTAL_BLOCK;
//                        checkEntitleMent(railData);
//                        break;
                    default:
                        checkEntitleMent(railData);
                        break;
                }
            }
        }
    }

    private void checkEntitleMent(final RailCommonData railCommonData) {
        String fileId = AppCommonMethods.getFileIdOfAssest(railData.getObject());

        new EntitlementCheck().checkAssetType(WebEpisodeDescriptionActivity.this, fileId, (status, response, purchaseKey, errorCode1, message) -> {
            if (status) {
                playerChecksCompleted = true;
                if (purchaseKey.equalsIgnoreCase(getResources().getString(R.string.FOR_PURCHASE_SUBSCRIPTION_ONLY)) || purchaseKey.equals(getResources().getString(R.string.FREE))) {
                    errorCode = AppLevelConstants.NO_ERROR;
                    railData = railCommonData;
                    checkErrors();
                } else if (purchaseKey.equalsIgnoreCase(getResources().getString(R.string.FOR_PURCHASED))) {

                    if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                        isDtvAccountAdded(railCommonData);
                        //check Dtv Account Added or Not

                    } else {
                        errorCode = AppLevelConstants.FOR_PURCHASED_ERROR;
                        checkErrors();
                    }

                } else {
                    if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                        isDtvAccountAdded(railCommonData);
                        //check Dtv Account Added or Not
                    } else {
                        errorCode = AppLevelConstants.USER_ACTIVE_ERROR;
                        checkErrors();
                        //not play
                    }
                }
            } else {
                callProgressBar();
                if (message != "")
                    showDialog(message);
            }
        });

    }

    private void isDtvAccountAdded(RailCommonData railCommonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewModel.getDtvAccountList().observe(WebEpisodeDescriptionActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String dtvAccount) {
                        try {
                            if (dtvAccount != null) {
                                if (dtvAccount.equalsIgnoreCase("0")) {
                                    isDtvAdded = false;
                                    callProgressBar();
                                    checkForSubscription(isDtvAdded, railCommonData);
                                } else if (dtvAccount.equalsIgnoreCase("")) {
                                    isDtvAdded = false;
                                    callProgressBar();
                                    checkForSubscription(isDtvAdded, railCommonData);
                                } else {
                                    isDtvAdded = true;
                                    callProgressBar();
                                    checkForSubscription(isDtvAdded, railCommonData);
                                }

                            } else {
                                // Api Failure Error
                                callProgressBar();
                                showDialog(getString(R.string.something_went_wrong_try_again));
                            }
                        } catch (Exception e) {
                            Log.e("ExceptionIs", e.toString());
                        }
                    }
                });

            }
        });

    }

    private void checkForSubscription(boolean isDtvAdded, RailCommonData railCommonData) {
        //***** Mobile + Non-Dialog + Non-DTV *************//
        if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.NON_DIALOG) && isDtvAdded == false) {
            runOnUiThread(() -> DialogHelper.openDialougeFornonDialog(WebEpisodeDescriptionActivity.this, false));
        }
        //********** Mobile + Non-Dialog + DTV ******************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.NON_DIALOG) && isDtvAdded == true) {
            runOnUiThread(() -> DialogHelper.openDialougeFornonDialog(WebEpisodeDescriptionActivity.this, false));
        }
        //*********** Mobile + Dialog + Non-DTV *****************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded == false) {
            if (AssetContent.isPurchaseAllowed(railCommonData.getObject().getMetas(), railCommonData.getObject(), WebEpisodeDescriptionActivity.this)) {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(WebEpisodeDescriptionActivity.this, true, false));
            } else {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(WebEpisodeDescriptionActivity.this, false, false));
            }
        }
        //************ Mobile + Dialog + DTV ********************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded == true) {
            if (AssetContent.isPurchaseAllowed(railCommonData.getObject().getMetas(), railCommonData.getObject(), WebEpisodeDescriptionActivity.this)) {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(WebEpisodeDescriptionActivity.this, true, false));
            } else {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(WebEpisodeDescriptionActivity.this, false, false));
            }
        } else {
            showDialog(getString(R.string.something_went_wrong_try_again));
        }
    }


    private void checkDevice(final RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(WebEpisodeDescriptionActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> checkEntitleMent(railData));

                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(WebEpisodeDescriptionActivity.this).refreshKS(response -> checkDevice(railData));
                    } else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }
                }


            }

        });

    }

    private void setMetas() {


        getEpisodeNumber();
        getDuration();
        getMovieRating();


//                if (StringBuilderHolder.getInstance().getText().length() > 0) {
//                    StringBuilderHolder.getInstance().subString(0, StringBuilderHolder.getInstance().getText().length() - 2);
//                }
//                getBinding().tvShortDescription.setText(StringBuilderHolder.getInstance().getText());
        String value = StringBuilderHolder.getInstance().getText().toString();
        if (value.length() > 0) {
            value = StringBuilderHolder.getInstance().getText().substring(0, value.length() - 2);
        }
        getBinding().tvShortDescription.setText(value);

        setBannerImage(asset);
    }

    private void setBannerImage(Asset asset) {

        StringBuilderHolder.getInstance().clear();
        for (int i = 0; i < asset.getImages().size(); i++) {

            if (asset.getImages().get(i).getRatio().equals("16x9")) {
                StringBuilderHolder.getInstance().append(asset.getImages().get(i).getUrl());
                StringBuilderHolder.getInstance().append(AppLevelConstants.WIDTH);
                StringBuilderHolder.getInstance().append("" + (int) getResources().getDimension(R.dimen.carousel_image_width));
                StringBuilderHolder.getInstance().append(AppLevelConstants.HEIGHT);
                StringBuilderHolder.getInstance().append("" + (int) getResources().getDimension(R.dimen.carousel_image_height));
                StringBuilderHolder.getInstance().append(AppLevelConstants.QUALITY);
            }
        }

        getBinding().backImg.setOnClickListener(view -> onBackPressed());
    }

    private void getEpisodeNumber() {
        DoubleValue episodeNumber = (DoubleValue) asset.getMetas().get(AppLevelConstants.KEY_EPISODE_NUMBER);

//        StringBuilderHolder.getInstance().clear();
        StringBuilderHolder.getInstance().append(getResources().getString(R.string.episode_no) + " ");
        if (episodeNumber != null && episodeNumber.getValue() != null) {
            StringBuilderHolder.getInstance().append("" + episodeNumber.getValue().intValue());
        }
        StringBuilderHolder.getInstance().append(" | ");

//        getBinding().episodeNumber.setText(StringBuilderHolder.getInstance().getText());
//        getBinding().episodeNumber.setText(+episodeNumber.getValue().intValue());

    }

    private void getMovieRating() {
        if (AssetContent.getParentalRating(map).length() > 0) {
//            StringBuilderHolder.getInstance().clear();
            StringBuilderHolder.getInstance().append(AssetContent.getParentalRating(map));
            StringBuilderHolder.getInstance().append(" | ");

//            getBinding().parentalRating.setText(StringBuilderHolder.getInstance().getText());
            //        getBinding().parentalRating.setText("| " + AssetContent.getParentalRating(map));
        }
    }

    private void getMovieCrews() {
        viewModel.getCrewLiveDAta(map).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String crewText) {

                PrintLogging.printLog(this.getClass(), "", "crewValusWebEpiIs" + crewText);

                if (TextUtils.isEmpty(crewText)) {
                    getBinding().crewLay.setVisibility(View.GONE);
                } else {
                    getBinding().crewLay.setVisibility(View.VISIBLE);
                    getBinding().setCrewValue(" " + crewText.trim());
                }


            }
        });
    }

    private void getMovieCasts() {
        viewModel.getCastLiveData(map).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String castTest) {

                PrintLogging.printLog(this.getClass(), "", "castValusWebEpiIs" + castTest);

                if (TextUtils.isEmpty(castTest)) {
                    getBinding().castLay.setVisibility(View.GONE);
                } else {
                    getBinding().castLay.setVisibility(View.VISIBLE);
                    getBinding().setCastValue(" " + castTest.trim());
                }


            }
        });
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(WebEpisodeDescriptionViewModel.class);

    }

    private void callwatchlistApi() {

        if (personalLists != null)
            personalLists.clear();
        playlist = new ArrayList<>();
        getIdTocompareWithWatchlist();
        if (isAdded) {
            if (iconClicked) {
                viewModel.deleteWatchlist(idfromAssetWatchlist).observe(WebEpisodeDescriptionActivity.this, new Observer<CommonResponse>() {
                    @Override
                    public void onChanged(@Nullable CommonResponse aBoolean) {
                        if (aBoolean.getStatus() == true) {
                            // new ToastHandler(WebSeriesDescriptionActivity.this).show(getApplicationContext().getResources().getString(R.string.removed_from_watchlist));
                            ToastHandler.show(getApplicationContext().getResources().getString(R.string.episode) + " " + getApplicationContext().getResources().getString(R.string.removed_from_watchlist), getApplicationContext());
                            isAdded = false;
                            iconClicked = false;
                            getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play_list), null, null);
                            getBinding().watchList.setTextColor(getResources().getColor(R.color.white));


                        } else {
                            if (aBoolean.getErrorCode().equals("")) {
                                //  showDialog(getString(R.string.something_went_wrong_try_again));
                            } else {
                                if (aBoolean.getErrorCode().equals(AppLevelConstants.ALREADY_UNFOLLOW_ERROR)) {
                                    ToastHandler.show(getApplicationContext().getResources().getString(R.string.episode) + " " + getApplicationContext().getResources().getString(R.string.already_remove_watchlist), getApplicationContext());
                                    //  ToastHandler.show(getApplicationContext().getResources().getString(R.string.removed_from_watchlist), getApplicationContext());
                                    isAdded = false;
                                    getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play_list), null, null);
                                    getBinding().watchList.setTextColor(getResources().getColor(R.color.white));
                                } else {
                                    showDialog(aBoolean.getMessage());
                                }

                            }
                        }

                    }
                });
            }
        } else {
            viewModel.listAllwatchList(id).observe(this, commonResponse -> {

                if (commonResponse.getStatus()) {
                    idfromAssetWatchlist = commonResponse.getAssetID();
                    isAdded = true;
                    getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.playlist_added_check_icon), null, null);
                    getBinding().watchList.setTextColor(getResources().getColor(R.color.primary_blue));
                } else {
                    if (iconClicked) {
                        personalLists = commonResponse.getPersonalLists();
                        if (personalLists != null) {
                            list = new ArrayList<>();

                            for (int i = 0; i < personalLists.size(); i++) {
                                if (playlist.isEmpty()) {
                                    playlist.add(personalLists.get(i));
                                    list.add(playlist.get(i).getPartnerListType());
                                } else {
                                    for (int k = 0; k < playlist.size(); k++) {
                                        list.add(playlist.get(k).getPartnerListType());
                                    }
                                    if (list.contains(personalLists.get(i).getPartnerListType())) {

                                    } else {
                                        playlist.add(personalLists.get(i));
                                    }
                                }
                            }

                            playlistId = Collections.max(list) + 1;


                        }


                        CommonPlaylistDialog.getInstance().createBottomSheet(WebEpisodeDescriptionActivity.this, playlist, this);
                        iconClicked = false;
                    }
                    isAdded = false;
                    getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play_list), null, null);
                    getBinding().watchList.setTextColor(getResources().getColor(R.color.white));
                }
            });

        }
    }

    private void checkConditionForWatchlist(CommonResponse commonResponse) {
        if (!commonResponse.getStatus()) {
            if (iconClicked) {
                // addToWatchlist();
                iconClicked = false;
            }
            isAdded = false;
            getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play_list), null, null);
            getBinding().watchList.setTextColor(getResources().getColor(R.color.white));

        } else {

            idfromAssetWatchlist = commonResponse.getAssetID();
            isAdded = true;
            getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.playlist_added_check_icon), null, null);
            getBinding().watchList.setTextColor(getResources().getColor(R.color.primary_blue));

        }
    }

    private void getIdTocompareWithWatchlist() {
        String one = "media_id='";
        String two = idofasset;
        String three = "'";
        id = one.concat(two).concat(three);
    }

    private void addToWatchlist(String title) {

        //  getTitletoAddInWatchlist();
        getIdTocompareWithWatchlist();
        viewModel.addToWatchlist(id, title, playlistId).observe(this, s -> {
            if (s != null) {
                checkAddedCondition(s);
            }
        });

    }

    private void checkAddedCondition(CommonResponse s) {
        if (s.getStatus()) {
            showAlertDialog(getApplicationContext().getResources().getString(R.string.episode_text) + " " + getApplicationContext().getResources().getString(R.string.added_to_watchlist));
            idfromAssetWatchlist = s.getAssetID();
            isAdded = true;
            getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.playlist_added_check_icon), null, null);
            getBinding().watchList.setTextColor(getResources().getColor(R.color.primary_blue));

        } else {
            switch (s.getErrorCode()) {
                case "":

                    showDialog(s.getMessage());
                    break;
                case AppLevelConstants.ALREADY_FOLLOW_ERROR:
                    showAlertDialog(getApplicationContext().getResources().getString(R.string.episode) + " " + getApplicationContext().getResources().getString(R.string.already_added_in_watchlist));
                    callwatchlistApi();
                    break;
                default:
                    showDialog(s.getMessage());
//                showDialog(s.getMessage(), 1, getResources().getString(R.string.ok), "", 1);
                    break;
            }

        }
    }

    private void getTitletoAddInWatchlist() {
        String one = "name='";
        String two = titleName;
        String three = "'";
        concatedTitleName = one.concat(two).concat(three);
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    private void setUIComponets(List<AssetCommonBean> assetCommonBeans, int counter, int type) {
        try {

            if (adapter != null) {
                if (type > 0) {
                    int temp = counter + tempCount;
                    loadedList.add(assetCommonBeans.get(0));
                    adapter.notifyItemChanged(temp);
                } else {
                    loadedList.add(assetCommonBeans.get(0));
                    adapter.notifyItemChanged(counter);
                }
            } else {
                loadedList.add(assetCommonBeans.get(0));
                adapter = new WebEpisodeDescriptionCommonAdapter(this, loadedList);
                getBinding().myRecyclerView.setAdapter(adapter);
            }
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }


    }

    private void loadDataFromModel() {

        viewModel.getChannelList(AppLevelConstants.TAB_DRAMA_EPISODES_DETAILS).observe(this, assetCommonBean -> {

            if (assetCommonBean != null && assetCommonBean.getStatus()) {
                dtChannelsList = assetCommonBean.getDTChannelList();
            }

        });


        clipList = new ArrayList<>();
        if (Constants.assetType == MediaTypeConstant.getClip()) {
            viewModel.getClipData(Constants.assetId, Constants.counter, Constants.assetType, map, AppLevelConstants.Rail3, seriesMediaType).observe(this, assetCommonBeans -> {
                if (assetCommonBeans != null)
                    clipList = assetCommonBeans;
            });
        }
        viewModel.getSeasonsListData(Constants.assetId, Constants.counter, Constants.assetType, map, layoutType, seriesMediaType).observe(this, integers -> {
            if (integers != null && integers.size() > 0) {
                seriesNumberList = integers;
                callSeasonEpisodes(seriesNumberList);
            } else {
                callCategoryRailAPI(dtChannelsList);
            }
        });
    }

    private void callSeasonEpisodes(List<Integer> seriesNumberList) {
        if (seasonCounter != seriesNumberList.size()) {
            viewModel.callSeasonEpisodes(map, Constants.assetType, 1, seriesNumberList, seasonCounter, AppLevelConstants.Rail3).observe(this, assetCommonBeans -> {
                if (assetCommonBeans != null && assetCommonBeans.get(0).getStatus()) {
                    getBinding().myRecyclerView.setVisibility(View.VISIBLE);
                    setUIComponets(assetCommonBeans, tempCount, 0);
                    tempCount++;
                    seasonCounter++;
                    callSeasonEpisodes(seriesNumberList);
                } else {
                    callCategoryRailAPI(dtChannelsList);
                }
            });
        } else {
            // tempCount--;
            callCategoryRailAPI(dtChannelsList);
        }

    }

    private void callCategoryRailAPI(List<VIUChannel> list) {
        if (dtChannelsList != null) {
            if (dtChannelsList.size() > 0) {
                channelList = list;
                if (counter != channelList.size() && counter < channelList.size()) {
                    loopend = 1;
                    viewModel.getListLiveData(channelList.get(counter).getId(), dtChannelsList, counter, 1).observe(this, assetCommonBeans -> {
                        if (assetCommonBeans != null && assetCommonBeans.size() > 0) {
                            boolean status = assetCommonBeans.get(0).getStatus();
                            if (status) {
                                setUIComponets(assetCommonBeans, counter, 1);
                                counter++;
                                callCategoryRailAPI(channelList);
                            } else {
                                if (counter != channelList.size()) {
                                    counter++;
                                    callCategoryRailAPI(channelList);
                                }
                            }

                        }

                    });
                } else {
                    loopend = 0;
                }
            }
        }
    }

    private void setExpandable() {
        getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
        getBinding().setExpandabletext(getResources().getString(R.string.more));
        getBinding().expandableLayout.setOnExpansionUpdateListener(expansionFraction -> getBinding().lessButton.setRotation(0 * expansionFraction));
        getBinding().lessButton.setOnClickListener(view -> {
            getBinding().descriptionText.toggle();
            getBinding().descriptionText.setEllipsis("...");
            if (getBinding().descriptionText.isExpanded()) {
                getBinding().descriptionText.setEllipsize(null);
            } else {
                getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
            }

            if (getBinding().expandableLayout.isExpanded()) {
                getBinding().setExpandabletext(getResources().getString(R.string.more));

            } else {
                getBinding().setExpandabletext(getResources().getString(R.string.less));
            }
            if (view != null) {
                getBinding().expandableLayout.expand();
            }
            getBinding().expandableLayout.collapse();
        });

    }

    private void openShareDialouge() {
        AppCommonMethods.openShareDialog(this, asset, getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PlayerRepository.getInstance().releasePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (KsPreferenceKey.getInstance(this).getUserActive()) {
            if (NetworkConnectivity.isOnline(this)) {
                this.idofasset = String.valueOf(assetId);
                this.titleName = name;
                isActive = true;
                callwatchlistApi();
                if (isParentalLocked)
                    assetRuleErrorCode = AppLevelConstants.PARENTAL_BLOCK;
            }
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();

    }

    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {
        if (loopend == 0) {
            resetCounters();
            getBinding().myRecyclerView.setVisibility(View.GONE);
            if (getBinding().descriptionText.isExpanded()) {
                getBinding().descriptionText.toggle();
            }
            assetRuleErrorCode = AppLevelConstants.NO_ERROR;

            setMetaData(commonData, layoutType);
            this.idofasset = String.valueOf(assetId);
            this.titleName = name;
            isAdded = false;
            iconClicked = false;
            isActive = true;
            callwatchlistApi();
        }

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

    private void resetCounters() {
        counter = 0;
        seasonCounter = 0;
        tempCount = 0;
        if (seriesNumberList != null)
            seriesNumberList.clear();

        if (clipList != null)
            clipList.clear();

        if (channelList != null)
            channelList.clear();
        adapter = null;
        loadedList.clear();
    }

    @Override
    public void onClick(String name, int id) {
        if (NetworkConnectivity.isOnline(getApplication())) {
            if (isActive) {
                playlistId = id;
                addToWatchlist(name);
            }
        } else {
            ToastHandler.show(getResources().getString(R.string.no_internet_connection), WebEpisodeDescriptionActivity.this);

        }
    }

    @Override
    public void onFinishEditDialog(String text) {
        if (NetworkConnectivity.isOnline(getApplication())) {
            addToWatchlist(text);
        } else {
            ToastHandler.show(getResources().getString(R.string.no_internet_connection), WebEpisodeDescriptionActivity.this);

        }
    }

    private void callProgressBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getBinding().includeProgressbar.progressBar.getVisibility() == View.VISIBLE) {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                } else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
                }

            }
        });
    }
}