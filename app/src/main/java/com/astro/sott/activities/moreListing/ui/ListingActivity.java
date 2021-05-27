package com.astro.sott.activities.moreListing.ui;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.activities.moreListing.adapter.PotraitListingAdapter;
import com.astro.sott.activities.moreListing.adapter.SquareListingAdapter;
import com.astro.sott.activities.moreListing.viewModel.ListingViewModel;
import com.astro.sott.adapter.CommonLandscapeListingAdapter;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.ListingActivityBinding;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.GridSpacingItemDecoration;
import com.astro.sott.utils.helpers.NavigationItem;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;

import java.util.ArrayList;
import java.util.List;


public class ListingActivity extends BaseBindingActivity<ListingActivityBinding> implements DetailRailClick {
    List list = new ArrayList<RailCommonData>();
    private int counter = 1, assetId, firstVisiblePosition, pastVisiblesItems, visibleItemCount, totalItemCount;
    private String layout, title;
    private boolean mIsLoading = true, isScrolling = false, isNewIntent = false;
    private int layoutType = 0;
    private PotraitListingAdapter commonPotraitListingAdapter;
    private SquareListingAdapter commonSquareListingAdapter;
    private CommonLandscapeListingAdapter commonLandscapeListingAdapter;
    private ListingViewModel viewModel;
    private BaseCategory baseCategory;
    private AssetCommonBean assetCommonBean;
    private int mScrollY;
    private Intent intent = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNewIntent) {
            callOnCreateInstances(intent);
            setRecyclerProperty();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callOnCreateInstances(Intent intent) {
        Log.e(this.getClass().getSimpleName(), "this==");

        modelCall();
        getIntentValue(intent);
        // setRecyclerProperty();
        connectionObserver();

    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(ListingViewModel.class);

    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            UIinitialization();
            loadDataFromModel();
        } else {
            noConnectionLayout();
        }
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    private void loadDataFromModel() {
        checkTypeOfList();

    }

    private void setUIComponets(List<RailCommonData> railList) {
      /* for (int i=0;i<railList.size();i++){
           list.add(railList.get(i));
       }*/
        list = railList;
        Log.e("layout====>", layout + " " + list.size());
        if (layout.equalsIgnoreCase(AppLevelConstants.TYPE3) || layout.equalsIgnoreCase(AppLevelConstants.TYPE6)) {
            commonPotraitListingAdapter = new PotraitListingAdapter(this, list, layoutType);
            getBinding().listRecyclerview.setAdapter(commonPotraitListingAdapter);
        } else if (layout.equalsIgnoreCase(AppLevelConstants.TYPE4)) {
            commonSquareListingAdapter = new SquareListingAdapter(this, railList, layoutType);
            getBinding().listRecyclerview.setAdapter(commonSquareListingAdapter);
        } else if (layout.equalsIgnoreCase(AppLevelConstants.TYPE5) || layout.equalsIgnoreCase("LDS")) {
            commonLandscapeListingAdapter = new CommonLandscapeListingAdapter(this, railList, layoutType, baseCategory);
            getBinding().listRecyclerview.setAdapter(commonLandscapeListingAdapter);
        } else {
            commonSquareListingAdapter = new SquareListingAdapter(this, railList, layoutType);
            getBinding().listRecyclerview.setAdapter(commonSquareListingAdapter);
        }
        getBinding().listRecyclerview.scrollToPosition(mScrollY);

        if (railList.get(0).getTotalCount() <= 20) {
            mIsLoading = false;
        } else {
            mIsLoading = true;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            //recreate();
            isNewIntent = true;
            counter = 1;
            list.clear();
            isScrolling = false;
            callOnCreateInstances(intent);
        } catch (Exception ignored) {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void UIinitialization() {
        swipeToRefresh();
    }

    private void getIntentValue(Intent intent) {
        if (intent != null) {
            layout = intent.getStringExtra(AppLevelConstants.LAYOUT_TYPE);
            assetCommonBean = intent.getExtras().getParcelable(AppLevelConstants.ASSET_COMMON_BEAN);
            if (assetCommonBean == null) {
                return;
            }
            layoutType = assetCommonBean.getMoreAssetType();
            title = assetCommonBean.getTitle();
            long idAsset = assetCommonBean.getID();
            assetId = (int) idAsset;
        } else {
            layout = getIntent().getStringExtra(AppLevelConstants.LAYOUT_TYPE);
            assetCommonBean = getIntent().getExtras().getParcelable(AppLevelConstants.ASSET_COMMON_BEAN);
            if (assetCommonBean == null) {
                return;
            }
            layoutType = assetCommonBean.getMoreAssetType();
            title = assetCommonBean.getTitle();
            long idAsset = assetCommonBean.getID();
            assetId = (int) idAsset;
        }
        if (getIntent().getExtras().getParcelable("baseCategory") != null)
            baseCategory = getIntent().getExtras().getParcelable("baseCategory");
        FirebaseEventManager.getFirebaseInstance(ListingActivity.this).trackScreenName(title + " Listing");

        setSupportActionBar(getBinding().toolbar.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    private void setRecyclerProperty() {
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        int spanCount, spacing;
        if (layout.equalsIgnoreCase(AppLevelConstants.TYPE5) || layout.equalsIgnoreCase(AppLevelConstants.TYPE7) || layout.equalsIgnoreCase("LDS")) {
            if (tabletSize) {
                spanCount = AppLevelConstants.SPAN_COUNT_LANDSCAPE_TAB;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppLevelConstants.LANSCAPE_SPACING_TAB, r.getDisplayMetrics());
            } else {
                spanCount = AppLevelConstants.SPAN_COUNT_LANDSCAPE;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppLevelConstants.LANSCAPE_SPACING, r.getDisplayMetrics());
            }
        } else {
            if (tabletSize) {
                spanCount = AppLevelConstants.SPAN_COUNT_SQUARE_TAB;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppLevelConstants.PORTRAIT_SPACING_TAB, r.getDisplayMetrics());
            } else {
                spanCount = AppLevelConstants.SPAN_COUNT_PORTRAIT;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppLevelConstants.PORTRAIT_SPACING, r.getDisplayMetrics());
            }
        }
        getBinding().listRecyclerview.hasFixedSize();
        getBinding().listRecyclerview.setNestedScrollingEnabled(false);
        getBinding().listRecyclerview.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ListingActivity.this, spanCount);
        getBinding().listRecyclerview.setLayoutManager(gridLayoutManager);

    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }


    private void swipeToRefresh() {

        getBinding().listRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                try {
                    // getBinding().listRecyclerview.getRecycledViewPool().clear();
                    //.notifyDataSetChanged();

                    GridLayoutManager layoutManager = ((GridLayoutManager) recyclerView.getLayoutManager());
                    firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    if (dy > 0) {
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                        if (mIsLoading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                // PrintLogging.printLog("","slidingValues"+getBinding().listRecyclerview.getAdapter().getItemCount()+" "+counter);
                                int adapterSize = getBinding().listRecyclerview.getAdapter().getItemCount();
                                if (adapterSize > 19) {
                                    mIsLoading = false;
                                    counter++;
                                    isScrolling = true;
                                    mScrollY += dy;
                                    connectionObserver();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            }
        });

    }

    @Override
    public ListingActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ListingActivityBinding.inflate(inflater);
    }

    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

    }

    private void checkTypeOfList() {
        if (assetCommonBean == null) {
            return;
        }
        getBinding().progressBar.setVisibility(View.VISIBLE);
        int type = viewModel.checkMoreType(assetCommonBean);
        if (type == AppLevelConstants.SIMILAR_MOVIES) {
            simmilarMovieListingCall();
        } else if (type == AppLevelConstants.YOU_MAY_LIKE) {
            youMayAlsoLikeListing();
        } else if (type == AppLevelConstants.WEB_EPISODE) {
            webEpisodeListing();
        } else if (type == AppLevelConstants.SPOTLIGHT_EPISODE) {
            spotLightEpisodeListing();
        } else if (type == AppLevelConstants.LIVE_CHANNEL_LIST) {
            liveChannelListing();
        } else if (type == AppLevelConstants.SIMILLAR_CHANNEL_LIST) {
            simillarChannelListing();
        } else {
            categoryListingCall();
        }
    }

    private void simillarChannelListing() {
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 8, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void liveChannelListing() {
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 7, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }


    private void spotLightEpisodeListing() {
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 4, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void webEpisodeListing() {
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 3, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void youMayAlsoLikeListing() {
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 1, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void simmilarMovieListingCall() {
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 2, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void setLayoutType(List<RailCommonData> railCommonData) {
        getBinding().progressBar.setVisibility(View.GONE);
        if (railCommonData != null) {
            if (railCommonData.size() > 0) {
                if (railCommonData.get(0).getStatus()) {
                    if (railCommonData.size() > 0) {

                        if (!isScrolling) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setUIComponets(railCommonData);
                                }
                            });

                        } else {
                            if (layout.equalsIgnoreCase(AppLevelConstants.TYPE3)) {
                                if (commonPotraitListingAdapter.getItemCount() == railCommonData.get(0).getTotalCount()) {
                                    mIsLoading = false;
                                } else {
                                    mIsLoading = true;
                                }
                                list = railCommonData;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        commonPotraitListingAdapter.notifyDataSetChanged();
                                    }
                                });

                            } else if (layout.equalsIgnoreCase(AppLevelConstants.TYPE4)) {
                                if (commonSquareListingAdapter.getItemCount() == railCommonData.get(0).getTotalCount()) {
                                    mIsLoading = false;
                                } else {
                                    mIsLoading = true;
                                }
                                commonSquareListingAdapter.notifyDataSetChanged();
                            } else if (layout.equalsIgnoreCase(AppLevelConstants.TYPE5) || layout.equalsIgnoreCase("LDS")) {
                                if (commonLandscapeListingAdapter.getItemCount() == railCommonData.get(0).getTotalCount()) {
                                    mIsLoading = false;
                                } else {
                                    mIsLoading = true;
                                }
                                commonLandscapeListingAdapter.notifyDataSetChanged();
                            } else {
                                if (commonSquareListingAdapter.getItemCount() == railCommonData.get(0).getTotalCount()) {
                                    mIsLoading = false;
                                } else {
                                    mIsLoading = true;
                                }
                                commonSquareListingAdapter.notifyDataSetChanged();
                            }
                            getBinding().listRecyclerview.scrollToPosition(mScrollY);
                        }
                    }
                }
            } else {
                getBinding().listRecyclerview.setAdapter(null);
            }
        } else {
            getBinding().listRecyclerview.setAdapter(null);
        }
    }

    private void categoryListingCall() {
        viewModel.getLiveData(assetId, counter, layout, isScrolling).observe(this, railCommonData -> setLayoutType(railCommonData));
    }


   /* @Override
    protected void onStart() {
        super.onStart();
        String className=new KsPreferenceKeys(ListingActivity.this).getClassName();
        PrintLogging.printLog("","className--"+className);
        if (className.equalsIgnoreCase("")){
            new ActivityLauncher(ListingActivity.this).portraitListing(ListingActivity.this, ListingActivity.class, type, assetCommonBean);
        }else if (className.equalsIgnoreCase("ListingActivity")){

        }else {
            new ActivityLauncher(ListingActivity.this).portraitListing(ListingActivity.this, ListingActivity.class, type, assetCommonBean);
        }
    }*/
}


