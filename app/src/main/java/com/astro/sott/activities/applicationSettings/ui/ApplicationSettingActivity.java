package com.astro.sott.activities.applicationSettings.ui;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;

import com.astro.sott.activities.applicationSettings.viewModel.ApplicationViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.activities.applicationSettings.adapter.ApplicationAdapter;
import com.astro.sott.databinding.ActivityApplicationSettingBinding;
import com.astro.sott.utils.helpers.NetworkConnectivity;

import java.util.List;

public class ApplicationSettingActivity extends BaseBindingActivity<ActivityApplicationSettingBinding> {
    private ApplicationViewModel viewModel;

    @Override
    public ActivityApplicationSettingBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityApplicationSettingBinding.inflate(inflater);

    }

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

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);
            modelCall();
            UIinitialization();
            loadDataFromModel();

        } else {
            noConnectionLayout();
        }


    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(ApplicationViewModel.class);
    }

    private void UIinitialization() {
        getBinding().recyclerViewMore.hasFixedSize();
        getBinding().recyclerViewMore.setNestedScrollingEnabled(false);
        getBinding().recyclerViewMore.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        getBinding().recyclerViewMore.addItemDecoration(itemDecor);

    }

    private void loadDataFromModel() {

        viewModel.getAllSampleData().observe(this, sectionDataModels -> {
            if (sectionDataModels != null && sectionDataModels.size() > 0) {

                setUIComponets(sectionDataModels);

            }
        });

    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    private void setUIComponets(List<String> mList) {

        ApplicationAdapter adapter = new ApplicationAdapter(this, mList);
        getBinding().recyclerViewMore.setAdapter(adapter);


    }
}
