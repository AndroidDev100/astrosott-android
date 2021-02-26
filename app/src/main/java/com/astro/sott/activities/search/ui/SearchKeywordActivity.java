package com.astro.sott.activities.search.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.R;
import com.astro.sott.activities.search.adapter.SearchKeywordAdapter;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.databinding.ActivitySearchKeywordBinding;
import com.astro.sott.utils.helpers.NetworkConnectivity;

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
//            modelCall();
//            resetLayout();
            UIinitialization();
            setClicks();
            loadDataFromModel();
        } else {

            noConnectionLayout();
        }
    }

    private void setClicks() {
        getBinding().backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void loadDataFromModel() {
        SearchKeywordAdapter searchKeywordAdapter = new SearchKeywordAdapter(SearchKeywordActivity.this);
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