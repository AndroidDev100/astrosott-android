package com.astro.sott.activities.forwardEPG;

import android.app.AlarmManager;
import android.app.PendingIntent;
import androidx.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.astro.sott.Alarm.MyReceiver;
import com.astro.sott.activities.liveChannel.viewModel.LiveChannelViewModel;
import com.astro.sott.activities.webSeriesDescription.adapter.LiveChannelCommonAdapter;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.WindowFocusCallback;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.R;
import com.astro.sott.databinding.ForwardedEpgActivityBinding;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.ProgramAsset;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ForwardedEPGActivity extends BaseBindingActivity<ForwardedEpgActivityBinding> implements DetailRailClick {
    private final List<AssetCommonBean> loadedList = new ArrayList<>();
    RailCommonData railData;
    Asset asset;
    int layoutType;
    LiveChannelViewModel viewModel;
    Map<String, MultilingualStringValueArray> map;
    FragmentManager manager;
    String startTimeStamp;
    String endTimeStamp;
    String externalIDs;
    ProgramAsset progAsset;
    String channelLogoUrl;
    String imageUrl;
    String image_url, cast = "";
    String logoUrl = "";
    private LiveChannelCommonAdapter adapter;
    private int tempCount = 0;
    private List<VIUChannel> channelList;
    private int counter = 0;
    private int loopend = 0;
    private List<VIUChannel> dtChannelsList;
    private WindowFocusCallback windowFocusListner;
    private long lastClickTime = 0;
    private boolean value;
    private String hour ="";
    private String minute = "";
    private String daate = "";
    private String month = "";
    private String year = "";
    private String time = "";
    Intent myIntent;
    PendingIntent pendingIntent;
    long reminderDateTimeInMilliseconds = 000;
    AlarmManager alarmManager;

    @Override
    public ForwardedEpgActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ForwardedEpgActivityBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionObserver();
    }

    public void intentValues() {
        setRecyclerProperties();

        getBinding().shareWith.setOnClickListener(view -> {

            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }

            lastClickTime = SystemClock.elapsedRealtime();
            openShareDialouge();
        });
        layoutType = getIntent().getIntExtra(AppLevelConstants.LAYOUT_TYPE, 0);
        railData = getIntent().getExtras().getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
        asset = railData.getObject();
        if (asset.getType() == MediaTypeConstant.getProgram(getApplicationContext())) {
            getSpecificAsset(asset.getId());
            getDataFromBack(railData, layoutType);
        } else {
            getDataFromBack(railData, layoutType);
        }
        value = new KsPreferenceKey(getApplicationContext()).getReminderId(asset.getId().toString());
        if(value == true){
            getBinding().reminder.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.notificationiconred,0,0);
            getBinding().reminder.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));
        }else {
            getBinding().reminder.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.notificationicon,0,0);
            getBinding().reminder.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
        }


        getBinding().reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }

                lastClickTime = SystemClock.elapsedRealtime();

                String currentTime = AppCommonMethods.getCurrentTimeStamp();
                Long startTime = asset.getStartDate();
                if (Long.valueOf(currentTime)>startTime) {
                    ToastHandler.display(getApplicationContext().getResources().getString(R.string.greaterdatemessage),getApplicationContext());
                }


                else {
                    value = new KsPreferenceKey(getApplicationContext()).getReminderId(asset.getId().toString());

                    if (value == true) {

                        cancelAlarmPopup();

                       // getBinding().reminder.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.notificationiconred,0,0);

                      //  ToastHandler.show(getApplicationContext().getResources().getString(R.string.reminder_exist), getApplicationContext());

                    } else {


                        splitStartTime(AppCommonMethods.get24HourTime1(asset, 1) + "");
                        //Random random = new Random();
//                        int requestCode = Integer.parseInt(String.format("%02d", random.nextInt(10000)));
                        Long value = asset.getId();
                        int requestCode = value.intValue();

                        alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);


                        myIntent = new Intent(getApplicationContext(), MyReceiver.class);
                        myIntent.putExtra(AppLevelConstants.ID, asset.getId());
                        myIntent.putExtra(AppLevelConstants.Title, asset.getName());
                        myIntent.putExtra(AppLevelConstants.DESCRIPTION, asset.getDescription());
                        myIntent.putExtra(AppLevelConstants.SCREEN_NAME, AppLevelConstants.PROGRAM);
                        myIntent.putExtra("requestcode", requestCode);
                        myIntent.setAction("com.dialog.dialoggo.MyIntent");
                        myIntent.setComponent(new ComponentName(getApplicationContext().getPackageName(), "com.dialog.dialoggo.Alarm.MyReceiver"));

//                    Random random = new Random();
//                    int requestCode = Integer.parseInt(String.format("%02d", random.nextInt(10000)));

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


                            Intent intent = new Intent();

                            intent.putExtra(AppLevelConstants.ID, asset.getId());
                            intent.putExtra(AppLevelConstants.Title, asset.getName());
                            intent.putExtra(AppLevelConstants.DESCRIPTION, asset.getDescription());
                            intent.putExtra(AppLevelConstants.SCREEN_NAME, AppLevelConstants.PROGRAM);
                            intent.putExtra("requestcode", requestCode);

                            intent.setComponent(new ComponentName(getApplicationContext().getPackageName(), "com.dialog.dialoggo.Alarm.MyReceiver"));
                            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);


                        } else {

                            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        }

                        Calendar calendarToSchedule = Calendar.getInstance();
                        calendarToSchedule.setTimeInMillis(System.currentTimeMillis());
                        calendarToSchedule.clear();

                        calendarToSchedule.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(daate), Integer.parseInt(hour), Integer.parseInt(minute), 0);


                        reminderDateTimeInMilliseconds = calendarToSchedule.getTimeInMillis();

                        PrintLogging.printLog("", "valueIsform" + reminderDateTimeInMilliseconds);

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(reminderDateTimeInMilliseconds, pendingIntent), pendingIntent);
                        } else {

                            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderDateTimeInMilliseconds, pendingIntent);
                        }


                        new KsPreferenceKey(getApplicationContext()).setReminderId(asset.getId().toString(), true);
                        ToastHandler.show(getApplicationContext().getResources().getString(R.string.reminder), getApplicationContext());
                        getBinding().reminder.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.notificationiconred,0,0);
                        getBinding().reminder.setTextColor(getApplicationContext().getResources().getColor(R.color.colorPrimary));

                    }
                }
            }
        });


    }

    private void cancelAlarmPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(getApplicationContext().getResources().getString(R.string.dialog));
        if(getApplicationContext() !=null) {
            builder.setMessage(getApplicationContext().getResources().getString(R.string.remove_alarm))
                    .setCancelable(true)
                    .setPositiveButton(getApplicationContext().getString(R.string.yes), (dialog, id) -> {
                        getBinding().reminder.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.notificationicon,0,0);
                        getBinding().reminder.setTextColor(getApplicationContext().getResources().getColor(R.color.white));
                        new KsPreferenceKey(getApplicationContext()).setReminderId(asset.getId().toString(), false);
                        cancelAlarm();

                    })
                    .setNegativeButton(getApplicationContext().getString(R.string.no), (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
            Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            bn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            bp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
    }

    private void cancelAlarm() {
        Long code = asset.getId();

        int requestCode = code.intValue();
        PrintLogging.printLog("", "notificationcancelRequestId-->>" + requestCode);


        if(pendingIntent!=null){
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(pendingIntent);
        }else {

            myIntent = new Intent(getApplicationContext(), MyReceiver.class);
            myIntent.putExtra(AppLevelConstants.ID, asset.getId());
            myIntent.putExtra(AppLevelConstants.Title, asset.getName());
            myIntent.putExtra(AppLevelConstants.DESCRIPTION, asset.getDescription());
            myIntent.putExtra(AppLevelConstants.SCREEN_NAME, AppLevelConstants.PROGRAM);
            myIntent.putExtra("requestcode",requestCode);
            myIntent.setAction("com.dialog.dialoggo.MyIntent");
            myIntent.setComponent(new ComponentName(getApplicationContext().getPackageName(),"com.dialog.dialoggo.Alarm.MyReceiver"));

            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);

        }

    }


    private void splitStartTime(String dateTime) {

        StringTokenizer tokens = new StringTokenizer(dateTime, " ");


        month = tokens.nextToken();
        daate = tokens.nextToken();
        year = tokens.nextToken();

        PrintLogging.printLog("","hoursandMinuteIs"+month+daate+year);
        String unUsedDate = tokens.nextToken();
        String time = tokens.nextToken();
        splitMinute(time);

        PrintLogging.printLog("","actualDateIs"+dateTime);
    }


    private void splitMinute(String time) {
        StringTokenizer tokens = new StringTokenizer(time, ":");
        hour = tokens.nextToken();
        minute = tokens.nextToken();

        PrintLogging.printLog("","hoursandMinuteIs"+hour+minute);
    }


    private void setRecyclerProperties() {
        getBinding().myRecyclerView.hasFixedSize();
        getBinding().myRecyclerView.setNestedScrollingEnabled(false);
        getBinding().myRecyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void getSpecificAsset(Long id) {
        if (asset.getType() == MediaTypeConstant.getProgram(getApplicationContext())) {
            progAsset = (ProgramAsset) asset;
            viewModel.getSpecificAsset( progAsset.getLinearAssetId().toString()).observe(this, railCommonData -> {
                if (railCommonData.getStatus() == true) {
                    //getDataFromBack(railCommonData,1);

                    int size = railCommonData.getObject().getImages().size();
                    for (int i = 0; i < size; i++) {
                        if (railCommonData.getObject().getImages().get(i).getRatio().equalsIgnoreCase("1:1")) {
                            channelLogoUrl = railCommonData.getObject().getImages().get(i).getUrl();
                        }
                    }
                    if (channelLogoUrl != null) {
                        if (!channelLogoUrl.equalsIgnoreCase("")) {
                            channelLogoUrl = channelLogoUrl + AppLevelConstants.WIDTH + (int) getResources().getDimension(R.dimen.square_image_width) + AppLevelConstants.HEIGHT + (int) getResources().getDimension(R.dimen.square_image_height) + AppLevelConstants.QUALITY;
                            ImageHelper.getInstance(this).loadImageTo(getBinding().channelLogo, channelLogoUrl, R.drawable.square1);
                        }
                    }

                }
            });
        } else {
            viewModel.getSpecificAsset( String.valueOf(asset.getId())).observe(this, railCommonData -> {
                if (railCommonData.getStatus() == true) {
                    getDataFromBack(railCommonData, 1);

                }
            });
        }
    }

    private void getDataFromBack(RailCommonData commonRailData, int layout) {
        railData = commonRailData;
        asset = commonRailData.getObject();
       /* mediaAsset=(MediaAsset)asset;
        externalIDs=mediaAsset.getExternalIds();
        AppConstants.EXTERNAL_IDS=externalIDs;*/
        map = asset.getTags();
        getChannelGenres();
        getStartEndTimestamp();
        setMetaDataValue();
        //getEPGChannels(asset);
        getRails();
        setPlayerFragment();
        getUrlToPlay(asset);
        setHungamaTag(asset);


    }

    private void setHungamaTag(Asset asset) {
        boolean isProviderAvailable = AssetContent.getHungamaTag(asset.getTags());
        if (isProviderAvailable){
            getBinding().playerImageLayout.hungama.setVisibility(View.VISIBLE);
        }else {
            getBinding().playerImageLayout.hungama.setVisibility(View.GONE);
        }
    }

    private void setPlayerFragment() {
        manager = getSupportFragmentManager();
        if (asset.getImages().size() > 0) {
            image_url = asset.getImages().get(0).getUrl();
            image_url = image_url + AppLevelConstants.WIDTH + (int) getResources().getDimension(R.dimen.carousel_image_width) + AppLevelConstants.HEIGHT + (int) getResources().getDimension(R.dimen.carousel_image_height) + AppLevelConstants.QUALITY;

            ImageHelper.getInstance(this).loadImageTo(getBinding().playerImageLayout.playerImage, image_url, R.drawable.square1);
        }

        getBinding().playerImageLayout.playIcon.setVisibility(View.GONE);
        getBinding().playerImageLayout.playIcon.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            //playerChecks(railData);

        });

        getBinding().playerImageLayout.backArrow.setOnClickListener(view -> onBackPressed());

    }

    private void getUrlToPlay(Asset asset) {
        int imageSize = asset.getImages().size();
        for (int i = 0; i < imageSize; i++) {
            if (asset.getImages().get(i).getRatio().equalsIgnoreCase("16:9")) {
                imageUrl = asset.getImages().get(i).getUrl();
            }
        }
        // fragment.getUrl(AppConstants.FORWARDED_EPG + "=:" + imageUrl, asset, railData.getProgress());
    }

    private void getSpecifi(Asset asset) {
        viewModel.getSpecificAsset( String.valueOf(asset.getId())).observe(this, railCommonData -> {
            if (railCommonData.getStatus() == true) {
                for (int i = 0; i < railCommonData.getObject().getImages().size(); i++) {
                    String ratio = railCommonData.getObject().getImages().get(i).getRatio();
                    if (ratio.equalsIgnoreCase("1:1")) {
                        logoUrl = railCommonData.getObject().getImages().get(i).getUrl();
                        logoUrl = logoUrl + AppLevelConstants.WIDTH + (int) getResources().getDimension(R.dimen.square_image_width) + AppLevelConstants.HEIGHT + (int) getResources().getDimension(R.dimen.square_image_height) + AppLevelConstants.QUALITY;
                        ImageHelper.getInstance(this).loadImageTo(getBinding().channelLogo, logoUrl, R.drawable.square1);
                    }
                }
            }
        });
    }

    private void getCasts(Map<String, MultilingualStringValueArray> tags) {
        viewModel.getCastLiveData(tags).observe(this, castTest -> {
            cast = castTest;
            if (castTest.equalsIgnoreCase("")) {
                getBinding().castHint.setVisibility(View.GONE);
            } else {
                getBinding().castHint.setVisibility(View.VISIBLE);
                getBinding().setCastValue(" " + castTest.trim());
            }

        });
    }

    private void getRails() {

        viewModel.getChannelList(AppLevelConstants.TAB_FORWARDED_EPG_DETAIL).observe(this, assetCommonBeans -> {
            if (assetCommonBeans != null && assetCommonBeans.getStatus()) {
                dtChannelsList = assetCommonBeans.getDTChannelList();
                callCategoryRailAPI(dtChannelsList);

            }

        });

    }

    private void getEPGChannels(Asset asset) {
        viewModel.getEPGChannelsList(externalIDs, startTimeStamp, endTimeStamp, 1,1).observe(this, railCommonData -> {
            if (railCommonData.size() > 0) {
                getBinding().contentFrame.setVisibility(View.VISIBLE);
                getBinding().moreFrame.setVisibility(View.VISIBLE);
                setMetaDataValue();
            } else {
                getBinding().moreFrame.setVisibility(View.INVISIBLE);
                getBinding().contentFrame.setVisibility(View.GONE);
            }
        });
    }

    private void callCategoryRailAPI(List<VIUChannel> list) {
        if (dtChannelsList != null) {
            if (dtChannelsList.size() > 0) {
                channelList = list;
                if (counter != channelList.size() && counter < channelList.size()) {
                    loopend = 1;
                    viewModel.getListLiveData(channelList.get(counter).getId(), dtChannelsList, counter, 1).observe(this, assetCommonBeans -> {
                        if (assetCommonBeans != null && assetCommonBeans.size() > 0) {
                            boolean status = assetCommonBeans.get(0).getStatus();
                            if (status) {
                                setUIComponets(assetCommonBeans, counter, 1);
                                counter++;
                                callCategoryRailAPI(channelList);
                            } else {
                                if (counter != channelList.size()) {
                                    counter++;
                                    callCategoryRailAPI(channelList);
                                }
                            }

                        }

                    });
                } else {
                    loopend = 0;
                }
            }
        }
    }

    private void setMetaDataValue() {
        /*if (railCommonData.size() > 0) {
            AppConstants.currentProgramID = railCommonData.get(0).getObject().getId().intValue();
            setValues(railCommonData.get(0));
        }*/

        setValues(railData);
    }

    private void setValues(RailCommonData commonData) {
        Asset asset = commonData.getObject();
        getBinding().programName.setText(asset.getName());


        getBinding().descriptionText.setText(asset.getDescription());

        String stTime = viewModel.getProgramTime(asset, 1);
        String endTime = viewModel.getProgramTime(asset, 2);
        String totalDuration = viewModel.getProgramDurtion(endTime, stTime);

        getBinding().start.setText(AppCommonMethods.setTime(commonData.getObject(), 1) + "");
        getBinding().end.setText(AppCommonMethods.setTime(commonData.getObject(), 2) + "");
        getBinding().totalDurationTxt.setText(totalDuration);
        getCasts(commonData.getObject().getTags());
        getBinding().descriptionText.post(() -> {
            int lineCount = getBinding().descriptionText.getLineCount();
            getBinding().descriptionText.setMaxLines(2);
            if (lineCount > 2) {
                getBinding().lessButton.setVisibility(View.VISIBLE);
            } else {
                if (!cast.equalsIgnoreCase("")) {
                    getBinding().lessButton.setVisibility(View.VISIBLE);
                } else {
                    getBinding().lessButton.setVisibility(View.INVISIBLE);
                }
            }
            // Use lineCount here
        });
        setExpandable();
    }

    private void setExpandable() {

        getBinding().expandableLayout.collapse();
        getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
        getBinding().setExpandabletext(getResources().getString(R.string.more));
        getBinding().expandableLayout.setOnExpansionUpdateListener(expansionFraction -> getBinding().lessButton.setRotation(0 * expansionFraction));
        getBinding().lessButton.setOnClickListener(view -> {

            getBinding().descriptionText.toggle();

            if (getBinding().descriptionText.isExpanded()) {
                getBinding().descriptionText.setEllipsize(null);
            } else {
                getBinding().descriptionText.setMaxLines(2);

                getBinding().descriptionText.setEllipsize(TextUtils.TruncateAt.END);
            }

            if (getBinding().expandableLayout.isExpanded()) {
                getBinding().setExpandabletext(getResources().getString(R.string.more));

            } else {
                getBinding().setExpandabletext(getResources().getString(R.string.less));
            }
            if (view != null) {
                getBinding().expandableLayout.expand();
            }
            getBinding().expandableLayout.collapse();
        });
    }


    private void getChannelGenres() {
        viewModel.getGenreLivedata(map).observe(this, s -> {
            if (s.equals("")) {
                getBinding().genreTxt.setVisibility(View.GONE);
                getBinding().dot.setVisibility(View.GONE);
            } else {
                getBinding().genreTxt.setVisibility(View.VISIBLE);
                getBinding().dot.setVisibility(View.VISIBLE);
                getBinding().genreTxt.setText(s.trim());

            }

        });
    }

    private void getStartEndTimestamp() {
        startTimeStamp = AppCommonMethods.getCurrentDateTimeStamp(1);
        endTimeStamp = AppCommonMethods.getCurrentDateTimeStamp(2);


    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(ForwardedEPGActivity.this)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean == true) {
            getBinding().noConnectionLayout.setVisibility(View.GONE);

            modelCall();
            intentValues();
        } else {
            noConnectionLayout();
        }
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(LiveChannelViewModel.class);

    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }


    private void setUIComponets(List<AssetCommonBean> assetCommonBeans, int counter, int type) {
        try {

            if (adapter != null) {
                if (type > 0) {
                    int temp = counter + tempCount;
                    loadedList.add(assetCommonBeans.get(0));
                    adapter.notifyItemChanged(temp);
                } else {
                    loadedList.add(assetCommonBeans.get(0));
                    adapter.notifyItemChanged(counter);
                }
            } else {
                loadedList.add(assetCommonBeans.get(0));
                adapter = new LiveChannelCommonAdapter(this, loadedList);
                getBinding().myRecyclerView.setAdapter(adapter);
            }
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }


    }

    private void openShareDialouge() {
        AppCommonMethods.openShareDialog(ForwardedEPGActivity.this, asset, getApplication().getApplicationContext());
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

    }


   /* private void getCurrentProgram(List<RailCommonData> railCommonData) {
        for (int i = 0; i < railCommonData.size(); i++) {
            Asset assetData = railCommonData.get(i).getObject();
            comparingPrograms(assetData, i);
        }
        setValues(railCommonData.get(currentProgIndex));
        PrintLogging.printLog("", "currentProgIndex" + currentProgIndex);
    }

    private void comparingPrograms(Asset assetData, int position) {
        Long endTime = assetData.getEndDate();
        String programTime = viewModel.getProgramTime(endTime);
        String currentTime = AppCommonMethods.getCurrentTime();
        currentProgIndex = viewModel.getTimeDifference(programTime, currentTime, position);
    }*/

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (windowFocusListner != null)

            windowFocusListner.windowFocusChange(hasFocus);
    }
}

