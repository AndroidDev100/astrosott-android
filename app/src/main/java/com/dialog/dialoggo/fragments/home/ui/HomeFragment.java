package com.dialog.dialoggo.fragments.home.ui;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;

import com.dialog.dialoggo.baseModel.TabsBaseFragment;
import com.dialog.dialoggo.fragments.home.viewModel.HomeFragmentViewModel;


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
