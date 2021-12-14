package com.astro.sott.activities.liveEvent;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.astro.sott.activities.liveChannel.ui.LiveChannel;
import com.astro.sott.Alarm.MyReceiver;
import com.astro.sott.activities.liveEvent.reminderDialog.ReminderDialogFragment;
import com.astro.sott.activities.loginActivity.ui.AstrLoginActivity;
import com.astro.sott.activities.movieDescription.viewModel.MovieDescriptionViewModel;
import com.astro.sott.activities.signUp.ui.SignUpActivity;
import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.activities.subscriptionActivity.ui.SubscriptionDetailActivity;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.callBacks.kalturaCallBacks.ProductPriceStatusCallBack;
import com.astro.sott.databinding.ActivityLiveEventBinding;
import com.astro.sott.fragments.dialog.PlaylistDialogFragment;
import com.astro.sott.fragments.episodeFrament.EpisodeDialogFragment;
import com.astro.sott.player.entitlementCheckManager.EntitlementCheck;
import com.astro.sott.thirdParty.CleverTapManager.CleverTapManager;
import com.astro.sott.thirdParty.conViva.ConvivaManager;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.PacksDateLayer;
import com.astro.sott.utils.billing.BuyButtonManager;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.SubMediaTypes;
import com.astro.sott.utils.userInfo.UserInfo;
import com.conviva.sdk.ConvivaSdkConstants;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.astro.sott.R;
import com.astro.sott.activities.parentalControl.viewmodels.ParentalControlViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.baseModel.RailBaseFragment;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.ParentalDialogCallbacks;
import com.astro.sott.callBacks.commonCallBacks.PlaylistCallback;
import com.astro.sott.databinding.MovieScreenBinding;
import com.astro.sott.fragments.detailRailFragment.DetailRailFragment;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.modelClasses.dmsResponse.ParentalLevels;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.networking.refreshToken.RefreshKS;
import com.astro.sott.player.geoBlockingManager.GeoBlockingCheck;
import com.astro.sott.player.houseHoldCheckManager.HouseHoldCheck;
import com.astro.sott.player.ui.PlayerActivity;
import com.astro.sott.repositories.player.PlayerRepository;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.CommonPlaylistDialog;
import com.astro.sott.utils.helpers.DialogHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.StringBuilderHolder;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.gson.Gson;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.LongValue;
import com.kaltura.client.types.MultilingualStringValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.PersonalList;
import com.kaltura.client.types.StringValue;
import com.kaltura.client.types.UserAssetRule;
import com.kaltura.client.types.Value;
import com.kaltura.client.utils.response.base.Response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;


public class LiveEventActivity extends BaseBindingActivity<ActivityLiveEventBinding> implements DetailRailClick, AlertDialogSingleButtonFragment.AlertDialogListener, ReminderDialogFragment.EditDialogListener {
    ArrayList<ParentalLevels> parentalLevels;
    private RailCommonData railData;
    private Asset asset;
    private String vodType;
    private String[] subscriptionIds;
    private int layoutType, playlistId = 1;
    private DoubleValue doubleValue;
    private boolean xofferWindowValue = false, playbackControlValue = false;
    private MovieDescriptionViewModel viewModel;
    private Map<String, MultilingualStringValueArray> map;
    private Map<String, Value> yearMap;
    private FragmentManager manager;
    private long assetId;
    private int assetType;
    private String poster_image_url;
    private List<PersonalList> playlist = new ArrayList<>();
    private List<PersonalList> personalLists = new ArrayList<>();
    private RailBaseFragment baseRailFragment;
    private String trailor_url = "";
    private boolean becomeVipButtonCLicked = false;

    private List<Integer> list;
    private String name, titleName, idfromAssetWatchlist;
    private boolean isActive, isAdded;
    private boolean iconClicked = false;
    private String image_url = "";
    private long lastClickTime = 0;
    private int errorCode = -1;
    private boolean playerChecksCompleted = false;
    private int assetRuleErrorCode = -1;
    private boolean isParentalLocked = false;
    private String defaultParentalRating = "";
    private String userSelectedParentalRating = "";
    private int userSelectedParentalPriority;
    private int priorityLevel;
    private int assetRestrictionLevel;
    private boolean assetKey = false;
    private boolean isDtvAdded = false;
    private String time = "", month = "", dd = "", year = "", hour = "", minute = "";
    private MyReceiver myReceiver;

    @Override
    public ActivityLiveEventBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityLiveEventBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentalLevels = new ArrayList<>();
        myReceiver = new MyReceiver();
        connectionObserver();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getMediaType(asset, railData);
    }

    private void intentValues() {
        layoutType = getIntent().getIntExtra(AppLevelConstants.LAYOUT_TYPE, 0);
        if (getIntent().getExtras() != null) {
            railData = getIntent().getExtras().getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
            if (railData != null) {
                Gson gson = new Gson();
                asset = railData.getObject();
                getDataFromBack(railData, layoutType);
            }
        }
    }

    private void getDataFromBack(RailCommonData commonRailData, int layout) {
        getBinding().playButton.setVisibility(View.GONE);
        railData = commonRailData;
        asset = railData.getObject();
        FirebaseEventManager.getFirebaseInstance(this).trackScreenName(asset.getName());
        FirebaseEventManager.getFirebaseInstance(LiveEventActivity.this).setRelatedAssetName(asset.getName());

        layoutType = layout;
        assetId = asset.getId();
        name = asset.getName();
        titleName = name;
        isActive = UserInfo.getInstance(this).isActive();
        map = asset.getTags();

        setPlayerFragment();
        getMediaType(asset, railData);
        checkReminder();

    }

    private void initReminderPopupFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ReminderDialogFragment cancelDialogFragment = ReminderDialogFragment.newInstance("Reminder", fileId);
        cancelDialogFragment.setEditDialogCallBack(LiveEventActivity.this);
        cancelDialogFragment.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void checkReminder() {
        boolean isReminderAdded = new KsPreferenceKey(getApplicationContext()).getReminderId(asset.getId().toString());
        if (isReminderAdded == true) {
            String currentTime = AppCommonMethods.getCurrentTimeStamp();
            String startTime = AppCommonMethods.getProgramStartTime(liveEventStartDate);
            Log.w("reminderDetails", currentTime + " " + startTime + " ");
            if (Long.valueOf(startTime) > Long.valueOf(currentTime)) {
                getBinding().reminderActive.setVisibility(View.VISIBLE);
                getBinding().reminder.setVisibility(View.GONE);
            } else {
                getBinding().reminderActive.setVisibility(View.GONE);
                getBinding().reminder.setVisibility(View.VISIBLE);
                try {
                    new KsPreferenceKey(LiveEventActivity.this).setReminderId(asset.getId().toString(), false);
                } catch (Exception ignored) {

                }

            }

        } else {
            getBinding().reminderActive.setVisibility(View.GONE);
            // getBinding().reminder.setVisibility(View.VISIBLE);
            try {
                new KsPreferenceKey(LiveEventActivity.this).setReminderId(asset.getId().toString(), false);
            } catch (Exception ignored) {

            }
        }
    }


    private void showAlertDialog(String msg) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance("", msg, getString(R.string.ok));
        alertDialog.setAlertDialogCallBack(alertDialog::dismiss);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    private void setPlayerFragment() {
        getBinding().backImg.setOnClickListener(view -> onBackPressed());

        manager = getSupportFragmentManager();
        getBinding().playButton.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            if (vodType.equalsIgnoreCase(EntitlementCheck.FREE)) {
                if (isPlayableOrNot()) {
                    FirebaseEventManager.getFirebaseInstance(this).liveButtonEvent(FirebaseEventManager.WATCH, asset, this, "");
                    playerChecks(railData);
                } else {
                    ToastHandler.display(getResources().getString(R.string.live_event_msg), this);
                }
            } else if (vodType.equalsIgnoreCase(EntitlementCheck.SVOD)) {
                if (UserInfo.getInstance(this).isActive()) {
                    FirebaseEventManager.getFirebaseInstance(this).liveButtonEvent(FirebaseEventManager.TRX_VIP, asset, this, "");

                    if (isPlayableOrNot()) {
                        fileId = AppCommonMethods.getFileIdOfAssest(railData.getObject());
                    } else {
                        fileId = AssetContent.getLiveEventPackageId(railData.getObject().getTags());
                    }
                    if (!fileId.equalsIgnoreCase("") && subscriptionIds != null) {
                        Intent intent = new Intent(this, SubscriptionDetailActivity.class);
                        intent.putExtra(AppLevelConstants.PLAYABLE, isPlayableOrNot());
                        intent.putExtra(AppLevelConstants.POSTER_IMAGE_URL, poster_image_url);
                        intent.putExtra(AppLevelConstants.FILE_ID_KEY, fileId);
                        intent.putExtra(AppLevelConstants.DATE, liveEventDate);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY, subscriptionIds);
                        intent.putExtra("SubscriptionIdBundle", bundle);
                        intent.putExtra(AppLevelConstants.FROM_KEY, "Live Event");
                        startActivity(intent);
                    }
                } else {
                    becomeVipButtonCLicked = true;
                    new ActivityLauncher(LiveEventActivity.this).signupActivity(LiveEventActivity.this, SignUpActivity.class, CleverTapManager.DETAIL_PAGE_BECOME_VIP);
                }

            } else if (vodType.equalsIgnoreCase(EntitlementCheck.TVOD)) {
                if (UserInfo.getInstance(this).isActive()) {
                    try {
                        FirebaseEventManager.getFirebaseInstance(this).liveButtonEvent(FirebaseEventManager.TRX_VIP, asset, this, "");
                    } catch (Exception e) {
                    }
                    if (isPlayableOrNot()) {
                        fileId = AppCommonMethods.getFileIdOfAssest(railData.getObject());
                    } else {
                        fileId = AssetContent.getLiveEventPackageId(railData.getObject().getTags());
                    }
                    if (!fileId.equalsIgnoreCase("") && subscriptionIds != null) {
                        Intent intent = new Intent(this, SubscriptionDetailActivity.class);
                        intent.putExtra(AppLevelConstants.PLAYABLE, isPlayableOrNot());
                        intent.putExtra(AppLevelConstants.POSTER_IMAGE_URL, poster_image_url);
                        intent.putExtra(AppLevelConstants.FILE_ID_KEY, fileId);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY, subscriptionIds);
                        intent.putExtra("SubscriptionIdBundle", bundle);
                        intent.putExtra(AppLevelConstants.DATE, liveEventDate);
                        intent.putExtra(AppLevelConstants.FROM_KEY, "Live Event");
                        startActivity(intent);
                    }
                } else {
                    becomeVipButtonCLicked = true;
                    new ActivityLauncher(LiveEventActivity.this).signupActivity(LiveEventActivity.this, SignUpActivity.class, CleverTapManager.DETAIL_PAGE_BECOME_VIP);
                }

            }


        });

    }

    private AlarmManager alarmManager;
    private int requestCode;
    private PendingIntent pendingIntent;
    Intent myIntent;
    String dateTimeForReminder = "";

    private void setReminder() {
        try {
            boolean isReminderAdded = new KsPreferenceKey(getApplicationContext()).getReminderId(asset.getId().toString());
            if (isReminderAdded == true) {
                showDialog();
            } else {
                boolean reminderCanAdd = checkProgramTiming(asset);
                if (reminderCanAdd) {
                    if (currentTime(asset)) {
                   /* Random random = new Random();
                    requestCode = Integer.parseInt(String.format("%02d", random.nextInt(10000)));*/
                        try {
                            if (viewModel != null) {
                                dateTimeForReminder = viewModel.getNotificationStartDate(liveEventStartDate) + " - " + AppCommonMethods.getEndTime(liveEventEndDate);
                                //  Log.w("dateTimeForReminder-->>" , dateTimeForReminder);
                            }
                        } catch (Exception ignored) {

                        }
                        Long code = asset.getId();
                        requestCode = code.intValue();
                        PrintLogging.printLog("", "notificationRequestId-->>" + requestCode);
                        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        long reminderDateTimeInMilliseconds = 000;

                        myIntent = new Intent(this, MyReceiver.class);
                        myIntent.putExtra("via", "LiveEventReminder");
                        myIntent.putExtra(AppLevelConstants.ID, Long.parseLong(asset.getId() + ""));
                        myIntent.putExtra(AppLevelConstants.Title, asset.getName());
                        myIntent.putExtra(AppLevelConstants.DESCRIPTION, asset.getDescription());
                        myIntent.putExtra(AppLevelConstants.DATETIME_REMINDER, dateTimeForReminder);
                        myIntent.putExtra(AppLevelConstants.SCREEN_NAME, "LiveEvent");
                        myIntent.putExtra("requestcode", requestCode);
                        myIntent.setAction("com.astro.sott.MyIntent");
                        myIntent.setComponent(new ComponentName(getPackageName(), "com.astro.sott.Alarm.MyReceiver"));


                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


                            Intent intent = new Intent();
                            intent.putExtra(AppLevelConstants.ID, Long.parseLong(asset.getId() + ""));
                            intent.putExtra(AppLevelConstants.Title, asset.getName());
                            intent.putExtra(AppLevelConstants.DESCRIPTION, asset.getDescription());
                            myIntent.putExtra(AppLevelConstants.DATETIME_REMINDER, dateTimeForReminder);
                            intent.putExtra(AppLevelConstants.SCREEN_NAME, "LiveEvent");
                            intent.putExtra("requestcode", requestCode);

                            intent.setComponent(new ComponentName(getPackageName(), "com.astro.sott.Alarm.MyReceiver"));
                            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                            pendingIntent = PendingIntent.getBroadcast(this, requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                        } else {

                            pendingIntent = PendingIntent.getBroadcast(this, requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }

                        Calendar calendarToSchedule = Calendar.getInstance();
                        calendarToSchedule.setTimeInMillis(System.currentTimeMillis());
                        calendarToSchedule.clear();

                        //  calendarToSchedule.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(dd), 11, 18, 0);
                        Log.w("reminderDetails", year + " " + month + " " + dd + "  " + hour + "  " + minute);
                        calendarToSchedule.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(dd), Integer.parseInt(hour), Integer.parseInt(minute), 0);

                        reminderDateTimeInMilliseconds = calendarToSchedule.getTimeInMillis();

                        PrintLogging.printLog("", "valueIsform" + reminderDateTimeInMilliseconds);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(reminderDateTimeInMilliseconds, pendingIntent), pendingIntent);
                        } else {

                            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderDateTimeInMilliseconds, pendingIntent);
                        }
                        try {
                            Toast.makeText(this, getResources().getString(R.string.reminder_added) + " " + asset.getName(), Toast.LENGTH_SHORT).show();
                        } catch (Exception ignored) {

                        }
                        new KsPreferenceKey(getApplicationContext()).setReminderId(asset.getId().toString(), true);
                        getBinding().reminderActive.setVisibility(View.VISIBLE);
                        getBinding().reminder.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.reminder_program_about_to_start), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, getResources().getString(R.string.reminder_cannot_set), Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception ignored) {

        }
    }

    private boolean currentTime(Asset asset) {
        String fiveMinuteBefore = AppCommonMethods.getFiveMinuteEarlyTimeStamp(liveEventStartDate);
        String currentTime = AppCommonMethods.getCurrentTimeStamp();
        String startTime = AppCommonMethods.getProgramStartTime(liveEventStartDate);
        Log.w("reminderDetails", currentTime + " " + startTime + " " + fiveMinuteBefore);
        if (Long.valueOf(startTime) > Long.valueOf(fiveMinuteBefore)) {
            if (Long.valueOf(currentTime) < Long.valueOf(fiveMinuteBefore)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean checkProgramTiming(Asset asset) {
       /* LongValue startValue = null, endValue = null;
        if (yearMap != null) {
            startValue = (LongValue) yearMap.get(AppLevelConstants.LiveEventProgramStartDate);
            endValue = (LongValue) yearMap.get(AppLevelConstants.LiveEventProgramEndDate);
            if (startValue != null) {
                liveEventStartDate = startValue.getValue();
            }
            if (endValue != null) {
                liveEventEndDate = endValue.getValue();
            }
            liveEventDate = AppCommonMethods.getLiveEventStartDate(liveEventStartDate);
        }
        splitStartTime(AppCommonMethods.getDateTimeFromtimeStampForReminder(liveEventStartDate));*/

        String fiveMinuteBefore = AppCommonMethods.getFiveMinuteEarlyTimeStamp(liveEventStartDate);
        String currentTime = AppCommonMethods.getCurrentTimeStamp();
        String startTime = AppCommonMethods.getProgramStartTime(liveEventStartDate);
        Log.w("reminderDetails", currentTime + " " + startTime + " " + fiveMinuteBefore);
        if (Long.valueOf(startTime) > Long.valueOf(currentTime)) {

            return true;
        } else {
            return false;
        }
    }

    private void showDialog() {
        if (asset != null) {
            new KsPreferenceKey(LiveEventActivity.this).setReminderId(asset.getId().toString(), false);
            getBinding().reminder.setVisibility(View.VISIBLE);
            getBinding().reminderActive.setVisibility(View.GONE);
            try {
                Toast.makeText(this, getResources().getString(R.string.reminder_removed) + " " + asset.getName(), Toast.LENGTH_SHORT).show();
            } catch (Exception ignored) {

            }
            cancelAlarm();
        }
        // initReminderPopupFragment();
    }

    private void splitStartTime(String startTime) {

        StringTokenizer tokens = new StringTokenizer(startTime, " ");
        String date = tokens.nextToken();// this will contain "Fruit"
        time = tokens.nextToken();
        splitDate(date);
        splitMinute(time);

    }

    private void splitDate(String date) {
        StringTokenizer tokens = new StringTokenizer(date, "-");
        dd = tokens.nextToken();
        month = tokens.nextToken();
        if (Integer.parseInt(month) != 0) {
            month = String.valueOf(Integer.parseInt(month) - 1);
        }
        year = tokens.nextToken();
    }

    private void splitMinute(String time) {
        StringTokenizer tokens = new StringTokenizer(time, ":");
        hour = tokens.nextToken();
        minute = tokens.nextToken();

    }

    private boolean isPlayableOrNot() {
        if ((AppCommonMethods.getCurrentTimeStampLong() != null) && (AppCommonMethods.getCurrentTimeStampLong() > liveEventStartDate) && (liveEventEndDate > AppCommonMethods.getCurrentTimeStampLong())) {
            return true;
        } else {
            return false;
        }

    }

    private void checkErrors() {
        if (playerChecksCompleted) {
            if (assetRuleErrorCode == AppLevelConstants.GEO_LOCATION_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeforGeoLocation(1, LiveEventActivity.this));
            } else if (errorCode == AppLevelConstants.USER_ACTIVE_ERROR) {
                runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(LiveEventActivity.this));
            } else if (errorCode == AppLevelConstants.NO_ERROR) {
                if (KsPreferenceKey.getInstance(this).getUserActive()) {
                    parentalCheck(railData);
                } else {
                    startPlayer();
                }
            }
        } else {
            DialogHelper.showAlertDialog(this, getString(R.string.play_check_message), getString(R.string.ok), this);
        }
    }

    private void parentalCheck(RailCommonData railData) {
        if (KsPreferenceKey.getInstance(this).getUserActive()) {
            if (KsPreferenceKey.getInstance(this).getParentalActive()) {
                ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(getApplicationContext());
                defaultParentalRating = responseDmsModel.getParams().getDefaultParentalLevel();
                userSelectedParentalRating = KsPreferenceKey.getInstance(getApplicationContext()).getUserSelectedRating();
                if (!userSelectedParentalRating.equalsIgnoreCase("")) {
                    assetKey = AssetContent.getAssetKey(asset.getTags(), userSelectedParentalRating, getApplicationContext());
                    if (assetKey) {
                        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                        checkOnlyDevice(railData);
                    } else {
                        validateParentalPin(railData);
                    }

                } else {
                    assetKey = AssetContent.getAssetKey(asset.getTags(), defaultParentalRating, getApplicationContext());
                    if (assetKey) {
                        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                        checkOnlyDevice(railData);
                    } else {
                        validateParentalPin(railData);
                    }
                }
            } else {
                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                checkOnlyDevice(railData);
            }
        }
    }

    private void validateParentalPin(RailCommonData railData) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.showValidatePinDialog(LiveEventActivity.this, null, "MOVIE", new ParentalDialogCallbacks() {
                    @Override
                    public void onPositiveClick(String pinText) {
                        ParentalControlViewModel parentalViewModel = ViewModelProviders.of(LiveEventActivity.this).get(ParentalControlViewModel.class);

                        parentalViewModel.validatePin(LiveEventActivity.this, pinText).observe(LiveEventActivity.this, commonResponse -> {
                            if (commonResponse.getStatus()) {
                                DialogHelper.hideValidatePinDialog();
                                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                                playerChecksCompleted = true;
                                //checkErrors();
                                checkOnlyDevice(railData);
                            } else {
                                Toast.makeText(LiveEventActivity.this, getString(R.string.incorrect_parental_pin), Toast.LENGTH_LONG).show();
                                assetRuleErrorCode = AppLevelConstants.PARENTAL_BLOCK;

                            }
                        });
                    }

                    @Override
                    public void onNegativeClick() {
                        DialogHelper.hideValidatePinDialog();
                    }
                });
            }
        });


    }

    private void checkOnlyDevice(RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(LiveEventActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> {
                        startPlayer();
                    });
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(LiveEventActivity.this).refreshKS(response -> checkDevice(railData));
                    } else {
                        showDialog(commonResponse.getMessage());
                    }
                }
            }

        });
    }


    private void startPlayer() {
        try {

            //  ConvivaManager.getConvivaAdAnalytics(this);
            Intent intent = new Intent(LiveEventActivity.this, PlayerActivity.class);
            intent.putExtra("isLivePlayer", true);
            intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railData);
            startActivity(intent);

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }


    private void playerChecks(final RailCommonData railData) {
        new GeoBlockingCheck().aseetAvailableOrNot(LiveEventActivity.this, railData.getObject(), (status, response, totalCount, errorcode, message) -> {
            if (status) {
                if (totalCount != 0) {
                    checkBlockingErrors(response);
                } else {
                    playerChecksCompleted = true;
                    checkErrors();
                }
            } else {
                showDialog(message);
            }
        });
    }

    private void checkBlockingErrors(Response<ListResponse<UserAssetRule>> response) {
        if (response != null && response.results != null && response.results.getObjects() != null) {
            for (UserAssetRule userAssetRule :
                    response.results.getObjects()) {
                switch (userAssetRule.getRuleType()) {
                    case GEO:
                        assetRuleErrorCode = AppLevelConstants.GEO_LOCATION_ERROR;
                        playerChecksCompleted = true;
                        checkErrors();
                        return;

                    default:
                        playerChecksCompleted = true;
                        checkErrors();
                        break;
                }
            }
        }
    }

    private String fileId = "";

    private void checkEntitleMent(final RailCommonData railCommonData) {

        if (isPlayableOrNot()) {
            fileId = AppCommonMethods.getFileIdOfAssest(asset);
            if (!fileId.equalsIgnoreCase("")) {
                new EntitlementCheck().checkAssetPurchaseStatus(LiveEventActivity.this, fileId, (apiStatus, purchasedStatus, vodType, purchaseKey, errorCode, message) -> {
                    this.errorCode = AppLevelConstants.NO_ERROR;
                    if (apiStatus) {
                        if (purchasedStatus) {
                            runOnUiThread(() -> {
                                getBinding().btnProgressBar.setVisibility(View.GONE);
                                if (playbackControlValue) {
                                    getBinding().playButton.setBackground(getResources().getDrawable(R.drawable.gradient_free));
                                    getBinding().playText.setTextColor(getResources().getColor(R.color.black));
                                    getBinding().playText.setText(getResources().getString(R.string.watch_now));
                                    getBinding().playButton.setVisibility(View.VISIBLE);
                                    becomeVipButtonCLicked = false;
                                    getBinding().starIcon.setVisibility(View.GONE);
                                }
                            });
                            this.vodType = EntitlementCheck.FREE;

                        } else {
                            if (vodType.equalsIgnoreCase(EntitlementCheck.SVOD)) {
                                if (xofferWindowValue) {
                                    runOnUiThread(() -> {
                                        getBinding().playButton.setBackground(getResources().getDrawable(R.drawable.gradient_svod));
                                        checkBuyTextButtonCondition(fileId);
                                        getBinding().starIcon.setVisibility(View.GONE);
                                        getBinding().playText.setTextColor(getResources().getColor(R.color.white));
                                        if (becomeVipButtonCLicked) {
                                            becomeVipButtonCLicked = false;
                                            if (UserInfo.getInstance(this).isActive()) {
                                                if (!fileId.equalsIgnoreCase("") && subscriptionIds != null) {
                                                    Intent intent = new Intent(this, SubscriptionDetailActivity.class);
                                                    intent.putExtra(AppLevelConstants.PLAYABLE, isPlayableOrNot());
                                                    intent.putExtra(AppLevelConstants.POSTER_IMAGE_URL, poster_image_url);
                                                    intent.putExtra(AppLevelConstants.FILE_ID_KEY, fileId);
                                                    intent.putExtra(AppLevelConstants.DATE, liveEventDate);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY, subscriptionIds);
                                                    intent.putExtra("SubscriptionIdBundle", bundle);
                                                    intent.putExtra(AppLevelConstants.FROM_KEY, "Live Event");
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                }
                                this.vodType = EntitlementCheck.SVOD;

                            } else if (vodType.equalsIgnoreCase(EntitlementCheck.TVOD)) {
                                if (xofferWindowValue) {
                                    runOnUiThread(() -> {
                                        getBinding().playButton.setBackground(getResources().getDrawable(R.drawable.gradient_svod));
                                        checkBuyTextButtonCondition(fileId);
                                        becomeVipButtonCLicked = false;
                                        getBinding().starIcon.setVisibility(View.VISIBLE);
                                        getBinding().playText.setTextColor(getResources().getColor(R.color.white));
                                        if (becomeVipButtonCLicked) {
                                            becomeVipButtonCLicked = false;
                                            if (UserInfo.getInstance(this).isActive()) {
                                                if (!fileId.equalsIgnoreCase("") && subscriptionIds != null) {
                                                    Intent intent = new Intent(this, SubscriptionDetailActivity.class);
                                                    intent.putExtra(AppLevelConstants.PLAYABLE, isPlayableOrNot());
                                                    intent.putExtra(AppLevelConstants.POSTER_IMAGE_URL, poster_image_url);
                                                    intent.putExtra(AppLevelConstants.FILE_ID_KEY, fileId);
                                                    intent.putExtra(AppLevelConstants.DATE, liveEventDate);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY, subscriptionIds);
                                                    intent.putExtra("SubscriptionIdBundle", bundle);
                                                    intent.putExtra(AppLevelConstants.FROM_KEY, "Live Event");
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                }

                                this.vodType = EntitlementCheck.TVOD;


                            }
                        }

                    } else {

                    }
                });
            } else {
                getBinding().btnProgressBar.setVisibility(View.GONE);
            }
        } else {
            fileId = AssetContent.getLiveEventPackageId(railData.getObject().getTags());
            if (!fileId.equalsIgnoreCase("")) {
                new EntitlementCheck().checkLiveEventPurchaseStatus(LiveEventActivity.this, fileId, (apiStatus, purchasedStatus, vodType, purchaseKey, errorCode, message) -> {
                    this.errorCode = AppLevelConstants.NO_ERROR;
                    if (apiStatus) {
                        if (purchasedStatus) {
                            runOnUiThread(() -> {
                                getBinding().btnProgressBar.setVisibility(View.GONE);
                                if (playbackControlValue) {
                                    getBinding().playButton.setBackground(getResources().getDrawable(R.drawable.live_event_button));
                                    getBinding().playText.setTextColor(getResources().getColor(R.color.heather));
                                    getBinding().playText.setText(getResources().getString(R.string.watch_now));
                                    getBinding().playButton.setVisibility(View.VISIBLE);
                                    getBinding().starIcon.setVisibility(View.GONE);
                                }
                            });
                            this.vodType = EntitlementCheck.FREE;

                        } else {
                            if (vodType.equalsIgnoreCase(EntitlementCheck.SVOD)) {
                                if (xofferWindowValue) {
                                    runOnUiThread(() -> {
                                        getBinding().playButton.setBackground(getResources().getDrawable(R.drawable.gradient_svod));
                                        checkBuyTextButtonCondition(fileId);
                                        getBinding().starIcon.setVisibility(View.VISIBLE);
                                        getBinding().playText.setTextColor(getResources().getColor(R.color.white));
                                        if (becomeVipButtonCLicked) {
                                            becomeVipButtonCLicked = false;
                                            if (UserInfo.getInstance(this).isActive()) {
                                                if (!fileId.equalsIgnoreCase("") && subscriptionIds != null) {
                                                    Intent intent = new Intent(this, SubscriptionDetailActivity.class);
                                                    intent.putExtra(AppLevelConstants.PLAYABLE, isPlayableOrNot());
                                                    intent.putExtra(AppLevelConstants.POSTER_IMAGE_URL, poster_image_url);
                                                    intent.putExtra(AppLevelConstants.FILE_ID_KEY, fileId);
                                                    intent.putExtra(AppLevelConstants.DATE, liveEventDate);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY, subscriptionIds);
                                                    intent.putExtra("SubscriptionIdBundle", bundle);
                                                    intent.putExtra(AppLevelConstants.FROM_KEY, "Live Event");
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                }
                                this.vodType = EntitlementCheck.SVOD;

                            } else if (vodType.equalsIgnoreCase(EntitlementCheck.TVOD)) {
                                if (xofferWindowValue) {
                                    runOnUiThread(() -> {
                                        getBinding().playButton.setBackground(getResources().getDrawable(R.drawable.gradient_svod));
                                        checkBuyTextButtonCondition(fileId);
                                        getBinding().starIcon.setVisibility(View.GONE);
                                        getBinding().playText.setTextColor(getResources().getColor(R.color.white));
                                        if (becomeVipButtonCLicked) {
                                            becomeVipButtonCLicked = false;
                                            if (UserInfo.getInstance(this).isActive()) {
                                                if (!fileId.equalsIgnoreCase("") && subscriptionIds != null) {
                                                    Intent intent = new Intent(this, SubscriptionDetailActivity.class);
                                                    intent.putExtra(AppLevelConstants.PLAYABLE, isPlayableOrNot());
                                                    intent.putExtra(AppLevelConstants.POSTER_IMAGE_URL, poster_image_url);
                                                    intent.putExtra(AppLevelConstants.FILE_ID_KEY, fileId);
                                                    intent.putExtra(AppLevelConstants.DATE, liveEventDate);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable(AppLevelConstants.SUBSCRIPTION_ID_KEY, subscriptionIds);
                                                    intent.putExtra("SubscriptionIdBundle", bundle);
                                                    intent.putExtra(AppLevelConstants.FROM_KEY, "Live Event");
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
                                }

                                this.vodType = EntitlementCheck.TVOD;


                            }
                        }

                    } else {

                    }
                });
            } else {
                getBinding().btnProgressBar.setVisibility(View.GONE);
            }
        }

    }

    private void checkBuyTextButtonCondition(String fileId) {
        BuyButtonManager.getInstance().getPackages(this, AppLevelConstants.LIVE_EVENT, fileId, isPlayableOrNot(), (packDetailList, packageType, lowestPackagePrice, subscriptionIds) -> {
            getBinding().btnProgressBar.setVisibility(View.GONE);
            PacksDateLayer.getInstance().setPackDetailList(packDetailList);
            this.subscriptionIds = subscriptionIds;
            if (packageType.equalsIgnoreCase(BuyButtonManager.SVOD_TVOD)) {
                getBinding().playText.setText(getResources().getString(R.string.buy_from) + " " + lowestPackagePrice);
                getBinding().playButton.setVisibility(View.VISIBLE);
            } else if (packageType.equalsIgnoreCase(BuyButtonManager.SVOD)) {
                getBinding().playText.setText(getResources().getString(R.string.become_vip));
                getBinding().playButton.setVisibility(View.VISIBLE);
            } else {
                getBinding().playText.setText(getResources().getString(R.string.buy));
                getBinding().playButton.setVisibility(View.VISIBLE);

            }
        });
    }

    private void checkDevice(final RailCommonData railData) {
        new HouseHoldCheck().checkHouseholdDevice(LiveEventActivity.this, commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    runOnUiThread(() -> checkEntitleMent(railData));
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(LiveEventActivity.this).refreshKS(response -> checkDevice(railData));
                    } else {
                        showDialog(commonResponse.getMessage());
                    }
                }
            }
        });
    }

    private void getMediaType(Asset asset, RailCommonData railCommonData) {
        setMovieMetaData(asset, 0);
    }


    private void getRefId(final int type, Asset asset) {
      /*  if (asset.getExternalId() != null && !asset.getExternalId().equalsIgnoreCase("")) {
            callTrailorAPI(asset.getExternalId(), type);
        }*/
    }


    private StringBuilder stringBuilder;
    private int lineCount = 0;

    private void setMetas() {
        //  getDuration();
        ///  getMovieYear();
        try {
            getBinding().programTitle.setText(asset.getName());
            getBinding().descriptionText.setText(asset.getDescription());
            getBinding().descriptionText.post(() -> {
                lineCount = getBinding().descriptionText.getLineCount();
                Log.d("linecountCheck", lineCount + "");
                lineCount = getBinding().descriptionText.getLineCount();
                if (lineCount <= 3) {
                    if ((!TextUtils.isEmpty(getBinding().subtitleText.getText())) || (!TextUtils.isEmpty(getBinding().castText.getText())) || (!TextUtils.isEmpty(getBinding().crewText.getText()))) {
                        getBinding().shadow.setVisibility(View.VISIBLE);
                        getBinding().lessButton.setVisibility(View.VISIBLE);
                    } else {
                        getBinding().shadow.setVisibility(View.GONE);
                        getBinding().lessButton.setVisibility(View.GONE);
                    }
                } else {

                }
            });


            stringBuilder = new StringBuilder();
            stringBuilder.append(viewModel.getStartDate(liveEventStartDate) + " - " + AppCommonMethods.getEndTime(liveEventEndDate) + " | ");
            getImage();
            getGenre();
            getXofferWindow();
            getPlayBackControl();
        } catch (Exception e) {

        }

    }

    private void getGenre() {


        ///"EEE, d MMM yyyy HH:mm:ss Z"


        viewModel.getSubGenreLivedata(asset.getTags()).observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                if (!TextUtils.isEmpty(s)) {
                    stringBuilder.append(s + " | ");
                }
                getChannelLanguage();
            }
        });


    }

    private void getChannelLanguage() {
        viewModel.getLanguageLiveData(asset.getTags()).observe(this, language -> {
            if (!TextUtils.isEmpty(language)) {
                if (language != null && !language.equalsIgnoreCase(""))
                    stringBuilder.append(language + " | ");
            }
            getParentalRating();
        });
    }

    private void getParentalRating() {
        if (!TextUtils.isEmpty(AssetContent.getParentalRating(asset.getTags()))) {

            stringBuilder.append(AssetContent.getParentalRating(asset.getTags()));

        }
        String value = stringBuilder.toString();
       /* if (value.length() > 0) {
            value = StringBuilderHolder.getInstance().getText().substring(0, value.length() - 2);
        }*/
        getBinding().metas.setText(value);
        StringBuilderHolder.getInstance().clear();

    }

    private void getImage() {
        if (asset.getImages().size() > 0) {
            for (int i = 0; i < asset.getImages().size(); i++) {
                if (asset.getImages().get(i).getRatio().equals("16x9")) {
                    String image_url = asset.getImages().get(i).getUrl();
                    poster_image_url = image_url + AppLevelConstants.WIDTH + (int) getResources().getDimension(R.dimen.detail_image_width) + AppLevelConstants.HEIGHT + (int) getResources().getDimension(R.dimen.carousel_image_height) + AppLevelConstants.QUALITY;
                    ImageHelper.getInstance(getBinding().playerImage.getContext()).loadImageToPotrait(getBinding().playerImage, poster_image_url, R.drawable.square1);
                }
            }
        }

    }

    private void callTrailorAPI(String ref_id, final int type) {
        if (type == 0) {
            viewModel.getTrailorURL(ref_id, asset.getType()).observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String _url) {
                    if (!TextUtils.isEmpty(_url)) {
                        trailor_url = _url;
                    }

                }
            });
        } else {
            viewModel.getAssetFromTrailor(ref_id, asset.getType()).observe(this, asset -> {
                if (asset != null) {
                    AssetContent.getVideoResolution(asset.getTags()).observe(this, new Observer<String>() {
                        @Override
                        public void onChanged(@Nullable String videoResolution) {
                            AssetContent.getUrl(asset, videoResolution);
                            //    setMovieMetaData(asset, type);
                        }
                    });

                }
            });
        }

    }


    private void setMovieMetaData(Asset asset, int type) {
        //PrintLogging.printLog(this.getClass(),"", "valuessId" +);
        setMetaDataValues(asset, type);
        getBinding().noConnectionLayout.setVisibility(View.GONE);
        setExpandable();
        getBinding().share.setOnClickListener(view -> {

            if (SystemClock.elapsedRealtime() - lastClickTime < AppLevelConstants.SHARE_DIALOG_DELAY) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            openShareDialouge();

        });
        getBinding().reminder.setOnClickListener(v -> {
            setReminder();
        });
        getBinding().reminderActive.setOnClickListener(v -> {
            setReminder();
        });
        // setRailFragment();
        setRailBaseFragment();
    }


    private void setRailBaseFragment() {
        FragmentManager fm = getSupportFragmentManager();
        DetailRailFragment detailRailFragment = new DetailRailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railData);
        detailRailFragment.setArguments(bundle);
        fm.beginTransaction().replace(R.id.rail_fragment, detailRailFragment).commitNow();
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(MovieDescriptionViewModel.class);

    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {

            modelCall();
            intentValues();
            manager = getSupportFragmentManager();

        } else {
            setConnectionLayout();
        }
    }


    private void setMetaDataValues(Asset asset, int type) {
        map = asset.getTags();
        yearMap = asset.getMetas();
        getEventDate(yearMap);
        PrintLogging.printLog(this.getClass(), "", "YearMapIS" + map.get("SubtitleLanguage"));

        StringBuilderHolder.getInstance().clear();

        setMetas();

        if (type == 1) {

            PrintLogging.printLog(this.getClass(), "type 1", "");
        } else {
            getRefId(0, asset);
        }

        assetId = asset.getId();
        assetType = asset.getType();


    }

    Long liveEventStartDate;
    Long liveEventEndDate;
    String liveEventDate;

    private void getEventDate(Map<String, Value> yearMap) {
        LongValue startValue = null, endValue = null;
        if (yearMap != null) {
            startValue = (LongValue) yearMap.get(AppLevelConstants.LiveEventProgramStartDate);
            endValue = (LongValue) yearMap.get(AppLevelConstants.LiveEventProgramEndDate);
            if (startValue != null) {
                liveEventStartDate = startValue.getValue();
            }
            if (endValue != null) {
                liveEventEndDate = endValue.getValue();
            }
            liveEventDate = AppCommonMethods.getLiveEventStartDate(liveEventStartDate);
        }
        if (AppCommonMethods.getCurrentTimeStampLong() > liveEventStartDate) {
            getBinding().reminder.setVisibility(View.GONE);
            getBinding().reminderActive.setVisibility(View.GONE);
        } else {
            getBinding().reminder.setVisibility(View.VISIBLE);
        }
        splitStartTime(AppCommonMethods.getDateTimeFromtimeStampForReminder(liveEventStartDate));
    }


    private void getPlayBackControl() {
        if (yearMap != null) {
            playbackControlValue = viewModel.getPlayBackControl(yearMap);
        } else {
            playbackControlValue = true;
        }
    }


    private void getXofferWindow() {
        StringValue stringValue = null;
        String xofferValue = "";
        if (yearMap != null) {
            stringValue = (StringValue) yearMap.get(AppLevelConstants.XOFFERWINDOW);
        }
        if (stringValue != null) {
            xofferValue = stringValue.getValue();
        }
        if (!xofferValue.equalsIgnoreCase("")) {
            xofferWindowValue = viewModel.isXofferWindow(xofferValue);
        } else {
            xofferWindowValue = true;
        }
    }


    private void getMovieRating() {


        if (!TextUtils.isEmpty(AssetContent.getParentalRating(map))) {
            StringBuilderHolder.getInstance().append(AssetContent.getParentalRating(map));
            StringBuilderHolder.getInstance().append(" | ");
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        try {
            if (KsPreferenceKey.getInstance(this).getUserActive()) {
                if (NetworkConnectivity.isOnline(this)) {
                    this.titleName = name;
                    isActive = true;
                }
            }
        } catch (IllegalStateException e) {
            PrintLogging.printLog("ExceptionIs", e.getMessage());
        }
    }

    private void setConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

    }

    private void setExpandable() {
//
        getBinding().expandableLayout.setOnExpansionUpdateListener(expansionFraction -> getBinding().lessButton.setRotation(0 * expansionFraction));
        getBinding().lessButton.setOnClickListener(view -> {
            getBinding().descriptionText.toggle();
            getBinding().descriptionText.setEllipsis("...");
            if (getBinding().descriptionText.isExpanded()) {
                getBinding().descriptionText.setEllipsize(null);
                getBinding().shadow.setVisibility(View.GONE);

            } else {
//                if(getBinding().descriptionText.getLineCount() >3)
//            {
//                getBinding().descriptionText.setMaxLines(3);
                getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
                getBinding().shadow.setVisibility(View.VISIBLE);

//            }
            }

            if (getBinding().expandableLayout.isExpanded()) {
                getBinding().textExpandable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0);
//                getBinding().textExpandable.setText(().getString(R.string.view_more));

            } else {
                getBinding().textExpandable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_up_24, 0);

//                getBinding().textExpandable.setText(getResources().getString(R.string.view_less));
            }
            if (view != null) {
                getBinding().expandableLayout.expand();
            }
            getBinding().expandableLayout.collapse();
        });

    }

    private void openShareDialouge() {
        try {
            CleverTapManager.getInstance().socialShare(this, asset, false);
            FirebaseEventManager.getFirebaseInstance(this).shareEvent(asset, this);
        } catch (Exception e) {

        }
        AppCommonMethods.openShareDialog(this, asset, getApplicationContext(), SubMediaTypes.LiveEvent.name());
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UserInfo.getInstance(this).isActive()) {
            if (NetworkConnectivity.isOnline(this)) {
                titleName = name;
                isActive = true;

                // getDataFromBack(railData, layoutType);

            }
        }
        checkEntitleMent(railData);
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction("com.astro.sott.Alarm.MyReceiver");
            registerReceiver(myReceiver, filter);
        } catch (Exception ignored) {

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PlayerRepository.getInstance().releasePlayer();

    }


    private void setBannerImage(Asset asset) {

        StringBuilderHolder.getInstance().clear();
        for (int i = 0; i < asset.getImages().size(); i++) {
            if (asset.getImages().get(i).getRatio().equals("1:1")) {


                StringBuilderHolder.getInstance().append(asset.getImages().get(i).getUrl());
                StringBuilderHolder.getInstance().append(AppLevelConstants.WIDTH);
                StringBuilderHolder.getInstance().append("" + (int) getResources().getDimension(R.dimen.carousel_image_width));
                StringBuilderHolder.getInstance().append(AppLevelConstants.HEIGHT);
                StringBuilderHolder.getInstance().append("" + (int) getResources().getDimension(R.dimen.carousel_image_height));
                StringBuilderHolder.getInstance().append(AppLevelConstants.QUALITY);
                Log.d("ImageUrlIs", asset.getImages().get(i).getUrl());
                break;
            }
        }


    }

    private void showDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }


    @Override
    public void onFinishDialog() {

    }


    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {
        getDataFromBack(commonData, layoutType);
        isActive = UserInfo.getInstance(this).isActive();
        checkEntitleMent(railData);
    }

    @Override
    public void onFinishEditDialog() {
        if (asset != null) {
            new KsPreferenceKey(LiveEventActivity.this).setReminderId(asset.getId().toString(), false);
            getBinding().reminder.setVisibility(View.VISIBLE);
            getBinding().reminderActive.setVisibility(View.GONE);
            Toast.makeText(this, getResources().getString(R.string.reminder_removed), Toast.LENGTH_SHORT).show();
            cancelAlarm();
        }
    }

    private void cancelAlarm() {
        try {
            Long code = asset.getId();

            int requestCode = code.intValue();
            PrintLogging.printLog("", "notificationcancelRequestId-->>" + requestCode);

            myIntent = new Intent(LiveEventActivity.this, MyReceiver.class);
            myIntent.putExtra(AppLevelConstants.ID, asset.getId());
            myIntent.putExtra(AppLevelConstants.Title, asset.getName());
            myIntent.putExtra(AppLevelConstants.DESCRIPTION, asset.getDescription());
            myIntent.putExtra(AppLevelConstants.SCREEN_NAME, AppLevelConstants.PROGRAM);
            myIntent.putExtra("requestcode", requestCode);
            myIntent.setAction("com.astro.sott.MyIntent");
            myIntent.setComponent(new ComponentName(getPackageName(), "com.astro.sott.Alarm.MyReceiver"));

            pendingIntent = PendingIntent.getBroadcast(LiveEventActivity.this, requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);

        } catch (Exception ignored) {

        }
    }
}

