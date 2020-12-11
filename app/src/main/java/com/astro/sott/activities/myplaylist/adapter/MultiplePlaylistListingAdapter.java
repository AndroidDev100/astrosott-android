package com.astro.sott.activities.myplaylist.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.astro.sott.activities.myplaylist.ui.MultiplePlaylistActivity;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.beanModel.login.CommonResponse;
import com.astro.sott.callBacks.commonCallBacks.PlaylistCallback;
import com.astro.sott.databinding.MultiplePlaylistListingBinding;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MediaImage;

public class MultiplePlaylistListingAdapter extends RecyclerView.Adapter<MultiplePlaylistListingAdapter.SingleItemHolder> {

    Activity context;
    PlaylistCallback itemClickListener;
    private  CommonResponse commonResponse;
    private int mSelectedPosition = 0;

    public MultiplePlaylistListingAdapter(CommonResponse arrayList, MultiplePlaylistActivity myPlaylist, Activity context) {
        this.commonResponse = arrayList;
        this.context = context;
        this.itemClickListener = myPlaylist;
    }

    @NonNull
    @Override
    public SingleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {


        MultiplePlaylistListingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.multiple_playlist_listing, parent, false);
        return new SingleItemHolder(binding);
    }
    @SuppressLint("RestrictedApi")
    @Override
    public void onBindViewHolder(@NonNull SingleItemHolder singleItemHolder, int position) {
       RailCommonData railCommonData;
        if (commonResponse.getRailCommonData() != null) {

            if (position < commonResponse.getRailCommonData().size()) {

                railCommonData = commonResponse.getRailCommonData().get(position);
                Asset asset = railCommonData.getObject();
                singleItemHolder.multiplePlaylistItemBinding.tvTitle.setText(commonResponse.getPersonalLists().get(position).getName());

                int videOCount = commonResponse.getList().get(position);
                String count_String;
                if (videOCount == 1) {
                    count_String = videOCount + " Video";
                } else {
                    count_String = videOCount + " Videos";
                }
                singleItemHolder.multiplePlaylistItemBinding.tvEpisode.setText(count_String);
                singleItemHolder.multiplePlaylistItemBinding.clRoot.setOnClickListener(view -> {

                    itemClickListener.onClick(commonResponse.getPersonalLists().get(position).getName(), commonResponse.getPersonalLists().get(position).getPartnerListType());
                });
                if (asset.getImages() != null) {
                    if (asset.getImages().size() > 0) {
                        for (int i = 0; i < asset.getImages().size(); i++) {
                            MediaImage assetCommonImages = asset.getImages().get(i);
                            if (assetCommonImages.getRatio().equalsIgnoreCase("1:1")) {
                                ImageHelper.getInstance(singleItemHolder.multiplePlaylistItemBinding.itemImage.getContext()).loadImageTo(singleItemHolder.multiplePlaylistItemBinding.itemImage, assetCommonImages.getUrl(), R.drawable.square1);
                                break;
                            }
                        }
                    } else {
                        ImageHelper.getInstance(singleItemHolder.multiplePlaylistItemBinding.itemImage.getContext()).loadImageToNil(singleItemHolder.multiplePlaylistItemBinding.itemImage, "", R.drawable.square1);
                    }
                } else {
                    ImageHelper.getInstance(singleItemHolder.multiplePlaylistItemBinding.itemImage.getContext()).loadImageToNil(singleItemHolder.multiplePlaylistItemBinding.itemImage, "", R.drawable.square1);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        Log.e("","getItemCount"+commonResponse.getPersonalLists().size());
        return commonResponse.getPersonalLists().size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        MultiplePlaylistListingBinding multiplePlaylistItemBinding;

        public SingleItemHolder(@NonNull MultiplePlaylistListingBinding itemView) {
            super(itemView.getRoot());
            this.multiplePlaylistItemBinding = itemView;
        }
    }
}

