package com.astro.sott.fragments.episodeFrament;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.astro.sott.R;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.activities.webSeriesDescription.viewModel.WebSeriesDescriptionViewModel;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.ContinueWatchingRemove;
import com.astro.sott.callBacks.commonCallBacks.RemoveAdsCallBack;
import com.astro.sott.databinding.EpisodeFooterFragmentBinding;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.kaltura.android.exoplayer2.util.Log;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MultilingualStringValueArray;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

public class EpisodesFragment extends BaseBindingFragment<EpisodeFooterFragmentBinding> implements ContinueWatchingRemove, RemoveAdsCallBack {
    FirstEpisodeCallback _mClickListener;
    BottomSheetDialog dialog;
    TextView pause_download, resume_download, cancel_download, go_to_mydownload_lay;
    long estimatedSize;
    WebSeriesDescriptionViewModel viewModel;
    Asset asset;
    LinearLayoutManager mLayoutManager;
    int counter = 1;
    EpisodeAdapter adapter;
    List<RailCommonData> loadedList;
    ArrayList<RailCommonData> list;
    Timer timer;
    int clickedPosition = -1;
    int interval = 0;
    int timeInterval = 4000;
    int typeOfDownload = 100;
    int activePosition = -1;
    String activeDownloadId = "";
    private Context context;
    private int layoutType;
    private Map<String, MultilingualStringValueArray> map;
    private List<Integer> seriesNumberList;
    private int seasonCounter = 0;

    @Override
    protected EpisodeFooterFragmentBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return EpisodeFooterFragmentBinding.inflate(inflater);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            _mClickListener = (FirstEpisodeCallback) activity;
            this.context = activity;
        } catch (ClassCastException e) {
            this.context = activity;
            throw new ClassCastException(activity.toString() + " must implement onViewSelected");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            loadedList = new ArrayList<>();

            int seasonNumber = getArguments().getInt(AppConstants.KEY_SEASON_NUMBER);
            if (seasonNumber > 0) {
                seasonCounter = seasonNumber - 1;
            }
            modelCall();
            getVideoRails();
        } catch (Exception e) {
        }
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(WebSeriesDescriptionViewModel.class);

    }

    public void getVideoRails() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            asset = bundle.getParcelable("ASSET_OBJ");
            layoutType = AppConstants.Rail5;

            if (asset != null) {
                map = asset.getTags();
            }
            connectionObserver();

        }
    }

    private void connectionObserver() {
        if (NetworkConnectivity.isOnline(getActivity())) {
            connectionValidation(true);
        } else {

            connectionValidation(false);
        }
    }

    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean == true) {
            getSeasons();
            UIinitialization();
        } else {
        }
    }

    private void UIinitialization() {
        setRecyclerProperties(getBinding().recyclerView);
        getBinding().loadMoreButton.setVisibility(View.GONE);
        getBinding().loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                callSeasonEpisodes(seriesNumberList);
            }
        });


    }

    public void setRecyclerProperties(RecyclerView recyclerView) {
       /* recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);*/
        mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
     //   ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    public void getSeasons() {
        viewModel.getSeasonsListData(asset.getId().intValue(), 1, asset.getType(), asset.getMetas(), layoutType, asset.getType()).observe(getActivity(), integers -> {
            if (integers != null) {
                if (integers.size() > 0) {
                    seriesNumberList = integers;
                    tabsVisibility(false);
                    callSeasonEpisodes(seriesNumberList);
                } else {

                    getBinding().upcoming.setVisibility(View.VISIBLE);
                    getBinding().recyclerView.setVisibility(View.GONE);
                    // tabsVisibility(true);
                }
            } else {

                getBinding().upcoming.setVisibility(View.VISIBLE);
                getBinding().recyclerView.setVisibility(View.GONE);
               // tabsVisibility(true);


            }

        });

    }

    public void tabsVisibility(boolean removetab) {

    }

    private void callSeasonEpisodes(List<Integer> seriesNumberList) {
        getBinding().loadMoreTxt.setText(getActivity().getResources().getString(R.string.loading));
        viewModel.callSeasonEpisodes(asset.getMetas(), asset.getType(), counter, seriesNumberList, seasonCounter, layoutType).observe(this, assetCommonBeans -> {
            getBinding().loadMoreTxt.setText("Load More");
            if (assetCommonBeans.get(0).getStatus()) {
                getBinding().retryTxt.setVisibility(View.GONE);
                getBinding().recyclerView.setVisibility(View.VISIBLE);
              //  _mClickListener.onFirstEpisodeData(assetCommonBeans);
                setUIComponets(assetCommonBeans);
                //callSeasonEpisodes(seriesNumberList);
            } else {
                getBinding().retryTxt.setVisibility(View.VISIBLE);
            }

        });
        getBinding().retryTxt.setOnClickListener(view -> {
            getSeasons();
        });

    }

    private void setUIComponets(List<AssetCommonBean> assetCommonBeans) {
        try {
            loadedList.addAll(assetCommonBeans.get(0).getRailAssetList());
            list = new ArrayList<>();
            for (int i = 0; i < loadedList.size(); i++) {
                list.add(i, loadedList.get(i));
            }

            checkExpiry(list);

            adapter = new EpisodeAdapter(getActivity(), list, getArguments().getInt(AppConstants.EPISODE_NUMBER));
            getBinding().recyclerView.setAdapter(adapter);

            int count = adapter.getItemCount();
            if (count >= assetCommonBeans.get(0).getTotalCount()) {
                getBinding().loadMoreButton.setVisibility(View.GONE);
            } else {
                getBinding().loadMoreButton.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {

            PrintLogging.printLog("ExceptionloadMore", "" + e);
        }

    }

    private void checkExpiry(ArrayList<RailCommonData> list) {


    }

    private void hitApiRecommendationRail() {

    }

    @Override
    public void remove(Long assetID, int position, int continueWatchingIndex, int listSize) {

    }

    @Override
    public void removeAdOnFailure(int position) {

    }



   /* @Override
    public void moveToPlay(int position, RailCommonData railCommonData, int type) {
        if (NetworkConnectivity.isOnline(getActivity())) {
            ((WebSeriesDescriptionActivity) context).moveToPlay(position, railCommonData, type);
            DTGManager dtgManager = DTGManager.getInstance().setAsset(null);
            try {
                dtgManager.registerAsset(railCommonData.getObject().getId().toString(), new PKMediaListener() {
                    @Override
                    public void isAssetRegistered(boolean status) {
                        try {
                        } catch (Exception ignore) {

                        }
                    }
                });
            } catch (Exception ex) {

            }
        } else {
            DownloadItem downloadItem = DTGManager.getInstance().getContentManager().findItem(railCommonData.getObject().getId().toString());
            if (downloadItem != null) {
                if (downloadItem.getState().name().equalsIgnoreCase(DownloadState.COMPLETED.toString())) {
                    Intent intent = new Intent(getActivity(), DTGPlayerActivity.class);
                    intent.putExtra("ASSET_ID", downloadItem.getItemId());
                    startActivity(intent);
                } else {
                    ToastHandler.show(getResources().getString(R.string.no_internet_connection), ApplicationMain.getAppContext());
                }
            } else {
                ToastHandler.show(getResources().getString(R.string.no_internet_connection), ApplicationMain.getAppContext());
            }
        }
    }
*/
    /*@Override
    public void downloadClick(int position, RailCommonData railCommonData) {
        this.clickedPosition = position;
        DownloadItem downloadItem = DTGManager.getInstance().getContentManager().findItem(railCommonData.getObject().getId().toString());
        if (downloadItem != null) {
            if (downloadItem.getState().name().equalsIgnoreCase(DownloadState.IN_PROGRESS.toString())) {
                resume_download.setVisibility(View.GONE);
                pause_download.setVisibility(View.VISIBLE);
                cancel_download.setVisibility(View.VISIBLE);
                dialog.show();
            } else if (downloadItem.getState().name().equalsIgnoreCase(DownloadState.PAUSED.toString())) {
                pause_download.setVisibility(View.GONE);
                resume_download.setVisibility(View.VISIBLE);
                cancel_download.setVisibility(View.VISIBLE);
                dialog.show();
            } else if (downloadItem.getState().name().equalsIgnoreCase(DownloadState.COMPLETED.toString())) {
                pause_download.setVisibility(View.GONE);
                resume_download.setVisibility(View.GONE);
                cancel_download.setVisibility(View.VISIBLE);
                cancel_download.setText("Remove Download");
                dialog.show();
            } else if (downloadItem.getState().name().equalsIgnoreCase(DownloadState.FAILED.toString())) {
                pause_download.setVisibility(View.GONE);
                resume_download.setVisibility(View.GONE);
                cancel_download.setVisibility(View.VISIBLE);
                cancel_download.setText("Remove Download");
                dialog.show();
            } else if (downloadItem.getState().name().equalsIgnoreCase(DownloadState.INFO_LOADED.toString())) {
                pause_download.setVisibility(View.GONE);
                resume_download.setVisibility(View.GONE);
                cancel_download.setVisibility(View.VISIBLE);
                cancel_download.setText("Remove Download");
                dialog.show();
            } else if (downloadItem.getState().name().equalsIgnoreCase(DownloadState.NEW.toString())) {
                pause_download.setVisibility(View.GONE);
                resume_download.setVisibility(View.GONE);
                cancel_download.setVisibility(View.VISIBLE);
                cancel_download.setText("Remove Download");
                dialog.show();
            } else {
                DTGManager.getInstance().cancel(railCommonData.getObject().getId() + "");
                DTGManager.getInstance().removeFromDatabase(railCommonData.getObject().getId() + "");
                notifyAdapterEverySeconds();
                activePosition = -1;
                ((WebSeriesDescriptionActivity) context).itemRemovedFromDB(position, railCommonData);
                ((WebSeriesDescriptionActivity) context).downloadClick(position, railCommonData);
            }
        } else {
            ((WebSeriesDescriptionActivity) context).downloadClick(position, railCommonData);
        }

        pause_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DTGManager.getInstance().pause(railCommonData.getObject().getId() + "");
                dialog.dismiss();
                try {
                    dtgSharedPrefHelper.setString("PAUSED_ID" + railCommonData.getObject().getId(), railCommonData.getObject().getId() + "");
                } catch (Exception ignored) {

                }
            }
        });

        resume_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean wifiConnected = NetworkConnectivity.getwifi(getActivity());
                boolean connectionPreference = new KsPreferenceKeys(getActivity()).getDownloadOverWifi();
                if (connectionPreference && !wifiConnected) {
                    showWifiDialog(railCommonData.getObject());
                } else {
                    resumeDownload(railCommonData.getObject());
                }
            }
        });

        cancel_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                DTGManager.getInstance().cancel(railCommonData.getObject().getId() + "");
                DTGManager.getInstance().removeFromDatabase(railCommonData.getObject().getId() + "");
                typeOfDownload = 1;
                activePosition = -1;
                adapter.updateIcons(0, 1);
                downloadCompleted(null);
                ((WebSeriesDescriptionActivity) context).itemRemovedFromDB(position, railCommonData);
            }
        });

        go_to_mydownload_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                new ActivityLauncher(getActivity()).myDownloadsActivity(getActivity(), MyDownloadsActivity.class);
            }
        });

    }*/

    public void getEpisodes(int seasonNumber) {
        loadedList.clear();
        adapter = null;
        seasonCounter = seasonNumber;
        callSeasonEpisodes(seriesNumberList);
    }

    //typeofdownload 1=single download 0=series download
    public void itemAddedForDownload(int typeOfDownload) {
        try {
            if (typeOfDownload == 2) {
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
                if (adapter != null) {
                    adapter.updateIcons(0, 100);
                    adapter.notifyDataSetChanged();
                }
            } else if (typeOfDownload == 1) {
                if (adapter != null) {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    adapter.updateIcons(0, 101);
                    adapter.notifyItemChanged(clickedPosition);
                    this.typeOfDownload = 1;
                }
            }
        } catch (Exception e) {

        }
    }

    private void initiateTimer() {
        if (timer == null) {
            timer = new Timer();
            final TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (getActivity() != null && !((Activity) getActivity()).isFinishing()) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                notifyAdapterPositionEverySeconds();
                            }
                        });
                    }
                }
            };

            timer.scheduleAtFixedRate(task, 0, timeInterval);
        }

    }

    private void notifyAdapterPositionEverySeconds() {
        if (activePosition == -1) {
            for (int i = 0; i < loadedList.size(); i++) {
                Asset asset = loadedList.get(i).getObject();
                if (!activeDownloadId.equalsIgnoreCase("")) {
                    if (activeDownloadId.equalsIgnoreCase(asset.getId().toString())) {
                        activePosition = i;
                        break;
                    }
                }
            }
        }

        PrintLogging.printLog("", "activePosition-->>" + activePosition);
        if (activePosition != -1) {
            if (getActivity() != null && !((Activity) getActivity()).isFinishing()) {
                if (adapter != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemChanged(activePosition);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } catch (Exception e) {

        }
    }

    private void notifyAdapterEverySeconds() {
        if (getActivity() != null && !((Activity) getActivity()).isFinishing()) {
            if (adapter != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    public void showWifiDialog(Asset downloadItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.wifi_dialog, null);
        TextView btn_update = (TextView) view.findViewById(R.id.btn_update);
        Switch switch_download = (Switch) view.findViewById(R.id.switch_download);

        builder.setView(view);
        builder.setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();

        if (!new KsPreferenceKey(getActivity()).getDownloadOverWifi()) {
            switch_download.setChecked(false);
        } else {
            switch_download.setChecked(true);
        }

        /*switch_download.setOnClickListener(v -> {

            if (switch_download.isChecked()) {
                new KsPreferenceKeys(getActivity()).setDownloadOverWifi(true);
            } else {
                new KsPreferenceKeys(getActivity()).setDownloadOverWifi(false);
            }
        });*/

        switch_download.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switch_download.isChecked()) {
                    new KsPreferenceKey(getActivity()).setDownloadOverWifi(true);
                } else {
                    new KsPreferenceKey(getActivity()).setDownloadOverWifi(false);
                }
            }
        });


    }


    public interface FirstEpisodeCallback {
        public void onFirstEpisodeData(List<AssetCommonBean> railCommonData);
    }


}
