package com.astro.sott.activities.loginActivity.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.modelClasses.DTVContactInfoModel;
import com.astro.sott.modelClasses.OtpModel;
import com.astro.sott.repositories.dtv.DTVRepository;
import com.astro.sott.repositories.loginRepository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<OtpModel> getMpin(String userName) {

        return LoginRepository.getInstance().getMpin(getApplication().getApplicationContext(), userName);
    }

    public LiveData<CommonResponse> registerUser(String userName) {
        return LoginRepository.getInstance().registerUser(getApplication().getApplicationContext(), userName);
    }

    public LiveData<CommonResponse> loginUser(String userName, boolean viaRegistration) {
        return LoginRepository.getInstance().loginUser(getApplication().getApplicationContext(), userName, viaRegistration);
    }

public LiveData<CommonResponse> addToken(boolean viaRegistration) {
    return LoginRepository.getInstance().addToken(getApplication().getApplicationContext(), viaRegistration);
}
public LiveData<OtpModel> verifyPin(String msisdn, String otp,String txnId,Boolean isUserVerified) {
        return LoginRepository.getInstance().verifyPin(getApplication().getApplicationContext(), msisdn, otp,txnId,isUserVerified);
    }

    public LiveData<OtpModel> getMsisdn() {
        return LoginRepository.getInstance().getMsidn(getApplication().getApplicationContext());
    }

    public LiveData<DTVContactInfoModel> getDtvAccountDetail(String dtvAccountNumber) {
        return LoginRepository.getInstance().getDtvAccountDetail(getApplication().getApplicationContext(), dtvAccountNumber);
    }

    public LiveData<String> updateDTVAccountData(String dtvAccount,String fragmentType) {

        return DTVRepository.getInstance().saveDTVAccount(getApplication().getApplicationContext(), dtvAccount, fragmentType);
    }

    public void checkUserPreference() {
        LoginRepository.getInstance().checkUserType(getApplication().getApplicationContext());
    }
}
