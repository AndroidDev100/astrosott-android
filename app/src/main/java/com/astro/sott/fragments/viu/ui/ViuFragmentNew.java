package com.astro.sott.fragments.viu.ui;

import android.os.Bundle;

import com.astro.sott.baseModel.TabsBaseFragment;
import com.astro.sott.fragments.video.viewModel.VideoViewModel;
import com.astro.sott.fragments.viu.viewModel.ViuViewModel;

public class ViuFragmentNew extends TabsBaseFragment<ViuViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(ViuViewModel.class);
    }

    public void sameClick() {
        super.onSameClick();
    }
}