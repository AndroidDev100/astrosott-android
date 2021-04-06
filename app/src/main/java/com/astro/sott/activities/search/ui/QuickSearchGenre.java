package com.astro.sott.activities.search.ui;

import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.activities.moreListing.ui.ListingActivity;
import com.astro.sott.activities.movieDescription.viewModel.MovieDescriptionViewModel;
import com.astro.sott.activities.search.adapter.QuickSearchGenreAdapter;
import com.astro.sott.activities.search.adapter.SearchKeywordAdapter;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DataLoadedOnFragment;
import com.astro.sott.callBacks.commonCallBacks.GenreSelectionCallBack;
import com.astro.sott.databinding.FragmentQuickSearchGenreBinding;
import com.astro.sott.db.search.SearchedKeywords;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.GridSpacingItemDecoration;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuickSearchGenre#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuickSearchGenre extends BaseBindingFragment<FragmentQuickSearchGenreBinding> {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DataLoadedOnFragment dataLoadedOnFragment;

    public QuickSearchGenre() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuickSearchGenre.
     */
    // TODO: Rename and change types and number of parameters
    public static QuickSearchGenre newInstance(String param1, String param2) {
        QuickSearchGenre fragment = new QuickSearchGenre();
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
        setViewModel();
        setClicks();
        UIinitialization();
        loadDataFromModel();
        try {
            if (getActivity() instanceof DataLoadedOnFragment){
                dataLoadedOnFragment=(DataLoadedOnFragment)getActivity();
            }
        }catch (Exception ignored){

        }
    }

    QuickSearchViewModel viewModel;
    private void setViewModel() {
        viewModel = ViewModelProviders.of(this).get(QuickSearchViewModel.class);
    }

    String selectedGenre="";
    private void loadDataFromModel() {
        getActivity().runOnUiThread(() -> viewModel.getGenreData(getActivity(),MediaTypeConstant.getFilterGenre(getActivity())).observe(getActivity(), commonResponse -> {
           //Log.w("genreResponse",commonResponse.toString());
            if (commonResponse!=null && commonResponse.size()>0 && commonResponse.get(0).getStatus()){
                dataLoadedOnFragment.isDataLoaded(true);
                Log.w("selectedColor--2","in"+"  "+commonResponse.size());
                QuickSearchGenreAdapter adapter = new QuickSearchGenreAdapter(QuickSearchGenre.this,commonResponse, new GenreSelectionCallBack() {
                    @Override
                    public void onClick(int position, List<RailCommonData> arrayList) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 0; i<arrayList.size(); i ++){
                            if (arrayList.get(i).isChecked()){
                                stringBuilder.append(AppLevelConstants.SEARCH_GENRE_CONSTATNT +arrayList.get(i).getName()+"'").append("  ");

                            }
                            if (stringBuilder.length() > 0) {
                                selectedGenre = stringBuilder.toString();
                                selectedGenre = selectedGenre.substring(0, selectedGenre.length() - 2);
                            } else {
                                selectedGenre = "";
                            }
                        }

                        Log.w("selectedGenres-:",selectedGenre);
                    }
                });
                getBinding().recyclerView.setAdapter(adapter);
                getBinding().recyclerView.setLayoutManager(gridLayoutManager);

                try {
                    List<SearchedKeywords> list=new ArrayList<>();
                    for (int i=0;i<commonResponse.size();i++){
                        SearchedKeywords searchedKeywords=new SearchedKeywords();
                        searchedKeywords.setKeyWords(commonResponse.get(i).getName());
                        searchedKeywords.setSelected(false);
                        list.add(searchedKeywords);
                    }
                    Gson gson = new Gson();
                    String userProfileData = gson.toJson(list);
                    KsPreferenceKey.getInstance(getActivity()).setUserProfileData(userProfileData);
                }catch (Exception ignored){

                }
            }else {
                dataLoadedOnFragment.isDataLoaded(false);
            }

        }));

        getBinding().recyclerView.setNestedScrollingEnabled(true);

       // getBinding().quickSearchBtn.setVisibility(View.VISIBLE);
    }
    GridLayoutManager gridLayoutManager;
    private void UIinitialization() {

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        int spanCount, spacing;
            if (tabletSize) {
                spanCount = AppLevelConstants.SPAN_COUNT_SQUARE_TAB;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppLevelConstants.PORTRAIT_SPACING_TAB, r.getDisplayMetrics());
            } else {
                spanCount = AppLevelConstants.SPAN_COUNT_LANDSCAPE;
                Resources r = getResources();
                spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppLevelConstants.PORTRAIT_SPACING, r.getDisplayMetrics());
            }

        gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        getBinding().recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));


    }
    private void setClicks() {
//        getBinding().toolbar.backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    protected FragmentQuickSearchGenreBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentQuickSearchGenreBinding.inflate(inflater);
    }


    public String getSelectedGenres() {
        return selectedGenre;
    }
}