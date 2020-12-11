package com.astro.sott.fragments.livetv.ui;

import android.os.Bundle;

import com.astro.sott.baseModel.TabsBaseFragment;
import com.astro.sott.fragments.livetv.viewModel.LiveTvViewModel;

public class LiveTvFragment extends TabsBaseFragment<LiveTvViewModel> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(LiveTvViewModel.class);
    }

    public void sameClick() {
        super.onSameClick();
    }
}
