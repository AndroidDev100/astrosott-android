package com.astro.sott.fragments.trailerFragment.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.astro.sott.activities.movieDescription.layers.YouMayAlsoLike;
import com.astro.sott.activities.webEpisodeDescription.layers.EpisodesLayer;
import com.astro.sott.activities.webEpisodeDescription.layers.SeasonsLayer;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.repositories.dtv.DTVRepository;
import com.astro.sott.utils.TabsData;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.repositories.trailerFragment.TrailerFragmentRepository;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TrailerFragmentViewModel extends AndroidViewModel {
    public TrailerFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    private List<Asset> finalList;

    public LiveData<String> getRefIdLivedata(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getRefIdData(map);
    }

    public void setTrailerData(List<Asset> trailerData) {
        TabsData.getInstance().setTrailerData(trailerData);
    }

    public void setYouMayAlsoLikeData(List<RailCommonData> trailerData) {
        TabsData.getInstance().setYouMayAlsoLikeData(trailerData);
    }
    public List<RailCommonData> getYouMayAlsoLikeData() {
        return TabsData.getInstance().getYouMayAlsoLikeData();
    }
    public LiveData<List<AssetCommonBean>> getYouMayAlsoLike(int assetId,
                                                             int counter,
                                                             int assetType,
                                                             Map<String, MultilingualStringValueArray> map
                                                             ) {
        return YouMayAlsoLike.getInstance().fetchSimilarMovie(getApplication().getApplicationContext(), assetId, counter, assetType, map);
    }
    public List<Asset> getTrailer() {
        return TabsData.getInstance().getTrailerData();
    }


    public void setHighLightsData(List<Asset> highLightsDataData) {
        TabsData.getInstance().setHighLightsData(highLightsDataData);
    }

    public List<Asset> getHighLights() {
        return TabsData.getInstance().getHighLightsData();
    }

    public LiveData<List<Asset>> getTrailer(String refId, int assetType) {
        return TrailerFragmentRepository.getInstance().getTrailerAsset(getApplication(), refId, assetType);
    }

    public LiveData<List<Asset>> getHighlight(String refId, int assetType) {
        return TrailerFragmentRepository.getInstance().getHighlightAsset(getApplication(), refId, assetType);
    }

    public LiveData<List<Integer>> getSeasonsListData(int assetId,
                                                      int counter,
                                                      int assetType,
                                                      Map<String, Value> map,
                                                      int layoutType,
                                                      String externalId) {
        return SeasonsLayer.getInstance().loadData(getApplication().getApplicationContext(), assetId, counter, assetType, map, layoutType, externalId);
    }

    public LiveData<List<AssetCommonBean>> callSeasonEpisodes(Asset map, int assetType, int counter, List<Integer> seriesNumberList, int seasonCounter, int layoutType) {
        return EpisodesLayer.getInstance().getEpisodesList(getApplication().getApplicationContext(), map, assetType, counter, seriesNumberList, seasonCounter, layoutType);
    }

    public LiveData<String> getDtvAccountList() {
        return DTVRepository.getInstance().getDtvAccountList(getApplication().getApplicationContext());

    }
}
