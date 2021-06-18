package com.astro.sott.fragments.ShowFragment.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.MediaTypeCallBack;
import com.astro.sott.databinding.SeriesShowItemBinding;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.ToastHandler;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MediaImage;

import java.util.List;

public class MovieShowsAdapter extends RecyclerView.Adapter<MovieShowsAdapter.SingleItemViewHolder> {
    private List<RailCommonData> similarItemList;
    private Activity mContext;
    private boolean isMovieShow;
    private DetailRailClick detailRailClick;


    public MovieShowsAdapter(Activity context, List<RailCommonData> loadedList) {
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
        SeriesShowItemBinding landscapeItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.series_show_item, parent, false);
        return new SingleItemViewHolder(landscapeItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemViewHolder holder, int position) {
        if (similarItemList.get(position).getObject().getMediaFiles() != null && similarItemList.get(position).getObject().getMediaFiles().size() > 0) {
            List<MediaImage> media = similarItemList.get(position).getObject().getImages();
            for (MediaImage mediaFile : media) {
                if (mediaFile.getRatio().equalsIgnoreCase("2x3")) {
                    String image_url = mediaFile.getUrl();
                    String final_url = image_url + AppLevelConstants.WIDTH + (int) 200 + AppLevelConstants.HEIGHT + (int) 300 + AppLevelConstants.QUALITY;
                    ImageHelper.getInstance(holder.landscapeItemBinding.image.getContext()).loadImageTo(holder.landscapeItemBinding.image, final_url, R.drawable.ic_potrait_placeholder);
                }
            }
        }
        holder.landscapeItemBinding.lanscapeTitle.setText(similarItemList.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return similarItemList.size();
    }


    class SingleItemViewHolder extends RecyclerView.ViewHolder {
        SeriesShowItemBinding landscapeItemBinding;

        public SingleItemViewHolder(@NonNull SeriesShowItemBinding itemView) {
            super(itemView.getRoot());
            landscapeItemBinding = itemView;
            final String name = mContext.getClass().getSimpleName();


            landscapeItemBinding.cardView.setOnClickListener(v -> {
                new ActivityLauncher(mContext).railClickCondition("", "", name, similarItemList.get(getLayoutPosition()), getLayoutPosition(), AppConstants.Rail5, similarItemList, new MediaTypeCallBack() {
                    @Override
                    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {
                        if (NetworkConnectivity.isOnline(mContext)) {
                            try {
                            FirebaseEventManager.getFirebaseInstance(mContext).showTabEvent(commonData.getObject());
                            }catch (Exception ex){}
                            detailRailClick.detailItemClicked(_url, position, type, commonData);
                        } else {
                            ToastHandler.show(mContext.getResources().getString(R.string.no_internet_connection), mContext);
                        }
                    }
                });
            });

        }


    }

}
