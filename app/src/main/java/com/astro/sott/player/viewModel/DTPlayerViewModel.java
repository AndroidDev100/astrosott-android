package com.astro.sott.player.viewModel;

import android.app.Activity;
import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Context;
import androidx.annotation.NonNull;
import android.widget.SeekBar;

import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.player.adapter.TrackItem;
import com.astro.sott.repositories.dtv.DTVRepository;
import com.astro.sott.repositories.liveChannel.LiveChannelRepository;
import com.astro.sott.repositories.player.PlayerRepository;
import com.astro.sott.repositories.splash.SplashRepository;
import com.kaltura.client.types.Asset;
import com.kaltura.playkit.PKMediaEntry;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.player.AudioTrack;
import com.kaltura.playkit.player.TextTrack;
import com.kaltura.playkit.player.VideoTrack;

import java.util.ArrayList;
import java.util.List;

public class DTPlayerViewModel extends AndroidViewModel {

    public DTPlayerViewModel(@NonNull Application application) {
        super(application);

    }

//    public LiveData<Player> initializePlayer(String url, Asset asset, String deviceid) {
//        return PlayerRepository.getInstance().initializePlayer(url, context,asset,deviceid);
//    }


    public LiveData<String> playerCallback(int progress, Activity activity) {
        return PlayerRepository.getInstance().playerLoadedMetadata(progress, getApplication().getApplicationContext(), activity);
    }

    public LiveData<Boolean> playPauseControl() {
        return PlayerRepository.getInstance().playPauseControl(getApplication().getApplicationContext());
    }

    public LiveData<List<VideoTrack>> loadVideotracksWithPlayer() {
        return PlayerRepository.getInstance().setVideoTracksinPlayer();

    }

    public LiveData<ArrayList<TrackItem>> getVideoTrackItems() {
        return PlayerRepository.getInstance().getVideoTrackItems();

    }

    public LiveData<Boolean> changeTrack(String uniqueID) {
        return PlayerRepository.getInstance().changeTrack(uniqueID,getApplication().getApplicationContext());
    }

    public LiveData<List<TextTrack>> loadCaptionWithPlayer() {
        return PlayerRepository.getInstance().setTextTracks();
    }

    public LiveData<TrackItem[]> getTextTrackItems() {
        return PlayerRepository.getInstance().getTextTrackItems();
    }

    public void changeTextTrack(String uniqueId) {
        PlayerRepository.getInstance().changeTextTrack(uniqueId);
    }

    public LiveData<String> getSeekBarProgress(SeekBar seekBar) {
        return PlayerRepository.getInstance().getPlayerCurrentPosition(seekBar);
    }

    public void getPlayerView(SeekBar seekBar) {
        PlayerRepository.getInstance().getViewOfPlayer(seekBar);
    }

    public LiveData<String> sendSeekBarProgress(int progress) {
        return PlayerRepository.getInstance().getSeekBarProgressContinues(progress);
    }

    public LiveData<Boolean> getPlayerState() {
        return PlayerRepository.getInstance().getStateofPlayer();
    }

    public void replayControl() {
        PlayerRepository.getInstance().control();
    }

    public LiveData<List<AudioTrack>> loadAudioWithPlayer() {
        return PlayerRepository.getInstance().setAudioTracks();
    }

    public LiveData<TrackItem[]> getAudioTrackItems() {
        return PlayerRepository.getInstance().getAudioTrackItems();
    }
    public LiveData<Boolean> haveAudioTracks() {
        return PlayerRepository.getInstance().haveAudioTracks();
    }

    public void removeCallBack() {
        PlayerRepository.getInstance().removeCallback();
    }

    public void changeAudioTrack(String uniqueId) {
        PlayerRepository.getInstance().changeAudioTrack(uniqueId);
    }

    public LiveData<Boolean> getPausestate() {
        return PlayerRepository.getInstance().checkPauseState();
    }

    public LiveData<Boolean> getResumeState() {
        return PlayerRepository.getInstance().getResumeStatus();
    }


    public LiveData<Boolean> seekPlayerForward() {
        return PlayerRepository.getInstance().seekPlayerForward();
    }

    public LiveData<Boolean> seekPlayerBackward() {
        return PlayerRepository.getInstance().seekPlayerBackward();
    }


    public LiveData<Boolean> getPlayerProgress() {
        return PlayerRepository.getInstance().getPlayerProgress();
    }

    public void pausePlayer() {
        PlayerRepository.getInstance().pausePlayer();
    }

    public void startPlayer() {
        PlayerRepository.getInstance().startPlayer();
    }


    public LiveData<Player> startPlayerBookmarking(PKMediaEntry mediaEntry, String deviceid,
                                                   Asset asset, int isPurchased,int assetPosition) {
        return PlayerRepository.getInstance().startPlayerBookmarking(getApplication().getApplicationContext(), mediaEntry, deviceid, asset, isPurchased,assetPosition);
    }

    public LiveData<Boolean> getStateOfPlayer() {
        return PlayerRepository.getInstance().getPlayerStateforPlay();
    }

    public LiveData<String> checkAssetType(String fileId) {
        return PlayerRepository.getInstance().getAssetPurchaseType(fileId, getApplication().getApplicationContext());
    }

    public void setResetPlayer() {
        PlayerRepository.getInstance().resetPlayer(getApplication().getApplicationContext());
    }

    public LiveData<Integer> userAssetRuleApi(Long assetId) {
        return PlayerRepository.getInstance().userAssetRule(getApplication().getApplicationContext(), assetId);
    }

    public LiveData<Player> getPlayerObject() {
        return PlayerRepository.getInstance().getPlayerObject();
    }


    public LiveData<CommonResponse> getNumberOfEpisodes(Asset asset, Context context) {
        return PlayerRepository.getInstance().getNumberOfEpisode(asset, context);
    }


    public void changeVideoRatio() {
        PlayerRepository.getInstance().updateVideoRatio();
    }
//    public LiveData<Player> getPlayerObject(Activity activity) {
//        return PlayerRepository.getInstance().startPlayerObject(context,activity);
//    }

    public void clearCallbacks() {
        PlayerRepository.getInstance().removeCallback();
        onCleared();
    }


    public LiveData<Boolean> seekToDuration() {
       return PlayerRepository.getInstance().seekToDuration();
    }

    public LiveData<Boolean> seekToStart() {
        return PlayerRepository.getInstance().seekToStart();
    }

    public LiveData<List<RailCommonData>> loadCatchupData(String externalId, String startTime, int type) {
        return LiveChannelRepository.getInstance().loadCatchupData(getApplication().getApplicationContext(), externalId, startTime,type);
    }

    public LiveData<List<RailCommonData>> liveCatchupData(String externalId) {
        return LiveChannelRepository.getInstance().liveCatchupData(getApplication().getApplicationContext(), externalId);
    }

    public LiveData<RailCommonData> getSpecificAsset(String assetId) {
        return new SplashRepository().getSpecificAsset(getApplication(), assetId);
    }

    public LiveData<String> getDtvAccountList() {
        return DTVRepository.getInstance().getDtvAccountList(getApplication().getApplicationContext());
    }

    public LiveData<String> getHungamaUrl(String providerExternalContentId) {
        return DTVRepository.getInstance().getHungamaUrl(getApplication().getApplicationContext(),providerExternalContentId);
    }
}
