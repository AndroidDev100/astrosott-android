package com.astro.sott.activities.manageDevice.adapter;

import android.content.Context;
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
import com.astro.sott.utils.commonMethods.AppCommonMethods;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ManageDeviceAdapter extends RecyclerView.Adapter<ManageDeviceAdapter.SingleItemHolder> {

    private List<AccountDeviceDetailsItem> itemList;
    private Context mContext;
    private DeviceDeleteCallBack deviceDeleteCallBack;

    public ManageDeviceAdapter(List<AccountDeviceDetailsItem> accountDeviceDetailsItems, Context deviceDeleteCallBack) {
        itemList = accountDeviceDetailsItems;
        this.deviceDeleteCallBack = (DeviceDeleteCallBack) deviceDeleteCallBack;
        this.mContext = deviceDeleteCallBack;
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
        if (itemList.get(position).getSerialNo().equalsIgnoreCase(AppCommonMethods.getDeviceId(mContext.getContentResolver()))) {
            holder.manageDeviceItemBinding.deleteIcon.setVisibility(View.GONE);
        } else {
            holder.manageDeviceItemBinding.deleteIcon.setVisibility(View.VISIBLE);
        }
        AppCommonMethods.getDeviceId(mContext.getContentResolver());
        if (itemList.get(position).getDeviceType().equalsIgnoreCase("IOS")||itemList.get(position).getDeviceType().equalsIgnoreCase("IOS_TABLET")) {
            holder.manageDeviceItemBinding.imgDevice.setBackground(mContext.getResources().getDrawable(R.drawable.ic_apple));
        } else if (itemList.get(position).getDeviceType().equalsIgnoreCase("ANDROID")||itemList.get(position).getDeviceType().equalsIgnoreCase("ANDROID_TABLET")) {
            holder.manageDeviceItemBinding.imgDevice.setBackground(mContext.getResources().getDrawable(R.drawable.ic_android_24px));
        } else if (itemList.get(position).getDeviceType().equalsIgnoreCase("PC")) {
            holder.manageDeviceItemBinding.imgDevice.setBackground(mContext.getResources().getDrawable(R.drawable.ic_laptop_24_px));
        } else {
            holder.manageDeviceItemBinding.imgDevice.setBackground(mContext.getResources().getDrawable(R.drawable.ic_android_24px));
        }
        holder.manageDeviceItemBinding.title.setText(itemList.get(position).getDeviceName() +" "+ itemList.get(position).getModelNo());
        holder.manageDeviceItemBinding.activeDate.setText("Last active " + getDate(itemList.get(position).getLastLoginTime()));
        holder.manageDeviceItemBinding.deleteIcon.setOnClickListener(view -> {
            if (itemList.get(position).getSerialNo() != null)
                deviceDeleteCallBack.onDelete(itemList.get(position).getSerialNo());
        });
//        if(position== (getItemCount()-1 )){
//
//        }

    }

    private String getDate(String lastTime) {
        String date = "";
        Date formatDate = null;

        String date_time[] = lastTime.split(" ");
        if (date_time[0] != null)
            date = date_time[0];
        SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        format1.setTimeZone(TimeZone.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy",Locale.getDefault());
        format2.setTimeZone(TimeZone.getDefault());
        try {
            formatDate = format1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (formatDate != null)
            date = format2.format(formatDate);

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
