package com.astro.sott.fragments.moreLikeThisFragment;


import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.activities.movieDescription.viewModel.MovieDescriptionViewModel;
import com.astro.sott.activities.webSeriesDescription.viewModel.WebSeriesDescriptionViewModel;
import com.astro.sott.baseModel.RailBaseFragment;
import com.astro.sott.baseModel.RecommendationRailFragment;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.R;
import com.astro.sott.activities.webEpisodeDescription.adapter.WebEpisodeDescriptionCommonAdapter;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.MoreLikeThis;
import com.astro.sott.databinding.FragmentMoreLikeThisBinding;
import com.astro.sott.utils.helpers.shimmer.Constants;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreLikeThisFragment extends BaseBindingFragment<FragmentMoreLikeThisBinding> {

    private final List<AssetCommonBean> loadedList = new ArrayList<>();
    private RailCommonData railCommonData;
    private WebSeriesDescriptionViewModel viewModel;
    private List<VIUChannel> dtChannelsList;
    private List<Integer> seriesNumberList;
    private List<AssetCommonBean> clipList;
    private int counter = 0;
    private WebEpisodeDescriptionCommonAdapter adapter = null;
    private int seasonCounter = 0;
    private boolean episodeTested = false;
    private Map<String, MultilingualStringValueArray> map;
    private List<VIUChannel> channelList;
    private int layoutType;
    private long assetId;
    private Handler handler;
    private Runnable runnable;
    private int tempCount = 0;

    private Asset asset;
    MoreLikeThis moreLikeThis;

    public MoreLikeThisFragment() {
        // Required empty public constructor
    }


    @Override
    protected FragmentMoreLikeThisBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentMoreLikeThisBinding.inflate(inflater);
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(WebSeriesDescriptionViewModel.class);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            this.moreLikeThis = (MoreLikeThis) getActivity();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        modelCall();
        railCommonData = getArguments().getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
        layoutType = AppLevelConstants.Rail3;
        if (railCommonData != null && railCommonData.getObject() != null)
            asset = railCommonData.getObject();
        map = asset.getTags();
        assetId = asset.getId();
        setRecyclerProperty();

        if (asset.getType() == MediaTypeConstant.getDrama(getActivity())) {
            getBinding().myRecyclerView.setVisibility(View.VISIBLE);
            loadDataFromModel();
        } else if (asset.getType() == MediaTypeConstant.getMovie(getActivity()) || asset.getType() == MediaTypeConstant.getShortFilm(getActivity())) {
            setRailFragment();

        }
    }

    private void setRecyclerProperty() {

        getBinding().myRecyclerView.hasFixedSize();
        getBinding().myRecyclerView.setNestedScrollingEnabled(false);
        getBinding().myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
    }

    private void setRailFragment() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        handler = new Handler();

        RecommendationRailFragment railBaseFragment = new RecommendationRailFragment();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (railBaseFragment != null) {
                    // railBaseFragment.setViewModel(MovieDescriptionViewModel.class);

                    Bundle args = new Bundle();
                    args.putInt("BUNDLE_TAB_ID", 0);
                    args.putInt("MEDIA_TYPE", asset.getType());
                    args.putInt("LAYOUT_TYPE", layoutType);
                    args.putParcelable("ASSET_OBJ", asset);
                    railBaseFragment.setArguments(args);
                    manager.beginTransaction().add(R.id.rail_fragment1, railBaseFragment).commit();

                }

            }
        };
        handler.postDelayed(runnable, 1000);
    }

    private void loadDataFromModel() {

        if (asset.getType() == MediaTypeConstant.getDrama(getActivity())) {
            viewModel.getChannelList(AppLevelConstants.TAB_DRAMA_DETAILS).observe(this, assetCommonBean -> {
                if (assetCommonBean != null && assetCommonBean.getStatus()) {
                    dtChannelsList = assetCommonBean.getDTChannelList();
                    clipList = new ArrayList<>();
                    if (Constants.assetType == MediaTypeConstant.getClip()) {
                        viewModel.getClipData(Constants.assetId, Constants.counter, Constants.assetType, map, layoutType, asset.getType()).observe(this, assetCommonBeans -> clipList = assetCommonBeans);
                    }

                    viewModel.getSeasonsListData(Constants.assetId, Constants.counter, Constants.assetType, asset.getMetas(), layoutType, asset.getType()).observe(this, integers -> {
                        if (integers != null && integers.size() > 0) {

                            seriesNumberList = integers;
                            callSeasonEpisodes(seriesNumberList);
                        } else {

                            moreLikeThis.moreLikeThisClicked(null);
                            episodeTested = true;
                            callCategoryRailAPI(dtChannelsList);
                        }


                    });
                } else {
                    viewModel.getSeasonsListData(Constants.assetId, Constants.counter, Constants.assetType, asset.getMetas(), layoutType, asset.getType()).observe(this, integers -> {
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

    @Override
    public void onDetach() {
        super.onDetach();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }


    private void callSeasonEpisodes(List<Integer> seriesNumberList) {
        if (seasonCounter != seriesNumberList.size()) {
            viewModel.callSeasonEpisodes(asset.getMetas(), Constants.assetType, 1, seriesNumberList, seasonCounter, layoutType).observe(this, assetCommonBeans -> {
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
            moreLikeThis.moreLikeThisClicked(null);
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

    private void checkEpisode(final List<AssetCommonBean> assetCommonBeanList) {
        episodeTested = true;
        if (assetCommonBeanList.size() > 0) {
            int type = assetCommonBeanList.get(0).getRailAssetList().get(0).getType();
            if (type == MediaTypeConstant.getWebEpisode(getActivity())) {
                PrintLogging.printLog(this.getClass(), "type", "");
            }
        }
        Handler mHandler = new Handler();
        RailCommonData railCommonData = assetCommonBeanList.get(0).getRailAssetList().get(0);


        moreLikeThis.moreLikeThisClicked(railCommonData);

        //   fdgdgdfgdfgdg
       /* mHandler.postDelayed(() -> {
            getBinding().ivPlayIcon.setClickable(true);
            getBinding().ivPlayIcon.setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                if (NetworkConnectivity.isOnline(getActivity())) {
                    checkErrors(railCommonData);
                } else {
                    ToastHandler.show(getResources().getString(R.string.no_internet_connection), WebSeriesDescriptionActivity.this);
                }
            });

        }, 2000);

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
        playerChecks(railCommonData);*/
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
                adapter = new WebEpisodeDescriptionCommonAdapter(getActivity(), loadedList);
                getBinding().myRecyclerView.setAdapter(adapter);
            }

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }


    }
}
