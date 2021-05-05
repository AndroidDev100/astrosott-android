package com.astro.sott.activities.search.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.activities.contentPreference.ui.HorizontalSpaceItemDecoration;
import com.astro.sott.activities.contentPreference.ui.VerticalSpaceItemDecoration;
import com.astro.sott.activities.search.ui.SearchKeywordActivity;
import com.astro.sott.databinding.GenreRecyclerItemBinding;
import com.astro.sott.databinding.LandscapeRecyclerItemBinding;
import com.astro.sott.databinding.LanguageRecyclerItemBinding;
import com.astro.sott.db.search.SearchedKeywords;
import com.astro.sott.modelClasses.dmsResponse.FilterLanguages;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.ArrayList;
import java.util.List;

import static com.astro.sott.utils.constants.AppConstants.TYPE_GENRE;
import static com.astro.sott.utils.constants.AppConstants.TYPE_LANGUAGE;

public class SearchKeywordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VERTICAL_ITEM_SPACE = 30;
    private static final int HORIONTAL_ITEM_SPACE = 10;
    private Activity context;
    private List<SearchedKeywords> searchedKeywordsList;
    ArrayList<FilterLanguages> filterLanguageList;

    public SearchKeywordAdapter(SearchKeywordActivity ctx, List<SearchedKeywords> searchedKeywords, ArrayList<FilterLanguages> filterLangList) {
        this.context= ctx;
        this.searchedKeywordsList=searchedKeywords;
        this.filterLanguageList=filterLangList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LANGUAGE:
                LanguageRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.language_recycler_item, parent, false);
                LanguageHolder langageHolder = new LanguageHolder(binding);
                setRecyclerProperties(binding.recyclerView);
                return langageHolder;
            case TYPE_GENRE:
            GenreRecyclerItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.genre_recycler_item, parent, false);
            GenreHolder genreHolder = new  GenreHolder(itemBinding);
            setRecyclerProperties(itemBinding.recyclerView);
                return genreHolder;
            default:
                itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.genre_recycler_item, parent, false);
                genreHolder = new GenreHolder(itemBinding);
                setRecyclerProperties(itemBinding.recyclerView);
                return genreHolder;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
            return TYPE_LANGUAGE;
        else
            return TYPE_GENRE;
    }

    private void setRecyclerProperties(RecyclerView recyclerView) {
        recyclerView.hasFixedSize();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
//      staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,1);
//      getBinding().recyclerview.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        recyclerView.addItemDecoration(new HorizontalSpaceItemDecoration(HORIONTAL_ITEM_SPACE));
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
//
        recyclerView.setLayoutManager(flowLayoutManager);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LanguageHolder) {
            try {
          LanguageAdapter languageAdapter=new LanguageAdapter(context,filterLanguageList);
          ((LanguageHolder) holder).ItemBinding.recyclerView.setAdapter(languageAdapter);

          checkArrowVisibility(holder);

                ((LanguageHolder) holder).ItemBinding.expandableArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (((LanguageHolder) holder).ItemBinding.recyclerView.getVisibility() == View.VISIBLE){
//                            ((LanguageHolder) holder).ItemBinding.recyclerView.setVisibility(View.GONE);
//                            ((LanguageHolder) holder).ItemBinding.expandableArrow.setBackgroundResource(R.drawable.arrow_up);
//
//                        }else {
//                            ((LanguageHolder) holder).ItemBinding.recyclerView.setVisibility(View.VISIBLE);
//                            ((LanguageHolder) holder).ItemBinding.expandableArrow.setBackgroundResource(R.drawable.arrow_down);
//
//                        }
                        checkArrowVisibility(holder);
                    }
                });

            }
            catch (Exception e) {
            }
        }
        if (holder instanceof GenreHolder) {
            try {
                GenreAdapter genreAdapter=new GenreAdapter(context,searchedKeywordsList);
                ((GenreHolder) holder).ItemBinding.recyclerView.setAdapter(genreAdapter);
                checkGenreArrowVisibility(holder);
                ((GenreHolder) holder).ItemBinding.expandableArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkGenreArrowVisibility(holder);
                    }
                });

            }
            catch (Exception e) {
            }
        }
    }

    private void checkGenreArrowVisibility(RecyclerView.ViewHolder holder) {
        if (((GenreHolder) holder).ItemBinding.recyclerView.getVisibility() == View.VISIBLE){
            ((GenreHolder) holder).ItemBinding.recyclerView.setVisibility(View.GONE);
            ((GenreHolder) holder).ItemBinding.expandableArrow.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);

        }else {
            ((GenreHolder) holder).ItemBinding.recyclerView.setVisibility(View.VISIBLE);
            ((GenreHolder) holder).ItemBinding.expandableArrow.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);

        }
    }

    private void checkArrowVisibility(RecyclerView.ViewHolder holder) {
        if (((LanguageHolder) holder).ItemBinding.recyclerView.getVisibility() == View.VISIBLE){
            ((LanguageHolder) holder).ItemBinding.recyclerView.setVisibility(View.GONE);
            ((LanguageHolder) holder).ItemBinding.expandableArrow.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24);

        }else {
            ((LanguageHolder) holder).ItemBinding.recyclerView.setVisibility(View.VISIBLE);
            ((LanguageHolder) holder).ItemBinding.expandableArrow.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_up_24);

        }
    }

    @Override
        public int getItemCount () {
            return 2;
        }

    private class LanguageHolder extends RecyclerView.ViewHolder {

        final LanguageRecyclerItemBinding ItemBinding;

        public LanguageHolder(LanguageRecyclerItemBinding ItemLayoutBinding) {
            super(ItemLayoutBinding.getRoot());
            ItemBinding = ItemLayoutBinding;

        }
    }

    private class GenreHolder extends RecyclerView.ViewHolder {

        final GenreRecyclerItemBinding ItemBinding;

        public GenreHolder(GenreRecyclerItemBinding ItemLayoutBinding) {
            super(ItemLayoutBinding.getRoot());
            ItemBinding = ItemLayoutBinding;

        }
    }
}


