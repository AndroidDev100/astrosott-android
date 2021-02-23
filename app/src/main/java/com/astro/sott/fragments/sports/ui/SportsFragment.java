package com.astro.sott.fragments.sports.ui;

import android.os.Bundle;

import com.astro.sott.baseModel.TabsBaseFragment;
import com.astro.sott.fragments.sports.viewModel.SportsViewModel;
import com.astro.sott.fragments.video.viewModel.VideoViewModel;

public class SportsFragment extends TabsBaseFragment<SportsViewModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(SportsViewModel.class);
    }

    public void sameClick() {
        super.onSameClick();
    }
}
