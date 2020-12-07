package com.dialog.dialoggo.activities.contentPreference.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.repositories.genre.GenreRepository;
import com.dialog.dialoggo.repositories.splash.SplashRepository;
import com.kaltura.client.types.Asset;

import java.util.List;

public class GenreViewModel extends AndroidViewModel {


    public GenreViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<Asset>> getGenre() {
        return GenreRepository.getInstance().getGenre(getApplication().getApplicationContext());

    }

    public LiveData<String> storeUserPrefrences(String value) {
        return GenreRepository.getInstance().storeUserPrefrences(getApplication().getApplicationContext(), value);
    }

    public LiveData<String> checkUserPreferences() {
        return new SplashRepository().checkUserPreferences(getApplication().getApplicationContext());
    }
}
