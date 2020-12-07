package com.dialog.dialoggo.activities.moreListing.ui;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.moreListing.viewModel.ContinueWatchingViewModel;
import com.dialog.dialoggo.adapter.CommonContWatchListingAdapter;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.callBacks.commonCallBacks.DetailRailClick;
import com.dialog.dialoggo.databinding.ListingActivityBinding;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.GridSpacingItemDecoration;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;

import java.util.ArrayList;
import java.util.List;

public class ContinueWatchingActivity extends BaseBindingActivity<ListingActivityBinding> implements DetailRailClick {

    int totalCount;
    String title;
    ContinueWatchingViewModel continueWatchingViewModel;
    private int counter = 1;
    private int firstVisiblePosition;
    private int pastVisiblesItems;
    private int visibleItemCount;
    private int continueWatchingRailCount = 0;
    private int continueWatchingCount = -1;

    private int totalItemCount;
    private String layout;
    private boolean mIsLoading = true;
    private boolean isScrolling = false;
    private int mScrollY;
    private CommonContWatchListingAdapter commonLandscapeListingAdapter;
    private boolean tabletSize;
    private List<RailCommonData> list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(getBinding().toolbar.toolbar);
        title = getIntent().getStringExtra("title");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        tabletSize = getResources().getBoolean(R.bool.isTablet);

        Log.e(this.getClass().getSimpleName(), "this");

        modelCall();
        connectionObserver();

        setRecyclerProperty();
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void setRecyclerProperty() {
        int spacing;
        int spanCount;

        if (tabletSize) {
            if (layout.equalsIgnoreCase(AppLevelConstants.TYPE5)) {
                spanCount = 4;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppLevelConstants.LANSCAPE_SPACING, r.getDisplayMetrics());
            } else {
                spanCount = 5;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppLevelConstants.PORTRAIT_SPACING, r.getDisplayMetrics());

            }
        } else {
            if (layout.equalsIgnoreCase(AppLevelConstants.TYPE5)) {
                spanCount = AppLevelConstants.SPAN_COUNT_LANDSCAPE;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppLevelConstants.LANSCAPE_SPACING, r.getDisplayMetrics());
            } else {
                spanCount = AppLevelConstants.SPAN_COUNT_PORTRAIT;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppLevelConstants.PORTRAIT_SPACING, r.getDisplayMetrics());

            }
        }
        getBinding().listRecyclerview.hasFixedSize();
        getBinding().listRecyclerview.setNestedScrollingEnabled(false);
        getBinding().listRecyclerview.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));

        if (tabletSize) {
            getBinding().listRecyclerview.setLayoutManager(new GridLayoutManager(this, 5));
        } else {
            getBinding().listRecyclerview.setLayoutManager(new GridLayoutManager(this, spanCount));
        }
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
//        getBinding().listRecyclerview.setLayoutManager(gridLayoutManager);
    }

    private void modelCall() {
        continueWatchingViewModel = ViewModelProviders.of(this).get(ContinueWatchingViewModel.class);
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            UIinitialization();
            getIntentValue();

        } else {
            noConnectionLayout();
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

    private void UIinitialization() {
        swipeToRefresh();
    }

    private void getIntentValue() {
        list = new ArrayList<>();
        layout = getIntent().getStringExtra("layouttype");

        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        continueWatchingViewModel.getContinueWatching().observe(this, assetCommonBean1 -> {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            sortContinueWatchingRail(getApplicationContext(), assetCommonBean1.getRailAssetList());
            //  list.addAll(assetCommonBean1.getRailAssetList());

        });


    }

    private void sortContinueWatchingRail(Context context, List<RailCommonData> railList) {

        List<Long> longList = AppCommonMethods.getContinueWatchingIDsPreferences(context);
        if (longList != null) {
            if (longList.size() > 0) {
                for (int y = 0; y < longList.size(); y++) {
                    long con_id = longList.get(y);
                    for (int j = 0; j < railList.size(); j++) {
                        if (con_id == railList.get(j).getId()) {
                            list.add(railList.get(j));
                            break;
                        }


                    }
                    commonLandscapeListingAdapter = new CommonContWatchListingAdapter(this, list, AppLevelConstants.Rail5);
                    getBinding().listRecyclerview.setAdapter(commonLandscapeListingAdapter);

                }

            }
        }
    }

    private void swipeToRefresh() {

        /*getBinding().listRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                try {

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

                }
            }
        });*/

    }


    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.tryAgain.setOnClickListener(view -> {
            connectionObserver();
        });
    }

    @Override
    public ListingActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ListingActivityBinding.inflate(inflater);
    }

    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

    }


}
