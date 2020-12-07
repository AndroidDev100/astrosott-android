package com.dialog.dialoggo.activities.movieDescription.viewModel;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.activities.movieDescription.layers.SimilarMovie;
import com.dialog.dialoggo.activities.movieDescription.layers.YouMayAlsoLike;
import com.dialog.dialoggo.baseModel.CategoryRailLayer;
import com.dialog.dialoggo.baseModel.ChannelLayer;
import com.dialog.dialoggo.baseModel.MovieBaseViewModel;
import com.dialog.dialoggo.beanModel.VIUChannel;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.repositories.dtv.DTVRepository;
import com.dialog.dialoggo.repositories.movieDescription.MovieDescriptionRepository;
import com.dialog.dialoggo.repositories.player.PlayerRepository;
import com.dialog.dialoggo.utils.helpers.AssetContent;
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
        return YouMayAlsoLike.getInstance().fetchSimilarMovie(getApplication().getApplicationContext(), assetId, counter, assetType, map, layoutType, screen_id, asset);
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
