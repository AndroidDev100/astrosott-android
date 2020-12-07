package com.dialog.dialoggo.activities.search.ui;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.catchUpRails.ui.CatchupActivity;
import com.dialog.dialoggo.activities.deviceMangment.helper.RecyclerTouchListener;
import com.dialog.dialoggo.activities.forwardEPG.ForwardedEPGActivity;
import com.dialog.dialoggo.activities.liveChannel.liveChannelManager.LiveChannelManager;
import com.dialog.dialoggo.activities.liveChannel.ui.LiveChannel;
import com.dialog.dialoggo.activities.movieDescription.ui.MovieDescriptionActivity;
import com.dialog.dialoggo.activities.search.adapter.ResultAdapterAll;
import com.dialog.dialoggo.activities.search.viewModel.ResultViewModel;
import com.dialog.dialoggo.activities.webEpisodeDescription.ui.WebEpisodeDescriptionActivity;
import com.dialog.dialoggo.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.beanModel.commonBeanModel.SearchModel;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonImages;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonUrls;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.callBacks.commonCallBacks.CheckLiveProgram;
import com.dialog.dialoggo.callBacks.commonCallBacks.ClickListener;
import com.dialog.dialoggo.databinding.ActivityResultBinding;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.MediaTypeConstant;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.StringBuilderHolder;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends BaseBindingActivity<ActivityResultBinding> {
    private boolean mIsLoading = true;
    private  boolean isScrolling = false;
    private int mScrollY;
    private SearchModel allResult;
    private ResultViewModel viewModel;
    private ResultAdapterAll adapter;
    private int counter = 1;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, firstVisiblePosition;
    private RailCommonData railCommonData;

    @Override
    public ActivityResultBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityResultBinding.inflate(inflater);
    }


    private void modelCall() {
        Log.e("Result ","Activity");
        viewModel = ViewModelProviders.of(this).get(ResultViewModel.class);
        UIinitialization();

    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(boolean aBoolean) {
        if (aBoolean) {
            loadDataFromModel();
        } else {
            noConnectionLayout();
        }
    }

    private void UIinitialization() {

        StringBuilderHolder.getInstance().clear();
        StringBuilderHolder.getInstance().append(getResources().getString(R.string.all) + " ");
        StringBuilderHolder.getInstance().append(allResult.getHeaderTitle() + "s");
        StringBuilderHolder.getInstance().append(" "+getResources().getString(R.string.results));
        StringBuilderHolder.getInstance().append(" (" + allResult.getTotalCount() + ")");
        getBinding().toolbar.tvSearchResultHeader.setText(StringBuilderHolder.getInstance().getText());

        getBinding().toolbar.homeIconBack.setOnClickListener(view -> onBackPressed());

        getBinding().toolbar.ivClose.setOnClickListener(view -> onBackPressed());

        getBinding().resultRecycler.hasFixedSize();
        getBinding().resultRecycler.setNestedScrollingEnabled(false);
//        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        getBinding().resultRecycler.setLayoutManager(mLayoutManager);

        boolean isTablet = this.getResources().getBoolean(R.bool.isTablet);
        if(isTablet){
            getBinding().resultRecycler.setLayoutManager(new GridLayoutManager(this,5));
        }else {

            getBinding().resultRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        }
        swipeToRefresh();

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());


    }

    private void loadDataFromModel() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        viewModel.getListSearchResult(ResultActivity.this, String.valueOf(allResult.getType()), allResult.getSearchString(), counter, isScrolling).observe(this, assets -> {

            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            if (assets != null && assets.size() > 0) {
                if (!isScrolling) {
                    setUIComponets(assets);
                } else {
                    mIsLoading = adapter.getItemCount() != allResult.getTotalCount();
                    adapter.notifyDataSetChanged();
                    getBinding().resultRecycler.scrollToPosition(mScrollY + mScrollY);

                }
            }
        });
    }


    private void setUIComponets(final List<Asset> allItemsInSection) {
        mIsLoading = true;
        if (counter > 1) {

            PrintLogging.printLog(this.getClass(), "counter" + counter, "");
        } else {
            adapter = new ResultAdapterAll(ResultActivity.this, allItemsInSection);
            getBinding().resultRecycler.setAdapter(adapter);
        }
        getBinding().resultRecycler.addOnItemTouchListener(new RecyclerTouchListener(ResultActivity.this, getBinding().resultRecycler, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                final Asset itemValue = allItemsInSection.get(position);

                if (itemValue != null) {

                    if (itemValue.getType() == MediaTypeConstant.getMovie(ResultActivity.this)) {
                        getRailCommonData(itemValue);
                        // new ToastHandler(this).show(allResult.getHeaderTitle());
                        if (railCommonData.getImages().size() == itemValue.getImages().size())
                            new ActivityLauncher(ResultActivity.this).detailActivity(ResultActivity.this, MovieDescriptionActivity.class, railCommonData, AppLevelConstants.Rail3);

                    } else if (itemValue.getType() == MediaTypeConstant.getShortFilm(ResultActivity.this)) {
                        getRailCommonData(itemValue);
                        //  new ToastHandler(this).show(allResult.getHeaderTitle());
                        if (railCommonData.getImages().size() == itemValue.getImages().size())
                            new ActivityLauncher(ResultActivity.this).detailActivity(ResultActivity.this, MovieDescriptionActivity.class, railCommonData, AppLevelConstants.Rail5);
//                        new ActivityLauncher(ResultActivity.this).detailActivity(ResultActivity.this, MovieDescriptionActivity.class, railCommonData, AppLevelConstants.Rail3);

                    } else if (itemValue.getType() == MediaTypeConstant.getDrama(ResultActivity.this)) {
                        getRailCommonData(itemValue);
                        //new ToastHandler(activity).show("Short Film");
                        if (railCommonData.getImages().size() == itemValue.getImages().size())
                            new ActivityLauncher(ResultActivity.this).webSeriesActivity(ResultActivity.this, WebSeriesDescriptionActivity.class, railCommonData, AppLevelConstants.Rail5);
//                        new ActivityLauncher(ResultActivity.this).detailActivity(ResultActivity.this, MovieDescriptionActivity.class, railCommonData, AppLevelConstants.Rail3);

                    } else if (itemValue.getType() == MediaTypeConstant.getWebEpisode(ResultActivity.this)) {
                        getRailCommonData(itemValue);
                        //  new ToastHandler(this).show(allResult.getHeaderTitle());
                        new ActivityLauncher(ResultActivity.this).webEpisodeActivity(ResultActivity.this, WebEpisodeDescriptionActivity.class, railCommonData, AppLevelConstants.Rail5);
//                    new ActivityLauncher(ResultActivity.this).detailActivity(ResultActivity.this, MovieDescriptionActivity.class, railCommonData, AppLevelConstants.Rail3);

                    } else if (itemValue.getType() == MediaTypeConstant.getLinear(ResultActivity.this)) {
                        getRailCommonData(itemValue);
                        if (railCommonData.getImages().size() == itemValue.getImages().size())
                            new ActivityLauncher(ResultActivity.this).liveChannelActivity(ResultActivity.this, LiveChannel.class, railCommonData);
                    } else if (itemValue.getType() == MediaTypeConstant.getProgram(ResultActivity.this)) {
                        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
                        new LiveChannelManager().getLiveProgram(ResultActivity.this, itemValue, new CheckLiveProgram() {
                            @Override
                            public void response(CommonResponse asset) {
                                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                                if (asset != null) {
                                    if (asset.getStatus()) {
                                        if (asset.getLivePrograme()) {
                                            PrintLogging.printLog(this.getClass(), "", "Live Program" + asset.getCurrentProgram().getName());
                                            getProgramRailCommonData(itemValue, "liveChannelCall-->>" + asset.getStatus());
                                            new ActivityLauncher(ResultActivity.this).liveChannelActivity(ResultActivity.this, LiveChannel.class, railCommonData);
                                        } else {
                                            getProgramRailCommonData(asset.getCurrentProgram(), "liveChannelCall-->>" + asset.getStatus() + "--" + asset.getProgramTime());
                                            if (asset.getProgramTime() == 1) {
                                                getProgramRailCommonData(itemValue, AppLevelConstants.PROGRAM_VIDEO_CLICLKED);
                                                new ActivityLauncher(ResultActivity.this).catchUpActivity(ResultActivity.this, CatchupActivity.class, railCommonData);
                                            } else {
                                                getProgramRailCommonData(itemValue, AppLevelConstants.PROGRAM_CLICKED);
                                                new ActivityLauncher(ResultActivity.this).forwardeEPGActivity(ResultActivity.this, ForwardedEPGActivity.class, railCommonData);
                                            }
                                        }
                                    } else {
                                        PrintLogging.printLog(this.getClass(), "", "forwardedEPG");
                                    }
                                } else {
                                    //Asset Not Found
                                    runOnUiThread(() -> ToastHandler.show("Asset Not Found", ResultActivity.this));

                                }
                            }
                        });
                        //  liveProgramCallBack.response(itemValue);
                        //new ActivityLauncher(activity).liveChannelActivity(activity, LiveChannel.class,railCommonData);
                    }

                }


            }


        }));
    }


    private void swipeToRefresh() {


        getBinding().resultRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                mScrollY += dy;
                               connectionObserver();

                            }
                        }
                    }
                } catch (Exception e) {
                    PrintLogging.printLog("Exception", "", "" + e);
                }
            }
        });


    }
    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundleValue();
        modelCall();
        connectionObserver();
    }


    private void getBundleValue() {

        if (getIntent().hasExtra("SearchResult")) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                extras = extras.getBundle("SearchResult");
                if (extras != null)
                    allResult = extras.getParcelable("Search_Show_All");
            }
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras " + "Search_Show_All");
        }
    }

    private void getProgramRailCommonData(Asset currentProgram, String program_videoItemClicked) {
        railCommonData = new RailCommonData();
        railCommonData.setObject(currentProgram);
    }

    private void getRailCommonData(Asset itemValue) {
        railCommonData = new RailCommonData();
        ArrayList<AssetCommonImages> imagelist = new ArrayList<>();
        for (int i = 0; i < itemValue.getImages().size(); i++) {
            AssetCommonImages assetCommonImages = new AssetCommonImages();
            assetCommonImages.setImageUrl(itemValue.getImages().get(i).getUrl());
            imagelist.add(assetCommonImages);
        }

        ArrayList<AssetCommonUrls> videoList = new ArrayList<>();
        for (int i = 0; i < itemValue.getMediaFiles().size(); i++) {
            AssetCommonUrls assetCommonImages = new AssetCommonUrls();
            assetCommonImages.setUrl(itemValue.getMediaFiles().get(i).getUrl());
            assetCommonImages.setDuration(String.valueOf(itemValue.getMediaFiles().get(i).getDuration()));
            assetCommonImages.setUrlType(itemValue.getMediaFiles().get(i).getType());
            videoList.add(assetCommonImages);
        }


        if (itemValue.getImages().size() == imagelist.size())
            railCommonData.setUrls(videoList);
        if (itemValue.getImages().size() == imagelist.size())
            railCommonData.setImages(imagelist);
        railCommonData.setTilte(allResult.getHeaderTitle());
        railCommonData.setObject(itemValue);
        //railCommonData.setCatchUpBuffer(itemValue.getEnableCatchUp());
        railCommonData.setId(itemValue.getId());
        railCommonData.setType(itemValue.getType());
        railCommonData.setName(itemValue.getName());


    }
}
