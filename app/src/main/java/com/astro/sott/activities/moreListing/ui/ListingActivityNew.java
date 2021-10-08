package com.astro.sott.activities.moreListing.ui;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.activities.moreListing.filter.ListingFilterActivity;
import com.astro.sott.activities.moreListing.viewModel.ListingViewModel;
import com.astro.sott.activities.search.ui.ActivitySearch;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.GridSpacingItemDecoration;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.R;
import com.astro.sott.adapter.experiencemng.CommonCircleListingAdapter;
import com.astro.sott.adapter.experiencemng.CommonLandscapeListingAdapteNew;
import com.astro.sott.adapter.experiencemng.CommonPosterListingAdapter;
import com.astro.sott.adapter.experiencemng.CommonPotraitListingAdapter;
import com.astro.sott.adapter.experiencemng.CommonSquareListingAdapter;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.RemoveItemClickListner;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.ListingactivityNewBinding;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.enveu.Enum.ImageType;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class ListingActivityNew extends BaseBindingActivity<ListingactivityNewBinding> implements DetailRailClick, RemoveItemClickListner {
    private int counter = 1;
    private int assetId;
    private int firstVisiblePosition;
    private int pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private String layout;
    private boolean mIsLoading = true;
    private boolean isScrolling = false;
    String title = "";
    private CommonPotraitListingAdapter commonPotraitListingAdapter;
    private CommonSquareListingAdapter commonSquareListingAdapter;
    private CommonCircleListingAdapter commonCircleAdapter;
    private CommonLandscapeListingAdapteNew commonLandscapeListingAdapter;
    private CommonPosterListingAdapter commonPosterListingAdapter;
    private ListingViewModel viewModel;
    private AssetCommonBean assetCommonBean;
    private int mScrollY;
    private final Intent intent = null;
    private boolean tabletSize;
    List<String> list = new ArrayList<>();

    String filterValue = "";
    private boolean isShowFilter = false;
    private boolean isSortable = false;
    private int pageSize = -1;
    private String filter[];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        list.clear();
        // SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(this);
        Gson gson = new Gson();
        String json = gson.toJson(list);
        // sharedPrefHelper.setString("SelectedPreferrence", json);


        tabletSize = getResources().getBoolean(R.bool.isTablet);
        // GAManager.getInstance().trackScreen(getResources().getString(R.string.more_rail_results));
        try {
            AppCommonMethods.resetFilter(ListingActivityNew.this);
        }catch (Exception ignored){

        }
        callOnCreateInstances(intent);

    }

    private void callOnCreateInstances(Intent intent) {
        Log.e(this.getClass().getSimpleName(), "this");

        //new ToolBarHandler(this).setAction(getBinding());
        //new ToolBarHandler(this).setActionListing(getBinding().toolbar, "potrait");
        modelCall();


        getIntentValue();
        setRecyclerProperty();
        connectionObserver();

    }

    private void setFilterRecyclerProperty(List<String> list) {
        try {
            getBinding().recyclerview.setVisibility(View.VISIBLE);
            getBinding().recyclerview.hasFixedSize();
            getBinding().recyclerview.setNestedScrollingEnabled(false);
            getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

            deepSearchcategoryListingCall();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    private void deepSearchcategoryListingCall() {
        if (NetworkConnectivity.isOnline(ListingActivityNew.this)) {

            handleProgressDialog();
            viewModel.getLiveSearchedData(assetId, list, filterValue, counter, layout, isScrolling, pageSize).observe(this, railCommonData -> setLayoutType(railCommonData));
        } else {
            ToastHandler.show(ListingActivityNew.this.getResources().getString(R.string.no_internet_connection), getApplicationContext());
        }
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
            //GAManager.getInstance().trackScreen(getResources().getString(R.string.no_internet_connection_page));
            noConnectionLayout();
        }
    }


    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        /*getBinding().connection.closeButton.setOnClickListener(view -> onBackPressed());
        getBinding().connection.retryTxt.setOnClickListener(view -> {
            PrintLogging.printLog("", "clicking");
            connectionObserver();
        });*/
    }

    private void loadDataFromModel() {
        checkTypeOfList();

    }

    private void handleProgressDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getBinding().progressBar.getVisibility() == View.VISIBLE) {
                    getBinding().progressBar.setVisibility(View.GONE);
                } else {
                    getBinding().progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void setUIComponets(List<RailCommonData> railList) {
        try {
            PrintLogging.printLog("", "layoutType-->>" + layout);
            mIsLoading = true;
            if (layout.equalsIgnoreCase(ImageType.PR1.name())) {
                Log.d("position","potrait");

                commonPotraitListingAdapter = new CommonPotraitListingAdapter(this, railList, AppConstants.Rail3, assetCommonBean.getTitle(), category.getCategory());
                getBinding().listRecyclerview.setAdapter(commonPotraitListingAdapter);
            } else if (layout.equalsIgnoreCase(ImageType.PR2.name())) {
                Log.d("position","poster");

                commonPosterListingAdapter = new CommonPosterListingAdapter(this, railList, AppConstants.Rail3, assetCommonBean.getTitle(), category.getCategory());
                getBinding().listRecyclerview.setAdapter(commonPosterListingAdapter);
            } else if (layout.equalsIgnoreCase(ImageType.SQR.name())) {
                Log.d("position","square");

                commonSquareListingAdapter = new CommonSquareListingAdapter(this, railList, AppConstants.Rail4, assetCommonBean.getTitle(), category.getCategory());
                getBinding().listRecyclerview.setAdapter(commonSquareListingAdapter);
            } else if (layout.equalsIgnoreCase(ImageType.CIR.name())) {
                Log.d("position","circle");

                Log.e("IMAGE", String.valueOf(railList.size()));
                commonCircleAdapter = new CommonCircleListingAdapter(this, railList, AppConstants.Rail2, assetCommonBean.getTitle());
                getBinding().listRecyclerview.setAdapter(commonCircleAdapter);
            } else if (layout.equalsIgnoreCase(ImageType.LDS.name())) {
                Log.d("position","landscape");

                commonLandscapeListingAdapter = new CommonLandscapeListingAdapteNew(this, railList, AppConstants.Rail7, assetCommonBean.getTitle(), category.getCategory());
                getBinding().listRecyclerview.setAdapter(commonLandscapeListingAdapter);
            } else {
                Log.d("position","landsc.");

                commonLandscapeListingAdapter = new CommonLandscapeListingAdapteNew(this, railList, AppConstants.Rail5, assetCommonBean.getTitle(), category.getCategory());
                getBinding().listRecyclerview.setAdapter(commonLandscapeListingAdapter);
            }


            /*adapter = new ListingAdapter(this, layout, railList);*/


            getBinding().listRecyclerview.scrollToPosition(mScrollY);
        } catch (Exception e) {
            PrintLogging.printLog("", "ExceptionIs-->>" + e.getMessage());
        }
    }

    private void UIinitialization() {
        swipeToRefresh();


    }

    VIUChannel category;

    private void getIntentValue() {
        try {
            layout = getIntent().getExtras().getString("layouttype");
            PrintLogging.printLog("", layout + "layout2");
            assetCommonBean = getIntent().getExtras().getParcelable("assetCommonBean");
            PrintLogging.printLog("", "morebuttontype----" + assetCommonBean.getMoreType());

            title = assetCommonBean.getTitle();
            FirebaseEventManager.getFirebaseInstance(ListingActivityNew.this).trackScreenName(title + " Listing");

            long idAsset = assetCommonBean.getID();
            assetId = (int) idAsset;
            getBinding().toolbar.tvSearchResultHeader.setText(title + "");

            getBinding().toolbar.homeIconBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            category = (VIUChannel) getIntent().getExtras().getParcelable("baseCategory");
            this.isShowFilter = getIntent().getExtras().getBoolean("hasFilter");
            this.isSortable = category.isSortable();
            try {
                Log.e("filterSize", category.getSizeofFilter()+"  "+isSortable);
                if (this.isSortable){
                    getBinding().toolbar.ivfilter.setVisibility(View.VISIBLE);
                }else {
                    try {
                        if (category.getSizeofFilter()>0){
                            getBinding().toolbar.ivfilter.setVisibility(View.VISIBLE);
                        }else {
                            getBinding().toolbar.ivfilter.setVisibility(View.GONE);
                        }

                    }catch (Exception e){

                    }

                }
            }catch (Exception e){

            }

            this.pageSize = category.getMorePageSize();
            if (pageSize <= 0) {
                pageSize = 20;
            }



            getBinding().toolbar.ivfilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ListingActivityNew.this, ListingFilterActivity.class);
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
        }

    }

    private void setRecyclerProperty() {
        int spacing;
        int spanCount;
        if (tabletSize) {

            if (layout.equalsIgnoreCase(ImageType.LDS.name()) || layout.equalsIgnoreCase(AppConstants.TYPE7)) {
                spanCount = 4;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstants.LANSCAPE_SPACING, r.getDisplayMetrics());
            } else {
                spanCount = 5;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstants.PORTRAIT_SPACING, r.getDisplayMetrics());

            }

//            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
//            getBinding().listRecyclerview.addItemDecoration(itemDecoration);
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(ListingActivity.this, spanCount);
//            getBinding().listRecyclerview.setLayoutManager(gridLayoutManager);
        } else {

            if (layout.equalsIgnoreCase(ImageType.LDS.name()) || layout.equalsIgnoreCase(AppConstants.TYPE7)) {
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(ListingActivityNew.this, spanCount);
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
    public ListingactivityNewBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ListingactivityNewBinding.inflate(inflater);
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
            simmilarMovieListingCall();
        } else if (type == AppConstants.YOU_MAY_LIKE) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
            youMayAlsoLikeListing();
        } else if (type == AppConstants.WEB_EPISODE) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
            webEpisodeListing();
        } else if (type == AppConstants.SPOTLIGHT_EPISODE) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
            spotLightEpisodeListing();
        } else if (type == AppConstants.SIMILLAR_UGC_VIDEOS) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
            simillarUGCVideosListing();
        } else if (type == AppConstants.SIMILLAR_UGC_CREATOR) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
            simillarUGCCreatorListing();
        } else if (type == AppConstants.LIVE_CHANNEL_LIST) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
            liveChannelListing();
        } else if (type == AppConstants.SIMILLAR_CHANNEL_LIST) {
            hideToolbarViews();
            PrintLogging.printLog("", "moreClicked" + "---" + type + "");
            simillarChannelListing();
        } else {
            showToolbarView();
            //categoryListingCall();
            deepSearchcategoryListingCall();
        }
    }


    private void showToolbarView() {
       /* getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.backButton.setVisibility(View.VISIBLE);
        getBinding().toolbar.searchIcon.setVisibility(View.GONE);
        getBinding().toolbar.videoIcon.setVisibility(View.GONE);
        getBinding().toolbar.searchIcon.setVisibility(View.VISIBLE);

        if (!isShowFilter && !isSortable)
            getBinding().toolbar.filterIcon.setVisibility(View.GONE);
        else
            getBinding().toolbar.filterIcon.setVisibility(View.VISIBLE);*/

    }

    private void hideToolbarViews() {
        /*getBinding().toolbar.homeIcon.setVisibility(View.GONE);
        getBinding().toolbar.backButton.setVisibility(View.VISIBLE);
        getBinding().toolbar.searchIcon.setVisibility(View.GONE);
        getBinding().toolbar.videoIcon.setVisibility(View.GONE);
        getBinding().toolbar.filterIcon.setVisibility(View.GONE);
        getBinding().toolbar.searchIcon.setVisibility(View.GONE);
        getBinding().toolbar.cancelIcon.setVisibility(View.GONE);*/
    }

    private void simillarChannelListing() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 8, isScrolling, counter).observe(this, railCommonData -> {
            PrintLogging.printLog("", "railCommonResponse" + railCommonData.get(0).getId());
            setLayoutType(railCommonData);
        });
    }

    private void liveChannelListing() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 7, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));

    }

    private void simillarUGCCreatorListing() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 6, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void simillarUGCVideosListing() {

        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 5, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void spotLightEpisodeListing() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 4, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void webEpisodeListing() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 3, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void youMayAlsoLikeListing() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 1, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

    private void simmilarMovieListingCall() {
        handleProgressDialog();
        viewModel.getSimmilarMovies(assetCommonBean, assetCommonBean.getAsset(), layout, 2, isScrolling, counter).observe(this, railCommonData -> setLayoutType(railCommonData));
    }

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
                            if (layout.equalsIgnoreCase(ImageType.PR1.name())) {
                                mIsLoading = commonPotraitListingAdapter.getItemCount() != railCommonData.get(0).getTotalCount();
                                commonPotraitListingAdapter.notifyDataSetChanged();
                            } else if (layout.equalsIgnoreCase(ImageType.PR2.name())) {
                                mIsLoading = commonPosterListingAdapter.getItemCount() != railCommonData.get(0).getTotalCount();
                                commonPosterListingAdapter.notifyDataSetChanged();
                            } else if (layout.equalsIgnoreCase(ImageType.SQR.name())) {
                                mIsLoading = commonSquareListingAdapter.getItemCount() != railCommonData.get(0).getTotalCount();
                                commonSquareListingAdapter.notifyDataSetChanged();
                            } else if (layout.equalsIgnoreCase(ImageType.CIR.name())) {
                                mIsLoading = commonCircleAdapter.getItemCount() != railCommonData.get(0).getTotalCount();
                                commonCircleAdapter.notifyDataSetChanged();
                            } else {
                                mIsLoading = commonLandscapeListingAdapter.getItemCount() != railCommonData.get(0).getTotalCount();
                                commonLandscapeListingAdapter.notifyDataSetChanged();
                            }
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
                getBinding().listRecyclerview.setVisibility(View.GONE);
                getBinding().noDataLayout.setVisibility(View.VISIBLE);
                getBinding().noData.retryTxt.setVisibility(View.GONE);
            }
        }
    }

    private void categoryListingCall() {
        handleProgressDialog();
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


    @Override
    protected void onStart() {
        super.onStart();

        /*try {
            if (list.size() > 0) {
                list.clear();
            }
            list = AppCommonMethods.getDeepSearchListPreferences(getApplicationContext());

            if (list.size() > 0) {
                setFilterRecyclerProperty(list);
            } else {
                getBinding().recyclerview.setVisibility(View.GONE);
                if (AppConstants.FILETR_CLICKED == false) {

                } else {
                    counter = 1;
                    isScrolling = false;
                    // loadDataFromModel();
                    deepSearchcategoryListingCall();
                }
            }
        } catch (Exception e) {

        }*/
    }

    @Override
    public void onClick(String name, int position) {
//        try {
//            list.remove(position);
//
//            filterAdapter.notifyItemRemoved(position);
//            AppCommonMethods.saveDeepSearchValue(getApplicationContext(),list);
//            PrintLogging.printLog("", "listSizeIs" + list.size());
//            if (filterAdapter.getItemCount() == 0) {
//                deepSearchcategoryListingCall();
//                getBinding().recyclerview.setVisibility(View.GONE);
//
//            } else {
//                getBinding().recyclerview.setVisibility(View.VISIBLE);
//                deepSearchcategoryListingCall();
//            }
//        }catch (IndexOutOfBoundsException e){
//
//        }

       /* try {
            list.remove(position);

            filterAdapter.notifyItemRemoved(position);

            PrintLogging.printLog("", "listSizeIs" + list.size());
            AppCommonMethods.saveDeepSearchValue(getApplicationContext(), list);
            if (filterAdapter.getItemCount() == 0) {
                counter = 1;
                isScrolling = false;
                // loadDataFromModel();
                deepSearchcategoryListingCall();
                getBinding().recyclerview.setVisibility(View.GONE);

            } else {
                getBinding().recyclerview.setVisibility(View.VISIBLE);
                counter = 1;
                isScrolling = false;
                //loadDataFromModel();
                deepSearchcategoryListingCall();
            }
        } catch (IndexOutOfBoundsException e) {

        }*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        list.clear();
       /* AppConstants.FILETR_CLICKED = false;
        AppCommonMethods.saveDeepSearchValue(getApplicationContext(), list);*/
//        list.clear();
//        SharedPrefHelper sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        sharedPrefHelper.setString("SelectedPreferrence", json);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!KsPreferenceKey.getInstance(ListingActivityNew.this).getFilterApply().equalsIgnoreCase("")) {
            if (KsPreferenceKey.getInstance(ListingActivityNew.this).getFilterApply().equalsIgnoreCase("true")) {
                KsPreferenceKey.getInstance(ListingActivityNew.this).setFilterApply("false");
                isScrolling=false;
                commonLandscapeListingAdapter=null;
                counter=1;
                if (pageSize <= 0) {
                    pageSize = 20;
                }
                connectionObserver();
            }
        }
    }
}



