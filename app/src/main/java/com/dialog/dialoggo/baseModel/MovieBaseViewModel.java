package com.dialog.dialoggo.baseModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.beanModel.VIUChannel;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.repositories.movieDescription.MovieDescriptionRepository;
import com.dialog.dialoggo.repositories.splash.SplashRepository;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;

import java.util.List;
import java.util.Map;

public abstract class MovieBaseViewModel extends AndroidViewModel {
    protected MovieBaseViewModel(@NonNull Application application) {
        super(application);
    }

    public abstract LiveData<List<AssetCommonBean>> getListLiveData(long channelID, List<VIUChannel> list, int counter, int swipeToRefresh);

    public abstract LiveData<List<AssetCommonBean>> getSimilarMovie(int assetId,
                                                                    int counter,
                                                                    int assetType,
                                                                    Map<String, MultilingualStringValueArray> map,
                                                                    int layoutType, int screen_id, Asset asset);

    public abstract LiveData<List<AssetCommonBean>> getYouMayAlsoLike(int assetId,
                                                                      int counter,
                                                                      int assetType,
                                                                      Map<String, MultilingualStringValueArray> map,
                                                                      int layoutType, int screen_id, Asset asset);


    public LiveData<RailCommonData> getSpecificAsset(String assetId) {
        return new SplashRepository().getSpecificAsset(getApplication().getApplicationContext(), assetId);
    }

    public abstract LiveData<AssetCommonBean> getChannelList(int screen_id);

    public void resetObject() {
//        CategoryRailLayer.resetObject();
//        SimilarMovie.resetObject();
//        YouMayAlsoLike.resetObject();
        MovieDescriptionRepository.resetObject();
    }
}
