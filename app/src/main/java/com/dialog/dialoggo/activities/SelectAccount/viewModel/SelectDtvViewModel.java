package com.dialog.dialoggo.activities.SelectAccount.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.DtvMbbHbbModel;
import com.dialog.dialoggo.activities.SelectAccount.SelectAccountModel.Response;
import com.dialog.dialoggo.repositories.dtv.DTVRepository;
import com.dialog.dialoggo.repositories.loginRepository.LoginRepository;

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
