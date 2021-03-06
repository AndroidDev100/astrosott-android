package com.astro.sott.activities.search.adapter;

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

import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.R;
import com.astro.sott.databinding.SearchItemBinding;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.kaltura.client.types.Asset;

import java.util.List;
import java.util.Random;

public class ResultAdapterAll extends RecyclerView.Adapter<ResultAdapterAll.SingleItemRowHolder> {
    private final Activity context;
    private final List<Asset> itemsList;

    public ResultAdapterAll(Activity context, List<Asset> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    public void notifyAdapter(List<Asset> itemsList) {
        this.itemsList.addAll(itemsList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        SearchItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.search_item, viewGroup, false);

        return new SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder viewHolder, final int position) {
        // viewHolder.searchItemBinding.setSingleItem(itemsList.get(position));
        if (itemsList.get(position).getImages().size() > 0) {
            ImageHelper.getInstance(viewHolder.searchItemBinding.image.getContext()).loadImageToLandscape(viewHolder.searchItemBinding.image, itemsList.get(position).getImages().get(0).getUrl(), R.drawable.ic_landscape_placeholder);
        } else {
            ImageHelper.getInstance(viewHolder.searchItemBinding.image.getContext()).loadPlaceHolder(viewHolder.searchItemBinding.image, R.drawable.ic_landscape_placeholder);
        }
        viewHolder.searchItemBinding.tvShortDescription.setText(itemsList.get(position).getDescription());
        viewHolder.searchItemBinding.setSingleItem(itemsList.get(position));
        viewHolder.searchItemBinding.tvTitle.setText(itemsList.get(position).getName());
        if (itemsList.get(position).getType() == MediaTypeConstant.getProgram(context)) {
            viewHolder.searchItemBinding.tvShortDescription.setVisibility(View.VISIBLE);
            viewHolder.searchItemBinding.tvShortDescription.setTextColor(context.getResources().getColor(R.color.yellow_orange));
            viewHolder.searchItemBinding.tvShortDescription.setText(AppCommonMethods.getProgramTimeDate(itemsList.get(position).getStartDate()) + " - " + AppCommonMethods.getEndTime(itemsList.get(position).getEndDate()));
        } else if (itemsList.get(position).getType() == MediaTypeConstant.getLinear(context)) {
            if (AssetContent.isLiveEvent(itemsList.get(position).getMetas())) {
                viewHolder.searchItemBinding.tvShortDescription.setVisibility(View.VISIBLE);
                String liveEventTime = AppCommonMethods.getLiveEventTime(itemsList.get(position));
                viewHolder.searchItemBinding.tvShortDescription.setTextColor(context.getResources().getColor(R.color.yellow_orange));
                viewHolder.searchItemBinding.tvShortDescription.setText(liveEventTime);
            }

        }


/*        if (itemsList.get(position).getType() == MediaTypeConstant.getLinear(context)) {

            Drawable background = viewHolder.searchItemBinding.creatorLay.getBackground();
            PrintLogging.printLog(this.getClass(), "", "circleviewww if" + "--->>");
            if (background instanceof GradientDrawable) {
                PrintLogging.printLog(this.getClass(), "", "circleviewww else" + "--->>");
                // cast to 'ShapeDrawable'
                GradientDrawable shapeDrawable = (GradientDrawable) background;
                Random random = new Random();

                int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

                shapeDrawable.setColor(color);
            }
            viewHolder.searchItemBinding.itemImage.setVisibility(View.GONE);
            viewHolder.searchItemBinding.creatorLay.setVisibility(View.GONE);
            viewHolder.searchItemBinding.setSingleItem(itemsList.get(position));
            viewHolder.searchItemBinding.tvEpisode.setVisibility(View.VISIBLE);
        } else {
            viewHolder.searchItemBinding.creatorLay.setVisibility(View.GONE);
            viewHolder.searchItemBinding.setSingleItem(itemsList.get(position));
        }

        boolean isProviderAvailable = AssetContent.getHungamaTag(itemsList.get(position).getTags());
        if (isProviderAvailable){
            viewHolder.searchItemBinding.hungama.setVisibility(View.VISIBLE);
        }else {
            viewHolder.searchItemBinding.hungama.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        final SearchItemBinding searchItemBinding;

        SingleItemRowHolder(SearchItemBinding binding) {
            super(binding.getRoot());
            this.searchItemBinding = binding;
        }
    }


}
