package com.dialog.dialoggo.repositories.genre;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import com.dialog.dialoggo.callBacks.commonCallBacks.UserPrefrencesCallBack;
import com.dialog.dialoggo.networking.ksServices.KsServices;
import com.kaltura.client.types.Asset;

import java.util.List;

public class GenreRepository {
    private static GenreRepository genreRepository;

    private GenreRepository() {

    }

    public static GenreRepository getInstance() {
        if (genreRepository == null) {
            genreRepository = new GenreRepository();
        }

        return genreRepository;
    }

    public LiveData<List<Asset>> getGenre(Context context) {
        final MutableLiveData<List<Asset>> connection = new MutableLiveData<>();
        final KsServices ksServices = new KsServices(context);
        ksServices.getGenre((status, result) -> {
            if (status) {
                connection.postValue(result.results.getObjects());
            }else{
                connection.postValue(null);
            }
        });


        return connection;
    }

    public LiveData<String> storeUserPrefrences(Context context, String value) {
        final MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

        KsServices ksServices = new KsServices(context);
        ksServices.storeUserPrefrences(value, new UserPrefrencesCallBack() {
            @Override
            public void response(String value) {
                        mutableLiveData.postValue(value);
            }

            @Override
            public void failure() {
                mutableLiveData.postValue(null);
            }
        });

        return mutableLiveData;
    }


}
