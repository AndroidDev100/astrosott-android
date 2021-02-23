package com.astro.sott.activities.movieDescription.viewModel;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.astro.sott.activities.movieDescription.layers.SimilarMovie;
import com.astro.sott.activities.movieDescription.layers.YouMayAlsoLike;
import com.astro.sott.baseModel.CategoryRailLayer;
import com.astro.sott.baseModel.ChannelLayer;
import com.astro.sott.baseModel.MovieBaseViewModel;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.repositories.dtv.DTVRepository;
import com.astro.sott.repositories.movieDescription.MovieDescriptionRepository;
import com.astro.sott.repositories.player.PlayerRepository;
import com.astro.sott.utils.helpers.AssetContent;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;

import java.util.List;
import java.util.Map;

public class MovieDescriptionViewModel extends MovieBaseViewModel {


    public MovieDescriptionViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<String> getCrewLiveDAta(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getCrewData(map);
    }

    public LiveData<String> getCastLiveData(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getCastData(map);
    }

    public LiveData<List<AssetCommonBean>> getListLiveData(long channelID, List<VIUChannel> list, int counter, int swipeToRefresh) {
        return CategoryRailLayer.getInstance().loadData(getApplication().getApplicationContext(), channelID, list, counter, swipeToRefresh);
    }

    public LiveData<List<AssetCommonBean>> getSimilarMovie(int assetId,
                                                           int counter,
                                                           int assetType,
                                                           Map<String, MultilingualStringValueArray> map,
                                                           int layoutType, int screen_id, Asset asset) {
        return SimilarMovie.getInstance().fetchSimilarMovie(getApplication().getApplicationContext(), assetId, counter, assetType, map, layoutType, screen_id, asset);
    }

    public LiveData<List<AssetCommonBean>> getYouMayAlsoLike(int assetId,
                                                             int counter,
                                                             int assetType,
                                                             Map<String, MultilingualStringValueArray> map,
                                                             int layoutType, int screen_id, Asset asset) {
        return YouMayAlsoLike.getInstance().fetchSimilarMovie(getApplication().getApplicationContext(), assetId, counter, assetType, map);
    }


    public LiveData<AssetCommonBean> getChannelList(int screen_id) {
        return ChannelLayer.getInstance().getChannelList(getApplication().getApplicationContext(), screen_id);
    }

    public void resetObject() {
        // VideoFragmentRepository.resetObject();
    }

    public LiveData<String> getGenreLivedata(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getGenredata(map);
    }

    public LiveData<String> getSubGenreLivedata(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getSubGenredata(map);
    }


    public LiveData<String> getUrlfromModel(Asset asset, String videoResolution) {
        return AssetContent.getUrl(asset,videoResolution);
    }

    public LiveData<String> getRefIdLivedata(Map<String, MultilingualStringValueArray> map) {
        return AssetContent.getRefIdData(map);
    }

    public LiveData<String> getTrailorURL(String ref_id, int assetType) {
        return MovieDescriptionRepository.getInstance().getTrailorURL(getApplication().getApplicationContext(), ref_id, assetType);
    }

    public LiveData<Asset> getAssetFromTrailor(String ref_id, Integer type) {
        return MovieDescriptionRepository.getInstance().getAssetFromTrailor(getApplication().getApplicationContext(), ref_id);
    }

    public LiveData<String> getLanguageLiveData(Map<String, MultilingualStringValueArray> map) {

        return AssetContent.getLanguageData(map);
    }

    public LiveData<String> getSubTitleLanguageLiveData(Map<String, MultilingualStringValueArray> map) {

        return AssetContent.getSubTitleLanguageData(map);
    }

    public LiveData<CommonResponse> listAllwatchList(String assetId) {
        return MovieDescriptionRepository.getInstance().compareWatchlist(assetId, getApplication().getApplicationContext());
    }

    public LiveData<CommonResponse> addToWatchlist(String id, String titleName, int playlistId) {
        return MovieDescriptionRepository.getInstance().addToWatchlist(id, titleName, getApplication().getApplicationContext(),playlistId);
    }

    public LiveData<CommonResponse> deleteWatchlist(String idfromAssetWatchlist) {
        return MovieDescriptionRepository.getInstance().deleteFromWatchlist(idfromAssetWatchlist, getApplication().getApplicationContext());
    }

    public LiveData<Integer> userAssetRuleApi(Long assetId) {
        return PlayerRepository.getInstance().userAssetRule(getApplication().getApplicationContext(), assetId);
    }

    public LiveData<String> getDtvAccountList() {
        return DTVRepository.getInstance().getDtvAccountList(getApplication().getApplicationContext());
    }
}
