package com.dialog.dialoggo.activities.myplaylist.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.repositories.myPlaylist.MyPlaylistRepo;

import java.util.List;

public class MyPlaylistViewModel extends AndroidViewModel {


    public MyPlaylistViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<RailCommonData>> getAllWatchlist(String ksql) {
        return MyPlaylistRepo.getInstance().getAllWatchlist(getApplication().getApplicationContext(),ksql);
    }

    public LiveData<CommonResponse> getWatchlistData(int counter, String partnerId) {
        return MyPlaylistRepo.getInstance().getWatchListData(getApplication().getApplicationContext(),counter,partnerId);
    }

    public LiveData<CommonResponse> deleteWatchlist(String idfromAssetWatchlist) {
        return MyPlaylistRepo.getInstance().deleteFromWatchlist(idfromAssetWatchlist,getApplication().getApplicationContext());
    }
}
