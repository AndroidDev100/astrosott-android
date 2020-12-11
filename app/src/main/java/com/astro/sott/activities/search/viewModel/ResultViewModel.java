package com.astro.sott.activities.search.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.astro.sott.repositories.search.ResultRepository;
import com.kaltura.client.types.Asset;

import java.util.ArrayList;


public class ResultViewModel extends AndroidViewModel {

    private final ResultRepository repository;

    public ResultViewModel(@NonNull Application context) {
        super(context);
        repository = ResultRepository.getInstance();

    }

    public LiveData<ArrayList<Asset>> getListSearchResult(Context context, String mediaType, String searchString, int counter, boolean isScrolling) {
        return repository.getResultSearchAll(context, mediaType, searchString, counter, isScrolling);
    }


}
