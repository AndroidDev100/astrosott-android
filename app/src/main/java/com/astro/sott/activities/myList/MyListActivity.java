package com.astro.sott.activities.myList;

import androidx.lifecycle.ViewModelProviders;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.astro.sott.activities.deviceMangment.helper.RecyclerTouchListener;
import com.astro.sott.activities.moreListing.ui.ListingActivityNew;
import com.astro.sott.activities.myList.adapter.MyWatchlistAdapter;
import com.astro.sott.activities.myList.viewModel.MyWatchlistViewModel;
import com.astro.sott.baseModel.BaseBindingActivity;
import com.astro.sott.callBacks.commonCallBacks.ClickListener;
import com.astro.sott.callBacks.commonCallBacks.ItemClickListener;
import com.astro.sott.databinding.ActivityMyWatchlistBinding;
import com.astro.sott.utils.helpers.GridSpacingItemDecoration;
import com.kaltura.client.types.PersonalList;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.fragments.dialog.AlertDialogFragment;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.ProgressHandler;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.helpers.ToolBarHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyListActivity extends BaseBindingActivity<ActivityMyWatchlistBinding>{
    private final ArrayList<RailCommonData> arrayList = new ArrayList<>();
    int type;
    private MyWatchlistViewModel viewModel;
    private List<PersonalList> personalListsInChunks = new ArrayList<>();
    private boolean mIsLoading = true;
    private boolean isScrolling = false;
    private List<PersonalList> personalLists = new ArrayList<>();
    private MyWatchlistAdapter searchNormalAdapter;
    private int count = 1, totalCOunt = 0, counter = 1;
    private int pastVisiblesItems, visibleItemCount, totalItemCount, firstVisiblePosition;
    private int itemPosition;


    @Override
    public ActivityMyWatchlistBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityMyWatchlistBinding.inflate(inflater);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  new ToolBarHandler(this).myWatchlistAction(getBinding());
        connectionObserver();
/*

        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.LEVEL, 9);
        eventValue.put(AFInAppEventParameterName.SCORE, 100);
        AppsFlyerLib.getInstance().trackEvent(this, AFInAppEventType.LEVEL_ACHIEVED, eventValue);*/

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
            modelCall();
            UIinitialization();
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
                if (NetworkConnectivity.isOnline(MyListActivity.this)) {
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
        viewModel = ViewModelProviders.of(this).get(MyWatchlistViewModel.class);
    }

    int spacing;
    int spanCount;
    private void UIinitialization() {
        swipeToRefresh();
        getBinding().recyclerViewMore.addOnItemTouchListener(new RecyclerTouchListener(this, getBinding().recyclerViewMore, new ClickListener() {


            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                PrintLogging.printLog("", "confirmDeletion  " + position);

                itemPosition = position;
                //showAlertDialog(getResources().getString(R.string.remove_watchlist_item));
//                confirmDeletion(position);


            }
        }));
        spanCount = AppConstants.SPAN_COUNT_LANDSCAPE;
        Resources r = getResources();
        spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstants.LANSCAPE_SPACING, r.getDisplayMetrics());

        getBinding().recyclerViewMore.hasFixedSize();
        getBinding().recyclerViewMore.setNestedScrollingEnabled(false);
        getBinding().recyclerViewMore.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, true));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyListActivity.this, spanCount);
        getBinding().recyclerViewMore.setLayoutManager(gridLayoutManager);
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
        viewModel.getWatchlistData(counter).observe(this, commonResponse -> {
            swipeToRefreshCheck();
            if (commonResponse != null) {
                totalCOunt = commonResponse.getTotalCount();
                if (commonResponse.getPersonalLists() != null&&commonResponse.getPersonalLists().size()>0) {
                    personalListsInChunks = commonResponse.getPersonalLists();
                    personalLists.addAll(commonResponse.getPersonalLists());
                    getWatchListDetail();
                    getBinding().emptyImg.setVisibility(View.GONE);

                } else {
                    if (arrayList.size() > 0) {

                    } else {
                        getBinding().emptyImg.setVisibility(View.VISIBLE);
                        setSwipe();
                    }

                }
            }

        });

    }

    private void setSwipe() {
        if (getBinding().emptyImg.getVisibility() == View.VISIBLE) {
            PrintLogging.printLog("", "image" + "---->>");
            getBinding().swipeContainer.setVisibility(View.GONE);
        } else {
            PrintLogging.printLog("", "image" + "=====");
            swipeToRefresh();
        }
    }

    private String getAssetId(List<PersonalList> personalLists) {
        int size = personalLists.size();
        String value = "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            String ksql = personalLists.get(i).getKsql();
            builder.append(ksql).append(",");
        }
        value = builder.toString();
        if (value.length() > 0) {
            value = value.substring(0, value.length() - 1);
        }
        return value;

    }

    private void setUIComponets(List<RailCommonData> railCommonData) {
        arrayList.addAll(railCommonData);

        if (!isScrolling) {
            searchNormalAdapter = new MyWatchlistAdapter(MyListActivity.this, arrayList);
            getBinding().recyclerViewMore.setAdapter(searchNormalAdapter);
            mIsLoading = searchNormalAdapter.getItemCount() != totalCOunt;

        } else {
            mIsLoading = searchNormalAdapter.getItemCount() != totalCOunt;
            // searchNormalAdapter.notifyAdapter(railCommonData);


            searchNormalAdapter.notifyItemInserted(searchNormalAdapter.getItemCount() - 1);
        }

        setPagination();
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
                                loadDataFromModel();

                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        });
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);
       // getBinding().connection.closeButton.setOnClickListener(view -> onBackPressed());
        //getBinding().connection.retryTxt.setOnClickListener(view -> connectionObserver());
    }


//    private boolean showing = false;

//    private void confirmDeletion(final int position) {
//        PrintLogging.printLog("", "confirmDeletion  " + position);
//        if (!showing) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage(this.getResources().getString(R.string.remove_watchlist_item))
//                    .setCancelable(true)
//                    .setPositiveButton("Yes", (dialog, id) -> {
//
//                        GAManager.getInstance().setEvent(GAManager.BROWSING, GAManager.VISIT_WATCHLIST_PAGE, GAManager.BROWSE_WATCHLIST, GAManager.zero);
//                        removeAssetFrmWatchlist(position);
//                        dialog.cancel();
//                        showing = false;
//
//                    })
//                    .setNegativeButton("No", (dialog, id) -> {
//                        dialog.cancel();
//                        showing = false;
//                    });
//            AlertDialog alert = builder.create();
//
//            alert.show();
//            showing = true;
//
//            Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
//            bn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
//            Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
//            bp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
//
//        }
//    }

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
        } catch (Exception ignored) {

        }
    }

    private void deleteItemFromList(int position, int loopIndex, Long pID) {

        deleteWatchListItem(String.valueOf(pID), position, loopIndex);

    }


    private void deleteWatchListItem(final String pID, final int position, final int loopIndex) {
        viewModel.deleteWatchlist(pID).observe(MyListActivity.this, aBoolean -> {
            PrintLogging.printLog("", "deleteDevice m" + aBoolean);
            if (aBoolean.getStatus()) {
                ToastHandler.show(getApplicationContext().getResources().getString(R.string.removed_from_watchlist), getApplicationContext());
                try {
                   /* counter = 1;
                    personalLists.clear();
                    personalListsInChunks.clear();
                    arrayList.clear();
                    loadDataFromModel();*/

                    //   personalListsInChunks.remove(position);

                    arrayList.remove(position);
                    personalLists.remove(loopIndex);
                    searchNormalAdapter.notifyItemRemoved(position);
                    if (searchNormalAdapter.getItemCount() == 0) {
                        getBinding().emptyImg.setVisibility(View.VISIBLE);
                        setSwipe();
                    } else {
                        getBinding().emptyImg.setVisibility(View.GONE);
                    }
                    //deleteWatchListItem(String.valueOf(pID), position, loopIndex);
                    //deleteWatchListItem(String.valueOf(pID), position, loopIndex);
                } catch (Exception e) {

                    PrintLogging.printLog("Exception", "" + e);
                }
            }
        });
    }

    private String getIdTocompareWithWatchlist(Long idofasset) {
        String one = "media_id='";
        String two = String.valueOf(idofasset);
        String three = "'";
        return one.concat(two).concat(three);
    }


   /* private void showAlertDialog(String message) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance(getResources().getString(R.string.app_name), message, getResources().getString(R.string.yes), getResources().getString(R.string.no));
//        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppConstants.TAG_FRAGMENT_ALERT);
    }*/

    /*@Override
    public void onFinishDialog() {

        removeAssetFrmWatchlist(itemPosition);
    }*/
}
