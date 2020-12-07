package com.dialog.dialoggo.fragments.video.ui;

import android.os.Bundle;

import com.dialog.dialoggo.baseModel.TabsBaseFragment;
import com.dialog.dialoggo.fragments.video.viewModel.VideoViewModel;


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
