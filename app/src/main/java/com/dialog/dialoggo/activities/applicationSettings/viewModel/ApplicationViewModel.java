package com.dialog.dialoggo.activities.applicationSettings.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.repositories.applicationRepo.ApplicationRepository;

import java.util.List;

public class ApplicationViewModel extends AndroidViewModel {

    public ApplicationViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<String>> getAllSampleData() {
        return new ApplicationRepository().createMoreList(getApplication());
    }

}
