package com.astro.sott.activities.SelectAccount.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.astro.sott.activities.SelectAccount.SelectAccountModel.DtvMbbHbbModel;
import com.astro.sott.activities.SelectAccount.SelectAccountModel.Response;
import com.astro.sott.repositories.dtv.DTVRepository;
import com.astro.sott.repositories.loginRepository.LoginRepository;

import java.util.List;

public class SelectDtvViewModel extends AndroidViewModel {

    public SelectDtvViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Response> getConnectionDetails(String phoneNumber) {
        return LoginRepository.getInstance().getConnectionDetails(phoneNumber);
    }

    public LiveData<String> updateDTVAccountData(String account) {
        return DTVRepository.getInstance().saveDTVAccountData(getApplication().getApplicationContext(), account);
    }

    public LiveData<String> updateDtvMbbHbbAccount(List<DtvMbbHbbModel> dtvMbbHbbModels) {
        return DTVRepository.getInstance().updateDtvMbbHbbAccount(getApplication().getApplicationContext(),dtvMbbHbbModels);
    }
}
