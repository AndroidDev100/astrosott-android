package com.astro.sott.activities.moreListing.ui;

import android.app.Service;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.activities.moreListing.adapter.MoreListingAdapter;
import com.astro.sott.activities.moreListing.viewModel.LandscapeListingViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.RemoveItemClickListner;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.ItemClickListener;
import com.astro.sott.databinding.LandscapeListingActivityBinding;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.GridSpacingItemDecoration;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.ProgressHandler;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.helpers.ToolBarHandler;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GridListingActivity extends BaseBindingActivity<LandscapeListingActivityBinding> implements DetailRailClick, RemoveItemClickListner, ItemClickListener {
    private int counter = 1;
    private int assetId;
    private int firstVisiblePosition;
    private int pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private String layout;
    private boolean mIsLoading = true;
    private boolean isScrolling = false;
    private boolean isShowFilter = false;
    private boolean isSortable = false;

    String title = "";

    private MoreListingAdapter listingAdapter;
    private LandscapeListingViewModel viewModel;
    private AssetCommonBean assetCommonBean;
    private int mScrollY;
    private final ProgressHandler progressDialog = new ProgressHandler();
    private boolean tabletSize;
    String filterValue = "";
    private int pageSize = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        list = new ArrayList<>();
        list.clear();
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        Log.e(this.getClass().getSimpleName(), "this");




      //  new ToolBarHandler(this).landscapeSetAction(getBinding());
      //  new ToolBarHandler(this).setActionListing(getBinding().toolbar, "potrait");

        modelCall();


        getIntentValue();
        setRecyclerProperty();

        connectionObserver();

    }



    private void deepSearchcategoryListingCall() {
        if (NetworkConnectivity.isOnline(GridListingActivity.this)) {
            handleProgressDialog();
            filterValue = "";
            viewModel.getLiveSearchedData(assetId, list, filterValue, counter, layout, isScrolling,pageSize).observe(this, railCommonData -> setLayoutType(railCommonData));
        } else {
            ToastHandler.show(GridListingActivity.this.getResources().getString(R.string.no_internet_connection), getApplicationContext());
        }
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(LandscapeListingViewModel.class);


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

    }

    private void loadDataFromModel() {
        checkTypeOfList();

    }


    private void handleProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getBinding().progressBar.getVisibility()==View.VISIBLE){
                    getBinding().progressBar.setVisibility(View.GONE);
                }else {
                    getBinding().progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void setUIComponets(List<RailCommonData> railList) {

        try {

            PrintLogging.printLog("", "layoutType-->>" + layout);
            mIsLoading = true;

            listingAdapter = new MoreListingAdapter(GridListingActivity.this, railList, layout, GridListingActivity.this);
            getBinding().listRecyclerview.setAdapter(listingAdapter);
          /*
            if (layout.equalsIgnoreCase(AppConstants.TYPE3)) {
                commonPotraitListingAdapter = new CommonPotraitListingAdapter(this, railList, AppConstants.Rail3, assetCommonBean.getTitle());
                getBinding().listRecyclerview.setAdapter(commonPotraitListingAdapter);
            } else if (layout.equalsIgnoreCase(AppConstants.TYPE4)) {
                commonSquareListingAdapter = new CommonSquareListingAdapter(this, railList, AppConstants.Rail4, assetCommonBean.getTitle());
                getBinding().listRecyclerview.setAdapter(commonSquareListingAdapter);
            } else if (layout.equalsIgnoreCase(AppConstants.TYPE2)) {
                Log.e("IMAGE", String.valueOf(railList.size()));
                commonCircleAdapter = new CommonCircleListingAdapter(this, railList, AppConstants.Rail2, assetCommonBean.getTitle());
                getBinding().listRecyclerview.setAdapter(commonCircleAdapter);
            } else if (layout.equalsIgnoreCase(AppConstants.TYPE7)) {
                commonLandscapeListingAdapter = new CommonLandscapeListingAdapter(this, railList, AppConstants.Rail7, assetCommonBean.getTitle());
                getBinding().listRecyclerview.setAdapter(commonLandscapeListingAdapter);
            } else {
                commonLandscapeListingAdapter = new CommonLandscapeListingAdapter(this, railList, AppConstants.Rail5, assetCommonBean.getTitle());
                getBinding().listRecyclerview.setAdapter(commonLandscapeListingAdapter);
            }*/


            /*adapter = new ListingAdapter(this, layout, railList);*/


            getBinding().listRecyclerview.scrollToPosition(mScrollY);
        } catch (Exception e) {
            PrintLogging.printLog("", "ExceptionIs-->>" + e.getMessage());
        }
    }

    private void UIinitialization() {
        swipeToRefresh();


    }

    private void getIntentValue() {
        layout = getIntent().getExtras().getString("layouttype");
        PrintLogging.printLog("", layout + "layout2");
        assetCommonBean = getIntent().getExtras().getParcelable("assetCommonBean");
        PrintLogging.printLog("", "morebuttontype----" + assetCommonBean.getMoreType());
        title = assetCommonBean.getTitle();
        getBinding().toolbar.tvSearchResultHeader.setText(title + "");
        long idAsset = assetCommonBean.getID();
        assetId = (int) idAsset;


        getBinding().toolbar.homeIconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        try {

            VIUChannel category = (VIUChannel) getIntent().getExtras().getParcelable("baseCategory");
            this.isShowFilter = getIntent().getExtras().getBoolean("hasFilter");
            this.isSortable = category.isSortable();
            this.pageSize = category.getMorePageSize();
            if(pageSize<=0)
            {
                pageSize=20;
            }


        } catch (Exception e) {
        }


    }

    private void setRecyclerProperty() {
        int spacing;
        int spanCount;

        if (tabletSize) {

            if (layout.equalsIgnoreCase(AppConstants.TYPE5) || layout.equalsIgnoreCase(AppConstants.TYPE7)) {
                spanCount = 4;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstants.LANSCAPE_SPACING, r.getDisplayMetrics());
            } else {
                spanCount = 5;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstants.PORTRAIT_SPACING, r.getDisplayMetrics());

            }

        } else {

            if (layout.equalsIgnoreCase(AppConstants.TYPE5) || layout.equalsIgnoreCase(AppConstants.TYPE7)) {
                spanCount = AppConstants.SPAN_COUNT_LANDSCAPE;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstants.LANSCAPE_SPACING, r.getDisplayMetrics());
            } else {
                spanCount = AppConstants.SPAN_COUNT_PORTRAIT;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstants.PORTRAIT_SPACING, r.getDisplayMetrics());

            }

        }

        getBinding().listRecyclerview.hasFixedSize();
        getBinding().listRecyclerview.setNestedScrollingEnabled(false);
        getBinding().listRecyclerview.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));
        getBinding().listRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // GridLayoutManager gridLayoutManager = new GridLayoutManager(GridListingActivity.this, spanCount);
        // getBinding().listRecyclerview.setLayoutManager(gridLayoutManager);

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

                    LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                    firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    if (dy > 0) {
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                        if (mIsLoading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                // PrintLogging.printLog("","slidingValues"+getBinding().listRecyclerview.getAdapter().getItemCount()+" "+counter);
                                int adapterSize = getBinding().listRecyclerview.getAdapter().getItemCount();
                                PrintLogging.printLog("", "counterValues-->>" + counter);
                                    mIsLoading = false;
                                    counter++;
                                    PrintLogging.printLog("", counter + "counterMoreLIsting");
                                    isScrolling = true;
                                    mScrollY += dy;
                                    connectionObserver();
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
    public LandscapeListingActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return LandscapeListingActivityBinding.inflate(inflater);
    }

    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

    }

    private void checkTypeOfList() {
        int type = viewModel.checkMoreType(assetCommonBean);
        PrintLogging.printLog("", "moreClicked" + "---" + type + "");
        if (type == AppConstants.SIMILAR_MOVIES) {

            hideToolbarViews();

            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
            //simmilarMovieListingCall();
        } else if (type == AppConstants.YOU_MAY_LIKE) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
           // youMayAlsoLikeListing();
        } else if (type == AppConstants.WEB_EPISODE) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
           // webEpisodeListing();
        } else if (type == AppConstants.SPOTLIGHT_EPISODE) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
           // spotLightEpisodeListing();
        } else if (type == AppConstants.SIMILLAR_UGC_VIDEOS) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
            //simillarUGCVideosListing();
        } else if (type == AppConstants.SIMILLAR_UGC_CREATOR) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
           // simillarUGCCreatorListing();
        } else if (type == AppConstants.LIVE_CHANNEL_LIST) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
           // liveChannelListing();
        } else if (type == AppConstants.SIMILLAR_CHANNEL_LIST) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
          //  simillarChannelListing();
        } else {
            // categoryListingCall();
            showToolbarView();
            deepSearchcategoryListingCall();
        }
    }

    private void showToolbarView() {


    }

    private void hideToolbarViews() {

    }

    /*private void simillarChannelListing() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getRailAssetList().get(0).getObject(), layout, 8, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void liveChannelListing() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getRailAssetList().get(0).getObject(), layout, 7, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));

    }

    private void simillarUGCCreatorListing() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getRailAssetList().get(0).getObject(), layout, 6, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void simillarUGCVideosListing() {

        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getRailAssetList().get(0).getObject(), layout, 5, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void spotLightEpisodeListing() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getRailAssetList().get(0).getObject(), layout, 4, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void webEpisodeListing() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getRailAssetList().get(0).getObject(), layout, 3, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void youMayAlsoLikeListing() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getRailAssetList().get(0).getObject(), layout, 1, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void simmilarMovieListingCall() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getRailAssetList().get(0).getObject(), layout, 2, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }*/

    private void setLayoutType(List<RailCommonData> railCommonData) {
        if (railCommonData != null) {
            if (railCommonData.size() > 0) {
                if (railCommonData.get(0).getStatus()) {
                    if (railCommonData.size() > 0) {
                        if (!isScrolling) {
                            int totalCount = railCommonData.get(0).getTotalCount();
                            getBinding().listRecyclerview.setVisibility(View.VISIBLE);
                            getBinding().noDataLayout.setVisibility(View.GONE);
                            setUIComponets(railCommonData);
                            handleProgressDialog();
                        } else {
                            handleProgressDialog();
                            /*if (layout.equalsIgnoreCase(AppConstants.TYPE3)) {
                                mIsLoading = commonPotraitListingAdapter.getItemCount() != railCommonData.get(0).getTotalCount();
                                commonPotraitListingAdapter.notifyDataSetChanged();
                            } else if (layout.equalsIgnoreCase(AppConstants.TYPE4)) {
                                mIsLoading = commonSquareListingAdapter.getItemCount() != railCommonData.get(0).getTotalCount();
                                commonSquareListingAdapter.notifyDataSetChanged();
                            } else if (layout.equalsIgnoreCase(AppConstants.TYPE2)) {
                                mIsLoading = commonCircleAdapter.getItemCount() != railCommonData.get(0).getTotalCount();
                                commonCircleAdapter.notifyDataSetChanged();
                            } else {
                                mIsLoading = commonLandscapeListingAdapter.getItemCount() != railCommonData.get(0).getTotalCount();
                                commonLandscapeListingAdapter.notifyDataSetChanged();
                            }*/
                            //adapter.notifyDataSetChanged();
                            PrintLogging.printLog("", mScrollY + "mScrollY");
                            getBinding().listRecyclerview.scrollToPosition(mScrollY);
                            //getBinding().listRecyclerview.scrollBy(0, mScrollY);
                        }

                    } else {
                        handleProgressDialog();
                    }
                } else {
                    handleProgressDialog();
                    if (counter == 1) {
                        getBinding().listRecyclerview.setVisibility(View.GONE);
                        getBinding().noDataLayout.setVisibility(View.VISIBLE);
                        getBinding().noData.retryTxt.setVisibility(View.GONE);

                    }
                }
            } else {
                handleProgressDialog();
            }
        }
    }

    private void categoryListingCall() {
        handleProgressDialog();
       // viewModel.getLiveData(assetId, counter, layout, isScrolling).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    List<String> list;

    @Override
    protected void onStart() {
        super.onStart();



    }


    @Override
    public void onClick(String name, int position) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        list.clear();

    }



    @Override
    public void onClick(int caption) {

    }
}



