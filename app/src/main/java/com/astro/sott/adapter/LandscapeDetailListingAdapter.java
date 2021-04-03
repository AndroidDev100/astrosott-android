package com.astro.sott.adapter;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.callBacks.commonCallBacks.MediaTypeCallBack;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.LandscapelistingItemBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LandscapeDetailListingAdapter extends RecyclerView.Adapter<LandscapeDetailListingAdapter.SingleItemRowHolder> {
    private final int layoutType;
    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private long lastClickTime = 0;
    private DetailRailClick detailRailClick;

    public LandscapeDetailListingAdapter(Activity context, List<RailCommonData> itemsList, int type) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        try {
            this.detailRailClick = ((DetailRailClick) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LandscapelistingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.landscapelisting_item, parent, false);
        return new SingleItemRowHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
        RailCommonData singleItem = itemsList.get(i);
        holder.landscapeItemBinding.livenowLay.setVisibility(View.GONE);
        holder.landscapeItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
        try {


            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageToLandscapeDetailListingAdapter(holder.landscapeItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.landscape);
            } else {
                ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageToPlaceholder(holder.landscapeItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.landscape, holder.landscapeItemBinding.itemImage), R.drawable.landscape);

            }
            if (singleItem.getType() == MediaTypeConstant.getProgram(mContext))
                getLiveMark(i, holder.landscapeItemBinding);
            else
                getPremimumMark(i, holder.landscapeItemBinding);

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }

    private void getLiveMark(int i, LandscapelistingItemBinding landscapeItemBinding) {
        landscapeItemBinding.livenowLay.setVisibility(View.VISIBLE);
    }

    private void getPremimumMark(int position, LandscapelistingItemBinding landscapeItemBinding) {

        landscapeItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
        Map<String, Value> map = itemsList.get(position).getObject().getMetas();

        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;
        while (itr.hasNext()) {
            key = (String) itr.next();
            if (key.equalsIgnoreCase("Is Exclusive")) {
                landscapeItemBinding.exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                BooleanValue doubleValue = (BooleanValue) map.get(key);
                if (doubleValue.getValue()) {
                    landscapeItemBinding.exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                } else {
                    landscapeItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
                }

            }
        }
    }


    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        private final LandscapelistingItemBinding landscapeItemBinding;

        private SingleItemRowHolder(LandscapelistingItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            landscapeItemBinding = flightItemLayoutBinding;
            final String name = mContext.getClass().getSimpleName();
            try {
                landscapeItemBinding.getRoot().setOnClickListener(view -> new ActivityLauncher(mContext).railClickCondition("","",name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType,itemsList, new MediaTypeCallBack() {
                    @Override
                    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {
                        if (NetworkConnectivity.isOnline(mContext)) {
                            detailRailClick.detailItemClicked(_url, position, type, commonData);
                        } else {
                            ToastHandler.show(mContext.getResources().getString(R.string.no_internet_connection), mContext);
                        }
                    }
                }));
            } catch (Exception ignore) {

            }

        }


    }

}
