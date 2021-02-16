package com.astro.sott.activities.manageDevice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.activities.audio.adapter.AudioAdapter;
import com.astro.sott.callBacks.DeviceDeleteCallBack;
import com.astro.sott.databinding.ManageDeviceItemBinding;
import com.astro.sott.usermanagment.modelClasses.getDevice.AccountDeviceDetailsItem;

import java.util.List;

public class ManageDeviceAdapter extends RecyclerView.Adapter<ManageDeviceAdapter.SingleItemHolder> {

    private List<AccountDeviceDetailsItem> itemList;
    private DeviceDeleteCallBack deviceDeleteCallBack;

    public ManageDeviceAdapter(List<AccountDeviceDetailsItem> accountDeviceDetailsItems, DeviceDeleteCallBack deviceDeleteCallBack) {
        itemList = accountDeviceDetailsItems;
        this.deviceDeleteCallBack = deviceDeleteCallBack;
    }

    @NonNull
    @Override
    public SingleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ManageDeviceItemBinding manageDeviceItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.manage_device_item, parent, false);
        return new ManageDeviceAdapter.SingleItemHolder(manageDeviceItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemHolder holder, int position) {

        holder.manageDeviceItemBinding.title.setText(itemList.get(position).getDeviceName());
        holder.manageDeviceItemBinding.activeDate.setText("Last active " + getDate(itemList.get(position).getLastLoginTime()));
        holder.manageDeviceItemBinding.deleteIcon.setOnClickListener(view -> {
            if (itemList.get(position).getSerialNo() != null)
                deviceDeleteCallBack.onDelete(itemList.get(position).getSerialNo());
        });

    }

    private String getDate(String lastTime) {
        String date = "";

        String date_time[] = lastTime.split(" ");
        if (date_time[0] != null)
            date = date_time[0];
        return date;

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {

        ManageDeviceItemBinding manageDeviceItemBinding;

        public SingleItemHolder(@NonNull ManageDeviceItemBinding itemView) {
            super(itemView.getRoot());
            manageDeviceItemBinding = itemView;
        }
    }
}
