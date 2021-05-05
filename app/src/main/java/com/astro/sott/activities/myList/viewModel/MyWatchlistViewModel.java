package com.astro.sott.activities.myList.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import android.content.Context;

import androidx.annotation.NonNull;

import com.astro.sott.activities.myList.MyWatchlistRepository;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;

import java.util.ArrayList;
import java.util.List;

public class MyWatchlistViewModel extends AndroidViewModel {
    private final Context context;

    public MyWatchlistViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }

    public LiveData<List<RailCommonData>> getAllWatchlist(String ksql) {
        return MyWatchlistRepository.getInstance().getAllWatchlist(context, ksql);
    }

    public LiveData<ArrayList<RailCommonData>> getTrendingListing(String customMediaType, String customGenre, String customGenreRule, int counter) {
        return MyWatchlistRepository.getInstance().getTrending(context, customMediaType, customGenre, customGenreRule, counter);
    }

    public LiveData<ArrayList<RailCommonData>> getEpgListing(String customDays, String linearAssetId, int counter) {
        return MyWatchlistRepository.getInstance().getEPGListing(context, customDays, linearAssetId, counter);
    }

    public LiveData<ArrayList<RailCommonData>> getPurchaseListing(String customMediaType, String customGenre, String customGenreRule, int counter) {
        return MyWatchlistRepository.getInstance().getPurchaseListing(context, customMediaType, customGenre, customGenreRule, counter);
    }

    public LiveData<CommonResponse> getWatchlistData(int counter) {
        return MyWatchlistRepository.getInstance().getWatchListData(counter, context);
    }

    public LiveData<CommonResponse> deleteWatchlist(String idfromAssetWatchlist) {
        return MyWatchlistRepository.getInstance().deleteFromWatchlist(idfromAssetWatchlist, context);
    }
}
