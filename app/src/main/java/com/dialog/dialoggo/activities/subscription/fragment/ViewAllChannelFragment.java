package com.dialog.dialoggo.activities.subscription.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.subscription.adapter.AllChannelGridAdapter;
import com.dialog.dialoggo.activities.subscription.callback.BottomSheetFragmentListener;
import com.dialog.dialoggo.activities.subscription.callback.ChannelDataUpdateListener;
import com.dialog.dialoggo.activities.subscription.listeners.GridRecyclerViewScrollListener;
import com.dialog.dialoggo.activities.subscription.manager.AllChannelManager;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.fragments.dialog.AlertDialogSingleButtonFragment;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ViewAllChannelFragment extends Fragment implements ChannelDataUpdateListener , AlertDialogSingleButtonFragment.AlertDialogListener {

    private static final String TAG = "ViewAllChannelFragment";


    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private int numberOfColumns = 4;
    List<RailCommonData> allChannelDataList = new ArrayList<>();
    private int mCounter = 1;

    private BottomSheetFragmentListener mListener;

    void setBottomSheetFragmentListener(BottomSheetFragmentListener listener) {
        this.mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_all_recycler_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AllChannelManager.getInstance().setDataUpdateListener(this);
        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        if(AllChannelManager.getInstance().getRailCommonDataList() != null) {
            allChannelDataList.addAll(AllChannelManager.getInstance().getRailCommonDataList());
        }
        initializeRecyclerView(allChannelDataList);
    }

    private void initializeRecyclerView(List<RailCommonData> allChannelDataList) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        AllChannelGridAdapter allChannelGridAdapter = new AllChannelGridAdapter(allChannelDataList);
        mRecyclerView.setAdapter(allChannelGridAdapter);

        mRecyclerView.addOnScrollListener(new GridRecyclerViewScrollListener(gridLayoutManager, new GridRecyclerViewScrollListener.DataLoader() {
            @Override
            public boolean onLoadMore() {
                mCounter++;
                progressBar.setVisibility(View.VISIBLE);
                mListener.loadMoreChannel(mCounter);
                return true;
            }
        }));
    }

    @Override
    public void addDataToChannelList(List<RailCommonData> channelDataList) {
        if(channelDataList != null && channelDataList.size() > 0) {
            allChannelDataList.addAll(channelDataList);
            if(mRecyclerView.getAdapter() != null) {
                mRecyclerView.getAdapter().notifyItemInserted(allChannelDataList.size());
            }
        } else {
            Log.d(TAG, getString(R.string.no_more_data));
           // showDialog(getResources().getString(R.string.something_went_wrong));
        }
        progressBar.setVisibility(View.GONE);
    }

    private void showDialog(String message) {
        FragmentManager fm = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        AlertDialogSingleButtonFragment alertDialog = AlertDialogSingleButtonFragment.newInstance(getResources().getString(R.string.dialog), message, getResources().getString(R.string.ok));
        alertDialog.setCancelable(false);
        alertDialog.setAlertDialogCallBack(this);
        if (fm != null)
            alertDialog.show(fm, AppLevelConstants.TAG_FRAGMENT_ALERT);
    }

    @Override
    public void noDataFound() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFinishDialog() {

    }
}