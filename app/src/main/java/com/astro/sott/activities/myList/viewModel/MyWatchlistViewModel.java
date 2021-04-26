package com.astro.sott.activities.myList.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;

import com.astro.sott.activities.myList.MyWatchlistRepository;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;

import java.util.List;

public class MyWatchlistViewModel extends AndroidViewModel {
    private final Context context;
    public MyWatchlistViewModel(@NonNull Application application) {
        super(application);
        this.context = application;
    }

    public LiveData<List<RailCommonData>> getAllWatchlist(String ksql) {
        return MyWatchlistRepository.getInstance().getAllWatchlist(context,ksql);
    }

    public LiveData<CommonResponse> getWatchlistData(int counter) {
        return MyWatchlistRepository.getInstance().getWatchListData(counter,context);
    }

    public LiveData<CommonResponse> deleteWatchlist(String idfromAssetWatchlist) {
        return MyWatchlistRepository.getInstance().deleteFromWatchlist(idfromAssetWatchlist,context);
    }
}
