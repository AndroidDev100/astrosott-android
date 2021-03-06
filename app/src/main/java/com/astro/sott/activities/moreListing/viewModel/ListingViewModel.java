package com.astro.sott.activities.moreListing.viewModel;


import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.astro.sott.repositories.moreListing.ListingRepository;
import com.astro.sott.baseModel.DeepSearchLayer;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.kaltura.client.types.Asset;

import java.util.List;


public class ListingViewModel extends AndroidViewModel {



    public ListingViewModel(@NonNull Application application) {
        super(application);


    }

    public LiveData<List<RailCommonData>> getLiveData(int assetId, int counter, String layout,boolean isScrolling) {
        return ListingRepository.getInstance().loadData(getApplication().getApplicationContext(),assetId,counter,layout,isScrolling);
    }

    public int checkMoreType(AssetCommonBean assetCommonBean) {
        return assetCommonBean.getMoreType();
    }

    public LiveData<List<RailCommonData>> getSimmilarMovies(AssetCommonBean assetCommonBean, Asset object, String layout, int listingType, boolean isScrolling, int counter) {
        return ListingRepository.getInstance().loadSimillarMovieData(getApplication().getApplicationContext(),assetCommonBean,getApplication().getApplicationContext(),object,layout,listingType,isScrolling,counter);
    }

    public LiveData<List<RailCommonData>> getLiveSearchedData(int assetId, List<String> list, String filterValue, int counter, String layout, boolean isScrolling,int pageSize) {
        return DeepSearchLayer.getInstance().loadSearchedData(getApplication().getApplicationContext(),assetId, filterValue,list,counter,layout,isScrolling,pageSize);
    }
}