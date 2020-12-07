package com.dialog.dialoggo.activities.videoQuality.ui;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.videoQuality.adapter.VideoQualityAdapter;
import com.dialog.dialoggo.activities.videoQuality.viewModel.VideoQualityViewModel;
import com.dialog.dialoggo.baseModel.BaseBindingActivity;
import com.dialog.dialoggo.callBacks.commonCallBacks.NotificationItemClickListner;
import com.dialog.dialoggo.databinding.VideoQualityActivityBinding;
import com.dialog.dialoggo.player.adapter.TrackItem;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.InboxMessage;

import java.util.ArrayList;
import java.util.List;

public class VideoQualityActivity extends BaseBindingActivity<VideoQualityActivityBinding> implements NotificationItemClickListner {

    private VideoQualityViewModel viewModel;
    private VideoQualityAdapter notificationAdapter;
    private List<InboxMessage> list;
    private ArrayList<TrackItem> arrayList;

    @Override
    public VideoQualityActivityBinding inflateBindingLayout(@NonNull LayoutInflater inflater) {
        return VideoQualityActivityBinding.inflate(inflater);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callModel();
        connectionObserver();
//        new ToolBarHandler(this).setVideoQuality(this, getBinding().toolbar);


        setSupportActionBar(getBinding().include.toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.video_quality));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void callModel() {
        viewModel = ViewModelProviders.of(this).get(VideoQualityViewModel.class);
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
            arrayList = viewModel.getQualityList();
            uiInitialization();
            setAdapter();

        } else {
            noConnectionLayout();
        }


    }

    private void setAdapter() {
        if (TextUtils.isEmpty(KsPreferenceKey.getInstance(this).getQualityName())) {
            arrayList.get(0).setSelected(true);
            KsPreferenceKey.getInstance(this).setQualityName(arrayList.get(0).getTrackName());
            KsPreferenceKey.getInstance(this).setQualityPosition(0);


        } else {
            arrayList.get(KsPreferenceKey.getInstance(this).getQualityPosition()).setSelected(true);
        }
        notificationAdapter = new VideoQualityAdapter(this, arrayList);
        getBinding().recyclerview.setAdapter(notificationAdapter);
    }

    private void uiInitialization() {
        getBinding().recyclerview.hasFixedSize();
        getBinding().recyclerview.setNestedScrollingEnabled(false);
        getBinding().recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
//        itemDecor.setDrawable(ContextCompat.getDrawable(VideoQualityActivity.this, R.drawable.item_decorator));
        getBinding().recyclerview.addItemDecoration(itemDecor);
    }

    private void noConnectionLayout() {
        getBinding().noConnectionLayout.setVisibility(View.VISIBLE);

        getBinding().connection.tryAgain.setOnClickListener(view -> connectionObserver());
    }

    @Override
    public void onClick(String id, String status) {
        notificationAdapter.notifyDataSetChanged();
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
}
