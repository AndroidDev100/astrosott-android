package com.astro.sott.activities.moreListing.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.activities.moreListing.filter.ListingFilterActivity;
import com.astro.sott.activities.myList.viewModel.MyWatchlistViewModel;
import com.astro.sott.activities.search.ui.ActivitySearch;
import com.astro.sott.activities.search.ui.SearchKeywordActivity;
import com.astro.sott.adapter.experiencemng.CommonLandscapeListingAdapteNew;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.ActivityCustomListingBinding;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.GridSpacingItemDecoration;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;


import java.util.ArrayList;

public class CustomListingActivity extends BaseBindingActivity<ActivityCustomListingBinding> implements DetailRailClick {
    private final ArrayList<RailCommonData> arrayList = new ArrayList<>();
    private VIUChannel category;
    private boolean tabletSize;
    int spacing;
    private boolean mIsLoading = true;
    private boolean isScrolling = false;
    private int count = 1, totalCOunt = 0, counter = 1;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, firstVisiblePosition;
    int spanCount;
    private long lastClickTime = 0;
    private MyWatchlistViewModel viewModel;
    private AssetCommonBean assetCommonBean;
    private String customMediaType = "", customRailType = "", customGenre = "", customGenreRule = "", customDays = "", customLinearAssetId = "", title = "";

    @Override
    protected ActivityCustomListingBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityCustomListingBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assetCommonBean = getIntent().getExtras().getParcelable("assetCommonBean");
        category = getIntent().getExtras().getParcelable("baseCategory");
        getBinding().toolbar.ivfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isApiCalling){
                    if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                        return;
                    }
                    lastClickTime = SystemClock.elapsedRealtime();
                    Intent intent = new Intent(CustomListingActivity.this, ListingFilterActivity.class);
                    startActivity(intent);
                }

            }
        });
        if (assetCommonBean != null) {
            title = assetCommonBean.getTitle();
            if (assetCommonBean.getCustomGenre() != null)
                customGenre = assetCommonBean.getCustomGenre();
            if (assetCommonBean.getCustomGenreRule() != null)
                customGenreRule = assetCommonBean.getCustomGenreRule();
            if (assetCommonBean.getCustomMediaType() != null)
                customMediaType = assetCommonBean.getCustomMediaType();
            if (assetCommonBean.getCustomRailType() != null)
                customRailType = assetCommonBean.getCustomRailType();
            if (assetCommonBean.getCustomLinearAssetId() != null)
                customLinearAssetId = assetCommonBean.getCustomLinearAssetId();
            if (assetCommonBean.getCustomDays() != null)
                customDays = assetCommonBean.getCustomDays();
        }
        FirebaseEventManager.getFirebaseInstance(CustomListingActivity.this).trackScreenName(title + " Listing");
        try {
            AppCommonMethods.resetFilter(CustomListingActivity.this);
        }catch (Exception ignored){

        }
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
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            modelCall();
            UIinitialization();
            loadData();

        } else {
            noConnectionLayout();
        }


    }

    boolean isApiCalling=false;
    private void loadData() {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        getBinding().noDataLayout.setVisibility(View.GONE);
        getBinding().recyclerViewMore.setVisibility(View.VISIBLE);
        if (customRailType.equalsIgnoreCase(AppLevelConstants.TRENDING)) {
            //need to apply filter-->>icon will always show
            getBinding().toolbar.ivfilter.setVisibility(View.VISIBLE);
            isApiCalling=true;
            getTrendingListing();
        } else if (customRailType.equalsIgnoreCase(AppLevelConstants.PPV_RAIL)) {
            getBinding().toolbar.ivfilter.setVisibility(View.GONE);
            getPPVLiSTING();
        } else if (customRailType.equalsIgnoreCase(AppLevelConstants.LIVECHANNEL_RAIL)) {
            //need to apply filter-->>icon will always show
            getBinding().toolbar.ivfilter.setVisibility(View.VISIBLE);
            isApiCalling=true;
            getEpgListing();
        }
    }

    private void getTrendingListing() {
        viewModel.getTrendingListing(customMediaType, customGenre, customGenreRule, counter).observe(this, assetListResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            isApiCalling=false;
            if (assetListResponse != null && assetListResponse.size() > 0) {
                totalCOunt = assetListResponse.get(0).getTotalCount();
                arrayList.addAll(assetListResponse);
                setUiComponent();
            }else {
                if (counter == 1) {
                    getBinding().recyclerViewMore.setVisibility(View.GONE);
                    getBinding().noDataLayout.setVisibility(View.VISIBLE);
                    getBinding().noData.retryTxt.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getEpgListing() {

        viewModel.getEpgListing(customDays, customLinearAssetId, counter).observe(this, assetListResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            isApiCalling=false;
            if (assetListResponse != null && assetListResponse.size() > 0) {
                totalCOunt = assetListResponse.get(0).getTotalCount();
                arrayList.addAll(assetListResponse);
                setUiComponent();
            }else {
                if (counter == 1) {
                    getBinding().recyclerViewMore.setVisibility(View.GONE);
                    getBinding().noDataLayout.setVisibility(View.VISIBLE);
                    getBinding().noData.retryTxt.setVisibility(View.GONE);
                }
            }
        });

    }

    private void getPPVLiSTING() {
        viewModel.getPurchaseListing(customMediaType, customGenre, customGenreRule, counter).observe(this, assetListResponse -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (assetListResponse != null && assetListResponse.size() > 0) {
                totalCOunt = assetListResponse.get(0).getTotalCount();
                arrayList.addAll(assetListResponse);
                setUiComponent();
            }
        });
    }

    private void setPagination() {


        getBinding().recyclerViewMore.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                try {

                    LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                    if (layoutManager != null)
                        firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    if (dy > 0 && layoutManager != null) {
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                        if (mIsLoading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                mIsLoading = false;
                                counter++;
                                isScrolling = true;
                                loadData();

                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });
    }

    private CommonLandscapeListingAdapteNew commonLandscapeListingAdapteNew;

    private void setUiComponent() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (commonLandscapeListingAdapteNew == null) {
                    commonLandscapeListingAdapteNew = new CommonLandscapeListingAdapteNew(CustomListingActivity.this, arrayList, AppConstants.Rail5, assetCommonBean.getTitle(), category.getCategory());
                    getBinding().recyclerViewMore.setAdapter(commonLandscapeListingAdapteNew);
                    mIsLoading = commonLandscapeListingAdapteNew.getItemCount() != totalCOunt;

                } else {
                    mIsLoading = commonLandscapeListingAdapteNew.getItemCount() != totalCOunt;
                    commonLandscapeListingAdapteNew.notifyDataSetChanged();
                }
            }
        });

    }

    private void UIinitialization() {

        tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            spanCount = 4;
            Resources r = getResources();
            spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstants.LANSCAPE_SPACING, r.getDisplayMetrics());
        } else {
            spanCount = AppConstants.SPAN_COUNT_LANDSCAPE;
            Resources r = getResources();
            spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstants.LANSCAPE_SPACING, r.getDisplayMetrics());
        }


        getBinding().recyclerViewMore.hasFixedSize();
        getBinding().recyclerViewMore.setNestedScrollingEnabled(false);
        getBinding().recyclerViewMore.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(CustomListingActivity.this, spanCount);
        getBinding().recyclerViewMore.setLayoutManager(gridLayoutManager);
        getBinding().toolbar.tvSearchResultHeader.setText(title);
        getBinding().toolbar.homeIconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setPagination();
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(MyWatchlistViewModel.class);
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (!KsPreferenceKey.getInstance(CustomListingActivity.this).getFilterApply().equalsIgnoreCase("")) {
                if (KsPreferenceKey.getInstance(CustomListingActivity.this).getFilterApply().equalsIgnoreCase("true")) {
                    KsPreferenceKey.getInstance(CustomListingActivity.this).setFilterApply("false");
                    mIsLoading=true;
                    commonLandscapeListingAdapteNew=null;
                    arrayList.clear();
                    counter=1;
                    totalCOunt=0;
                    loadData();
                }
            }
        }catch (Exception ignored){

        }
    }
}