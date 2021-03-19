package com.astro.sott.activities.webSeriesDescription.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.astro.sott.baseModel.ChannelLayer;
import com.astro.sott.repositories.movieDescription.MovieDescriptionRepository;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.activities.webEpisodeDescription.layers.ClipLayer;
import com.astro.sott.activities.webEpisodeDescription.layers.EpisodesLayer;
import com.astro.sott.activities.webEpisodeDescription.layers.SeasonsLayer;
import com.astro.sott.baseModel.CategoryRailLayer;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.repositories.dtv.DTVRepository;
import com.astro.sott.repositories.splash.SplashRepository;
import com.astro.sott.repositories.webSeriesDescription.WebSeriesDescriptionRepository;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.Bookmark;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Value;

import java.util.List;
import java.util.Map;

public class WebSeriesDescriptionViewModel extends AndroidViewModel {
    public WebSeriesDescriptionViewModel(@NonNull Application application) {
        super(application);

    }

    public LiveData<String> getSubGenreLivedata(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getSubGenredata(map);
    }

    public LiveData<List<AssetCommonBean>> callEpisodes(Asset map, int assetType, int counter, int seasonCounter, int layoutType) {
        return EpisodesLayer.getInstance().getEpisodesListWithoutSeason(getApplication().getApplicationContext(), map, assetType, counter, seasonCounter, layoutType);
    }

    public LiveData<String> getSubTitleLanguageLiveData(Map<String, MultilingualStringValueArray> map) {

        return AssetContent.getSubTitleLanguageData(map);
    }

    public LiveData<String> getCrewLiveDAta(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getCrewData(map);
    }

    public boolean isXofferWindow(String xofferValue) {
        return AppCommonMethods.isXofferWindow(xofferValue);
    }

    public LiveData<String> getCastLiveData(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getCastData(map);
    }

    public boolean getPlayBackControl(Map<String, Value> metas) {
        return AssetContent.plabackControl(metas);
    }

    public LiveData<String> getLanguageLiveData(Map<String, MultilingualStringValueArray> map) {

        return AssetContent.getLanguageData(map);
    }

    public LiveData<RailCommonData> getSpecificAsset(String assetId) {
        return new SplashRepository().getSpecificAsset(getApplication().getApplicationContext(), assetId);
    }


    public LiveData<List<AssetCommonBean>> getListLiveData(long channelID, List<VIUChannel> list, int counter, int swipeToRefresh) {
        return CategoryRailLayer.getInstance().loadData(getApplication().getApplicationContext(), channelID, list, counter, swipeToRefresh);
    }


    public LiveData<List<Integer>> getSeasonsListData(int assetId,
                                                      int counter,
                                                      int assetType,
                                                      Map<String, Value> map,
                                                      int layoutType,
                                                      String seriesMediaTyp) {
        return SeasonsLayer.getInstance().loadData(getApplication().getApplicationContext(), assetId, counter, assetType, map, layoutType, seriesMediaTyp);
    }

    public LiveData<List<Bookmark>> getEpisodeProgress(String assetList) {
        return EpisodesLayer.getInstance().getEpisodeProgress(getApplication().getApplicationContext(), assetList);
    }

    public LiveData<List<AssetCommonBean>> getClipData(int assetId,
                                                       int counter,
                                                       int assetType,
                                                       Map<String,
                                                               MultilingualStringValueArray> map,
                                                       int layoutType,
                                                       int seriesMediaTyp) {
        return ClipLayer.getInstance().loadData(getApplication().getApplicationContext(), assetId, counter, assetType, map, layoutType, seriesMediaTyp);
    }

    public LiveData<String> getGenreLivedata(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getGenredata(map);
    }

    public LiveData<CommonResponse> listAllwatchList(String assetId) {
        return MovieDescriptionRepository.getInstance().compareWatchlist(assetId, getApplication().getApplicationContext());
    }

    public LiveData<CommonResponse> addToWatchlist(String id, String titleName, int playlistId) {
        return MovieDescriptionRepository.getInstance().addToWatchlist(id, titleName, getApplication().getApplicationContext(), playlistId);
    }

    public LiveData<String> getImage(Asset asset) {
        return AssetContent.getImageUrl(asset);
    }


    public LiveData<String> getRefIdLivedata(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getRefIdData(map);
    }

    public LiveData<RailCommonData> getClipData(String ref_id) {
        return WebSeriesDescriptionRepository.getInstance().getClipData(getApplication().getApplicationContext(), ref_id);
    }

    public LiveData<String> listAllSeriesList(long assetID) {
        return WebSeriesDescriptionRepository.getInstance().seriesFollowList(getApplication().getApplicationContext(), assetID);
    }

    public LiveData<CommonResponse> addToFollowlist(long assetId) {
        return WebSeriesDescriptionRepository.getInstance().addToFollowlist(assetId, getApplication());
    }

    public LiveData<CommonResponse> deleteWatchlist(String idfromAssetWatchlist) {
        return MovieDescriptionRepository.getInstance().deleteFromWatchlist(idfromAssetWatchlist, getApplication().getApplicationContext());
    }

    public LiveData<CommonResponse> getNumberOfEpisodes(Asset asset) {
        return WebSeriesDescriptionRepository.getInstance().getNumberOfEpisode(asset, getApplication().getApplicationContext());
    }

    public LiveData<List<AssetCommonBean>> callSeasonEpisodes(Asset map, int assetType, int counter, List<Integer> seriesNumberList, int seasonCounter, int layoutType) {
        return EpisodesLayer.getInstance().getEpisodesList(getApplication().getApplicationContext(), map, assetType, counter, seriesNumberList, seasonCounter, layoutType);
    }

    public LiveData<AssetCommonBean> getChannelList(int screen_id) {
        return ChannelLayer.getInstance().getChannelList(getApplication().getApplicationContext(), screen_id);
    }

    public LiveData<String> getDtvAccountList() {
        return DTVRepository.getInstance().getDtvAccountList(getApplication().getApplicationContext());
    }

}
