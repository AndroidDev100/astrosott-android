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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.activities.parentalControl.viewmodels.ParentalControlViewModel;
import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.activities.subscriptionActivity.ui.SubscriptionDetailActivity;
import com.astro.sott.fragments.dialog.AlertDialogFragment;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.player.entitlementCheckManager.EntitlementCheck;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.helpers.ActivityLauncher;
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
import com.astro.sott.utils.userInfo.UserInfo;
import com.bumptech.glide.Glide;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MediaAsset;
import com.kaltura.client.types.MultilingualStringValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.ProgramAsset;
import com.kaltura.client.types.StringValue;
import com.kaltura.client.types.UserAssetRule;
import com.kaltura.client.types.Value;
import com.kaltura.client.utils.response.base.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;


public class LiveChannel extends BaseBindingActivity<ActivityLiveChannelBinding> implements DetailRailClick,
        AlertDialogSingleButtonFragment.AlertDialogListener, LiveChannelActivityListener, AlertDialogFragment.AlertDialogListener {

    private static final String TAG = "LiveChannel";
    private RailCommonData railData;
    private int layoutType;
    private String vodType, cridId = "";
    private FragmentManager manager;
    private String externalIDs, programName = "";
    private LiveChannelViewModel activityViewModel;
    private long lastClickTime;
    private String image_url = "";
    private boolean xofferWindowValue = false, playbackControlValue = false;

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
    private int indicatorWidth;


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
            if (programAsset != null) {
                FirebaseEventManager.getFirebaseInstance(this).trackScreenName(railData.getObject().getName() + "-" + programAsset.getName());
            } else {
                FirebaseEventManager.getFirebaseInstance(this).trackScreenName(railData.getObject().getName());
            }
            getDataFromBack(railData);
            if (railData.getObject() != null)
                Constants.channelName = railData.getObject().getName();
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
        try {
            ProgramAsset program = (ProgramAsset) programAsset;
            if (program.getCrid() != null)
                cridId = program.getCrid();

            getBinding().programTitle.setText(programAsset.getName());
            getBinding().descriptionText.setText(programAsset.getDescription());
            stringBuilder = new StringBuilder();
            stringBuilder.append(activityViewModel.getStartDate(programAsset.getStartDate()) + " - " + AppCommonMethods.getEndTime(programAsset.getEndDate()) + " | ");
            getImage();
            getGenre();
        } catch (Exception e) {

        }

    }


    private StringBuilder stringBuilder;

    private void getGenre() {


        ///"EEE, d MMM yyyy HH:mm:ss Z"


        activityViewModel.getSubGenreLivedata(programAsset.getTags()).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                if (!TextUtils.isEmpty(s)) {
                    stringBuilder.append(s + " | ");
                }
                getChannelLanguage();
                getParentalRating();
            }
        });


    }

    private void getChannelLanguage() {
        String language = "";
        MultilingualStringValue stringValue = null;
        if (programAsset.getMetas() != null)
            stringValue = (MultilingualStringValue) programAsset.getMetas().get(AppLevelConstants.KEY_LANGUAGE);
        if (stringValue != null)
            language = stringValue.getValue();

        if (language != null && !language.equalsIgnoreCase(""))
            stringBuilder.append(language + " | ");
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
        if (backRailData.getObject() != null && backRailData.getObject().getMetas() != null) {
            getPlayBackControl(backRailData.getObject().getMetas());
            getXofferWindow(backRailData.getObject().getMetas());

        }
        getBinding().vipButtonLive.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            if (vodType.equalsIgnoreCase(EntitlementCheck.FREE)) {
                callProgressBar();
                try {
                    FirebaseEventManager.getFirebaseInstance(this).liveButtonEvent(FirebaseEventManager.WATCH, programAsset, this, railData.getName());
                } catch (Exception e) {
                }
                playerChecks(railData);
            } else if (vodType.equalsIgnoreCase(EntitlementCheck.SVOD)) {
                if (UserInfo.getInstance(this).isActive()) {
                    try {
                        FirebaseEventManager.getFirebaseInstance(this).liveButtonEvent(FirebaseEventManager.TRX_VIP, asset, this, "");
                    } catch (Exception e) {
                    }
                    fileId = AppCommonMethods.getFileIdOfAssest(railData.getObject());
                    if (!fileId.equalsIgnoreCase("")) {
                        Intent intent = new Intent(this, SubscriptionDetailActivity.class);
                        intent.putExtra(AppLevelConstants.FILE_ID_KEY, fileId);
                        startActivity(intent);
                    }
                } else {
                    new ActivityLauncher(LiveChannel.this).astrLoginActivity(LiveChannel.this, AstrLoginActivity.class, "");
                }

            }
        });

        getBinding().share.setOnClickListener(v -> {
            try {
                FirebaseEventManager.getFirebaseInstance(this).shareEvent(asset);
            }catch (Exception e){

            }
            AppCommonMethods.openShareDialog(this, programAsset, this, "");
        });
        getBinding().astroPlayButton.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            if (vodType.equalsIgnoreCase(EntitlementCheck.FREE)) {
                callProgressBar();
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
                    new ActivityLauncher(LiveChannel.this).astrLoginActivity(LiveChannel.this, AstrLoginActivity.class, "");
                }

            }


        });


        callYouMaylAlsoLike();

        getBinding().backImg.setOnClickListener(view -> onBackPressed());
    }

    private void getPlayBackControl(Map<String, Value> metas) {
        if (metas != null) {
            playbackControlValue = activityViewModel.getPlayBackControl(metas);
        } else {
            playbackControlValue = true;
        }
    }

    private String fileId = "";

    private void checkEntitleMent(final RailCommonData railCommonData) {


        fileId = AppCommonMethods.getFileIdOfAssest(railData.getObject());

        new EntitlementCheck().checkAssetPurchaseStatus(LiveChannel.this, fileId, (apiStatus, purchasedStatus, vodType, purchaseKey, errorCode, message) -> {
            this.errorCode = AppLevelConstants.NO_ERROR;
            if (apiStatus) {
                if (purchasedStatus) {
                    runOnUiThread(() -> {
                        if (playbackControlValue) {
                            getBinding().vipButtonLive.setBackground(getResources().getDrawable(R.drawable.gradient_free));
                            getBinding().playText.setText(getResources().getString(R.string.watch_now));
                            if (programAsset != null) {
                                if (programAsset.getStartDate() <= AppCommonMethods.getCurrentTimeStampLong()) {
                                    getBinding().vipButtonLive.setVisibility(View.VISIBLE);
                                } else {
                                    getBinding().vipButtonLive.setVisibility(View.GONE);
                                }
                            } else {
                                getBinding().vipButtonLive.setVisibility(View.VISIBLE);
                            }
//                        getBinding().astroPlayButton.setVisibility(View.VISIBLE);
                            getBinding().starIcon.setVisibility(View.GONE);
                            getBinding().playText.setTextColor(getResources().getColor(R.color.black));

                        }
                    });
                    this.vodType = EntitlementCheck.FREE;

                } else {
                    if (vodType.equalsIgnoreCase(EntitlementCheck.SVOD)) {
                        runOnUiThread(() -> {
                            getBinding().vipButtonLive.setBackground(getResources().getDrawable(R.drawable.gradient_svod));
                            getBinding().playText.setText(getResources().getString(R.string.become_vip));
                            getBinding().playText.setTextColor(getResources().getColor(R.color.white));
                            getBinding().vipButtonLive.setVisibility(View.VISIBLE);
//                                getBinding().astroPlayButton.setVisibility(View.VISIBLE);
                            getBinding().starIcon.setVisibility(View.GONE);
                            getCridDetail();
                        });
                        this.vodType = EntitlementCheck.SVOD;


                    } else if (vodType.equalsIgnoreCase(EntitlementCheck.TVOD)) {
                        runOnUiThread(() -> {
//                                getBinding().astroPlayButton.setVisibility(View.VISIBLE);
                            getBinding().vipButtonLive.setBackground(getResources().getDrawable(R.drawable.gradient_svod));
                            getBinding().playText.setText(getResources().getString(R.string.rent_movie));
                            getBinding().vipButtonLive.setVisibility(View.VISIBLE);
//                                getBinding().astroPlayButton.setVisibility(View.VISIBLE);
                            getBinding().starIcon.setVisibility(View.GONE);
                            getBinding().playText.setTextColor(getResources().getColor(R.color.white));


                        });

                        this.vodType = EntitlementCheck.TVOD;


                    }
                }

            } else {

            }
        });


    }

    private void getCridDetail() {
        if (UserInfo.getInstance(this).isActive()) {
            if (!cridId.equalsIgnoreCase("")) {
                activityViewModel.getCridDetail(cridId).observe(this, asset1 -> {
                    if (asset1 != null) {
                        checkCridEntitleMent(asset1);
                    }
                });
            }
        }
    }

    private Asset cridAsset;

    private void checkCridEntitleMent(Asset asset1) {
        cridAsset = asset1;
        String cridFileId = AppCommonMethods.getFileIdOfAssest(asset1);
        new EntitlementCheck().checkAssetPurchaseStatus(LiveChannel.this, cridFileId, (apiStatus, purchasedStatus, vodType, purchaseKey, errorCode, message) -> {
            this.errorCode = AppLevelConstants.NO_ERROR;
            if (apiStatus) {
                if (purchasedStatus) {
                    runOnUiThread(() -> {
                        showAlertDialog(asset1.getName(), "");
                    });

                } else {

                }

            } else {

            }
        });
    }

    private void showAlertDialog(String title, String msg) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance(title, getResources().getString(R.string.event_is_live), getResources().getString(R.string.go), getResources().getString(R.string.cancel));
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(Objects.requireNonNull(fm), "fragment_alert");
    }

    private void getXofferWindow(Map<String, Value> metas) {
        StringValue stringValue = null;
        String xofferValue = "";
        if (metas != null) {
            stringValue = (StringValue) metas.get(AppLevelConstants.XOFFERWINDOW);
        }
        if (stringValue != null) {
            xofferValue = stringValue.getValue();
        }
        if (!xofferValue.equalsIgnoreCase("")) {
            xofferWindowValue = activityViewModel.isXofferWindow(xofferValue);
        } else {
            xofferWindowValue = true;
        }
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
                if (UserInfo.getInstance(this).isActive()) {
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
        if (UserInfo.getInstance(this).isActive()) {
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
        intent.putExtra("programAsset", programAsset);
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
        checkEntitleMent(railData);
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
//                    case PARENTAL:
//                        assetRuleErrorCode = AppLevelConstants.PARENTAL_BLOCK;
//                        checkEntitleMent(railData);
//                        break;
                    default:
                        playerChecksCompleted = true;
                        checkErrors();
                        break;
                }
            }
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
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            getBinding().tabLayout.setLayoutParams(params);
        }
        LiveChannelPagerAdapter liveChannelPagerAdapter = new LiveChannelPagerAdapter(this, getSupportFragmentManager(), railData, adapterCount);
        getBinding().pager.setAdapter(liveChannelPagerAdapter);
        if ((adapterCount > 0)) {

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

            getBinding().indicator.setVisibility(View.VISIBLE);
            getBinding().blackLine.setVisibility(View.VISIBLE);

            getBinding().tabLayout.setVisibility(View.VISIBLE);
            getBinding().tabLayout.setBackground(getDrawable(R.drawable.tab_bg));
        }
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

        checkEntitleMent(railData);
        getBinding().pager.disableScroll(true);
        getBinding().pager.setOffscreenPageLimit(0);
    }

    @Override
    public void onFinishDialog() {
        RailCommonData railCommonData = new RailCommonData();
        railCommonData.setObject(cridAsset);
        new ActivityLauncher(this).liveEventActivity(railCommonData, this);
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
//            getBinding().scrollViewProgrssBar.setVisibility(showProgressBar ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void showScrollViewProgressBarView(boolean showProgressBarView) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            showScrollViewProgressBar(showProgressBarView);
        }
    }


}
