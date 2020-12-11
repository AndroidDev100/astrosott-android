package com.astro.sott.adapter;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import android.os.SystemClock;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.MediaTypeCallBack;
import com.astro.sott.databinding.ContinuewatchinglistingItemBinding;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonContWatchListingAdapter extends RecyclerView.Adapter<CommonContWatchListingAdapter.SingleItemRowHolder> {
    private final List<RailCommonData> itemsList;
    private Activity mContext;
    private DetailRailClick detailRailClick;
    private int layoutType;
    private long lastClickTime = 0;


    public CommonContWatchListingAdapter(Activity context, List<RailCommonData> itemsList, int type) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        try {
            this.detailRailClick = ((DetailRailClick) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int i) {
        ContinuewatchinglistingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.continuewatchinglisting_item, parent, false);
        return new SingleItemRowHolder(binding);

    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        RailCommonData singleItem = itemsList.get(i);


        try {
            AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
            // holder.landscapeItemBinding.setImage(assetCommonImages);

            ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageTocontinueWatchingListing(holder.landscapeItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.square1);
            holder.landscapeItemBinding.progressBar.setProgress(singleItem.getPosition());
            getPremimumMark(i, holder.landscapeItemBinding);
        } catch (Exception e) {

        }
        boolean isProviderAvailable = AssetContent.getHungamaTag(singleItem.getObject().getTags());
        if (isProviderAvailable){
            holder.landscapeItemBinding.hungama.setVisibility(View.VISIBLE);
        }else {
            holder.landscapeItemBinding.hungama.setVisibility(View.GONE);
        }
    }

    private void getPremimumMark(int position, ContinuewatchinglistingItemBinding landscapeItemBinding) {

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

        public ContinuewatchinglistingItemBinding landscapeItemBinding;

        private SingleItemRowHolder(ContinuewatchinglistingItemBinding view) {
            super(view.getRoot());
            this.landscapeItemBinding = view;
            final String name = mContext.getClass().getSimpleName();

            landscapeItemBinding.getRoot().setOnClickListener(view1 -> {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                new ActivityLauncher(mContext).railClickCondition("","",name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType,itemsList, new MediaTypeCallBack() {
                    @Override
                    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

                    }
                });

            });
        }


    }


}
