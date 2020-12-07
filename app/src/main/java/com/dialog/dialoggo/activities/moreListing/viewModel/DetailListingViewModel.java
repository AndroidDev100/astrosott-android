package com.dialog.dialoggo.activities.moreListing.viewModel;


import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.repositories.moreListing.DetailListingRepository;
import com.kaltura.client.types.Asset;

import java.util.List;


public class DetailListingViewModel extends AndroidViewModel {



    public DetailListingViewModel(@NonNull Application application) {
        super(application);


    }

    public LiveData<List<RailCommonData>> getLiveData(int assetId, int counter, String layout,boolean isScrolling) {
        return DetailListingRepository.getInstance().loadData(getApplication().getApplicationContext(),assetId,counter,layout,isScrolling);
    }

    public int checkMoreType(AssetCommonBean assetCommonBean) {
        return assetCommonBean.getMoreType();
    }

    public LiveData<List<RailCommonData>> getSimmilarMovies(AssetCommonBean assetCommonBean, Asset object, String layout, int listingType, boolean isScrolling, int counter) {
        return DetailListingRepository.getInstance().loadSimillarMovieData(getApplication().getApplicationContext(),assetCommonBean,getApplication().getApplicationContext(),object,layout,listingType,isScrolling,counter);
    }
}