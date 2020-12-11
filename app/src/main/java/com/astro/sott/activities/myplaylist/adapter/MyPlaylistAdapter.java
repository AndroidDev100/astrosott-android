package com.astro.sott.activities.myplaylist.adapter;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.ItemClickListener;
import com.astro.sott.callBacks.commonCallBacks.MediaTypeCallBack;
import com.astro.sott.databinding.PlaylistItemBinding;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.PrintLogging;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.MediaImage;

import java.util.ArrayList;

public class MyPlaylistAdapter extends RecyclerView.Adapter<MyPlaylistAdapter.SingleItemRowHolder> {
    private final ArrayList<RailCommonData> railList;
    private final ItemClickListener itemClickListener;
    private final Activity mContext;

    // This object helps you save/restore the open/close state of each view
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public MyPlaylistAdapter(Activity activity, ArrayList<RailCommonData> itemsList, ItemClickListener listener) {
        this.railList = itemsList;
        this.itemClickListener = listener;
        this.mContext = activity;
    }

    @NonNull
    @Override

    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        PlaylistItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.playlist_item, viewGroup, false);
        return new SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder singleItemRowHolder, int position) {


        RailCommonData singleItem = railList.get(position);
        Asset asset = singleItem.getObject();


        // Save/restore the open/close state.
        // You need to provide a String id which uniquely defines the data object.
        viewBinderHelper.bind(singleItemRowHolder.playlistItemBinding.swipeRevealLayout, String.valueOf(singleItem.getId()));
        if (asset.getImages() != null) {
            if (asset.getImages().size() > 0) {
                for(int i=0;i<asset.getImages().size();i++) {
                    MediaImage assetCommonImages = asset.getImages().get(i);
                    if (assetCommonImages.getRatio().equalsIgnoreCase("1:1")) {
                        ImageHelper.getInstance(singleItemRowHolder.playlistItemBinding.itemImage.getContext()).loadImageTo(singleItemRowHolder.playlistItemBinding.itemImage, assetCommonImages.getUrl(), R.drawable.square1);
                        break;
                    }
                }
            } else {
                ImageHelper.getInstance(singleItemRowHolder.playlistItemBinding.itemImage.getContext()).loadImageToNil(singleItemRowHolder.playlistItemBinding.itemImage, "", R.drawable.square1);
            }
        } else {
            ImageHelper.getInstance(singleItemRowHolder.playlistItemBinding.itemImage.getContext()).loadImageToNil(singleItemRowHolder.playlistItemBinding.itemImage, "", R.drawable.square1);
        }
        singleItemRowHolder.playlistItemBinding.tvTitle.setText(singleItem.getName());
        singleItemRowHolder.playlistItemBinding.tvEpisode.setText(AssetContent.getWatchListGenre(asset.getTags()));
        //   singleItemRowHolder.playlistItemBinding.setImage(assetCommonImages);
        //   singleItemRowHolder.playlistItemBinding.setTitle(singleItem);
        //  singleItemRowHolder.playlistItemBinding.tvEpisode.setText(AssetContent.getWatchListGenre(asset.getTags()));


    }

    @Override
    public int getItemCount() {

        return railList.size();
    }


    public class SingleItemRowHolder extends RecyclerView.ViewHolder {
        final PlaylistItemBinding playlistItemBinding;

        SingleItemRowHolder(@NonNull PlaylistItemBinding itemView) {
            super(itemView.getRoot());
            this.playlistItemBinding = itemView;
            final String name = mContext.getClass().getSimpleName();
            try {
                if (railList.size() > 0) {
                    playlistItemBinding.clRoot.setOnClickListener(view -> new ActivityLauncher(mContext).railClickCondition("","",name, railList.get(getLayoutPosition()), getLayoutPosition(), AppLevelConstants.Rail5,railList, new MediaTypeCallBack() {
                        @Override
                        public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {

                        }
                    }));
                    playlistItemBinding.flDeleteNotification.setOnClickListener(view -> itemClickListener.onClick(getAdapterPosition()));
                }
            }catch (IndexOutOfBoundsException e){
                PrintLogging.printLog("Exception",e.toString());
            }

        }
    }
}
