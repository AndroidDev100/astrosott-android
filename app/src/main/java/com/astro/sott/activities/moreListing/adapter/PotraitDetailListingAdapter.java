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
import com.astro.sott.callBacks.commonCallBacks.MediaTypeCallBack;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.PotraitListingItemBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PotraitDetailListingAdapter extends RecyclerView.Adapter<PotraitDetailListingAdapter.SingleItemRowHolder> {


    private final int layoutType;
    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private long lastClickTime = 0;


    public PotraitDetailListingAdapter(Activity context,
                                       List<RailCommonData> itemsList, int type) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;


    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        PotraitListingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.potrait_listing_item, parent, false);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {

        RailCommonData singleItem = itemsList.get(i);

        try {
            if (singleItem.getImages().size() > 0) {

//                singleItem.getType();
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);

                ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext()).loadImageToPortraitDetailListing(holder.potraitItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.portrait);

            } else {
                ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext()).loadImageTo(holder.potraitItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.portrait, holder.potraitItemBinding.itemImage), R.drawable.portrait);

            }
            getPremimumMark(i, holder.potraitItemBinding);

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }


    }

    private void getPremimumMark(int position, PotraitListingItemBinding potraitItemBinding) {
        potraitItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
        Map<String, Value> map = itemsList.get(position).getObject().getMetas();

        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;
        while (itr.hasNext()) {
            key = (String) itr.next();
            if (key.equalsIgnoreCase("Is Exclusive")) {
                potraitItemBinding.exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                BooleanValue doubleValue = (BooleanValue) map.get(key);

                if (doubleValue.getValue()) {
                    potraitItemBinding.exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                } else {
                    potraitItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
                }

            }
        }
    }


    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final PotraitListingItemBinding potraitItemBinding;

        private SingleItemRowHolder(PotraitListingItemBinding view) {
            super(view.getRoot());
            this.potraitItemBinding = view;
            final String name = mContext.getClass().getSimpleName();

            potraitItemBinding.getRoot().setOnClickListener(view1 -> {


                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                new ActivityLauncher(mContext).railClickCondition("","",name, itemsList.get(getLayoutPosition()),getLayoutPosition(), layoutType,itemsList, new MediaTypeCallBack() {
                    @Override
                    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

                    }
                });
            });


        }

    }
}
