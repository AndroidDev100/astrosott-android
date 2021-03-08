package com.astro.sott.activities.search.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.activities.search.adapter.QuickSearchGenreAdapter;
import com.astro.sott.activities.search.adapter.SearchKeywordAdapter;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.databinding.FragmentQuickSearchGenreBinding;

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
        setClicks();
        UIinitialization();
        loadDataFromModel();
    }
    private void loadDataFromModel() {
       QuickSearchGenreAdapter adapter = new QuickSearchGenreAdapter(QuickSearchGenre.this);
        getBinding().recyclerView.setAdapter(adapter);
        getBinding().recyclerView.setNestedScrollingEnabled(true);
       // getBinding().quickSearchBtn.setVisibility(View.VISIBLE);
    }

    private void UIinitialization() {
        getBinding().recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

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


}