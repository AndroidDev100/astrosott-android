package com.astro.sott.fragments.detailRailFragment.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astro.sott.R;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.FragmentBoxSetDetailBinding;
import com.astro.sott.fragments.detailRailFragment.adapter.DetailPagerAdapter;
import com.astro.sott.fragments.trailerFragment.viewModel.TrailerFragmentViewModel;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoxSetDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoxSetDetailFragment extends BaseBindingFragment<FragmentBoxSetDetailBinding> {
    private TrailerFragmentViewModel trailerFragmentViewModel;
    private RailCommonData railCommonData;
    private List<Asset> trailerData;
    private int isTrailerCount = 0;
    private int indicatorWidth;

    private List<Asset> highlightsData;
    private int trailerFragmentType = 0;

    private String externalId = "";
    private Asset asset;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BoxSetDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BoxSetDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BoxSetDetailFragment newInstance(String param1, String param2) {
        BoxSetDetailFragment fragment = new BoxSetDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        railCommonData = getArguments().getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
        if (railCommonData != null)
            asset = railCommonData.getObject();
        modelCall();
        externalId = asset.getExternalId();
        getRefId(asset.getType());
    }

    private void getRefId(final int type) {
        if (!TextUtils.isEmpty(externalId)) {
            getMovie(externalId, type);
        } else {
            callYouMayAlsoLike();
        }
    }

    private void getMovie(String ref_id, int assetType) {
        trailerFragmentViewModel.setMovieShows(null);
        trailerFragmentViewModel.getShows(ref_id, MediaTypeConstant.getMovie(getActivity())).observe(this, assetList -> {
            if (assetList != null) {
                if (assetList.size() > 0) {
                    isTrailerCount = 1;
                    trailerFragmentViewModel.setMovieShows(assetList);
                    getTrailer(externalId, assetType);

                } else {
                    getSeries(ref_id, assetType);
                }
            } else {
                getSeries(ref_id, assetType);
            }

        });
    }

    private void getSeries(String ref_id, int assetType) {
        trailerFragmentViewModel.setSeriesShows(null);
        trailerFragmentViewModel.getShows(ref_id, MediaTypeConstant.getMovie(getActivity())).observe(this, assetList -> {
            if (assetList != null) {
                if (assetList.size() > 0) {
                    isTrailerCount = 1;
                    trailerFragmentViewModel.setSeriesShows(assetList);
                    getTrailer(externalId, assetType);
                } else {
                    getTrailer(externalId, assetType);
                }

            } else {
                getTrailer(externalId, assetType);
            }

        });
    }


    private void getTrailer(String ref_id, int assetType) {
        trailerFragmentViewModel.getTrailer(ref_id, assetType).observe(this, assetList -> {
            if (assetList != null) {
                if (assetList.size() > 0) {
                    getTrailerAndHighlights(assetList);
                    isTrailerCount++;
                    callYouMayAlsoLike();
                } else {
                    callYouMayAlsoLike();
                }

            } else {
                callYouMayAlsoLike();

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
        if (asset.getType() == MediaTypeConstant.getCollection(getActivity())) {
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
        }
    }

    private void callYouMayAlsoLike() {
        long assetId = asset.getId();
        trailerFragmentViewModel.setYouMayAlsoLikeData(null);

        trailerFragmentViewModel.getYouMayAlsoLike((int) assetId, 1, asset.getType(), asset.getTags()).observe(this, assetCommonBeans -> {
            try {
                if (assetCommonBeans.size() > 0) {
                    if (assetCommonBeans.get(0).getStatus()) {
                        trailerFragmentViewModel.setYouMayAlsoLikeData(assetCommonBeans.get(0).getRailAssetList());
                        isTrailerCount++;
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

    private void viewPagerSetup() {
        try {
            if (isTrailerCount == 1) {
                ViewGroup.LayoutParams params = getBinding().tabLayout.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                getBinding().tabLayout.setLayoutParams(params);
            }

            DetailPagerAdapter detailPagerAdapter = new DetailPagerAdapter(getChildFragmentManager(), getActivity(), railCommonData, isTrailerCount, trailerFragmentType);
            getBinding().pager.setAdapter(detailPagerAdapter);
            getBinding().pager.disableScroll(true);
            Log.e("TrailerCount", isTrailerCount + "");

            if ((isTrailerCount > 0)) {

                getBinding().tabLayout.setupWithViewPager(getBinding().pager);

                getBinding().tabLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        if ((getBinding().tabLayout.getTabCount() > 0)) {
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
                changeTabsFont();
                getBinding().indicator.setVisibility(View.VISIBLE);
                getBinding().blackLine.setVisibility(View.VISIBLE);

                getBinding().tabLayout.setVisibility(View.VISIBLE);
            }
        } catch (ArithmeticException e) {
            Log.d("TAG", e + "");
        }
    }

    private void changeTabsFont() {
        //  Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/"+ Constants.FontStyle);
        ViewGroup vg = (ViewGroup) getBinding().tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setSingleLine();
                }
            }
        }
    }

    private void modelCall() {
        trailerFragmentViewModel = ViewModelProviders.of(this).get(TrailerFragmentViewModel.class);
    }

    @Override
    protected FragmentBoxSetDetailBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentBoxSetDetailBinding.inflate(inflater);
    }


}