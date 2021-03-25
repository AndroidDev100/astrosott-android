package com.astro.sott.baseModel;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.activities.movieDescription.adapter.MovieDescriptionCommonAdapter;
import com.astro.sott.activities.movieDescription.ui.MovieDescriptionActivity;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.adapter.CommonAdapter;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.ContinueWatchingRemove;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.HeroItemClickListner;
import com.astro.sott.callBacks.commonCallBacks.RemoveAdsCallBack;
import com.astro.sott.databinding.DetailFooterFragmentBinding;
import com.astro.sott.fragments.detailRailFragment.adapter.SimilarAdapter;
import com.astro.sott.fragments.home.viewModel.HomeFragmentViewModel;
import com.astro.sott.repositories.homeTab.HomeFragmentRepository;
import com.astro.sott.utils.TabsData;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.SpacingItemDecoration;
import com.astro.sott.utils.helpers.StringUtils;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecommendationRailFragment extends BaseBindingFragment<DetailFooterFragmentBinding> implements ContinueWatchingRemove, RemoveAdsCallBack, HeroItemClickListner {
    HomeFragmentViewModel viewModel;
    int tabId;
    int mediaType;
    Asset asset;
    List<VIUChannel> dtChannelsList;
    List<VIUChannel> channelList;
    private List<RailCommonData> youMayAlsoLikeData;
    int continueWatchingIndex = -1;
    MovieDescriptionCommonAdapter adapter;
    private SimilarAdapter similarAdapter;
    List<AssetCommonBean> loadedList;
    private int mScrollY;
    int counter = 1;
    private int totalCount;
    private int firstVisiblePosition, pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean mIsLoading = true, isScrolling = false;


    int swipeToRefresh = 0;
    private Context context;
    private boolean iscalled = false;
    private int assetType;
    private Map<String, MultilingualStringValueArray> map;
    private int layoutType;
    private long assetId;

    public RecommendationRailFragment() {
        // Required empty public constructor
    }

    @Override
    protected DetailFooterFragmentBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return DetailFooterFragmentBinding.inflate(inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            loadedList = new ArrayList<>();
            modelCall();
            iscalled = true;
            UIinitialization();

            getVideoRails();
        } catch (Exception e) {
            Log.w("", e + "");
            //  tabsVisibility(true);
        }
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(HomeFragmentViewModel.class);

    }

    public void getVideoRails() {
        //   AppConstants.FRAGMENT_CALL = AppConstants.FRAGMENT_CALL + 1;
        if (channelList != null)
            channelList.clear();
        if (dtChannelsList != null)
            dtChannelsList.clear();
        Bundle bundle = getArguments();
        if (bundle != null) {
            tabId = bundle.getInt("BUNDLE_TAB_ID");
            mediaType = bundle.getInt("MEDIA_TYPE");
            asset = bundle.getParcelable("ASSET_OBJ");
            layoutType = bundle.getInt("LAYOUT_TYPE");
            connectionObserver();

        }
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(getActivity())) {
            connectionValidation(true);
        } else {

            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            if (TabsData.getInstance().getYouMayAlsoLikeData() != null) {
                youMayAlsoLikeData = new ArrayList<>();
                setSimilarUIComponent(TabsData.getInstance().getYouMayAlsoLikeData());
            }
        }
    }

    private void UIinitialization() {
        setRecyclerProperties(getBinding().recyclerView);
    }

    public void setRecyclerProperties(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.addItemDecoration(new SpacingItemDecoration(20, 2));

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);

        setPagination();
    }

    private void setPagination() {

        getBinding().loadMore.setOnClickListener(v -> {
            counter++;
            callYouMayAlsoLike(asset.getId(), counter, asset.getType(), asset.getTags());

        });
        /*getBinding().recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                try {
                    LinearLayoutManager layoutManager = ((LinearLayoutManager) getBinding().recyclerView.getLayoutManager());
                    firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    if (dy > 0) {
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                        if (mIsLoading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                mIsLoading = false;

                                isScrolling = true;
                                mScrollY += dy;
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage());
                }
            }
        });*/
    }


    private void callYouMayAlsoLike(Long assetId, int counter, int assetType, Map<String, MultilingualStringValueArray> map) {
        long asseId = assetId;
        getBinding().loadMore.setText(getActivity().getResources().getString(R.string.loading));
        viewModel.getYouMayAlsoLike((int) asseId, counter, assetType, map).observe(this, assetCommonBeans -> {
            try {
                getBinding().loadMore.setText("Load More");
                if (assetCommonBeans.size() > 0) {
                    if (assetCommonBeans.get(0).getStatus()) {
                        setSimilarUIComponent(assetCommonBeans.get(0).getRailAssetList());
                    } else {
                        getBinding().loadMore.setVisibility(View.GONE);

                    }
                } else {
                    getBinding().loadMore.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                getBinding().loadMore.setVisibility(View.GONE);

            }


        });

    }

    private void setSimilarUIComponent(List<RailCommonData> railCommonData) {
        youMayAlsoLikeData.addAll(railCommonData);
        if (youMayAlsoLikeData.get(0) != null)
            totalCount = youMayAlsoLikeData.get(0).getTotalCount();
        if (similarAdapter == null) {
            similarAdapter = new SimilarAdapter(getActivity(), youMayAlsoLikeData);
            getBinding().recyclerView.setAdapter(similarAdapter);
        } else {
            similarAdapter.notifyDataSetChanged();
        }
        if (totalCount <= similarAdapter.getItemCount()) {
            getBinding().loadMore.setVisibility(View.GONE);
        } else {
            getBinding().loadMore.setVisibility(View.GONE);

        }

    }

    private void callRailAPI(List<VIUChannel> list) {
        // tabsVisibility(false);
        channelList = list;
        if (viewModel!=null && getActivity()!=null && !getActivity().isFinishing()) {
            if (counter < channelList.size()) {
                viewModel.getListLiveData(channelList.get(counter).getId(), dtChannelsList, counter, swipeToRefresh, loadedList).observe((LifecycleOwner) context, assetCommonBeans -> {
                    if (assetCommonBeans != null && assetCommonBeans.size() > 0) {
                        boolean status = assetCommonBeans.get(0).getStatus();
                        if (status) {
                            setUIComponets(assetCommonBeans);
                            getBinding().recyclerView.setVisibility(View.VISIBLE);

                        } else {
                            if (counter != channelList.size()) {
                                getBinding().recyclerView.setVisibility(View.VISIBLE);
                                counter++;
                                callRailAPI(channelList);
                            } else {
                                getBinding().recyclerView.setVisibility(View.GONE);
                            }
                        }
                    }
                });
            } else {
                swipeToRefresh = 1;
                PrintLogging.printLog("", "sizeOfLoadedList--" + loadedList.size());
            }
        }
    }

    private void setUIComponets(List<AssetCommonBean> assetCommonBeans) {
        try {
            if (channelList != null && channelList.size() > 0 && !StringUtils.isNullOrEmptyOrZero(channelList.get(counter).getDescription())) {
                String val = channelList.get(counter).getDescription();
                if (val.equalsIgnoreCase(AppConstants.KEY_CONTINUE_WATCHING)) {
                    continueWatchingIndex = counter - 1;
                }
            }

            if (adapter != null) {
                loadedList.add(assetCommonBeans.get(0));
                adapter.notifyItemChanged(adapter.getItemCount());
                counter++;
                callRailAPI(channelList);
            } else {
                loadedList.add(assetCommonBeans.get(0));
                if (channelList.size() > 3) {
                    if (loadedList.size() >= 1) {
                        if (context instanceof WebSeriesDescriptionActivity) {
                            adapter = new MovieDescriptionCommonAdapter((WebSeriesDescriptionActivity) context, loadedList);
                        } else if (context instanceof MovieDescriptionActivity) {
                            adapter = new MovieDescriptionCommonAdapter((MovieDescriptionActivity) context, loadedList);
                            // ((MovieDescriptionActivity) context).showTabs();
                        }
                        getBinding().recyclerView.setAdapter(adapter);
                        counter++;
                        callRailAPI(channelList);
                    }
                } else {
                    if (context instanceof WebSeriesDescriptionActivity) {
                        adapter = new MovieDescriptionCommonAdapter((WebSeriesDescriptionActivity) context, loadedList);
                    } else if (context instanceof MovieDescriptionActivity) {
                        adapter = new MovieDescriptionCommonAdapter((MovieDescriptionActivity) context, loadedList);
                        //  ((MovieDescriptionActivity) context).showTabs();
                    }
                    getBinding().recyclerView.setAdapter(adapter);
                    counter++;
                    callRailAPI(channelList);
                }
            }
        } catch (Exception e) {
            PrintLogging.printLog("", "catchAdapterSet--" + e.toString());
        }

    }

    @Override
    public void remove(Long assetID, int position, int continueWatchingIndex, int listSize) {

    }

    @Override
    public void removeAdOnFailure(int position) {

    }


    @Override
    public void onDetach() {
        super.onDetach();
        HomeFragmentRepository.getInstance().resetObject();

    }

    @Override
    public void heroItemClick(int position, RailCommonData railCommonData, AssetCommonBean commonData) {


    }


}
