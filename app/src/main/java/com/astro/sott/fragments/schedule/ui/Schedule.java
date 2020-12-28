package com.astro.sott.fragments.schedule.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.activities.liveChannel.ui.LiveChannel;
import com.astro.sott.activities.parentalControl.viewmodels.ParentalControlViewModel;
import com.astro.sott.activities.subscription.manager.AllChannelManager;
import com.astro.sott.callBacks.commonCallBacks.ParentalDialogCallbacks;
import com.astro.sott.fragments.dialog.AlertDialogSingleButtonFragment;
import com.astro.sott.fragments.nowPlaying.NowPlaying;
import com.astro.sott.modelClasses.dmsResponse.ParentalLevels;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.networking.refreshToken.RefreshKS;
import com.astro.sott.player.entitlementCheckManager.EntitlementCheck;
import com.astro.sott.player.geoBlockingManager.GeoBlockingCheck;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.Alarm.MyReceiver;
import com.astro.sott.R;
import com.astro.sott.activities.liveChannel.listener.LiveChannelActivityListener;
import com.astro.sott.activities.liveChannel.viewModel.LiveChannelViewModel;
import com.astro.sott.baseModel.BaseActivity;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.SpecificAssetCallBack;
import com.astro.sott.callBacks.commonCallBacks.EpgNotAvailableCallBack;
import com.astro.sott.databinding.FragmentScheduleBinding;
import com.astro.sott.fragments.schedule.adapter.ProgramsAdapter;
import com.astro.sott.player.houseHoldCheckManager.HouseHoldCheck;
import com.astro.sott.player.ui.PlayerActivity;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.DialogHelper;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.ProgressHandler;
import com.astro.sott.utils.helpers.RecyclerAnimator;
import com.astro.sott.utils.helpers.shimmer.Constants;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.ListResponse;
import com.kaltura.client.types.ProgramAsset;
import com.kaltura.client.types.UserAssetRule;
import com.kaltura.client.utils.response.base.Response;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class Schedule extends BaseBindingFragment<FragmentScheduleBinding> implements EpgNotAvailableCallBack, AlertDialogSingleButtonFragment.AlertDialogListener, SpecificAssetCallBack {
    private int i = 0;
    private String startTimeStamp;
    private String endTimeStamp;
    private LiveChannelViewModel viewModel;
    private int position = 0;
    private LinearLayoutManager manager;
    private String values = "Today";
    private CustomRunnable customRunnable;
    private ProgramsAdapter adapter;
    private View previousView;
    private BaseActivity baseActivity;
    private String month = "";
    private String dd = "";
    private String year = "";
    private String hour = "";
    private String minute = "";
    private String time = "";
    private MyReceiver myReceiver;
    PendingIntent pendingIntent = null;
    AlarmManager alarmManager;
    Intent myIntent;
    long reminderDateTimeInMilliseconds = 000;
    int mm;
    int yr;
    int ddy;
    private boolean playerChecksCompleted = false;
    private int assetRuleErrorCode = -1;
    private int errorCode = -1;
    private RailCommonData railCommonData;
    private RailCommonData railCommonData1;
    private ProgressHandler progressDialog;
    private boolean isParentalLocked = false;
    private String defaultParentalRating = "";
    private String userSelectedParentalRating = "";
    private int userSelectedParentalPriority;
    private int priorityLevel;
    private int assetRestrictionLevel;
    ArrayList<ParentalLevels> parentalLevels;
    private int counter;
    private int firstVisiblePosition, pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean mIsLoading = true, isScrolling = false;
    private int mScrollY, totalCOunt = 0;
    private ArrayList<RailCommonData> arrayList = new ArrayList<>();
    private LiveChannelActivityListener mListener;
    private int totalProgramListCount;
    private boolean assetKey = false;
    private boolean isLiveChannel = true;
    private boolean isDtvAdded = false;

    @Override
    public FragmentScheduleBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return FragmentScheduleBinding.inflate(inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
        try {
            mListener = (LiveChannelActivityListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement LiveChannelActivityListener");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getActivity() != null) {
            ((LiveChannel) getActivity()).setLiveChannelCommunicator((int oldScrollX, int oldScrollY) -> {
                if(totalProgramListCount != arrayList.size()) {
                    mListener.showScrollViewProgressBarView(true);
                    onScrollListener.onScrolled(getBinding().programRecyclerview, oldScrollX, oldScrollY);
                }
            });
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentalLevels = new ArrayList<>();
        connectionObserver();
        myReceiver = new MyReceiver();
      //  progressDialog = new ProgressHandler(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initializeRecyclerView();
        scrollRefresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isVisible()) {
            createEpgDateChips();
        }
    }

    private void createEpgDateChips() {
        LayoutInflater inflater = (LayoutInflater) baseActivity.getSystemService(LAYOUT_INFLATER_SERVICE);

        int size = 11;

        for (int i = 0; i < size; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date()); // Now use today date.
            c.add(Calendar.DATE, -3); // Adding 5 days

            String cast = getCurrentDate(i, c.getTime());
            if (cast != null && inflater != null) {
                final LinearLayout clickableColumn = (LinearLayout) inflater.inflate(
                        R.layout.view_schedule_date, null);
                TextView thumbnailImage = clickableColumn
                        .findViewById(R.id.thumbnail_image);
                thumbnailImage.setText(cast);
                thumbnailImage.setTextSize(12);
                clickableColumn.setContentDescription(String.valueOf(i));
                getBinding().castsContainer.addView(clickableColumn);

                clickableColumn.setOnClickListener(v -> {
                    selectDate(v);
                    TextView thumbnailImage1 = clickableColumn
                            .findViewById(R.id.thumbnail_image);
                    values = thumbnailImage1.getText().toString();
                    isScrolling = false;
                });
            }
        }
        selectDate(getBinding().castsContainer.getChildAt(3));

        customRunnable = new CustomRunnable(getBinding().castsContainer, getBinding().scrollable, baseActivity);
        getBinding().scrollable.post(customRunnable);

    }

    private void callChannelsByTimeStamp(int i) {
        PrintLogging.printLog(this.getClass(), "", "valueOfIis" + i);
        getCurrentTime(i);
        getBinding().programRecyclerview.setVisibility(View.GONE);
        startTimeStamp = AppCommonMethods.getNextDateTimeStamp(1, i);
        endTimeStamp = AppCommonMethods.getNextDateTimeStamp(2, i);
        counter = 1;
        if (arrayList != null) {
            arrayList.clear();
            getBinding().programRecyclerview.setAdapter(null);
        }
        getEPGChannels();
    }

    private String getCurrentTime(int i) {
        String output = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date()); // Now use today date.
            c.add(Calendar.DATE, i); // Adding 5 days
            output = sdf.format(c.getTime());

            month = checkDigit(c.get(Calendar.MONTH));
            dd = checkDigit(c.get(Calendar.DATE));
            year = checkDigit(c.get(Calendar.YEAR));


            PrintLogging.printLog(this.getClass(), "", "currentDateIs" + month + dd + year);

        } catch (Exception e) {
            PrintLogging.printLog("", "dateIsssuess" + e.toString());
        }
        return output;
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(baseActivity)) {
            connectionValidation(true);
        } else {
            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean) {
          //  intentValues();
            modelCall();
            getStartEndTimestamp();
//            new Handler().postDelayed(this::getEPGChannels, 500);
        } else {
            ToastHandler.show(getResources().getString(R.string.no_connection), baseActivity.getApplicationContext());
        }
    }

    private void intentValues() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            railCommonData1 = bundle.getParcelable(AppLevelConstants.RAIL_DATA_OBJECT);
            AllChannelManager.getInstance().setRailCommonData(railCommonData1);

        }
    }

    private void initializeRecyclerView() {
        manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        // getBinding().programRecyclerview.setNestedScrollingEnabled(false);
        getBinding().programRecyclerview.setLayoutManager(manager);
        DividerItemDecoration itemDecor = new DividerItemDecoration(baseActivity, LinearLayoutManager.VERTICAL);
        getBinding().programRecyclerview.addItemDecoration(itemDecor);
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(LiveChannelViewModel.class);
    }

    private void getEPGChannels() {
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        getBinding().noItem.setVisibility(View.GONE);

        if(viewModel == null){
            return;
        }
        viewModel.getEPGChannelsList(NowPlaying.EXTERNAL_IDS, startTimeStamp, endTimeStamp, 2, counter).observe(this, railCommonData -> {
            if (railCommonData != null && railCommonData.size() > 0) {
                getBinding().noItem.setVisibility(View.GONE);
                getBinding().programRecyclerview.setVisibility(View.VISIBLE);
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                totalProgramListCount = railCommonData.get(0).getTotalCount();
                setMetaDataValue(railCommonData);
            } else {
                if (arrayList.size() == 0) {
                    getBinding().noItem.setVisibility(View.VISIBLE);
                    getBinding().programRecyclerview.setVisibility(View.GONE);
                }
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                if(mListener != null) {
                    mListener.showScrollViewProgressBarView(false);
                }
            }
        });
    }

    private void setMetaDataValue(List<RailCommonData> railCommonData) {
        if (railCommonData.size() > 0) {
//            new RecyclerAnimator(baseActivity).animate(getBinding().programRecyclerview);
            setValues(railCommonData);
        }
    }

    private void setValues(final List<RailCommonData> commonData) {
        arrayList.addAll(commonData);
        if (!compareDates()) {
            position = -1;
            setAdapter();
        } else {
            moveListTocurrentPrograme(arrayList);
        }

    }


    private void setAdapter() {

        if (!isScrolling) {
            new RecyclerAnimator(baseActivity).animate(getBinding().programRecyclerview);
//            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            adapter = new ProgramsAdapter(baseActivity, arrayList, position, Schedule.this, Schedule.this);
            getBinding().programRecyclerview.setAdapter(adapter);
            manager.scrollToPositionWithOffset(position, 0);
            mIsLoading = adapter.getItemCount() != totalCOunt;
            adapter.updateLiveChannelCount(position);
            mListener.showScrollViewProgressBarView(false);
        } else {
            if(mListener != null) {
                new Handler().postDelayed(() -> mListener.showScrollViewProgressBarView(false),800);
            }

            if(getResources().getBoolean(R.bool.isTablet)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mIsLoading = adapter.getItemCount() != totalCOunt;
                        adapter.updateLiveChannelCount(position);
                        adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }else {
                mIsLoading = adapter.getItemCount() != totalCOunt;
                adapter.updateLiveChannelCount(position);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void resetAdapter() {
        if (adapter != null) {
            // adapter = null;
            getBinding().programRecyclerview.setAdapter(adapter);
            manager.scrollToPositionWithOffset(position, 0);
        }
    }


    private void moveListTocurrentPrograme(List<RailCommonData> commonData) {
        boolean status = compareDates();
        if (status) {
            int id = Constants.currentProgramID;
            for (int i = 0; i < commonData.size(); i++) {

                int val = commonData.get(i).getObject().getId().intValue();

                if (id == val) {
                    position = i;
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    setAdapter();
                    return;
                }
            }
            counter++;
            getEPGChannels();
        } else {
            position = -1;

        }

    }

    private boolean compareDates() {
        return values.contains("Today");
    }

    private void getStartEndTimestamp() {
        startTimeStamp = AppCommonMethods.getCurrentDateTimeStamp(1);
        endTimeStamp = AppCommonMethods.getCurrentDateTimeStamp(2);
    }

    private String getCurrentDate(int i, Date startDate) {
        String output = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.US);
            Calendar c = Calendar.getInstance();
            c.setTime(startDate); // Now use today date.
            c.add(Calendar.DATE, i); // Adding 5 days
            if (i == 2) {
                output = "Yesterday";

            } else if (i == 3) {
                output = "Today";


            } else if (i == 4) {
                output = "Tomorrow";

            } else {
                output = sdf.format(c.getTime());
            }

//          String  month = checkDigit(c.get(Calendar.MONTH));
//           String dd = checkDigit(c.get(Calendar.DATE));
//         String   year = checkDigit(c.get(Calendar.YEAR));
//
//            PrintLogging.printLog(this.getClass(), "", "currentDateIs" + month+dd+year);

        } catch (Exception e) {
            PrintLogging.printLog(this.getClass(), "", "dateIsssuess" + e.toString());
        }
        return output;
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customRunnable != null) {
            customRunnable = null;
        }
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    @Override
    public void itemClicked(boolean status, RailCommonData railData) {
        if (!status) {
            showDialog(getResources().getString(R.string.no_catchup_available));
        } else {

            railCommonData = railData;


            ProgramAsset progAsset = (ProgramAsset) railData.getObject();

            viewModel.getSpecificAsset(progAsset.getLinearAssetId().toString()).observe(this, railCommonData -> {
                if (railCommonData != null && railCommonData.getStatus()) {
                    getBinding().includeProgressbarPlay.progressBar.setOnClickListener(view1 -> {

                    });
                    if (KsPreferenceKey.getInstance(getActivity()).getUserActive()) {
                        callProgressBar();
                        playerChecks(railCommonData.getObject());
                    }else {
                        DialogHelper.showLoginDialog(getActivity());
                    }


                }
            });


        }

    }

    private void playerChecks(final Asset asset) {
        new GeoBlockingCheck().aseetAvailableOrNot(getActivity(), asset, (status, response, totalCount, errorcode, message) -> {
            if (status) {
                if (totalCount != 0) {
                    checkBlockingErrors(response, asset);
                } else {
                    checkEntitleMent(asset);
                }
            }else {
                callProgressBar();
                showDialog(message);
            }
        });
    }

    private void checkBlockingErrors(Response<ListResponse<UserAssetRule>> response, Asset asset) {
        if (response != null && response.results != null && response.results.getObjects() != null) {
            for (UserAssetRule userAssetRule :
                    response.results.getObjects()) {
                switch (userAssetRule.getRuleType()) {
                    case GEO:
                        assetRuleErrorCode = AppLevelConstants.GEO_LOCATION_ERROR;
                        playerChecksCompleted = true;
                        checkErrors(asset);
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


    private void checkErrors(Asset asset) {
        if(getActivity() == null){
            return;
        }
        if (playerChecksCompleted) {
            if (assetRuleErrorCode == AppLevelConstants.GEO_LOCATION_ERROR) {
                getActivity().runOnUiThread(() -> DialogHelper.openDialougeforGeoLocation(1, getActivity()));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.FOR_PURCHASED_ERROR) {
                getActivity().runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(getActivity()));
                callProgressBar();
            } else if (errorCode == AppLevelConstants.USER_ACTIVE_ERROR) {
                getActivity().runOnUiThread(() -> DialogHelper.openDialougeForEntitleMent(getActivity()));
                callProgressBar();
            }
//            else if (assetRuleErrorCode == AppLevelConstants.PARENTAL_BLOCK) {
//                if (KsPreferenceKey.getInstance(getActivity()).getUserActive())
//                    validateParentalPin(asset);
//                else
//                    getUrl(AssetContent.getURL(asset), asset, playerProgress, isLivePlayer, "");
//            }
            else if (errorCode == AppLevelConstants.NO_ERROR) {

                if (KsPreferenceKey.getInstance(getActivity()).getUserActive()) {
                    parentalCheck(asset);
                } else {
                    callProgressBar();
                    if(getActivity() == null){
                        return;
                    }
                    KsPreferenceKey.getInstance(getActivity()).setCatchupValue(true);
                    Intent intent = new Intent(getActivity(), PlayerActivity.class);
                    intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railCommonData);
                    getActivity().startActivity(intent);
                }


//                else
//                    getUrl(AssetContent.getURL(asset), asset, playerProgress, isLivePlayer, "");
            }
        } else {
            callProgressBar();
            DialogHelper.showAlertDialog(getActivity(), getString(R.string.play_check_message), getString(R.string.ok), this);
        }
    }

    private void parentalCheck(Asset asset) {
        if (KsPreferenceKey.getInstance(getActivity()).getUserActive()) {
            if (KsPreferenceKey.getInstance(getActivity()).getParentalActive()) {
                ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(getActivity());
                defaultParentalRating = responseDmsModel.getParams().getDefaultParentalLevel();
                userSelectedParentalRating = KsPreferenceKey.getInstance(getActivity()).getUserSelectedRating();
                if (!userSelectedParentalRating.equalsIgnoreCase("")) {
                    assetKey = AssetContent.getAssetKey(asset.getTags(),userSelectedParentalRating, getActivity());
                    if(assetKey){
                        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                        checkOnlyDevice(asset);
                    }else {
                        validateParentalPin(asset);
                    }

                } else {
                    assetKey = AssetContent.getAssetKey(asset.getTags(),defaultParentalRating, getActivity());
                    if(assetKey){
                        assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                        checkOnlyDevice(asset);
                    }else {
                        validateParentalPin(asset);
                    }
                }
            } else {
                assetRuleErrorCode = AppLevelConstants.NO_ERROR;
                checkOnlyDevice(asset);
            }
        }
    }



    private void validateParentalPin(Asset asset) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DialogHelper.showValidatePinDialog(getActivity(), null, "Schedule", new ParentalDialogCallbacks() {
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
                        DialogHelper.hideValidatePinDialog();
                        callProgressBar();
                    }
                });
            }
        });

    }


    private void checkOnlyDevice(Asset asset) {
        if(getActivity() == null)
            return;
        new HouseHoldCheck().checkHouseholdDevice(getActivity(), commonResponse -> {
            if (commonResponse != null) {
                if (commonResponse.getStatus()) {
                    //play next episode here
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callProgressBar();
                                KsPreferenceKey.getInstance(getActivity()).setCatchupValue(true);
                                Intent intent = new Intent(getActivity(), PlayerActivity.class);
                                intent.putExtra(AppLevelConstants.RAIL_DATA_OBJECT, railCommonData);
                                getActivity().startActivity(intent);
                            }
                        });
                    }
                } else {
                    if (commonResponse.getErrorCode().equals(AppLevelConstants.KS_EXPIRE)) {
                        new RefreshKS(getActivity()).refreshKS(response -> checkDevice(asset));
                    }else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }
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
                    }else {
                        callProgressBar();
                        showDialog(commonResponse.getMessage());
                    }
                }
            }

        });

    }

    private void checkEntitleMent(final Asset asset) {
       String fileId = AppCommonMethods.getFileIdOfAssest(asset);
        new EntitlementCheck().checkAssetType(getActivity(), fileId, (status, response, purchaseKey, errorCode1, message) -> {
            if (status) {
                playerChecksCompleted = true;
                try {
                    if (purchaseKey.equalsIgnoreCase(getResources().getString(R.string.FOR_PURCHASE_SUBSCRIPTION_ONLY)) || purchaseKey.equals(getResources().getString(R.string.FREE))) {
                        errorCode = AppLevelConstants.NO_ERROR;
                        checkErrors(asset);
                    } else if (purchaseKey.equals(getResources().getString(R.string.FOR_PURCHASED))) {
//                                    errorCode = AppLevelConstants.FOR_PURCHASED_ERROR;
//                                    checkErrors(asset);
                        if (KsPreferenceKey.getInstance(getActivity()).getUserActive()) {
                            isDtvAccountAdded(asset);
                            //check Dtv Account Added or Not

                        } else {
                            errorCode = AppLevelConstants.FOR_PURCHASED_ERROR;
                            checkErrors(asset);
                        }

                    } else {
                        if (KsPreferenceKey.getInstance(getActivity()).getUserActive()) {
                            isDtvAccountAdded(asset);
                            //check Dtv Account Added or Not
                        } else {
                            errorCode = AppLevelConstants.USER_ACTIVE_ERROR;
                            checkErrors(asset);
                            //not play
                        }
//                                    errorCode = AppLevelConstants.USER_ACTIVE_ERROR;
//                                    checkErrors(asset);
                    }
                } catch (Exception e) {
                    PrintLogging.printLog("Exception", "", "" + e);
                }
            }else {
                callProgressBar();
                if (message!="")
                    showDialog(message);
            }
//                            playerChecksCompleted = true;
        });

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
                                    callProgressBar();
                                    checkForSubscription(isDtvAdded,asset);

                                } else if (dtvAccount.equalsIgnoreCase("")) {
                                    isDtvAdded = false;
                                    callProgressBar();
                                    checkForSubscription(isDtvAdded,asset);
                                } else {
                                    isDtvAdded = true;
                                    callProgressBar();
                                    checkForSubscription(isDtvAdded,asset);
                                }

                            } else {
                                // Api Failure Error
                                callProgressBar();
                                showDialog(getString(R.string.something_went_wrong_try_again));
                            }
                        }catch (Exception e){
                            Log.e("ExceptionIs",e.toString());
                        }
                    }
                });

            }
        });
    }

    private void checkForSubscription(boolean isDtvAdded, Asset asset) {
        //***** Mobile + Non-Dialog + Non-DTV *************//
        if(KsPreferenceKey.getInstance(getActivity()).getUserType().equalsIgnoreCase(AppLevelConstants.NON_DIALOG) && isDtvAdded==false){
            getActivity().runOnUiThread(() ->DialogHelper.openDialougeFornonDialog(getActivity(),isLiveChannel));
        }
        //********** Mobile + Non-Dialog + DTV ******************//
        else if (KsPreferenceKey.getInstance(getActivity()).getUserType().equalsIgnoreCase(AppLevelConstants.NON_DIALOG) && isDtvAdded==true){
            getActivity().runOnUiThread(() ->DialogHelper.openDialougeFornonDialog(getActivity(),isLiveChannel));
        }
        //*********** Mobile + Dialog + Non-DTV *****************//
        else if(KsPreferenceKey.getInstance(getActivity()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded==false){
            if(AssetContent.isPurchaseAllowed(asset.getMetas(), asset,getActivity())){
                getActivity().runOnUiThread(() ->DialogHelper.openDialougeForDtvAccount(getActivity(), true,isLiveChannel));
            }else {
                getActivity().runOnUiThread(() ->DialogHelper.openDialougeForDtvAccount(getActivity(), false,isLiveChannel));
            }
        }
        //************ Mobile + Dialog + DTV ********************//
        else if (KsPreferenceKey.getInstance(getActivity()).getUserType().equalsIgnoreCase(AppLevelConstants.DIALOG) && isDtvAdded==true){
            if(AssetContent.isPurchaseAllowed(asset.getMetas(), asset,getActivity())){
                getActivity().runOnUiThread(() ->DialogHelper.openDialougeForDtvAccount(getActivity(), true,isLiveChannel));
            }else {
                getActivity().runOnUiThread(() ->DialogHelper.openDialougeForDtvAccount(getActivity(), false,isLiveChannel));
            }
        }else {
            showDialog(getString(R.string.something_went_wrong_try_again));
        }
    }

    private void scrollRefresh() {
        getBinding().programRecyclerview.addOnScrollListener(onScrollListener);
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            try {
                LinearLayoutManager layoutManager = ((LinearLayoutManager) getBinding().programRecyclerview.getLayoutManager());
                firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                if (dy > 0) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    if (mIsLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            mIsLoading = false;
                            counter++;
                            isScrolling = true;
                            mScrollY += dy;
                            getEPGChannels();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }
        }
    };


    private void selectDate(View v) {
        if (previousView != null)
            previousView.setSelected(false);
        v.setSelected(true);

        callChannelsByTimeStamp(Integer.valueOf(v.getContentDescription().toString()) - 3);
        // connectionObserver();
        previousView = v;
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

    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (baseActivity != null) {
            baseActivity = null;
        }
    }

    @Override
    public void getAsset(boolean status, Asset asset) {

        splitStartTime(AppCommonMethods.get24HourTime(asset, 1) + "");
        mm = Integer.parseInt(month.trim());
        yr = Integer.parseInt(year.trim());
        ddy = Integer.parseInt(dd.trim());

        Random random = new Random();
//        int requestCode = Integer.parseInt(String.format("%02d", random.nextInt(10000)));
        Long code = asset.getId();

        int requestCode = code.intValue();
        PrintLogging.printLog("", "notificationRequestId-->>" + requestCode);
        //String requestCode = String.valueOf(asset.getExternalId());
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        myIntent = new Intent(getActivity(), MyReceiver.class);
        myIntent.putExtra(AppLevelConstants.ID, asset.getId());
        myIntent.putExtra(AppLevelConstants.Title, asset.getName());
        myIntent.putExtra(AppLevelConstants.DESCRIPTION, asset.getDescription());
        myIntent.putExtra(AppLevelConstants.SCREEN_NAME, AppLevelConstants.PROGRAM);
        myIntent.putExtra("requestcode", requestCode);
        myIntent.setAction("com.dialog.dialoggo.MyIntent");
        myIntent.setComponent(new ComponentName(getActivity().getPackageName(), "com.dialog.dialoggo.Alarm.MyReceiver"));

//                    Random random = new Random();
//                    int requestCode = Integer.parseInt(String.format("%02d", random.nextInt(10000)));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            Intent intent = new Intent();

            intent.putExtra(AppLevelConstants.ID, asset.getId());
            intent.putExtra(AppLevelConstants.Title, asset.getName());
            intent.putExtra(AppLevelConstants.DESCRIPTION, asset.getDescription());
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

        calendarToSchedule.set(yr, mm, ddy, Integer.parseInt(hour), Integer.parseInt(minute), 0);


        reminderDateTimeInMilliseconds = calendarToSchedule.getTimeInMillis();

        PrintLogging.printLog("", "valueIsform" + reminderDateTimeInMilliseconds);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(reminderDateTimeInMilliseconds, pendingIntent), pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, reminderDateTimeInMilliseconds, pendingIntent);
        }


    }

    @Override
    public void cancelReminder(Asset asset) {
        Long code = asset.getId();

        int requestCode = code.intValue();
        PrintLogging.printLog("", "notificationcancelRequestId-->>" + requestCode);


        if (pendingIntent != null) {
            pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(pendingIntent);
        } else {

            myIntent = new Intent(getActivity(), MyReceiver.class);
            myIntent.putExtra(AppLevelConstants.ID, asset.getId());
            myIntent.putExtra(AppLevelConstants.Title, asset.getName());
            myIntent.putExtra(AppLevelConstants.DESCRIPTION, asset.getDescription());
            myIntent.putExtra(AppLevelConstants.SCREEN_NAME, AppLevelConstants.PROGRAM);
            myIntent.putExtra("requestcode", requestCode);
            myIntent.setAction("com.dialog.dialoggo.MyIntent");
            myIntent.setComponent(new ComponentName(getActivity().getPackageName(), "com.dialog.dialoggo.Alarm.MyReceiver"));

            pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);

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


    private static class CustomRunnable implements Runnable {
        private final WeakReference<HorizontalScrollView> tvResendReference;
        private final WeakReference<LinearLayout> layoutWeakReference;
        private final WeakReference<Context> contextWeakReference;

        private CustomRunnable(LinearLayout castsContainer, HorizontalScrollView horizontalScrollView, BaseActivity baseActivity) {
            tvResendReference = new WeakReference<>(horizontalScrollView);
            layoutWeakReference = new WeakReference<>(castsContainer);
            contextWeakReference = new WeakReference<>(baseActivity);
        }

        @Override
        public void run() {
            HorizontalScrollView horizontalScrollView = tvResendReference.get();
            LinearLayout linearLayout = layoutWeakReference.get();
            Context context = contextWeakReference.get();
            if (horizontalScrollView != null && linearLayout != null) {
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                int deviceWidth = metrics.widthPixels;
                int deviceHeight = metrics.heightPixels;
                // ToastHandler.show(deviceWidth+"------"+deviceHeight+"",context);
                Log.w("deviceWidthdeviceWidth", deviceWidth + deviceHeight + "");
                int target;
                if (deviceWidth <= 1080) {
                    target = linearLayout.getChildAt(1).getLeft();
                } else if (context.getResources().getBoolean(R.bool.isTablet)) {
                    target = linearLayout.getChildAt(1).getLeft();

                } else {
                    target = linearLayout.getChildAt(0).getLeft();
                }
                //

                /*int targetX = target.getRight();
                int targetWidth = target.getWidth();
                int childrenInScreen = deviceWidth / targetWidth;
                int scrollToX = targetX -   // position 7th child from left less
                        ((childrenInScreen    // ( how many child contained in screen
                                * targetWidth) / 2)    // multiplied by their width ) divide by 2
                        + (targetWidth / 2);   // adding ( the child view divide by 2 )
                // autoscroll until the target x axis*/
                horizontalScrollView.smoothScrollTo(target, 0);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dialog.dialoggo.Alarm.MyReceiver");
        getActivity().registerReceiver(myReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            // getActivity().unregisterReceiver(myReceiver);
        } catch (IllegalArgumentException e) {

        } catch (Exception e) {

        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        resetAdapter();
    }


    private void callProgressBar() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (getBinding().includeProgressbar.progressBar.getVisibility() == View.VISIBLE) {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                } else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
                }

            }
        });
    }
}
