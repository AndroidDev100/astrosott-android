package com.astro.sott.activities.webEpisodeDescription.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.astro.sott.activities.webEpisodeDescription.layers.EpisodesLayer;
import com.astro.sott.baseModel.ChannelLayer;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.activities.webEpisodeDescription.layers.ClipLayer;
import com.astro.sott.activities.webEpisodeDescription.layers.SeasonsLayer;
import com.astro.sott.baseModel.CategoryRailLayer;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.repositories.dtv.DTVRepository;
import com.astro.sott.repositories.splash.SplashRepository;
import com.astro.sott.repositories.webEpisodeDescription.WebEpisodeDescriptionRepository;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;

import java.util.List;
import java.util.Map;

public class WebEpisodeDescriptionViewModel extends AndroidViewModel {

    public WebEpisodeDescriptionViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getCrewLiveDAta(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getCrewData(map);
    }

    public LiveData<String> getCastLiveData(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getCastData(map);
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
                                                      Map<String,
                                                              MultilingualStringValueArray> map,
                                                      int layoutType,
                                                      int seriesMediaTyp) {
        return SeasonsLayer.getInstance().loadData(getApplication().getApplicationContext(), assetId, counter, assetType, map, layoutType, seriesMediaTyp);
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

    public LiveData<String> getUrlfromModel(Asset asset,String videoResolution) {
        return AssetContent.getUrl(asset, videoResolution);
    }

    public LiveData<Asset> getAssetFromClip(String ref_id, Integer type) {
        return WebEpisodeDescriptionRepository.getInstance().getAssetFromClip(getApplication().getApplicationContext(), ref_id);
    }

    public LiveData<String> getRefIdLivedata(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getRefIdData(map);
    }

    public LiveData<CommonResponse> listAllwatchList(String assetId) {
        return WebEpisodeDescriptionRepository.getInstance().compareWatchlist(assetId, getApplication().getApplicationContext());
    }

    public LiveData<CommonResponse> addToWatchlist(String id, String titleName, int playlistId) {
        return WebEpisodeDescriptionRepository.getInstance().addToWatchlist(id, titleName, getApplication().getApplicationContext(),playlistId);
    }

    public LiveData<CommonResponse> deleteWatchlist(String idfromAssetWatchlist) {
        return WebEpisodeDescriptionRepository.getInstance().deleteFromWatchlist(idfromAssetWatchlist, getApplication().getApplicationContext());
    }

    public LiveData<List<AssetCommonBean>> callSeasonEpisodes(Map<String, MultilingualStringValueArray> map, int assetType, int counter, List<Integer> seriesNumberList, int seasonCounter, int layoutType) {
        return EpisodesLayer.getInstance().getEpisodesList(getApplication().getApplicationContext(), map, assetType, counter, seriesNumberList, seasonCounter, layoutType);
    }

    public LiveData<AssetCommonBean> getChannelList(int screen_id) {
        return ChannelLayer.getInstance().getChannelList(getApplication().getApplicationContext(), screen_id);
    }

    public LiveData<String> getDtvAccountList() {
        return DTVRepository.getInstance().getDtvAccountList(getApplication().getApplicationContext());
    }

}
