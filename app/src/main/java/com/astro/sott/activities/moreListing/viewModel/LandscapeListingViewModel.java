package com.astro.sott.activities.moreListing.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.astro.sott.baseModel.DeepSearchLayer;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.kaltura.client.types.Asset;

import java.util.List;

public class LandscapeListingViewModel extends AndroidViewModel {

    LiveData<RailCommonData> liveData;

    public LandscapeListingViewModel(@NonNull Application application) {
        super(application);


    }



    public LiveData<List<RailCommonData>> getLiveSearchedData(int assetId, List<String> list, String filterValue, int counter, String layout, boolean isScrolling,int pageSize) {
        return DeepSearchLayer.getInstance().loadSearchedData(getApplication().getApplicationContext(),assetId,filterValue,list,counter,layout,isScrolling,pageSize);
    }

    public int checkMoreType(AssetCommonBean assetCommonBean) {
        return assetCommonBean.getMoreType();
    }


}
