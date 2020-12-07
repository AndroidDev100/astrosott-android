package com.dialog.dialoggo.activities.mbbaccount.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.dialog.dialoggo.repositories.mbbaccountrepository.MBBAccountRepository;

public class MBBViewModel extends AndroidViewModel {

    public MBBViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getMBBAccountList() {
        return MBBAccountRepository.getInstance().getMBBAccountList(getApplication().getApplicationContext());
    }
}
