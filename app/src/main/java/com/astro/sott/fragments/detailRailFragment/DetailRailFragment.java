package com.astro.sott.fragments.detailRailFragment;


import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;

import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.FragmentDetailRailBinding;
import com.astro.sott.fragments.detailRailFragment.adapter.DetailPagerAdapter;
import com.astro.sott.fragments.trailerFragment.viewModel.TrailerFragmentViewModel;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.StringValue;
import com.kaltura.client.types.Value;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailRailFragment extends BaseBindingFragment<FragmentDetailRailBinding> {

    private RailCommonData railCommonData;
    private TrailerFragmentViewModel trailerFragmentViewModel;
    private int isTrailerCount = 1;
    private Asset asset;
    private List<Integer> seriesNumberList;
    private int trailerFragmentType = 0;
    private int seasonCounter = 0;
    int counter = 1;
    private String externalId = "";


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
            if (asset.getType() == MediaTypeConstant.getSeries(getActivity())) {
                getSeasons();
            } else if (asset.getType() == MediaTypeConstant.getMovie(getActivity())) {
                if (asset.getExternalId() != null)
                    externalId = asset.getExternalId();
                getRefId(asset.getType(), asset.getMetas());
            }
        } catch (NullPointerException e) {

        }

    }

    private void getSeasons() {
        trailerFragmentViewModel.getSeasonsListData(asset.getId().intValue(), 1, asset.getType(), asset.getMetas(), AppConstants.Rail5, asset.getType()).observe(getActivity(), integers -> {
            if (integers != null) {
                if (integers.size() > 0) {
                    seriesNumberList = integers;
                    getEpisode(seriesNumberList);
                } else {
                    isTrailerCount = 1;
                    viewPagerSetup();

                }
            } else {
                isTrailerCount = 1;
                viewPagerSetup();


            }

        });


    }

    private void getEpisode(List<Integer> seriesNumberList) {
        trailerFragmentViewModel.callSeasonEpisodes(asset.getMetas(), asset.getType(), counter, seriesNumberList, seasonCounter, AppConstants.Rail5).observe(this, assetCommonBeans -> {
            if (assetCommonBeans.get(0).getStatus()) {
                isTrailerCount = 2;
                viewPagerSetup();

            } else {
                isTrailerCount = 1;
                viewPagerSetup();
            }
        });
    }

    private String externalRefId;

    private void getRefId(final int type, Map<String, Value> map) {
        StringValue refValue = null;
        if (map != null) {
            refValue = (StringValue) map.get(AppLevelConstants.KEY_EXTERNAL_REF_ID);
        }
        if (refValue != null)
            externalRefId = refValue.getValue();

        if (!TextUtils.isEmpty(externalId)) {
            getTrailer(externalId, type);
        }
    }

    private void getTrailer(String ref_id, int assetType) {
        trailerFragmentViewModel.getTrailer(ref_id, assetType).observe(this, assetList -> {

            if (assetList != null) {
                if (assetList.size() > 0) {
                    isTrailerCount = 2;
                    trailerFragmentType = 1;
                    getHighlight(assetType);
                } else {
                    isTrailerCount = 1;
                    getHighlight(assetType);

                }

            } else {
                isTrailerCount = 1;
                getHighlight(assetType);

            }

        });
    }

    private void getHighlight(int assetType) {

        trailerFragmentViewModel.getHighlight(externalRefId, assetType).observe(this, assetList -> {
            if (assetList != null) {
                if (assetList.size() > 0) {
                    isTrailerCount = 2;
                    if (trailerFragmentType == 1) {
                        trailerFragmentType = 2;
                    } else {
                        trailerFragmentType = 3;
                    }
                    viewPagerSetup();
                } else {
                    viewPagerSetup();
                }

            } else {
                viewPagerSetup();
            }

        });
    }


    private void viewPagerSetup() {
        try {
            DetailPagerAdapter detailPagerAdapter = new DetailPagerAdapter(getChildFragmentManager(), getActivity(), railCommonData, isTrailerCount, trailerFragmentType);
            getBinding().pager.setAdapter(detailPagerAdapter);
            getBinding().pager.disableScroll(true);
            getBinding().tabLayout.setupWithViewPager(getBinding().pager);
            getBinding().pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    getBinding().pager.reMeasureCurrentPage(i);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });
        } catch (IllegalStateException e) {

        }
    }
}
