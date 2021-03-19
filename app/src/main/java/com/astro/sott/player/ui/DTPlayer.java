package com.astro.sott.player.ui;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.activities.loginActivity.LoginActivity;
import com.astro.sott.activities.parentalControl.viewmodels.ParentalControlViewModel;
import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.adapter.CatchupTvAdapter;
import com.astro.sott.callBacks.DoubleClick;
import com.astro.sott.callBacks.WindowFocusCallback;
import com.astro.sott.callBacks.commonCallBacks.CatchupCallBack;
import com.astro.sott.callBacks.commonCallBacks.ParentalDialogCallbacks;
import com.astro.sott.fragments.dialog.AlertDialogFragment;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.fragments.nowPlaying.NowPlaying;
import com.astro.sott.modelClasses.dmsResponse.ParentalLevels;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.networking.refreshToken.RefreshKS;
import com.astro.sott.player.entitlementCheckManager.EntitlementCheck;
import com.astro.sott.player.geoBlockingManager.GeoBlockingCheck;
import com.astro.sott.player.viewModel.DTPlayerViewModel;
import com.astro.sott.thirdParty.conViva.ConvivaManager;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.Network;
import com.astro.sott.utils.helpers.NetworkChangeReceiver;
import com.astro.sott.utils.helpers.SwipeGestureListener;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.Alarm.MyReceiver;
import com.astro.sott.BuildConfig;
import com.astro.sott.R;
import com.astro.sott.activities.home.HomeActivity;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.DoubleClickListener;
import com.astro.sott.callBacks.DragListner;
import com.astro.sott.callBacks.PhoneListenerCallBack;
import com.astro.sott.databinding.FragmentDtplayerBinding;
import com.astro.sott.fragments.dialog.AlertDialogNetworkFragment;
import com.astro.sott.networking.errorCallBack.ErrorCallBack;
import com.astro.sott.networking.ksServices.KsServices;
import com.astro.sott.player.adapter.TrackItem;
import com.astro.sott.player.houseHoldCheckManager.HouseHoldCheck;
import com.astro.sott.repositories.player.PlayerRepository;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.DialogHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PhoneStateListenerHelper;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.UDID;
import com.conviva.sdk.ConvivaSdkConstants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.LiveAsset;
import com.kaltura.client.types.MediaAsset;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.ProgramAsset;
import com.kaltura.client.types.UserAssetRule;
import com.kaltura.client.types.Value;
import com.kaltura.client.utils.response.base.Response;
import com.kaltura.netkit.connect.response.PrimitiveResult;
import com.kaltura.netkit.utils.OnCompletion;
import com.kaltura.netkit.utils.SessionProvider;
import com.kaltura.playkit.PKMediaEntry;
import com.kaltura.playkit.PKMediaFormat;
import com.kaltura.playkit.PKMediaSource;
import com.kaltura.playkit.PlayKitManager;
import com.kaltura.playkit.Player;
import com.kaltura.playkit.PlayerEvent;
import com.kaltura.playkit.ads.AdController;
import com.kaltura.playkit.player.PKAspectRatioResizeMode;
import com.kaltura.playkit.plugins.ads.AdEvent;
import com.kaltura.playkit.plugins.ads.AdInfo;
import com.kaltura.playkit.providers.MediaEntryProvider;
import com.kaltura.playkit.providers.api.phoenix.APIDefines;
import com.kaltura.playkit.providers.base.OnMediaLoadCompletion;
import com.kaltura.playkit.providers.ott.PhoenixMediaProvider;

import org.apache.commons.lang3.builder.CompareToBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.StringTokenizer;

import static android.content.Context.POWER_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

public class DTPlayer extends BaseBindingFragment<FragmentDtplayerBinding> implements SeekBar.OnSeekBarChangeListener,
        PhoneListenerCallBack, WindowFocusCallback, AlertDialogFragment.AlertDialogListener,
        AlertDialogSingleButtonFragment.AlertDialogListener, View.OnClickListener, AudioManager.OnAudioFocusChangeListener,
        NetworkChangeReceiver.ConnectivityReceiverListener, AlertDialogNetworkFragment.AlertDialogNetworkListener, CatchupCallBack {

    private static final String TAG = "DTPlayer";

    private static Asset asset;
    private static int episodeNumber;
    boolean lockEnable = false;
    List<TrackItem> trackItemList;
    boolean adRunning = false;
    TrackItem[] captionList;
    TrackItem[] audioList;

    boolean isVideoError = false;
    boolean hasPostRoll = false;
    boolean hasMidRoll = false;
    boolean allAdsCompleted = false;
    boolean isWaitingBinge = false;
    boolean hasPreRoll = false;
    boolean isPlayerEnded = false;
    boolean isAdPause = false;
    boolean exitPlayer = false;
    boolean isPause = false;
    String startTimeStamp;
    String endTimeStamp;
    PendingIntent pendingIntent = null;
    AlarmManager alarmManager;
    Intent myIntent;
    long reminderDateTimeInMilliseconds = 000;
    int mm;
    int yr;
    int ddy;
    ArrayList<ParentalLevels> parentalLevels;
    private BottomSheetDialog dialog;
    private BottomSheetDialog bottomSheetDialog;
    private Dialog dialogQuality;
    private BottomSheetDialog dialogCaption;
    private BottomSheetDialog dialogAudio;
    private Runnable myRunnable;
    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;
    private DTPlayerViewModel viewModel;
    private int handlerTime = 4000;
    private boolean timer = true;
    private Handler timeHandler;
    private String trackName = "";
    private String audioTrackName = "";
    private String captionName = "";
    private String image_url;
    private int assetType;
    private boolean isPlayerStart = false;
    private int isPurchased = 1;
    private String playerURL;
    private Asset playerAsset;
    private String programName1 = "";
    private int playerProgress;
    private Player runningPlayer;
    private String selectedTrack;
    private int assetPosition = 0;
    private boolean isPlayerIconClick = false;
    private boolean isError = false;
    private BaseActivity baseActivity;
    private boolean isLiveChannel = false;
    private boolean isDtvAdded = false;
    private long lastClickTime = 0;
    private Map<String, MultilingualStringValueArray> map;
    private final BroadcastReceiver networkReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK);
                if (networkInfo != null)
                    Log.d("internetCheck", "We have internet connection. Good to go." + networkInfo.getDetailedState());

                if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED) {
                    //Log.d("internetCheck", "We have internet connection. Good to go.");
                    if (viewModel != null) {
                        viewModel.getPlayerObject().observe(DTPlayer.this, player -> {
                            if (player != null) {
                                if (isPlayerStart) {
                                    if (!player.isPlaying()) {
                                        playContentOnReconnect();
                                    }

                                }

                            }
                        });
                    }

                } else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED) {
                    // Log.d("internetCheck", "We have lost internet connection");
                    if (viewModel != null) {
                        runningPlayer.pause();
                        viewModel.getPlayerObject().observe(DTPlayer.this, player ->
                                {

                                    //  pausePlayerOnInternetGone(player);
                                }


                        );
                    }

                }

            }
        }
    };
    private GestureDetector gestureDetector;
    private boolean drag = false;
    private boolean isParentalLocked = false;
    private String defaultParentalRating = "";
    private String userSelectedParentalRating = "";
    private int userSelectedParentalPriority;
    private int priorityLevel;
    private int assetRestrictionLevel;
    private boolean assetKey = false;
    private String currentLiveProgramAseet = "";
    private Asset catchupAsset;
    private boolean checkIsliveAsset = false;
    private boolean isProgramClicked = false;
    private String catchupLiveProgName = "";
    private Map<String, Value> dvrMap;
    private BottomSheetBehavior bottomSheetBehavior;
    private String values = "Today";
    private Boolean isEnable = false;
    private Boolean programCatchupEnable = false;
    private List<RailCommonData> currentRailCommonData;
    private List<RailCommonData> individualProgram;
    private Long startTime;
    private String currentProgramId;
    private MyReceiver myReceiver;
    private String month = "";
    private String dd = "";
    private String year = "";
    private String hour = "";
    private String minute = "";
    private String time = "";
    private boolean dvrEnabled = false;
    private boolean isBingeView = false;
    private NetworkChangeReceiver receiver = null;
    private IntentFilter filter;
    private boolean isInternet = true;
    private Animation animationFadeOut;
    private Animation animationFadeIn;
    private CountDownTimer cTimer = null;
    private List<Asset> episodesList;
    private Asset nextPlayingAsset;
    private boolean isFirstAd = true;
    private boolean hasEpisodesList = false;
    private boolean isSeries = false;
    private boolean hasNextEpisode = false;
    private boolean playerChecksCompleted = false;
    private int seasonNumber;
    private int errorCode = -1;
    private boolean isDialogShowing = false, isAudioTracks = false, isCaption = false;
    private AudioManager mAudioManager;
    private Boolean isLivePlayer = false;
    private final BroadcastReceiver headsetRecicer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                if (intent.getExtras() != null) {
                    int headsetState = intent.getExtras().getInt("state");
                    if (isPlayerStart && headsetState == 0) {
                        pausePlayer();


                    }
                }
            }
        }
    };
    private int assetRuleErrorCode = -1;
    private boolean isDialogOpen = false;
    private MediaAsset mediaAsset;


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (receiver != null) {
            isInternet = isConnected;
            if (isConnected) {
            } else {
                if (!DialogHelper.isIsDialog())
                    if (runningPlayer != null) {
                        runningPlayer.pause();
                        getBinding().playButton.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
                        PrintLogging.printLog("onNetworkConnectionChanged", "onNetworkConnectionChanged");
                    }
                showNoInternetDialog();
            }
        }
    }

    private void showNoInternetDialog() {
        try {
            FragmentManager fm = getFragmentManager();
            AlertDialogNetworkFragment alertDialog = AlertDialogNetworkFragment.newInstance(getResources().getString(R.string.no_internet_header), getResources().getString(R.string.no_internet_description), getResources().getString(R.string.try_again_small), getResources().getString(R.string.cancel));
            alertDialog.setCancelable(false);
            alertDialog.setAlertDialogCallBack(this);
            if (fm != null)
                alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
        } catch (IllegalStateException e) {

        }
    }

    @Override
    public void onFinishDialog(boolean status) {
        if (status) {
            getBinding().rl1.setVisibility(View.GONE);
            getBinding().pBar.setVisibility(View.VISIBLE);
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (NetworkChangeReceiver.isOnline(getActivity())) {
                        replayOnConnect();
                    } else {
                        if (Network.isMobileDataNetworkAvailable(getActivity()) || Network.checkConnectivityTypeMobile(getBaseActivity())) {
                            replayOnConnect();
                        } else {
                            showNoInternetDialog();
                        }
                    }
                }
            }, 2000);


        } else {
            if (timer && timeHandler != null) {
                timeHandler.removeCallbacks(myRunnable);
            }
            if (runningPlayer != null) {
                PrintLogging.printLog(this.getClass(), "", "runningPlayer");
                runningPlayer.stop();
                runningPlayer.destroy();
            }

            if (viewModel != null)
                viewModel.clearCallbacks();
            baseActivity.onBackPressed();

        }

    }

    private void replayOnConnect() {
        getBinding().rl1.setVisibility(View.VISIBLE);
        getBinding().pBar.setVisibility(View.GONE);
        isInternet = true;
        if (runningPlayer != null) {
            if (isLivePlayer) {
                getUrl(playerURL, playerAsset, playerProgress, true, programName1);
            } else {
                runningPlayer.play();
                PrintLogging.printLog("replayOnConnect", "replayOnConnect");

                getBinding().playButton.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        checkAutoRotation();
        individualProgram = new ArrayList<>();
        myReceiver = new MyReceiver();

        powerManager = (PowerManager) baseActivity.getSystemService(POWER_SERVICE);
        if (powerManager != null)
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyApp::MyWakelockTag");


        receiver = new NetworkChangeReceiver();
        filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            Toast.makeText(getActivity(), "startcalled", Toast.LENGTH_LONG);
            if (wakeLock != null) {
                wakeLock.acquire();
            }
            TelephonyManager mgr = (TelephonyManager) baseActivity.getSystemService(TELEPHONY_SERVICE);
            if (mgr != null) {
                mgr.listen(PhoneStateListenerHelper.getInstance(this), PhoneStateListener.LISTEN_CALL_STATE);
            }

        } catch (Exception e) {
            //   Log.e("TAG", e.getMessage());
        }
    }

    @Override
    public FragmentDtplayerBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentDtplayerBinding.inflate(inflater);
    }

    private void modelCall() {
        try {
            viewModel = ViewModelProviders.of(this).get(DTPlayerViewModel.class);
        } catch (IllegalStateException e) {

        }
    }

    public void getUrl(String urlToplay, final Asset asset, int prog, Boolean isLivePlayer, String programName) {
        hasNextEpisode = false;
        hasEpisodesList = false;
        isPlayerStart = false;
        playerAsset = asset;
        playerURL = urlToplay;
        playerProgress = prog;
        programName1 = programName;
        this.asset = asset;
        isSeries = (asset.getType() == MediaTypeConstant.getWebEpisode(getActivity()));
        getNextEpisode(asset);

        if (isSeries) {
            getBinding().name.setText("S" + seasonNumber + ":E" + episodeNumber + " \"" + asset.getName() + "\"");
        } else if (isLivePlayer) {
            getBinding().name.setText("\"" + programName + "\"");

        } else {
            getBinding().name.setText("\"" + asset.getName() + "\"");

        }

        this.isLivePlayer = isLivePlayer;
        // if(urlToplay!=null&&urlToplay.equalsIgnoreCase(""))
        if (!this.isLivePlayer) {
            dvrEnabled = false;
            if (KsPreferenceKey.getInstance(getActivity()).getCatchupValue()) {

                getBinding().rlDown.setVisibility(View.VISIBLE);
                getBinding().liveTxt.setVisibility(View.INVISIBLE);
                getBinding().playerMediaControls.setVisibility(View.INVISIBLE);


                if (currentRailCommonData != null && currentRailCommonData.size() > 0) {
                    getCurrentCatchupTimeStamp(asset);
                    performClickForCatchUpContent();
                } else {
                    try {
//                        ProgramAsset programAsset = (ProgramAsset) asset;

                        Log.d("externalIDIs", NowPlaying.EXTERNAL_IDS);

                        getCurrentCatchupTimeStamp(asset);
                        performClickForCatchUpContent();
                    } catch (Exception e) {

                    }
                }
            } else {
                getBinding().rlDown.setVisibility(View.VISIBLE);
                getBinding().liveTxt.setVisibility(View.INVISIBLE);
                getBinding().playerMediaControls.setVisibility(View.VISIBLE);
            }


            //  getBinding().goLive.setVisibility(View.GONE);
        } else {

            dvrMap = asset.getMetas();
            dvrEnabled = AssetContent.getTSTVData(dvrMap);
            isEnable = ((LiveAsset) asset).getEnableCatchUp();


            // isEnable = false;
            Log.d("IsCatchupEnable", dvrEnabled + "");
            Log.d("IsCatchupEnable", isLivePlayer + "");
            mediaAsset = (MediaAsset) asset;
            NowPlaying.EXTERNAL_IDS = mediaAsset.getExternalIds();

            try {

                getCurrentProgramTimeStamp();
                performClickForCatchUpContent();


            } catch (Exception e) {
                Log.d("ExceptionIs", e.getMessage());
            }


            if (dvrEnabled) {

                getBinding().seekBar.setVisibility(View.VISIBLE);
                getBinding().rlDown.setVisibility(View.GONE);
                getBinding().liveTxt.setVisibility(View.VISIBLE);
                getBinding().playerMediaControls.setVisibility(View.GONE);
                performClickForCatchUpContent();
                // getBinding().goLive.setVisibility(View.VISIBLE);

            } else {
                getBinding().rlDown.setVisibility(View.GONE);
                getBinding().seekBar.setVisibility(View.GONE);
                getBinding().liveTxt.setVisibility(View.VISIBLE);
                getBinding().playerMediaControls.setVisibility(View.GONE);
                // getBinding().goLive.setVisibility(View.GONE);
            }

        }
        checkAssetTypeCondition(urlToplay, asset, prog);

    }

    private void getCurrentCatchupTimeStamp(Asset asset) {
        try {
            startTime = asset.getStartDate();

            currentProgramId = String.valueOf(asset.getId());

            //Showing bottomSheet for CatchupTV
            //   showCatchupUI(startTime, asset);

        } catch (Exception e) {

        }

    }


    private void checkAssetTypeCondition(String urlToplay, Asset asset, int prog) {
        assetType = asset.getType();
        modelCall();
        hidePlayerControls();
        UIControllers();
        getAssetImage(asset, UDID.getDeviceId(requireContext(), requireContext().getContentResolver()));
        startPhoenixInit();
//        setTouchFalse();
    }

    private void getNextEpisode(Asset asset) {
        checkSeasonAndEpisodeNumber(asset.getMetas());
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(DTPlayerViewModel.class);
        }
        viewModel.getNumberOfEpisodes(asset, getActivity()).observe(this, commonResponse -> {
            if (commonResponse.getStatus()) {
                episodesList = commonResponse.getAssetList().results.getObjects();
                sortListWithEPSD(episodesList);
            }
        });
    }

    private void sortListWithEPSD(List<Asset> episodesList) {

        Collections.sort(episodesList, new Comparator<Asset>() {
            @Override
            public int compare(Asset p1, Asset p2) {
                int seasonNumber = AssetContent.getSpecificSeason(p1.getMetas());
                int seasonNumber2 = AssetContent.getSpecificSeason(p2.getMetas());

                int episodeNumber = AssetContent.getSpecificEpisode(p1.getMetas());
                int episodeNumber2 = AssetContent.getSpecificEpisode(p2.getMetas());

                return new CompareToBuilder().append(seasonNumber, seasonNumber2).append(episodeNumber, episodeNumber2).toComparison();
            }
        });
        try {
            Handler mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkEpisode(episodesList);
                }
            }, 2000);

        } catch (Exception e) {
            PrintLogging.printLog(this.getClass(), "", "checkEpisode" + "checkEpisode");
        }

    }

    private void checkEpisode(List<Asset> episodesList) {
        int nextEpisodeCounter = -1;
        boolean found = false;
        hasEpisodesList = true;
        if (episodesList.size() > 0) {
            for (int i = 0; i < episodesList.size(); i++) {
                int listEpisode = AssetContent.getSpecificEpisode(episodesList.get(i).getMetas());
                //  if (listSeason == seasonNumber) {
                //   if(episodesList.get(i).getId()==asset.getId()){

                Log.d("EpisodeNumberisEqual", listEpisode + "");
                Log.d("SeasonNumber", episodeNumber + "");

                if (listEpisode == episodeNumber) {
                    found = true;
                    if ((i + 1) < episodesList.size())
                        nextEpisodeCounter = i + 1;
                    break;
                }
                //  }
            }

            if (found) {
                if ((nextEpisodeCounter != -1) && episodesList.size() > nextEpisodeCounter) {
                    nextPlayingAsset = episodesList.get(nextEpisodeCounter);
                    hasNextEpisode = true;
                }
            }

        }
    }

    private void playerChecks(final Asset asset) {
        new GeoBlockingCheck().aseetAvailableOrNot(getActivity(), asset, (status, response, totalCount, errorcode, message) -> {
            if (status) {
                if (totalCount != 0) {
                    playerChecksCompleted = true;
                    checkBlockingErrors(response, asset);
                } else {
                    checkEntitleMent(asset);
                }
            } else {
                showDialog(message);
            }
        });
    }

    private void checkErrors(Asset asset) {
        try {
            if (playerChecksCompleted) {
                if (assetRuleErrorCode == AppLevelConstants.GEO_LOCATION_ERROR) {
                    getActivity().runOnUiThread(() -> DialogHelper.openDialougeforGeoLocation(1, getActivity()));
                } else if (errorCode == AppLevelConstants.FOR_PURCHASED_ERROR) {
                    getActivity().runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(getActivity()));
                } else if (errorCode == AppLevelConstants.USER_ACTIVE_ERROR) {
                    getActivity().runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(getActivity()));
                } else if (errorCode == AppLevelConstants.NO_MEDIA_FILE) {
                    showDialog(getString(R.string.no_media_file));
                } else if (errorCode == AppLevelConstants.NO_ERROR) {
                    if (KsPreferenceKey.getInstance(getActivity()).getUserActive()) {
                        parentalCheck(asset);
                    } else {
                        if (isProgramClicked) {
                            if (checkIsliveAsset) {
                                getUrl(AssetContent.getURL(asset), asset, playerProgress, true, catchupLiveProgName);
                            } else {
                                getUrl(AssetContent.getURL(catchupAsset), catchupAsset, playerProgress, false, asset.getName());
                            }
                        } else {
                            getUrl(AssetContent.getURL(asset), asset, playerProgress, isLivePlayer, "");
                        }
                    }
                } else {
                    PrintLogging.printLog("", "elseValuePrint-->>" + assetRuleErrorCode + "  " + errorCode);
                }
            } else {
                DialogHelper.showAlertDialog(getActivity(), getString(R.string.play_check_message), getString(R.string.ok), this);
            }
        } catch (Exception e) {

        }
    }


    private void parentalCheck(Asset asset) {
        if (KsPreferenceKey.getInstance(getActivity()).getUserActive()) {
            if (KsPreferenceKey.getInstance(getActivity()).getParentalActive()) {
                ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(getActivity());
                defaultParentalRating = responseDmsModel.getParams().getDefaultParentalLevel();
                userSelectedParentalRating = KsPreferenceKey.getInstance(getActivity()).getUserSelectedRating();
                if (!userSelectedParentalRating.equalsIgnoreCase("")) {
                    assetKey = AssetContent.getAssetKey(asset.getTags(), userSelectedParentalRating, getActivity());
                    if (assetKey) {
                        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                        checkOnlyDevice(asset);
                    } else {
                        validateParentalPin(asset);
                    }

                } else {
                    assetKey = AssetContent.getAssetKey(asset.getTags(), defaultParentalRating, getActivity());
                    if (assetKey) {
                        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                        checkOnlyDevice(asset);
                    } else {
                        validateParentalPin(asset);
                    }
                }
            } else {
                if (isProgramClicked) {
                    if (checkIsliveAsset) {
                        getUrl(AssetContent.getURL(DTPlayer.asset), DTPlayer.asset, playerProgress, true, catchupLiveProgName);
                    } else {
                        getUrl(AssetContent.getURL(catchupAsset), catchupAsset, playerProgress, false, DTPlayer.asset.getName());
                    }
                } else {
                    getUrl(AssetContent.getURL(DTPlayer.asset), DTPlayer.asset, playerProgress, isLivePlayer, "");
                }
            }
        }
    }


    private void validateParentalPin(Asset asset) {


        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.showValidatePinDialog(getActivity(), null, AppLevelConstants.PLAYER, new ParentalDialogCallbacks() {
                    @Override
                    public void onPositiveClick(String pinText) {

                        ParentalControlViewModel parentalViewModel = ViewModelProviders.of(getActivity()).get(ParentalControlViewModel.class);

                        parentalViewModel.validatePin(getActivity(), pinText).observe(getActivity(), commonResponse -> {
                            if (commonResponse.getStatus()) {
                                DialogHelper.hideValidatePinDialog();
                                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                                // checkErrors(asset);
                                checkOnlyDevice(asset);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.incorrect_parental_pin), Toast.LENGTH_LONG).show();
                            }
                        });

                    }

                    @Override
                    public void onNegativeClick() {
                        //  DialogHelper.hideValidatePinDialog();
                        if (!((Activity) getActivity()).isFinishing()) {
                            if (getActivity() != null) {
                                getActivity().onBackPressed();
                            }
                        }
                    }
                });
            }
        });

    }

    private void checkOnlyDevice(Asset asset) {
        new HouseHoldCheck().checkHouseholdDevice(getActivity(), commonResponse -> {
            if (commonResponse != null) {

                try {
                    if (commonResponse.getStatus()) {
                        //play next episode here
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                if (isProgramClicked) {
                                    if (checkIsliveAsset) {
                                        getUrl(AssetContent.getURL(asset), asset, playerProgress, true, catchupLiveProgName);
                                    } else {
                                        getUrl(AssetContent.getURL(catchupAsset), catchupAsset, playerProgress, false, asset.getName());
                                    }
                                } else {
                                    getUrl(AssetContent.getURL(asset), asset, playerProgress, isLivePlayer, "");
                                }
                            }
                        });
                    } else {
                        if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                            new RefreshKS(getActivity()).refreshKS(response -> checkDevice(asset));
                        } else {
                            showDialog(commonResponse.getMessage());
                        }
                    }
                } catch (Exception e) {

                }


            }

        });
    }

    private void checkDevice(final Asset asset) {
        new HouseHoldCheck().checkHouseholdDevice(getActivity(), commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    checkEntitleMent(asset);

                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(getActivity()).refreshKS(response -> checkDevice(asset));
                    } else {
                        showDialog(commonResponse.getMessage());
                    }
                }
            }

        });

    }

    private void checkBlockingErrors(Response<ListResponse<UserAssetRule>> response, Asset asset) {
        if (response != null && response.results != null && response.results.getObjects() != null) {
            for (UserAssetRule userAssetRule :
                    response.results.getObjects()) {
                switch (userAssetRule.getRuleType()) {
                    case GEO:
                        playerChecksCompleted = true;
                        assetRuleErrorCode = AppLevelConstants.GEO_LOCATION_ERROR;
                        break;
//                    case PARENTAL:
//                        assetRuleErrorCode = AppLevelConstants.PARENTAL_BLOCK;
//                        checkEntitleMent(asset);
//                        break;
                    default:
                        checkEntitleMent(asset);
                        break;
                }
            }
        }
    }

    private void checkUserLoginCondition(Asset asset) {
        checkEntitleMent(asset);
    }

    private void checkEntitleMent(final Asset asset) {


        try {

            String fileId = AppCommonMethods.getFileIdOfAssest(asset);

            if (getActivity() != null) {
                new EntitlementCheck().checkAssetType(getActivity(), fileId, (status, response, purchaseKey, errorCode1, message) -> {
                    if (status) {
                        playerChecksCompleted = true;
                        if (purchaseKey.equalsIgnoreCase(getResources().getString(R.string.FOR_PURCHASE_SUBSCRIPTION_ONLY)) || purchaseKey.equals(getResources().getString(R.string.FREE))) {
                            errorCode = AppLevelConstants.NO_ERROR;
                        } else if (purchaseKey.equals(getResources().getString(R.string.FOR_PURCHASED))) {
                            if (KsPreferenceKey.getInstance(getActivity()).getUserActive()) {
                                isDtvAccountAdded(asset);
                                //check Dtv Account Added or Not

                            } else {
                                errorCode = AppLevelConstants.FOR_PURCHASED_ERROR;

                            }
                        } else {
                            if (KsPreferenceKey.getInstance(getActivity()).getUserActive()) {
                                isDtvAccountAdded(asset);
                                //check Dtv Account Added or Not
                            } else {
                                errorCode = AppLevelConstants.USER_ACTIVE_ERROR;
                                //not play
                            }


                        }
                    } else {
                        if (message != "")
                            showDialog(message);
                    }
//                                    playerChecksCompleted = true;
                });
            }
        } catch (Exception e) {

        }
    }

    private void isDtvAccountAdded(Asset asset) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewModel.getDtvAccountList().observe(getActivity(), new Observer<String>() {
                    @Override
                    public void onChanged(String dtvAccount) {
                        try {
                            if (dtvAccount != null) {
                                if (dtvAccount.equalsIgnoreCase("0")) {
                                    isDtvAdded = false;
                                    checkForSubscription(isDtvAdded, asset);

                                } else if (dtvAccount.equalsIgnoreCase("")) {
                                    isDtvAdded = false;
                                    checkForSubscription(isDtvAdded, asset);

                                } else {
                                    isDtvAdded = true;
                                    checkForSubscription(isDtvAdded, asset);

                                }

                            } else {
                                // Api Failure Error

                                showDialog(getString(R.string.something_went_wrong_try_again));
                            }
                        } catch (Exception e) {
                            Log.e("ExceptionIs", e.toString());
                        }
                    }
                });

            }
        });

    }

    private void checkForSubscription(boolean isDtvAdded, Asset asset) {
        //***** Mobile + Non-Dialog + Non-DTV *************//
        if (KsPreferenceKey.getInstance(getActivity()).getUserType().equalsIgnoreCase(AppLevelConstants.NON_DIALOG) && isDtvAdded == false) {
            getActivity().runOnUiThread(() -> DialogHelper.openDialougeFornonDialog(getActivity(), isLiveChannel));
            getActivity().finish();
        }
        //********** Mobile + Non-Dialog + DTV ******************//
        else if (KsPreferenceKey.getInstance(getActivity()).getUserType().equalsIgnoreCase(AppLevelConstants.NON_DIALOG) && isDtvAdded == true) {
            getActivity().runOnUiThread(() -> DialogHelper.openDialougeFornonDialog(getActivity(), isLiveChannel));
            getActivity().finish();
        }
        //*********** Mobile + Dialog + Non-DTV *****************//
        else if (KsPreferenceKey.getInstance(getActivity()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded == false) {
            if (AssetContent.isPurchaseAllowed(asset.getMetas(), asset, getActivity())) {
                getActivity().runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(getActivity(), true, isLiveChannel));
                getActivity().finish();
            } else {
                getActivity().runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(getActivity(), false, isLiveChannel));
                getActivity().finish();
            }
        }
        //************ Mobile + Dialog + DTV ********************//
        else if (KsPreferenceKey.getInstance(getActivity()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded == true) {
            if (AssetContent.isPurchaseAllowed(asset.getMetas(), asset, getActivity())) {
                getActivity().runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(getActivity(), true, isLiveChannel));
                getActivity().finish();
            } else {
                getActivity().runOnUiThread(() -> DialogHelper.openDialougeForDtvAccount(getActivity(), false, isLiveChannel));
                getActivity().finish();
            }
        } else {
            showDialog(getString(R.string.something_went_wrong_try_again));
            getActivity().finish();
        }
    }

    private void hidePlayerControls() {
        getBinding().rl1.setVisibility(View.VISIBLE);
        getBinding().playButton.setVisibility(View.GONE);
        getBinding().seekBar.setVisibility(View.GONE);
        getBinding().currentTime.setVisibility(View.GONE);
        getBinding().totalDuration.setVisibility(View.GONE);
        getBinding().fullscreen.setVisibility(View.GONE);
        getBinding().forward.setVisibility(View.GONE);
        getBinding().backward.setVisibility(View.GONE);
        getBinding().pBar.setVisibility(View.VISIBLE);
        getBinding().playericon.setVisibility(View.GONE);
        // getBinding().ivQuality.setVisibility(View.GONE);
        getBinding().ivCancel.setVisibility(View.GONE);
        getBinding().loading.setVisibility(View.VISIBLE);
        getBinding().linearAutoPlayLayout.setVisibility(View.GONE);
        getBinding().slash.setVisibility(View.GONE);
        getBinding().subtitleAudio.setVisibility(View.GONE);
        getBinding().quality.setVisibility(View.GONE);
      //  getBinding().shareWith.setVisibility(View.GONE);

    }

    private void playContent() {
        callHandler();
        if (lockEnable) {

        } else {
            try {
                getBinding().rl1.setVisibility(View.VISIBLE);
                getBinding().playButton.setVisibility(View.VISIBLE);
                getBinding().playButton.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
                //  getBinding().seekBar.setVisibility(View.VISIBLE);
                getBinding().currentTime.setVisibility(View.VISIBLE);
                getBinding().totalDuration.setVisibility(View.VISIBLE);
                getBinding().fullscreen.setVisibility(View.GONE);
                getBinding().forward.setVisibility(View.VISIBLE);
                getBinding().backward.setVisibility(View.GONE);
                //   getBinding().ivQuality.setVisibility(View.VISIBLE);
                getBinding().name.setVisibility(View.VISIBLE);
                getBinding().ivCancel.setVisibility(View.VISIBLE);
                getBinding().slash.setVisibility(View.VISIBLE);
                getBinding().subtitleAudio.setVisibility(View.VISIBLE);
                getBinding().quality.setVisibility(View.VISIBLE);
               // getBinding().shareWith.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                // Log.e("Exception",e.getMessage());
            }
        }
        getBinding().rl.setVisibility(View.VISIBLE);
        getBinding().playericon.setVisibility(View.GONE);
        getBinding().pBar.setVisibility(View.GONE);

        if (dvrEnabled) {
            getBinding().goLive.setVisibility(View.VISIBLE);
            getBinding().seekBar.setVisibility(View.VISIBLE);
        } else {
            getBinding().goLive.setVisibility(View.GONE);

            //Write code here to check content is Program or not

            if (!isLivePlayer) {
                getBinding().seekBar.setVisibility(View.VISIBLE);

                if (KsPreferenceKey.getInstance(getActivity()).getCatchupValue()) {
                    getBinding().arrowBack.setVisibility(View.VISIBLE);
                    getBinding().arrowForward.setVisibility(View.VISIBLE);
                    getBinding().playCatchup.setVisibility(View.VISIBLE);
                    getBinding().currentTime.setVisibility(View.VISIBLE);
                    getBinding().totalDuration.setVisibility(View.VISIBLE);
                    getBinding().ivCancel.setVisibility(View.VISIBLE);
                    getBinding().slash.setVisibility(View.VISIBLE);
                    getBinding().subtitleAudio.setVisibility(View.VISIBLE);
                    getBinding().quality.setVisibility(View.VISIBLE);
                }

            } else {
                getBinding().seekBar.setVisibility(View.GONE);

            }

        }

        if (isLivePlayer && isEnable && dvrEnabled) {
            getBinding().arrowBack.setVisibility(View.VISIBLE);
            getBinding().arrowForward.setVisibility(View.VISIBLE);
            getBinding().playCatchup.setVisibility(View.VISIBLE);

        } else if (isLivePlayer && isEnable) {
            getBinding().arrowBack.setVisibility(View.VISIBLE);
            getBinding().arrowForward.setVisibility(View.VISIBLE);
            getBinding().playCatchup.setVisibility(View.INVISIBLE);
        } else if (isLivePlayer && dvrEnabled) {
            getBinding().arrowBack.setVisibility(View.VISIBLE);
            getBinding().arrowForward.setVisibility(View.VISIBLE);
            getBinding().playCatchup.setVisibility(View.VISIBLE);
        }
        getBinding().loading.setVisibility(View.GONE);
        isPurchased = 1;
        getBinding().linearAutoPlayLayout.setVisibility(View.GONE);
     //   getBinding().lockIcon.setVisibility(View.VISIBLE);
        getBinding().rlUp.setVisibility(View.VISIBLE);

        if (currentRailCommonData != null && currentRailCommonData.size() > 0) {
            getBinding().bottomSheetLayout.bottomSheet.setVisibility(View.VISIBLE);
        }

        if (!isLivePlayer)
            getBinding().rlDown.setVisibility(View.VISIBLE);

    }

    private void getCurrentProgramTimeStamp() {
        viewModel.liveCatchupData(NowPlaying.EXTERNAL_IDS).observe(this, railCommonData -> {
            if (railCommonData != null && railCommonData.size() > 0) {
                try {
                    startTime = railCommonData.get(0).getObject().getStartDate();

                    currentProgramId = String.valueOf(railCommonData.get(0).getObject().getId());

                    //Showing bottomSheet for CatchupTV
                    showCatchupUI(startTime, railCommonData.get(0).getObject());

                } catch (Exception e) {

                }

            }
        });
    }

    //Performing Single and Double Tap on Forward and Backward Icon
    private void performClickForCatchUpContent() {
        getBinding().playCatchup.setOnClickListener(view -> {
            playPauseControl();
        });

        getBinding().arrowBack.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (isLivePlayer) {
                    if (dvrEnabled) {
                        getBinding().pBar.setVisibility(View.VISIBLE);
                        final LiveData<Boolean> booleanLiveData = viewModel.seekToStart();
                        booleanLiveData.observe(baseActivity, aBoolean -> {

                            booleanLiveData.removeObservers(baseActivity);
                            if (booleanLiveData.hasObservers()) return;
                            if (aBoolean != null && aBoolean) {
                                getBinding().pBar.setVisibility(View.GONE);
                            }
                            try {
                                getBinding().seekBar.setProgress(((int) runningPlayer.getCurrentPosition()));
                                getBinding().currentTime.setText(stringForTime(getBinding().seekBar.getProgress()));

                            } catch (Exception w) {
                                PrintLogging.printLog("Exception", "", "" + w);
                            }
                        });
                    }
//                    if (currentRailCommonData!=null && currentRailCommonData.size() > 0) {
//                        int size = currentRailCommonData.size();
//                        // Checking CurrentProgramId with previous ProgramId
//                        for (int i = 0; i < size; i++) {
//                            if (currentProgramId.equalsIgnoreCase(String.valueOf(currentRailCommonData.get(i).getObject().getId()))) {
//
//                                checkcurrentProgramIsLive(currentRailCommonData.get(i - 1).getObject());
//                            }
//                        }
//                    }
                } else {

                }
            }

            @Override
            public void onDoubleClick(View v) {
                if (currentRailCommonData != null && currentRailCommonData.size() > 0) {
                    getBinding().pBar.setVisibility(View.VISIBLE);

                    if (runningPlayer != null) {
                        if (runningPlayer.isPlaying()) {
                            runningPlayer.pause();
                        }
                    }

                    int size = currentRailCommonData.size();
                    // Checking CurrentProgramId with previous ProgramId

                    Log.d("SizeIs", size + "");

                    for (int i = 0; i < size; i++) {
                        if (currentProgramId.equalsIgnoreCase(String.valueOf(currentRailCommonData.get(i).getObject().getId()))) {
                            getBinding().bottomSheetLayout.bottomSheet.setVisibility(View.INVISIBLE);
                            getBinding().playCatchup.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
                            try {
                                checkcurrentProgramIsLive(currentRailCommonData.get(i - 1).getObject());
                                Log.d("SizeIs", currentRailCommonData.get(i - 1).getObject().getName() + "");
                            } catch (IndexOutOfBoundsException e) {

                            }

                        }
                    }
                }
            }
        }));

        getBinding().arrowForward.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

                if (isLivePlayer) {
                    if (dvrEnabled) {
                        getBinding().pBar.setVisibility(View.VISIBLE);
                        final LiveData<Boolean> booleanLiveData = viewModel.seekToDuration();
                        booleanLiveData.observe(baseActivity, aBoolean -> {

                            booleanLiveData.removeObservers(baseActivity);
                            if (booleanLiveData.hasObservers()) return;
                            if (aBoolean != null && aBoolean) {
                                getBinding().pBar.setVisibility(View.GONE);
                            }
                            try {
                                getBinding().seekBar.setProgress(((int) runningPlayer.getCurrentPosition()));
                                getBinding().currentTime.setText(stringForTime(getBinding().seekBar.getProgress()));

                            } catch (Exception w) {
                                PrintLogging.printLog("Exception", "", "" + w);
                            }
                        });
                    }
//                    if (currentRailCommonData!=null && currentRailCommonData.size() > 0) {
//
//                        int size = currentRailCommonData.size();
//                        // Checking CurrentProgramId with next ProgramId
//                        for (int i = 0; i < size; i++) {
//
//                            if (currentProgramId.equalsIgnoreCase(String.valueOf(currentRailCommonData.get(i).getObject().getId()))) {
//
//
//                                checkcurrentProgramIsLive(currentRailCommonData.get(i + 1).getObject());
//                            }
//                        }
//                    }


                } else {

                }


            }

            @Override
            public void onDoubleClick(View v) {

                if (currentRailCommonData != null && currentRailCommonData.size() > 0) {
                    getBinding().pBar.setVisibility(View.VISIBLE);
                    if (runningPlayer != null) {
                        if (runningPlayer.isPlaying()) {
                            runningPlayer.pause();
                        }
                    }

                    int size = currentRailCommonData.size();
                    // Checking CurrentProgramId with next ProgramId
                    for (int i = 0; i < size; i++) {

                        if (currentProgramId.equalsIgnoreCase(String.valueOf(currentRailCommonData.get(i).getObject().getId()))) {
                            getBinding().bottomSheetLayout.bottomSheet.setVisibility(View.INVISIBLE);
                            getBinding().playCatchup.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
                            try {
                                checkcurrentProgramIsLive(currentRailCommonData.get(i + 1).getObject());
                            } catch (IndexOutOfBoundsException e) {

                            }
                        }
                    }
                }
            }
        }));


    }

    private void openShareDialouge() {
        AppCommonMethods.openShareDialog(getActivity(), asset, getActivity());
    }


    private void checkcurrentProgramIsLive(Asset object) {
        isLiveChannel = true;
        viewModel.liveCatchupData(NowPlaying.EXTERNAL_IDS).observe(this, railCommonData -> {
            if (railCommonData != null && railCommonData.size() > 0) {
                try {

                    if (object.getStartDate() > railCommonData.get(0).getObject().getStartDate()) {
                        getBinding().pBar.setVisibility(View.GONE);
                        // ToastHandler.display("Forwarded EPG", getActivity());
                        setReminder(object);
                        KsPreferenceKey.getInstance(getActivity()).setCatchupValue(true);
                        checkIsliveAsset = false;
                    } else if (object.getStartDate() < railCommonData.get(0).getObject().getStartDate()) {
                        KsPreferenceKey.getInstance(getActivity()).setCatchupValue(true);
                        ProgramAsset programAsset = (ProgramAsset) object;
                        //TODO Show popup for No Catchup Enable Program and finish activity
                        if (!programAsset.getEnableCatchUp()) {
                            getBinding().pBar.setVisibility(View.GONE);
                            noCatchupEnabledDialog();

                        } else {
                            if (object.getType() == MediaTypeConstant.getProgram(getActivity())) {
                                hideCatchupControls();
                                KsPreferenceKey.getInstance(getActivity()).setCatchupValue(true);
                                checkIsliveAsset = false;
                                isProgramClicked = true;
                                getSpecificAsset(object);
                            }
                        }


                        // getUrl(AssetContent.getURL(object), object, playerProgress, false, object.getName());


                        //ToastHandler.display("Backward EPG", getActivity());
                    } else if (object.getStartDate().equals(railCommonData.get(0).getObject().getStartDate())) {
                        catchupLiveProgName = railCommonData.get(0).getObject().getName();
                        if (object.getType() == MediaTypeConstant.getProgram(getActivity())) {
                            hideCatchupControls();
                            KsPreferenceKey.getInstance(getActivity()).setCatchupValue(false);
                            checkIsliveAsset = true;
                            isProgramClicked = true;
                            getSpecificAsset(object);
                        }


                    }


                } catch (Exception e) {

                }

            }
        });
    }

    private void hideCatchupControls() {
        getBinding().rl1.setVisibility(View.GONE);
        getBinding().arrowBack.setVisibility(View.GONE);
        getBinding().arrowForward.setVisibility(View.GONE);
        getBinding().currentTime.setVisibility(View.GONE);
        getBinding().totalDuration.setVisibility(View.GONE);
        getBinding().playCatchup.setVisibility(View.GONE);
        getBinding().goLive.setVisibility(View.GONE);
        getBinding().seekBar.setVisibility(View.GONE);
        getBinding().slash.setVisibility(View.GONE);
        getBinding().subtitleAudio.setVisibility(View.GONE);
        getBinding().quality.setVisibility(View.GONE);
    }

    private void noCatchupEnabledDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setTitle(getActivity().getResources().getString(R.string.dialog));
        if (getActivity() != null) {
            builder.setMessage(getActivity().getResources().getString(R.string.catchup_message))
                    .setCancelable(false)
                    .setPositiveButton(getActivity().getString(R.string.ok), (dialog, id) -> {

//                        if (!((Activity) getActivity()).isFinishing()) {
//                            if (getActivity() != null) {
//                                getActivity().onBackPressed();
//                                KsPreferenceKey.getInstance(getActivity()).setCatchupValue(false);
//
//                            }
//                        }

                        if (runningPlayer != null) {
                            if (!runningPlayer.isPlaying()) {
                                runningPlayer.play();
                                KsPreferenceKey.getInstance(getActivity()).setCatchupValue(true);

                            }
                        }
                        getBinding().playCatchup.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
                        getBinding().bottomSheetLayout.bottomSheet.setVisibility(View.VISIBLE);

                    });
            AlertDialog alert = builder.create();
            alert.show();

            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            bp.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        }

    }

    private void getSpecificAsset(Asset object) {

        if (object.getType() == MediaTypeConstant.getProgram(getActivity())) {
            ProgramAsset progAsset = (ProgramAsset) object;
            catchupAsset = object;


            PrintLogging.printLog(this.getClass(), "", "programAssetId" + progAsset.getLinearAssetId());
            viewModel.getSpecificAsset(progAsset.getLinearAssetId().toString()).observe(this, railCommonData -> {
                if (railCommonData != null && railCommonData.getStatus()) {


                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getBinding().pBar.setVisibility(View.GONE);
                            checkErrors(railCommonData.getObject());
                        }
                    }, 3000);
                    playerChecks(railCommonData.getObject());


                }
            });
        }
    }

    private void setReminder(Asset object) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogStyle);
        builder.setTitle(getActivity().getResources().getString(R.string.dialog));
        if (getActivity() != null) {
            builder.setMessage(getActivity().getResources().getString(R.string.set_catchup_reminder))
                    .setCancelable(false)
                    .setPositiveButton(getActivity().getString(R.string.yes), (dialog, id) -> {

                        setReminderTime(object, dialog);

                    })
                    .setNegativeButton(getActivity().getString(R.string.no), (dialog, id) -> {
                        dialog.cancel();
                        if (runningPlayer != null) {
                            if (!runningPlayer.isPlaying()) {
                                runningPlayer.play();


                            }
                        }
                        getBinding().playCatchup.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
                        getBinding().bottomSheetLayout.bottomSheet.setVisibility(View.VISIBLE);

//                        if (!((Activity) getActivity()).isFinishing()) {
//                            if (getActivity() != null) {
//                                getActivity().onBackPressed();
//                                KsPreferenceKey.getInstance(getActivity()).setCatchupValue(false);
//
//                            }
//                        }

                    });
            AlertDialog alert = builder.create();
            alert.show();
            Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            bn.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            bp.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        }
    }

    private void setReminderTime(Asset object, DialogInterface dialog) {
        splitStartTime(AppCommonMethods.get24HourTime(object, 1) + "");
        Calendar c = (AppCommonMethods.getDate(object));

        month = checkDigit(c.get(Calendar.MONTH));
        dd = checkDigit(c.get(Calendar.DATE));
        year = checkDigit(c.get(Calendar.YEAR));

        Log.d("DateValueIs", month + dd + year + "");


        Random random = new Random();
        Long code = object.getId();

        int requestCode = code.intValue();
        PrintLogging.printLog("", "notificationRequestId-->>" + requestCode);
        //String requestCode = String.valueOf(asset.getExternalId());
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        myIntent = new Intent(getActivity(), MyReceiver.class);
        myIntent.putExtra(AppLevelConstants.ID, object.getId());
        myIntent.putExtra(AppLevelConstants.Title, object.getName());
        myIntent.putExtra(AppLevelConstants.DESCRIPTION, object.getDescription());
        myIntent.putExtra(AppLevelConstants.SCREEN_NAME, AppLevelConstants.PROGRAM);
        myIntent.putExtra("requestcode", requestCode);
        myIntent.setAction("com.dialog.dialoggo.MyIntent");
        myIntent.setComponent(new ComponentName(getActivity().getPackageName(), "com.dialog.dialoggo.Alarm.MyReceiver"));

//                    Random random = new Random();
//                    int requestCode = Integer.parseInt(String.format("%02d", random.nextInt(10000)));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            Intent intent = new Intent();

            intent.putExtra(AppLevelConstants.ID, object.getId());
            intent.putExtra(AppLevelConstants.Title, object.getName());
            intent.putExtra(AppLevelConstants.DESCRIPTION, object.getDescription());
            intent.putExtra(AppLevelConstants.SCREEN_NAME, AppLevelConstants.PROGRAM);
            intent.putExtra("requestcode", requestCode);

            intent.setComponent(new ComponentName(getActivity().getPackageName(), "com.dialog.dialoggo.Alarm.MyReceiver"));
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        } else {

            pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        Calendar calendarToSchedule = Calendar.getInstance();
        calendarToSchedule.setTimeInMillis(System.currentTimeMillis());
        calendarToSchedule.clear();

        calendarToSchedule.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(dd), Integer.parseInt(hour), Integer.parseInt(minute), 0);


        reminderDateTimeInMilliseconds = calendarToSchedule.getTimeInMillis();

        PrintLogging.printLog("", "valueIsform" + reminderDateTimeInMilliseconds);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(reminderDateTimeInMilliseconds, pendingIntent), pendingIntent);
        } else {

            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderDateTimeInMilliseconds, pendingIntent);
        }

        new KsPreferenceKey(getActivity()).setReminderId(object.getId().toString(), true);
        ToastHandler.show(getActivity().getResources().getString(R.string.reminder), getActivity());
        dialog.cancel();
        if (runningPlayer != null) {
            if (!runningPlayer.isPlaying()) {
                runningPlayer.play();


            }
        }
        getBinding().playCatchup.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
        getBinding().bottomSheetLayout.bottomSheet.setVisibility(View.VISIBLE);

//        if (!((Activity) getActivity()).isFinishing()) {
//            if (getActivity() != null) {
//                getActivity().onBackPressed();
//                KsPreferenceKey.getInstance(getActivity()).setCatchupValue(false);
//
//            }
//        }
    }


    private String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }


    private void showCatchupUI(Long startTime, Asset object) {
        /*try {

            bottomSheetBehavior = BottomSheetBehavior.from(getBinding().bottomSheetLayout.bottomSheet);
            // bottomSheetBehavior.setPeekHeight(360);
            // change the state of the bottom sheet

            gestureDetector = new GestureDetector(getActivity(), new SwipeGestureListener(bottomSheetBehavior, new DragListner() {
                @Override
                public void dragging(String name) {
                    if (name.equalsIgnoreCase("up")) {
                        drag = true;
                        timeHandler.removeCallbacks(myRunnable);
                        getBinding().seekBar.setVisibility(View.GONE);
                        getBinding().rlUp.setVisibility(View.GONE);
                        getBinding().playerMediaControls.setVisibility(View.GONE);
                        getBinding().currentTime.setVisibility(View.GONE);
                        getBinding().totalDuration.setVisibility(View.GONE);
                        getBinding().goLive.setVisibility(View.GONE);
                        getBinding().lockImg.setVisibility(View.GONE);
                        getBinding().arrowBack.setVisibility(View.GONE);
                        getBinding().arrowForward.setVisibility(View.GONE);
                        getBinding().playCatchup.setVisibility(View.INVISIBLE);
                    } else if (name.equalsIgnoreCase("down")) {
                        Log.d("ElseCalled", "True");
                        drag = false;
                    }
                }
            }));


            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


            //Initializing RecyclerView
            getBinding().bottomSheetLayout.recycleview.hasFixedSize();
            getBinding().bottomSheetLayout.recycleview.setNestedScrollingEnabled(false);
            getBinding().bottomSheetLayout.recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));


            loadPreviousCatchup(startTime, object);

            // set hideable or not
            bottomSheetBehavior.setHideable(false);

            bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View view, int newState) {


                    String state = "";

                    switch (newState) {
                        case BottomSheetBehavior.STATE_DRAGGING: {
                            state = "DRAGGING";

                            //Removing Animation and Hiding Views
                            if (timer && timeHandler != null) {
                                timeHandler.removeCallbacks(myRunnable);
                                getBinding().seekBar.setVisibility(View.GONE);
                                getBinding().rlUp.setVisibility(View.GONE);
                                getBinding().playerMediaControls.setVisibility(View.GONE);
                                getBinding().currentTime.setVisibility(View.GONE);
                                getBinding().totalDuration.setVisibility(View.GONE);
                                getBinding().goLive.setVisibility(View.GONE);
                                getBinding().lockImg.setVisibility(View.GONE);
                                getBinding().arrowBack.setVisibility(View.GONE);
                                getBinding().arrowForward.setVisibility(View.GONE);
                                getBinding().playCatchup.setVisibility(View.INVISIBLE);

                            }
                            break;
                        }
                        case BottomSheetBehavior.STATE_SETTLING: {
                            break;
                        }
                        case BottomSheetBehavior.STATE_EXPANDED: {
                            bottomSheetBehavior.setPeekHeight(180);
                            break;
                        }
                        case BottomSheetBehavior.STATE_COLLAPSED: {
                            getBinding().bottomSheetLayout.bottomSheet.setVisibility(View.VISIBLE);
                            //Calling Animation and Showing View
                            bottomSheetBehavior.setPeekHeight(40);
                            callHandler();
                            //  ShowAndHideView();


                            if (isLivePlayer) {

                                if (dvrEnabled) {
                                    getBinding().goLive.setVisibility(View.VISIBLE);
                                    getBinding().seekBar.setVisibility(View.VISIBLE);
                                    getBinding().rlUp.setVisibility(View.VISIBLE);
                                    getBinding().playerMediaControls.setVisibility(View.GONE);
                                    getBinding().currentTime.setVisibility(View.GONE);
                                    getBinding().totalDuration.setVisibility(View.GONE);
                                    getBinding().lockImg.setVisibility(View.VISIBLE);
                                    getBinding().arrowBack.setVisibility(View.VISIBLE);
                                    getBinding().arrowForward.setVisibility(View.VISIBLE);
                                    getBinding().playCatchup.setVisibility(View.VISIBLE);
                                } else {
                                    getBinding().goLive.setVisibility(View.GONE);
                                    getBinding().seekBar.setVisibility(View.GONE);
                                    getBinding().playerMediaControls.setVisibility(View.GONE);
                                    getBinding().currentTime.setVisibility(View.GONE);
                                    getBinding().totalDuration.setVisibility(View.GONE);
                                    getBinding().lockImg.setVisibility(View.VISIBLE);
                                    getBinding().rlUp.setVisibility(View.VISIBLE);
                                    getBinding().arrowBack.setVisibility(View.VISIBLE);
                                    getBinding().arrowForward.setVisibility(View.VISIBLE);
                                    getBinding().playCatchup.setVisibility(View.INVISIBLE);
                                }


                            } else {
                                if (KsPreferenceKey.getInstance(getActivity()).getCatchupValue()) {
                                    getBinding().seekBar.setVisibility(View.VISIBLE);
                                    getBinding().rlUp.setVisibility(View.VISIBLE);
                                    getBinding().playerMediaControls.setVisibility(View.GONE);
                                    getBinding().currentTime.setVisibility(View.VISIBLE);
                                    getBinding().totalDuration.setVisibility(View.VISIBLE);
                                    getBinding().lockImg.setVisibility(View.VISIBLE);
                                    getBinding().goLive.setVisibility(View.GONE);
                                    getBinding().arrowBack.setVisibility(View.VISIBLE);
                                    getBinding().arrowForward.setVisibility(View.VISIBLE);
                                    getBinding().playCatchup.setVisibility(View.VISIBLE);
                                } else {
                                    getBinding().seekBar.setVisibility(View.VISIBLE);
                                    getBinding().rlUp.setVisibility(View.VISIBLE);
                                    getBinding().playerMediaControls.setVisibility(View.VISIBLE);
                                    getBinding().currentTime.setVisibility(View.VISIBLE);
                                    getBinding().totalDuration.setVisibility(View.VISIBLE);
                                    getBinding().lockImg.setVisibility(View.VISIBLE);
                                    getBinding().goLive.setVisibility(View.GONE);
                                    getBinding().arrowBack.setVisibility(View.GONE);
                                    getBinding().arrowForward.setVisibility(View.GONE);
                                    getBinding().playCatchup.setVisibility(View.INVISIBLE);
                                }


                            }


                            break;
                        }
                        case BottomSheetBehavior.STATE_HIDDEN: {
                            state = "HIDDEN";
                            break;
                        }
                    }


                }

                @Override
                public void onSlide(@NonNull View view, float v) {

                }
            });
        } catch (IllegalArgumentException e) {
            Log.d("ExceptionIs", e.getMessage());
        }*/
    }

    private void loadPreviousCatchup(Long startTime, Asset object) {
        viewModel.loadCatchupData(NowPlaying.EXTERNAL_IDS, String.valueOf(startTime), 1).observe(this, railCommonData -> {
            if (railCommonData != null && railCommonData.size() > 0) {
                try {
                    Collections.reverse(railCommonData);

                    RailCommonData currentRailCommonData = new RailCommonData();

                    currentRailCommonData.setObject(object);
                    railCommonData.add(currentRailCommonData);

                    loadNextCatchup(startTime, railCommonData);

                } catch (Exception e) {

                }

            } else {
                RailCommonData currentRailCommonData = new RailCommonData();

                currentRailCommonData.setObject(object);
                railCommonData.add(currentRailCommonData);

                loadNextCatchup(startTime, railCommonData);
            }
        });
    }

    private void loadNextCatchup(Long startTime, List<RailCommonData> railCommonDataPrevious) {
        viewModel.loadCatchupData(NowPlaying.EXTERNAL_IDS, String.valueOf(startTime), 2).observe(this, railCommonData -> {
            if (railCommonData != null && railCommonData.size() > 0) {
                try {

                    railCommonDataPrevious.addAll(railCommonData);

                    currentRailCommonData = new ArrayList<>();
                    for (int i = 0; i < railCommonDataPrevious.size(); i++) {
                        currentRailCommonData.add(railCommonDataPrevious.get(i));
                    }


                    for (int i = 0; i < railCommonDataPrevious.size(); i++) {
//                        int stTime = Integer.parseInt(AppCommonMethods.getProgramTime(railCommonDataPrevious.get(i).getObject(), 1));
//                        int endTime = Integer.parseInt(AppCommonMethods.getProgramTime(railCommonDataPrevious.get(i).getObject(), 2));

                        Long stTime = railCommonDataPrevious.get(i).getObject().getStartDate();
                        Long endTime = railCommonDataPrevious.get(i).getObject().getEndDate();

                        //    Long currentTime = Integer.parseInt(getDateTimeStamp(System.currentTimeMillis()));


                        String currentTime = AppCommonMethods.getCurrentTimeStamp();
                        // Long currentTime = System.currentTimeMillis();
                        if (Long.valueOf(currentTime) > stTime && Long.valueOf(currentTime) < endTime) {
                            new KsPreferenceKey(getActivity()).setLiveCatchUpId(railCommonDataPrevious.get(i).getObject().getId().toString(), true);
                        } else {
                            new KsPreferenceKey(getActivity()).setLiveCatchUpId(railCommonDataPrevious.get(i).getObject().getId().toString(), false);
                        }
                    }

//                    getBinding().bottomSheetLayout.bottomSheet.setVisibility(View.VISIBLE);

                    CatchupTvAdapter catchupTvAdapter = new CatchupTvAdapter(getActivity(), railCommonDataPrevious, isLivePlayer, currentProgramId, DTPlayer.this);
                    getBinding().bottomSheetLayout.recycleview.setAdapter(catchupTvAdapter);


                    //  Log.d("asdsadsad", railCommonData1.size() + "");
                } catch (Exception e) {

                }

            }
        });
    }

    private String getDateTimeStamp(Long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a", Locale.US);
        return formatter.format(timeStamp);
    }

    private void startPhoenixInit() {

        map = asset.getTags();

        OnMediaLoadCompletion playLoadedEntry = response -> {
            if (baseActivity != null) {
                baseActivity.runOnUiThread(() -> {
                    if (response != null) {
                        if (response.isSuccess()) {

                            onMediaLoaded(response.getResponse());
                        } else {
                            ConvivaManager.getConvivaVideoAnalytics(baseActivity).reportPlaybackFailed(response.getError().toString());
                            /*handling 601 error code for session token expire*/
                            isError = true;
                            if (response.getError().getCode().equalsIgnoreCase("601")) {
                                DialogHelper.showLoginDialog(getActivity());
                            } else {
                                //showDialog(response.getError().getMessage());
                                if (baseActivity != null && !baseActivity.isFinishing()) {
                                    if (response.getError().getCode().equalsIgnoreCase(AppLevelConstants.KS_EXPIRE)) {
                                        loggedOutMessage();
                                    } else {
                                        showDialog(new ErrorCallBack().ErrorMessage(response.getError().getCode(), response.getError().getMessage()));
                                    }
                                }
                            }
                        }
                    }

                });
            }

        };
        startDishOttMediaLoadingProd(playLoadedEntry, asset);
    }

    private void getHungamaUrlToPlayContent(String providerExternalContentId) {
        viewModel.getHungamaUrl(providerExternalContentId).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String contentUrl) {
                if (contentUrl != null) {
                    Log.d("asasasasaasa", contentUrl);
                    PKMediaEntry mediaEntry = createMediaEntry(contentUrl);
                    onMediaLoaded(mediaEntry);
                } else {
                    isError = true;
                    showDialog(getString(R.string.something_went_wrong_try_again));
                }

            }
        });
    }

    private PKMediaEntry createMediaEntry(String contentUrl) {
        PKMediaEntry mediaEntry = new PKMediaEntry();

        //Set id for the entry.
        mediaEntry.setId(String.valueOf(asset.getId()));
        mediaEntry.setName(asset.getName());
        // mediaEntry.setDuration(881000);
        //Set media entry type. It could be Live,Vod or Unknown.
        //In this sample we use Vod.
        mediaEntry.setMediaType(PKMediaEntry.MediaEntryType.Vod);

        //Create list that contains at least 1 media source.
        //Each media entry can contain a couple of different media sources.
        //All of them represent the same content, the difference is in it format.
        //For example same entry can contain PKMediaSource with dash and another
        // PKMediaSource can be with hls. The player will decide by itself which source is
        // preferred for playback.
        List<PKMediaSource> mediaSources = createMediaSources(contentUrl);

        //Set media sources to the entry.
        mediaEntry.setSources(mediaSources);

        return mediaEntry;
    }

    private List<PKMediaSource> createMediaSources(String contentUrl) {
        PKMediaSource mediaSource = new PKMediaSource();

        //Set the id.
        mediaSource.setId(String.valueOf(asset.getId()));

        //Set the content url. In our case it will be link to hls source(.m3u8).
        mediaSource.setUrl(contentUrl);

        //Set the format of the source. In our case it will be hls in case of mpd/wvm formats you have to to call mediaSource.setDrmData method as well
        mediaSource.setMediaFormat(PKMediaFormat.hls);

        // Add DRM data if required
//        if (LICENSE_URL != null) {
//            mediaSource.setDrmData(Collections.singletonList(
//                    new PKDrmParams(LICENSE_URL, PKDrmParams.Scheme.WidevineCENC)
//            ));
//        }

        return Collections.singletonList(mediaSource);
    }

    private void loggedOutMessage() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(baseActivity, R.style.AppAlertTheme);
            builder.setTitle(baseActivity.getResources().getString(R.string.dialog));
            builder.setMessage(baseActivity.getResources().getString(R.string.logged_out_message))
                    .setCancelable(true)
                    .setPositiveButton("Ok", (dialog, id) -> {
                        KsPreferenceKey.getInstance(baseActivity).setUserActive(false);
                        KsPreferenceKey.getInstance(baseActivity).setUserActive(false);
                        KsPreferenceKey.getInstance(baseActivity).setUser(null);
                        KsPreferenceKey.getInstance(baseActivity).setStartSessionKs("");
                        KsPreferenceKey.getInstance(baseActivity).setMsisdn("");
                        KsPreferenceKey.getInstance(baseActivity).setUserSelectedRating("");
                        KsPreferenceKey.getInstance(baseActivity).setParentalActive(false);
                        KsPreferenceKey.getInstance(baseActivity).setUserType("");
                        new ActivityLauncher(baseActivity).homeScreen(baseActivity, HomeActivity.class);

                    });

            android.app.AlertDialog alert = builder.create();
            alert.show();
            alert.setCancelable(false);
            Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            bn.setTextColor(ContextCompat.getColor(baseActivity, R.color.blue));
            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            bp.setTextColor(ContextCompat.getColor(baseActivity, R.color.colorPrimary));
        } catch (Exception ignored) {

        }
    }

    private void startDishOttMediaLoadingProd(final OnMediaLoadCompletion completion, Asset asset) {
        SessionProvider ksSessionProvider = new SessionProvider() {
            @Override
            public String baseUrl() {

                if (BuildConfig.FLAVOR.equalsIgnoreCase("prod")) {
                    Log.d("LoadedUrlIs", "PROD");
                    return KsPreferenceKey.getInstance(baseActivity).getKalturaPhoenixUrl();
                } else {
                    Log.d("LoadedUrlIs", "QA");
                    return "https://rest-sgs1.ott.kaltura.com/api_v3/";
                }


            }

            @Override
            public void getSessionToken(OnCompletion<PrimitiveResult> completion) {

                String ks1 = KsPreferenceKey.getInstance(baseActivity).getStartSessionKs();
                if (ks1.isEmpty()) {
                    ks1 = KsPreferenceKey.getInstance(baseActivity).getAnonymousks();

                }
                if (completion != null) {
                    completion.onComplete(new PrimitiveResult(ks1));
                }
            }

            @Override
            public int partnerId() {
                return AppLevelConstants.PARTNER_ID;
            }
        };

        new KsServices(baseActivity).callBookMarking(asset, position -> {
            if (assetType == MediaTypeConstant.getProgram(baseActivity)) {
                assetPosition = 0;
            } else {
                assetPosition = position;
            }

        });

        String mediaId = asset.getId().toString();
        AssetContent.getVideoResolution(asset.getTags()).observe(this, videoResolution -> {
            String format = "";
            if (AppCommonMethods.callpreference(baseActivity) != null && AppCommonMethods.callpreference(baseActivity).getParams() != null && AppCommonMethods.callpreference(baseActivity).getParams().getFilesFormat() != null && AppCommonMethods.callpreference(baseActivity).getParams().getFilesFormat().getDASHWV() != null) {
                format = AppCommonMethods.callpreference(baseActivity).getParams().getFilesFormat().getDASHWV();
            } else {
                format = AppLevelConstants.DASH_WIDEVINE;
            }
            MediaEntryProvider mediaProvider;
            if (asset.getType() == MediaTypeConstant.getLinear(baseActivity)) {
                if (!isLivePlayer) {
                    mediaProvider = new PhoenixMediaProvider()
                            .setSessionProvider(ksSessionProvider)
                            .setAssetId(mediaId)
                            .setProtocol(PhoenixMediaProvider.HttpProtocol.Https)
                            .setContextType(APIDefines.PlaybackContextType.Catchup)
                            .setAssetType(APIDefines.KalturaAssetType.Epg)
                            .setFormats(format);
                    mediaProvider.load(completion);
                } else {
                    mediaProvider = new PhoenixMediaProvider()
                            .setSessionProvider(ksSessionProvider)
                            .setAssetId(mediaId)
                            .setProtocol(PhoenixMediaProvider.HttpProtocol.Https)
                            .setContextType(APIDefines.PlaybackContextType.Playback)
                            .setAssetType(APIDefines.KalturaAssetType.Media)
                            .setFormats(format);
                    mediaProvider.load(completion);
                }
            } else if (asset.getType() == MediaTypeConstant.getProgram(baseActivity)) {
                mediaProvider = new PhoenixMediaProvider()
                        .setSessionProvider(ksSessionProvider)
                        .setAssetId(mediaId)
                        .setProtocol(PhoenixMediaProvider.HttpProtocol.Https)
                        .setContextType(APIDefines.PlaybackContextType.Catchup)
                        .setAssetType(APIDefines.KalturaAssetType.Epg)
                        .setFormats(format);
                mediaProvider.load(completion);

            } else {
                mediaProvider = new PhoenixMediaProvider()
                        .setSessionProvider(ksSessionProvider)
                        .setAssetId(mediaId)
                        .setProtocol(PhoenixMediaProvider.HttpProtocol.Https)
                        .setContextType(APIDefines.PlaybackContextType.Playback)
                        .setAssetType(APIDefines.KalturaAssetType.Media)
                        .setFormats(format);
                mediaProvider.load(completion);
            }


        });

    }

    private void onMediaLoaded(PKMediaEntry mediaEntry) {
        try {
            if (dvrEnabled) {
                mediaEntry.setMediaType(PKMediaEntry.MediaEntryType.DvrLive);
            }

            if (getActivity() != null && getActivity().getSystemService(Context.AUDIO_SERVICE) != null) {
                mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            }

            int result = mAudioManager.requestAudioFocus(this,
                    // Use the music stream.
                    AudioManager.STREAM_MUSIC,
                    // Request permanent focus.
                    AudioManager.AUDIOFOCUS_GAIN);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                startPlayer(mediaEntry);
            }
        } catch (Exception e) {

        }
    }

    private void startPlayer(PKMediaEntry mediaEntry) {
    /*    new KsServices(baseActivity).callBookMarking(asset, position -> {
            if (assetType == MediaTypeConstant.getProgram(baseActivity)) {
                assetPosition = 0;
            } else {
                assetPosition = position;
            }

        });*/
        Log.e("DTPlayer", "DTPlayer assetPosition" + assetPosition);
        try {
            viewModel.startPlayerBookmarking(mediaEntry, UDID.getDeviceId(baseActivity, baseActivity.getContentResolver()), asset, isPurchased, assetPosition).observe(this, player -> {
                if (player != null) {
                    adsCallBackHandling(player);
                    getPlayerView(player);
                    player.getSettings().setSurfaceAspectRatioResizeMode(PKAspectRatioResizeMode.fill);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            haveAudioOrNot();
                            haveSubtitleorNot();
                        }
                    }, 4000);

                }
            });
        } catch (Exception e) {
            PrintLogging.printLog("Exception", e.toString());
        }


    }

    private void haveAudioOrNot() {

        viewModel.loadAudioWithPlayer().observe(this, audioTracks -> {
            if (audioTracks != null && audioTracks.size() > 0) {
                viewModel.getAudioTrackItems().observe(baseActivity, trackItems -> {
                    if (trackItems.length == 1) {
                        if (trackItems[0] != null) {
                            isAudioTracks = true;
                        } else {
                            isAudioTracks = false;
                        }
                    } else {
                        if (trackItems.length >= 1) {
                           /* for (int i = 0; i < trackItems.length; i++) {
                                String trackLang = trackItems[i].getTrackName().trim();
                                String lang = new KsPreferenceKey(baseActivity).getAudioLanguage().trim();
                                if (trackLang.equalsIgnoreCase(lang)) {
                                    viewModel.changeAudioTrack(trackItems[i].getUniqueId());
                                    break;
                                }
                            }*/
                            isAudioTracks = true;
                        } else {
                            isAudioTracks = false;
                        }
                    }

                });

            } else {
                isAudioTracks = false;
            }

        });
    }

    public void showAdsView() {
        adRunning = true;
      //  getBinding().lockIcon.setVisibility(View.GONE);
        getBinding().rl1.setVisibility(View.GONE);
        getBinding().loading.setVisibility(View.GONE);
        getBinding().pBar.setVisibility(View.GONE);

    }

    private void adsCallBackHandling(final Player player) {

        player.addListener(this, AdEvent.started, event -> {
            //Some events holds additional data objects in them.
            //In order to get access to this object you need first cast event to
            //the object it belongs to. You can learn more about this kind of objects in
            //our documentation.
            //  ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdMetric("ADS STARTED");

            if (isFirstAd) {
                new Handler().postDelayed(() -> {
                    haveAudioOrNot();
                    haveSubtitleorNot();

                }, 7000);
                isFirstAd = false;
            }
            AdEvent.AdStartedEvent adStartedEvent = event;

            //Then you can use the data object itself.
            AdInfo adInfo = adStartedEvent.adInfo;
            //Print to log content type of this ad.
            showAdsView();
            Log.d(TAG, "ad event received: " + event.eventType().name()
                    + ". Additional info: ad content type is: "
                    + adInfo.getAdContentType());
        });

        player.addListener(this, AdEvent.contentResumeRequested, event -> {
            Log.d(TAG, "ADS_PLAYBACK_ENDED");
        });

        player.addListener(this, AdEvent.adPlaybackInfoUpdated, event -> {
            AdEvent.AdPlaybackInfoUpdated playbackInfoUpdated = event;
            Log.d(TAG, "AD_PLAYBACK_INFO_UPDATED  = " + playbackInfoUpdated.width + "/" + playbackInfoUpdated.height + "/" + playbackInfoUpdated.bitrate);
        });

        player.addListener(this, AdEvent.skippableStateChanged, event -> {
            Log.d(TAG, "SKIPPABLE_STATE_CHANGED");
        });

        player.addListener(this, AdEvent.adRequested, event -> {
            AdEvent.AdRequestedEvent adRequestEvent = event;
            Log.d(TAG, "AD_REQUESTED adtag = " + adRequestEvent.adTagUrl);
        });

        player.addListener(this, AdEvent.playHeadChanged, event -> {
            AdEvent.AdPlayHeadEvent adEventProress = event;
            //Log.d(TAG, "received AD PLAY_HEAD_CHANGED " + adEventProress.adPlayHead);
        });


        player.addListener(this, AdEvent.adBreakStarted, event -> {
            Log.d(TAG, "AD_BREAK_STARTED");
            showAdsView();
        });
        player.addListener(this, AdEvent.adBreakEnded, event -> {
            //  ConvivaManager.getConvivaVideoAnalytics(baseActivity).reportAdBreakEnded();
        });

        player.addListener(this, AdEvent.cuepointsChanged, event -> {
            AdEvent.AdCuePointsUpdateEvent cuePointsList = event;
            Log.d(TAG, "AD_CUEPOINTS_UPDATED HasPostroll = " + cuePointsList.cuePoints.hasPostRoll());
            hasPostRoll = cuePointsList.cuePoints.hasPostRoll();
            hasPreRoll = cuePointsList.cuePoints.hasPreRoll();
            hasMidRoll = cuePointsList.cuePoints.hasMidRoll();

        });
        player.addListener(this, PlayerEvent.stopped, event -> {
            ConvivaManager.convivaPlayerStoppedReportRequest();

        });
        player.addListener(this, PlayerEvent.playing, event -> {
            if (player != null) {
                ConvivaManager.convivaPlayerPlayReportRequest();
                AdController adController = player.getController(AdController.class);
                if (adController != null) {
                    if (adController.isAdDisplayed()) {
                        Log.d(TAG, "AD CONTROLLER API: " + adController.getAdCurrentPosition() + "/" + adController.getAdDuration());
                    }
                    adController.skip();
                }
            }
            adRunning = false;
          //  getBinding().lockIcon.setVisibility(View.VISIBLE);
            if (lockEnable) {
                getBinding().rl1.setVisibility(View.GONE);
            } else {
                getBinding().rl1.setVisibility(View.VISIBLE);
                playContent();
            }


        });
        player.addListener(this, PlayerEvent.playbackInfoUpdated, event -> {


        });

        player.addListener(this, AdEvent.loaded, event -> {
            Map<String, Object> contentInfo = new HashMap<String, Object>();
            contentInfo.put(ConvivaSdkConstants.POD_INDEX, event.adInfo.getPodIndex());
            contentInfo.put(ConvivaSdkConstants.POD_DURATION, event.adInfo.getAdDuration());
            contentInfo.put(ConvivaManager.AD_SYSTEM, "DFP");
            contentInfo.put(ConvivaManager.AD_TECHNOLOGY, "Client Side");
            contentInfo.put(ConvivaManager.AD_STITCHER, "NA");
            ConvivaManager.getConvivaVideoAnalytics(baseActivity).reportAdBreakStarted(ConvivaSdkConstants.AdPlayer.CONTENT, ConvivaSdkConstants.AdType.CLIENT_SIDE, contentInfo);
            showAdsView();
        });

        player.addListener(this, AdEvent.started, event -> {
            AdEvent.AdStartedEvent adStartedEvent = event;
            Map<String, Object> contentInfo = new HashMap<String, Object>();
            contentInfo.put(ConvivaSdkConstants.ASSET_NAME, adStartedEvent.adInfo.getAdTitle());
            contentInfo.put(ConvivaSdkConstants.STREAM_URL, "https://pubads.g.doubleclick.net/gampad/ads?sz=640x480&iu=/124319096/external/ad_rule_samples&ciu_szs=300x250&ad_rule=1&impl=s&gdfp_req=1&env=vp&output=vmap&unviewed_position_start=1&cust_params=deployment%3Ddevsite%26sample_ar%3Dpreonly&cmsid=496&vid=short_onecue&correlator=");
            ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdLoaded(contentInfo);
            ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdStarted();
            ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdMetric(ConvivaSdkConstants.PLAYBACK.PLAYER_STATE, ConvivaSdkConstants.PlayerState.PLAYING);


            showAdsView();
            Log.d(TAG, "AD_STARTED w/h - " + adStartedEvent.adInfo.getAdWidth() + "/" + adStartedEvent.adInfo.getAdHeight());
        });

        player.addListener(this, AdEvent.adBreakReady, event -> {
            ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdMetric(ConvivaSdkConstants.PLAYBACK.PLAYER_STATE, ConvivaSdkConstants.PlayerState.PLAYING);
            Log.d(TAG, "PLay");
        });


        player.addListener(this, AdEvent.resumed, event -> {
            ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdMetric(ConvivaSdkConstants.PLAYBACK.PLAYER_STATE, ConvivaSdkConstants.PlayerState.PLAYING);

            adRunning = true;
            showAdsView();
            Log.d(TAG, "PLAY");
        });

        player.addListener(this, AdEvent.paused, event -> {
            ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdMetric(ConvivaSdkConstants.PLAYBACK.PLAYER_STATE, ConvivaSdkConstants.PlayerState.PAUSED);

            isAdPause = true;
            Log.d(TAG, "AD_PAUSED");
        });

        player.addListener(this, AdEvent.skipped, event -> {
            checkFatalError();
            Log.d(TAG, "AD_SKIPPED");
        });
        player.addListener(this, PlayerEvent.ended, event -> {
            isPlayerEnded = true;


        });

        player.addListener(this, AdEvent.allAdsCompleted, event -> {
            Log.d(TAG, "AD_ALL_ADS_COMPLETED");
            allAdsCompleted = true;
            adRunning = false;
            checkPostBinge(player);
        });

        player.addListener(this, AdEvent.completed, event -> {
            Log.d(TAG, "AD_COMPLETED");
            ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdMetric(ConvivaSdkConstants.PLAYBACK.PLAYER_STATE, ConvivaSdkConstants.PlayerState.STOPPED);
            ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdEnded();
            ConvivaManager.getConvivaVideoAnalytics(baseActivity).reportAdBreakEnded();
            ConvivaManager.removeConvivaAdsSession();


            // ConvivaManager.getConvivaAdAnalytics(baseActivity).reportAdEnded();
            if (isPlayerEnded && isWaitingBinge) {
                allAdsCompleted = true;
                checkPostBinge(player);
            } else if (exitPlayer && !isSeries) {
                exitPlayeriew(player);
            } else {
                adRunning = false;
                checkFatalError();
            }
            //  Toast.makeText(getActivity(), "completed", Toast.LENGTH_LONG).show();

        });

        player.addListener(this, AdEvent.firstQuartile, event -> {
            Log.d(TAG, "FIRST_QUARTILE");
            adRunning = true;
        });

        player.addListener(this, AdEvent.midpoint, event -> {
            Log.d(TAG, "MIDPOINT");
            if (player != null) {
                AdController adController = player.getController(AdController.class);
                if (adController != null) {
                    if (adController.isAdDisplayed()) {
                        Log.d(TAG, "AD CONTROLLER API: " + adController.getAdCurrentPosition() + "/" + adController.getAdDuration());
                    }
                    //adController.skip();
                }
            }
        });

        player.addListener(this, AdEvent.thirdQuartile, event -> {
            Log.d(TAG, "THIRD_QUARTILE");
        });

        player.addListener(this, AdEvent.adBreakEnded, event -> {
            Log.d(TAG, "AD_BREAK_ENDED");
        });

        player.addListener(this, AdEvent.adClickedEvent, event -> {
            AdEvent.AdClickedEvent advtClickEvent = event;

            Log.d(TAG, "AD_CLICKED url = " + advtClickEvent.clickThruUrl);
        });


        player.addListener(this, AdEvent.adBufferStart, event -> {
            AdEvent.AdBufferStart adBufferStartEvent = event;
            Log.d(TAG, "AD_events-->>" + "AD_BUFFER_START");
            showAdsView();

        });

        player.addListener(this, AdEvent.adBufferEnd, event -> {
            AdEvent.AdBufferEnd adBufferEnd = event;
            Log.d(TAG, "AD_events-->>" + "AD_BUFFER_END");
            showAdsView();
        });


        player.addListener(this, AdEvent.error, event -> {
            AdEvent.Error adError = event;
            //  checkFatalError();
            if (player != null) {

                if (adError.error.errorType.name().toUpperCase().contains("QUIET_LOG_ERROR") || adError.error.errorType.name().toUpperCase().contains("VIDEO_PLAY_ERROR")) {
                  //  getBinding().lockIcon.setVisibility(View.VISIBLE);
                    if (lockEnable) {
                        getBinding().rl1.setVisibility(View.GONE);
                    } else {
                        getBinding().rl1.setVisibility(View.VISIBLE);
                        playContent();
                    }
                }
                if (hasPostRoll) {
                    allAdsCompleted = true;
                }
                if (isPlayerEnded && isWaitingBinge) {
                    allAdsCompleted = true;
                    checkPostBinge(player);
                } else {
                    adRunning = false;
                    allAdsCompleted = true;
                }

            }
            Log.e(TAG, "AD_ERROR : " + adError.error.errorType.name());
        });

        player.addListener(this, PlayerEvent.error, event -> {
            Log.e(TAG, "PLAYER ERROR " + event.error.message);
        });
    }

    public void checkPostBinge(Player player) {
        if (allAdsCompleted && isPlayerEnded && isWaitingBinge) {
            allAdsCompleted = false;
            isPlayerEnded = false;
            if (isSeries && hasNextEpisode) {
                getBinding().pBar.setVisibility(View.GONE);
                setBingView(player);
            }
        } else if (exitPlayer && !isSeries) {
            exitPlayeriew(player);
        }
    }

    public void checkFatalError() {
        adRunning = false;
        if (isVideoError) {
            playBackError();
        } else {
          //  getBinding().lockIcon.setVisibility(View.VISIBLE);

            if (lockEnable) {
                getBinding().rl1.setVisibility(View.GONE);
            } else {
                getBinding().rl1.setVisibility(View.VISIBLE);
                playContent();

            }

        }

    }

    private void getAssetImage(Asset asset, String deviceid) {
        if (asset.getImages().size() > 0) {

//            getBinding().urlimage.setVisibility(View.GONE);
            image_url = asset.getImages().get(0).getUrl();
            image_url = image_url + AppLevelConstants.WIDTH + (int) getResources().getDimension(R.dimen.carousel_image_width) + AppLevelConstants.HEIGHT + (int) getResources().getDimension(R.dimen.carousel_image_height) + AppLevelConstants.QUALITY;
//            ImageHelper.getInstance(getBinding().urlimage.getContext()).loadImageTo(getBinding().urlimage,image_url,R.drawable.square1);
        }
        if (runningPlayer != null) {
            runningPlayer.getView().setVisibility(View.GONE);
            runningPlayer.stop();
            hidePlayerControls();
        }

    }

    private void getPlayerView(final Player player) {

        runningPlayer = player;
        View view = player.getView();
        getBinding().rl.addView(view);




        /*if(isSeries && lockEnable)
        {
            getBinding().rl1.setVisibility(View.GONE);
            getBinding().listViewSettings.setVisibility(View.GONE);
        }*/
        viewModel.playerCallback(assetPosition, baseActivity).observe(this, s -> {
            if (TextUtils.isEmpty(s)) {
                if (adRunning && AppCommonMethods.isAdsEnable) {
                    isVideoError = true;
                } else {

                    player.pause();
                    playBackError();
                }
            } else {
                getBinding().totalDuration.setText(s);
                final LiveData<Boolean> booleanLiveData = viewModel.getStateOfPlayer();
                booleanLiveData.observe(baseActivity, aBoolean -> {

                    hideSoftKeyButton();
                    isPlayerStart = true;
                    // playContent();

                    if (lockEnable) {
                        getBinding().rl1.setVisibility(View.GONE);
                        getBinding().listViewSettings.setVisibility(View.GONE);
                    } else {
                        getBinding().rl1.setVisibility(View.VISIBLE);
                        getBinding().listViewSettings.setVisibility(View.VISIBLE);
                    }

                    if (!adRunning)
                        playContent();
                    booleanLiveData.removeObservers(baseActivity);
                    if (booleanLiveData.hasObservers()) return;
                    if (aBoolean != null && aBoolean) {
                        getBinding().pBar.setVisibility(View.GONE);
//                        getBinding().urlimage.setVisibility(View.GONE);
                        setVideoQuality();
                    }
                });
            }
        });

        viewModel.getSeekBarProgress(getBinding().seekBar).observe(this, s -> {
            if (runningPlayer != null) {
                runningPlayer.getView().setVisibility(View.VISIBLE);
                long durtion = runningPlayer.getDuration();
                long currentPos = runningPlayer.getCurrentPosition();
                if (isPlayerStart) {

                    if (currentPos > 10000) {
                        getBinding().backward.setVisibility(View.VISIBLE);
                    } else {
                        getBinding().backward.setVisibility(View.GONE);
                    }
                    long runningTime = durtion - currentPos;
                    if (runningTime > 10000) {
                        getBinding().forward.setVisibility(View.VISIBLE);
                    } else {
                        getBinding().forward.setVisibility(View.GONE);
                    }
                    if (adRunning) {
                        getBinding().listViewSettings.setVisibility(View.GONE);
                        getBinding().rl1.setVisibility(View.GONE);
                     //   getBinding().lockIcon.setVisibility(View.GONE);
                        getBinding().backward.setVisibility(View.GONE);
                        getBinding().forward.setVisibility(View.GONE);
                    }


                }

            }


            getBinding().currentTime.setText(s);
        });
        getBinding().ivCancel.setOnClickListener(view1 -> {
            exitPlayeriew(player);

        });


        viewModel.getPlayerState().observe(this, aBoolean -> {
            if (aBoolean != null && aBoolean) {
                if (isSeries && hasNextEpisode) {
                    if (hasPostRoll) {
                        isWaitingBinge = true;
                    } else {
                        setBingView(player);
                    }
                } else {
                    if (hasPostRoll) {
                        exitPlayer = true;
                    } else {
                        exitPlayeriew(player);
                    }
                }
            }
        });

    }

    private void hideSoftKeyButton() {
        View decorView = baseActivity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);

    }

    public void setBingView(Player player) {
        isBingeView = true;
        getBinding().pBar.setVisibility(View.GONE);
      //  getBinding().lockIcon.setVisibility(View.GONE);
        getBinding().rlUp.setVisibility(View.INVISIBLE);
        getBinding().rlDown.setVisibility(View.INVISIBLE);
        getBinding().linearAutoPlayLayout.setVisibility(View.VISIBLE);
        getAssetImage(asset);
        Animation aniFade = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        getBinding().autoplaylayout.assetImage.startAnimation(aniFade);
        getBinding().autoplaylayout.close.bringToFront();
        getBinding().autoplaylayout.close.setOnClickListener(v -> exitPlayeriew(player));
        getBinding().autoplaylayout.playNextEpisode.setOnClickListener(v ->
                playNextEpisode()
        );
        getBinding().autoplaylayout.close.bringToFront();
        if (!hasEpisodesList)
            getNextEpisode(asset);

        startTimer();

    }

    private void getAssetImage(Asset asset) {
        boolean ratioFound = false;
        for (int i = 0; i < asset.getImages().size(); i++) {
            if (asset.getImages().get(i).getRatio().equalsIgnoreCase("16:9")) {
                image_url = asset.getImages().get(0).getUrl();
                image_url = image_url + AppLevelConstants.WIDTH + (int) getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().autoplaylayout.assetImage, image_url, R.drawable.landscape);
                ratioFound = true;
            }
        }
        if (ratioFound) {
            ImageHelper.getInstance(getActivity()).loadImageTo(getBinding().autoplaylayout.assetImage, image_url, R.drawable.landscape);
        }
    }

    private void exitPlayeriew(Player player) {
        getBinding().lockImg.setVisibility(View.VISIBLE);
        getBinding().linearAutoPlayLayout.setVisibility(View.GONE);

        if (timer && timeHandler != null) {
            timeHandler.removeCallbacks(myRunnable);

        }
        PrintLogging.printLog(this.getClass(), "", "exitPlayeriew");

        if (runningPlayer != null) {
            PrintLogging.printLog(this.getClass(), "", "runningPlayer");
            runningPlayer.stop();
            runningPlayer.destroy();
        }
        if (player != null) {
            PrintLogging.printLog(this.getClass(), "", "Player");

            player.stop();
            player.destroy();
        }
        if (viewModel != null)
            viewModel.clearCallbacks();

        baseActivity.onBackPressed();
    }

    private void playBackError() {
        getBinding().playButton.setVisibility(View.GONE);
        getBinding().seekBar.setVisibility(View.GONE);
        getBinding().currentTime.setVisibility(View.GONE);
        getBinding().totalDuration.setVisibility(View.GONE);
        getBinding().fullscreen.setVisibility(View.GONE);
        getBinding().forward.setVisibility(View.GONE);
        getBinding().backward.setVisibility(View.GONE);
        getBinding().pBar.setVisibility(View.GONE);
        getBinding().playericon.setVisibility(View.GONE);
        getBinding().slash.setVisibility(View.GONE);
        getBinding().subtitleAudio.setVisibility(View.GONE);
        getBinding().quality.setVisibility(View.GONE);
        //  getBinding().ivQuality.setVisibility(View.GONE);
        getBinding().goLive.setVisibility(View.GONE);

        if (!DialogHelper.isIsDialog()) {
            if (getActivity() != null && !getActivity().isFinishing()) {
                if (NetworkConnectivity.isOnline(getActivity())) {
                    DialogHelper.playerAlertDialog(this.getActivity(), getString(R.string.play_back_error), getString(R.string.ok), new AlertDialogSingleButtonFragment.AlertDialogListener() {
                        @Override
                        public void onFinishDialog() {
                            DialogHelper.setIsDialog(false);
                            runningPlayer.stop();
                            runningPlayer.destroy();
                            getActivity().finish();
                        }
                    });
                }
            }
        }

    }

    private void setVideoQuality() {
        selectedTrack = KsPreferenceKey.getInstance(baseActivity).getQualityName();
        if (!TextUtils.isEmpty(selectedTrack)) {
            trackName = selectedTrack;
            Log.d("TrackNameIs", trackName);
            Log.d("TrackNameIs", selectedTrack);

            final LiveData<Boolean> booleanLiveData = viewModel.changeTrack(trackName);
        }
    }

    private void notPlayContentWithoutInternet() {
        getBinding().playButton.setVisibility(View.VISIBLE);
        getBinding().seekBar.setVisibility(View.GONE);
        getBinding().currentTime.setVisibility(View.GONE);
        getBinding().totalDuration.setVisibility(View.GONE);
        getBinding().fullscreen.setVisibility(View.GONE);
        getBinding().forward.setVisibility(View.GONE);
        getBinding().backward.setVisibility(View.GONE);
        getBinding().pBar.setVisibility(View.GONE);
        getBinding().playericon.setVisibility(View.GONE);
        getBinding().slash.setVisibility(View.GONE);
        getBinding().subtitleAudio.setVisibility(View.GONE);
        getBinding().quality.setVisibility(View.GONE);
        //   getBinding().ivQuality.setVisibility(View.GONE);
    }

    private void callHandler() {
        timer = true;
      //  getBinding().lockIcon.setVisibility(View.VISIBLE);
        myRunnable = this::ShowAndHideView;

        timeHandler = new Handler();
        timeHandler.postDelayed(myRunnable, 3000);
    }

    private void ShowAndHideView() {
        Log.d("DragValueIs", drag + "");



        if (drag)
            return;

        if (lockEnable)
            return;

        if (adRunning) {
         //   getBinding().lockIcon.setVisibility(View.GONE);
            return;
        }
        try {
            animationFadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            animationFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    getBinding().listViewSettings.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if (getBinding().rl1.getVisibility() == View.VISIBLE) {
                Log.d("HideView", "True");
                getBinding().rl1.startAnimation(animationFadeOut);
                getBinding().rl1.setVisibility(View.GONE);
                if (getBinding().videoDialog.getVisibility() == View.VISIBLE || getBinding().audioDialog.getVisibility() == View.VISIBLE){
                    getBinding().audioDialog.setVisibility(View.GONE);
                    getBinding().videoDialog.setVisibility(View.GONE);
                }
                getBinding().listViewSettings.setVisibility(View.GONE);
                timer = true;
                hideSoftKeyButton();
            } else {
                Log.d("HideView", "False");
                getBinding().rl1.setVisibility(View.VISIBLE);
                if (isSeries) {
                    if (getBinding().forward.getVisibility() == View.VISIBLE || getBinding().backward.getVisibility() == View.VISIBLE) {
                        getBinding().playButton.setVisibility(View.VISIBLE);
                        getBinding().rlDown.setVisibility(View.VISIBLE);
                        getBinding().ivCancel.setVisibility(View.VISIBLE);
                        getBinding().rlDown.setVisibility(View.VISIBLE);
                        getBinding().totalDuration.setVisibility(View.VISIBLE);
                        getBinding().seekBar.setVisibility(View.VISIBLE);
                        getBinding().currentTime.setVisibility(View.VISIBLE);
                        getBinding().listViewSettings.setVisibility(View.GONE);
                        getBinding().slash.setVisibility(View.VISIBLE);
                        getBinding().subtitleAudio.setVisibility(View.VISIBLE);
                        getBinding().quality.setVisibility(View.VISIBLE);
                    }
                }
                getBinding().rl1.startAnimation(animationFadeIn);

                callHandler();
            }
        } catch (Exception ex) {

        }
    }

    private void UIControllers() {

        getBinding().seekBar.setProgress(0);
        getBinding().seekBar.setMax(100);

        getBinding().seekBar.setOnSeekBarChangeListener(this);


//        getBinding().rl.setOnClickListener(view -> {
//            if (timer) {
//                timeHandler.removeCallbacks(myRunnable);
//            }
//            ShowAndHideView();
//
//        });


        getBinding().playericon.setOnClickListener(view -> {

            isPlayerIconClick = true;
            showAlertDialog(getResources().getString(R.string.purchase_dialouge), getResources().getString(R.string.login), getResources().getString(R.string.cancel));
        });

        getBinding().shareWith.setOnClickListener(view -> {
//
            if (SystemClock.elapsedRealtime() - lastClickTime < AppLevelConstants.SHARE_DIALOG_DELAY) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            openShareDialouge();
        });


        getBinding().lockIcon.setOnClickListener(view -> {
            if (lockEnable) {
                if (timeHandler != null && timer) {
                    timeHandler.removeCallbacks(myRunnable);
                }
                getBinding().lockImg.setImageResource(R.drawable.ic_lock_open);
                lockEnable = false;
                //   getBinding().ivQuality.setVisibility(View.VISIBLE);
                ShowAndHideView();

            } else {
                clearAndReset();
                if (timer && timeHandler != null) {
                    timeHandler.removeCallbacks(myRunnable);
                }
            }
        });

        getBinding().rl.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if (drag)
                    drag = false;
                try {
                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        getBinding().bottomSheetLayout.bottomSheet.setVisibility(View.INVISIBLE);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                } catch (Exception e) {

                }


                if (isPlayerStart) {
                    if (lockEnable) {
//                        if (getBinding().lockIcon.getVisibility() == View.VISIBLE) {
//                            getBinding().lockIcon.setVisibility(View.GONE);
//                        } else {
//                            getBinding().lockIcon.setVisibility(View.VISIBLE);
//                            if (adRunning || isBingeView)
//                                getBinding().lockIcon.setVisibility(View.GONE);
//                        }
                    } else {
                        if (timer) {
                            if (timeHandler != null)
                                timeHandler.removeCallbacks(myRunnable);
                        }
                        ShowAndHideView();
                    }

                }

            }

            @Override
            public void onDoubleClick(View view) {
                if (lockEnable) {
//                    if (getBinding().lockIcon.getVisibility() == View.VISIBLE) {
//                        getBinding().lockIcon.setVisibility(View.GONE);
//                    } else {
//                        getBinding().lockIcon.setVisibility(View.VISIBLE);
//                    }
                } else {
                    viewModel.changeVideoRatio();
                }

            }
        }));
        getBinding().rl1.setOnClickListener(new DoubleClick(new DoubleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (drag) {
                    if (isPlayerStart) {
                        if (lockEnable) {
//                            if (getBinding().lockIcon.getVisibility() == View.VISIBLE) {
//                                getBinding().lockIcon.setVisibility(View.GONE);
//                            } else {
//                                getBinding().lockIcon.setVisibility(View.VISIBLE);
//                                if (adRunning || isBingeView)
//                                    getBinding().lockIcon.setVisibility(View.GONE);
//                            }

                        } else {
                            if (timer) {

                                if (timeHandler != null)
                                    timeHandler.removeCallbacks(myRunnable);
                            }
                            ShowAndHideView();
                        }

                    }
                }

            }

            @Override
            public void onDoubleClick(View view) {
                viewModel.changeVideoRatio();
            }
        }));

        getBinding().rl1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    gestureDetector.onTouchEvent(motionEvent);

                } catch (Exception e) {

                }
                return false;
            }
        });

        getBinding().forward.setOnClickListener(view -> {
            getBinding().pBar.setVisibility(View.VISIBLE);
            final LiveData<Boolean> booleanLiveData = viewModel.seekPlayerForward();
            if (booleanLiveData == null || baseActivity == null) {
                return;
            }
            booleanLiveData.observe(baseActivity, aBoolean -> {

                booleanLiveData.removeObservers(baseActivity);
                if (booleanLiveData.hasObservers()) return;
                if (aBoolean != null && aBoolean) {
                    getBinding().pBar.setVisibility(View.GONE);
                }
                try {
                    getBinding().seekBar.setProgress(((int) runningPlayer.getCurrentPosition()));
                    getBinding().currentTime.setText(stringForTime(getBinding().seekBar.getProgress()));

                } catch (Exception w) {
                    PrintLogging.printLog("Exception", "", "" + w);
                }
            });
        });


        getBinding().goLive.setOnClickListener(view -> {
            getBinding().pBar.setVisibility(View.VISIBLE);
            final LiveData<Boolean> booleanLiveData = viewModel.seekToDuration();
            if (booleanLiveData == null || baseActivity == null) {
                return;
            }
            booleanLiveData.observe(baseActivity, aBoolean -> {

                booleanLiveData.removeObservers(baseActivity);
                if (booleanLiveData.hasObservers()) return;
                if (aBoolean != null && aBoolean) {
                    getBinding().pBar.setVisibility(View.GONE);
                }
                try {
                    getBinding().seekBar.setProgress(((int) runningPlayer.getCurrentPosition()));
                    getBinding().currentTime.setText(stringForTime(getBinding().seekBar.getProgress()));

                } catch (Exception w) {
                    PrintLogging.printLog("Exception", "", "" + w);
                }
            });
        });

        getBinding().backward.setOnClickListener(view -> {
            getBinding().pBar.setVisibility(View.VISIBLE);
            final LiveData<Boolean> booleanLiveData = viewModel.seekPlayerBackward();
            if (booleanLiveData == null || baseActivity == null) {
                return;
            }
            booleanLiveData.observe(baseActivity, aBoolean -> {
                booleanLiveData.removeObservers(baseActivity);
                if (booleanLiveData.hasObservers()) return;
                if (aBoolean != null && aBoolean) {
                    getBinding().pBar.setVisibility(View.GONE);
                }
                try {
                    getBinding().seekBar.setProgress(((int) runningPlayer.getCurrentPosition()));
                    getBinding().currentTime.setText(stringForTime(getBinding().seekBar.getProgress()));
                } catch (Exception e) {
                    PrintLogging.printLog("Exception", "", "" + e);
                }

            });
        });


        getBinding().playButton.setOnClickListener(view -> playPauseControl());


        // getBinding().ivQuality.setOnClickListener(view -> chooseQuality_Caption_Audio());



        getBinding().quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBinding().audioDialog.getVisibility() == View.VISIBLE){
                    getBinding().audioDialog.setVisibility(View.GONE);
                }
                chooseVideoquality();
            }
        });

        getBinding().subtitleAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getBinding().videoDialog.getVisibility() == View.VISIBLE){
                    getBinding().videoDialog.setVisibility(View.GONE);
                }
                chooseAudio();
            }
        });

        getBinding().fullscreen.setOnClickListener(view -> getBinding().ivCancel.performClick());

        getBinding().autoplaylayout.replayvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                getBinding().linearAutoPlayLayout.setVisibility(View.GONE);
                getUrl(playerURL, asset, playerProgress, isLivePlayer, "");
            }
        });


    }

    void clearAndReset() {
       // getBinding().lockIcon.setVisibility(View.GONE);
        getBinding().rl1.clearAnimation();
        getBinding().rl1.setVisibility(View.GONE);
        getBinding().listViewSettings.setVisibility(View.GONE);
        if (animationFadeIn != null)
            animationFadeIn.cancel();
        if (animationFadeOut != null)
            animationFadeOut.cancel();
        getBinding().lockImg.setImageResource(R.drawable.ic_lock);
        lockEnable = true;
    }

    void startTimer() {
        cTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                getBinding().autoplaylayout.upnext.setText("Next episode in " + (millisUntilFinished / 1000));
            }

            public void onFinish() {
                playNextEpisode();
            }
        };
        cTimer.start();
    }

    private void playNextEpisode() {
        isLiveChannel = false;
        cancelTimer();
        if (hasNextEpisode) {
            if (runningPlayer != null) {
              //  getBinding().lockIcon.setVisibility(View.VISIBLE);
                getBinding().rlUp.setVisibility(View.VISIBLE);
                getBinding().rlDown.setVisibility(View.VISIBLE);
                runningPlayer.getView().setVisibility(View.GONE);
                runningPlayer.stop();
                hidePlayerControls();
            }

            if (nextPlayingAsset != null) {
                asset = nextPlayingAsset;
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBingeView = false;
                        checkErrors(asset);
                    }
                }, 3000);
                playerChecks(asset);
            }
        }
    }

    void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    private void chooseQuality_Caption_Audio() {
        isDialogShowing = true;
        ArrayList<String> videoQualityArrayList = new ArrayList<>();
        videoQualityArrayList.add(getString(R.string.video_quality));
        if (isAudioTracks) {
            videoQualityArrayList.add(getString(R.string.audio));
        }
        if (isCaption) {
            videoQualityArrayList.add(getString(R.string.subtitle));
        }
        ArrayAdapter<String> arrayAdapter;

        arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_layout, R.id.tv_setting_text, videoQualityArrayList);
        getBinding().listViewSettings.setAdapter(arrayAdapter);
        getBinding().listViewSettings.setVisibility(View.VISIBLE);
        getBinding().listViewSettings.setOnItemClickListener((parent, view, position, id) -> {
            getBinding().listViewSettings.setVisibility(View.GONE);
            switch (position) {
                case 0:
                    chooseVideoquality();
                    break;
                case 1:
                    if (isAudioTracks) {
                        chooseAudio();
                    } else {
                        chooseCaption();
                    }
                    break;
                case 2:
                    chooseCaption();
                    break;


            }
        });
    }

    private void chooseQualityDialouge() {
        Dialog videodialog = new Dialog(baseActivity, android.R.style.Theme_Dialog);
        videodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        videodialog.setContentView(R.layout.video_quality);
        videodialog.setCanceledOnTouchOutside(true);

        videodialog.setCanceledOnTouchOutside(true);
        if (videodialog.getWindow() != null) {
            videodialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            videodialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            videodialog.show();
        }

    }

    private void addAudioToPlayer() {
        dialog.cancel();
        final RecyclerView recycleview;
        View modalbottomsheet1 = getLayoutInflater().inflate(R.layout.bottom_sheet_layout_audio, null);
        dialogAudio = new BottomSheetDialog(baseActivity);
        dialogAudio.setContentView(modalbottomsheet1);
        dialogAudio.show();
        recycleview = dialogAudio.findViewById(R.id.recycleview);
        if (recycleview != null) {
            recycleview.setLayoutManager(new LinearLayoutManager(baseActivity));
            viewModel.loadAudioWithPlayer().observe(this, audioTracks -> {
                if (audioTracks != null && audioTracks.size() > 0) {

                    viewModel.getAudioTrackItems().observe(baseActivity, trackItems -> {

                        AudioAdapter audioAdapter = new AudioAdapter(trackItems);
                        recycleview.setAdapter(audioAdapter);
                    });


                } else {
                    dialogAudio.cancel();
                    callHandler();
                    ToastHandler.show(baseActivity.getResources().getString(R.string.no_tracks_available), baseActivity.getApplicationContext());
                }

            });
        }

    }

    private void addCaptionToPlayer() {
        dialog.cancel();
        final RecyclerView recycleview;
        View modalbottomsheet1 = getLayoutInflater().inflate(R.layout.bottom_sheet_layout_caption, null);
        dialogCaption = new BottomSheetDialog(baseActivity);
        dialogCaption.setContentView(modalbottomsheet1);
        dialogCaption.show();
        recycleview = dialogCaption.findViewById(R.id.recycleview);
        if (recycleview != null) {
            recycleview.setLayoutManager(new LinearLayoutManager(baseActivity));
            viewModel.loadCaptionWithPlayer().observe(this, textTracks -> {
                if (textTracks != null && textTracks.size() > 0) {

                    viewModel.getTextTrackItems().observe(baseActivity, trackItems -> {
                        CaptionAdapter captionAdapter = new CaptionAdapter(trackItems);
                        recycleview.setAdapter(captionAdapter);
                    });


                } else {
                    dialogCaption.cancel();
//                    hideSoftKeyButton();
                    callHandler();
                    ToastHandler.show(baseActivity.getResources().getString(R.string.no_caption_available), baseActivity.getApplicationContext());
                }

            });
        }
    }

    public void haveSubtitleorNot() {
        viewModel.loadCaptionWithPlayer().observe(this, textTracks -> {
            if (textTracks != null && textTracks.size() > 0) {
                viewModel.getTextTrackItems().observe(baseActivity, trackItems -> {
                    if (trackItems != null && trackItems.length > 0) {
                        /*for (int i = 0; i < trackItems.length; i++) {
                            String trackLang = trackItems[i].getTrackName().trim();
                            String lang = new KsPreferenceKey(baseActivity).getSubtitleLanguage().trim();
                            if (trackLang.equalsIgnoreCase(lang)) {
                                viewModel.changeAudioTrack(trackItems[i].getUniqueId());
                                break;
                            }
                        }*/

                        isCaption = true;
                    } else {
                        isCaption = false;
                    }
                });
            } else {
                isCaption = false;
            }
        });
    }

    private void chooseCaption() {


    }

    private void chooseAudio() {

        if (getBinding().audioQuality.recycleviewAudio != null) {
            getBinding().audioQuality.recycleviewAudio.setLayoutManager(new LinearLayoutManager(baseActivity));
            getBinding().audioQuality.recycleviewAudio.setNestedScrollingEnabled(false);
            viewModel.loadAudioWithPlayer().observe(this, audioTracks -> {
                if (audioTracks != null && audioTracks.size() > 0) {
                    viewModel.getAudioTrackItems().observe(baseActivity, trackItems -> {
                        if (trackItems.length > 0) {
                            audioList = trackItems;
//                            for (int i = 0; i < trackItems.length; i++) {
//                                if (audioTrackName == "") {
//                                    if (trackItems[0] != null) {
//                                        audioTrackName = trackItems[0].getTrackName();
//                                        trackItems[0].setSelected(true);
//                                    }
//                                    break;
//                                } else {
//                                    String compareName = trackItems[i].getTrackName();
//                                    if (compareName == audioTrackName) {
//                                        trackItems[i].setSelected(true);
//                                        audioTrackName = trackItems[i].getTrackName();
//                                    } else {
//                                        trackItems[i].setSelected(false);
//                                    }
//                                }
//
//                            }
                            AudioAdapter audioAdapter = new AudioAdapter(trackItems);
                            getBinding().audioQuality.recycleviewAudio.setAdapter(audioAdapter);
                        }else {
                            getBinding().audioQuality.recycleviewAudio.setVisibility(View.GONE);
                            getBinding().audioQuality.titleAudio.setVisibility(View.GONE);
                        }

                    });


                } else {
                   // dialogAudio.cancel();
                    getBinding().audioQuality.recycleviewAudio.setVisibility(View.GONE);
                    getBinding().audioQuality.titleAudio.setVisibility(View.GONE);
                    //callHandler();
                   // ToastHandler.show(baseActivity.getResources().getString(R.string.no_tracks_available), baseActivity.getApplicationContext());
                }

            });
        }


        if (getBinding().audioQuality.recycleviewSubtitle != null) {
            getBinding().audioQuality.recycleviewSubtitle.setLayoutManager(new LinearLayoutManager(baseActivity));
            getBinding().audioQuality.recycleviewSubtitle.setNestedScrollingEnabled(false);
            viewModel.loadCaptionWithPlayer().observe(this, textTracks -> {
                if (textTracks != null && textTracks.size() > 0) {

                    viewModel.getTextTrackItems().observe(baseActivity, trackItems -> {
                        if (trackItems.length>0) {
                            captionList = trackItems;
//                        if (captionName == "") {
//                            captionList[0].setSelected(true);
//                            captionName = captionList[0].getTrackName();
//                        }
                            CaptionAdapter captionAdapter = new CaptionAdapter(trackItems);
                            getBinding().audioQuality.recycleviewSubtitle.setAdapter(captionAdapter);
                        }else {
                            getBinding().audioQuality.recycleviewSubtitle.setVisibility(View.GONE);
                            getBinding().audioQuality.titleSubtitle.setVisibility(View.GONE);
                        }
                    });


                } else {
                   // dialogQuality.cancel();
//                    hideSoftKeyButton();
                   // callHandler();
                    getBinding().audioQuality.recycleviewSubtitle.setVisibility(View.GONE);
                    getBinding().audioQuality.titleSubtitle.setVisibility(View.GONE);
                    //ToastHandler.show(baseActivity.getResources().getString(R.string.no_caption_available), baseActivity.getApplicationContext());
                }

            });
        }

        if (captionList!=null || audioList!=null){
            getBinding().audioDialog.setVisibility(View.VISIBLE);
            getBinding().audioDialog.bringToFront();
        }else {
            getBinding().audioDialog.setVisibility(View.GONE);
        }


    }

    private void chooseVideoquality() {

        final RecyclerView recycleview;
        callHandler();
       // dialogQuality = new Dialog(getBaseActivity(), R.style.AppAlertTheme);
      //  dialogQuality.setContentView(R.layout.layout_dialog_settings);
      //  dialogQuality.setTitle(getString(R.string.title_video_quality));
       // recycleview = dialogQuality.findViewById(R.id.recycleview);

//        Button closeButton = dialogQuality.findViewById(R.id.close);
//        closeButton.setOnClickListener(v -> dialogQuality.cancel());
        if (getBinding().videoQuality.recycleview != null) {
            getBinding().videoQuality.recycleview.setLayoutManager(new LinearLayoutManager(baseActivity));
            viewModel.loadVideotracksWithPlayer().observe(this, videoTracks -> {
                if (videoTracks != null && videoTracks.size() > 0) {
                    viewModel.getVideoTrackItems().observe(baseActivity, trackItems -> {
                        if (trackItems.size() > 0) {
                            getBinding().videoDialog.setVisibility(View.VISIBLE);
                            getBinding().videoDialog.bringToFront();
                            trackItemList = trackItems;

                            for (int i = 0; i < trackItemList.size(); i++) {
                                if (trackName.equalsIgnoreCase("")) {

                                } else {
                                    String compareName = trackItemList.get(i).getTrackName();
                                    if (trackName.equals(compareName)) {
                                       // trackItemList.get(i).setSelected(true);
                                        // break;
                                    } else {
                                       // trackItemList.get(i).setSelected(false);
                                    }
                                }
                            }
                            VideoTracksAdapter videoTracksAdapter = new VideoTracksAdapter(trackItems);
                            getBinding().videoQuality.recycleview.setAdapter(videoTracksAdapter);
                           // dialogQuality.show();

                        } else {
                            getBinding().videoDialog.setVisibility(View.GONE);
                            ToastHandler.show(baseActivity.getResources().getString(R.string.no_video_available), baseActivity.getApplicationContext());

                        }
                    });

                } else {
                    getBinding().videoDialog.setVisibility(View.GONE);
                    isDialogShowing = false;
                    hideSoftKeyButton();
                    ToastHandler.show(baseActivity.getResources().getString(R.string.no_tracks_available), baseActivity.getApplicationContext());
                }
            });
        } else {
            ToastHandler.show(baseActivity.getResources().getString(R.string.no_tracks_available), baseActivity.getApplicationContext());
        }
    }

    private void replayControl() {
        viewModel.replayControl();

        getBinding().forward.setVisibility(View.VISIBLE);
        getBinding().backward.setVisibility(View.VISIBLE);
        getBinding().seekBar.setVisibility(View.VISIBLE);
        getBinding().playButton.setVisibility(View.VISIBLE);
        getBinding().currentTime.setVisibility(View.VISIBLE);
        getBinding().totalDuration.setVisibility(View.VISIBLE);
        getBinding().fullscreen.setVisibility(View.GONE);

    }

    private void playPauseControl() {

        viewModel.playPauseControl().observe(this, aBoolean -> {
            if (aBoolean != null && aBoolean) {

                if (KsPreferenceKey.getInstance(getActivity()).getCatchupValue() || dvrEnabled) {
                    getBinding().playCatchup.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
                } else {

                    getBinding().playButton.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
                }
            } else {
                if (NetworkConnectivity.isOnline(baseActivity)) {
                    if (KsPreferenceKey.getInstance(getActivity()).getCatchupValue() || dvrEnabled) {
                        getBinding().playCatchup.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_play));
                    } else {
                        getBinding().playButton.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_play));
                    }
//                        getBinding().playButton.setBackgroundResource(R.drawable.play);
                } else {

                    showDialog(getResources().getString(R.string.no_internet_connection));
//                        showNoInternetDialog();
                }

            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekbar, int progress,
                                  boolean fromTouch) {

        viewModel.sendSeekBarProgress(seekbar.getProgress()).observe(this, s -> getBinding().currentTime.setText(s));

    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
        viewModel.removeCallBack();
        ConvivaManager.convivaPlayerSeekStartedReportRequest(baseActivity);
        if (timer) {
            timeHandler.removeCallbacks(myRunnable);
        }

        final LiveData<Boolean> booleanLiveData = viewModel.getPlayerProgress();
        booleanLiveData.observe(baseActivity, aBoolean -> {
            booleanLiveData.removeObservers(baseActivity);
            if (booleanLiveData.hasObservers()) return;
            if (aBoolean != null && aBoolean) {
                getBinding().pBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        ConvivaManager.convivaPlayerSeekStoppedReportRequest(baseActivity);
        getBinding().pBar.setVisibility(View.VISIBLE);
        viewModel.getPlayerView(seekBar);
        callHandler();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("PlayerPauseCalled", "OnResumeTrue");
        if (getBaseActivity() != null) {
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            getBaseActivity().registerReceiver(networkReceiver, intentFilter);

            IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
            getBaseActivity().registerReceiver(headsetRecicer, receiverFilter);


            Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
            setConnectivityListener(this);

            IntentFilter filter = new IntentFilter();
            filter.addAction("com.dialog.dialoggo.Alarm.MyReceiver");
            getActivity().registerReceiver(myReceiver, filter);

        }
        resumePlayer();
    }

    private void resumePlayer() {
        Log.d("PlayerPauseCalled", "OnResumeTrue");
        if (viewModel != null) {
            if (isPlayerStart) {
                if (NetworkConnectivity.isOnline(baseActivity)) {
                    viewModel.getResumeState().observe(this, aBoolean -> {
                        if (aBoolean != null && aBoolean) {
                            if (runningPlayer != null) {
                                //  runningPlayer.play();
                                hidePlayerWigetOnResume();
                                if (KsPreferenceKey.getInstance(getActivity()).getCatchupValue() || dvrEnabled) {
                                    //  runningPlayer.play();
                                    getBinding().playCatchup.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
                                } else {
                                    //   runningPlayer.play();
                                    getBinding().playButton.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
                                }
                            }


                        }
                    });
                }

            } else {
                Log.d("PlayerPauseCalled", "else");
                getUrl(playerURL, playerAsset, playerProgress, isLivePlayer, "");
                if (adRunning) {
                    getBinding().rl1.setVisibility(View.GONE);
                    getBinding().listViewSettings.setVisibility(View.GONE);
                }
            }
        }

    }

    public void setConnectivityListener(NetworkChangeReceiver.ConnectivityReceiverListener listener) {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }

    private void hidePlayerWigetOnResume() {
        getBinding().rl1.setVisibility(View.GONE);
        getBinding().listViewSettings.setVisibility(View.GONE);
        getBinding().pBar.setVisibility(View.VISIBLE);
        getBinding().loading.setVisibility(View.VISIBLE);
     //   getBinding().lockIcon.setVisibility(View.GONE);

        Log.e("hidePlayerWigetOnResume", "hidePlayerWigetOnResume");

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getBinding().pBar.setVisibility(View.GONE);
              //  getBinding().lockIcon.setVisibility(View.VISIBLE);
                getBinding().loading.setVisibility(View.GONE);

                if (lockEnable) {
                    getBinding().rl1.setVisibility(View.GONE);
                    getBinding().listViewSettings.setVisibility(View.GONE);
                } else {
                    getBinding().rl1.setVisibility(View.VISIBLE);
                    getBinding().listViewSettings.setVisibility(View.VISIBLE);
                }
            }
        }, 4000);


    }

    private void checkSeasonAndEpisodeNumber(Map<String, Value> metas) {
        seasonNumber = AssetContent.getSpecificSeason(metas);
        episodeNumber = AssetContent.getSpecificEpisode(metas);
        Log.d("EpisodeNumberIs", episodeNumber + "");
        Log.d("SeasonNumberIs", seasonNumber + "");
    }

    private void setTouchFalse() {
        getBinding().rl1.setOnTouchListener((v, event) -> false);
        getBinding().rl.setOnTouchListener((v, event) -> false);
        getBinding().rl1.setClickable(false);
        getBinding().rl.setClickable(false);
        getBinding().rl1.setFocusable(false);
        getBinding().rl.setFocusable(false);
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d("PlayerPauseCalled", "Pause");

        if (getActivity() != null) {
            PowerManager powerManager = (PowerManager) getActivity().getSystemService(POWER_SERVICE);
            boolean isScreenOn = powerManager.isScreenOn();
            if (runningPlayer != null) {
                runningPlayer.pause();
                runningPlayer.onApplicationPaused();
            }
//            if (android.os.Build.VERSION.SDK_INT >= 27) {
//                this.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            }
        }
        if (receiver != null) {
            Objects.requireNonNull(getActivity()).unregisterReceiver(receiver);
            NetworkChangeReceiver.connectivityReceiverListener = null;
        }
        this.baseActivity.unregisterReceiver(networkReceiver);
        this.baseActivity.unregisterReceiver(headsetRecicer);
    }


    private void pausePlayer() {
        Log.d("PlayerPauseCalled", "Enter");
        isPause = true;
        if (viewModel != null) {
            if (isPlayerStart) {
                viewModel.getPausestate().observe(this, aBoolean -> {
                    if (aBoolean != null && aBoolean) {

                        if (KsPreferenceKey.getInstance(getActivity()).getCatchupValue() || dvrEnabled) {
                            Log.d("PlayerPauseCalled", "Pause1");
                            getBinding().playCatchup.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.play));
                        } else {
                            Log.d("PlayerPauseCalled", "Pause2");
                            getBinding().playButton.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.play));
                        }
                        try {
                            if (isLivePlayer) {
                                if (runningPlayer != null) {
                                    runningPlayer.play();
                                    if (KsPreferenceKey.getInstance(getActivity()).getCatchupValue() || dvrEnabled) {
                                        Log.d("PlayerPauseCalled", "Pause3");
                                        getBinding().playCatchup.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));

                                    }
                                }
                            }
                        } catch (Exception e) {

                        }

                    }
                });

            } else {

                PlayerRepository.getInstance().releasePlayer();
            }

        }
    }


    @Override
    public void onCallStateRinging() {
        pausePlayer();

    }

    @Override
    public void onCallStateIdle() {
        //   resumePlayer();
    }

    @Override
    public void windowFocusChange(boolean hasFocus) {
        if (hasFocus) {
            if (viewModel != null) {
                viewModel.getPlayerObject().observe(DTPlayer.this, player -> {
                    if (player != null) {
                        if (!player.isPlaying() && isPause) {
                            isPause = false;
                            if (player != null)
                                player.play();
                            if (KsPreferenceKey.getInstance(getActivity()).getCatchupValue() || dvrEnabled) {
                                getBinding().playCatchup.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
                            } else {

                                getBinding().playButton.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
                            }


                        }
                    }
                });
            }
        } else {
            if (!isDialogShowing) {
                pausePlayer();
            }
        }
    }

    private void pausePlayerOnInternetGone(final Player player) {
        new Handler().postDelayed(() -> {
            if (player != null) {
                player.pause();
                notPlayContentWithoutInternet();
            }
        }, 10000);

    }

    private void playContentOnReconnect() {
        if (assetType == MediaTypeConstant.getLinear(baseActivity)) {

            getBinding().playericon.setVisibility(View.GONE);
            getBinding().pBar.setVisibility(View.GONE);
            getBinding().rl1.setVisibility(View.VISIBLE);
            getBinding().playButton.setVisibility(View.VISIBLE);
            getBinding().playButton.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
//            getBinding().playButton.setBackgroundResource(R.drawable.play);
            getBinding().seekBar.setVisibility(View.VISIBLE);
            getBinding().currentTime.setVisibility(View.VISIBLE);
            getBinding().totalDuration.setVisibility(View.VISIBLE);
            getBinding().fullscreen.setVisibility(View.GONE);
            getBinding().forward.setVisibility(View.GONE);
            getBinding().backward.setVisibility(View.GONE);
            getBinding().slash.setVisibility(View.VISIBLE);
            getBinding().subtitleAudio.setVisibility(View.GONE);
            getBinding().quality.setVisibility(View.GONE);
            //  getBinding().ivQuality.setVisibility(View.VISIBLE);


        } else {
            getBinding().playericon.setVisibility(View.GONE);
            getBinding().pBar.setVisibility(View.GONE);
            getBinding().rl1.setVisibility(View.VISIBLE);
            getBinding().playButton.setVisibility(View.VISIBLE);
            getBinding().playButton.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
//            getBinding().playButton.setBackgroundResource(R.drawable.play);
            getBinding().seekBar.setVisibility(View.VISIBLE);
            getBinding().currentTime.setVisibility(View.VISIBLE);
            getBinding().totalDuration.setVisibility(View.VISIBLE);
            getBinding().fullscreen.setVisibility(View.GONE);
            getBinding().forward.setVisibility(View.VISIBLE);
            getBinding().backward.setVisibility(View.VISIBLE);
            getBinding().slash.setVisibility(View.VISIBLE);
            getBinding().subtitleAudio.setVisibility(View.VISIBLE);
            getBinding().quality.setVisibility(View.VISIBLE);
            //  getBinding().ivQuality.setVisibility(View.VISIBLE);

        }
        //  getBinding().ivQuality.setVisibility(View.VISIBLE);


    }

    private String stringForTime(long timeMs) {
        StringBuilder formatBuilder = new StringBuilder();
        Formatter formatter = new Formatter(formatBuilder, Locale.getDefault());

        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return hours > 0 ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
                : formatter.format("%02d:%02d", minutes, seconds).toString();
    }

    private void showAlertDialog(String msg, String positiveButtonText, String negativeButtonText) {
        FragmentManager fm = getFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance(getResources().getString(R.string.dialog), msg, positiveButtonText, negativeButtonText);
        alertDialog.setAlertDialogCallBack(this);
        if (fm != null)
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void showDialog(String message) {
        FragmentManager fm = getFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        if (fm != null)
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    @Override
    public void onFinishDialog() {

        if (isPlayerIconClick) {
            isPlayerIconClick = false;
            new ActivityLauncher(baseActivity).loginActivity(baseActivity, LoginActivity.class, 0, "");
        } else if (isError) {
            isError = false;
            getActivity().onBackPressed();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (baseActivity != null) {
            baseActivity = null;
        }
        if (cTimer != null) {
            cTimer.cancel();
        }
        KsPreferenceKey.getInstance(getActivity()).setCatchupValue(false);
        PlayerRepository.getInstance().removeCallBacks();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.quality:
                chooseVideoquality();
                break;
            case R.id.caption:
                addCaptionToPlayer();
                break;
            case R.id.audio: {
                addAudioToPlayer();
                callHandler();
            }
            break;
            case R.id.cancel:
                dialog.cancel();
                isDialogShowing = false;
                break;
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (PlayerRepository.getInstance() != null) {
                PlayerRepository.getInstance().destroCallBacks();
            }
            ConvivaManager.convivaPlayerStoppedReportRequest();
            ConvivaManager.getConvivaVideoAnalytics(baseActivity).reportPlaybackEnded();
            ConvivaManager.removeConvivaSession();
            if (powerManager != null) {
                if (wakeLock != null) {
                    wakeLock.release();
                }
                TelephonyManager mgr = (TelephonyManager) baseActivity.getSystemService(TELEPHONY_SERVICE);
                if (mgr != null) {
                    mgr.listen(PhoneStateListenerHelper.getInstance(this), PhoneStateListener.LISTEN_NONE);
                }
            }
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
        if (mAudioManager != null)
            mAudioManager.abandonAudioFocus(this);
    }

    @Override
    public void catchupCallback(String url, RailCommonData commonData, String programName) {

        if (currentProgramId.equalsIgnoreCase(commonData.getObject().getId().toString())) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            if (drag)
                drag = false;
        } else {
            getBinding().bottomSheetLayout.bottomSheet.setVisibility(View.INVISIBLE);

            //  bottomSheetBehavior.setPeekHeight(0);
            //  getBinding().bottomSheetLayout.bottomSheet.setVisibility(View.GONE);
            if (runningPlayer != null) {
                if (runningPlayer.isPlaying()) {
                    runningPlayer.pause();
                }
            }
            // bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            getBinding().pBar.setVisibility(View.VISIBLE);
            checkcurrentProgramIsLive(commonData.getObject());
            getBinding().playCatchup.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.ic_pause));
            if (drag)
                drag = false;
        }


        // getUrl(playerURL,commonData.getObject(),0,false,programName);
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            getActivity().unregisterReceiver(myReceiver);
        } catch (IllegalArgumentException e) {

        } catch (Exception e) {

        }
    }

    private void splitStartTime(String startTime) {

        StringTokenizer tokens = new StringTokenizer(startTime, " ");
        String date = tokens.nextToken();// this will contain "Fruit"
        time = tokens.nextToken();

        splitMinute(time);

    }

    private void splitMinute(String time) {
        StringTokenizer tokens = new StringTokenizer(time, ":");
        hour = tokens.nextToken();
        minute = tokens.nextToken();

        PrintLogging.printLog("", "hoursandMinuteIs" + hour + minute);
    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {

        RelativeLayout tracksQuality;
        ImageView tick,imgQuality;
        private TextView qualityText, description;
        private AppCompatRadioButton playbackQualityRadio;
        private Button closeButton;
        private RelativeLayout layout;

        private ViewHolder1(View itemView) {
            super(itemView);
            // tracksQuality = itemView.findViewById(R.id.tracksQuality);
            //   playbackQualityRadio = itemView.findViewById(R.id.playbackQualityRadio);
            qualityText = itemView.findViewById(R.id.quality_text);
            description = itemView.findViewById(R.id.description);
            tick = itemView.findViewById(R.id.tick);
            imgQuality = itemView.findViewById(R.id.img);
            layout = itemView.findViewById(R.id.video_quality__layout);
        }
    }

    static class ViewHolder3 extends RecyclerView.ViewHolder {

        final TextView playbackCaption;
        final RelativeLayout tracksCaption;
        final ImageView tick;


        private ViewHolder3(View itemView) {
            super(itemView);
            playbackCaption = itemView.findViewById(R.id.playbackCaption);
            tracksCaption = itemView.findViewById(R.id.tracksCaption);
            tick = itemView.findViewById(R.id.tick);

        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {

        final TextView audioTracks;
        final RelativeLayout audio;
        final ImageView tick;


        private ViewHolder2(View itemView) {
            super(itemView);
            audioTracks = itemView.findViewById(R.id.audioTracks);
            audio = itemView.findViewById(R.id.audio);
            tick = itemView.findViewById(R.id.tick);

        }
    }

    class VideoTracksAdapter extends RecyclerView.Adapter<ViewHolder1> {
        final ArrayList<TrackItem> tracks;
        private int selectedIndex = -1;

        private VideoTracksAdapter(ArrayList<TrackItem> videoTracks) {
            this.tracks = videoTracks;

        }

        @NonNull
        @Override
        public ViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playback_quality, parent, false);
            return new ViewHolder1(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder1 holder, final int position) {
            if (trackItemList.get(position).isSelected()) {
               // holder.tick.setBackgroundResource(R.drawable.tick);
                holder.layout.setBackgroundColor(getResources().getColor(R.color.grape_purple));
            } else {
               // holder.tick.setBackgroundResource(0);
                holder.layout.setBackgroundColor(getResources().getColor(R.color.transparentColor));
                //viewHolder.notificationItemBinding.titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            holder.qualityText.setText(tracks.get(position).getTrackName());

             if(tracks.get(position).getTrackName().equalsIgnoreCase(AppLevelConstants.LOW)){
                holder.imgQuality.setBackgroundResource(R.drawable.ic_low_quality);

            }else if(tracks.get(position).getTrackName().equalsIgnoreCase(AppLevelConstants.MEDIUM)){
                holder.imgQuality.setBackgroundResource(R.drawable.ic_medium_quality);

            }else if(tracks.get(position).getTrackName().equalsIgnoreCase(AppLevelConstants.HIGH)){
                holder.imgQuality.setBackgroundResource(R.drawable.ic_video_quality);

            }

//            holder.description.setText(tracks.get(position).getTrackDescription());
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = trackItemList.indexOf(new TrackItem("", "", true));
                    getBinding().quality.setText(trackItemList.get(position).getTrackName());
                    if (trackItemList.get(position).getTrackName().equalsIgnoreCase(AppLevelConstants.LOW)){
                        getBinding().quality.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_low_quality,0,0,0);
                    }else  if (trackItemList.get(position).getTrackName().equalsIgnoreCase(AppLevelConstants.MEDIUM)){
                        getBinding().quality.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_medium_quality,0,0,0);
                    }else  if (trackItemList.get(position).getTrackName().equalsIgnoreCase(AppLevelConstants.HIGH)){
                        getBinding().quality.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_video_quality,0,0,0);
                    }

                    if (index == -1) {

                        trackItemList.get(position).setSelected(true);
                        trackName = trackItemList.get(position).getTrackName();
                        getBinding().videoDialog.setVisibility(View.GONE);
                        hideSoftKeyButton();
                        getBinding().pBar.setVisibility(View.VISIBLE);
                        isDialogShowing = false;
                        final LiveData<Boolean> booleanLiveData = viewModel.changeTrack(trackItemList.get(position).getTrackName());
                        booleanLiveData.removeObservers(getActivity());
                        booleanLiveData.observe(getActivity(), aBoolean -> {
                            if (aBoolean != null && aBoolean) {
                                new Handler().postDelayed(() -> getBinding().pBar.setVisibility(View.GONE), 1000);
                            }
                        });
                    } else {
                        if (trackItemList.get(position).getTrackName() == trackName) {
                            getBinding().videoDialog.setVisibility(View.GONE);
                            hideSoftKeyButton();
                        } else {
                            trackItemList.get(index).setSelected(false);
                            trackItemList.get(position).setSelected(true);
                            trackName = trackItemList.get(position).getTrackName();

                            getBinding().videoDialog.setVisibility(View.GONE);
                            hideSoftKeyButton();
                            getBinding().pBar.setVisibility(View.VISIBLE);
                            isDialogShowing = false;
                            final LiveData<Boolean> booleanLiveData = viewModel.changeTrack(trackItemList.get(position).getTrackName());
                            booleanLiveData.removeObservers(getActivity());
                            booleanLiveData.observe(getActivity(), aBoolean -> {
                                if (aBoolean != null && aBoolean) {
                                    new Handler().postDelayed(() -> getBinding().pBar.setVisibility(View.GONE), 1000);
                                }
                            });
                        }


                    }
                    notifyDataSetChanged();

                }
            });


        }

        @Override
        public int getItemCount() {
            return trackItemList.size();
        }
    }

    class CaptionAdapter extends RecyclerView.Adapter<ViewHolder3> {
        final TrackItem[] tracks;
        int finalInde;

        private CaptionAdapter(TrackItem[] videoTracks) {
            this.tracks = videoTracks;

        }

        @NonNull
        @Override
        public ViewHolder3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playback_caption, parent, false);

            return new ViewHolder3(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder3 holder, final int position) {
            int index = 0;

            if (captionList[position].isSelected()) {
                index = position;
              //  holder.playbackCaption.setTextColor(getResources().getColor(R.color.green));
              //  holder.tick.setBackgroundResource(R.drawable.tick);
            } else {
               // holder.playbackCaption.setTextColor(getResources().getColor(R.color.heather));
               // holder.tick.setBackgroundResource(0);
                //viewHolder.notificationItemBinding.titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            holder.playbackCaption.setText(captionList[position].getTrackName());

            finalInde = index;
            holder.tracksCaption.setOnClickListener(view -> {
                captionList[finalInde].setSelected(false);
                captionList[position].setSelected(true);
                captionName = captionList[position].getTrackName();
                viewModel.changeTextTrack(captionList[position].getUniqueId());
               // getBinding().audioDialog.setVisibility(View.GONE);
               // dialogQuality.cancel();
                hideSoftKeyButton();

                notifyDataSetChanged();
            });
            if (captionList[position].getTrackName().equalsIgnoreCase(captionName)){
                holder.playbackCaption.setTextColor(getResources().getColor(R.color.green));
            }else {
                holder.playbackCaption.setTextColor(getResources().getColor(R.color.heather));
            }


        }

        @Override
        public int getItemCount() {
            return tracks.length;
        }
    }

    class AudioAdapter extends RecyclerView.Adapter<ViewHolder2> {
        final TrackItem[] tracks;

        private AudioAdapter(TrackItem[] audioTracks) {
            this.tracks = audioTracks;

        }

        @NonNull
        @Override
        public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playback_audio, parent, false);

            return new ViewHolder2(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder2 holder, final int position) {
            int index = 0;
            if (tracks[position] != null) {
                holder.audioTracks.setText(tracks[position].getTrackName());

                if (tracks[position].isSelected()) {
                    index = position;
                   // holder.tick.setBackgroundResource(R.drawable.tick);
                } else {
                  //  holder.tick.setBackgroundResource(0);
                    //viewHolder.notificationItemBinding.titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
                int finalIndex = index;
                holder.audio.setOnClickListener(view -> {
                    tracks[finalIndex].setSelected(false);
                    tracks[position].setSelected(true);
                    audioTrackName = tracks[position].getTrackName();
                    //   holder.tick.setVisibility(View.VISIBLE);
                    viewModel.changeAudioTrack(tracks[position].getUniqueId());
                  //  dialogQuality.cancel();
                    hideSoftKeyButton();
                    notifyDataSetChanged();


                });
                if (tracks[position].getTrackName().equalsIgnoreCase(audioTrackName)){
                    holder.audioTracks.setTextColor(getResources().getColor(R.color.green));
                }else {
                    holder.audioTracks.setTextColor(getResources().getColor(R.color.heather));
                }
            }


        }

        private String changeLanguage(String language) {

            Locale streamLang = new Locale(language);
            Locale locale = new Locale("en");
            //streamLang.getISO3Language();

            return streamLang.getDisplayLanguage(locale);
        }

        @Override
        public int getItemCount() {
            return tracks.length;
        }
    }

}