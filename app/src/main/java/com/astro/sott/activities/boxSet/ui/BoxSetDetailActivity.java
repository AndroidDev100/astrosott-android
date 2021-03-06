package com.astro.sott.activities.boxSet.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.astro.sott.R;
import com.astro.sott.activities.SelectAccount.UI.SelectDtvAccountActivity;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.liveEvent.LiveEventActivity;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.movieDescription.viewModel.MovieDescriptionViewModel;
import com.astro.sott.activities.parentalControl.viewmodels.ParentalControlViewModel;
import com.astro.sott.activities.signUp.ui.SignUpActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.ParentalDialogCallbacks;
import com.astro.sott.databinding.BoxSetDetailBinding;
import com.astro.sott.fragments.detailRailFragment.ui.BoxSetDetailFragment;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.modelClasses.dmsResponse.ParentalLevels;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.networking.refreshToken.RefreshKS;
import com.astro.sott.player.entitlementCheckManager.EntitlementCheck;
import com.astro.sott.player.geoBlockingManager.GeoBlockingCheck;
import com.astro.sott.player.houseHoldCheckManager.HouseHoldCheck;
import com.astro.sott.player.ui.PlayerActivity;
import com.astro.sott.repositories.player.PlayerRepository;
import com.astro.sott.thirdParty.CleverTapManager.CleverTapManager;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.DialogHelper;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.StringBuilderHolder;
import com.astro.sott.utils.helpers.SubMediaTypes;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;
import com.google.gson.Gson;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.StringValue;
import com.kaltura.client.types.UserAssetRule;
import com.kaltura.client.types.Value;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.Map;


public class BoxSetDetailActivity extends BaseBindingActivity<BoxSetDetailBinding> implements DetailRailClick, AlertDialogSingleButtonFragment.AlertDialogListener {
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
    private int assetType, watchPosition = 0;
    private String trailor_url = "";
    private String name, titleName, idfromAssetWatchlist;
    private boolean isActive, isAdded;
    private long lastClickTime = 0;
    private int errorCode = -1;
    private boolean playerChecksCompleted = false;
    private int assetRuleErrorCode = -1;
    private boolean isParentalLocked = false;
    private String defaultParentalRating = "";
    private String userSelectedParentalRating = "";
    private boolean assetKey = false;
    private boolean isDtvAdded = false;


    @Override
    public BoxSetDetailBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return BoxSetDetailBinding.inflate(inflater);
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
        railData = commonRailData;
        asset = railData.getObject();
        if (asset != null)
            getBookmarking(asset);
        FirebaseEventManager.getFirebaseInstance(this).trackScreenName(asset.getName());
        FirebaseEventManager.getFirebaseInstance(BoxSetDetailActivity.this).setRelatedAssetName(asset.getName());

        layoutType = layout;
        assetId = asset.getId();
        name = asset.getName();
        titleName = name;
        isActive = UserInfo.getInstance(this).isActive();
        map = asset.getTags();
        if (isActive)
            isWatchlistedOrNot();
        setPlayerFragment();
        getMediaType(asset, railData);
        callSpecificAsset(assetId);
        if (playbackControlValue)
            checkEntitleMent(railData);


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
        getBinding().astroPlayButton.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            if (vodType.equalsIgnoreCase(EntitlementCheck.FREE)) {
                callProgressBar();
                playerChecks(railData);
            }


        });

    }

    private void checkErrors() {
        if (playerChecksCompleted) {
            if (assetRuleErrorCode == AppLevelConstants.GEO_LOCATION_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeforGeoLocation(1, BoxSetDetailActivity.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.USER_ACTIVE_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(BoxSetDetailActivity.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.NO_ERROR) {
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

    private void validateParentalPin(RailCommonData railData) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.showValidatePinDialog(BoxSetDetailActivity.this, null, "MOVIE", new ParentalDialogCallbacks() {
                    @Override
                    public void onPositiveClick(String pinText) {
                        ParentalControlViewModel parentalViewModel = ViewModelProviders.of(BoxSetDetailActivity.this).get(ParentalControlViewModel.class);

                        parentalViewModel.validatePin(BoxSetDetailActivity.this, pinText).observe(BoxSetDetailActivity.this, commonResponse -> {
                            if (commonResponse.getStatus()) {
                                DialogHelper.hideValidatePinDialog();
                                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                                playerChecksCompleted = true;
                                //checkErrors();
                                checkOnlyDevice(railData);
                            } else {
                                ToastHandler.show(getString(R.string.something_went_wrong), BoxSetDetailActivity.this);
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
        new HouseHoldCheck().checkHouseholdDevice(BoxSetDetailActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> {
                        startPlayer();
                    });
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(BoxSetDetailActivity.this).refreshKS(response -> checkDevice(railData));
                    } else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }
                }
            }

        });
    }


    private void startPlayer() {
        try {
            callProgressBar();
            //  ConvivaManager.getConvivaAdAnalytics(this);
            Intent intent = new Intent(BoxSetDetailActivity.this, PlayerActivity.class);
            intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
            startActivity(intent);

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }

    private void showWifiDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.wifi_dialog, null);
        TextView btn_update = (TextView) view.findViewById(R.id.btn_update);
        Switch switch_download = (Switch) view.findViewById(R.id.switch_download);
        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
        if (!new KsPreferenceKey(this).getDownloadOverWifi()) {
            switch_download.setChecked(false);
        } else {
            switch_download.setChecked(true);
        }
        switch_download.setOnCheckedChangeListener((compoundButton, b) -> {
            if (switch_download.isChecked()) {
                new KsPreferenceKey(this).setDownloadOverWifi(true);
            } else {
                new KsPreferenceKey(this).setDownloadOverWifi(false);
            }
        });
        btn_update.setOnClickListener(view1 -> {
            alert.dismiss();
            if (!new KsPreferenceKey(this).getDownloadOverWifi()) {
                callProgressBar();
                playerChecks(railData);

            }
        });
    }

    private void playerChecks(final RailCommonData railData) {
        new GeoBlockingCheck().aseetAvailableOrNot(BoxSetDetailActivity.this, railData.getObject(), (status, response, totalCount, errorcode, message) -> {
            if (status) {
                if (totalCount != 0) {
                    checkBlockingErrors(response);
                } else {
                    playerChecksCompleted = true;
                    checkErrors();
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

                    default:
                        playerChecksCompleted = true;
                        checkErrors();
                        break;
                }
            }
        }
    }

    private void getBookmarking(Asset asset) {
        if (UserInfo.getInstance(this).isActive()) {
            viewModel.getBookmarking(asset).observe(this, integer -> {
                watchPosition = integer;
            });
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void checkEntitleMent(final RailCommonData railCommonData) {

        String fileId = "";

        fileId = AppCommonMethods.getFileIdOfAssest(railData.getObject());

        new EntitlementCheck().checkAssetPurchaseStatus(BoxSetDetailActivity.this, fileId, (apiStatus, purchasedStatus, vodType, purchaseKey, errorCode, message) -> {
            this.errorCode = AppLevelConstants.NO_ERROR;
            if (apiStatus) {
                if (purchasedStatus) {
                    runOnUiThread(() -> {
                        getBinding().astroPlayButton.setBackground(getResources().getDrawable(R.drawable.gradient_free));
                        if (watchPosition > 0) {
                            getBinding().playText.setText(getResources().getString(R.string.resume));
                        } else {
                            getBinding().playText.setText(getResources().getString(R.string.watch_now));
                        }
                        getBinding().astroPlayButton.setVisibility(View.GONE);
                        getBinding().starIcon.setVisibility(View.GONE);
                        getBinding().playText.setTextColor(getResources().getColor(R.color.black));


                    });
                    this.vodType = EntitlementCheck.FREE;

                } else {
                    if (vodType.equalsIgnoreCase(EntitlementCheck.SVOD)) {
                        if (xofferWindowValue) {
                            runOnUiThread(() -> {
                                getBinding().astroPlayButton.setBackground(getResources().getDrawable(R.drawable.gradient_svod));
                                getBinding().playText.setText(getResources().getString(R.string.become_vip));
                                getBinding().playText.setTextColor(getResources().getColor(R.color.white));
                                getBinding().astroPlayButton.setVisibility(View.GONE);
                                getBinding().starIcon.setVisibility(View.VISIBLE);

                            });
                        }
                        this.vodType = EntitlementCheck.SVOD;

                    } else if (vodType.equalsIgnoreCase(EntitlementCheck.TVOD)) {
                        if (xofferWindowValue) {
                            runOnUiThread(() -> {
                                getBinding().astroPlayButton.setBackground(getResources().getDrawable(R.drawable.gradient_svod));
                                getBinding().playText.setText(getResources().getString(R.string.rent_movie));
                                getBinding().astroPlayButton.setVisibility(View.GONE);
                                getBinding().playText.setTextColor(getResources().getColor(R.color.white));

                            });
                        }

                        this.vodType = EntitlementCheck.TVOD;


                    }
                }

            } else {

            }
        });

       /* new EntitlementCheck().checkAssetType(MovieDescriptionActivity.this, fileId, (status, response, purchaseKey, errorCode1, message) -> {
            if (status) {
                playerChecksCompleted = true;
                if (purchaseKey.equalsIgnoreCase(getResources().getString(R.string.FOR_PURCHASE_SUBSCRIPTION_ONLY)) || purchaseKey.equals(getResources().getString(R.string.FREE))) {
                    errorCode = AppLevelConstants.NO_ERROR;
                    railData = railCommonData;
                } else if (purchaseKey.equalsIgnoreCase(getResources().getString(R.string.FOR_PURCHASED))) {
                    if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                        isDtvAccountAdded(railCommonData);
                    } else {
                        errorCode = AppLevelConstants.FOR_PURCHASED_ERROR;
                    }
                } else {
                    if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                        isDtvAccountAdded(railCommonData);
                    } else {
                        errorCode = AppLevelConstants.USER_ACTIVE_ERROR;
                    }
                }
            } else {
                callProgressBar();
                if (message != "")
                    showDialog(message);
            }
        });
*/

    }


    private void isDtvAccountAdded(RailCommonData railCommonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewModel.getDtvAccountList().observe(BoxSetDetailActivity.this, new Observer<String>() {
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
            runOnUiThread(() -> DialogHelper.openDialougeFornonDialog(BoxSetDetailActivity.this, false));
        }
        //********** Mobile + Non-Dialog + DTV ******************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.NON_DIALOG) && isDtvAdded == true) {
            runOnUiThread(() -> DialogHelper.openDialougeFornonDialog(BoxSetDetailActivity.this, false));
        }
        //*********** Mobile + Dialog + Non-DTV *****************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded == false) {
            if (AssetContent.isPurchaseAllowed(railCommonData.getObject().getMetas(), railCommonData.getObject(), BoxSetDetailActivity.this)) {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(BoxSetDetailActivity.this, true, false));
            } else {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(BoxSetDetailActivity.this, false, false));
            }
        }
        //************ Mobile + Dialog + DTV ********************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded == true) {
            if (AssetContent.isPurchaseAllowed(railCommonData.getObject().getMetas(), railCommonData.getObject(), BoxSetDetailActivity.this)) {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(BoxSetDetailActivity.this, true, false));
            } else {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(BoxSetDetailActivity.this, false, false));
            }
        } else {
            showDialog(getString(R.string.something_went_wrong_try_again));
        }
    }

    private void checkDevice(final RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(BoxSetDetailActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> checkEntitleMent(railData));
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(BoxSetDetailActivity.this).refreshKS(response -> checkDevice(railData));
                    } else {
                        callProgressBar();
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


    private void getMovieCrews() {
        viewModel.getCrewLiveDAta(map).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String crewText) {

                PrintLogging.printLog(this.getClass(), "", "crewValusIs" + crewText);

                if (TextUtils.isEmpty(crewText)) {
                    getBinding().crewLay.setVisibility(View.GONE);
                } else {
                    getBinding().crewLay.setVisibility(View.VISIBLE);
                    getBinding().crewText.setText(" " + crewText);
                }

            }
        });
    }

    private void getMovieCasts() {
        viewModel.getCastLiveData(map).observe(this, castTest -> {
            if (TextUtils.isEmpty(castTest)) {
                getBinding().castLay.setVisibility(View.GONE);
            } else {
                getBinding().castLay.setVisibility(View.VISIBLE);
                getBinding().castText.setText(" " + castTest);
            }

        });
    }

    private void setMetas() {
        getMovieYear();

    }


    private void setMovieMetaData(Asset asset, int type) {
        setMetaDataValues(asset, type);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        setExpandable();
        getBinding().shareWith.setOnClickListener(view -> {

            if (SystemClock.elapsedRealtime() - lastClickTime < AppLevelConstants.SHARE_DIALOG_DELAY) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            openShareDialouge();
        });
        setWatchlist();
        // setRailFragment();
        setRailBaseFragment();
    }


    private void setRailBaseFragment() {
        FragmentManager fm = getSupportFragmentManager();
        BoxSetDetailFragment boxSetDetailFragment = new BoxSetDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        boxSetDetailFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.rail_fragment, boxSetDetailFragment).commitNow();
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

        getBinding().setMovieAssestModel(asset);
        getBinding().setMovieAssestModel(asset);
        setMetas();

        getMovieCasts();
        getMovieCrews();
        setSubtitleLanguage();
        getDuration();
        if (type == 1) {

            PrintLogging.printLog(this.getClass(), "type 1", "");
        } else {
            getRefId(0, asset);
        }

        assetId = asset.getId();
        assetType = asset.getType();


    }

    private void setSubtitleLanguage() {

        viewModel.getSubTitleLanguageLiveData(map).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String crewText) {

                PrintLogging.printLog(this.getClass(), "", "crewValusIs" + crewText);

                if (TextUtils.isEmpty(crewText)) {
                    getBinding().subtitleLay.setVisibility(View.GONE);
                } else {
                    getBinding().subtitleLay.setVisibility(View.VISIBLE);
                    getBinding().subtitleText.setText(" " + crewText);
                }

            }
        });
    }


    private void getLanguage() {
        viewModel.getLanguageLiveData(map).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                if (!TextUtils.isEmpty(s)) {
                    StringBuilderHolder.getInstance().append(s);
                    StringBuilderHolder.getInstance().append(" | ");
                    PrintLogging.printLog(this.getClass(), "", "language " + StringBuilderHolder.getInstance().getText());

                }

                getMovieRating();
                String value = StringBuilderHolder.getInstance().getText().toString();
                if (value.length() > 0) {
                    value = StringBuilderHolder.getInstance().getText().substring(0, value.length() - 2);
                }
                getBinding().tvShortDescription.setText(value);
                getBinding().movieTitle.setText(asset.getName());
                MultilingualStringValue stringValue = null;
                String description = "";
                if (asset.getMetas() != null)
                    stringValue = (MultilingualStringValue) asset.getMetas().get(AppLevelConstants.KEY_LONG_DESCRIPTION);
                if (stringValue != null)
                    description = stringValue.getValue();
                getBinding().descriptionText.setText(description);


                setBannerImage(asset);
            }
        });
    }

    private String duraton;

    private void getDuration() {
        duraton = AppCommonMethods.getURLDuration(asset);

        if (!TextUtils.isEmpty(duraton)) {
            getBinding().durationLay.setVisibility(View.VISIBLE);
            getBinding().durationText.setText(" " + duraton);
        } else {
            getBinding().durationLay.setVisibility(View.GONE);

        }


    }

    private void getMovieYear() {

        if (yearMap != null) {
            doubleValue = (DoubleValue) yearMap.get(AppLevelConstants.YEAR);
        }
        if (doubleValue != null) {
            String s = String.valueOf(doubleValue.getValue());


            if (!TextUtils.isEmpty(s)) {

                StringBuilderHolder.getInstance().append(s.substring(0, 4));
                StringBuilderHolder.getInstance().append(" | ");

            }
        }
        getSubGenre();
        getXofferWindow();
        getPlayBackControl();

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


    private void getSubGenre() {

        viewModel.getSubGenreLivedata(map).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                getBinding().setTagText(s.trim());

                if (!TextUtils.isEmpty(s)) {
                    StringBuilderHolder.getInstance().append(s.trim());
                    StringBuilderHolder.getInstance().append(" | ");

                    PrintLogging.printLog(this.getClass(), "", "setMetas " + StringBuilderHolder.getInstance().getText());
                }

                getLanguage();


            }
        });
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

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }


    private void setExpandable() {
        getBinding().expandableLayout.collapse();
        getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
//        getBinding().textExpandable.setText(getResources().getString(R.string.view_more));
        getBinding().expandableLayout.setOnExpansionUpdateListener(expansionFraction -> getBinding().lessButton.setRotation(0 * expansionFraction));
        getBinding().lessButton.setOnClickListener(view -> {

            getBinding().descriptionText.toggle();

            if (getBinding().descriptionText.isExpanded()) {
                getBinding().descriptionText.setEllipsize(null);
                getBinding().shadow.setVisibility(View.GONE);
            } else {
                getBinding().shadow.setVisibility(View.VISIBLE);
                getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
            }

            if (getBinding().expandableLayout.isExpanded()) {
//                getBinding().textExpandable.setText(getResources().getString(R.string.view_more));
                getBinding().textExpandable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);


            } else {
                getBinding().textExpandable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);

//                getBinding().textExpandable.setText(getResources().getString(R.string.view_less));
            }
            if (view != null) {
                getBinding().expandableLayout.expand();
            }
            getBinding().expandableLayout.collapse();
        });

    }

    private void openShareDialouge() {
        try {
            CleverTapManager.getInstance().socialShare(this, asset, false);
            FirebaseEventManager.getFirebaseInstance(this).shareEvent(asset, this);
        } catch (Exception e) {

        }
        AppCommonMethods.openShareDialog(this, asset, getApplicationContext(), SubMediaTypes.BoxSet.name());
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
                isWatchlistedOrNot();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PlayerRepository.getInstance().releasePlayer();

    }

    private void setWatchlist() {
        getBinding().watchList.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            if (NetworkConnectivity.isOnline(getApplication())) {
                if (UserInfo.getInstance(this).isActive()) {
                    FirebaseEventManager.getFirebaseInstance(this).clickButtonEvent(FirebaseEventManager.ADD_MY_LIST, asset, this);

                    if (isAdded) {
                        deleteWatchlist();
                    } else {
                        addToWatchlist(titleName);
                    }
                } else {
                    new ActivityLauncher(BoxSetDetailActivity.this).signupActivity(BoxSetDetailActivity.this, SignUpActivity.class, CleverTapManager.DETAIL_PAGE_MY_LIST);
                }
            } else {
                ToastHandler.show(getResources().getString(R.string.no_internet_connection), BoxSetDetailActivity.this);

            }
        });

    }

    private void deleteWatchlist() {
        viewModel.deleteWatchlist(idfromAssetWatchlist).observe(BoxSetDetailActivity.this, aBoolean -> {
            if (aBoolean != null && aBoolean.getStatus()) {
                isAdded = false;
                ToastHandler.show(
                        getResources().getString(R.string.show_is) + "",
                        BoxSetDetailActivity.this);
                getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.favorite_unselected), null, null);
                getBinding().watchList.setTextColor(getResources().getColor(R.color.grey));
            } else {
                if (aBoolean != null && aBoolean.getErrorCode().equals("")) {
                    showDialog(aBoolean.getMessage());
                } else {
                    if (aBoolean != null && aBoolean.getErrorCode().equals(AppLevelConstants.ALREADY_UNFOLLOW_ERROR)) {
                        isAdded = false;
                        ToastHandler.show(getResources().getString(R.string.show_is) + "",
                                BoxSetDetailActivity.this);
                        getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.favorite_unselected), null, null);
                        getBinding().watchList.setTextColor(getResources().getColor(R.color.grey));
                    } else {
                        if (aBoolean != null)
                            showDialog(aBoolean.getMessage());
                    }

                }
            }

        });
    }

    private void addToWatchlist(String title) {

        if (UserInfo.getInstance(this).isActive()) {

            viewModel.addToWatchlist(assetId + "", title, playlistId).observe(this, s -> {
                if (s != null) {
                    checkAddedCondition(s);
                }
            });
        }
    }

    private void checkAddedCondition(CommonResponse s) {
        if (s.getStatus()) {
            ToastHandler.show(getResources().getString(R.string.show_is) + " " + getResources().getString(R.string.added_to_watchlist),
                    BoxSetDetailActivity.this);
            idfromAssetWatchlist = s.getAssetID();
            isAdded = true;
            getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.favorite_24_px), null, null);
            getBinding().watchList.setTextColor(getResources().getColor(R.color.aqua_marine));

        } else {
            switch (s.getErrorCode()) {
                case "":
                    showDialog(s.getMessage());
                    break;
                case AppLevelConstants.ALREADY_FOLLOW_ERROR:
                    ToastHandler.show(getResources().getString(R.string.show_is) + " " + getResources().getString(R.string.already_added_in_watchlist) + "",
                            BoxSetDetailActivity.this);
                    break;
                default:
                    showDialog(s.getMessage());
                    break;
            }

        }
    }


    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

        if (getBinding().descriptionText.isExpanded()) {
            getBinding().descriptionText.toggle();
        }
        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
        getDataFromBack(commonData, layoutType);

        titleName = name;
        isAdded = false;
        isActive = true;
    }

    private void isWatchlistedOrNot() {
        viewModel.listAllwatchList(assetId + "").observe(this, commonResponse -> {
            if (commonResponse.getStatus()) {
                if (commonResponse != null) {
                    idfromAssetWatchlist = commonResponse.getAssetID();
                    isAdded = true;
                    getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.favorite_24_px), null, null);
                    getBinding().watchList.setTextColor(getResources().getColor(R.color.aqua_marine));
                } else {
                    isAdded = false;
                    getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.favorite_unselected), null, null);
                    getBinding().watchList.setTextColor(getResources().getColor(R.color.title_color));

                }
            } else {
                isAdded = false;
                getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.favorite_unselected), null, null);
                getBinding().watchList.setTextColor(getResources().getColor(R.color.title_color));
            }
        });
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

