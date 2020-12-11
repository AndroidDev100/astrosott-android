package com.astro.sott.fragments.home.ui;

import android.os.Bundle;

import com.astro.sott.baseModel.TabsBaseFragment;
import com.astro.sott.fragments.home.viewModel.HomeFragmentViewModel;


public class HomeFragment extends TabsBaseFragment<HomeFragmentViewModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(HomeFragmentViewModel.class);
    }
    public void sameClick(){
        super.onSameClick();
    }
}
