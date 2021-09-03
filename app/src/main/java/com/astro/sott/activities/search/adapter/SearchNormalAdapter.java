package com.astro.sott.activities.search.adapter;

import android.app.Activity;

import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.adapter.RibbonAdapter;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.databinding.QuickSearchItemBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.R;
import com.astro.sott.databinding.SearchItemBinding;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MediaImage;
import com.kaltura.client.types.MultilingualStringValueArray;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class SearchNormalAdapter extends RecyclerView.Adapter<SearchNormalAdapter.SingleItemRowHolder> {
    private final Activity activity;
    private final List<Asset> itemsList;
    private final SearchNormalItemListener itemListener;

    public SearchNormalAdapter(Activity activity, List<Asset> itemsList, SearchNormalItemListener listener) {
        this.activity = activity;
        this.itemsList = itemsList;
        this.itemListener = listener;
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        QuickSearchItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.quick_search_item, viewGroup, false);
        return new SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder viewHolder, final int position) {
        boolean imageAvailable = false;
        Asset singleItem = itemsList.get(position);

        AppCommonMethods.setBillingUi(viewHolder.searchItemBinding.billingImage, singleItem.getTags(), singleItem.getType(), activity);
        setRecycler(viewHolder.searchItemBinding.metas.recyclerView, singleItem.getTags());

        if (itemsList.get(position).getType() == MediaTypeConstant.getWebEpisode(activity)) {
            Drawable background = viewHolder.searchItemBinding.creatorLay.getBackground();
            if (background instanceof GradientDrawable) {
                GradientDrawable shapeDrawable = (GradientDrawable) background;
                Random random = new Random();
                int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                shapeDrawable.setColor(color);
            }
            for (MediaImage image :
                    itemsList.get(position).getImages()) {
                if (image.getRatio().equals("16x9")) {
                    String image_url = image.getUrl();
                    String final_url = image_url + AppLevelConstants.WIDTH + (int) activity.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) activity.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                    Log.w("imageUrl", image_url);
                    Log.w("imageUrl", final_url);
                    itemsList.get(position).getImages().get(0).setUrl(final_url);
                    imageAvailable = true;
                }
            }
            try {
                if (!imageAvailable) {
                    if (itemsList.get(0).getImages().size() > 0 && itemsList.size() > 0)
                        itemsList.get(position).getImages().get(0).setUrl("");
                }
            } catch (Exception e) {

            }


            viewHolder.searchItemBinding.itemImage.setVisibility(View.VISIBLE);
            viewHolder.searchItemBinding.creatorLay.setVisibility(View.GONE);
            viewHolder.searchItemBinding.setSingleItem(itemsList.get(position));
            viewHolder.searchItemBinding.tvEpisode.setVisibility(View.VISIBLE);
        } else {
            viewHolder.searchItemBinding.setSingleItem(itemsList.get(position));
        }
        viewHolder.searchItemBinding.clRoot.setOnClickListener(view -> itemListener.onItemClicked(itemsList.get(position), 23));
    }

    private void setRecycler(RecyclerView recyclerView, Map<String, MultilingualStringValueArray> tags) {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        RibbonAdapter ribbonAdapter = new RibbonAdapter(AssetContent.getRibbon(tags));
        recyclerView.setAdapter(ribbonAdapter);

    }

    @Override
    public int getItemCount() {
       /* int limitView = 5;
        if (itemsList.size() > 4)
            return limitView;
        else*/
        return itemsList.size();
    }

    public interface SearchNormalItemListener {
        void onItemClicked(Asset itemValue, int position);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        final QuickSearchItemBinding searchItemBinding;

        SingleItemRowHolder(QuickSearchItemBinding binding) {
            super(binding.getRoot());
            this.searchItemBinding = binding;
        }

    }

}
