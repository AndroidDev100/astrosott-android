package com.astro.sott.fragments.detailRailFragment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.adapter.CommonLandscapeAdapter;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonBean;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.ExclusiveItemBinding;
import com.astro.sott.databinding.LandscapeItemBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ImageHelper;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.SingleItemViewHolder> {
    private List<RailCommonData> similarItemList;

    public SimilarAdapter(List<RailCommonData> loadedList) {
        similarItemList = loadedList;
    }

    @NonNull
    @Override
    public SimilarAdapter.SingleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LandscapeItemBinding landscapeItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.landscape_item, parent, false);
        return new SingleItemViewHolder(landscapeItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarAdapter.SingleItemViewHolder holder, int position) {
        RailCommonData singleItem = similarItemList.get(position);
        if (singleItem.getImages().size() > 0) {
            AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
            ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageToLandscape(holder.landscapeItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.landscape);

        } else {
            ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageToPlaceholder(holder.landscapeItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.landscape, holder.landscapeItemBinding.itemImage), R.drawable.landscape);

        }

        try {
            holder.landscapeItemBinding.titleLayout.setVisibility(View.VISIBLE);
            holder.landscapeItemBinding.tvTitle.setVisibility(View.VISIBLE);
            holder.landscapeItemBinding.tvTitle.setText(similarItemList.get(position).getObject().getName());
            holder.landscapeItemBinding.tvDescription.setText(similarItemList.get(position).getObject().getDescription());
        }catch (Exception ignored){

        }
        getPremimumMark(position,holder.landscapeItemBinding.exclusiveLayout);
    }

    @Override
    public int getItemCount() {
        return similarItemList.size();
    }
    private void getPremimumMark(int position, ExclusiveItemBinding exclusiveLayout) {

        exclusiveLayout.exclLay.setVisibility(View.GONE);
        Map<String, Value> map = similarItemList.get(position).getObject().getMetas();

        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;
        while (itr.hasNext()) {
            key = (String) itr.next();
            if (key.equalsIgnoreCase("Is Exclusive")) {
                exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                BooleanValue doubleValue = (BooleanValue) map.get(key);

                if (doubleValue.getValue()) {
                    exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                } else {
                    exclusiveLayout.exclLay.setVisibility(View.GONE);
                }

            }
        }
    }

    class SingleItemViewHolder extends RecyclerView.ViewHolder {
        LandscapeItemBinding landscapeItemBinding;

        public SingleItemViewHolder(@NonNull LandscapeItemBinding itemView) {
            super(itemView.getRoot());
            landscapeItemBinding = itemView;
        }
    }

}
