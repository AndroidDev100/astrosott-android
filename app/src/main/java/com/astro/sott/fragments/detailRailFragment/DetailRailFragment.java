package com.astro.sott.fragments.detailRailFragment;


import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.FragmentDetailRailBinding;
import com.astro.sott.fragments.detailRailFragment.adapter.DetailPagerAdapter;
import com.astro.sott.fragments.trailerFragment.viewModel.TrailerFragmentViewModel;
import com.astro.sott.utils.TabsData;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.google.android.material.tabs.TabLayout;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.StringValue;
import com.kaltura.client.types.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailRailFragment extends BaseBindingFragment<FragmentDetailRailBinding> {

    private RailCommonData railCommonData;
    private List<Asset> trailerData;
    private List<Asset> highlightsData;

    private TrailerFragmentViewModel trailerFragmentViewModel;
    private int isTrailerCount = 0;
    private Asset asset;
    private List<Integer> seriesNumberList;
    private int trailerFragmentType = 0;
    private int seasonCounter = 0;
    int counter = 1;
    private String externalId = "";
    private int indicatorWidth;
    private Boolean indicatorflag = false;

    public DetailRailFragment() {

        // Required empty public constructor
    }


    @Override
    protected FragmentDetailRailBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentDetailRailBinding.inflate(inflater);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // viewPagerSetup();
    }

    private void modelCall() {
        trailerFragmentViewModel = ViewModelProviders.of(this).get(TrailerFragmentViewModel.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            railCommonData = getArguments().getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
            if (railCommonData != null)
                asset = railCommonData.getObject();
            modelCall();
            if (asset.getExternalId() != null)
                externalId = asset.getExternalId();
            if (asset.getType() == MediaTypeConstant.getSeries(getActivity())) {
                getSeasons();
            } else if (asset.getType() == MediaTypeConstant.getMovie(getActivity()) || asset.getType() == MediaTypeConstant.getCollection(getActivity())) {
                getRefId(asset.getType());
            }
        } catch (NullPointerException e) {

        }

    }

    private void getSeasons() {
        trailerFragmentViewModel.getSeasonsListData(asset.getId().intValue(), 1, asset.getType(), asset.getMetas(), AppConstants.Rail5, externalId).observe(getActivity(), integers -> {
            if (integers != null) {
                if (integers.size() > 0) {
                    seriesNumberList = integers;
                    trailerFragmentViewModel.setOpenSeriesData(null);
                    trailerFragmentViewModel.setSeasonList(integers);
                    getEpisode(seriesNumberList);
                } else {
                    trailerFragmentViewModel.setSeasonList(null);
                    trailerFragmentViewModel.setClosedSeriesData(null);
                    getOpenSeriesEpisodes();
                }
            } else {
                trailerFragmentViewModel.setSeasonList(null);
                trailerFragmentViewModel.setClosedSeriesData(null);
                getOpenSeriesEpisodes();

            }

        });


    }

    private void getOpenSeriesEpisodes() {
        trailerFragmentViewModel.callEpisodes(asset, asset.getType(), counter, seasonCounter, AppConstants.Rail5, AppLevelConstants.KEY_EPISODE_NUMBER, this).observe(this, assetCommonBeans -> {
            if (assetCommonBeans.get(0).getStatus()) {
                trailerFragmentViewModel.setOpenSeriesData(assetCommonBeans);
                isTrailerCount = 1;
                getRefId(asset.getType());
                trailerFragmentType = 1;

            } else {
                trailerFragmentViewModel.setOpenSeriesData(null);
                getRefId(asset.getType());

            }
        });
    }

    private void getEpisode(List<Integer> seriesNumberList) {
        trailerFragmentViewModel.callSeasonEpisodes(asset, asset.getType(), counter, seriesNumberList, seasonCounter, AppConstants.Rail5, AppLevelConstants.KEY_EPISODE_NUMBER, this).observe(this, assetCommonBeans -> {
            if (assetCommonBeans.get(0).getStatus()) {
                trailerFragmentViewModel.setClosedSeriesData(assetCommonBeans);
                isTrailerCount = 1;
                getRefId(asset.getType());
                trailerFragmentType = 1;

            } else {
                trailerFragmentViewModel.setClosedSeriesData(null);
                getRefId(asset.getType());

            }
        });
    }


    private void getRefId(final int type) {
        if (!TextUtils.isEmpty(externalId)) {
            getTrailer(externalId, type);
        }
    }

    private void getTrailer(String ref_id, int assetType) {
        trailerFragmentViewModel.getTrailer(ref_id, assetType).observe(this, assetList -> {
            if (assetList != null) {
                if (assetList.size() > 0) {
                    getTrailerAndHighlights(assetList);
                    if (asset.getType() == MediaTypeConstant.getSeries(getActivity())) {
                        isTrailerCount = 2;
                    } else {
                        isTrailerCount = 1;
                    }
                    callYouMayAlsoLike();
                } else {
                    callYouMayAlsoLike();
                }

            } else {
                callYouMayAlsoLike();

            }

        });
    }

    private void callYouMayAlsoLike() {
        long assetId = asset.getId();
        trailerFragmentViewModel.setYouMayAlsoLikeData(null);

        trailerFragmentViewModel.getYouMayAlsoLike((int) assetId, 1, asset.getType(), asset.getTags()).observe(this, assetCommonBeans -> {
            try {
                if (assetCommonBeans.size() > 0) {
                    if (assetCommonBeans.get(0).getStatus()) {
                        trailerFragmentViewModel.setYouMayAlsoLikeData(assetCommonBeans.get(0).getRailAssetList());
                        if (asset.getType() == MediaTypeConstant.getSeries(getActivity())) {
                            if (trailerFragmentType == 1) {
                                trailerFragmentType = 4;
                                isTrailerCount = 2;
                            } else if (trailerFragmentType == 2) {
                                trailerFragmentType = 5;
                                isTrailerCount = 2;
                            } else if (trailerFragmentType == 0) {
                                trailerFragmentType = 7;
                                isTrailerCount = 1;
                            } else {
                                isTrailerCount = 3;
                                trailerFragmentType = 6;
                            }

                        } else {
                            if (trailerFragmentType == 0) {
                                isTrailerCount = 1;
                            } else {
                                isTrailerCount = 2;
                            }
                        }
                        viewPagerSetup();
                    } else {
                        viewPagerSetup();
                    }
                } else {
                    viewPagerSetup();
                }
            } catch (Exception e) {
            }


        });
    }

    private void getTrailerAndHighlights(List<Asset> assetList) {
        trailerData = new ArrayList<>();
        highlightsData = new ArrayList<>();
        for (Asset assetItem : assetList) {
            if (assetItem.getType() == MediaTypeConstant.getTrailer(getActivity())) {
                trailerData.add(assetItem);
            } else if (assetItem.getType() == MediaTypeConstant.getHighlight(getActivity())) {
                highlightsData.add(assetItem);
            }
        }
        trailerFragmentViewModel.setTrailerData(trailerData);
        trailerFragmentViewModel.setHighLightsData(highlightsData);
        if (asset.getType() == MediaTypeConstant.getMovie(getActivity())) {
            if (trailerData.size() > 0) {
                trailerFragmentType = 1;
            }
            if (highlightsData.size() > 0) {
                if (trailerFragmentType == 1) {
                    trailerFragmentType = 2;
                } else {
                    trailerFragmentType = 3;
                }

            }
        } else {
            if (trailerFragmentType == 1) {
                trailerFragmentType = 3;
            } else {
                trailerFragmentType = 2;
            }
        }
    }


    private void viewPagerSetup() {
        try {
            if (isTrailerCount == 1) {
                ViewGroup.LayoutParams params = getBinding().tabLayout.getLayoutParams();
                params.width = (int) 650;
                getBinding().tabLayout.setLayoutParams(params);
            }

            DetailPagerAdapter detailPagerAdapter = new DetailPagerAdapter(getChildFragmentManager(), getActivity(), railCommonData, isTrailerCount, trailerFragmentType);
            getBinding().pager.setAdapter(detailPagerAdapter);
            getBinding().pager.disableScroll(true);
            Log.e("TrailerCount",isTrailerCount+"");

              if ((isTrailerCount > 0)) {

                  getBinding().tabLayout.setupWithViewPager(getBinding().pager);

                  getBinding().tabLayout.post(new Runnable() {
                      @Override
                      public void run() {
                          if((getBinding().tabLayout.getTabCount() > 0 ) ){
                              indicatorWidth = getBinding().tabLayout.getWidth() / getBinding().tabLayout.getTabCount();
                          }
                          Log.d("TabCount", getBinding().tabLayout.getTabCount() + "");

                          Log.d("tabLayout", getBinding().tabLayout.getWidth() + "");
                          Log.d("indicator", indicatorWidth + "");
                          //Assign new width
                          RelativeLayout.LayoutParams indicatorParams = (RelativeLayout.LayoutParams) getBinding().indicator.getLayoutParams();
                          indicatorParams.width = indicatorWidth;
                          getBinding().indicator.setLayoutParams(indicatorParams);
                      }
                  });
                  getBinding().pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                      @Override
                      public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                              RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getBinding().indicator.getLayoutParams();
                              //Multiply positionOffset with indicatorWidth to get translation
                              float translationOffset = (positionOffset + i) * (indicatorWidth);
                              params.leftMargin = (int) translationOffset;
                              getBinding().indicator.setLayoutParams(params);

                      }

                      @Override
                      public void onPageSelected(int i) {

                      getBinding().pager.reMeasureCurrentPage(i);
                      }

                      @Override
                      public void onPageScrollStateChanged(int i) {


                      }
                  });
                  getBinding().indicator.setVisibility(View.VISIBLE);

                  getBinding().tabLayout.setVisibility(View.VISIBLE);
              }
        } catch (ArithmeticException e) {
                 Log.d("TAG",e+"");
        }
    }
}
