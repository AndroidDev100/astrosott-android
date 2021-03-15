package com.astro.sott.activities.search.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.activities.search.adapter.SearchKeywordAdapter;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.ActivitySearchKeywordBinding;
import com.astro.sott.db.search.SearchedKeywords;
import com.astro.sott.modelClasses.dmsResponse.AudioLanguages;
import com.astro.sott.modelClasses.dmsResponse.FilterLanguages;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.NetworkConnectivity;
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
    List<SearchedKeywords> newObject;
    private ResponseDmsModel responseDmsModel;
    private ArrayList<FilterLanguages> filterLanguageList;
    private void callGenreData() {
        try {
            Gson gson = new Gson();
            String json = KsPreferenceKey.getInstance(SearchKeywordActivity.this).getUserProfileData();
            Type type = new TypeToken<List<SearchedKeywords>>() {}.getType();
            newObject = gson.fromJson(json, type);
            Log.w("savedGenre",newObject.get(0).getKeyWords());

            responseDmsModel = AppCommonMethods.callpreference(this);
            filterLanguageList = responseDmsModel.getFilterLanguageList();
        }catch (Exception ignored){

        }

    }

    private void setClicks() {
        getBinding().apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterApply("true");
                onBackPressed();
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

    private void freePiadClickHandling(int itemPosition) {
        if (itemPosition==1){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterFreePaid("All");
            getBinding().freePaidAll.setTextColor(Color.parseColor("#151024"));
            getBinding().freePaidAll.setBackgroundColor(Color.parseColor("#00e895"));

            getBinding().freePaidFree.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidFree.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().freePaidPaid.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidPaid.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

        }
        else if (itemPosition==2){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterFreePaid("Free");
            getBinding().freePaidFree.setTextColor(Color.parseColor("#151024"));
            getBinding().freePaidFree.setBackgroundColor(Color.parseColor("#00e895"));

            getBinding().freePaidAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidAll.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().freePaidPaid.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidPaid.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
        }else {
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterFreePaid("Paid");
            getBinding().freePaidPaid.setTextColor(Color.parseColor("#151024"));
            getBinding().freePaidPaid.setBackgroundColor(Color.parseColor("#00e895"));

            getBinding().freePaidFree.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidFree.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().freePaidAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().freePaidAll.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
        }
    }

    private void contentTypeClickHandling(int itemPosition) {
        if (itemPosition==1){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterContentType("All");
            getBinding().contentTypeAll.setTextColor(Color.parseColor("#151024"));
            getBinding().contentTypeAll.setBackgroundColor(Color.parseColor("#00e895"));

            getBinding().contentTypeOnDemand.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeOnDemand.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().contentTypeLive.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeLive.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

        }else if (itemPosition==2){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterContentType("On Demand");
            getBinding().contentTypeOnDemand.setTextColor(Color.parseColor("#151024"));
            getBinding().contentTypeOnDemand.setBackgroundColor(Color.parseColor("#00e895"));

            getBinding().contentTypeAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeAll.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().contentTypeLive.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeLive.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

        }else {
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterContentType("Live");
            getBinding().contentTypeLive.setTextColor(Color.parseColor("#151024"));
            getBinding().contentTypeLive.setBackgroundColor(Color.parseColor("#00e895"));

            getBinding().contentTypeOnDemand.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeOnDemand.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().contentTypeAll.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().contentTypeAll.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

        }
    }

    private void sortClickHandling(int itemPosition) {
        if (itemPosition==1){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterSortBy("A-Z");
            getBinding().sortATZ.setTextColor(Color.parseColor("#151024"));
            getBinding().sortATZ.setBackgroundColor(Color.parseColor("#00e895"));

            getBinding().sortPopular.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortPopular.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().sortNewest.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortNewest.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

        }else if (itemPosition==2){
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterSortBy("Popular");
            getBinding().sortPopular.setTextColor(Color.parseColor("#151024"));
            getBinding().sortPopular.setBackgroundColor(Color.parseColor("#00e895"));

            getBinding().sortATZ.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortATZ.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().sortNewest.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortNewest.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
        }else {
            KsPreferenceKey.getInstance(SearchKeywordActivity.this).setFilterSortBy("Newest");
            getBinding().sortNewest.setTextColor(Color.parseColor("#151024"));
            getBinding().sortNewest.setBackgroundColor(Color.parseColor("#00e895"));

            getBinding().sortPopular.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortPopular.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));

            getBinding().sortATZ.setTextColor(getResources().getColor(R.color.grey_text));
            getBinding().sortATZ.setBackgroundColor(getResources().getColor(R.color.edit_text_blue_bg));
        }
    }

    private void loadDataFromModel() {
        SearchKeywordAdapter searchKeywordAdapter = new SearchKeywordAdapter(SearchKeywordActivity.this,newObject,filterLanguageList);
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
}