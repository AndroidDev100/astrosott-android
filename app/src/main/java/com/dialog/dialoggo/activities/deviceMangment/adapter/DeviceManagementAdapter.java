package com.dialog.dialoggo.activities.deviceMangment.adapter;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import android.graphics.PorterDuff;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.callBacks.commonCallBacks.ItemDeleteListener;
import com.dialog.dialoggo.databinding.DeviceItemBinding;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.UDID;
import com.kaltura.client.types.HouseholdDevice;

import java.util.List;

public class DeviceManagementAdapter extends RecyclerView.Adapter<DeviceManagementAdapter.ViewHolder> {
    private final ItemDeleteListener itemClickListener;
    private final List<HouseholdDevice> itemsList;
    private final Activity mContext;

    public DeviceManagementAdapter(Activity context, List<HouseholdDevice> itemsList, ItemDeleteListener call) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.itemClickListener = call;


    }

    @NonNull
    @Override
    public DeviceManagementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        DeviceItemBinding deviceItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.device_item, viewGroup, false);
        return new DeviceManagementAdapter.ViewHolder(deviceItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceManagementAdapter.ViewHolder holder, final int i) {


        HouseholdDevice householdDevice = itemsList.get(i);
        if (householdDevice.getBrandId() == 2) {
            holder.deviceItemBinding.ivOs.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_android_24_px));
            holder.deviceItemBinding.ivEditDelete.setVisibility(View.VISIBLE);
        } else if (householdDevice.getBrandId() == 1) {
            holder.deviceItemBinding.ivOs.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_iphone_24_px));
            holder.deviceItemBinding.ivEditDelete.setVisibility(View.VISIBLE);
//            holder.deviceItemBinding.moreListIcon.setBackgroundResource(R.drawable.ios_icon);
        } else if (householdDevice.getBrandId() == 22) {
            holder.deviceItemBinding.ivOs.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.desktop_icon));
            holder.deviceItemBinding.ivEditDelete.setVisibility(View.VISIBLE);
//            holder.deviceItemBinding.moreListIcon.setBackgroundResource(R.drawable.ios_icon);
        } else{
            holder.deviceItemBinding.ivOs.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_hdmi_24px));
            holder.deviceItemBinding.ivEditDelete.setVisibility(View.GONE);
//            holder.deviceItemBinding.moreListIcon.setBackgroundResource(R.drawable.ios_icon);
        }
        if (UDID.getDeviceId(mContext,mContext.getContentResolver()).equals(itemsList.get(i).getUdid())) {
            holder.deviceItemBinding.ivEditDelete.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_edit));
//            holder.deviceItemBinding.ivDelete.setVisibility(View.GONE);
//            holder.deviceItemBinding.ivEdit.setVisibility(View.VISIBLE);
        } else {
            holder.deviceItemBinding.ivEditDelete.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_delete));
            holder.deviceItemBinding.ivEditDelete.setColorFilter(ContextCompat.getColor(mContext, R.color.more_item_title), PorterDuff.Mode.SRC_IN);
//            holder.deviceItemBinding.ivDelete.setVisibility(View.VISIBLE);
//            holder.deviceItemBinding.ivEdit.setVisibility(View.GONE);
        }

        holder.deviceItemBinding.tvDevice.setText(householdDevice.getName());
        // setIcons(holder.deviceItemBinding.moreListIcon, i);

        holder.deviceItemBinding.ivEditDelete.setOnClickListener(view -> {
            if (UDID.getDeviceId(mContext,mContext.getContentResolver()).equals(itemsList.get(i).getUdid())) {


                itemClickListener.itemClicked(i, AppLevelConstants.EDIT);
            } else {

                itemClickListener.itemClicked(i, AppLevelConstants.DELETE);

            }

        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final DeviceItemBinding deviceItemBinding;

        private ViewHolder(DeviceItemBinding view) {
            super(view.getRoot());
            deviceItemBinding = view;
            //moreItemBinding.mRoot.setOnClickListener(this);
            deviceItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PrintLogging.printLog(this.getClass(), "", "valuessClicked" + itemsList.get(getLayoutPosition()).toString());

//                    if (itemsList.get(getLayoutPosition()).toString().equalsIgnoreCase(AppConstants.LIVE_TV)){
//                        itemClickListener.onClick(AppConstants.LIVE_TV);
//                    }else if (itemsList.get(getLayoutPosition()).toString().equalsIgnoreCase(AppConstants.TRENDING_TALENT)){
//                        itemClickListener.onClick(AppConstants.TRENDING_TALENT);
//                    }
//                    else if (itemsList.get(getLayoutPosition()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.logout))){
//                        itemClickListener.onClick(mContext.getResources().getString(R.string.logout));
//                    }

                }
            });
        }

    }
}
