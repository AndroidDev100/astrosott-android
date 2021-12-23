package com.astro.sott.activities.moreListing.filter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.activities.search.adapter.QuickSearchGenreAdapter;
import com.astro.sott.activities.search.adapter.SearchKeywordAdapter;
import com.astro.sott.activities.search.constants.SearchFilterEnum;
import com.astro.sott.activities.search.ui.QuickSearchGenre;
import com.astro.sott.activities.search.ui.QuickSearchViewModel;
import com.astro.sott.activities.search.ui.SearchKeywordActivity;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.GenreSelectionCallBack;
import com.astro.sott.databinding.ActivitySearchKeywordBinding;
import com.astro.sott.db.search.SearchedKeywords;
import com.astro.sott.modelClasses.dmsResponse.FilterLanguages;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListingFilterActivity extends BaseBindingActivity<ActivitySearchKeywordBinding> {

    @Override
    protected ActivitySearchKeywordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySearchKeywordBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel();
        connectionObserver();

    }

    QuickSearchViewModel viewModel;
    private void setViewModel() {
        viewModel = ViewModelProviders.of(this).get(QuickSearchViewModel.class);
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(boolean aBoolean) {
        if (aBoolean) {
            UIinitialization();
            setClicks();
            loadGenreData();
        } else {
            noConnectionLayout();
        }
    }

    List<SearchedKeywords> newObjectNew;
    List<SearchedKeywords> newObject;
    private ResponseDmsModel responseDmsModel;
    private ArrayList<FilterLanguages> filterLanguageList;
    private ArrayList<FilterLanguages> filterLanguageListNew;
    List<String> filterLanguageSavedList;
    List<String> filterGenreSavedList;
    int fromReset = 0;

    private void callGenreData() {

        try {
            filterLanguageSavedList = new ArrayList<>();
            filterLanguageList = new ArrayList<>();
            newObject = new ArrayList<>();
            Gson gson = new Gson();
            String json = KsPreferenceKey.getInstance(ListingFilterActivity.this).getUserProfileData();
            Type type = new TypeToken<List<SearchedKeywords>>() {
            }.getType();
            newObjectNew = gson.fromJson(json, type);
            //  Log.w("savedGenre",newObjectNew.get(0).getKeyWords());

            if (newObjectNew != null) {
                filterGenreSavedList = AppCommonMethods.createFilterGenreList(KsPreferenceKey.getInstance(this).getFilterGenreSelection());

                for (int i = 0; i < newObjectNew.size(); i++) {
                    SearchedKeywords filterLanguages = new SearchedKeywords();
                    filterLanguages.setKeyWords(newObjectNew.get(i).getKeyWords());
                    if (AppCommonMethods.checkLangeageAdded(newObjectNew.get(i).getKeyWords(), filterGenreSavedList)) {
                        filterLanguages.setSelected(true);
                    } else {
                        filterLanguages.setSelected(false);
                    }

                    newObject.add(filterLanguages);
                }
            }

            responseDmsModel = AppCommonMethods.callpreference(this);
            filterLanguageListNew = responseDmsModel.getFilterLanguageList();
            filterLanguageSavedList = AppCommonMethods.createFilterLanguageList(KsPreferenceKey.getInstance(this).getFilterLanguageSelection());

            for (int i = 0; i < filterLanguageListNew.size(); i++) {
                FilterLanguages filterLanguages = new FilterLanguages();
                filterLanguages.setValue(filterLanguageListNew.get(i).getValue());
                filterLanguages.setKey(filterLanguageListNew.get(i).getKey());
                if (AppCommonMethods.checkLangeageAdded(filterLanguageListNew.get(i).getValue(), filterLanguageSavedList)) {
                    filterLanguages.setSelected(true);
                } else {
                    filterLanguages.setSelected(false);
                }

                filterLanguageList.add(filterLanguages);
            }
        } catch (Exception ignored) {

        }

    }

    private void setClicks() {
        checkFilterSoted(this);
        getBinding().clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromReset = 1;
                resetFilterScreen();
            }
        });
        getBinding().apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromReset == 1) {
                    fromReset = 0;
                    resetScreen();
                    KsPreferenceKey.getInstance(ListingFilterActivity.this).setFilterApply("true");
                    onBackPressed();
                } else {
                    if (checkSelections()) {
                        KsPreferenceKey.getInstance(ListingFilterActivity.this).setFilterApply("true");
                        onBackPressed();
                    } else {
                        ToastHandler.show(getResources().getString(R.string.select_one), getApplicationContext());
                    }
                }
            }
        });
        getBinding().backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getBinding().sortATZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortClickHandling(1);
            }
        });

        getBinding().sortPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortClickHandling(2);
            }
        });

        getBinding().sortNewest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortClickHandling(3);
            }
        });

        getBinding().contentTypeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentTypeClickHandling(1);
            }
        });

        getBinding().contentTypeOnDemand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentTypeClickHandling(2);
            }
        });

        getBinding().contentTypeLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentTypeClickHandling(3);
            }
        });

        getBinding().freePaidAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freePiadClickHandling(1);
            }
        });

        getBinding().freePaidFree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freePiadClickHandling(2);
            }
        });

        getBinding().freePaidPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freePiadClickHandling(3);
            }
        });

    }

    List<SearchedKeywords> newResetList;
    List<FilterLanguages> newFilterResetList;

    private void resetFilterScreen() {
        try {
            newResetList = new ArrayList<>();
            newFilterResetList = new ArrayList<>();
            searchKeywordAdapter = null;
            sortClickHandling(4);
            contentTypeClickHandling(4);
            freePiadClickHandling(4);
            if (newObject != null && newObject.size() > 0) {
                newResetList.addAll(newObject);
                newObject.clear();
                for (int i = 0; i < newResetList.size(); i++) {
                    SearchedKeywords filterLanguages = new SearchedKeywords();
                    filterLanguages.setKeyWords(newResetList.get(i).getKeyWords());
                    filterLanguages.setSelected(false);
                    newObject.add(filterLanguages);
                }

            }

            if (filterLanguageList != null && filterLanguageList.size() > 0) {
                newFilterResetList.addAll(filterLanguageList);
                filterLanguageList.clear();
                for (int i = 0; i < newFilterResetList.size(); i++) {
                    FilterLanguages filterLanguages = new FilterLanguages();
                    filterLanguages.setValue(newFilterResetList.get(i).getValue());
                    filterLanguages.setSelected(false);
                    filterLanguageList.add(filterLanguages);
                }

            }

            loadDataFromModel();

        } catch (Exception e) {

        }

    }

    private void resetScreen() {
        try {
            AppCommonMethods.resetFilter(ListingFilterActivity.this);
            searchKeywordAdapter = null;
            sortClickHandling(4);
            contentTypeClickHandling(4);
            freePiadClickHandling(4);
            connectionObserver();
        } catch (Exception ignored) {

        }
    }

    private void checkFilterSoted(Context context) {
        if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.AZ.name())) {
            sortClickHandling(1);
        } else if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.POPULAR.name())) {
            sortClickHandling(2);
        } else if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.NEWEST.name())) {
            sortClickHandling(3);
        }

        if (KsPreferenceKey.getInstance(context).getFilterContentType().equalsIgnoreCase(SearchFilterEnum.ALL.name())) {
            contentTypeClickHandling(1);
        } else if (KsPreferenceKey.getInstance(context).getFilterContentType().equalsIgnoreCase(SearchFilterEnum.ONDEMAND.name())) {
            contentTypeClickHandling(2);
        } else if (KsPreferenceKey.getInstance(context).getFilterContentType().equalsIgnoreCase(SearchFilterEnum.LIVE.name())) {
            contentTypeClickHandling(3);
        }

        if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.ALL.name())) {
            freePiadClickHandling(1);
        } else if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.FREE.name())) {
            freePiadClickHandling(2);
        } else if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.PAID.name())) {
            freePiadClickHandling(3);
        }
    }

    private void freePiadClickHandling(int itemPosition) {
        if (itemPosition == 1) {
            KsPreferenceKey.getInstance(ListingFilterActivity.this).setFilterFreePaid(SearchFilterEnum.ALL.name());
            getBinding().freePaidAll.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().freePaidAll.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_filter_selected));

            getBinding().freePaidFree.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidFree.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));

            getBinding().freePaidPaid.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidPaid.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));

        } else if (itemPosition == 2) {
            KsPreferenceKey.getInstance(ListingFilterActivity.this).setFilterFreePaid(SearchFilterEnum.FREE.name());
            getBinding().freePaidFree.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().freePaidFree.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_filter_selected));

            getBinding().freePaidAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidAll.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));

            getBinding().freePaidPaid.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidPaid.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));
        } else if (itemPosition == 3) {
            KsPreferenceKey.getInstance(ListingFilterActivity.this).setFilterFreePaid(SearchFilterEnum.PAID.name());
            getBinding().freePaidPaid.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().freePaidPaid.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_filter_selected));

            getBinding().freePaidFree.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidFree.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));

            getBinding().freePaidAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidAll.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));
        } else {
            getBinding().freePaidPaid.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidPaid.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));

            getBinding().freePaidFree.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidFree.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));

            getBinding().freePaidAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidAll.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));
        }
    }

    private void contentTypeClickHandling(int itemPosition) {
        if (itemPosition == 1) {
            KsPreferenceKey.getInstance(ListingFilterActivity.this).setFilterContentType(SearchFilterEnum.ALL.name());
            getBinding().contentTypeAll.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
//            getBinding().contentTypeAll.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));
            getBinding().contentTypeAll.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_filter_selected));


            getBinding().contentTypeOnDemand.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeOnDemand.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));

            getBinding().contentTypeLive.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().contentTypeLive.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().contentTypeLive.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));


        } else if (itemPosition == 2) {
            KsPreferenceKey.getInstance(ListingFilterActivity.this).setFilterContentType(SearchFilterEnum.ONDEMAND.name());
            getBinding().contentTypeOnDemand.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
//            getBinding().contentTypeOnDemand.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));
            getBinding().contentTypeOnDemand.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_filter_selected));


            getBinding().contentTypeAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeAll.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));

            getBinding().contentTypeLive.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().contentTypeLive.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().contentTypeLive.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));


        } else if (itemPosition == 3) {
            KsPreferenceKey.getInstance(ListingFilterActivity.this).setFilterContentType(SearchFilterEnum.LIVE.name());
            getBinding().contentTypeLive.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().contentTypeLive.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_filter_selected));

            getBinding().contentTypeOnDemand.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().contentTypeOnDemand.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().contentTypeOnDemand.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));


            getBinding().contentTypeAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeAll.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));

        } else {
            getBinding().contentTypeLive.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().contentTypeLive.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().contentTypeLive.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));


            getBinding().contentTypeOnDemand.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().contentTypeOnDemand.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().contentTypeOnDemand.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));


            getBinding().contentTypeAll.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().contentTypeAll.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().contentTypeAll.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));



        }
    }

    private void sortClickHandling(int itemPosition) {
        if (itemPosition == 1) {
            KsPreferenceKey.getInstance(ListingFilterActivity.this).setFilterSortBy(SearchFilterEnum.AZ.name());
            getBinding().sortATZ.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
//            getBinding().sortATZ.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));
            getBinding().sortATZ.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_filter_selected));


            getBinding().sortPopular.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().sortPopular.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().sortPopular.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));


            getBinding().sortNewest.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().sortNewest.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().sortNewest.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));


        } else if (itemPosition == 2) {
            KsPreferenceKey.getInstance(ListingFilterActivity.this).setFilterSortBy(SearchFilterEnum.POPULAR.name());
            getBinding().sortPopular.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().sortPopular.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_filter_selected));
//            getBinding().sortPopular.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));

            getBinding().sortATZ.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().sortATZ.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().sortATZ.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));



            getBinding().sortNewest.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().sortNewest.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().sortNewest.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));

        } else if (itemPosition == 3) {
            KsPreferenceKey.getInstance(ListingFilterActivity.this).setFilterSortBy(SearchFilterEnum.NEWEST.name());
            getBinding().sortNewest.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
//            getBinding().sortNewest.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));
            getBinding().sortNewest.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_filter_selected));


            getBinding().sortPopular.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().sortPopular.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().sortPopular.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));


            getBinding().sortATZ.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().sortATZ.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().sortATZ.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));

        } else {
            getBinding().sortNewest.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().sortNewest.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().sortNewest.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));


            getBinding().sortPopular.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().sortPopular.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().sortPopular.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));


            getBinding().sortATZ.setTextColor(getResources().getColor(R.color.grey_text));
//            getBinding().sortATZ.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
            getBinding().sortATZ.setBackground(ContextCompat.getDrawable(getApplicationContext(),R.drawable.round_purple_bg));

        }
    }

    SearchKeywordAdapter searchKeywordAdapter;

    private void loadDataFromModel() {
        searchKeywordAdapter = new SearchKeywordAdapter(ListingFilterActivity.this, newObject, filterLanguageList);
        getBinding().recyclerView.setAdapter(searchKeywordAdapter);
    }

    private void UIinitialization() {
        getBinding().recyclerView.hasFixedSize();
        getBinding().recyclerView.setNestedScrollingEnabled(false);
        getBinding().recyclerView.hasFixedSize();
        getBinding().recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    private boolean checkSelections() {
        boolean selected = false;
        if (!KsPreferenceKey.getInstance(this).getFilterSortBy().equalsIgnoreCase("")) {
            selected = true;
            return selected;
        } else if (!KsPreferenceKey.getInstance(this).getFilterContentType().equalsIgnoreCase("")) {
            selected = true;
            return selected;
        } else if (!KsPreferenceKey.getInstance(this).getFilterFreePaid().equalsIgnoreCase("")) {
            selected = true;
            return selected;
        } else if (!KsPreferenceKey.getInstance(this).getFilterLanguageSelection().equalsIgnoreCase("")) {
            selected = true;
            return selected;
        } else if (!KsPreferenceKey.getInstance(this).getFilterGenreSelection().equalsIgnoreCase("")) {
            selected = true;
            return selected;
        } else {
            return selected;
        }

    }

    private void loadGenreData() {
        if (KsPreferenceKey.getInstance(ListingFilterActivity.this).getUserProfileData()!=null && !KsPreferenceKey.getInstance(ListingFilterActivity.this).getUserProfileData().equalsIgnoreCase("")){
            callGenreData();
            loadDataFromModel();
        }else {
            runOnUiThread(() -> viewModel.getGenreData(ListingFilterActivity.this, MediaTypeConstant.getFilterGenre(ListingFilterActivity.this)).observe(ListingFilterActivity.this, commonResponse -> {
                if (commonResponse != null && commonResponse.size() > 0 && commonResponse.get(0).getStatus()) {
                    try {
                        List<SearchedKeywords> list = new ArrayList<>();
                        for (int i = 0; i < commonResponse.size(); i++) {
                            SearchedKeywords searchedKeywords = new SearchedKeywords();
                            searchedKeywords.setKeyWords(commonResponse.get(i).getName());
                            searchedKeywords.setSelected(false);
                            list.add(searchedKeywords);
                        }
                        Gson gson = new Gson();
                        String userProfileData = gson.toJson(list);
                        KsPreferenceKey.getInstance(ListingFilterActivity.this).setUserProfileData(userProfileData);
                    } catch (Exception ignored) {

                    }
                } else {

                }
                callGenreData();
                loadDataFromModel();

            }));
        }
    }


}