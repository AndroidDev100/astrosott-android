package com.astro.sott.repositories.player;

import android.app.Activity;
import android.app.AlertDialog;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import androidx.core.content.ContextCompat;

import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.astro.sott.activities.language.ui.LanguageSettingsActivity;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.thirdParty.conViva.ConvivaManager;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.player.adapter.TrackItem;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.shimmer.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MediaAsset;
import com.kaltura.playkit.PKLog;
import com.kaltura.playkit.PKMediaConfig;
import com.kaltura.playkit.PKMediaEntry;
import com.kaltura.playkit.PKPluginConfigs;
import com.kaltura.playkit.PlayKitManager;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.PlayerEvent;
import com.kaltura.playkit.player.ABRSettings;
import com.kaltura.playkit.player.AudioTrack;
import com.kaltura.playkit.player.LoadControlBuffers;
import com.kaltura.playkit.player.PKAspectRatioResizeMode;
import com.kaltura.playkit.player.PKTracks;
import com.kaltura.playkit.player.TextTrack;
import com.kaltura.playkit.player.VideoTrack;
import com.kaltura.playkit.plugins.ima.IMAConfig;
import com.kaltura.playkit.plugins.ima.IMAPlugin;
import com.kaltura.playkit.plugins.kava.KavaAnalyticsConfig;
import com.kaltura.playkit.plugins.kava.KavaAnalyticsPlugin;
import com.kaltura.playkit.plugins.ott.PhoenixAnalyticsConfig;
import com.kaltura.playkit.plugins.ott.PhoenixAnalyticsEvent;
import com.kaltura.playkit.plugins.ott.PhoenixAnalyticsPlugin;
import com.kaltura.playkit.plugins.playback.KalturaPlaybackRequestAdapter;
import com.kaltura.playkit.plugins.playback.KalturaUDRMLicenseRequestAdapter;
import com.kaltura.playkit.providers.MediaEntryProvider;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.kaltura.client.APIOkRequestsExecutor.TAG;


public class PlayerRepository {
    private static final Long START_POSITION = 0L;
    private static final PKLog log = PKLog.get("PlayerRepository");
    public static PKMediaConfig mediaConfig;
    private static PlayerRepository INSTANCE = null;
    private final Handler mHandler = new Handler();
    MutableLiveData<Boolean> isAudioTracks;
    PKPluginConfigs playerPluginConfig;
    IMAConfig imaConfig;
    private MutableLiveData<String> integerMutableLiveData;
    private SeekBar seekBar1;
    private List<VideoTrack> videoTracks;
    private List<TextTrack> textTracks;
    private List<AudioTrack> audioTracks;
    private MutableLiveData<Boolean> booleanMutableLiveData;
    private Player player;
    private TrackItem[] videoTrackItems;
    private TrackItem[] textTrackItems;
    private TrackItem[] audioTrackItems;
    private ArrayList<TrackItem> trackItemsList;
    private PKTracks tracks;
    private KavaAnalyticsConfig kavaAnalyticsConfig;
    private MediaEntryProvider mediaProvider;
    private StringBuilder formatBuilder;
    private Formatter formatter;
    private final Runnable updateTimeTask = new Runnable() {
        public void run() {
            seekBar1.setProgress(((int) player.getCurrentPosition()));
            seekBar1.setMax(((int) player.getDuration()));
            mHandler.postDelayed(this, 100);
            integerMutableLiveData.postValue(stringForTime(player.getCurrentPosition()));

        }
    };
    private boolean isFilled = false;

    private PlayerRepository() {
    }

    public static PlayerRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerRepository();
        }

        return (INSTANCE);
    }

    public LiveData<String> playerLoadedMetadata(final int progress, final Context context, final Activity activity) {
        final MutableLiveData<String> playerMutableLiveData = new MutableLiveData<>();
        //PlayerListeners


        player.addListener(this, PlayerEvent.error, event -> {
            PlayerEvent.Type error = ((PlayerEvent.Error) event).type;
            if (event.error.isFatal()) {
                //  player.pause();
                playerMutableLiveData.postValue("");
            }


        });


        player.getSettings().setSurfaceAspectRatioResizeMode(PKAspectRatioResizeMode.fill);

        player.addListener(this, PlayerEvent.Type.LOADED_METADATA, event -> {
            Constants.duration = stringForTime(player.getDuration());
            playerMutableLiveData.postValue(Constants.duration);
            // player.seekTo(progress * 1000);
            //    player.play();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.w("playerEvent", "loadedmetadata");
                    checkSubtitleAndAudioFromSettings();
                }
            }, 500);

        });

        player.addListener(this, PlayerEvent.Type.PLAY, event -> {
            Log.w("playerEvent", "play");
        });

        player.addListener(this, PlayerEvent.Type.ENDED, event -> {
            booleanMutableLiveData.postValue(true);
            mHandler.removeCallbacks(updateTimeTask);
        });

        player.addListener(this, PlayerEvent.Type.TRACKS_AVAILABLE, event -> {
            PlayerEvent.TracksAvailable tracksAvailable = (PlayerEvent.TracksAvailable) event;
            populateSpinnersWithTrackInfo(tracksAvailable.tracksInfo, context);
            Gson gson = new Gson();
            Log.e("Tracks Available", gson.toJson(tracksAvailable.tracksInfo));
            Log.w("playerEvent", "trackAvailable");
        });

/*
        player.addListener(this, PlayerEvent.error, event -> {
            PlayerEvent.Type error = ((PlayerEvent.Error) event).type;
//                    if (event.error.isFatal()){
//                        player.pause();
//                        playerMutableLiveData.postValue("");
//                    }else{
//                        Toast.makeText(activity,"Hello",Toast.LENGTH_LONG).show();
//                    }
            player.pause();
            playerMutableLiveData.postValue("");
        });
*/

        player.addListener(this, PhoenixAnalyticsEvent.Type.CONCURRENCY_ERROR, event -> {
            PhoenixAnalyticsEvent.ConcurrencyErrorEvent concurrencyErrorEvent = (PhoenixAnalyticsEvent.ConcurrencyErrorEvent) event;
            if (activity != null) {
                player.stop();
                player.destroy();
                openConcurrencyDialouge(activity);
            }

        });
        player.addListener(this, PlayerEvent.surfaceAspectRationSizeModeChanged, event -> {
            log.d("resizeMode updated" + event.resizeMode);
        });
        return playerMutableLiveData;
    }

    private void checkSubtitleAndAudioFromSettings() {
        try {
            if (player != null) {
                if (new KsPreferenceKey(context).getAudioLanguageIndex() > -1 && !new KsPreferenceKey(context).getAudioLangKey().equalsIgnoreCase("")) {
                    if (tracks != null && tracks.getAudioTracks().size() > 0) {
                        for (int i = 0; i < tracks.getAudioTracks().size(); i++) {

                            AudioTrack audioTrackInfo = audioTracks.get(i);
                            if (audioTrackInfo.isAdaptive()) {

                            } else {
                                String lang = audioTrackInfo.getLanguage();
                                Log.w("audioAndSubtitleSelect", lang + "  " + new KsPreferenceKey(context).getAudioLangKey());
                                if (lang.equalsIgnoreCase(new KsPreferenceKey(context).getAudioLangKey())) {
                                    String uniqueId = audioTrackInfo.getUniqueId();
                                    Log.w("audioAndSubtitleSelect", uniqueId);
                                    changeAudioTrack(uniqueId);
                                    break;
                                }
                                // changeAudioTrack();
                            }
                        }
                    }
                }
            }

            if (player != null) {
                if (new KsPreferenceKey(context).getSubtitleLanguageIndex() > -1 && !new KsPreferenceKey(context).getSubTitleLangKey().equalsIgnoreCase("")) {
                    if (tracks != null && tracks.getTextTracks().size() > 0) {
                        //TrackItem[] trackItems = new TrackItem[tracks.getTextTracks().size()];
                        for (int i = 0; i < tracks.getTextTracks().size(); i++) {
                            TextTrack textTrackInfo = tracks.getTextTracks().get(i);
                            String lang = textTrackInfo.getLanguage();
                            Log.w("audioAndSubtitleSelect", lang + "  2" + new KsPreferenceKey(context).getSubTitleLangKey());
                            if (lang.equalsIgnoreCase(new KsPreferenceKey(context).getSubTitleLangKey())) {
                                String uniqueId = textTrackInfo.getUniqueId();
                                Log.w("audioAndSubtitleSelect", uniqueId);
                                changeTextTrack(uniqueId);
                                break;
                            }
                        }
                    }
                }
            }


            //  changeTextTrack();
        } catch (Exception ignored) {

        }
    }

    private String calculatePlayerTime(long milliseconds) {
        String finalTimerString;
        String secondsString;

        //Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours == 0) {
            finalTimerString = "";
        } else {
            finalTimerString = hours + ":";
        }

        // Pre appending 0 to seconds if it is one digit
        if (seconds == 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    public void destroCallBacks() {
        if (mHandler != null && updateTimeTask != null)
            mHandler.removeCallbacks(updateTimeTask);
    }

    public LiveData<Boolean> playPauseControl(Context context) {
        final MutableLiveData<Boolean> playPause = new MutableLiveData<>();
        if (player != null) {
            if (player.isPlaying()) {
                ConvivaManager.convivaPlayerPauseReportRequest();
                playPause.postValue(false);
                player.pause();

                //  mHandler.removeCallbacks(updateTimeTask);
            } else {
                if (NetworkConnectivity.isOnline(context)) {
                    playPause.postValue(true);
                    player.play();
                    mHandler.postDelayed(updateTimeTask, 100);
                } else {
                    playPause.postValue(false);
                }

            }
        }
        return playPause;
    }

    private void populateSpinnersWithTrackInfo(PKTracks tracksInfo, Context context) {
        tracks = tracksInfo;
        videoTracks = new ArrayList<>();
        textTracks = new ArrayList<>();
        audioTracks = new ArrayList<>();
        if (tracksInfo.getVideoTracks().size() > 0) {
            videoTracks = tracksInfo.getVideoTracks();
        }
        if (tracksInfo.getAudioTracks().size() > 0) {
            audioTracks = tracksInfo.getAudioTracks();
        }
        if (tracksInfo.getTextTracks().size() > 0) {
            textTracks = tracksInfo.getTextTracks();
        }

        trackItemsList = buildVideoTrackItems(tracksInfo.getVideoTracks(), context);
        audioTrackItems = buildAudioTrackItems(tracksInfo.getAudioTracks());
        Log.w("audioTrackItems", "aa  " + audioTrackItems.length);
        textTrackItems = buildTextTrackItems(tracksInfo.getTextTracks());
    }

    private ArrayList<TrackItem> buildVideoTrackItems(List<VideoTrack> videoTracks, Context context) {
        int lowBitrate = -1;
        int highBitrate = -1;
        ArrayList<TrackItem> arrayList = new ArrayList<>();
        boolean track1 = true;
        boolean track2 = true;
        boolean track3 = true;

        for (int i = 0; i < videoTracks.size(); i++) {
            VideoTrack videoTrackInfo = videoTracks.get(i);
            Log.e("tracksVideo", videoTracks.get(i).getBitrate() + "");

            if (videoTrackInfo.isAdaptive()) {
                // arrayList.add(new TrackItem(AppLevelConstants.AUTO, videoTrackInfo.getUniqueId(), context.getString(R.string.auto_description)));
            } else {
//                if (i == 1) {
//                    if (videoTrackInfo.getBitrate()>0 && videoTrackInfo.getBitrate() < Long.valueOf(KsPreferenceKey.getInstance(context).getLowBitrateMaxLimit())) {
//                        arrayList.add(new TrackItem(AppLevelConstants.LOW, videoTrackInfo.getUniqueId(), context.getString(R.string.low_description)));
//                    }
//                } else if (i == 2) {
//                    if (videoTrackInfo.getBitrate()>0 && videoTrackInfo.getBitrate() < Long.valueOf(KsPreferenceKey.getInstance(context).getMediumBitrateMaxLimit())) {
//                        arrayList.add(new TrackItem(AppLevelConstants.MEDIUM, videoTrackInfo.getUniqueId(), context.getString(R.string.medium_description)));
//                    }
//                } else if (i == 3) {
//                    {
//                        if (videoTrackInfo.getBitrate()>0 && videoTrackInfo.getBitrate() < Long.valueOf(KsPreferenceKey.getInstance(context).getHighBitrateMaxLimit())) {
//                            arrayList.add(new TrackItem(AppLevelConstants.HIGH, videoTrackInfo.getUniqueId(), context.getString(R.string.high_description)));
//                        }
//                    }
//                }

                if (videoTrackInfo.getBitrate() > 0 && videoTrackInfo.getBitrate() < Long.valueOf(KsPreferenceKey.getInstance(context).getLowBitrateMaxLimit())) {
                    if (track1) {
                        arrayList.add(new TrackItem(AppLevelConstants.LOW, videoTrackInfo.getUniqueId(), context.getString(R.string.low_description)));
                        track1 = false;
                    }
                } else if (videoTrackInfo.getBitrate() > Long.valueOf(KsPreferenceKey.getInstance(context).getLowBitrateMaxLimit()) && videoTrackInfo.getBitrate() < Long.valueOf(KsPreferenceKey.getInstance(context).getMediumBitrateMaxLimit())) {
                    if (track2) {
                        arrayList.add(new TrackItem(AppLevelConstants.MEDIUM, videoTrackInfo.getUniqueId(), context.getString(R.string.medium_description)));
                        track2 = false;
                    }
                } else if (videoTrackInfo.getBitrate() > Long.valueOf(KsPreferenceKey.getInstance(context).getMediumBitrateMaxLimit()) && videoTrackInfo.getBitrate() < Long.valueOf(KsPreferenceKey.getInstance(context).getHighBitrateMaxLimit())) {
                    if (track3) {
                        arrayList.add(new TrackItem(AppLevelConstants.HIGH, videoTrackInfo.getUniqueId(), context.getString(R.string.high_description)));
                        track3 = false;
                    }
                }


            }
        }


        /*TrackItem[] trackItems = new TrackItem[videoTracks.size()];
        int tracksSize=videoTracks.size();
        if (tracksSize>0){
            if (tracksSize>4){
                for (int i = 0; i < 3; i++) {
                    VideoTrack videoTrackInfo = videoTracks.get(i);
                    if (videoTrackInfo.isAdaptive()) {

                        trackItems[i] = new TrackItem("Auto", videoTrackInfo.getUniqueId());
                    } else {

                        if (i==1){
                            trackItems[i] = new TrackItem("Low", videoTrackInfo.getUniqueId());
                        }else if (i==2){
                            trackItems[i] = new TrackItem("Medium", videoTrackInfo.getUniqueId());
                        }else if (i==3){
                            {
                                trackItems[i] = new TrackItem("High", videoTrackInfo.getUniqueId());
                            }
                        }

                    }
            }
        }


        }

*/


        return arrayList;
    }

    public void removeCallBacks() {
        try {
            if (mHandler != null) {
                mHandler.removeCallbacks(updateTimeTask);
            }

        } catch (Exception e) {

        }

    }

    private TrackItem[] buildAudioTrackItems(List<AudioTrack> audioTracks) {
        TrackItem[] trackItems = {};
        if (audioTracks.size() > 0) {
            trackItems = new TrackItem[audioTracks.size()];
            try {
                Log.w("audioAndSubtitle", "t1 --" + audioTracks.size());
                for (int i = 0; i < audioTracks.size(); i++) {
                    Log.w("audioAndSubtitle", "t2");
                    AudioTrack audioTrackInfo = audioTracks.get(i);
                    if (audioTrackInfo.isAdaptive()) {
                        Log.w("audioAndSubtitle", "t3");
                        if (audioTrackInfo.getLabel() != null) {
                            trackItems[i] = new TrackItem(audioTrackInfo.getLabel() + " ", audioTrackInfo.getUniqueId(), audioTrackInfo.getLanguage());
                            //  arrayList.add(new TrackItem(AppLevelConstants.AUTO, videoTrackInfo.getUniqueId(), context.getString(R.string.auto_description)));
                        }
                    } else {
                        Log.w("audioAndSubtitle", "t4");
                        trackItems[i] = new TrackItem(audioTrackInfo.getLanguage() + " ", audioTrackInfo.getUniqueId(), audioTrackInfo.getLanguage());
                    }

//                String label = audioTrackInfo.getLabel() != null ? audioTrackInfo.getLabel() : audioTrackInfo.getLanguage();
//                // String bitrate = (audioTrackInfo.getBitrate() > 0) ? "" + audioTrackInfo.getBitrate() : "";
//                String label2 = audioTracks.get(0).getLanguage();
//                if (label2 != null) {
//                    String label1 = audioTrackInfo.getLanguage();
//                    PrintLogging.printLog("", "Languageis" + label);
//                    trackItems[i] = new TrackItem(label1 + " ", audioTrackInfo.getUniqueId());
//
//                } else {
//                    trackItems[i] = new TrackItem("Default" + " ", audioTrackInfo.getUniqueId());
//                }
                }
            } catch (Exception e) {
                Log.w("audioTrackItems", "crashHappen" + e.getMessage());
            }

        }
        return trackItems;
    }

    private String changeLanguage(String language) {

        Locale streamLang = new Locale(language);
        Locale locale = new Locale("en");
        //streamLang.getISO3Language();

        return streamLang.getDisplayLanguage(locale);
    }

    private TrackItem[] buildTextTrackItems(List<TextTrack> textTracks) {
        TrackItem[] trackItems = new TrackItem[textTracks.size()];
        for (int i = 0; i < textTracks.size(); i++) {
            if (i == 0) {
                TextTrack textTrackInfo = textTracks.get(i);
                String name = "none";
                trackItems[i] = new TrackItem(name, textTrackInfo.getUniqueId(), textTrackInfo.getLanguage());
            } else {
                TextTrack textTrackInfo = textTracks.get(i);
                String name = textTrackInfo.getLabel();
                trackItems[i] = new TrackItem(name, textTrackInfo.getUniqueId(), textTrackInfo.getLanguage());
            }
        }
        return trackItems;
    }

    public LiveData<List<VideoTrack>> setVideoTracksinPlayer() {
        final MutableLiveData<List<VideoTrack>> playerMutableLiveData = new MutableLiveData<>();

        playerMutableLiveData.postValue(videoTracks);
        return playerMutableLiveData;
    }

    public LiveData<ArrayList<TrackItem>> getVideoTrackItems() {
        final MutableLiveData<ArrayList<TrackItem>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.postValue(trackItemsList);
        return mutableLiveData;
    }

    public LiveData<Boolean> changeTrack(String UniqueId, Context context) {
        PrintLogging.printLog(this.getClass(), "", "uniqueIdss-->>" + UniqueId);
        MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
        if (tracks != null) {
//            if (UniqueId.equalsIgnoreCase(AppLevelConstants.AUTO)) {
//                String selected = getSelectedIndex(1, tracks.getVideoTracks());
//                if (!selected.equalsIgnoreCase("")) {
//                    player.changeTrack(selected);
//                    trackListener(booleanMutableLiveData);
//                    // getPlayerState(booleanMutableLiveData);
//                } else {
//                    booleanMutableLiveData.postValue(true);
//                }
//
//            }
            if (UniqueId.equalsIgnoreCase(AppLevelConstants.LOW)) {
                String selected = getSelectedIndex(1, tracks.getVideoTracks(), context);
                PrintLogging.printLog(this.getClass(), "", "selctedIndex" + selected);
                if (!selected.equalsIgnoreCase("")) {
                    player.changeTrack(selected);
                    trackListener(booleanMutableLiveData);
                    //  getPlayerState(booleanMutableLiveData);
                    booleanMutableLiveData.postValue(true);

                } else {
                    booleanMutableLiveData.postValue(true);
                }

            } else if (UniqueId.equalsIgnoreCase(AppLevelConstants.MEDIUM)) {
                String selected = getSelectedIndex(2, tracks.getVideoTracks(), context);
                if (!selected.equalsIgnoreCase("")) {
                    player.changeTrack(selected);
                    trackListener(booleanMutableLiveData);
                    // getPlayerState(booleanMutableLiveData);
                    booleanMutableLiveData.postValue(true);
                } else {
                    booleanMutableLiveData.postValue(true);
                }

            } else if (UniqueId.equalsIgnoreCase(AppLevelConstants.HIGH)) {
                String selected = getSelectedIndex(3, tracks.getVideoTracks(), context);
                if (!selected.equalsIgnoreCase("")) {
                    player.changeTrack(selected);
                    trackListener(booleanMutableLiveData);
                    //  getPlayerState(booleanMutableLiveData);
                    // booleanMutableLiveData.postValue(true);
                } else {
                    booleanMutableLiveData.postValue(true);
                }
            }

        }


        return booleanMutableLiveData;
    }

    public LiveData<Boolean> changeInitialTrack(String UniqueId, Context context, TextView textView) {
        try {
            PrintLogging.printLog(this.getClass(), "", "uniqueIdss-->>" + UniqueId);
            MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
            if (tracks != null) {
//            if (UniqueId.equalsIgnoreCase(AppLevelConstants.AUTO)) {
//                String selected = getSelectedIndex(1, tracks.getVideoTracks());
//                if (!selected.equalsIgnoreCase("")) {
//                    player.changeTrack(selected);
//                    trackListener(booleanMutableLiveData);
//                    // getPlayerState(booleanMutableLiveData);
//                } else {
//                    booleanMutableLiveData.postValue(true);
//                }
//
//            }
            /*if (UniqueId.equalsIgnoreCase(AppLevelConstants.LOW)) {
                String selected = getSelectedIndex(1, tracks.getVideoTracks(), context);
                PrintLogging.printLog(this.getClass(), "", "selctedIndex" + selected);
                if (!selected.equalsIgnoreCase("")) {
                    player.changeTrack(selected);
                    trackListener(booleanMutableLiveData);
                    //  getPlayerState(booleanMutableLiveData);
                    booleanMutableLiveData.postValue(true);

                } else {
                    booleanMutableLiveData.postValue(true);
                }

            } else if (UniqueId.equalsIgnoreCase(AppLevelConstants.MEDIUM)) {
                String selected = getSelectedIndex(2, tracks.getVideoTracks(), context);
                if (!selected.equalsIgnoreCase("")) {
                    player.changeTrack(selected);
                    trackListener(booleanMutableLiveData);
                    // getPlayerState(booleanMutableLiveData);
                    booleanMutableLiveData.postValue(true);
                } else {
                    booleanMutableLiveData.postValue(true);
                }

            } else if (UniqueId.equalsIgnoreCase(AppLevelConstants.HIGH)) {
                String selected = getSelectedIndex(3, tracks.getVideoTracks(), context);
                if (!selected.equalsIgnoreCase("")) {
                    player.changeTrack(selected);
                    trackListener(booleanMutableLiveData);
                    //  getPlayerState(booleanMutableLiveData);
                    // booleanMutableLiveData.postValue(true);
                } else {
                    booleanMutableLiveData.postValue(true);
                }
            }
*/
                if (UniqueId.equalsIgnoreCase(AppLevelConstants.HIGH)) {
                    String selected = getSelectedIndex(3, tracks.getVideoTracks(), context);
                    if (!selected.equalsIgnoreCase("")) {
                        player.changeTrack(selected);
                        trackListener(booleanMutableLiveData);
                        textView.setText("High Quality");
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_video_quality, 0, 0, 0);
                        //  getPlayerState(booleanMutableLiveData);
                        booleanMutableLiveData.postValue(true);
                    } else {
                        //booleanMutableLiveData.postValue(true);
                        String selectedMedium = getSelectedIndex(2, tracks.getVideoTracks(), context);
                        if (!selectedMedium.equalsIgnoreCase("")) {
                            player.changeTrack(selectedMedium);
                            // getPlayerState(booleanMutableLiveData);
                            textView.setText("Medium Quality");
                            booleanMutableLiveData.postValue(true);
                            trackListener(booleanMutableLiveData);
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_medium_quality, 0, 0, 0);
                        } else {
                            //booleanMutableLiveData.postValue(true);
                            String selectedLow = getSelectedIndex(1, tracks.getVideoTracks(), context);
                            PrintLogging.printLog(this.getClass(), "", "selctedIndex" + selected);
                            if (!selectedLow.equalsIgnoreCase("")) {
                                player.changeTrack(selectedLow);
                                trackListener(booleanMutableLiveData);
                                //  getPlayerState(booleanMutableLiveData);
                                textView.setText("Low Quality");
                                booleanMutableLiveData.postValue(true);
                                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_low_quality, 0, 0, 0);
                            } else {
                                booleanMutableLiveData.postValue(true);
                            }
                        }
                    }
                }

            }
        } catch (Exception ignored) {

        }
        return booleanMutableLiveData;
    }


    private void trackListener(MutableLiveData<Boolean> booleanLiveData) {
        player.addListener(this, PlayerEvent.videoTrackChanged, event -> {
            booleanLiveData.postValue(true);


        });
    }

    private String getSelectedIndex(int type, List<VideoTrack> videoTracks, Context context) {
        String selectedIndex = "";
//        if (type == 1) {
//            for (int i = 0; i < videoTracks.size(); i++) {
//                VideoTrack videoTrackInfo = videoTracks.get(i);
//                Gson gson = new Gson();
//                if (videoTrackInfo.isAdaptive()) {
//                    selectedIndex = videoTrackInfo.getUniqueId();
//                }
//            }
//        }
        if (type == 1) {
            for (int i = 0; i < videoTracks.size(); i++) {
                VideoTrack videoTrackInfo = videoTracks.get(i);
                if (videoTrackInfo.getBitrate() > 0 && videoTrackInfo.getBitrate() < Long.valueOf(KsPreferenceKey.getInstance(context).getLowBitrateMaxLimit())) {
                    //  ConvivaManager.convivaPlayerSetBitRate(videoTrackInfo.getBitrate());
                    selectedIndex = videoTrackInfo.getUniqueId();
                }
            }
        } else if (type == 2) {
            // 450001, 600000
            for (int i = 0; i < videoTracks.size(); i++) {
                VideoTrack videoTrackInfo = videoTracks.get(i);
                PrintLogging.printLog(this.getClass(), "", "PrintBitMapssss" + videoTrackInfo.getBitrate() + " --" + type);
                if (videoTrackInfo.getBitrate() > Long.valueOf(KsPreferenceKey.getInstance(context).getLowBitrateMaxLimit()) && videoTrackInfo.getBitrate() < Long.valueOf(KsPreferenceKey.getInstance(context).getMediumBitrateMaxLimit())) {
                    //  ConvivaManager.convivaPlayerSetBitRate(videoTrackInfo.getBitrate());
                    selectedIndex = videoTrackInfo.getUniqueId();
                }
            }
        } else if (type == 3) {
            // 600001, 1000000
            for (int i = 0; i < videoTracks.size(); i++) {
                VideoTrack videoTrackInfo = videoTracks.get(i);

                if (videoTrackInfo.getBitrate() > Long.valueOf(KsPreferenceKey.getInstance(context).getMediumBitrateMaxLimit()) && videoTrackInfo.getBitrate() < Long.valueOf(KsPreferenceKey.getInstance(context).getHighBitrateMaxLimit())) {
                    selectedIndex = videoTrackInfo.getUniqueId();
                }
            }
        }

        return selectedIndex;
    }

    public LiveData<List<TextTrack>> setTextTracks() {
        MutableLiveData<List<TextTrack>> listMutableLiveData = new MutableLiveData<>();
        listMutableLiveData.postValue(textTracks);
        return listMutableLiveData;
    }

    public LiveData<TrackItem[]> getTextTrackItems() {
        MutableLiveData<TrackItem[]> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.postValue(textTrackItems);
        return mutableLiveData;
    }

    public void changeTextTrack(String uniqueId) {
        player.changeTrack(uniqueId);
    }

    public LiveData<String> getPlayerCurrentPosition(SeekBar seekBar) {
        integerMutableLiveData = new MutableLiveData<>();
        this.seekBar1 = seekBar;
        updateProgressBar();
        return integerMutableLiveData;
    }

    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    public void getViewOfPlayer(SeekBar seekBar) {
        this.seekBar1 = seekBar;
        mHandler.removeCallbacks(updateTimeTask);
        player.seekTo(seekBar.getProgress());
        updateProgressBar();
    }

    public LiveData<String> getSeekBarProgressContinues(int progress) {
        MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
        stringMutableLiveData.postValue(stringForTime(progress));
        return stringMutableLiveData;
    }

    public LiveData<Boolean> getStateofPlayer() {
        booleanMutableLiveData = new MutableLiveData<>();
        return booleanMutableLiveData;
    }

    public void control() {
        player.seekTo(0);
        player.play();
        updateProgressBar();
    }

    public LiveData<List<AudioTrack>> setAudioTracks() {
        Log.w("audioTracks==>>", audioTracks + "  ");
        MutableLiveData<List<AudioTrack>> listMutableLiveData = new MutableLiveData<>();
        listMutableLiveData.postValue(audioTracks);
        return listMutableLiveData;
    }

    public LiveData<TrackItem[]> getAudioTrackItems() {
        Log.w("audioTracks==>>", audioTrackItems.length + "  ");
        MutableLiveData<TrackItem[]> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.postValue(audioTrackItems);
        return mutableLiveData;
    }

    public LiveData<Boolean> haveAudioTracks() {
        isAudioTracks = new MutableLiveData<>();
        return isAudioTracks;
    }

    public void removeCallback() {
        mHandler.removeCallbacks(updateTimeTask);
    }

    public void changeAudioTrack(String uniqueId) {
        player.changeTrack(uniqueId);

    }

    public LiveData<Boolean> checkPauseState() {
        MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
        if (player != null) {
            player.pause();
            booleanMutableLiveData.postValue(true);
        }
        return booleanMutableLiveData;
    }

    public LiveData<Boolean> getResumeStatus() {
        MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
        if (player != null) {
            player.onApplicationResumed();
            if (mHandler != null) {
                mHandler.postDelayed(updateTimeTask, 100);
            }
            booleanMutableLiveData.postValue(true);
        }
        return booleanMutableLiveData;
    }


    public void releasePlayer() {
        if (player != null) {
            player.stop();
        }
    }

    public LiveData<Boolean> seekPlayerForward() {
        MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
        long duration = player.getCurrentPosition();
        PrintLogging.printLog(this.getClass(), "", "durationplayer" + duration);
        stringForTime(duration);
        player.seekTo(duration + 10000);
        getPlayerState(booleanMutableLiveData);
        return booleanMutableLiveData;
    }

    public LiveData<Boolean> seekPlayerBackward() {
        if (player != null) {
            MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
            long duration = player.getCurrentPosition();
            stringForTime(duration);
            player.seekTo(duration - 10000);
            getPlayerState(booleanMutableLiveData);

        }
        return booleanMutableLiveData;
    }

    private void getPlayerState(final MutableLiveData<Boolean> booleanMutableLiveData) {
        player.addListener(this, PlayerEvent.Type.STATE_CHANGED, event -> {
            PlayerEvent.StateChanged stateChanged = (PlayerEvent.StateChanged) event;
            switch (stateChanged.newState) {
                case IDLE:
                    //  log.d("StateChange Idle");
                    break;
                case LOADING:
                    //  log.d("StateChange Loading");
                    break;
                case READY:
                    booleanMutableLiveData.postValue(true);
                    ConvivaManager.convivaPlayerPlayReportRequest();

                    //  log.d("StateChange Ready");
                    //  mPlayerControlsView.setProgressBarVisibility(false);
                    break;
                case BUFFERING:
                    ConvivaManager.convivaPlayerBufferReportRequest();
                    // log.e("StateChange Buffering");
                    // mPlayerControlsView.setProgressBarVisibility(true);
                    // booleanMutableLiveData.postValue(false);
                    break;
            }

        });
    }

    public LiveData<Boolean> getPlayerProgress() {
        MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
        getPlayerState(booleanMutableLiveData);
        return booleanMutableLiveData;
    }

    private void subscribePhoenixAnalyticsReportEvent() {
        //Subscribe to the event.
        //Event subscription.
        player.addListener(this, PhoenixAnalyticsEvent.reportSent, event -> {
            String reportedEventName = event.reportedEventName;
            Log.i(TAG, "PhoenixAnalytics report sent. Reported event name: " + reportedEventName);
        });
    }

    public void pausePlayer() {
        if (player != null) {
            player.pause();
        }
    }

    public void startPlayer() {
        if (player != null) {
            player.play();
        }
    }

    Context context;

    public LiveData<Player> startPlayerBookmarking(final Context context,
                                                   PKMediaEntry mediaEntry,
                                                   final String deviceid,
                                                   final Asset asset,
                                                   int isPurchased,
                                                   int assetPosition) {
        final MutableLiveData<Player> playerMutableLiveData = new MutableLiveData<>();

        if (player != null) {
            player.destroy();
        }

        this.context = context;
        if (isPurchased == 1) {
            formatBuilder = new StringBuilder();
            formatter = new Formatter(formatBuilder, Locale.getDefault());

            registerPlugins(context, asset);

            PKMediaConfig mediaConfig;

            if (mediaEntry.getMediaType() != PKMediaEntry.MediaEntryType.Vod) {
//                mediaConfig = new PKMediaConfig().setMediaEntry(mediaEntry).setStartPosition(null);
                mediaConfig = new PKMediaConfig().setMediaEntry(mediaEntry).setStartPosition(null);
            } else if (assetPosition > 0) {
                mediaConfig = new PKMediaConfig().setMediaEntry(mediaEntry).setStartPosition(new Long(assetPosition));
            } else {
                mediaConfig = new PKMediaConfig().setMediaEntry(mediaEntry).setStartPosition(START_POSITION);
            }


            String ks = KsPreferenceKey.getInstance(context).getStartSessionKs();


            PhoenixAnalyticsConfig phoenixPluginConfig = new PhoenixAnalyticsConfig(AppLevelConstants.PARTNER_ID, KsPreferenceKey.getInstance(context).getKalturaPhoenixUrl(), ks, 30);


            playerPluginConfig = new PKPluginConfigs();
            addKavaPluginConfig(context, playerPluginConfig, asset);


            playerPluginConfig.setPluginConfig(PhoenixAnalyticsPlugin.factory.getName(), phoenixPluginConfig.toJson());

           /* if (asset.getType() == MediaTypeConstant.getProgram(context) || asset.getType() == MediaTypeConstant.getLinear(context)) {
                PrintLogging.printLog("ValueIS", "0");
            } else {*/
            if (AppCommonMethods.isAdsEnable) {
                if (!AssetContent.isAdsEnable(asset.getMetas())) {
                    getAdsContextApi();
                    addIMAConfig(context, playerPluginConfig);
                }
            }
            /*  }*/


            player = PlayKitManager.loadPlayer(context, playerPluginConfig);

            playerMutableLiveData.postValue(player);

            KalturaPlaybackRequestAdapter.install(player, "com.astro.sott"); // in case app developer wants to give customized referrer instead the default referrer in the playmanifest
            KalturaUDRMLicenseRequestAdapter.install(player, "com.astro.sott");

            player.getSettings().allowClearLead(true);
            player.getSettings().setSecureSurface(true);
            player.getSettings().setAdAutoPlayOnResume(true);

            LoadControlBuffers loadControlBuffers = new LoadControlBuffers().
                    setMinPlayerBufferMs(2000).
                    setMaxPlayerBufferMs(40000).
                    setBackBufferDurationMs(2000).
                    setMinBufferAfterReBufferMs(2000).
                    setMinBufferAfterInteractionMs(2000).
                    setRetainBackBufferFromKeyframe(true);
            // player.getSettings().setPlayerBuffers(loadControlBuffers);

            subscribePhoenixAnalyticsReportEvent();

//            player.getSettings().setABRSettings(new ABRSettings().setMinVideoBitrate(200000).setInitialBitrateEstimate(150000));


           /* if (asset.getType() == MediaTypeConstant.getLinear(context)) {
                player.getSettings().setABRSettings(new ABRSettings().setMaxVideoBitrate(Long.parseLong(KsPreferenceKey.getInstance(context).getHighBitrateMaxLimit()) / 2));
            } else {*/

            player.getSettings().setABRSettings(new ABRSettings().setMaxVideoBitrate(Long.parseLong(KsPreferenceKey.getInstance(context).getHighBitrateMaxLimit())));

            /*  }*/

            player.prepare(mediaConfig);
            player.play();
//        subscribeToTracksAvailableEvent();

        } else {
            playerMutableLiveData.postValue(null);
        }


        return playerMutableLiveData;
    }

    private void getAdsContextApi() {
    }

    private void addKavaPluginConfig(Context context, PKPluginConfigs pluginConfigs, Asset asset) {
        String entryId = "";
        try {
            MediaAsset mediaAsset = (MediaAsset) asset;
            if (asset.getType() != MediaTypeConstant.getProgram(context)) {
                entryId = mediaAsset.getEntryId();
            }
            if (KsPreferenceKey.getInstance(context).getUserActive()) {

                kavaAnalyticsConfig = new KavaAnalyticsConfig()
                        .setApplicationVersion(BuildConfig.VERSION_NAME)
                        .setPartnerId(BuildConfig.KAVA_PARTNER_ID)
                        .setUserId(KsPreferenceKey.getInstance(context).getUser().getId())
                        .setEntryId(entryId);
                pluginConfigs.setPluginConfig(KavaAnalyticsPlugin.factory.getName(), kavaAnalyticsConfig);
            } else {
                kavaAnalyticsConfig = new KavaAnalyticsConfig()
                        .setApplicationVersion(BuildConfig.VERSION_NAME)
                        .setPartnerId(BuildConfig.KAVA_PARTNER_ID)
                        .setEntryId(entryId);
                pluginConfigs.setPluginConfig(KavaAnalyticsPlugin.factory.getName(), kavaAnalyticsConfig);

            }

        } catch (Exception ignored) {

        }

    }


    private void addIMAConfig(Context context, PKPluginConfigs playerPluginConfig) {

        //   String imaVastTag = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpremidpostpod&cmsid=496&vid=short_onecue&correlator=";
        String imaVastTag = "";
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        if (responseDmsModel != null && responseDmsModel.getParams() != null && responseDmsModel.getParams().getAdTagURL() != null && responseDmsModel.getParams().getAdTagURL().getURL() != null) {
            imaVastTag = responseDmsModel.getParams().getAdTagURL().getURL();
        }

        //       String imaVastTag = "https://pubads.g.doubleclick.net/gampad/live/ads?sz=640x360&iu=%2F21633895671%2FQA%2FAndroid_Native_App%2FCOH&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=sample_ar%3Dskippablelinear%26Gender%3DU%26Age%3DNULL%26KidsPinEnabled%3DN%26AppVersion%3D0.1.58%26DeviceModel%3DAndroid%20SDK%20built%20for%20x86%26OptOut%3DFalse%26OSVersion%3D9%26PackageName%3Dcom.tv.v18.viola%26description_url%3Dhttps%253A%252F%252Fwww.voot.com%26first_time%3DFalse&cmsid=2467608&ppid=2fbdf28d-5bf9-4f43-b49e-19c4ca1f10f8&vid=0_o71549bv&ad_rule=1&correlator=246819";
        // String imaVastTag =  "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpostonlybumper&cmsid=496&vid=short_onecue&correlator=";
        //  String imaVastTag = "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpremidpostpod&cmsid=496&vid=short_onecue&correlator=";

        imaConfig = new IMAConfig();
        imaConfig.setAdTagUrl(imaVastTag);
        imaConfig.setAdLoadTimeOut(25);
        imaConfig.enableDebugMode(true);

        playerPluginConfig.setPluginConfig(IMAPlugin.factory.getName(), imaConfig);
    }

//    private void startDishOttMediaLoadingProd(final OnMediaLoadCompletion completion, Asset asset, final Context context) {
//        SessionProvider ksSessionProvider = new SessionProvider() {
//            @Override
//            public String baseUrl() {
//                String PhoenixBaseUrl = PHOENIX_ANALYTICS_BASE_URL;
//                return PhoenixBaseUrl;
//            }
//
//            @Override
//            public void getSessionToken(OnCompletion<PrimitiveResult> completion) {
//                String ks1 = KsPreferenceKey.getInstance(context).getStartSessionKs();
//                String PnxKS = "";
//                if (completion != null) {
//                    completion.onComplete(new PrimitiveResult(ks1));
//                }
//            }
//
//            @Override
//            public int partnerId() {
//                int OttPartnerId = AppLevelConstants.PARTNER_ID;
//                return OttPartnerId;
//            }
//        };
//
//        String mediaId = asset.getId().toString();//"311078";//"561107";
//        String format = AppLevelConstants.DASH;//"Tablet Main";//
//
//        mediaProvider = new PhoenixMediaProvider()
//                .setSessionProvider(ksSessionProvider)
//                .setAssetId(mediaId)
//                //.setReferrer()
//                .setProtocol(PhoenixMediaProvider.HttpProtocol.Https)
//                .setContextType(APIDefines.PlaybackContextType.Playback)
//                .setAssetType(APIDefines.KalturaAssetType.Media)
//                .setFormats(format);
//
//        mediaProvider.load(completion);
//    }


    private void onMediaLoaded(PKMediaEntry mediaEntry, String deviceid, Activity activity, MutableLiveData<Player> playerMutableLiveData, Asset asset, Context context) {


        PKMediaConfig mediaConfig = new PKMediaConfig().setMediaEntry(mediaEntry).setStartPosition(START_POSITION);
        PKPluginConfigs pluginConfig = new PKPluginConfigs();


        configurePlugins(pluginConfig, context);


        player = PlayKitManager.loadPlayer(activity, pluginConfig);
        KalturaPlaybackRequestAdapter.install(player, "myApp"); // in case app developer wants to give customized referrer instead the default referrer in the playmanifest
        KalturaUDRMLicenseRequestAdapter.install(player, "myApp");

        player.getSettings().setSecureSurface(false);
        player.getSettings().setAdAutoPlayOnResume(true);


        log.d("Player: " + player.getClass());


        player.prepare(mediaConfig);

//        subscribeToTracksAvailableEvent();

        playerMutableLiveData.postValue(player);


        // player.play();
    }


    private void configurePlugins(PKPluginConfigs pluginConfigs, Context context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("delay", 1200);
        pluginConfigs.setPluginConfig("PhoenixAnalytics", jsonObject);


        addDishPhoenixAnalyticsPlugin(pluginConfigs, context);


    }

    private void addDishPhoenixAnalyticsPlugin(PKPluginConfigs config, Context context) {

        String ks = KsPreferenceKey.getInstance(context).getStartSessionKs();


        //Set plugin entry to the plugin configs.
        PhoenixAnalyticsConfig phoenixAnalyticsConfig = new PhoenixAnalyticsConfig(AppLevelConstants.PARTNER_ID, KsPreferenceKey.getInstance(context).getKalturaPhoenixUrl(), ks, 30);
        config.setPluginConfig(PhoenixAnalyticsPlugin.factory.getName(), phoenixAnalyticsConfig);
    }


    private void registerPlugins(Context context, Asset asset) {
        PlayKitManager.registerPlugins(context, KavaAnalyticsPlugin.factory);
        PlayKitManager.registerPlugins(context, PhoenixAnalyticsPlugin.factory);
        if (asset.getType() == MediaTypeConstant.getLinear(context) || asset.getType() == MediaTypeConstant.getProgram(context)) {

        } else {
            if (AppCommonMethods.isAdsEnable)
                PlayKitManager.registerPlugins(context, IMAPlugin.factory);
        }
    }

    public LiveData<Boolean> getPlayerStateforPlay() {
        MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();

        getPlayerState(booleanMutableLiveData);
        return booleanMutableLiveData;
    }

    public LiveData<String> getAssetPurchaseType(String fileId, Context context) {
        final MutableLiveData<String> stringMutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);

        ksServices.getAssetPurchaseStatus(fileId, (status, response, s, errorCode1, message) -> {
            if (status) {
                stringMutableLiveData.postValue(response.results.getObjects().get(0).getPurchaseStatus().toString());
            }
        });

        return stringMutableLiveData;
    }

    private String stringForTime(long timeMs) {

        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return hours > 0 ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
                : formatter.format("%02d:%02d", minutes, seconds).toString();
    }

    public void resetPlayer(Context appContext) {
        if (player != null) {
            player.destroy();
        }
    }

    public LiveData<Integer> userAssetRule(Context context, Long assetId) {
        final MutableLiveData<Integer> stringMutableLiveData = new MutableLiveData<>();
        KsServices ksServices = new KsServices(context);

        ksServices.assetRuleApi(assetId, (status, response, i, errorcode, message) -> {
            if (status) {
                int totalCount = response.results.getTotalCount();
                stringMutableLiveData.postValue(totalCount);
            } else {
                stringMutableLiveData.postValue(-1);

            }
        });

        return stringMutableLiveData;
    }


    public LiveData<Player> getPlayerObject() {
        MutableLiveData<Player> playerMutableLiveData = new MutableLiveData<>();
        if (player != null) {
            playerMutableLiveData.postValue(player);
        } else {
            playerMutableLiveData.postValue(null);
        }
        return playerMutableLiveData;
    }

    private void openConcurrencyDialouge(final Activity activity) {
        if (activity != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(activity.getResources().getString(R.string.concurrency_error))
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, id) -> {
                        dialog.cancel();
                        activity.onBackPressed();

//                        if(player!= null){
//
//                            player.stop();
//                            dialog.cancel();
//                                activity.onBackPressed();
//
//
//                        }
                    });

            AlertDialog alert = builder.create();
            if (!activity.isFinishing()) {
                alert.show();
                Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                bn.setTextColor(ContextCompat.getColor(activity, R.color.blue));
                Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                bp.setTextColor(ContextCompat.getColor(activity, R.color.blue));
            }


        }

    }

    public void updateVideoRatio() {
        if (player != null) {
            if (!isFilled) {
                player.updateSurfaceAspectRatioResizeMode(PKAspectRatioResizeMode.zoom);
            } else {
                player.updateSurfaceAspectRatioResizeMode(PKAspectRatioResizeMode.fit);
            }
            isFilled = !isFilled;
        }
    }

    public LiveData<CommonResponse> getNumberOfEpisode(Asset asset, Context appContext) {
        final MutableLiveData<CommonResponse> mutableLiveData = new MutableLiveData<>();
        final CommonResponse commonResponse = new CommonResponse();
        KsServices ksServices = new KsServices(appContext);
        int mediaType = asset.getType();
        if (mediaType == MediaTypeConstant.getEpisode(appContext)) {
            mediaType = MediaTypeConstant.getEpisode(appContext);
        }
        String seriesId = AssetContent.getSeriesId(asset.getMetas());
        if (seriesId.equalsIgnoreCase("")) {

        } else {
            ksServices.callNumberOfEpisodes(seriesId, mediaType, (status, errorCode, message, assetList) -> {
                if (status) {
                    if (assetList.results.getTotalCount() > 0) {

                        commonResponse.setStatus(true);
                        commonResponse.setTotalEpisodes(assetList.results.getTotalCount());
                        commonResponse.setAssetList(assetList);
                        mutableLiveData.postValue(commonResponse);
                    } else {
                        commonResponse.setStatus(false);
                        mutableLiveData.postValue(commonResponse);
                    }

                } else {
                    commonResponse.setStatus(false);
                    mutableLiveData.postValue(commonResponse);
                }
            });
        }


        return mutableLiveData;
    }

    public LiveData<Boolean> seekToDuration() {
        MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
        //  long duration = player.getDuration();
        // PrintLogging.printLog(this.getClass(), "", "durationplayer" + duration);
        // stringForTime(duration);

        if (player != null) {
            player.seekTo(player.getDuration() - 5000);
            getPlayerState(booleanMutableLiveData);
        }
        return booleanMutableLiveData;
    }

    public LiveData<Boolean> seekToStart() {
        MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
        //  long duration = player.getDuration();
        // PrintLogging.printLog(this.getClass(), "", "durationplayer" + duration);
        // stringForTime(duration);
        player.seekTo(0);
        getPlayerState(booleanMutableLiveData);
        return booleanMutableLiveData;
    }
}


