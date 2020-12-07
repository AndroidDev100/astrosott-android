package com.dialog.dialoggo.activities.webSeriesDescription.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
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
import android.view.ViewGroup;
import android.widget.Toast;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.loginActivity.LoginActivity;
import com.dialog.dialoggo.activities.parentalControl.viewmodels.ParentalControlViewModel;
import com.dialog.dialoggo.activities.subscription.manager.AllChannelManager;
import com.dialog.dialoggo.activities.webEpisodeDescription.adapter.WebEpisodeDescriptionCommonAdapter;
import com.dialog.dialoggo.activities.webSeriesDescription.viewModel.WebSeriesDescriptionViewModel;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.beanModel.VIUChannel;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.callBacks.commonCallBacks.DetailRailClick;
import com.dialog.dialoggo.callBacks.commonCallBacks.MoreLikeThis;
import com.dialog.dialoggo.callBacks.commonCallBacks.ParentalDialogCallbacks;
import com.dialog.dialoggo.databinding.ActivityWebSeriesDescriptionBinding;
import com.dialog.dialoggo.fragments.detailRailFragment.DetailRailFragment;
import com.dialog.dialoggo.fragments.dialog.AlertDialogFragment;
import com.dialog.dialoggo.fragments.dialog.AlertDialogSingleButtonFragment;
import com.dialog.dialoggo.modelClasses.dmsResponse.ParentalLevels;
import com.dialog.dialoggo.modelClasses.dmsResponse.ResponseDmsModel;
import com.dialog.dialoggo.networking.refreshToken.RefreshKS;
import com.dialog.dialoggo.player.entitlementCheckManager.EntitlementCheck;
import com.dialog.dialoggo.player.geoBlockingManager.GeoBlockingCheck;
import com.dialog.dialoggo.player.houseHoldCheckManager.HouseHoldCheck;
import com.dialog.dialoggo.player.ui.PlayerActivity;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.constants.AppConstants;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.AssetContent;
import com.dialog.dialoggo.utils.helpers.DialogHelper;
import com.dialog.dialoggo.utils.helpers.MediaTypeConstant;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.StringBuilderHolder;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.dialog.dialoggo.utils.helpers.shimmer.Constants;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.UserAssetRule;
import com.kaltura.client.types.Value;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WebSeriesDescriptionActivity extends BaseBindingActivity<ActivityWebSeriesDescriptionBinding> implements DetailRailClick, AlertDialogSingleButtonFragment.AlertDialogListener, AlertDialogFragment.AlertDialogListener, MoreLikeThis {
    private final Handler mHandler = new Handler();
    private final List<AssetCommonBean> loadedList = new ArrayList<>();
    private Asset asset;
    private int layoutType;
    private WebSeriesDescriptionViewModel viewModel;
    private Map<String, MultilingualStringValueArray> map;
    private String image_url = "";
    private Map<String, Value> yearMap;
    private DoubleValue doubleValue;
    private boolean isActive, isAdded;
    private long assetId;
    private boolean iconClicked = false;
    private long lastClickTime;
    private String fileId = "";
    private boolean isPurchased;
    private String idfromAssetWatchlist, cast = "", crew = "", genre, language, subGenre;
    private WebEpisodeDescriptionCommonAdapter adapter = null;
    private int tempCount = 0;
    private List<Integer> seriesNumberList;
    private List<AssetCommonBean> clipList;
    private List<VIUChannel> channelList;
    private List<VIUChannel> dtChannelsList;
    private int seasonCounter = 0;
    private int counter = 0;
    private boolean playerChecksCompleted = false;
    private int errorCode = -1;
    private boolean episodeTested = false;
    private int assetRuleErrorCode = -1;
    private RailCommonData railData;
    private boolean isParentalLocked = false;
    private String defaultParentalRating = "";
    private String userSelectedParentalRating = "";
    private int userSelectedParentalPriority;
    private int priorityLevel;
    private int assetRestrictionLevel;
    ArrayList<ParentalLevels> parentalLevels;
    private RailCommonData commonData;
    private boolean assetKey = false;
    private boolean isDtvAdded = false;

    @Override
    public ActivityWebSeriesDescriptionBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityWebSeriesDescriptionBinding.inflate(inflater);
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

    private void intentValues() {
        // layoutType = getIntent().getIntExtra(AppLevelConstants.LAYOUT_TYPE, 0);
        layoutType = AppLevelConstants.Rail3;
        if (getIntent().getExtras() != null) {
            railData = getIntent().getExtras().getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
         //   AllChannelManager.getInstance().setRailCommonData(railData);
            if (railData != null) {
                asset = railData.getObject();
                getDatafromBack();
            }
        }

        getBinding().ivPlayIcon.setClickable(true);
        getBinding().ivPlayIcon.setOnClickListener(view -> {

            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                if(commonData!=null) {
                    callProgressBar();
                    playerChecks(commonData);
                }
            }else {
                DialogHelper.showLoginDialog(WebSeriesDescriptionActivity.this);
            }

        });



    }

    private void getDatafromBack() {
        getBinding().setMovieAssestModel(asset);
        map = asset.getTags();
        yearMap = asset.getMetas();
        assetId = asset.getId();
        getRefId();
        Constants.id = asset.getId();
        Constants.assetType = asset.getType();
        Constants.assetId = (int) Constants.id;
        getMovieCasts();
        getMovieCrews();

        StringBuilderHolder.getInstance().clear();
        setMetas();

        setBannerImage(assetId);
        setHungamaTag(asset);
    }

    private void setHungamaTag(Asset asset) {
        boolean isProviderAvailable = AssetContent.getHungamaTag(asset.getTags());
        if (isProviderAvailable){
            getBinding().hungama.setVisibility(View.VISIBLE);
        }else {
            getBinding().hungama.setVisibility(View.GONE);
        }
    }

    private void setMetas() {
        viewModel.getGenreLivedata(map).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {


                if (!TextUtils.isEmpty(s)) {
                    StringBuilderHolder.getInstance().append(s.trim());
                    StringBuilderHolder.getInstance().append(" | ");
                }

                getLanguage();
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

                getDuration();

                getMovieYear();

                getMovieRating();


                String value = StringBuilderHolder.getInstance().getText().toString();
                if (value.length() > 0) {
                    value = StringBuilderHolder.getInstance().getText().substring(0, value.length() - 2);
                }
                getBinding().tvShortDescription.setText(value);
            }
        });
    }

    private void getRefId() {
        viewModel.getRefIdLivedata(map).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String ref_id) {
                if (!TextUtils.isEmpty(ref_id)) {
                    PrintLogging.printLog(this.getClass(), "", "refIdPrint" + ref_id);
                }
            }
        });

    }

    private void getMovieYear() {
        PrintLogging.printLog(this.getClass(), "", yearMap + "djsfkjdsfnkdsnfkds");
        if (yearMap != null) {
            doubleValue = (DoubleValue) yearMap.get(AppLevelConstants.YEAR);
        }
        if (doubleValue != null) {
            String s = String.valueOf(doubleValue.getValue());

            StringBuilderHolder.getInstance().append(s.substring(0, 4));
            StringBuilderHolder.getInstance().append(" | ");

        }

    }

    private void getMovieRating() {

        if (AssetContent.getParentalRating(map).length() > 0) {
            StringBuilderHolder.getInstance().append(AssetContent.getParentalRating(map));
            StringBuilderHolder.getInstance().append("| ");
//            getBinding().parentalRating.setText(StringBuilderHolder.getInstance().getText());
        }
    }

    private void setBannerImage(long assetId) {
        AppCommonMethods.setImages(railData, getApplicationContext(), getBinding().webseriesimage);
        getBinding().backImg.setOnClickListener(view -> onBackPressed());
    }

    private void getMovieCrews() {
        viewModel.getCrewLiveDAta(map).observe(this, crewText -> {

            if (TextUtils.isEmpty(crewText)) {
                getBinding().crewLay.setVisibility(View.GONE);
            } else {
                getBinding().crewLay.setVisibility(View.VISIBLE);
                getBinding().setCrewValue(" " + crewText.trim());
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

    @Override
    protected void onResume() {
        super.onResume();
        if (KsPreferenceKey.getInstance(this).getUserActive()) {
            if (NetworkConnectivity.isOnline(this)) {
                isActive = true;
                callwatchlistApi();
                if (isParentalLocked)
                    assetRuleErrorCode = AppLevelConstants.PARENTAL_BLOCK;
                //  callwatchlistApi();

            }
        }
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(WebSeriesDescriptionViewModel.class);

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
            getBinding().includeProgressbar.progressBar.setOnClickListener(view1 -> {

            });
            getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
            modelCall();
            intentValues();
            setExpandable();
            getBinding().shareWith.setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < AppLevelConstants.SHARE_DIALOG_DELAY) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                openShareDialouge();
            });

            getBinding().textwatchlist.setOnClickListener(new View.OnClickListener() {



                final boolean isActive = KsPreferenceKey.getInstance(getApplicationContext()).getUserActive();

                @Override
                public void onClick(View view) {

                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                        return;
                    }
                    lastClickTime = SystemClock.elapsedRealtime();

                    if (NetworkConnectivity.isOnline(getApplication())) {
                        if (isActive) {
                            if (isAdded) {
                                viewModel.deleteWatchlist(assetId).observe(WebSeriesDescriptionActivity.this, new Observer<CommonResponse>() {
                                    @Override
                                    public void onChanged(@Nullable CommonResponse aBoolean) {

                                        if (aBoolean != null) {
                                            PrintLogging.printLog(this.getClass(), "", "deleteDevice m" + aBoolean);
                                            if (aBoolean.getStatus()) {
                                                removeFromFollow(getString(R.string.removed_from_followlist));
                                                // new ToastHandler(WebSeriesDescriptionActivity.this).show(getApplicationContext().getResources().getString(R.string.removed_from_watchlist));
                                                isAdded = false;
                                                getBinding().textwatchlist.setText(getResources().getString(R.string.follow));
                                                getBinding().textwatchlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.outline_subscriptions_24px), null, null);
                                                getBinding().textwatchlist.setTextColor(getResources().getColor(R.color.white));
                                            } else {
                                                if (aBoolean.getErrorCode().equals("")) {
                                                    showDialog(aBoolean.getMessage());
                                                } else {
                                                    if (aBoolean.getErrorCode().equals(AppLevelConstants.ALREADY_UNFOLLOW_ERROR)) {

                                                        isAdded = false;

                                                        getBinding().textwatchlist.setText(getResources().getString(R.string.follow));
                                                        getBinding().textwatchlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.outline_subscriptions_24px), null, null);
                                                        getBinding().textwatchlist.setTextColor(getResources().getColor(R.color.white));
                                                    } else {
                                                        showDialog(aBoolean.getMessage());
                                                    }

                                                }
                                            }
                                        }
                                    }
                                });
                            } else {
                                addToWatchlist();
                            }
                        } else {
                            iconClicked = true;
                            new ActivityLauncher(WebSeriesDescriptionActivity.this).loginActivity(WebSeriesDescriptionActivity.this, LoginActivity.class, 0, "");
                        }
                    } else {
                        ToastHandler.show(getResources().getString(R.string.no_internet_connection), WebSeriesDescriptionActivity.this);

                    }
                }
            });
            setRailBaseFragment();

            //  loadDataFromModel();

        } else {
            noConnectionLayout();
        }
    }

    private void setRailBaseFragment() {
        FragmentManager fm = getSupportFragmentManager();
        DetailRailFragment detailRailFragment = new DetailRailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        detailRailFragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.rail_fragment, detailRailFragment).commit();
    }

    private void removeFromFollow(String msg) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getString(R.string.dialog), msg, getString(R.string.ok));
        alertDialog.setAlertDialogCallBack(alertDialog::dismiss);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);

    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    private void setUIComponets(List<AssetCommonBean> assetCommonBeans, int counter, int type) {

        try {
            if (!episodeTested) {
                checkEpisode(assetCommonBeans);
            }
            if (adapter != null) {
                if (type > 0) {
                    loadedList.add(assetCommonBeans.get(0));
                    adapter.notifyItemChanged(counter + tempCount);
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

    private void checkEpisode(final List<AssetCommonBean> assetCommonBeanList) {
        episodeTested = true;
        if (assetCommonBeanList.size() > 0) {
            int type = assetCommonBeanList.get(0).getRailAssetList().get(0).getType();
            if (type == MediaTypeConstant.getWebEpisode(WebSeriesDescriptionActivity.this)) {
                PrintLogging.printLog(this.getClass(), "type", "");
            }
        }


       // Handler mHandler = new Handler();
        RailCommonData railCommonData = assetCommonBeanList.get(0).getRailAssetList().get(0);
//        mHandler.postDelayed(() -> {
//            getBinding().ivPlayIcon.setClickable(true);
//            getBinding().ivPlayIcon.setOnClickListener(view -> {
//                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
//                    return;
//                }
//                lastClickTime = SystemClock.elapsedRealtime();
//                if (NetworkConnectivity.isOnline(WebSeriesDescriptionActivity.this)) {
//                    checkErrors(railCommonData);
//                } else {
//                    ToastHandler.show(getResources().getString(R.string.no_internet_connection), WebSeriesDescriptionActivity.this);
//                }
//            });
//
//        }, 2000);

        getBinding().includeProgressbar.progressBar.setOnClickListener(view1 -> {

        });
        getBinding().ivPlayIcon.setClickable(true);
        getBinding().ivPlayIcon.setOnClickListener(view -> {


            boolean isTablet = this.getResources().getBoolean(R.bool.isTablet);
            if(isTablet){
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                    callProgressBar();
                    playerChecks(railCommonData);

                }else {
                    DialogHelper.showLoginDialog(WebSeriesDescriptionActivity.this);
                }



            }else {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                    callProgressBar();
                    playerChecks(railCommonData);
                }else {
                    new ActivityLauncher(WebSeriesDescriptionActivity.this).loginActivity(WebSeriesDescriptionActivity.this, LoginActivity.class, 0, "");
                }


            }


        });


        AssetContent.getVideoResolution(asset.getTags()).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String videoResolution) {
                if (videoResolution.equals(AppConstants.HD)) {
                    fileId = AppCommonMethods.getFileIdOfAssest(railCommonData.getObject(), AppConstants.HD);
                } else {
                    fileId = AppCommonMethods.getFileIdOfAssest(railCommonData.getObject(), AppConstants.SD);
                }
            }
        });
       // playerChecks(railCommonData);
    }

    private void checkErrors(RailCommonData railCommonData) {
        if (playerChecksCompleted) {
            if (assetRuleErrorCode == AppLevelConstants.GEO_LOCATION_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeforGeoLocation(1, WebSeriesDescriptionActivity.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.FOR_PURCHASED_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(WebSeriesDescriptionActivity.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.USER_ACTIVE_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(WebSeriesDescriptionActivity.this));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.NO_MEDIA_FILE) {
                showDialog(getString(R.string.no_media_file));
                callProgressBar();
            }
//            else if (assetRuleErrorCode == AppLevelConstants.PARENTAL_BLOCK) {
//                isParentalLocked = true;
//                if (KsPreferenceKey.getInstance(this).getUserActive())
//                    validateParentalPin(railCommonData);
//                else
//                    startPlayer(railCommonData);
//            }
//            else if (errorCode == AppLevelConstants.NO_ERROR && (assetRuleErrorCode == AppLevelConstants.NO_ERROR || assetRuleErrorCode == -1)) {
//                if (KsPreferenceKey.getInstance(this).getUserActive())
//                    checkOnlyDevice(railCommonData);
//                else {
//                    startPlayer(railCommonData);
//                }
//            }
            else if (errorCode == AppLevelConstants.NO_ERROR) {
                if (KsPreferenceKey.getInstance(this).getUserActive()) {
                    parentalCheck(railCommonData);
                } else {
                    startPlayer(railCommonData);
                }
            }
        }else {
            callProgressBar();
            DialogHelper.showAlertDialog(this, getString(R.string.play_check_message), getString(R.string.ok), this);
        }
    }

    private void parentalCheck(RailCommonData railCommonData) {
        if (KsPreferenceKey.getInstance(this).getUserActive()) {
            if (KsPreferenceKey.getInstance(this).getParentalActive()) {
                ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(getApplicationContext());
                defaultParentalRating = responseDmsModel.getParams().getDefaultParentalLevel();
                userSelectedParentalRating = KsPreferenceKey.getInstance(getApplicationContext()).getUserSelectedRating();
                if (!userSelectedParentalRating.equalsIgnoreCase("")) {
                    assetKey = AssetContent.getAssetKey(asset.getTags(),userSelectedParentalRating, getApplicationContext());
                    if(assetKey){
                        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                        checkOnlyDevice(railCommonData);
                    }else {
                        validateParentalPin(railCommonData);
                    }

                } else {
                    assetKey = AssetContent.getAssetKey(asset.getTags(),defaultParentalRating, getApplicationContext());
                    if(assetKey){
                        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                        checkOnlyDevice(railCommonData);
                    }else {
                        validateParentalPin(railCommonData);
                    }
                }
            } else {
                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                checkOnlyDevice(railCommonData);
            }
        }
    }

    private void startPlayer(RailCommonData railCommonData) {
        callProgressBar();
        Intent intent = new Intent(WebSeriesDescriptionActivity.this, PlayerActivity.class);
        intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railCommonData);
        startActivity(intent);
    }

    private void validateParentalPin(RailCommonData railCommonData) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.showValidatePinDialog(WebSeriesDescriptionActivity.this, null, "WEBSERIES", new ParentalDialogCallbacks() {
                    @Override
                    public void onPositiveClick(String pinText) {
                        ParentalControlViewModel parentalViewModel = ViewModelProviders.of(WebSeriesDescriptionActivity.this).get(ParentalControlViewModel.class);

                        parentalViewModel.validatePin(WebSeriesDescriptionActivity.this, pinText).observe(WebSeriesDescriptionActivity.this, commonResponse -> {
                            if (commonResponse.getStatus()) {
                                DialogHelper.hideValidatePinDialog();
                                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                                playerChecksCompleted = true;
                                // checkErrors(railCommonData);
                                checkOnlyDevice(railCommonData);
                            } else {
                                Toast.makeText(WebSeriesDescriptionActivity.this, getString(R.string.incorrect_parental_pin), Toast.LENGTH_LONG).show();
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
        new HouseHoldCheck().checkHouseholdDevice(WebSeriesDescriptionActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> {
                        Log.d("CheckOnlyDevice","True");
                        startPlayer(railData);
                    });
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(WebSeriesDescriptionActivity.this).refreshKS(response -> checkDevice(railData));
                    }else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }
                }
            }
        });
    }

    private void loadDataFromModel() {
        if (asset.getType() == MediaTypeConstant.getDrama(WebSeriesDescriptionActivity.this)) {
            viewModel.getChannelList(AppLevelConstants.TAB_DRAMA_DETAILS).observe(this, assetCommonBean -> {
                if (assetCommonBean != null && assetCommonBean.getStatus()) {
                    dtChannelsList = assetCommonBean.getDTChannelList();
                    clipList = new ArrayList<>();
                    if (Constants.assetType == MediaTypeConstant.getClip()) {
                        viewModel.getClipData(Constants.assetId, Constants.counter, Constants.assetType, map, layoutType, asset.getType()).observe(this, assetCommonBeans -> clipList = assetCommonBeans);
                    }
                    viewModel.getSeasonsListData(Constants.assetId, Constants.counter, Constants.assetType, map, layoutType, asset.getType()).observe(this, integers -> {
                        if (integers != null && integers.size() > 0) {
                            seriesNumberList = integers;
                            callSeasonEpisodes(seriesNumberList);
                        } else {
                            episodeTested = true;
                            callCategoryRailAPI(dtChannelsList);
                        }

                    });
                } else {
                    viewModel.getSeasonsListData(Constants.assetId, Constants.counter, Constants.assetType, map, layoutType, asset.getType()).observe(this, integers -> {
                        if (integers != null && integers.size() > 0) {
                            seriesNumberList = integers;
                            callSeasonEpisodes(seriesNumberList);
                        } else {
                            callCategoryRailAPI(dtChannelsList);
                        }

                    });
                }
            });
        }
    }

    private void callSeasonEpisodes(List<Integer> seriesNumberList) {
        if (seasonCounter != seriesNumberList.size()) {
            viewModel.callSeasonEpisodes(map, Constants.assetType, 1, seriesNumberList, seasonCounter, layoutType).observe(this, assetCommonBeans -> {
                if (assetCommonBeans != null && assetCommonBeans.get(0).getStatus()) {
                    getBinding().myRecyclerView.setVisibility(View.VISIBLE);
                    setUIComponets(assetCommonBeans, tempCount, 0);
                    tempCount++;
                    seasonCounter++;
                    if (!episodeTested) {
                        checkEpisode(assetCommonBeans);
                    }
                    callSeasonEpisodes(seriesNumberList);
                } else {
                    callCategoryRailAPI(dtChannelsList);
                }
            });
        } else {
            tempCount--;
            callCategoryRailAPI(dtChannelsList);
        }

    }

    private void callCategoryRailAPI(List<VIUChannel> list) {
        if (dtChannelsList != null) {
            if (dtChannelsList.size() > 0) {
                channelList = list;
                if (counter != channelList.size() && counter < channelList.size()) {
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

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

    @Override
    protected void onStart() {
        super.onStart();
        if (KsPreferenceKey.getInstance(this).getUserActive()) {
            if (NetworkConnectivity.isOnline(this)) {
                isActive = true;
                callwatchlistApi();
            }
        }
    }

    private void callwatchlistApi() {

        viewModel.listAllSeriesList(assetId).observe(this, s -> {
            if (TextUtils.isEmpty(s)) {
                if (iconClicked) {
                    addToWatchlist();
                    iconClicked = false;
                }
                isAdded = false;

                getBinding().textwatchlist.setText(getResources().getString(R.string.follow));
                getBinding().textwatchlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.outline_subscriptions_24px), null, null);
                getBinding().textwatchlist.setTextColor(getResources().getColor(R.color.white));

            } else {
                idfromAssetWatchlist = s;
                isAdded = true;
                getBinding().textwatchlist.setText(getResources().getString(R.string.following));
                getBinding().textwatchlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_subscriptions_24px), null, null);
                getBinding().textwatchlist.setTextColor(getResources().getColor(R.color.primary_blue));
            }
        });
    }

    private void addToWatchlist() {
        viewModel.addToFollowlist(assetId).observe(this, s -> {

            if (s != null) {

                if (s.getStatus()) {
                    addToFollow(getString(R.string.added_to_followlist));
                    idfromAssetWatchlist = s.getAssetID();
                    isAdded = true;

                    getBinding().textwatchlist.setText(getResources().getString(R.string.following));
                    getBinding().textwatchlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_subscriptions_24px), null, null);
                    getBinding().textwatchlist.setTextColor(getResources().getColor(R.color.primary_blue));

                } else {
                    if (s.getErrorCode().equals("")) {
                        showDialog(s.getMessage());
                    } else {
                        switch (s.getErrorCode()) {
                            case AppLevelConstants.ALREADY_FOLLOW_ERROR:
                                addToFollow(getString(R.string.already_added_in_follow));
                                idfromAssetWatchlist = String.valueOf(assetId);
                                isAdded = true;

                                getBinding().textwatchlist.setText(getResources().getString(R.string.following));
                                getBinding().textwatchlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.baseline_subscriptions_24px), null, null);
                                getBinding().textwatchlist.setTextColor(getResources().getColor(R.color.primary_blue));


                                break;
                            case AppLevelConstants.ALREADY_UNFOLLOW_ERROR:

                                isAdded = false;
                                getBinding().textwatchlist.setText(getResources().getString(R.string.follow));
                                getBinding().textwatchlist.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.drawable.outline_subscriptions_24px), null, null);
                                getBinding().textwatchlist.setTextColor(getResources().getColor(R.color.white));

                                break;
                            default:
                                showDialog(s.getMessage());
                                break;
                        }

                    }
                }

            }
        });
    }

    private void addToFollow(String msg) {


        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getString(R.string.dialog), msg, getString(R.string.ok));
        alertDialog.setAlertDialogCallBack(alertDialog::dismiss);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);

    }


    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

    }

    private void getDuration() {
        String duraton = AppCommonMethods.getURLDuration(asset);

        if (!TextUtils.isEmpty(duraton)) {
            StringBuilderHolder.getInstance().append(duraton);
            StringBuilderHolder.getInstance().append(" | ");
        }
    }


    private void showDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }


    private void showAlertDialog(String msg) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance(getResources().getString(R.string.dialog), msg, getResources().getString(R.string.login), getResources().getString(R.string.cancel));
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }


    @Override
    public void onFinishDialog() {
        if (isPurchased) {
            isPurchased = false;
            new ActivityLauncher(WebSeriesDescriptionActivity.this).loginActivity(WebSeriesDescriptionActivity.this, LoginActivity.class, 0, "");
        }
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void playerChecks(final RailCommonData railCommonData) {
        new GeoBlockingCheck().aseetAvailableOrNot(WebSeriesDescriptionActivity.this, railCommonData.getObject(), (status, response, totalCount,errorcode,message) -> {
            if (status) {
                if (totalCount != 0) {
                    checkBlockingErrors(response, railCommonData);
                } else {
                    checkEntitleMent(railCommonData);
                }
            }else {
                callProgressBar();
                showDialog(message);
            }
        });
    }

    private void checkBlockingErrors(Response<ListResponse<UserAssetRule>> response, RailCommonData railCommonData) {
        if (response != null && response.results != null && response.results.getObjects() != null) {
            for (UserAssetRule userAssetRule :
                    response.results.getObjects()) {
                switch (userAssetRule.getRuleType()) {
                    case GEO:
                        assetRuleErrorCode = AppLevelConstants.GEO_LOCATION_ERROR;
                        playerChecksCompleted = true;
                        checkErrors(railCommonData);
                        return;
//                    case PARENTAL:
//                        assetRuleErrorCode = AppLevelConstants.PARENTAL_BLOCK;
//                        checkEntitleMent(railCommonData);
//                        break;
                    default:
                        checkEntitleMent(railCommonData);
                        break;
                }
            }
        }
    }

    private void checkEntitleMent(final RailCommonData railCommonData) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            AssetContent.getVideoResolution(asset.getTags()).observe(WebSeriesDescriptionActivity.this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String videoResolution) {
                String fileId = "";
                if (videoResolution.equals(AppConstants.HD)) {
                    fileId = AppCommonMethods.getFileIdOfAssest(railCommonData.getObject(), AppConstants.HD);
                    AllChannelManager.getInstance().setChannelId(fileId);

                } else {
                    fileId = AppCommonMethods.getFileIdOfAssest(railCommonData.getObject(), AppConstants.SD);
                    AllChannelManager.getInstance().setChannelId(fileId);

                }
                if (fileId.equals("")) {
                    playerChecksCompleted = true;
                    errorCode = AppLevelConstants.NO_MEDIA_FILE;
                    checkErrors(railCommonData);
                } else {
                    new EntitlementCheck().checkAssetType(WebSeriesDescriptionActivity.this, fileId, (status, response, purchaseKey,errorCode1, message) -> {
                        if (status) {
                            playerChecksCompleted = true;
                            if (purchaseKey.equalsIgnoreCase(getResources().getString(R.string.FOR_PURCHASE_SUBSCRIPTION_ONLY)) || purchaseKey.equals(getResources().getString(R.string.FREE))) {
                                errorCode = AppLevelConstants.NO_ERROR;
                                checkErrors(railCommonData);
                            } else if (purchaseKey.equalsIgnoreCase(getResources().getString(R.string.FOR_PURCHASED))) {
                                if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                                     isDtvAccountAdded(railCommonData);
                                    //check Dtv Account Added or Not

                                } else {
                                    errorCode = AppLevelConstants.FOR_PURCHASED_ERROR;
                                    checkErrors(railCommonData);
                                }
                            } else {
                                if (KsPreferenceKey.getInstance(getApplicationContext()).getUserActive()) {
                                    isDtvAccountAdded(railCommonData);
                                    //check Dtv Account Added or Not
                                } else {
                                    errorCode = AppLevelConstants.USER_ACTIVE_ERROR;
                                    checkErrors(railCommonData);
                                    //not play
                                }
                            }
                        }else {
                            callProgressBar();
                            if (message!="")
                                showDialog(message);
                        }
                    });
                }
               // playerChecksCompleted = true;
            }
        });
            }
        });


    }

    private void isDtvAccountAdded(RailCommonData railCommonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewModel.getDtvAccountList().observe(WebSeriesDescriptionActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String dtvAccount) {
                        try {
                            if (dtvAccount != null) {
                                if (dtvAccount.equalsIgnoreCase("0")) {
                                    isDtvAdded = false;
                                    callProgressBar();
                                    checkForSubscription(isDtvAdded,railCommonData);

                                } else if (dtvAccount.equalsIgnoreCase("")) {
                                    isDtvAdded = false;
                                    callProgressBar();
                                    checkForSubscription(isDtvAdded,railCommonData);
                                } else {
                                    isDtvAdded = true;
                                    callProgressBar();
                                    checkForSubscription(isDtvAdded,railCommonData);
                                }

                            } else {
                                // Api Failure Error
                                callProgressBar();
                                showDialog(getString(R.string.something_went_wrong_try_again));
                            }
                        }catch (Exception e){
                            Log.e("ExceptionIs",e.toString());
                        }
                    }
                });

            }
        });
    }

    private void checkForSubscription(boolean isDtvAdded, RailCommonData railCommonData) {
        //***** Mobile + Non-Dialog + Non-DTV *************//
        if(KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.NON_DIALOG) && isDtvAdded==false){
            runOnUiThread(() ->DialogHelper.openDialougeFornonDialog(WebSeriesDescriptionActivity.this,false));
        }
        //********** Mobile + Non-Dialog + DTV ******************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.NON_DIALOG) && isDtvAdded==true){
            runOnUiThread(() ->DialogHelper.openDialougeFornonDialog(WebSeriesDescriptionActivity.this,false));
        }
        //*********** Mobile + Dialog + Non-DTV *****************//
        else if(KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded==false){
            if(AssetContent.isPurchaseAllowed(railCommonData.getObject().getMetas(), railCommonData.getObject(),WebSeriesDescriptionActivity.this)){
                runOnUiThread(() ->DialogHelper.openDialougeForDtvAccount(WebSeriesDescriptionActivity.this, true,false));
            }else {
                runOnUiThread(() ->DialogHelper.openDialougeForDtvAccount(WebSeriesDescriptionActivity.this, false,false));
            }
        }
        //************ Mobile + Dialog + DTV ********************//
        else if (KsPreferenceKey.getInstance(getApplicationContext()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded==true){
            if(AssetContent.isPurchaseAllowed(railCommonData.getObject().getMetas(), railCommonData.getObject(),WebSeriesDescriptionActivity.this)){
                runOnUiThread(() ->DialogHelper.openDialougeForDtvAccount(WebSeriesDescriptionActivity.this, true,false));
            }else {
                runOnUiThread(() ->DialogHelper.openDialougeForDtvAccount(WebSeriesDescriptionActivity.this, false,false));
            }
        }else {
            showDialog(getString(R.string.something_went_wrong_try_again));
        }
    }


    private void checkDevice(final RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(WebSeriesDescriptionActivity.this, commonResponse -> {
//                PrintLogging.printLog("", "errorCodeFormResponseIs" + commonResponse.getStatus());

            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> checkEntitleMent(railData));

                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(WebSeriesDescriptionActivity.this).refreshKS(response -> checkDevice(railData));
                    }else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }

                }

            }

        });

    }

    @Override
    public void moreLikeThisClicked(RailCommonData railCommonData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            }
        });

        if(railCommonData!=null && railCommonData.getObject()!=null) {


            this.commonData = railCommonData;


            Log.d("asadadfr", railCommonData.getObject().getId().toString());
//            Handler mHandler = new Handler();
//            mHandler.postDelayed(() -> {
//                getBinding().
//                Icon.setClickable(true);
//                getBinding().ivPlayIcon.setOnClickListener(view -> {
//                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
//                        return;
//                    }
//                    lastClickTime = SystemClock.elapsedRealtime();
//                    if (NetworkConnectivity.isOnline(WebSeriesDescriptionActivity.this)) {
//                        checkErrors(railCommonData);
//                    } else {
//                        ToastHandler.show(getResources().getString(R.string.no_internet_connection), WebSeriesDescriptionActivity.this);
//                    }
//                });
//            }, 2000);

            AssetContent.getVideoResolution(railCommonData.getObject().getTags()).observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String videoResolution) {
                    if (videoResolution.equals(AppConstants.HD)) {
                        fileId = AppCommonMethods.getFileIdOfAssest(railCommonData.getObject(), AppConstants.HD);
                    } else {
                        fileId = AppCommonMethods.getFileIdOfAssest(railCommonData.getObject(), AppConstants.SD);
                    }
                }
            });
           // playerChecks(railCommonData);
        }

    }

    private void callProgressBar(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(getBinding().includeProgressbar.progressBar.getVisibility() == View.VISIBLE){
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                } else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
                }

            }
        });
    }
}


