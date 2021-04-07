package com.astro.sott.fragments.ShowFragment.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;

import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentShowBinding;
import com.astro.sott.fragments.ShowFragment.adapter.SeriesShowAdapter;
import com.astro.sott.fragments.ShowFragment.adapter.MovieShowsAdapter;
import com.astro.sott.utils.TabsData;
import com.astro.sott.utils.helpers.SpacingItemDecoration;
import com.kaltura.client.types.Asset;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowFragment extends BaseBindingFragment<FragmentShowBinding> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkData();
    }

    private void checkData() {
        if (TabsData.getInstance().getMovieShows() != null && TabsData.getInstance().getMovieShows().size() > 0) {
            setRecycler(true);
            setUiComponent(TabsData.getInstance().getMovieShows(), true);

        } else if (TabsData.getInstance().getSeriesShows() != null && TabsData.getInstance().getMovieShows().size() > 0) {
            setRecycler(false);
            setUiComponent(TabsData.getInstance().getSeriesShows(), false);
        }
    }

    private void setRecycler(boolean isMovieShow) {
        getBinding().recyclerView.setNestedScrollingEnabled(false);
        getBinding().recyclerView.setHasFixedSize(false);
        getBinding().recyclerView.addItemDecoration(new SpacingItemDecoration(20, 2));
        GridLayoutManager mLayoutManager;
        if (!isMovieShow) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        }
        getBinding().recyclerView.setLayoutManager(mLayoutManager);
    }

    private void setUiComponent(List<Asset> shows, boolean isMovieShow) {
        if (!isMovieShow) {
            SeriesShowAdapter showAdapter = new SeriesShowAdapter(getActivity(), shows, isMovieShow);
            getBinding().recyclerView.setAdapter(showAdapter);
        } else {
            MovieShowsAdapter seriesShowAdapter = new MovieShowsAdapter(getActivity(), shows);
            getBinding().recyclerView.setAdapter(seriesShowAdapter);
        }


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShowFragment newInstance(String param1, String param2) {
        ShowFragment fragment = new ShowFragment();
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
    protected FragmentShowBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentShowBinding.inflate(inflater);
    }


}