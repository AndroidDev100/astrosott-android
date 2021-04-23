package com.astro.sott.fragments.homenewtab.ui;

import android.os.Bundle;

import com.astro.sott.baseModel.TabsBaseFragment;
import com.astro.sott.fragments.home.viewModel.HomeFragmentViewModel;
import com.astro.sott.fragments.homenewtab.viewModel.HomeTabNewViewModel;

public class HomeTabNew extends TabsBaseFragment<HomeTabNewViewModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(HomeTabNewViewModel.class);
    }
    public void sameClick(){
        super.onSameClick();
    }
}
