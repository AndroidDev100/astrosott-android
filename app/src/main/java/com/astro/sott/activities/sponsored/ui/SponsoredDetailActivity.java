package com.astro.sott.activities.sponsored.ui;

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
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.astro.sott.R;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.activities.movieDescription.viewModel.MovieDescriptionViewModel;
import com.astro.sott.activities.parentalControl.viewmodels.ParentalControlViewModel;
import com.astro.sott.activities.signUp.ui.SignUpActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.SponsoredTabData;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.ParentalDialogCallbacks;
import com.astro.sott.databinding.BoxSetDetailBinding;
import com.astro.sott.databinding.SponsoredDetailBinding;
import com.astro.sott.fragments.detailRailFragment.DetailRailFragment;
import com.astro.sott.fragments.detailRailFragment.adapter.DetailPagerAdapter;
import com.astro.sott.fragments.detailRailFragment.adapter.SponsoredPagerAdapter;
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
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.StringBuilderHolder;
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
import java.util.List;
import java.util.Map;


public class SponsoredDetailActivity extends BaseBindingActivity<SponsoredDetailBinding> implements DetailRailClick, AlertDialogSingleButtonFragment.AlertDialogListener {
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
    private int indicatorWidth;
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
    public SponsoredDetailBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return SponsoredDetailBinding.inflate(inflater);
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
        if (asset.getName() != null)
            FirebaseEventManager.getFirebaseInstance(SponsoredDetailActivity.this).trackScreenName(asset.getName());
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
                runOnUiThread(() -> DialogHelper.openDialougeforGeoLocation(1, SponsoredDetailActivity.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.USER_ACTIVE_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(SponsoredDetailActivity.this));
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
                DialogHelper.showValidatePinDialog(SponsoredDetailActivity.this, null, "MOVIE", new ParentalDialogCallbacks() {
                    @Override
                    public void onPositiveClick(String pinText) {
                        ParentalControlViewModel parentalViewModel = ViewModelProviders.of(SponsoredDetailActivity.this).get(ParentalControlViewModel.class);

                        parentalViewModel.validatePin(SponsoredDetailActivity.this, pinText).observe(SponsoredDetailActivity.this, commonResponse -> {
                            if (commonResponse.getStatus()) {
                                DialogHelper.hideValidatePinDialog();
                                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                                playerChecksCompleted = true;
                                //checkErrors();
                                checkOnlyDevice(railData);
                            } else {
                                Toast.makeText(SponsoredDetailActivity.this, getString(R.string.incorrect_parental_pin), Toast.LENGTH_LONG).show();
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
                    getBinding().watchList.setTextColor(getResources().getColor(R.color.grey));

                }
            } else {
                isAdded = false;
                getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.favorite_unselected), null, null);
                getBinding().watchList.setTextColor(getResources().getColor(R.color.grey));
            }
        });
    }

    private void checkOnlyDevice(RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(SponsoredDetailActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> {
                        startPlayer();
                    });
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(SponsoredDetailActivity.this).refreshKS(response -> checkDevice(railData));
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
            Intent intent = new Intent(SponsoredDetailActivity.this, PlayerActivity.class);
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
        new GeoBlockingCheck().aseetAvailableOrNot(SponsoredDetailActivity.this, railData.getObject(), (status, response, totalCount, errorcode, message) -> {
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

    private void checkEntitleMent(final RailCommonData railCommonData) {
        String fileId = "";

        fileId = AppCommonMethods.getFileIdOfAssest(railData.getObject());

        new EntitlementCheck().checkAssetPurchaseStatus(SponsoredDetailActivity.this, fileId, (apiStatus, purchasedStatus, vodType, purchaseKey, errorCode, message) -> {
            this.errorCode = AppLevelConstants.NO_ERROR;
            if (apiStatus) {
                if (purchasedStatus) {
                    runOnUiThread(() -> {
                        getBinding().astroPlayButton.setBackground(getResources().getDrawable(R.drawable.gradient_free));
                        getBinding().playText.setText(getResources().getString(R.string.watch_now));
                        getBinding().astroPlayButton.setVisibility(View.VISIBLE);
                        getBinding().playText.setTextColor(getResources().getColor(R.color.black));


                    });
                    this.vodType = EntitlementCheck.FREE;

                } else {
                    if (vodType.equalsIgnoreCase(EntitlementCheck.SVOD)) {
                        if (xofferWindowValue) {
                            runOnUiThread(() -> {
                                getBinding().astroPlayButton.setBackground(getResources().getDrawable(R.drawable.gradient_svod));
                                getBinding().playText.setText(getResources().getString(R.string.become_vip));
                                getBinding().astroPlayButton.setVisibility(View.VISIBLE);
                                getBinding().playText.setTextColor(getResources().getColor(R.color.white));

                            });
                        }
                        this.vodType = EntitlementCheck.SVOD;

                    } else if (vodType.equalsIgnoreCase(EntitlementCheck.TVOD)) {
                        if (xofferWindowValue) {
                            runOnUiThread(() -> {
                                getBinding().astroPlayButton.setBackground(getResources().getDrawable(R.drawable.gradient_button));
                                getBinding().playText.setText(getResources().getString(R.string.rent_movie));
                                getBinding().astroPlayButton.setVisibility(View.VISIBLE);
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


    private void checkDevice(final RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(SponsoredDetailActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> checkEntitleMent(railData));
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(SponsoredDetailActivity.this).refreshKS(response -> checkDevice(railData));
                    } else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }
                }
            }
        });
    }

    private void getMediaType(Asset asset, RailCommonData railCommonData) {
        if (asset.getType() == MediaTypeConstant.getTrailer(SponsoredDetailActivity.this)) {
            trailor_url = AssetContent.getTrailorUrl(asset);
            getRefId(1, asset);
        } else {
            setMovieMetaData(asset, 0);
        }
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
            try {
                CleverTapManager.getInstance().socialShare(this, asset, false);
                FirebaseEventManager.getFirebaseInstance(this).shareEvent(asset, this);
            } catch (Exception e) {

            }
            openShareDialouge();
        });
        setWatchlist();

        // setRailFragment();
        setRailBaseFragment();
    }

    private void setWatchlist() {
        getBinding().watchList.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            FirebaseEventManager.getFirebaseInstance(this).clickButtonEvent("add_mylist", asset, this);
            if (NetworkConnectivity.isOnline(getApplication())) {
                if (UserInfo.getInstance(this).isActive()) {
                    if (isAdded) {
                        deleteWatchlist();
                    } else {
                        addToWatchlist(titleName);
                    }
                } else {
                    new ActivityLauncher(SponsoredDetailActivity.this).signupActivity(SponsoredDetailActivity.this, SignUpActivity.class, CleverTapManager.DETAIL_PAGE_MY_LIST);
                }
            } else {
                ToastHandler.show(getResources().getString(R.string.no_internet_connection), SponsoredDetailActivity.this);

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
            Toast.makeText(this, getApplicationContext().getResources().getString(R.string.show_is) + " " + getApplicationContext().getResources().getString(R.string.added_to_watchlist), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, getApplicationContext().getResources().getString(R.string.show_is) + " " + getApplicationContext().getResources().getString(R.string.already_added_in_watchlist), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    showDialog(s.getMessage());
                    break;
            }

        }
    }

    private void deleteWatchlist() {
        viewModel.deleteWatchlist(idfromAssetWatchlist).observe(SponsoredDetailActivity.this, aBoolean -> {
            if (aBoolean != null && aBoolean.getStatus()) {
                isAdded = false;
                Toast.makeText(this, getApplicationContext().getResources().getString(R.string.show_is) + " " + getApplicationContext().getResources().getString(R.string.removed_from_watchlist), Toast.LENGTH_SHORT).show();
                getBinding().watchList.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.favorite_unselected), null, null);
                getBinding().watchList.setTextColor(getResources().getColor(R.color.grey));
            } else {
                if (aBoolean != null && aBoolean.getErrorCode().equals("")) {
                    showDialog(aBoolean.getMessage());
                } else {
                    if (aBoolean != null && aBoolean.getErrorCode().equals(AppLevelConstants.ALREADY_UNFOLLOW_ERROR)) {
                        isAdded = false;
                        Toast.makeText(this, getApplicationContext().getResources().getString(R.string.show_is) + " " + getApplicationContext().getResources().getString(R.string.removed_from_watchlist), Toast.LENGTH_SHORT).show();
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

    private void setRailBaseFragment() {
        viewPagerSetup();
    /*    FragmentManager fm = getSupportFragmentManager();
        BoxSetDetailFragment boxSetDetailFragment = new BoxSetDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        boxSetDetailFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.rail_fragment, boxSetDetailFragment).commitNow();*/
    }

    private int tabCount = 0;
    private List<SponsoredTabData> sponsoredTabDataList;

    private void setViewPager() {

    }

    private void viewPagerSetup() {
        sponsoredTabDataList = new ArrayList<>();
        sponsoredTabDataList.addAll(viewModel.getTabsData(map));
        tabCount = sponsoredTabDataList.size();
        try {
            if (tabCount == 1) {
                ViewGroup.LayoutParams params = getBinding().tabLayout.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                getBinding().tabLayout.setLayoutParams(params);
            }

            SponsoredPagerAdapter detailPagerAdapter = new SponsoredPagerAdapter(getSupportFragmentManager(), sponsoredTabDataList);
            getBinding().pager.setAdapter(detailPagerAdapter);
            getBinding().pager.disableScroll(true);

            if ((tabCount > 0)) {

                getBinding().tabLayout.setupWithViewPager(getBinding().pager);

                getBinding().tabLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        if ((getBinding().tabLayout.getTabCount() > 0)) {
                            indicatorWidth = getBinding().tabLayout.getWidth() / getBinding().tabLayout.getTabCount();
                        }
                        Log.d("TabCount", getBinding().tabLayout.getTabCount() + "");

                        Log.d("tabLayout", getBinding().tabLayout.getWidth() + "");
                        Log.d("indicator", indicatorWidth + "");
                        //Assign new width
                        RelativeLayout.LayoutParams indicatorParams = (RelativeLayout.LayoutParams) getBinding().indicator.getLayoutParams();
                        indicatorParams.width = indicatorWidth;
                        getBinding().indicator.setLayoutParams(indicatorParams);
                    }
                });
                getBinding().pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getBinding().indicator.getLayoutParams();
                        //Multiply positionOffset with indicatorWidth to get translation
                        float translationOffset = (positionOffset + i) * (indicatorWidth);
                        params.leftMargin = (int) translationOffset;
                        getBinding().indicator.setLayoutParams(params);

                    }

                    @Override
                    public void onPageSelected(int i) {

                        getBinding().pager.reMeasureCurrentPage(i);
                    }

                    @Override
                    public void onPageScrollStateChanged(int i) {


                    }
                });
                changeTabsFont();
                getBinding().indicator.setVisibility(View.VISIBLE);
                getBinding().blackLine.setVisibility(View.VISIBLE);

                getBinding().tabLayout.setVisibility(View.VISIBLE);
            }
        } catch (ArithmeticException e) {
            Log.d("TAG", e + "");
        }
    }

    private void changeTabsFont() {
        //  Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/"+ Constants.FontStyle);
        ViewGroup vg = (ViewGroup) getBinding().tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setSingleLine();
                }
            }
        }
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
        if (yearMap != null)
            playbackControlValue = viewModel.getPlayBackControl(yearMap);
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
        AppCommonMethods.openShareDialog(this, asset, getApplicationContext(), "");
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

