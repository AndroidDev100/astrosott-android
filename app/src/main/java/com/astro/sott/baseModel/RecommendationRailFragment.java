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
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.StringUtils;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecommendationRailFragment extends BaseBindingFragment<DetailFooterFragmentBinding> implements ContinueWatchingRemove, RemoveAdsCallBack, DetailRailClick, HeroItemClickListner {
    HomeFragmentViewModel viewModel;
    int tabId;
    int mediaType;
    Asset asset;
    List<VIUChannel> dtChannelsList;
    List<VIUChannel> channelList;
    int counterValueApiFail = 0;
    int continueWatchingIndex = -1;
    MovieDescriptionCommonAdapter adapter;
    List<AssetCommonBean> loadedList;
    int counter = 0;
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
        counter = 0;
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
            if (context instanceof WebSeriesDescriptionActivity) {
                getBaseCategoryRail();
            } else
                youMayAlsoLike();
        }
    }

    private void UIinitialization() {
        setRecyclerProperties(getBinding().recyclerView);
    }

    public void setRecyclerProperties(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void getBaseCategoryRail() {
        HomeFragmentRepository.getInstance().getCategories(String.valueOf(tabId)).observe(getActivity(), baseCategoriesList -> {
            if (baseCategoriesList.size() > 0) {
                dtChannelsList = new ArrayList<>();
                for (int j = 0; j < baseCategoriesList.size(); j++) {
                    VIUChannel tempChannel = new VIUChannel(getActivity(), baseCategoriesList.get(j));
                    dtChannelsList.add(tempChannel);
                }
                channelList = dtChannelsList;
                counter = 0;
                callRailAPI(dtChannelsList);
            } else {
                // tabsVisibility(true);
                PrintLogging.printLog("", "in error");
            }
        });
    }

    /*private void tabsVisibility(boolean removetab) {
        try {
            if (context instanceof WebSeriesDescriptionActivity) {
                if (removetab)
                    ((WebSeriesDescriptionActivity) context).removeTab(2, context.getResources().getString(R.string.more_like_this));
                ((WebSeriesDescriptionActivity) context).isRecommended = true;
                ((WebSeriesDescriptionActivity) context).showTabs();
            } else if (context instanceof MovieDescriptionActivity) {
                if (removetab)
                    ((MovieDescriptionActivity) context).removeTab(1, context.getResources().getString(R.string.more_like_this));
                ((MovieDescriptionActivity) context).isRecommended = true;
                ((MovieDescriptionActivity) context).showTabs();
            } else if (context instanceof ShortFilmActivity) {
                if (removetab)
                    ((ShortFilmActivity) context).removeTab(1, context.getResources().getString(R.string.more_like_this));
                ((ShortFilmActivity) context).isRecommended = true;
                ((ShortFilmActivity) context).showTabs();
            }
        } catch (Exception e) {
        }
    }*/

    private void youMayAlsoLike() {
        assetId = asset.getId();
        map = asset.getTags();
        assetType = asset.getType();
        dtChannelsList = new ArrayList<>();
        if (channelList != null)
            channelList.clear();

        if (dtChannelsList != null)
            dtChannelsList.clear();
        /*viewModel.getChannelList(tabId).observe(getActivity(), assetCommonBean -> {
            if (assetCommonBean.getStatus()) {
                PrintLogging.printLog("RecommendationRailFragment", "channelList--" + assetCommonBean.getChannelList().size());
                if (dtChannelsList != null && dtChannelsList.size() > 0)
                    dtChannelsList.clear();

                dtChannelsList = assetCommonBean.getDTChannelList();
                callYouMayAlsoLike((int) assetId, 0, assetType, map, layoutType, tabId, asset);

            } else {
                if (dtChannelsList != null && dtChannelsList.size() > 0)
                    dtChannelsList.clear();
                PrintLogging.printLog("RecommendationRailFragment", "in error");
            }

        });*/

        callYouMayAlsoLike((int) assetId, 1, assetType, map, layoutType, tabId, asset);


    }

    private void callYouMayAlsoLike(int assetId, int counter, int assetType, Map<String, MultilingualStringValueArray> map, int layoutType, int screen_id, Asset asset) {
        viewModel.getYouMayAlsoLike(assetId, counter, assetType, map, layoutType, screen_id, asset).observe(this, assetCommonBeans -> {
            try {
                PrintLogging.printLog("RecommendationRailFragment", "similarMovieListing-->>" + assetCommonBeans.size() + " " + assetCommonBeans.get(0).getStatus());
                if (assetCommonBeans.size() > 0) {
                    if (assetCommonBeans.get(0).getStatus()) {
                        channelList = dtChannelsList;
                        //  setUIComponets(assetCommonBeans);

                        setSimilarUIComponent(assetCommonBeans);
                        // getBaseCategoryRail();
                    } else {
                        getBinding().upcoming.setVisibility(View.VISIBLE);
                      //  getBaseCategoryRail();
                    }
                } else {
                    getBinding().upcoming.setVisibility(View.VISIBLE);

                    //   getBaseCategoryRail();
                }
            } catch (Exception e) {
                getBinding().upcoming.setVisibility(View.VISIBLE);

                //  getBaseCategoryRail();
            }


        });

    }

    private void setSimilarUIComponent(List<AssetCommonBean> assetCommonBeans) {
        SimilarAdapter similarAdapter = new SimilarAdapter(assetCommonBeans.get(0).getRailAssetList());
        getBinding().recyclerView.setAdapter(similarAdapter);

    }

    private void callRailAPI(List<VIUChannel> list) {
        // tabsVisibility(false);
        channelList = list;
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
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {
       /* if (context instanceof MovieDescriptionActivity) {
            ((MovieDescriptionActivity) context).detailRailItemClick(commonData);
        } else if (context instanceof ShortFilmActivity) {
            ((ShortFilmActivity) context).detailRailItemClick(commonData);
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        HomeFragmentRepository.getInstance().resetObject();
        // if (viewModel != null)
        // viewModel.resetViewModel();
    }

    @Override
    public void heroItemClick(int position, RailCommonData railCommonData, AssetCommonBean commonData) {
        //heroClickRedirection(railCommonData.getRailDetail(),railCommonData,commonData);
        /*HeroDiversion heroDiversion;

        if (context instanceof WebSeriesDescriptionActivity) {
            heroDiversion = new HeroDiversion(railCommonData, commonData, (WebSeriesDescriptionActivity) context, (WebSeriesDescriptionActivity) context, ViewModelProviders.of(this).get(HomeViewModel.class));
            heroDiversion.heroClickRedirection();
        } else if (context instanceof MovieDescriptionActivity) {
            heroDiversion = new HeroDiversion(railCommonData, commonData, (MovieDescriptionActivity) context, (MovieDescriptionActivity) context, ViewModelProviders.of(this).get(HomeViewModel.class));
            heroDiversion.heroClickRedirection();
        } else if (context instanceof ShortFilmActivity) {
            heroDiversion = new HeroDiversion(railCommonData, commonData, (ShortFilmActivity) context, (ShortFilmActivity) context, ViewModelProviders.of(this).get(HomeViewModel.class));
            heroDiversion.heroClickRedirection();
        }*/


    }


}
