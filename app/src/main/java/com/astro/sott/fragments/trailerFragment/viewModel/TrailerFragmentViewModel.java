package com.astro.sott.fragments.trailerFragment.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.astro.sott.activities.movieDescription.layers.YouMayAlsoLike;
import com.astro.sott.activities.webEpisodeDescription.layers.EpisodesLayer;
import com.astro.sott.activities.webEpisodeDescription.layers.SeasonsLayer;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.repositories.dtv.DTVRepository;
import com.astro.sott.utils.TabsData;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.repositories.trailerFragment.TrailerFragmentRepository;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Value;

import java.util.List;
import java.util.Map;

public class TrailerFragmentViewModel extends AndroidViewModel {
    public TrailerFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    private List<Asset> finalList;
    private List<AssetCommonBean> episodeList;

    public LiveData<String> getRefIdLivedata(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getRefIdData(map);
    }

    public void setTrailerData(List<Asset> trailerData) {
        TabsData.getInstance().setTrailerData(trailerData);
    }

    public void setMovieShows(List<Asset> trailerData) {
        TabsData.getInstance().setMovieShows(trailerData);
    }

    public List<Asset> getMovieShows() {
        return TabsData.getInstance().getMovieShows();
    }

    public void setSeriesShows(List<Asset> seriesShows) {
        TabsData.getInstance().setSeriesShows(seriesShows);
    }

    public List<Asset> getSeriesShows() {
        return TabsData.getInstance().getSeriesShows();
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

    public void setSeasonList(List<Integer> seasonList) {
        TabsData.getInstance().setSeasonList(seasonList);
    }

    public List<Integer> getSeasonList() {
        return TabsData.getInstance().getSeasonList();
    }

    public List<AssetCommonBean> getOpenSeriesData() {
        return TabsData.getInstance().getOpenSeriesData();
    }

    public List<AssetCommonBean> getClosedSeriesData() {
        return TabsData.getInstance().getClosedSeriesData();
    }

    public void setClosedSeriesData(List<AssetCommonBean> closedSeriesData) {
        TabsData.getInstance().setClosedSeriesData(closedSeriesData);
    }

    public void setOpenSeriesData(List<AssetCommonBean> openSeriesData) {
        TabsData.getInstance().setOpenSeriesData(openSeriesData);
    }

    public List<Asset> getHighLights() {
        return TabsData.getInstance().getHighLightsData();
    }

    public LiveData<List<Asset>> getTrailer(String refId, int assetType) {
        return TrailerFragmentRepository.getInstance().getTrailerAsset(getApplication(), refId, assetType);
    }

    public LiveData<List<Asset>> getShows(String refId, int assetType) {
        return TrailerFragmentRepository.getInstance().getShowFromBoxSet(getApplication(), refId, assetType);
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

    public LiveData<List<AssetCommonBean>> callSeasonEpisodes(Asset map, int assetType, int counter, List<Integer> seriesNumberList, int seasonCounter, int layoutType, String sortType, LifecycleOwner owner) {
        TabsData.getInstance().setSortType(sortType);
        return checkHasEpisodeNumberForSeason(getApplication().getApplicationContext(), map, assetType, counter, seasonCounter, layoutType, sortType, owner, seriesNumberList);
    }

    public LiveData<List<AssetCommonBean>> callEpisodes(Asset map, int assetType, int counter, int seasonCounter, int layoutType, String sortType, LifecycleOwner owner) {
        TabsData.getInstance().setSortType(sortType);
        return checkHasEpisodeNumber(getApplication().getApplicationContext(), map, assetType, counter, seasonCounter, layoutType, sortType, owner);
    }

    private LiveData<List<AssetCommonBean>> checkHasEpisodeNumberForSeason(Context applicationContext, Asset map, int assetType, int counter, int seasonCounter, int layoutType, String sortType, LifecycleOwner owner, List<Integer> seriesNumberList) {
        MutableLiveData<List<AssetCommonBean>> mutableLiveData = new MutableLiveData<>();
        EpisodesLayer.getInstance().getEpisodesList(getApplication().getApplicationContext(), map, assetType, counter, seriesNumberList, seasonCounter, layoutType, sortType).observe(owner, assetCommonBeans -> {
            if (assetCommonBeans.get(0) != null && assetCommonBeans.get(0).getStatus()) {
                if (assetCommonBeans.get(0).getRailAssetList() != null && assetCommonBeans.get(0).getRailAssetList().get(0) != null && assetCommonBeans.get(0).getRailAssetList().get(0).getObject() != null && assetCommonBeans.get(0).getRailAssetList().get(0).getObject().getMetas() != null) {
                    int episodeValue = AppCommonMethods.getEpisodeNumber(assetCommonBeans.get(0).getRailAssetList().get(0).getObject().getMetas());
                    if (episodeValue == -1) {
                        callEpisodesWithTitleSortType(applicationContext, map, assetType, counter, seasonCounter, layoutType, AppLevelConstants.KEY_TITLE_SORT, mutableLiveData, owner, "season", seriesNumberList);
                    } else {
                        mutableLiveData.postValue(assetCommonBeans);
                    }

                } else {
                    mutableLiveData.postValue(assetCommonBeans);

                }
            } else {
                mutableLiveData.postValue(assetCommonBeans);

            }
        });
        return mutableLiveData;

    }

    private LiveData<List<AssetCommonBean>> checkHasEpisodeNumber(Context applicationContext, Asset map, int assetType, int counter, int seasonCounter, int layoutType, String sortType, LifecycleOwner owner) {
        MutableLiveData<List<AssetCommonBean>> mutableLiveData = new MutableLiveData<>();
        EpisodesLayer.getInstance().getEpisodesListWithoutSeason(applicationContext, map, assetType, counter, seasonCounter, layoutType, sortType).observe(owner, assetCommonBeans -> {
            if (assetCommonBeans.get(0) != null && assetCommonBeans.get(0).getStatus()) {
                if (assetCommonBeans.get(0).getRailAssetList() != null && assetCommonBeans.get(0).getRailAssetList().get(0) != null && assetCommonBeans.get(0).getRailAssetList().get(0).getObject() != null && assetCommonBeans.get(0).getRailAssetList().get(0).getObject().getMetas() != null) {
                    int episodeValue = AppCommonMethods.getEpisodeNumber(assetCommonBeans.get(0).getRailAssetList().get(0).getObject().getMetas());
                    if (episodeValue == -1) {
                        callEpisodesWithTitleSortType(applicationContext, map, assetType, counter, seasonCounter, layoutType, "TitleSortName", mutableLiveData, owner, "", null);
                    } else {
                        mutableLiveData.postValue(assetCommonBeans);
                    }

                } else {
                    mutableLiveData.postValue(assetCommonBeans);

                }
            } else {
                mutableLiveData.postValue(assetCommonBeans);

            }
        });
        return mutableLiveData;

    }

    private void callEpisodesWithTitleSortType(Context applicationContext, Asset map, int assetType, int counter, int seasonCounter, int layoutType, String sortType, MutableLiveData<List<AssetCommonBean>> mutableLiveData, LifecycleOwner owner, String type, List<Integer> seriesNumberList) {
        if (type.equalsIgnoreCase("season")) {
            EpisodesLayer.getInstance().getEpisodesList(getApplication().getApplicationContext(), map, assetType, counter, seriesNumberList, seasonCounter, layoutType, sortType).observe(owner, assetCommonBeans -> {
                mutableLiveData.postValue(assetCommonBeans);
            });
        } else {
            EpisodesLayer.getInstance().getEpisodesListWithoutSeason(applicationContext, map, assetType, counter, seasonCounter, layoutType, sortType).observe(owner, assetCommonBeans -> {
                TabsData.getInstance().setSortType(sortType);
                mutableLiveData.postValue(assetCommonBeans);
            });
        }

    }


    public LiveData<String> getDtvAccountList() {
        return DTVRepository.getInstance().getDtvAccountList(getApplication().getApplicationContext());

    }

    public String getParentRefId(Map<String, MultilingualStringValueArray> tags) {
        return AssetContent.getParentRefId(tags);
    }
}
