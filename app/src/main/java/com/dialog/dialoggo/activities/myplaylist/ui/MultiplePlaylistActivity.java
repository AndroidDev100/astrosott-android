package com.dialog.dialoggo.activities.myplaylist.ui;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.myplaylist.adapter.MultiplePlaylistListingAdapter;
import com.dialog.dialoggo.activities.myplaylist.viewModel.MyPlaylistViewModel;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.beanModel.login.CommonResponse;
import com.dialog.dialoggo.callBacks.commonCallBacks.PlaylistCallback;
import com.dialog.dialoggo.databinding.ActivityMultiplePlaylistBinding;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.kaltura.client.types.PersonalList;

import java.util.ArrayList;
import java.util.List;

public class MultiplePlaylistActivity extends BaseBindingActivity<ActivityMultiplePlaylistBinding> implements PlaylistCallback {
    int countLoop;
    List<Integer> list;
    private MyPlaylistViewModel viewModel;
    private List<PersonalList> personalLists = new ArrayList<>();
    private List<Integer> totalCountlist;
    private int counter = 1;
    private List<RailCommonData> railCommonDataList;
    private String partnerId = "";
    private List<PersonalList> playlist = new ArrayList<>();
    private List<PersonalList> finalpersonalList = new ArrayList<>();


    @Override
    protected ActivityMultiplePlaylistBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityMultiplePlaylistBinding.inflate(inflater);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        modelCall();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //we can even intialise it in UIinitialization rather then checking and clearing
        if (personalLists != null) {
            personalLists.clear();
        }
        if (playlist != null) {
            playlist.clear();
        }
        if (finalpersonalList != null) {
            finalpersonalList.clear();
        }
        partnerId = "";
        UIinitialization();
        getBinding().swipeContainer.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.dark_gray_background));
        getBinding().swipeContainer.setColorSchemeColors(getResources().getColor(R.color.primary_blue));
        setSupportActionBar(getBinding().toolbar.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.my_playlist));

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        connectionObserver();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UIinitialization() {


        swipeToRefresh();

        getBinding().recyclerViewMore.hasFixedSize();
        getBinding().recyclerViewMore.setNestedScrollingEnabled(false);
        getBinding().recyclerViewMore.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(MyPlaylistViewModel.class);
    }

    private void swipeToRefreshCheck() {
        if (getBinding().swipeContainer != null) {
            if (getBinding().swipeContainer.isRefreshing()) {
                getBinding().swipeContainer.setRefreshing(false);
            }
        }
    }


    private void loadDataFromModel() {

        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
//        viewModel.getAllWatchlist().observe(this, new Observer<List<RailCommonData>>() {
//            @Override
//            public void onChanged(@Nullable List<RailCommonData> railCommonData) {
//                if (railCommonData.size()>0){
//                    if (railCommonData.get(0).getStatus()==true){
//                        swipeToRefreshCheck();
//                        getBinding().emptyImg.setVisibility(View.GONE);
//                        setUIComponets(railCommonData);
//                    }else {
//                        swipeToRefreshCheck();
//                        getBinding().emptyImg.setVisibility(View.VISIBLE);
//                    }
//                }else {
//
//                    getBinding().emptyImg.setVisibility(View.VISIBLE);
//                }
//            }
//        });

        viewModel.getWatchlistData(counter, partnerId).observe(this, commonResponse -> {
            swipeToRefreshCheck();
            if (commonResponse != null) {
                if (commonResponse.getPersonalLists() != null) {

                    personalLists = commonResponse.getPersonalLists();

                    for (int i = 0; i < personalLists.size(); i++) {
                        if (playlist.isEmpty()) {
                            playlist.add(personalLists.get(i));
                        } else {
                            if (playlist.size() > 0) {
                                list = new ArrayList<>();
                                int size = playlist.size();
                                for (int k = 0; k < size; k++) {
                                    list.add(playlist.get(k).getPartnerListType());
                                }

                                if (list.contains(personalLists.get(i).getPartnerListType())) {

                                } else {
                                    playlist.add(personalLists.get(i));
                                }

                            }
                        }
                    }
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getDetail();
                        }
                    }, 1000);



                    /*getBinding().recyclerViewMore.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    MultiplePlaylistListingAdapter multiplePlaylistAdapter = new MultiplePlaylistListingAdapter(playlist, MultiplePlaylistActivity.this, this);
                    getBinding().recyclerViewMore.setAdapter(multiplePlaylistAdapter);*/


                  /*  personalListsInChunks = commonResponse.getPersonalLists();
                    personalLists.addAll(commonResponse.getPersonalLists());
                    getWatchListDetail();
                    getBinding().emptyImg.setVisibility(View.GONE);*/

                } else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    getBinding().emptyImg.setVisibility(View.VISIBLE);
                    setSwipe();

                }
            }else {
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                getBinding().emptyImg.setVisibility(View.VISIBLE);
            }

        });

    }

    private void getDetail() {

        viewModel.getAllWatchlist(getAssetId(playlist)).observe(this, railCommonData -> {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            if (railCommonData != null) {
                if (railCommonData.size() > 0) {
                    if (railCommonData.get(0).getStatus()) {
                        //     getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);

                        swipeToRefreshCheck();
                        getBinding().emptyImg.setVisibility(View.GONE);
                        setUIComponets(railCommonData);
                    } else {
                        //swipeToRefreshCheck();
                        getBinding().emptyImg.setVisibility(View.VISIBLE);
                        setSwipe();
                    }
                } else {

                    getBinding().emptyImg.setVisibility(View.VISIBLE);
                    setSwipe();
                }
            }
        });
    }

    private void setUIComponets(List<RailCommonData> railCommonData) {
        railCommonDataList = new ArrayList<>();


        CommonResponse commonResponse = new CommonResponse();
        for (int k = 0; k < playlist.size(); k++)
            PrintLogging.printLog(MultiplePlaylistActivity.class, "", playlist.get(k).getPartnerListType() + "kadklnfjkds");
        //  commonResponse.setPersonalLists(playlist);
        for (int i = 0; i < playlist.size(); i++) {
            String ksql = playlist.get(i).getKsql();
            String[] arr = ksql.split("'");
            String two = arr[1];
            for (int j = 0; j < railCommonData.size(); j++) {
                if (two.equalsIgnoreCase(railCommonData.get(j).getId().toString())) {
                    finalpersonalList.add(playlist.get(i));
                    railCommonDataList.add(railCommonData.get(j));
                    break;
                }
            }

        }
        //Collections.reverse(railCommonData);
        commonResponse.setPersonalLists(finalpersonalList);
        commonResponse.setRailCommonData(railCommonDataList);
        totalCountlist = new ArrayList<>();

        getVideoCount(commonResponse, 0);


    }

    private void getVideoCount(CommonResponse commonResponse, int count) {
        countLoop = count;
        if (count < playlist.size()) {
            PrintLogging.printLog(MultiplePlaylistActivity.class, "", playlist.get(count).getPartnerListType().toString() + "---------" + "playlist count");

            viewModel.getWatchlistData(counter, playlist.get(count).getPartnerListType().toString()).observe(this, commonResponse1 -> {
                if (commonResponse != null)

                    totalCountlist.add(commonResponse1.getTotalCount());
                countLoop++;
                getVideoCount(commonResponse, countLoop);

            });
        } else {
            if (playlist.size() == totalCountlist.size()) {
                commonResponse.setList(totalCountlist);
                PrintLogging.printLog(MultiplePlaylistActivity.class, "commonResponse", "" + commonResponse.getRailCommonData().size());

                getBinding().recyclerViewMore.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
                MultiplePlaylistListingAdapter multiplePlaylistAdapter = new MultiplePlaylistListingAdapter(commonResponse, MultiplePlaylistActivity.this, this);
                getBinding().recyclerViewMore.setAdapter(multiplePlaylistAdapter);
            } else {
                PrintLogging.printLog(MultiplePlaylistActivity.class, "", "");
            }
        }
    }


    private String getAssetId(List<PersonalList> personalLists) {
        int size = personalLists.size();
        String value;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String ksql = personalLists.get(i).getKsql();
            String[] arr = ksql.split("'");
            String one = arr[0];
            String two = arr[1];
            builder.append(two).append(",");
        }
        value = builder.toString();
        if (value.length() > 0)
            value = value.substring(0, value.length() - 1);
        return value;


    }

    private void setSwipe() {
        if (getBinding().emptyImg.getVisibility() == View.VISIBLE) {
            getBinding().swipeContainer.setVisibility(View.GONE);
        } else {
            swipeToRefresh();
        }
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
            getBinding().noConnectionLayout.setVisibility(View.GONE);

            loadDataFromModel();

        } else {
            noConnectionLayout();
        }


    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    private void swipeToRefresh() {
        if (getBinding().emptyImg.getVisibility() == View.VISIBLE) {
            getBinding().swipeContainer.setVisibility(View.GONE);
        } else {

            getBinding().swipeContainer.setOnRefreshListener(() -> {
                if (NetworkConnectivity.isOnline(MultiplePlaylistActivity.this)) {
                    counter = 1;
                    partnerId = "";
                    personalLists.clear();
                    if (list != null)
                        list.clear();
                    playlist.clear();
                    finalpersonalList.clear();
                    loadDataFromModel();
                } else {
                    swipeToRefreshCheck();
                    ToastHandler.show(getResources().getString(R.string.no_internet_connection), getApplicationContext());
                }
            });
        }

    }

    @Override
    public void onClick(String name, int id) {
        partnerId = id + "";
        new ActivityLauncher(MultiplePlaylistActivity.this).playlistActivity(MultiplePlaylistActivity.this, MyPlaylist.class, partnerId, name);
    }
}
