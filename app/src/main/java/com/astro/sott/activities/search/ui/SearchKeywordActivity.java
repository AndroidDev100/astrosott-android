package com.astro.sott.activities.search.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.activities.search.adapter.SearchKeywordAdapter;
import com.astro.sott.activities.search.constants.SearchFilterEnum;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.ActivitySearchKeywordBinding;
import com.astro.sott.db.search.SearchedKeywords;
import com.astro.sott.modelClasses.dmsResponse.AudioLanguages;
import com.astro.sott.modelClasses.dmsResponse.FilterLanguages;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchKeywordActivity extends BaseBindingActivity<ActivitySearchKeywordBinding> {

    @Override
    protected ActivitySearchKeywordBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivitySearchKeywordBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionObserver();

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
            callGenreData();
//            modelCall();
//            resetLayout();
            UIinitialization();
            setClicks();
            loadDataFromModel();
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
    private void callGenreData() {
        try {
            filterLanguageSavedList=new ArrayList<>();
            filterLanguageList=new ArrayList<>();
            newObject=new ArrayList<>();
            Gson gson = new Gson();
            String json = KsPreferenceKey.getInstance(SearchKeywordActivity.this).getUserProfileData();
            Type type = new TypeToken<List<SearchedKeywords>>() {}.getType();
            newObjectNew = gson.fromJson(json, type);
            Log.w("savedGenre",newObjectNew.get(0).getKeyWords());

            filterGenreSavedList=AppCommonMethods.createFilterGenreList(KsPreferenceKey.getInstance(this).getFilterGenreSelection());

            for (int i=0;i<newObjectNew.size();i++){
                SearchedKeywords filterLanguages=new SearchedKeywords();
                filterLanguages.setKeyWords(newObjectNew.get(i).getKeyWords());
                if(AppCommonMethods.checkLangeageAdded(newObjectNew.get(i).getKeyWords(),filterGenreSavedList)){
                    filterLanguages.setSelected(true);
                }else {
                    filterLanguages.setSelected(false);
                }

                newObject.add(filterLanguages);
            }


            responseDmsModel = AppCommonMethods.callpreference(this);
            filterLanguageListNew = responseDmsModel.getFilterLanguageList();
            filterLanguageSavedList=AppCommonMethods.createFilterLanguageList(KsPreferenceKey.getInstance(this).getFilterLanguageSelection());

            for (int i=0;i<filterLanguageListNew.size();i++){
                FilterLanguages filterLanguages=new FilterLanguages();
                filterLanguages.setValue(filterLanguageListNew.get(i).getValue());
                filterLanguages.setKey(filterLanguageListNew.get(i).getKey());
                if(AppCommonMethods.checkLangeageAdded(filterLanguageListNew.get(i).getValue(),filterLanguageSavedList)){
                    filterLanguages.setSelected(true);
                }else {
                    filterLanguages.setSelected(false);
                }

                filterLanguageList.add(filterLanguages);
            }

        }catch (Exception ignored){

        }

    }

    private void setClicks() {
        checkFilterSoted(this);
        getBinding().clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCommonMethods.resetFilter(SearchKeywordActivity.this);
                searchKeywordAdapter=null;
                sortClickHandling(4);
                contentTypeClickHandling(4);
                freePiadClickHandling(4);
                connectionObserver();
            }
        });
        getBinding().apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelections()){
                    KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterApply("true");
                    onBackPressed();
                }else {
                    ToastHandler.show("Selection Required", getApplicationContext());
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

    private void checkFilterSoted(Context context) {
        if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.AZ.name())){
            sortClickHandling(1);
        }
        else if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.POPULAR.name())){
            sortClickHandling(2);
        }
        else if (KsPreferenceKey.getInstance(context).getFilterSortBy().equalsIgnoreCase(SearchFilterEnum.NEWEST.name())){
            sortClickHandling(3);
        }

        if (KsPreferenceKey.getInstance(context).getFilterContentType().equalsIgnoreCase(SearchFilterEnum.ALL.name())){
            contentTypeClickHandling(1);
        }else if (KsPreferenceKey.getInstance(context).getFilterContentType().equalsIgnoreCase(SearchFilterEnum.ONDEMAND.name())){
            contentTypeClickHandling(2);
        }
        else if (KsPreferenceKey.getInstance(context).getFilterContentType().equalsIgnoreCase(SearchFilterEnum.LIVE.name())){
            contentTypeClickHandling(3);
        }

        if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.ALL.name())){
            freePiadClickHandling(1);
        }else if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.FREE.name())){
            freePiadClickHandling(2);
        }
        else if (KsPreferenceKey.getInstance(context).getFilterFreePaid().equalsIgnoreCase(SearchFilterEnum.PAID.name())){
            freePiadClickHandling(3);
        }
    }

    private void freePiadClickHandling(int itemPosition) {
        if (itemPosition==1){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterFreePaid(SearchFilterEnum.ALL.name());
            getBinding().freePaidAll.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().freePaidAll.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));

            getBinding().freePaidFree.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidFree.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().freePaidPaid.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidPaid.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

        }
        else if (itemPosition==2){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterFreePaid(SearchFilterEnum.FREE.name());
            getBinding().freePaidFree.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().freePaidFree.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));

            getBinding().freePaidAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidAll.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().freePaidPaid.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidPaid.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
        }else if (itemPosition==3){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterFreePaid(SearchFilterEnum.PAID.name());
            getBinding().freePaidPaid.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().freePaidPaid.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));

            getBinding().freePaidFree.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidFree.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().freePaidAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidAll.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
        }else {
            getBinding().freePaidPaid.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidPaid.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().freePaidFree.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidFree.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().freePaidAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidAll.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
        }
    }

    private void contentTypeClickHandling(int itemPosition) {
        if (itemPosition==1){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterContentType(SearchFilterEnum.ALL.name());
            getBinding().contentTypeAll.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().contentTypeAll.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));

            getBinding().contentTypeOnDemand.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeOnDemand.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().contentTypeLive.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeLive.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

        }else if (itemPosition==2){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterContentType(SearchFilterEnum.ONDEMAND.name());
            getBinding().contentTypeOnDemand.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().contentTypeOnDemand.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));

            getBinding().contentTypeAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeAll.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().contentTypeLive.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeLive.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

        }else if (itemPosition==3){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterContentType(SearchFilterEnum.LIVE.name());
            getBinding().contentTypeLive.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().contentTypeLive.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));

            getBinding().contentTypeOnDemand.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeOnDemand.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().contentTypeAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeAll.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

        }else {
            getBinding().contentTypeLive.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeLive.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().contentTypeOnDemand.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeOnDemand.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().contentTypeAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeAll.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

        }
    }

    private void sortClickHandling(int itemPosition) {
        if (itemPosition==1){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterSortBy(SearchFilterEnum.AZ.name());
            getBinding().sortATZ.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().sortATZ.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));

            getBinding().sortPopular.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortPopular.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().sortNewest.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortNewest.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

        }else if (itemPosition==2){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterSortBy(SearchFilterEnum.POPULAR.name());
            getBinding().sortPopular.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().sortPopular.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));

            getBinding().sortATZ.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortATZ.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().sortNewest.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortNewest.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
        }else if (itemPosition==3){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterSortBy(SearchFilterEnum.NEWEST.name());
            getBinding().sortNewest.setTextColor(getResources().getColor(R.color.filter_text_selected_color));
            getBinding().sortNewest.setBackgroundColor(getResources().getColor(R.color.filter_text_selected_bg));

            getBinding().sortPopular.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortPopular.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().sortATZ.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortATZ.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
        }else {
            getBinding().sortNewest.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortNewest.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().sortPopular.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortPopular.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().sortATZ.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortATZ.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
        }
    }
    SearchKeywordAdapter searchKeywordAdapter;
    private void loadDataFromModel() {
        searchKeywordAdapter = new SearchKeywordAdapter(SearchKeywordActivity.this,newObject,filterLanguageList);
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
        boolean selected=false;
        if (!KsPreferenceKey.getInstance(this).getFilterSortBy().equalsIgnoreCase("")){
            selected=true;
            return selected;
        }
        else if (!KsPreferenceKey.getInstance(this).getFilterContentType().equalsIgnoreCase("")){
            selected=true;
            return selected;
        }
        else if (!KsPreferenceKey.getInstance(this).getFilterFreePaid().equalsIgnoreCase("")){
            selected=true;
            return selected;
        }
        else if (!KsPreferenceKey.getInstance(this).getFilterLanguageSelection().equalsIgnoreCase("")){
            selected=true;
            return selected;
        }
        else if (!KsPreferenceKey.getInstance(this).getFilterGenreSelection().equalsIgnoreCase("")){
            selected=true;
            return selected;
        }else {
            return selected;
        }

    }

}