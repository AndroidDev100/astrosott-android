package com.dialog.dialoggo.activities.dtvActivity.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.repositories.dtv.DTVRepository;

public class dtvViewModel extends AndroidViewModel {
    public dtvViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getDtvAccountList() {
       return DTVRepository.getInstance().getDtvAccountList(getApplication().getApplicationContext());
    }
}
