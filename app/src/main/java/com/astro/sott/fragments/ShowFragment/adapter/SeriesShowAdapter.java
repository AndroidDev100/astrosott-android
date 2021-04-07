package com.astro.sott.fragments.ShowFragment.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.RelatedItemBinding;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.ImageHelper;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MediaImage;

import java.util.List;

public class SeriesShowAdapter extends RecyclerView.Adapter<SeriesShowAdapter.SingleItemViewHolder> {
    private List<Asset> similarItemList;
    private Activity mContext;
    private boolean isMovieShow;
    private DetailRailClick detailRailClick;


    public SeriesShowAdapter(Activity context, List<Asset> loadedList, boolean isMovieShow) {
        similarItemList = loadedList;
        this.isMovieShow = isMovieShow;
        mContext = context;
        try {
            this.detailRailClick = ((DetailRailClick) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @NonNull
    @Override
    public SingleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelatedItemBinding landscapeItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.related_item, parent, false);
        return new SingleItemViewHolder(landscapeItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemViewHolder holder, int position) {
        if (similarItemList.get(position).getMediaFiles() != null && similarItemList.get(position).getMediaFiles().size() > 0) {
            List<MediaImage> media = similarItemList.get(position).getImages();
            for (MediaImage mediaFile : media) {
                if (mediaFile.getRatio().equalsIgnoreCase("16x9")) {
                    String image_url = mediaFile.getUrl();
                    String final_url = image_url + AppLevelConstants.WIDTH + (int) mContext.getResources().getDimension(R.dimen.landscape_image_width) + AppLevelConstants.HEIGHT + (int) mContext.getResources().getDimension(R.dimen.landscape_image_height) + AppLevelConstants.QUALITY;
                    ImageHelper.getInstance(holder.landscapeItemBinding.image.getContext()).loadImageTo(holder.landscapeItemBinding.image, final_url, R.drawable.landscape);
                }
            }
        }

    }


    @Override
    public int getItemCount() {
        return similarItemList.size();
    }


    class SingleItemViewHolder extends RecyclerView.ViewHolder {
        RelatedItemBinding landscapeItemBinding;

        public SingleItemViewHolder(@NonNull RelatedItemBinding itemView) {
            super(itemView.getRoot());
            landscapeItemBinding = itemView;
            final String name = mContext.getClass().getSimpleName();


            landscapeItemBinding.cardView.setOnClickListener(v -> {

            });

        }


    }

}
