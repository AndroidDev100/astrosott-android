package com.astro.sott.activities.dtvActivity.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.astro.sott.repositories.dtv.DTVRepository;

public class dtvViewModel extends AndroidViewModel {
    public dtvViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getDtvAccountList() {
       return DTVRepository.getInstance().getDtvAccountList(getApplication().getApplicationContext());
    }
}
