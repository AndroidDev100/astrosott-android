package com.astro.sott.fragments.episodeFrament;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.activities.webSeriesDescription.ui.WebSeriesDescriptionActivity;
import com.astro.sott.activities.webSeriesDescription.viewModel.WebSeriesDescriptionViewModel;
import com.astro.sott.baseModel.BaseBindingFragment;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.ContinueWatchingRemove;
import com.astro.sott.callBacks.commonCallBacks.EpisodeCallBAck;
import com.astro.sott.callBacks.commonCallBacks.EpisodeClickListener;
import com.astro.sott.callBacks.commonCallBacks.RemoveAdsCallBack;
import com.astro.sott.databinding.EpisodeFooterFragmentBinding;
import com.astro.sott.utils.TabsData;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.utils.userInfo.UserInfo;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.Bookmark;
import com.kaltura.client.types.MultilingualStringValueArray;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class EpisodesFragment extends BaseBindingFragment<EpisodeFooterFragmentBinding> implements ContinueWatchingRemove, RemoveAdsCallBack, EpisodeClickListener,EpisodeCallBAck {
    FirstEpisodeCallback _mClickListener;
    BottomSheetDialog dialog;
    TextView pause_download, resume_download, cancel_download, go_to_mydownload_lay;
    WebSeriesDescriptionViewModel viewModel;
    private List<AssetCommonBean> closedSeriesData;
    private StringBuilder listOfAsset;
    private List<RailCommonData> finalEpisodeList;
    Asset asset;
    LinearLayoutManager mLayoutManager;
    private int seasonNumber = 0;

    int counter = 1;
    private int selectedIndex = 0;
    private int totalPages;

    EpisodeAdapter adapter;
    List<RailCommonData> loadedList;
    ArrayList<RailCommonData> list;
    private android.app.AlertDialog alertDialog;
    private android.app.AlertDialog.Builder builder;

    private String seriesType = "";

    Timer timer;
    int clickedPosition = -1;
    int timeInterval = 4000;
    int typeOfDownload = 100;
    int activePosition = -1;
    String activeDownloadId = "";
    private Context context;
    private int layoutType;
    private String externalId = "";
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

    @Override
    public void episodeList(List<RailCommonData> railList) {
        ((WebSeriesDescriptionActivity) context).episodeCallback(railList);
    }

    class SeasonListAdapter extends RecyclerView.Adapter<SeasonListAdapter.ViewHolder> {
        private final List<Integer> list;
        private int selectedPos;
        private Context mContext;

        //TrackGroup list;
        public SeasonListAdapter(List<Integer> list, int selectedPos, Context context) {
            this.list = list;
            this.selectedPos = selectedPos;
            mContext = context;
        }

        @NonNull
        @Override
        public SeasonListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_season_listing, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            holder.season.setText(mContext.getResources().getString(R.string.season) + " " + list.get(position).toString());
            if (selectedIndex == position) {
                holder.season.setTextColor(mContext.getResources().getColor(R.color.moretitlecolor));
                //holder.season.setTextSize(getResources().getDimension(R.dimen.large_text_size));
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                holder.season.setTypeface(boldTypeface);
            } else {
                holder.season.setTextColor(mContext.getResources().getColor(R.color.white));
                //holder.season.setTextSize(getResources().getDimension(R.dimen.large_text_size));
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
                holder.season.setTypeface(boldTypeface);
            }

            holder.season.setOnClickListener(v -> {
                alertDialog.cancel();
                selectedIndex = position;
                seasonNumber = list.get(position);
                getBinding().seasonText.setText(getResources().getString(R.string.season) + " " + seasonNumber);
                getEpisodes(selectedIndex);

            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView season;

            public ViewHolder(View itemView) {
                super(itemView);
                season = itemView.findViewById(R.id.season_name);
            }
        }

    }

    class EpisodeListAdapter extends RecyclerView.Adapter<EpisodeListAdapter.ViewHolder> {
        private Context mContext;
        private int tOtalPages;
        private int startNo = 1;
        private int endNo = 20;
        private int totalData;

        //TrackGroup list;
        public EpisodeListAdapter(Context context, int counter, int total) {
            mContext = context;
            tOtalPages = counter;
            totalData = total;
        }

        @NonNull
        @Override
        public EpisodeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_season_listing, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            if (position + 1 == tOtalPages) {
                holder.season.setText("EPISODE " + startNo + " - " + totalData);
            } else {
                holder.season.setText("EPISODE " + startNo + " - " + endNo);
            }
            startNo += 20;
            endNo += 20;
            if (selectedIndex == position) {
                holder.season.setTextColor(mContext.getResources().getColor(R.color.moretitlecolor));
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                holder.season.setTypeface(boldTypeface);
            } else {
                holder.season.setTextColor(mContext.getResources().getColor(R.color.white));
                Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.NORMAL);
                holder.season.setTypeface(boldTypeface);
            }

            holder.season.setOnClickListener(v -> {
                alertDialog.cancel();
                selectedIndex = position;
                counter = position + 1;
                callEpisodes();
                getBinding().seasonText.setText(holder.season.getText());

            });
        }

        @Override
        public int getItemCount() {
            return tOtalPages;
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            TextView season;

            public ViewHolder(View itemView) {
                super(itemView);
                season = itemView.findViewById(R.id.season_name);
            }
        }

    }


    private void connectionValidation(Boolean aBoolean) {
        if (aBoolean == true) {
            if (asset.getExternalId() != null)
                externalId = asset.getExternalId();
            UIinitialization();

            if (TabsData.getInstance().getSeasonList() != null && TabsData.getInstance().getClosedSeriesData() != null) {
                getBinding().season.setVisibility(View.VISIBLE);
                seriesType = "closed";
                closedSeriesData = TabsData.getInstance().getClosedSeriesData();
                getBinding().seasonText.setText(closedSeriesData.get(0).getTitle());

                seriesNumberList = TabsData.getInstance().getSeasonList();
                _mClickListener.onFirstEpisodeData(TabsData.getInstance().getClosedSeriesData(), AppLevelConstants.CLOSE);
                setClosedUIComponets(TabsData.getInstance().getClosedSeriesData());
            } else {
                getOpenSeriesData();
            }
            //getSeasons();
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

        getBinding().season.setOnClickListener(view -> {
            if (seriesType.equalsIgnoreCase("closed")) {
                showSeasonList();
            } else if (seriesType.equalsIgnoreCase("open")) {
                showEpisodeList();
            }
        });

    }

    public void setRecyclerProperties(RecyclerView recyclerView) {
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        //   ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    public void getSeasons() {
        viewModel.getSeasonsListData(asset.getId().intValue(), 1, asset.getType(), asset.getMetas(), layoutType, externalId).observe(getActivity(), integers -> {
            if (integers != null) {
                if (integers.size() > 0) {
                    seriesNumberList = integers;
                    tabsVisibility(false);
                    callSeasonEpisodes(seriesNumberList);
                } else {
                    // tabsVisibility(true);
                }
            } else {
                getOpenSeriesData();

                // tabsVisibility(true);


            }

        });

    }

    private List<AssetCommonBean> openSeriesData;
    private int total;
    private double totalCount;

    private void getOpenSeriesData() {
        openSeriesData = TabsData.getInstance().getOpenSeriesData();
        totalCount = openSeriesData.get(0).getTotalCount();
        if (openSeriesData != null && openSeriesData.size() > 0) {
            double a = totalCount / 20;
            totalPages = (int) Math.ceil(a);
            seriesType = "open";
            getBinding().season.setVisibility(View.VISIBLE);
            total = openSeriesData.get(0).getTotalCount();
            if ((totalCount > 20)) {
                getBinding().seasonText.setText("EPISODE 1 - 20");
            } else {
                getBinding().seasonText.setText("EPISODE 1 - " + total);
            }
            getBinding().season.setEnabled(false);
            _mClickListener.onFirstEpisodeData(TabsData.getInstance().getOpenSeriesData(),AppLevelConstants.OPEN);
            setUIComponets(TabsData.getInstance().getOpenSeriesData());
        }

    }

    public void showSeasonList() {
        if (selectedIndex == -1) {
            return;
        }

        SeasonListAdapter listAdapter = new SeasonListAdapter(seriesNumberList, selectedIndex, context);
        builder = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View content = inflater.inflate(R.layout.season_custom_dialog, null);
        builder.setView(content);
        RecyclerView mRecyclerView = content.findViewById(R.id.my_recycler_view);
        ImageView imageView = content.findViewById(R.id.close);
        imageView.setOnClickListener(v -> {
            alertDialog.cancel();
        });

        //Creating Adapter to fill data in Dialog
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(listAdapter);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(ActivityCompat.getDrawable(getActivity(), R.color.black_transparent));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        alertDialog.show();
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(alertDialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT; // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        alertDialog.getWindow().setAttributes(lWindowParams);

    }

    public void showEpisodeList() {
        if (selectedIndex == -1) {
            return;
        }

        EpisodeListAdapter listAdapter = new EpisodeListAdapter(context, totalPages, total);
        builder = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View content = inflater.inflate(R.layout.season_custom_dialog, null);
        builder.setView(content);
        RecyclerView mRecyclerView = content.findViewById(R.id.my_recycler_view);
        ImageView imageView = content.findViewById(R.id.close);
        imageView.setOnClickListener(v -> {
            alertDialog.cancel();
        });

        //Creating Adapter to fill data in Dialog
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(listAdapter);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(ActivityCompat.getDrawable(getActivity(), R.color.black_transparent));
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        alertDialog.show();
        WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(alertDialog.getWindow().getAttributes());
        lWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT; // this is where the magic happens
        lWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        alertDialog.getWindow().setAttributes(lWindowParams);

    }

    public void tabsVisibility(boolean removetab) {

    }

    private void callSeasonEpisodes(List<Integer> seriesNumberList) {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        getBinding().loadMoreTxt.setText(getActivity().getResources().getString(R.string.loading));
        viewModel.callSeasonEpisodes(asset, asset.getType(), counter, seriesNumberList, seasonCounter, layoutType, TabsData.getInstance().getSortType()).observe(this, assetCommonBeans -> {
            getBinding().loadMoreTxt.setText("Load More");
            getBinding().progressBar.setVisibility(View.GONE);

            if (assetCommonBeans.get(0).getStatus()) {
                getBinding().retryTxt.setVisibility(View.GONE);
                getBinding().seasonText.setText(assetCommonBeans.get(0).getTitle());
                getBinding().season.setVisibility(View.VISIBLE);
                getBinding().recyclerView.setVisibility(View.VISIBLE);
                setClosedUIComponets(assetCommonBeans);
            } else {
            }

        });


    }

    private void callEpisodes() {
        getBinding().progressBar.setVisibility(View.VISIBLE);
        viewModel.callEpisodes(asset, asset.getType(), counter, seasonCounter, layoutType, TabsData.getInstance().getSortType()).observe(this, assetCommonBeans -> {
            getBinding().progressBar.setVisibility(View.GONE);
            if (listOfAsset != null)
                listOfAsset.setLength(0);
            if (assetCommonBeans.get(0).getStatus()) {
                //  _mClickListener.onFirstEpisodeData(assetCommonBeans);
                setUIComponets(assetCommonBeans);
                //callSeasonEpisodes(seriesNumberList);
            } else {
                //getBinding().retryTxt.setVisibility(View.VISIBLE);
            }

        });
        getBinding().retryTxt.setOnClickListener(view -> {
            //  getSeasons();
        });

    }

    private void setUIComponets(List<AssetCommonBean> assetCommonBeans) {
        try {
            loadedList = assetCommonBeans.get(0).getRailAssetList();
            list = new ArrayList<>();
            for (int i = 0; i < loadedList.size(); i++) {
                list.add(i, loadedList.get(i));
            }
            if (UserInfo.getInstance(getActivity()).isActive()) {
                getAssetListForProgress(loadedList);
            } else {
                setOpenSeriesAdapter(loadedList);
            }
/* if (count >= assetCommonBeans.get(0).getTotalCount()) {
            getBinding().loadMoreButton.setVisibility(View.GONE);
        } else {
            getBinding().loadMoreButton.setVisibility(View.VISIBLE);
        }*/

        } catch (Exception e) {

            PrintLogging.printLog("ExceptionloadMore", "" + e);
        }

    }

    int loadListSize = 0;

    private void getAssetListForProgress(List<RailCommonData> loadedList) {

        listOfAsset = new StringBuilder();
        try {
            for (RailCommonData railCommonData : loadedList) {
                if (railCommonData.getObject() != null && railCommonData.getObject().getId() != null) {
                    loadListSize++;
                    if (loadedList.size() != loadListSize) {
                        listOfAsset.append(railCommonData.getObject().getId() + ",");
                    } else {
                        listOfAsset.append(railCommonData.getObject().getId() + "");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        getProgressList();
    }

    private void getProgressList() {
        viewModel.getEpisodeProgress(listOfAsset.toString()).observe(this, bookmarkList -> {
            if (bookmarkList != null) {
                matchBookMarkList(bookmarkList);
            } else {
                if (seriesType.equalsIgnoreCase("open")) {
                    setOpenSeriesAdapter(loadedList);
                } else {
                    setCLosedSeriesAdapter(loadedList);
                }
            }
        });
    }

    private void matchBookMarkList(List<Bookmark> bookmarkList) {
        finalEpisodeList = new ArrayList<>();
        for (RailCommonData railCommonData : loadedList) {
            RailCommonData finalEpisode = new RailCommonData();
            finalEpisode.setObject(railCommonData.getObject());
            for (Bookmark bookmark : bookmarkList) {
                if (bookmark.getId().equalsIgnoreCase(railCommonData.getId().toString())) {
                    finalEpisode.setProgress(bookmark.getPosition());
                }
            }
            finalEpisodeList.add(finalEpisode);
        }
        if (seriesType.equalsIgnoreCase("open")) {
            setOpenSeriesAdapter(finalEpisodeList);
        } else {
            setCLosedSeriesAdapter(finalEpisodeList);
        }
    }

    private void setCLosedSeriesAdapter(List<RailCommonData> finalEpisodeList) {
        checkExpiry(list);
        adapter = new EpisodeAdapter(getActivity(), finalEpisodeList, getArguments().getInt(AppConstants.EPISODE_NUMBER), this,this);
        getBinding().recyclerView.setAdapter(adapter);

        int count = adapter.getItemCount();
        if (count >= totalCount) {
            getBinding().loadMoreButton.setVisibility(View.GONE);
        } else {
            getBinding().loadMoreButton.setVisibility(View.VISIBLE);
        }

    }

    private void setOpenSeriesAdapter(List<RailCommonData> finalEpisodeList) {
        adapter = new EpisodeAdapter(getActivity(), finalEpisodeList, getArguments().getInt(AppConstants.EPISODE_NUMBER), this,this);
        getBinding().recyclerView.setAdapter(adapter);

        if (seriesType.equalsIgnoreCase("open")) {
            if (totalPages > 1)
                getBinding().season.setEnabled(true);
        }

    }

    private void setClosedUIComponets(List<AssetCommonBean> assetCommonBeans) {
        try {
            loadedList.addAll(assetCommonBeans.get(0).getRailAssetList());
            list = new ArrayList<>();
            for (int i = 0; i < loadedList.size(); i++) {
                list.add(i, loadedList.get(i));
            }
            totalCount = assetCommonBeans.get(0).getTotalCount();
            if (UserInfo.getInstance(getActivity()).isActive()) {
                getAssetListForProgress(loadedList);
            } else {
                setCLosedSeriesAdapter(loadedList);
            }

          /*  checkExpiry(list);
            adapter = new EpisodeAdapter(getActivity(), loadedList, getArguments().getInt(AppConstants.EPISODE_NUMBER), this);
            getBinding().recyclerView.setAdapter(adapter);

            int count = adapter.getItemCount();
            if (count >= assetCommonBeans.get(0).getTotalCount()) {
                getBinding().loadMoreButton.setVisibility(View.GONE);
            } else {
                getBinding().loadMoreButton.setVisibility(View.VISIBLE);
            }*/

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
        counter = 1;
        adapter = null;
        seasonCounter = selectedIndex;
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

    @Override
    public void onClick(int position) {

    }

    @Override
    public void moveToPlay(int position, RailCommonData railCommonData, int type,List<RailCommonData> railList) {
        ((WebSeriesDescriptionActivity) context).moveToPlay(position, railCommonData, type,railList);

    }




    public interface FirstEpisodeCallback {
        public void onFirstEpisodeData(List<AssetCommonBean> railCommonData, String open);
    }


}
