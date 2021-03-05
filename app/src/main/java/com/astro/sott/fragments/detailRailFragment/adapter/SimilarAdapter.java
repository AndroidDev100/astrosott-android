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
import com.astro.sott.databinding.RelatedItemBinding;
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
        RelatedItemBinding landscapeItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.related_item, parent, false);
        return new SingleItemViewHolder(landscapeItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarAdapter.SingleItemViewHolder holder, int position) {
        RailCommonData singleItem = similarItemList.get(position);
        if (singleItem.getImages().size() > 0) {
            AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
            ImageHelper.getInstance(holder.landscapeItemBinding.image.getContext()).loadImageToLandscape(holder.landscapeItemBinding.image, assetCommonImages.getImageUrl(), R.drawable.landscape);

        } else {
            ImageHelper.getInstance(holder.landscapeItemBinding.image.getContext()).loadImageToPlaceholder(holder.landscapeItemBinding.image, AppCommonMethods.getImageURI(R.drawable.landscape, holder.landscapeItemBinding.image), R.drawable.landscape);

        }
        try {
            AppCommonMethods.setBillingUi(holder.landscapeItemBinding.metas.billingImage, singleItem.getObject().getTags());

        }catch (Exception e){

        }

        holder.landscapeItemBinding.lanscapeTitle.setText(singleItem.getName());

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
        RelatedItemBinding landscapeItemBinding;

        public SingleItemViewHolder(@NonNull RelatedItemBinding itemView) {
            super(itemView.getRoot());
            landscapeItemBinding = itemView;
        }
    }

}
