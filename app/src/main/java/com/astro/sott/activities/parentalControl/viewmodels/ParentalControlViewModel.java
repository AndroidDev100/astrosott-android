package com.astro.sott.activities.parentalControl.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.astro.sott.activities.parentalControl.repositories.ParentalControlRepository;
import com.astro.sott.beanModel.login.CommonResponse;

public class ParentalControlViewModel extends AndroidViewModel {
    public ParentalControlViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<CommonResponse> getOttUserDetails(Context context) {
         return  ParentalControlRepository.getInstance().getOttUserDetails(context);
    }

    public LiveData<String> updateParentalControl(Context context,String partenalStatus) {
        return ParentalControlRepository.getInstance().updateParentalControl(context,partenalStatus);
    }

    public LiveData<CommonResponse> validatePin(Context context, String pinText) {
        return ParentalControlRepository.getInstance().validatePin(context,pinText);
    }

    public LiveData<CommonResponse> setPin(Context context, String pinText) {
        return ParentalControlRepository.getInstance().setPin(context,pinText);
    }

    public LiveData<CommonResponse> enableParental(Context context) {
        return ParentalControlRepository.getInstance().enableParental(context);
    }
    public LiveData<CommonResponse> disableParental(Context context){
        return ParentalControlRepository.getInstance().disableParental(context);
    }
}
