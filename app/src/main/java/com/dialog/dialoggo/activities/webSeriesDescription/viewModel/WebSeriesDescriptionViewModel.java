package com.dialog.dialoggo.activities.webSeriesDescription.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.dialog.dialoggo.activities.webEpisodeDescription.layers.ClipLayer;
import com.dialog.dialoggo.activities.webEpisodeDescription.layers.EpisodesLayer;
import com.dialog.dialoggo.activities.webEpisodeDescription.layers.SeasonsLayer;
import com.dialog.dialoggo.baseModel.CategoryRailLayer;
import com.dialog.dialoggo.baseModel.ChannelLayer;
import com.dialog.dialoggo.beanModel.VIUChannel;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.repositories.dtv.DTVRepository;
import com.dialog.dialoggo.repositories.splash.SplashRepository;
import com.dialog.dialoggo.repositories.webSeriesDescription.WebSeriesDescriptionRepository;
import com.dialog.dialoggo.utils.helpers.AssetContent;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;

import java.util.List;
import java.util.Map;

public class WebSeriesDescriptionViewModel extends AndroidViewModel {
    public WebSeriesDescriptionViewModel(@NonNull Application application) {
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

//    public LiveData<String> getSubTitleLangLivedata(Map<String, MultilingualStringValueArray> map) {
//        return AssetContent.getSubTitleLang(map);
//    }

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

    public LiveData<CommonResponse> deleteWatchlist(long assetId) {
        return WebSeriesDescriptionRepository.getInstance().deleteFromWatchlist(assetId, getApplication().getApplicationContext());
    }

    public LiveData<CommonResponse> getNumberOfEpisodes(Asset asset) {
        return WebSeriesDescriptionRepository.getInstance().getNumberOfEpisode(asset, getApplication().getApplicationContext());
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
