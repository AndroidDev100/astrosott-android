package com.dialog.dialoggo.activities.notification.ui;

import android.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.notification.adapter.NotificationAdapter;
import com.dialog.dialoggo.activities.notification.viewModel.NotificationViewModel;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.callBacks.commonCallBacks.NotificationDeleteClickListener;
import com.dialog.dialoggo.callBacks.commonCallBacks.NotificationItemClickListner;
import com.dialog.dialoggo.databinding.ActivityNotificationBinding;
import com.dialog.dialoggo.fragments.dialog.AlertDialogFragment;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.kaltura.client.types.InboxMessage;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends BaseBindingActivity<ActivityNotificationBinding> implements NotificationItemClickListner, NotificationDeleteClickListener, AlertDialogFragment.AlertDialogListener {
    private NotificationViewModel viewModel;
    private  NotificationAdapter notificationAdapter;
    private  List<InboxMessage> list;
    private int position;

    @Override
    public ActivityNotificationBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return ActivityNotificationBinding.inflate(inflater);


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionObserver();
//        new ToolBarHandler(this).setNotificationAction(getBinding().toolbar);


        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.notifications));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        getBinding().swipeContainer.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.dark_gray_background));
        getBinding().swipeContainer.setColorSchemeColors(getResources().getColor(R.color.primary_blue));
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

    private void loadDataFromModel() {

        if (!getBinding().swipeContainer.isRefreshing()) {
            getBinding().includeProgressbar.progressBar.setVisibility(View.VISIBLE);
        } else {
            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);

        }
        viewModel.getNotification().observe(this, inboxMessages -> {

            getBinding().includeProgressbar.progressBar.setVisibility(View.GONE);
            swipeToRefreshCheck();
            if (inboxMessages != null) {
                if (inboxMessages.size() > 0) {
                    getBinding().emptyImg.setVisibility(View.GONE);
                    getBinding().recyclerview.setVisibility(View.VISIBLE);
                    setUIComponets(inboxMessages);
                } else {
                    getBinding().emptyImg.setVisibility(View.VISIBLE);
                    getBinding().recyclerview.setVisibility(View.GONE);
                    setSwipe();
                }
            } else {
                getBinding().emptyImg.setVisibility(View.VISIBLE);
                getBinding().recyclerview.setVisibility(View.GONE);
                setSwipe();
            }

        });
    }

    private void setSwipe() {
        if (getBinding().emptyImg.getVisibility() == View.VISIBLE) {
            PrintLogging.printLog(this.getClass(), "", "image" + "---->>");
            getBinding().swipeContainer.setVisibility(View.GONE);
        } else {
            PrintLogging.printLog(this.getClass(), "", "image" + "=====");
            swipeToRefresh();
        }
    }


    private void setUIComponets(List<InboxMessage> inboxMessages) {
        list = new ArrayList<>();
        for (int i = 0; i < inboxMessages.size(); i++) {
            PrintLogging.printLog(this.getClass(), "", "statusNoti-->>" + inboxMessages.get(i).getStatus().toString());
            if (inboxMessages.get(i).getStatus().toString().equals(AppLevelConstants.STATUS_DELETED)) {

                PrintLogging.printLog(this.getClass(), "status delted ", "");
            } else {
                list.add(inboxMessages.get(i));
            }
        }

        if (list.size() == 0) {
            getBinding().emptyImg.setVisibility(View.VISIBLE);
            getBinding().recyclerview.setVisibility(View.GONE);
            setSwipe();
        } else {
            notificationAdapter = new NotificationAdapter(NotificationActivity.this, list, NotificationActivity.this, NotificationActivity.this);
            getBinding().recyclerview.setAdapter(notificationAdapter);

        }

    }

    private void modelCall() {
        viewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
    }


    private void swipeToRefresh() {

        if (getBinding().emptyImg.getVisibility() == View.VISIBLE) {
            getBinding().swipeContainer.setVisibility(View.GONE);

        } else {
            getBinding().swipeContainer.setOnRefreshListener(() -> {
                if (NetworkConnectivity.isOnline(NotificationActivity.this)) {
                    loadDataFromModel();
                } else {
                    swipeToRefreshCheck();
                    ToastHandler.show(getResources().getString(R.string.no_internet_connection), getApplicationContext());
                }
            });

        }


    }

    private void swipeToRefreshCheck() {
        if (getBinding().swipeContainer != null) {
            if (getBinding().swipeContainer.isRefreshing()) {
                getBinding().swipeContainer.setRefreshing(false);
            }
        }
    }

    private void UIinitialization() {


        swipeToRefresh();

//        getBinding().recyclerview.addOnItemTouchListener(new RecyclerTouchListener(this, getBinding().recyclerview, new ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//                confirmDeletion(position);
//            }
//        }));
        getBinding().recyclerview.hasFixedSize();
        getBinding().recyclerview.setNestedScrollingEnabled(false);
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        getBinding().recyclerview.addItemDecoration(itemDecor);
    }

    private void confirmDeletion(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getResources().getString(R.string.remove_watchlist_item))
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, id) -> removeAssetFrmNotificationlist(position))
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
        Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        bn.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
        Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        bp.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
    }

    private void removeAssetFrmNotificationlist(int position) {
        if (list.size() > 0) {
            String id = list.get(position).getId();
            String status = AppLevelConstants.CHANGE_STATUS_DELETED;
            list.remove(position);
            notificationAdapter.notifyItemRemoved(position);
            viewModel.updatestatus(id, status).observe(this, status1 -> {
                if (status1 != null && status1) {
                    loadDataFromModel();
                }

            });
        }
    }


    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    @Override
    public void onClick(String id, String status) {
        viewModel.updatestatus(id, status).observe(this, status1 -> {
//            if (status1 == true) {
//                // loadDataFromModel();
//            }

        });
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

    private void showAlertDialog(String msg) {
        FragmentManager fm = getSupportFragmentManager();
        AlertDialogFragment alertDialog = AlertDialogFragment.newInstance(getResources().getString(R.string.dialog), msg, getResources().getString(R.string.yes), getResources().getString(R.string.no));
        alertDialog.setAlertDialogCallBack(this);
        alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    @Override
    public void notificationDelete(int position) {
        this.position = position;
        showAlertDialog(getResources().getString(R.string.remove_notification));
    }

    @Override
    public void onFinishDialog() {
        removeAssetFrmNotificationlist(position);

    }
}



