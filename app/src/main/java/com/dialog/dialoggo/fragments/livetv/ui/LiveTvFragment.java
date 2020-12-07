package com.dialog.dialoggo.fragments.livetv.ui;

import android.os.Bundle;

import com.dialog.dialoggo.baseModel.TabsBaseFragment;
import com.dialog.dialoggo.fragments.livetv.viewModel.LiveTvViewModel;

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
