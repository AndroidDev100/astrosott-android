package com.dialog.dialoggo.fragments.moreTab.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.repositories.moreTab.MoreFragmentRepository;

import java.util.List;

public class MoreFragmentViewModel extends AndroidViewModel {

    public MoreFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<String>> getAllSampleData() {
        return new MoreFragmentRepository().createMoreList(getApplication());
    }

    public LiveData<CommonResponse> logoutApi() {
        return new MoreFragmentRepository().callLogoutApi(getApplication());
    }
}
