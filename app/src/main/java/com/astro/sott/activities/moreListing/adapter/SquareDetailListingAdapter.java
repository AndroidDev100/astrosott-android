package com.astro.sott.activities.moreListing.adapter;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.MediaTypeCallBack;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.R;
import com.astro.sott.databinding.SquareListingItemBinding;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SquareDetailListingAdapter extends RecyclerView.Adapter<SquareDetailListingAdapter.SingleItemRowHolder> {


    private final int layoutType;
    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private long lastClickTime = 0;


    public SquareDetailListingAdapter(Activity context, List<RailCommonData> itemsList, int type) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;

    }

    @NonNull
    @Override
    public SquareDetailListingAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        SquareListingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.square_listing_item, parent, false);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SquareDetailListingAdapter.SingleItemRowHolder holder, int i) {
        RailCommonData singleItem = itemsList.get(i);
        try {
            boolean isProviderAvailable = AssetContent.getHungamaTag(singleItem.getObject().getTags());
            if (isProviderAvailable){
                holder.squareItemBinding.hungama.setVisibility(View.VISIBLE);
            }else {
                holder.squareItemBinding.hungama.setVisibility(View.GONE);
            }
            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                ImageHelper.getInstance(holder.squareItemBinding.itemImage.getContext()).loadImageTo(holder.squareItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.square1);
            } else {
                ImageHelper.getInstance(holder.squareItemBinding.itemImage.getContext()).loadImageTo(holder.squareItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.square1, holder.squareItemBinding.itemImage), R.drawable.square1);
            }
            if (singleItem.getProgress() > 0) {
                holder.squareItemBinding.progressBar.setVisibility(View.VISIBLE);
                holder.squareItemBinding.progressBar.setProgress(singleItem.getPosition());
            } else {
                holder.squareItemBinding.progressBar.setVisibility(View.GONE);
            }
            getPremimumMark(i, holder.squareItemBinding);
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }

    private void getPremimumMark(int position, SquareListingItemBinding squareItemBinding) {
        squareItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
        Map<String, Value> map = itemsList.get(position).getObject().getMetas();

        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;
        while (itr.hasNext()) {
            key = (String) itr.next();
            if (key.equalsIgnoreCase("Is Exclusive")) {
                squareItemBinding.exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                BooleanValue doubleValue = (BooleanValue) map.get(key);

                if (doubleValue.getValue()) {
                    squareItemBinding.exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                } else {
                    squareItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final SquareListingItemBinding squareItemBinding;

        private SingleItemRowHolder(SquareListingItemBinding view) {
            super(view.getRoot());
            this.squareItemBinding = view;
            final String name = mContext.getClass().getSimpleName();

            squareItemBinding.getRoot().setOnClickListener(view1 -> {

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