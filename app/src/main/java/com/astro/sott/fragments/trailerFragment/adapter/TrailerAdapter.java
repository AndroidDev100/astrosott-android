package com.astro.sott.fragments.trailerFragment.adapter;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.R;
import com.astro.sott.callBacks.commonCallBacks.TrailerAsset;
import com.astro.sott.databinding.TrailerItemBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.kaltura.client.types.Asset;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerItemHolder> {

    private List<Asset> assetList;
    private Context context;
    private TrailerAsset trailerAssetCallBack;
    private long lastClickTime = 0;


    public TrailerAdapter(Context mContext, List<Asset> assetList, TrailerAsset trailerAssetCallBack) {
        this.assetList = assetList;
        this.context = mContext;
        this.trailerAssetCallBack = trailerAssetCallBack;
    }


    @NonNull
    @Override
    public TrailerAdapter.TrailerItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        TrailerItemBinding trailerItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.trailer_item, viewGroup, false);

        return new TrailerItemHolder(trailerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.TrailerItemHolder holder, int i) {
        Asset asset = assetList.get(i);
        if (assetList.get(i).getImages() != null && assetList.get(i).getImages().size() > 0) {
            for (int imageCounter = 0; imageCounter < asset.getImages().size(); imageCounter++) {
                if (asset.getImages().get(imageCounter).getRatio().equalsIgnoreCase("16x9")) {
                    String image_url = asset.getImages().get(imageCounter).getUrl();
                    String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.trailer_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.trailer_image_height) + AppLevelConstants.QUALITY;
                    ImageHelper.getInstance(holder.trailerItemBinding.trailerImage.getContext()).loadImageTo(holder.trailerItemBinding.trailerImage, final_url, R.drawable.ic_landscape_placeholder);
                    break;
                }
            }
        }
        holder.trailerItemBinding.trailerName.setText(asset.getName());

        if (context.getResources().getBoolean(R.bool.isTablet)) {
            holder.trailerItemBinding.trailerDescription.setText(asset.getDescription());
            holder.trailerItemBinding.durationTxt.setText(AppCommonMethods.getURLDuration(asset));

        }
        holder.trailerItemBinding.playIcon.setOnClickListener(view -> {

            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();

            if (NetworkConnectivity.isOnline(context)) {

                trailerAssetCallBack.getTrailerAsset(asset);
            } else {
                ToastHandler.show(context.getResources().getString(R.string.no_internet_connection), context);
            }

        });


    }

    @Override
    public int getItemCount() {
        return assetList.size();
    }

    public class TrailerItemHolder extends RecyclerView.ViewHolder {

        final TrailerItemBinding trailerItemBinding;

        TrailerItemHolder(@NonNull TrailerItemBinding binding) {
            super(binding.getRoot());
            this.trailerItemBinding = binding;

        }


    }
}
