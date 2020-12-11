package com.astro.sott.fragments.video.ui;

import android.os.Bundle;

import com.astro.sott.baseModel.TabsBaseFragment;
import com.astro.sott.fragments.video.viewModel.VideoViewModel;


public class VideoFragment extends TabsBaseFragment<VideoViewModel> {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewModel(VideoViewModel.class);
    }

    public void sameClick() {
        super.onSameClick();
    }
}
