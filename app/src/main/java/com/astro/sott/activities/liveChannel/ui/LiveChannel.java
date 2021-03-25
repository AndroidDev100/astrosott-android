package com.astro.sott.activities.liveChannel.ui;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.astro.sott.activities.parentalControl.viewmodels.ParentalControlViewModel;
import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.player.entitlementCheckManager.EntitlementCheck;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.R;
import com.astro.sott.activities.liveChannel.adapter.LiveChannelPagerAdapter;
import com.astro.sott.activities.liveChannel.listener.LiveChannelActivityListener;
import com.astro.sott.activities.liveChannel.viewModel.LiveChannelViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.ParentalDialogCallbacks;
import com.astro.sott.databinding.ActivityLiveChannelBinding;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.fragments.schedule.listeners.LiveChannelCommunicator;
import com.astro.sott.modelClasses.dmsResponse.ParentalLevels;
import com.astro.sott.networking.refreshToken.RefreshKS;
import com.astro.sott.player.geoBlockingManager.GeoBlockingCheck;
import com.astro.sott.player.houseHoldCheckManager.HouseHoldCheck;
import com.astro.sott.player.ui.PlayerActivity;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.DialogHelper;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.StringBuilderHolder;
import com.astro.sott.utils.helpers.shimmer.Constants;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.bumptech.glide.Glide;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MediaAsset;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.ProgramAsset;
import com.kaltura.client.types.UserAssetRule;
import com.kaltura.client.utils.response.base.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.ArrayList;
import java.util.Map;
import java.util.TimeZone;


public class LiveChannel extends BaseBindingActivity<ActivityLiveChannelBinding> implements DetailRailClick,
        AlertDialogSingleButtonFragment.AlertDialogListener, LiveChannelActivityListener {

    private static final String TAG = "LiveChannel";
    private RailCommonData railData;
    private int layoutType;
    private FragmentManager manager;
    private String externalIDs, programName;
    private LiveChannelViewModel activityViewModel;
    private long lastClickTime;
    private String image_url = "";
    private int errorCode = -1;
    private Asset asset;
    private boolean playerChecksCompleted = false;
    private int assetRuleErrorCode = -1;
    private boolean isParentalLocked = false;
    private String defaultParentalRating = "";
    private String userSelectedParentalRating = "";
    private int userSelectedParentalPriority;
    private int priorityLevel;
    private int assetRestrictionLevel;
    ArrayList<ParentalLevels> parentalLevels;
    private Map<String, MultilingualStringValueArray> map;
    private List<RailCommonData> mRailCommonDataList;
    private boolean isLiveChannel = true;
    private boolean isDtvAdded = false;

    private LiveChannelCommunicator mLiveChannelCommunicator;
    private boolean assetKey = false;
    private Asset programAsset;

    @Override
    public ActivityLiveChannelBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityLiveChannelBinding.inflate(inflater);
    }

    public void setLiveChannelCommunicator(LiveChannelCommunicator liveChannelCommunicator) {
        this.mLiveChannelCommunicator = liveChannelCommunicator;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("LIVE CHANNEL", "TRUE");
        activityViewModel = ViewModelProviders.of(this).get(LiveChannelViewModel.class);
        parentalLevels = new ArrayList<>();
        connectionObserver();
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(LiveChannel.this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            intentValues();
            getBinding().pager.disableScroll(true);
            getBinding().pager.setOffscreenPageLimit(0);

        } else {
            noConnectionLayout();
        }
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }



    private void intentValues() {
        if (getIntent().getExtras() != null)
            railData = getIntent().getExtras().getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
        programAsset = getIntent().getExtras().getParcelable(AppLevelConstants.PROGRAM_ASSET);
        if (programAsset != null)
            setProgramMetas();
        if (railData != null) {
            getDataFromBack(railData);
            Constants.channelName = railData.getName();
            setImages(railData, this, getBinding().channelLogo);
        }
    }

    public static void setImages(RailCommonData railCommonData, Context context, ImageView webseriesimage) {
        try {
            if (railCommonData == null) {
                return;
            }
            if (railCommonData.getObject().getImages().size() > 0) {
                for (int i = 0; i < railCommonData.getObject().getImages().size(); i++) {
                    if (railCommonData.getObject().getImages().get(i).getRatio().equals("16x9")) {
                        String image_url = railCommonData.getObject().getImages().get(i).getUrl();
                        String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.detail_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.carousel_image_height) + AppLevelConstants.QUALITY;
                        ImageHelper.getInstance(webseriesimage.getContext()).loadImageToPotrait(webseriesimage, final_url, R.drawable.square1);
                    }
                }
            }
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "" + e);
        }

    }

    private void setProgramMetas() {
        getBinding().programTitle.setText(programAsset.getName());
        getBinding().descriptionText.setText(programAsset.getDescription());
        stringBuilder = new StringBuilder();
        stringBuilder.append(activityViewModel.getStartDate(programAsset.getStartDate()) + " | ");
        getImage();
        getGenre();

    }


    private StringBuilder stringBuilder;

    private void getGenre() {


        ///"EEE, d MMM yyyy HH:mm:ss Z"


        activityViewModel.getGenreLivedata(programAsset.getTags()).observe(this, new Observer<String>() {
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
        if (!TextUtils.isEmpty(AssetContent.getParentalRating(programAsset.getTags()))) {

            stringBuilder.append(AssetContent.getParentalRating(programAsset.getTags()));

        }
        String value = stringBuilder.toString();
       /* if (value.length() > 0) {
            value = StringBuilderHolder.getInstance().getText().substring(0, value.length() - 2);
        }*/
        getBinding().metas.setText(value);
        StringBuilderHolder.getInstance().clear();

    }

    private void getDataFromBack(RailCommonData backRailData) {
        AllChannelManager.getInstance().setRailCommonData(backRailData);
        PrintLogging.printLog(this.getClass(), "", "programAssetId" + backRailData.getObject().getName());
        /*if (backRailData.getObject().getType() == MediaTypeConstant.getProgram(LiveChannel.this)) {
            getSpecificAsset(backRailData);
        } else {
            setProgrameValues(backRailData);
            viewPagerIntializtion();

        }*/
        getBinding().astroPlayButton.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            getBinding().includeProgressbar.progressBar.setOnClickListener(view1 -> {

            });

            /*if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                callProgressBar();
                programName = Constants.programName;
                playerChecks(railData);
            }else {
                DialogHelper.showLoginDialog(LiveChannel.this);
            }*/
            callProgressBar();
            programName = programAsset.getName();
            playerChecks(railData);


        });
        callYouMaylAlsoLike();

        getBinding().backImg.setOnClickListener(view -> onBackPressed());
    }

    private int adapterCount = 1;

    private void callYouMaylAlsoLike() {
        long assetId = railData.getObject().getId();
        asset = railData.getObject();
        activityViewModel.setYouMayAlsoLikeData(null);
        activityViewModel.getYouMayAlsoLike((int) assetId, 1, asset.getType(), asset.getTags()).observe(this, assetCommonBeans -> {
            try {
                if (assetCommonBeans.size() > 0) {
                    if (assetCommonBeans.get(0).getStatus()) {
                        activityViewModel.setYouMayAlsoLikeData(assetCommonBeans.get(0).getRailAssetList());
                        adapterCount = 2;
                        viewPagerIntializtion();
                    } else {
                        viewPagerIntializtion();
                    }
                } else {
                    viewPagerIntializtion();
                }
            } catch (Exception e) {
            }


        });
    }


    private void setProgrameValues(RailCommonData backRailData) {
        railData = backRailData;
        asset = railData.getObject();
        setPlayerFragment();
    }


    private void setPlayerFragment() {


        //getBinding().ivPlayIcon.setClickable(true);

    }

    private void getImage() {
        if (programAsset.getImages().size() > 0) {
            for (int i = 0; i < programAsset.getImages().size(); i++) {
                if (programAsset.getImages().get(i).getRatio().equals("16x9")) {
                    String image_url = programAsset.getImages().get(i).getUrl();
                    String final_url = image_url + AppLevelConstants.WIDTH + (int) getResources().getDimension(R.dimen.detail_image_width) + AppLevelConstants.HEIGHT + (int) getResources().getDimension(R.dimen.carousel_image_height) + AppLevelConstants.QUALITY;
                    ImageHelper.getInstance(getBinding().playerImage.getContext()).loadImageToPotrait(getBinding().playerImage, final_url, R.drawable.square1);
                }
            }
        }

    }

    private void getSpecificAsset(RailCommonData asset) {
        if (asset.getObject().getType() == MediaTypeConstant.getProgram(LiveChannel.this)) {
            ProgramAsset progAsset = (ProgramAsset) asset.getObject();

            PrintLogging.printLog(this.getClass(), "", "programAssetId" + progAsset.getLinearAssetId());
            activityViewModel.getSpecificAsset(progAsset.getLinearAssetId().toString()).observe(this, railCommonData -> {
                if (railCommonData != null && railCommonData.getStatus()) {
                    setProgrameValues(railCommonData);
                    viewPagerIntializtion();
                }
            });
        } else {
            setProgrameValues(asset);

            activityViewModel.getSpecificAsset(String.valueOf(asset.getId())).observe(this, railCommonData -> {
                if (railCommonData != null && railCommonData.getStatus()) {
                }
            });
        }

    }


    private void checkErrors() {
        if (playerChecksCompleted) {
            if (assetRuleErrorCode == AppLevelConstants.GEO_LOCATION_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeforGeoLocation(1, LiveChannel.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.FOR_PURCHASED_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(LiveChannel.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.USER_ACTIVE_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(LiveChannel.this));
                callProgressBar();
            }
//            else if (assetRuleErrorCode == AppLevelConstants.PARENTAL_BLOCK) {
//                isParentalLocked = true;
//
//                if (KsPreferenceKey.getInstance(this).getUserActive())
//                    validateParentalPin();
//                else
//                    startPlayer();
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
        Intent intent = new Intent(LiveChannel.this, PlayerActivity.class);
        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        intent.putExtra("isLivePlayer", true);
        intent.putExtra(AppLevelConstants.PROGRAM_NAME, programName);
        startActivity(intent);
    }

    private void checkOnlyDevice(RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(LiveChannel.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) runOnUiThread(() -> {
                    startPlayer();
                });
                else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(LiveChannel.this).refreshKS(response -> checkDevice(railData));
                    } else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }
                }


            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (KsPreferenceKey.getInstance(this).getUserActive()) {
            if (NetworkConnectivity.isOnline(this)) {
                if (isParentalLocked)
                    assetRuleErrorCode = AppLevelConstants.PARENTAL_BLOCK;

            }
        }
    }

    private void validateParentalPin(RailCommonData railData) {


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.showValidatePinDialog(LiveChannel.this, null, "LiveChannel", new ParentalDialogCallbacks() {
                    @Override
                    public void onPositiveClick(String pinText) {
                        ParentalControlViewModel parentalViewModel = ViewModelProviders.of(LiveChannel.this).get(ParentalControlViewModel.class);

                        parentalViewModel.validatePin(LiveChannel.this, pinText).observe(LiveChannel.this, commonResponse -> {
                            if (commonResponse.getStatus()) {
                                DialogHelper.hideValidatePinDialog();
                                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                                playerChecksCompleted = true;
                                // checkErrors();
                                checkOnlyDevice(railData);
                            } else {
                                Toast.makeText(LiveChannel.this, getString(R.string.incorrect_parental_pin), Toast.LENGTH_LONG).show();
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

    private void playerChecks(final RailCommonData railData) {
        new GeoBlockingCheck().aseetAvailableOrNot(LiveChannel.this, railData.getObject(), (status, response, totalCount, errorcode, message) -> {
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

    private void checkUserLoginCondition(RailCommonData railData) {
//        boolean status = KsPreferenceKey.getInstance(getApplicationContext()).getUserActive();
//        if (!status) {
        checkEntitleMent(railData);
//        }else{
//            playerChecksCompleted=true;
//        }
    }

    private void checkEntitleMent(final RailCommonData railCommonData) {
        String fileId = AppCommonMethods.getFileIdOfAssest(railData.getObject());
        new EntitlementCheck().checkAssetType(LiveChannel.this, fileId, (status, response, purchaseKey, errorCode1, message) -> {
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

    //DynamicData Api Call to Check DtvAccount
    private void isDtvAccountAdded(RailCommonData railCommonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activityViewModel.getDtvAccountList().observe(LiveChannel.this, new Observer<String>() {
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
            runOnUiThread(() -> DialogHelper.openDialougeFornonDialog(LiveChannel.this, isLiveChannel));
        }
        //********** Mobile + Non-Dialog + DTV ******************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.NON_DIALOG) && isDtvAdded == true) {
            runOnUiThread(() -> DialogHelper.openDialougeFornonDialog(LiveChannel.this, isLiveChannel));
        }
        //*********** Mobile + Dialog + Non-DTV *****************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded == false) {
            if (AssetContent.isPurchaseAllowed(railCommonData.getObject().getMetas(), railCommonData.getObject(), LiveChannel.this)) {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(LiveChannel.this, true, isLiveChannel));
            } else {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(LiveChannel.this, false, isLiveChannel));
            }
        }
        //************ Mobile + Dialog + DTV ********************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded == true) {
            if (AssetContent.isPurchaseAllowed(railCommonData.getObject().getMetas(), railCommonData.getObject(), LiveChannel.this)) {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(LiveChannel.this, true, isLiveChannel));
            } else {
                runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(LiveChannel.this, false, isLiveChannel));
            }
        } else {
            showDialog(getString(R.string.something_went_wrong_try_again));
        }
    }

    private void showDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void checkDevice(final RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(LiveChannel.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> checkEntitleMent(railData));

                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(LiveChannel.this).refreshKS(response -> checkDevice(railData));
                    } else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }
                }


            }

        });

    }

    private void viewPagerIntializtion() {
        if (adapterCount == 1) {
            ViewGroup.LayoutParams params = getBinding().tabLayout.getLayoutParams();
            params.width = (int) 650;
            getBinding().tabLayout.setLayoutParams(params);
        }
        LiveChannelPagerAdapter liveChannelPagerAdapter = new LiveChannelPagerAdapter(this, getSupportFragmentManager(), railData, adapterCount);
        getBinding().pager.setAdapter(liveChannelPagerAdapter);
        getBinding().tabLayout.setupWithViewPager(getBinding().pager);
        getBinding().pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                getBinding().pager.reMeasureCurrentPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        getBinding().tabLayout.setVisibility(View.VISIBLE);
        /*if (getResources().getBoolean(R.bool.isTablet)) {
            getBinding().scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY) {
                        //code to fetch more data for endless scrolling
                        int selectedTabPosition = getBinding().pager.getCurrentItem();
                        if (selectedTabPosition == 1) {
                            mLiveChannelCommunicator.loadMoreData(oldScrollX, oldScrollY);
                        }
                    }
                }
            });
        }*/
    }

    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {
        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
        getDataFromBack(commonData);
        getBinding().pager.disableScroll(true);
        getBinding().pager.setOffscreenPageLimit(0);
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

    private void showScrollViewProgressBar(boolean showProgressBar) {
        runOnUiThread(() -> {
            getBinding().scrollViewProgrssBar.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void showScrollViewProgressBarView(boolean showProgressBarView) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            showScrollViewProgressBar(showProgressBarView);
        }
    }


}
