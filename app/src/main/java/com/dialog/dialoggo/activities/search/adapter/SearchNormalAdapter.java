package com.dialog.dialoggo.activities.search.adapter;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.databinding.SearchItemBinding;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.AssetContent;
import com.dialog.dialoggo.utils.helpers.MediaTypeConstant;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MediaImage;

import java.util.List;
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
        SearchItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.search_item, viewGroup, false);
        return new SingleItemRowHolder(itemBinding);
    }
    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder viewHolder, final int position) {
        boolean imageAvailable = false;

        boolean isProviderAvailable = AssetContent.getHungamaTag(itemsList.get(position).getTags());
        if (isProviderAvailable){
           viewHolder.searchItemBinding.hungama.setVisibility(View.VISIBLE);
        }else {
            viewHolder.searchItemBinding.hungama.setVisibility(View.GONE);
        }

        if (itemsList.get(position).getType() == MediaTypeConstant.getLinear(activity)) {
            Drawable background = viewHolder.searchItemBinding.creatorLay.getBackground();
            if (background instanceof GradientDrawable) {
                GradientDrawable shapeDrawable = (GradientDrawable) background;
                Random random = new Random();
                int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                shapeDrawable.setColor(color);
            }
            for (MediaImage image :
                    itemsList.get(position).getImages()) {
                if (image.getRatio().equals("1:1")) {
                    String image_url = image.getUrl();
                    String final_url = image_url + AppLevelConstants.WIDTH + (int) activity.getResources().getDimension(R.dimen.square_image_width) + AppLevelConstants.HEIGHT + (int) activity.getResources().getDimension(R.dimen.square_image_height) + AppLevelConstants.QUALITY;
                    itemsList.get(position).getImages().get(0).setUrl(final_url);
                    imageAvailable = true;
                }
            }
            if (!imageAvailable) {
                if(itemsList.get(0).getImages().size()>0 && itemsList.size()>0)
                itemsList.get(position).getImages().get(0).setUrl("");
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


    @Override
    public int getItemCount() {
        int limitView = 5;
        if (itemsList.size() > 4)
            return limitView;
        else return itemsList.size();
    }

    public interface SearchNormalItemListener {
        void onItemClicked(Asset itemValue, int position);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        final SearchItemBinding searchItemBinding;

        SingleItemRowHolder(SearchItemBinding binding) {
            super(binding.getRoot());
            this.searchItemBinding = binding;
        }

    }

}
