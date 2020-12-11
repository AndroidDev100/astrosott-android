package com.astro.sott.activities.addDTVAccountNumber;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.astro.sott.repositories.dtv.DTVRepository;

public class AddDTVViewModel extends AndroidViewModel {
    public AddDTVViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> updateDTVAccountData(String dtvAccount) {

        return DTVRepository.getInstance().saveDTVAccount(getApplication().getApplicationContext(), dtvAccount,"");
    }
}
