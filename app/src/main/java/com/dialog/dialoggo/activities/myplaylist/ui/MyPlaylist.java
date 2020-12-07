package com.dialog.dialoggo.activities.myplaylist.ui;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.myplaylist.adapter.MyPlaylistAdapter;
import com.dialog.dialoggo.activities.myplaylist.viewModel.MyPlaylistViewModel;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.callBacks.commonCallBacks.ItemClickListener;
import com.dialog.dialoggo.databinding.ActivityMyPlaylistBinding;
import com.dialog.dialoggo.fragments.dialog.AlertDialogSingleButtonFragment;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.kaltura.client.types.PersonalList;

import java.util.ArrayList;
import java.util.List;

public class MyPlaylist extends BaseBindingActivity<ActivityMyPlaylistBinding> implements ItemClickListener, AlertDialogSingleButtonFragment.AlertDialogListener {
    MyPlaylistAdapter searchNormalAdapter;
    ArrayList<RailCommonData> arrayList = new ArrayList<>();
    String partnerId = "", title = "";
    private MyPlaylistViewModel viewModel;
    private boolean mIsLoading = true;
    private boolean isScrolling = false;
    private int mScrollY, totalCOunt = 0;
    private List<Integer> list;

    private List<PersonalList> playlist = new ArrayList<>();

    private List<PersonalList> personalListsInChunks = new ArrayList<>();
    private List<PersonalList> personalLists = new ArrayList<>();
    private int counter = 1;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, firstVisiblePosition;

    @Override
    public ActivityMyPlaylistBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityMyPlaylistBinding.inflate(inflater);
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        partnerId = getIntent().getStringExtra("partnerIdType");
        title = getIntent().getStringExtra("personalListName");
        modelCall();
        UIinitialization();
        getBinding().swipeContainer.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.dark_gray_background));
        getBinding().swipeContainer.setColorSchemeColors(getResources().getColor(R.color.primary_blue));
        setSupportActionBar(getBinding().toolbar.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (arrayList != null)
            arrayList.clear();

        if (personalListsInChunks != null)
            personalListsInChunks.clear();
        if (personalLists != null)
            personalLists.clear();

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

    private void swipeToRefreshCheck() {
        if (getBinding().swipeContainer != null) {
            if (getBinding().swipeContainer.isRefreshing()) {
                getBinding().swipeContainer.setRefreshing(false);
            }
        }
    }

    private void swipeToRefresh() {
        if (getBinding().emptyImg.getVisibility() == View.VISIBLE) {
            getBinding().swipeContainer.setVisibility(View.GONE);
        } else {

            getBinding().swipeContainer.setOnRefreshListener(() -> {
                if (NetworkConnectivity.isOnline(MyPlaylist.this)) {
                    counter = 1;
                    personalLists.clear();
                    personalListsInChunks.clear();
                    arrayList.clear();

                    loadDataFromModel();
                } else {
                    swipeToRefreshCheck();
                    ToastHandler.show(getResources().getString(R.string.no_internet_connection), getApplicationContext());
                }
            });
        }

    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(MyPlaylistViewModel.class);
    }


    private void UIinitialization() {
        swipeToRefresh();

        getBinding().recyclerViewMore.hasFixedSize();
        getBinding().recyclerViewMore.setNestedScrollingEnabled(false);
        getBinding().recyclerViewMore.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }

    private void getWatchListDetail() {

        viewModel.getAllWatchlist(getAssetId(personalListsInChunks)).observe(this, railCommonData -> {
            if (railCommonData != null) {
                if (railCommonData.size() > 0) {
                    if (railCommonData.get(0).getStatus()) {
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

    private void loadDataFromModel() {
        getBinding().transparentLayout.setVisibility(View.VISIBLE);
        getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);

        viewModel.getWatchlistData(counter, partnerId).observe(this, commonResponse -> {
            swipeToRefreshCheck();
            if (commonResponse != null) {
                totalCOunt = commonResponse.getTotalCount();
                if (commonResponse.getPersonalLists() != null&&commonResponse.getPersonalLists().size()>0) {
                    PrintLogging.printLog(MyPlaylist.class, "MyPlaylist", "MyPlaylist" + commonResponse.getPersonalLists().size());

                    personalListsInChunks = commonResponse.getPersonalLists();
                    personalLists.addAll(commonResponse.getPersonalLists());
                    getWatchListDetail();
                    getBinding().emptyImg.setVisibility(View.GONE);

                } else {
                    getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                    if (counter == 1) {
                        getBinding().emptyImg.setVisibility(View.VISIBLE);
                        setSwipe();
                    }
                }
            }else {
                getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
                getBinding().emptyImg.setVisibility(View.VISIBLE);
            }

        });

    }

    private void setSwipe() {
        if (getBinding().emptyImg.getVisibility() == View.VISIBLE) {
            getBinding().swipeContainer.setVisibility(View.GONE);
        } else {
            swipeToRefresh();
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

        PrintLogging.printLog(MyPlaylist.class, "MyPlaylist", "MyPlaylist" + value);

        return value;
        //
        // loadWatchlistData(value);

    }

    private void setUIComponets(List<RailCommonData> railCommonData) {
        arrayList.addAll(railCommonData);

        if (!isScrolling) {
            setAdapter(arrayList);

            mIsLoading = searchNormalAdapter.getItemCount() != totalCOunt;
            getBinding().transparentLayout.setVisibility(View.GONE);
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);


        } else {
            mIsLoading = searchNormalAdapter.getItemCount() != totalCOunt;
            // searchNormalAdapter.notifyAdapter(railCommonData);
            searchNormalAdapter.notifyDataSetChanged();
            getBinding().transparentLayout.setVisibility(View.GONE);

            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);

        }

        setPagination();
    }

    private void setAdapter(ArrayList<RailCommonData> arrayList) {

        searchNormalAdapter = new MyPlaylistAdapter(MyPlaylist.this, arrayList, MyPlaylist.this);
        getBinding().recyclerViewMore.setAdapter(searchNormalAdapter);
    }

    private void setPagination() {


        getBinding().recyclerViewMore.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                try {

                    LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                    if (layoutManager != null)
                        firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                    if (dy > 0 && layoutManager != null) {
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                        if (mIsLoading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                mIsLoading = false;
                                counter++;
                                isScrolling = true;
                                mScrollY += dy;
                                connectionObserver();

                            }
                        }
                    }
                } catch (Exception e) {
                    PrintLogging.printLog("Exception", "", "" + e);
                }
            }
        });
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    @Override
    public void onClick(int position) {
        confirmDeletion(position);
    }


    private void confirmDeletion(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        builder.setTitle(getResources().getString(R.string.dialog));
        builder.setMessage(this.getResources().getString(R.string.remove_watchlist_item))
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, id) -> {
                    if (NetworkConnectivity.isOnline(getApplication())) {
                        removeAssetFrmWatchlist(position);
                    } else {
                        ToastHandler.show(getResources().getString(R.string.no_internet_connection), getApplicationContext());

                    }
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
        Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        bn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
        Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        bp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary_blue));
    }

    private void removeAssetFrmWatchlist(int position) {
        try {

            Long metaId = arrayList.get(position).getId();
            String metaKSQL = getIdTocompareWithWatchlist(metaId);
            for (int i = 0; i < personalLists.size(); i++) {
                if (personalLists.size() > 0) {
                    if (metaKSQL.equals(personalLists.get(i).getKsql())) {
                        Long pId = personalLists.get(i).getId();
                        deleteItemFromList(position, i, pId);
                    }
                }

            }
        } catch (Exception e) {

            PrintLogging.printLog("Exception", "", "" + e);
        }
    }

    private void deleteItemFromList(int position, int loopIndex, Long pID) {

        deleteWatchListItem(String.valueOf(pID), position, loopIndex);

    }


    private void deleteWatchListItem(final String pID, final int position, final int loopIndex) {
        viewModel.deleteWatchlist(pID).observe(MyPlaylist.this, aBoolean -> {
            if (aBoolean != null && aBoolean.getStatus()) {
                try {

                    counter = 1;
                    personalLists.clear();
                    personalListsInChunks.clear();
                    arrayList.clear();
                    setAdapter(new ArrayList<>());
                    loadDataFromModel();
                    ToastHandler.show(getApplicationContext().getResources().getString(R.string.playlist_removed), getApplicationContext());


                  /*  personalLists.remove(position);
                    arrayList.remove(position);
                    searchNormalAdapter.notifyItemRemoved(position);*/
                    /*if (arrayList.size() == 0) {
                        getBinding().emptyImg.setVisibility(View.VISIBLE);
                    }*/
                    //  counter = 1;
                    // loadDataFromModel();
                } catch (Exception e) {
                    PrintLogging.printLog("Exception", "", "" + e);
                }
            }else {
                showDialog(aBoolean.getMessage());
            }
        });
    }

    private String getIdTocompareWithWatchlist(Long idofasset) {
        String one = "media_id='";
        String two = String.valueOf(idofasset);
        String three = "'";
        return one.concat(two).concat(three);
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
}
