package com.dialog.dialoggo.fragments.detailRailFragment;


import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;

import com.dialog.dialoggo.baseModel.BaseBindingFragment;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.databinding.FragmentDetailRailBinding;
import com.dialog.dialoggo.fragments.detailRailFragment.adapter.DetailPagerAdapter;
import com.dialog.dialoggo.fragments.trailerFragment.viewModel.TrailerFragmentViewModel;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailRailFragment extends BaseBindingFragment<FragmentDetailRailBinding> {

    private RailCommonData railCommonData;
    private TrailerFragmentViewModel trailerFragmentViewModel;
    private int isTrailerCount = 1;
    private Asset asset;


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
            getRefId(asset.getType(), asset.getTags());
        }catch (NullPointerException e){

        }

    }

    private void getRefId(final int type, Map<String, MultilingualStringValueArray> map) {



        trailerFragmentViewModel.getRefIdLivedata(map).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String ref_id) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewPagerSetup();
                    }
                },1000);


                if (!TextUtils.isEmpty(ref_id)) {
                    getTrailer(ref_id, type);
                }
            }
        });
    }

    private void getTrailer(String ref_id, int assetType) {
        trailerFragmentViewModel.getTrailer(ref_id, assetType).observe(this, assetList -> {

            if (assetList != null) {
                if (assetList.size() > 0) {
                    isTrailerCount = 2;
                } else {
                    isTrailerCount = 1;
                }

            } else {
                isTrailerCount = 1;
            }

        });
    }

    private void viewPagerSetup() {
        try {
            DetailPagerAdapter detailPagerAdapter = new DetailPagerAdapter(getChildFragmentManager(), getActivity(), railCommonData, isTrailerCount);
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
        }catch (IllegalStateException e){

        }
    }
}
