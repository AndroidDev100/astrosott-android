package com.astro.sott.baseModel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.callBacks.commonCallBacks.RemoveAdsCallBack;
import com.astro.sott.fragments.sports.viewModel.SportsViewModel;
import com.astro.sott.fragments.video.viewModel.VideoViewModel;
import com.astro.sott.repositories.homeTab.HomeFragmentRepository;
import com.astro.sott.utils.helpers.ShimmerDataModel;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.R;
import com.astro.sott.adapter.CommonAdapter;
import com.astro.sott.adapter.shimmer.CustomShimmerAdapter;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.ContinueWatchingRemove;
import com.astro.sott.callBacks.commonCallBacks.HeroItemClickListner;
import com.astro.sott.databinding.FragmentHomeBinding;
import com.astro.sott.fragments.home.viewModel.HomeFragmentViewModel;
import com.astro.sott.fragments.livetv.viewModel.LiveTvViewModel;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;

import java.util.ArrayList;
import java.util.List;

public class TabsBaseFragment<T extends HomeBaseViewModel> extends BaseBindingFragment<FragmentHomeBinding> implements ContinueWatchingRemove, RemoveAdsCallBack, HeroItemClickListner {

    private final List<AssetCommonBean> loadedList = new ArrayList<>();
    private HomeBaseViewModel viewModel;
    private boolean mIsLoading = true, isScrolling = false;
    private int counter = 0;
    private int swipeToRefresh = 0;
    private int count = 0;
    private List<VIUChannel> channelList;
    private List<VIUChannel> dtChannelsList;
    private List<VIUChannel> newDtChannelsList;
    private int counterValueApiFail = 0;
    private CommonAdapter adapter;
    private int mScrollY;
    private OnFragmentInteractionListener mListener;
    private boolean crouseInjected = false;
    private String screenID;

    protected void setViewModel(Class<? extends HomeBaseViewModel> viewModelClass) {
        viewModel = ViewModelProviders.of(this).get(viewModelClass);
    }

    @Override
    public FragmentHomeBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentHomeBinding.inflate(inflater);
    }

    private void modelCall() {
        getBinding().transparentLayout.setOnClickListener(view -> {

        });
        setTabId();
        connectionObserver();
    }

    private void connectionObserver() {
        if (getActivity() != null
                && NetworkConnectivity.isOnline(getActivity())) {
            swipeToRefresh = 2;
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().swipeContainer.setRefreshing(true);
            UIinitialization();
            // loadDataFromModel();
            getBaseCategoryRail();
        } else {
            noConnectionLayout();
        }
    }

    List<BaseCategory> baseCategories;

    public void getBaseCategoryRail() {
        HomeFragmentRepository.getInstance().getCategories(screenID).observe((LifecycleOwner) getActivity(), baseCategoriesList -> {
            if (baseCategoriesList.size() > 0) {
                baseCategories = baseCategoriesList;
                dtChannelsList = new ArrayList<>();
                for (int j = 0; j < baseCategoriesList.size(); j++) {
                    VIUChannel tempChannel = new VIUChannel(getActivity(), baseCategoriesList.get(j));
                    dtChannelsList.add(tempChannel);
                }
                if (dtChannelsList.size() == baseCategoriesList.size()) {
                    loadDataFromModel();
                }
            } else {
                getBinding().swipeContainer.setRefreshing(false);
                getBinding().myRecyclerView.setVisibility(View.GONE);
                getBinding().noDataLayout.setVisibility(View.VISIBLE);
                PrintLogging.printLog("", "in error");
            }


        });
    }


    private void UIinitialization() {
        swipeToRefresh();
        getBinding().myRecyclerView.setHasFixedSize(true);
        getBinding().myRecyclerView.setItemViewCacheSize(20);
        // getBinding().myRecyclerView.setNestedScrollingEnabled(false);
        getBinding().myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        CustomShimmerAdapter adapter = new CustomShimmerAdapter(getActivity(), new ShimmerDataModel(getActivity()).getList(0), new ShimmerDataModel(getActivity()).getSlides());
        getBinding().myRecyclerView.setAdapter(adapter);
        getBinding().noData.retryTxt.setOnClickListener(view -> {
            getBinding().noDataLayout.setVisibility(View.GONE);
            getBinding().myRecyclerView.setVisibility(View.VISIBLE);
            swipeToRefresh = 2;
            counter = 0;
            counterValueApiFail = 0;
            connectionObserver();
        });
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

/*
    private void loadDataFromModel() {

        if (getActivity() != null && swipeToRefresh != 1) {
            viewModel.getChannelList().observe(getActivity(), assetCommonBean -> {
                if (assetCommonBean != null && assetCommonBean.getStatus()) {
                    dtChannelsList = assetCommonBean.getDTChannelList();
                    callRailAPI(dtChannelsList);
                } else {
                    getBinding().myRecyclerView.setVisibility(View.GONE);
                    getBinding().noDataLayout.setVisibility(View.VISIBLE);
                }

            });
        }
    }
*/

    private void loadDataFromModel() {

        if (swipeToRefresh == 1) {

        } else {
            callRailAPI(dtChannelsList);
        }
    }


/*
    private void callRailAPI(List<VIUChannel> list) {
        channelList = list;
        if (getActivity() != null && counter < channelList.size()) {
            viewModel.getListLiveData(channelList.get(counter).getId(), dtChannelsList, counter, swipeToRefresh, crouseInjected).observe(getActivity(), assetCommonBeans -> {
                mIsLoading = true;
                if (assetCommonBeans != null && assetCommonBeans.size() > 0) {
                    if (assetCommonBeans.get(0) != null) {
                        boolean status = assetCommonBeans.get(0).getStatus();
                        if (status) {
                            crouseInjected = true;
                            setUIComponets(assetCommonBeans);
                            counter++;
                            counterValueApiFail++;
                            callRailAPI(channelList);
                            getBinding().noDataLayout.setVisibility(View.GONE);
                            getBinding().myRecyclerView.setVisibility(View.VISIBLE);
                        } else {
                            swipeToRefresh = 0;
                            if (counter != channelList.size()) {
                                getBinding().noDataLayout.setVisibility(View.GONE);
                                getBinding().myRecyclerView.setVisibility(View.VISIBLE);
                                counter++;
                                callRailAPI(channelList);
                            } else {
                                getBinding().myRecyclerView.setVisibility(View.GONE);
                                getBinding().noDataLayout.setVisibility(View.VISIBLE);
                                getBinding().transparentLayout.setVisibility(View.GONE);
                            }
                        }

                    }
                } else {
                    counter++;
                    callRailAPI(channelList);
                    swipeToRefresh = 1;
                }
            });

        } else {
            swipeToRefresh = 1;
        }
    }
*/

    private void callRailAPI(List<VIUChannel> list) {
        channelList = list;
        if (viewModel!=null && getActivity()!=null && !getActivity().isFinishing()) {
            if (counter != channelList.size() && counter < channelList.size()) {
                viewModel.getListLiveData(channelList.get(counter).getId(), dtChannelsList, counter, swipeToRefresh, loadedList).observe(getActivity(), new Observer<List<AssetCommonBean>>() {
                    @Override
                    public void onChanged(@Nullable List<AssetCommonBean> assetCommonBeans) {

                        mIsLoading = true;
                        if (assetCommonBeans.size() > 0) {
                            PrintLogging.printLog("", "sizeAsset" + assetCommonBeans.size() + "");
                            boolean status = false;
                            if (assetCommonBeans.get(0) != null && assetCommonBeans.get(0).getStatus())
                                status = assetCommonBeans.get(0).getStatus();
                            PrintLogging.printLog("", "sizeAsset" + assetCommonBeans.get(0).getStatus() + "");


                            if (status == true) {
                            /*if (swipeToRefresh == 2) {
                                adapter = null;
                                loadedList.clear();
                            }*/
                                swipeToRefresh = 0;
                                setUIComponets(assetCommonBeans);
                                counter++;
                                counterValueApiFail++;
                                callRailAPI(channelList);
                                getBinding().noDataLayout.setVisibility(View.GONE);
                                getBinding().myRecyclerView.setVisibility(View.VISIBLE);
                                //getBinding().transparentLayout.setVisibility(View.GONE);

                            } else {
                                swipeToRefresh = 0;
                                if (counter != channelList.size()) {
                                    getBinding().noDataLayout.setVisibility(View.GONE);
                                    getBinding().myRecyclerView.setVisibility(View.VISIBLE);
                                    //getBinding().transparentLayout.setVisibility(View.GONE);
                                    counter++;
                                    callRailAPI(channelList);
                                } else {
                                    getBinding().myRecyclerView.setVisibility(View.GONE);
                                    getBinding().noDataLayout.setVisibility(View.VISIBLE);
                                    PrintLogging.printLog("", "in error");
                                    getBinding().transparentLayout.setVisibility(View.GONE);
                                }
                            }

                        } else {
                            // getBinding().myRecyclerView.setVisibility(View.GONE);
                            // getBinding().noDataLayout.setVisibility(View.VISIBLE);
                            // PrintLogging.printLog("","in error");

                            // ToastHandler.show(getActivity().getResources().getString(R.string.no_feeds_available), ApplicationMain.getAppContext());
                        }


                    }
                });

            } else {
                if (adapter != null) {
                    if (adapter.getItemCount() <= 0) {
                        showNoData();
                    }
                } else {
                    showNoData();

                }

                swipeToRefresh = 1;
                PrintLogging.printLog("", "sizeOfLoadedList--" + loadedList.size());
            }
        }

    }

    private void showNoData() {
        getBinding().swipeContainer.setRefreshing(false);
        getBinding().myRecyclerView.setVisibility(View.GONE);
        getBinding().noDataLayout.setVisibility(View.VISIBLE);
        PrintLogging.printLog("", "in error");
        getBinding().transparentLayout.setVisibility(View.GONE);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBinding().swipeContainer.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.dark_gray_background));
        getBinding().swipeContainer.setColorSchemeColors(getResources().getColor(R.color.primary_blue));
    }

    private void setUIComponets(List<AssetCommonBean> assetCommonBeans) {
        try {
            if (adapter != null) {
                loadedList.add(assetCommonBeans.get(0));
                adapter.notifyItemChanged(counter);
                getBinding().myRecyclerView.scrollToPosition(mScrollY + 500);
            } else {
                loadedList.add(assetCommonBeans.get(0));
                if (channelList.size() > 3) {
                    if (loadedList.size() > 2) {
                        getBinding().transparentLayout.setVisibility(View.GONE);
                        swipeToRefreshCheck();
                        adapter = new CommonAdapter(getActivity(), loadedList, loadedList.get(0).getSlides(), this, this, this);
                        getBinding().myRecyclerView.setAdapter(adapter);

                    }
                } else {
                    getBinding().transparentLayout.setVisibility(View.GONE);
                    swipeToRefreshCheck();
                    adapter = new CommonAdapter(getActivity(), loadedList, loadedList.get(0).getSlides(), this, this, this);
                    getBinding().myRecyclerView.setAdapter(adapter);
                }


            }


        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

    }

    private void swipeToRefresh() {
        getBinding().swipeContainer.setOnRefreshListener(() -> {
            if (NetworkConnectivity.isOnline(getBaseActivity())) {
                if (swipeToRefresh == 1) {
                    swipeToRefresh = 2;
                    counter = 0;
                    counterValueApiFail = 0;
                    adapter = null;
                    crouseInjected = false;
                    loadedList.clear();
                    connectionObserver();
                } else {
                    swipeToRefreshCheck();
                }
            } else {
                swipeToRefreshCheck();
                ToastHandler.show(getBaseActivity().getResources().getString(R.string.no_internet_connection), getActivity().getApplicationContext());
            }
        });

        RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                        //End of list
                        if (mIsLoading) {
                            mIsLoading = false;
                            if (channelList != null && counter != channelList.size()) {

                                getBinding().noDataLayout.setVisibility(View.GONE);
                                getBinding().myRecyclerView.setVisibility(View.VISIBLE);
                                //getBinding().transparentLayout.setVisibility(View.GONE);
                                mScrollY += dy;

                            }
                        }
                    }
                }
            }
        };

        getBinding().myRecyclerView.addOnScrollListener(mScrollListener);


    }

    private void swipeToRefreshCheck() {
        if (getBinding().swipeContainer != null) {
            if (getBinding().swipeContainer.isRefreshing()) {
                getBinding().swipeContainer.setRefreshing(false);
                getBinding().transparentLayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        modelCall();
        viewModel.resetObject();

    }

    protected void onSameClick() {
        if (getBinding() != null && getBinding().myRecyclerView != null)
            getBinding().myRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void remove(Long assetID, int position, int continueWatchingIndex, int listSize) {

    }

    @Override
    public void heroItemClick(int position, RailCommonData railCommonData, AssetCommonBean commonData) {

    }

    @Override
    public void removeAdOnFailure(int position) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String name);
    }

    @Override
    public void onStart() {
        super.onStart();
//        if(viewModel instanceof  HomeFragmentViewModel){
//            HomeActivity homeActivity = (HomeActivity) getActivity();
//            homeActivity.setToHome();
//        }
    }

    private void setTabId() {

        if (viewModel instanceof HomeFragmentViewModel) {
            screenID = String.valueOf(AppConstants.TAB_FIRST);
            //screenID = "14";
        } else if (viewModel instanceof LiveTvViewModel) {
            screenID = String.valueOf(AppConstants.TAB_FOUR);
        } else if (viewModel instanceof VideoViewModel) {
            screenID = String.valueOf(AppConstants.TAB_SECOND);
        } else if (viewModel instanceof SportsViewModel) {
            screenID = String.valueOf(AppConstants.TAB_THIRD);
        } else {
            screenID = String.valueOf(AppConstants.TAB_FIRST);
        }
    }

}
