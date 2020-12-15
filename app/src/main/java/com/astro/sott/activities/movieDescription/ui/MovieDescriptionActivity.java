package com.astro.sott.activities.movieDescription.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.astro.sott.activities.movieDescription.viewModel.MovieDescriptionViewModel;
import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.fragments.dialog.PlaylistDialogFragment;
import com.astro.sott.player.entitlementCheckManager.EntitlementCheck;
import com.astro.sott.utils.helpers.ActivityLauncher;
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
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.PersonalList;
import com.kaltura.client.types.UserAssetRule;
import com.kaltura.client.types.Value;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class MovieDescriptionActivity extends BaseBindingActivity<MovieScreenBinding> implements DetailRailClick, AlertDialogSingleButtonFragment.AlertDialogListener, PlaylistDialogFragment.EditDialogListener, PlaylistCallback {
    BottomSheetDialog dialog;
    String final_url;
    ArrayList<ParentalLevels> parentalLevels;
    private RailCommonData railData;
    private Asset asset;
    private int layoutType, playlistId = 1;
    private DoubleValue doubleValue;
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
    private String idofasset, name, titleName, id, concatedTitleName, idfromAssetWatchlist;
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
    public MovieScreenBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return MovieScreenBinding.inflate(inflater);
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
        layoutType = layout;
        assetId = asset.getId();
        name = asset.getName();
        isActive = KsPreferenceKey.getInstance(getApplicationContext()).getUserActive();
        map = asset.getTags();
        setPlayerFragment();
        getMediaType(asset, railData);
        callSpecificAsset(assetId);
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

            if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                callProgressBar();
                playerChecks(railData);
            } else {
                DialogHelper.showLoginDialog(MovieDescriptionActivity.this);
            }


        });

    }

    private void checkErrors() {
        if (playerChecksCompleted) {
            if (assetRuleErrorCode == AppLevelConstants.GEO_LOCATION_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeforGeoLocation(1, MovieDescriptionActivity.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.FOR_PURCHASED_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(MovieDescriptionActivity.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.USER_ACTIVE_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(MovieDescriptionActivity.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.NO_MEDIA_FILE) {
                showDialog(getString(R.string.no_media_file));
                callProgressBar();
            }
//            else if (assetRuleErrorCode == AppLevelConstants.PARENTAL_BLOCK) {
//                isParentalLocked = false;
////                if (KsPreferenceKey.getInstance(this).getUserActive())
////                    validateParentalPin();
////                else
//                    startPlayer();
//            }


            else if (errorCode == AppLevelConstants.NO_ERROR) {
                if (KsPreferenceKey.getInstance(this).getUserActive()) {
                    parentalCheck(railData);
                } else {
                    startPlayer();
                }
            }
            PrintLogging.printLog("", "elseValuePrint-->>" + errorCode + "  " + assetRuleErrorCode);
//            else {
//                PrintLogging.printLog("", "elseValuePrint-->>" + assetRuleErrorCode + "  " + errorCode);
//            }
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
                DialogHelper.showValidatePinDialog(MovieDescriptionActivity.this, null, "MOVIE", new ParentalDialogCallbacks() {
                    @Override
                    public void onPositiveClick(String pinText) {
                        ParentalControlViewModel parentalViewModel = ViewModelProviders.of(MovieDescriptionActivity.this).get(ParentalControlViewModel.class);

                        parentalViewModel.validatePin(MovieDescriptionActivity.this, pinText).observe(MovieDescriptionActivity.this, commonResponse -> {
                            if (commonResponse.getStatus()) {
                                DialogHelper.hideValidatePinDialog();
                                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                                playerChecksCompleted = true;
                                //checkErrors();
                                checkOnlyDevice(railData);
                            } else {
                                Toast.makeText(MovieDescriptionActivity.this, getString(R.string.incorrect_parental_pin), Toast.LENGTH_LONG).show();
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
        new HouseHoldCheck().checkHouseholdDevice(MovieDescriptionActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> {
                        startPlayer();
                    });
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(MovieDescriptionActivity.this).refreshKS(response -> checkDevice(railData));
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
            boolean wifiConnected = NetworkConnectivity.isWifiConnected(this);
            boolean connectionPreference = new KsPreferenceKey(this).getDownloadOverWifi();
            if (connectionPreference && !wifiConnected) {
                showWifiDialog();
            } else {
                Intent intent = new Intent(MovieDescriptionActivity.this, PlayerActivity.class);
                intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
                startActivity(intent);
            }

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
                Intent intent = new Intent(MovieDescriptionActivity.this, PlayerActivity.class);
                intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
                startActivity(intent);
            }
        });
    }

    private void playerChecks(final RailCommonData railData) {
        new GeoBlockingCheck().aseetAvailableOrNot(MovieDescriptionActivity.this, railData.getObject(), (status, response, totalCount, errorcode, message) -> {
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

                    default:
                        checkEntitleMent(railData);
                        break;
                }
            }
        }
    }

    private void checkEntitleMent(final RailCommonData railCommonData) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AssetContent.getVideoResolution(asset.getTags()).observe(MovieDescriptionActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String videoResolution) {
                        String fileId = "";
                        if (videoResolution.equals(AppConstants.HD)) {
                            fileId = AppCommonMethods.getFileIdOfAssest(railData.getObject(), AppConstants.HD);
                            AllChannelManager.getInstance().setChannelId(fileId);
                        } else {
                            fileId = AppCommonMethods.getFileIdOfAssest(railData.getObject(), AppConstants.SD);
                            AllChannelManager.getInstance().setChannelId(fileId);
                        }
                        if (fileId.equals("")) {
                            playerChecksCompleted = true;
                            errorCode = AppLevelConstants.NO_MEDIA_FILE;
                            checkErrors();
                        } else {
                            new EntitlementCheck().checkAssetType(MovieDescriptionActivity.this, fileId, (status, response, purchaseKey, errorCode1, message) -> {
                                if (status) {
                                    playerChecksCompleted = true;
                                    if (purchaseKey.equalsIgnoreCase(getResources().getString(R.string.FOR_PURCHASE_SUBSCRIPTION_ONLY)) || purchaseKey.equals(getResources().getString(R.string.FREE))) {
                                        errorCode = AppLevelConstants.NO_ERROR;
                                        railData = railCommonData;
                                        checkErrors();
                                    } else if (purchaseKey.equalsIgnoreCase(getResources().getString(R.string.FOR_PURCHASED))) {
                                        if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                                            isDtvAccountAdded(railCommonData);
                                        } else {
                                            errorCode = AppLevelConstants.FOR_PURCHASED_ERROR;
                                            checkErrors();
                                        }
                                    } else {
                                        if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                                            isDtvAccountAdded(railCommonData);
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
//                playerChecksCompleted = true;

                    }
                });

            }
        });


    }


    private void isDtvAccountAdded(RailCommonData railCommonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewModel.getDtvAccountList().observe(MovieDescriptionActivity.this, new Observer<String>() {
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
            runOnUiThread(() -> DialogHelper.openDialougeFornonDialog(MovieDescriptionActivity.this, false));
        }
        //********** Mobile + Non-Dialog + DTV ******************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.NON_DIALOG) && isDtvAdded == true) {
            runOnUiThread(() -> DialogHelper.openDialougeFornonDialog(MovieDescriptionActivity.this, false));
        }
        //*********** Mobile + Dialog + Non-DTV *****************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded == false) {
            if (AssetContent.isPurchaseAllowed(railCommonData.getObject().getMetas(), railCommonData.getObject(), MovieDescriptionActivity.this)) {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(MovieDescriptionActivity.this, true, false));
            } else {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(MovieDescriptionActivity.this, false, false));
            }
        }
        //************ Mobile + Dialog + DTV ********************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded == true) {
            if (AssetContent.isPurchaseAllowed(railCommonData.getObject().getMetas(), railCommonData.getObject(), MovieDescriptionActivity.this)) {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(MovieDescriptionActivity.this, true, false));
            } else {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(MovieDescriptionActivity.this, false, false));
            }
        } else {
            showDialog(getString(R.string.something_went_wrong_try_again));
        }
    }

    private void checkDevice(final RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(MovieDescriptionActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> checkEntitleMent(railData));
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(MovieDescriptionActivity.this).refreshKS(response -> checkDevice(railData));
                    } else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }
                }
            }
        });
    }

    private void getMediaType(Asset asset, RailCommonData railCommonData) {
        if (asset.getType() == MediaTypeConstant.getTrailer(MovieDescriptionActivity.this)) {
//            getBinding().trailorTxt.setText(getResources().getString(R.string.watch_movie));
            trailor_url = AssetContent.getTrailorUrl(asset);

//            fragment.getUrl(AssetContent.getTrailorUrl(asset), asset, railData.getProgress());
            getRefId(1);
        } else {

//            getUrlToPlay(asset);
            setMovieMetaData(asset, 0);
        }
    }


    private void getRefId(final int type) {
        viewModel.getRefIdLivedata(map).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String ref_id) {
                if (!TextUtils.isEmpty(ref_id)) {
                    PrintLogging.printLog(this.getClass(), "", "refIdPrint" + ref_id);
                    callTrailorAPI(ref_id, type);
                }
            }
        });
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
                    getBinding().setCrewValue(" " + crewText.trim());
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
                getBinding().setCastValue(" " + castTest.trim());
            }

        });
    }

    private void setMetas() {
        viewModel.getGenreLivedata(map).observe(this, new Observer<String>() {
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
//                    PrintLogging.printLog(this.getClass(), "", "trailorYYYYY" + asset.getId() + " " + asset.getType());
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

        setHungamaTag(asset);
    }

    private void setHungamaTag(Asset asset) {
        boolean isProviderAvailable = AssetContent.getHungamaTag(asset.getTags());
        if (isProviderAvailable) {
            getBinding().hungama.setVisibility(View.VISIBLE);
        } else {
            getBinding().hungama.setVisibility(View.GONE);
        }
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

        getBinding().setMovieAssestModel(asset);
        getBinding().setMovieAssestModel(asset);
        setMetas();

        getMovieCasts();
        getMovieCrews();
        if (type == 1) {

            PrintLogging.printLog(this.getClass(), "type 1", "");
        } else {
            getRefId(0);
        }

        assetId = asset.getId();
        assetType = asset.getType();
        //asset.getMediaFiles().get(0).getDuration();


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


                getDuration();

                getMovieYear();

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
        });
    }

    private void getDuration() {
        String duraton = AppCommonMethods.getURLDuration(asset);

        if (!TextUtils.isEmpty(duraton)) {

            StringBuilderHolder.getInstance().append(duraton);
            StringBuilderHolder.getInstance().append(" | ");


            PrintLogging.printLog(this.getClass(), "", "duration " + StringBuilderHolder.getInstance().getText());
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

    }

    private void getMovieRating() {


        if (!TextUtils.isEmpty(AssetContent.getParentalRating(map))) {

            StringBuilderHolder.getInstance().append(AssetContent.getParentalRating(map));
            StringBuilderHolder.getInstance().append(" | ");

        }

    }

    private void setRailFragment() {
        baseRailFragment = (RailBaseFragment) manager.findFragmentById(R.id.rail_fragment);
        if (baseRailFragment != null) {
            baseRailFragment.setViewModel(MovieDescriptionViewModel.class);
            if (assetType == MediaTypeConstant.getShortFilm(this))
                baseRailFragment.callRailAPI((int) assetId, 1, assetType, map, layoutType, AppLevelConstants.TAB_SHORTFILM_DESCRIPTION, asset);
            else
                baseRailFragment.callRailAPI((int) assetId, 1, assetType, map, layoutType, AppLevelConstants.TAB_MOVIE_DESCRIPTION, asset);

        }
    }


    private void callwatchlistApi() {
        if (!KsPreferenceKey.getInstance(this).getUserActive())
            return;

        if (personalLists != null)
            personalLists.clear();
        playlist = new ArrayList<>();
        getIdTocompareWithWatchlist();
        if (isAdded) {
            if (iconClicked) {
                viewModel.deleteWatchlist(idfromAssetWatchlist).observe(MovieDescriptionActivity.this, new Observer<CommonResponse>() {
                    @Override
                    public void onChanged(@Nullable CommonResponse aBoolean) {
                        if (aBoolean.getStatus() == true) {
                            // new ToastHandler(WebSeriesDescriptionActivity.this).show(getApplicationContext().getResources().getString(R.string.removed_from_watchlist));
                            ToastHandler.show(getApplicationContext().getResources().getString(R.string.movie) + " " + getApplicationContext().getResources().getString(R.string.removed_from_watchlist), getApplicationContext());
                            isAdded = false;
                            iconClicked = false;
                            getBinding().playlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play_list), null, null);
                            getBinding().playlist.setTextColor(getResources().getColor(R.color.white));


                        } else {
                            if (aBoolean.getErrorCode().equals("")) {
                                //  showDialog(getString(R.string.something_went_wrong_try_again));
                            } else {
                                if (aBoolean.getErrorCode().equals(AppLevelConstants.ALREADY_UNFOLLOW_ERROR)) {
                                    ToastHandler.show(getApplicationContext().getResources().getString(R.string.movie) + " " + getApplicationContext().getResources().getString(R.string.already_remove_watchlist), getApplicationContext());
                                    //  ToastHandler.show(getApplicationContext().getResources().getString(R.string.removed_from_watchlist), getApplicationContext());
                                    isAdded = false;
                                    getBinding().playlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play_list), null, null);
                                    getBinding().playlist.setTextColor(getResources().getColor(R.color.white));
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
                    if (commonResponse != null) {
                        idfromAssetWatchlist = commonResponse.getAssetID();
                        isAdded = true;
                        getBinding().playlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.playlist_added_check_icon), null, null);
                        getBinding().playlist.setTextColor(getResources().getColor(R.color.primary_blue));
                    }
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


                        CommonPlaylistDialog.getInstance().createBottomSheet(MovieDescriptionActivity.this, playlist, this);
                        iconClicked = false;
                    }
                    isAdded = false;
                    getBinding().playlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play_list), null, null);
                    getBinding().playlist.setTextColor(getResources().getColor(R.color.white));
                }
            });

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (KsPreferenceKey.getInstance(this).getUserActive()) {
                this.idofasset = String.valueOf(assetId);
                if (NetworkConnectivity.isOnline(this)) {
                    //  this.idofasset = String.valueOf(assetId);
                    this.titleName = name;
                    isActive = true;
                    callwatchlistApi();
                }
            }
        } catch (IllegalStateException e) {
            PrintLogging.printLog("ExceptionIs", e.getMessage());
        }
    }

    private void checkConditionForWatchlist(CommonResponse commonResponse) {
        if (!commonResponse.getStatus()) {
            if (iconClicked) {
                //  addToWatchlist();
                iconClicked = false;
            }
            isAdded = false;
            getBinding().playlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play_list), null, null);
            getBinding().playlist.setTextColor(getResources().getColor(R.color.white));

        } else {
            if (KsPreferenceKey.getInstance(MovieDescriptionActivity.this).getUserActive()) {
                idfromAssetWatchlist = commonResponse.getAssetID();
                isAdded = true;
                getBinding().playlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.playlist_added_check_icon), null, null);
                getBinding().playlist.setTextColor(getResources().getColor(R.color.primary_blue));
            }
        }
    }

    private void getIdTocompareWithWatchlist() {
        String one = "media_id='";
        String two = idofasset;
        String three = "'";
        id = one.concat(two).concat(three);
    }


    private void setConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }


    private void setExpandable() {
        getBinding().expandableLayout.collapse();
        getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
        getBinding().setExpandabletext(getResources().getString(R.string.more));
        getBinding().expandableLayout.setOnExpansionUpdateListener(expansionFraction -> getBinding().lessButton.setRotation(0 * expansionFraction));
        getBinding().lessButton.setOnClickListener(view -> {

            getBinding().descriptionText.toggle();

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
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (KsPreferenceKey.getInstance(this).getUserActive()) {
            if (NetworkConnectivity.isOnline(this)) {
                idofasset = String.valueOf(assetId);
                titleName = name;
                isActive = true;
//                if (isParentalLocked)
//                    assetRuleErrorCode = AppLevelConstants.PARENTAL_BLOCK;
                //  callwatchlistApi();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PlayerRepository.getInstance().releasePlayer();
        // this.finish();

    }

    private void setWatchlist() {
        getBinding().playlist.setOnClickListener(view -> {
            iconClicked = true;
            playlist.clear();
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            if (NetworkConnectivity.isOnline(getApplication())) {
                if (KsPreferenceKey.getInstance(this).getUserActive()) {
                    callwatchlistApi();
                } else {
                    new ActivityLauncher(MovieDescriptionActivity.this).loginActivity(MovieDescriptionActivity.this, LoginActivity.class, 0, "");
                }
            } else {
                ToastHandler.show(getResources().getString(R.string.no_internet_connection), MovieDescriptionActivity.this);

            }
            //CommonPlaylistDialog.getInstance().createBottomSheet(MovieDescriptionActivity.this);







           /* if (isActive) {
                if (isAdded) {
                    viewModel.deleteWatchlist(idfromAssetWatchlist).observe(MovieDescriptionActivity.this, aBoolean -> {
                        if (aBoolean != null && aBoolean.getStatus()) {
                            if (assetType == MediaTypeConstant.getMovie(getApplicationContext())) {
                                showAlertDialog(getApplicationContext().getResources().getString(R.string.movie) + " " + getApplicationContext().getResources().getString(R.string.removed_from_watchlist));
                            } else {
                                showAlertDialog(getApplicationContext().getResources().getString(R.string.short_video) + " " + getApplicationContext().getResources().getString(R.string.removed_from_watchlist));

                            }
                            isAdded = false;
                            getBinding().playlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play_list), null, null);
                            getBinding().playlist.setTextColor(getResources().getColor(R.color.white));
                        } else {
                            if (aBoolean != null && aBoolean.getErrorCode().equals("")) {

                                showDialog(aBoolean.getMessage());
//                                        showDialog(aBoolean.getMessage(), 1, getResources().getString(R.string.ok), "", 1);
                            } else {
                                if (aBoolean != null && aBoolean.getErrorCode().equals(AppLevelConstants.ALREADY_UNFOLLOW_ERROR)) {
                                    if (assetType == MediaTypeConstant.getMovie(getApplicationContext())) {
                                        showAlertDialog(getApplicationContext().getResources().getString(R.string.movie) + " " + getApplicationContext().getResources().getString(R.string.already_remove_watchlist));
                                    } else {
                                        showAlertDialog(getApplicationContext().getResources().getString(R.string.short_video) + " " + getApplicationContext().getResources().getString(R.string.already_remove_watchlist));

                                    }
                                    isAdded = false;
                                    getBinding().playlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.play_list), null, null);
                                    getBinding().playlist.setTextColor(getResources().getColor(R.color.white));

                                } else {
                                    if (aBoolean != null)
                                        showDialog(aBoolean.getMessage());
//                                            showDialog(aBoolean.getMessage(), 1, getResources().getString(R.string.ok), "", 1);
                                }

                            }
                        }

                    });
                } else {
                    addToWatchlist();
                }
            } else {

                iconClicked = true;
                new ActivityLauncher(MovieDescriptionActivity.this).loginActivity(MovieDescriptionActivity.this, LoginActivity.class, 0);
            }*/
        });

    }

    private void addToWatchlist(String title) {

        //  getTitletoAddInWatchlist();
        getIdTocompareWithWatchlist();
        if (KsPreferenceKey.getInstance(MovieDescriptionActivity.this).getUserActive()) {

            viewModel.addToWatchlist(id, title, playlistId).observe(this, s -> {
                if (s != null) {
                    checkAddedCondition(s);
                }
            });
        }
    }

    private void checkAddedCondition(CommonResponse s) {
        if (s.getStatus()) {
            if (assetType == MediaTypeConstant.getMovie(getApplicationContext())) {
                showAlertDialog(getApplicationContext().getResources().getString(R.string.movie_text) + " " + getApplicationContext().getResources().getString(R.string.added_to_watchlist));
            } else {
                showAlertDialog(getApplicationContext().getResources().getString(R.string.short_video_text) + " " + getApplicationContext().getResources().getString(R.string.added_to_watchlist));

            }
            idfromAssetWatchlist = s.getAssetID();
            isAdded = true;
            getBinding().playlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.playlist_added_check_icon), null, null);
            getBinding().playlist.setTextColor(getResources().getColor(R.color.primary_blue));

        } else {
            switch (s.getErrorCode()) {
                case "":
                    showDialog(s.getMessage());
//                showDialog(s.getMessage(), 1, getResources().getString(R.string.ok), "", 1);
                    break;
                case AppLevelConstants.ALREADY_FOLLOW_ERROR:
                    if (assetType == MediaTypeConstant.getMovie(getApplicationContext())) {
                        showAlertDialog(getApplicationContext().getResources().getString(R.string.movie) + " " + getApplicationContext().getResources().getString(R.string.already_added_in_watchlist));
                    } else {
                        showAlertDialog(getApplicationContext().getResources().getString(R.string.short_video) + " " + getApplicationContext().getResources().getString(R.string.already_added_in_watchlist));

                    }
                    // callwatchlistApi();
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


    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

        if (getBinding().descriptionText.isExpanded()) {
            getBinding().descriptionText.toggle();
        }
        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
        getDataFromBack(commonData, layoutType);

        idofasset = String.valueOf(assetId);
        titleName = name;
        isAdded = false;
        iconClicked = false;
        isActive = true;
        callwatchlistApi();
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
//                image_url = asset.getImages().get(i).getUrl();
//                image_url = image_url + AppLevelConstants.WIDTH + (int) getResources().getDimension(R.dimen.carousel_image_width) + AppLevelConstants.HEIGHT + (int) getResources().getDimension(R.dimen.carousel_image_height) + AppLevelConstants.QUALITY;
                Log.d("ImageUrlIs", asset.getImages().get(i).getUrl());
                break;
            }
        }
//        if (!StringBuilderHolder.getInstance().getText().toString().equals("")) {
//            Glide.with(getBinding().webseriesimage.getContext()).load(StringBuilderHolder.getInstance().getText().toString()).thumbnail(0.7f).into(getBinding().webseriesimage);
//        }

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
    public void onFinishEditDialog(String text) {
        PrintLogging.printLog(MovieDescriptionActivity.class, "", playlistId + "sdjkfdbjgbjdg");
        if (NetworkConnectivity.isOnline(getApplication())) {
            addToWatchlist(text);
        } else {
            ToastHandler.show(getResources().getString(R.string.no_internet_connection), MovieDescriptionActivity.this);

        }

    }

    @Override
    public void onClick(String name, int caption) {
        if (NetworkConnectivity.isOnline(getApplication())) {
            if (isActive) {
                playlistId = caption;
                addToWatchlist(name);
            }
        } else {
            ToastHandler.show(getResources().getString(R.string.no_internet_connection), MovieDescriptionActivity.this);

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

