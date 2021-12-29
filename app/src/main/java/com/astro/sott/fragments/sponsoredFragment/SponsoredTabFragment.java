package com.astro.sott.fragments.sponsoredFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.activities.movieDescription.viewModel.MovieDescriptionViewModel;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.FragmentSponsoredTabBinding;
import com.astro.sott.fragments.ShowFragment.adapter.SeriesShowAdapter;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.SpacingItemDecoration;
import com.kaltura.client.types.Asset;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SponsoredTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SponsoredTabFragment extends BaseBindingFragment<FragmentSponsoredTabBinding> {
    private MovieDescriptionViewModel viewModel;
    private String channelId;
    private List<RailCommonData> railCommonDataList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SponsoredTabFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SponsoredTabFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SponsoredTabFragment newInstance(String param1, String param2) {
        SponsoredTabFragment fragment = new SponsoredTabFragment();
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
        modelCall();


    }

    @Override
    protected FragmentSponsoredTabBinding inflateBindingLayout(@NonNull @NotNull LayoutInflater inflater) {
        return FragmentSponsoredTabBinding.inflate(inflater);
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null && getArguments().getString("COLLECTION_ID") != null)
            channelId = getArguments().getString("COLLECTION_ID");
        setRecycler();
        getSponsorData();
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(MovieDescriptionViewModel.class);

    }

    private void setRecycler() {
        getBinding().recyclerView.setNestedScrollingEnabled(false);
        getBinding().recyclerView.setHasFixedSize(false);
        getBinding().recyclerView.addItemDecoration(new SpacingItemDecoration(20, 2));
        GridLayoutManager mLayoutManager;
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        getBinding().recyclerView.setLayoutManager(mLayoutManager);
    }

    private void getSponsorData() {
        if (channelId != null) {
            viewModel.getSponsorChannelData(channelId).observe(getViewLifecycleOwner(), asset -> {
                if (asset != null && asset.size() > 0) {
                    getBinding().noDataLayout.setVisibility(View.GONE);
                    railCommonDataList = new ArrayList<>();
                    for (Asset assetData : asset) {
                        RailCommonData railCommonData = new RailCommonData();
                        railCommonData.setObject(assetData);
                        railCommonDataList.add(railCommonData);
                    }
                    SeriesShowAdapter showAdapter = new SeriesShowAdapter(getActivity(), railCommonDataList, true);
                    getBinding().recyclerView.setAdapter(showAdapter);
                } else {
                    getBinding().noDataLayout.setVisibility(View.VISIBLE);
                }
            });
        }

    }
}