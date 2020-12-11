package com.astro.sott.activities.videoQuality.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.databinding.VideoQualityItemBinding;
import com.astro.sott.player.adapter.TrackItem;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;

import java.util.List;

public class VideoQualityAdapter extends RecyclerView.Adapter<VideoQualityAdapter.SingleItemRowHolder> {


    private final List<TrackItem> list;
    private final Context context;

    public VideoQualityAdapter(Context context, List<TrackItem> list) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


        VideoQualityItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.video_quality_item, viewGroup, false);

        return new SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull final SingleItemRowHolder viewHolder, final int position) {
        viewHolder.notificationItemBinding.titleText.setText(list.get(position).getTrackName());
        viewHolder.notificationItemBinding.description.setText(list.get(position).getTrackDescription());


        if (list.get(position).isSelected()) {
            viewHolder.notificationItemBinding.tick.setBackgroundResource(R.drawable.tick);
        } else {
            viewHolder.notificationItemBinding.tick.setBackgroundResource(0);
            //viewHolder.notificationItemBinding.titleText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }

        viewHolder.notificationItemBinding.videoQualityLayout.setOnClickListener(view -> {

            int index = list.indexOf(new TrackItem("", "", true));

            if (index == -1) {

                list.get(position).setSelected(true);
            } else {
                list.get(index).setSelected(false);
                list.get(position).setSelected(true);

            }
            KsPreferenceKey.getInstance(context).setQualityPosition(position);
            KsPreferenceKey.getInstance(context).setQualityName(list.get(position).getTrackName());
            notifyDataSetChanged();

//                if (position == 0) {
//                    MoengageManager.getInstance().trackSelectionEvent(ApplicationMain.getAppContext().getResources().getString(R.string.auto));
//                } else if (position == 1) {
//                    MoengageManager.getInstance().trackSelectionEvent(ApplicationMain.getAppContext().getResources().getString(R.string.low));
//
//                } else if (position == 2) {
//                    MoengageManager.getInstance().trackSelectionEvent(ApplicationMain.getAppContext().getResources().getString(R.string.medium));
//
//                } else if (position == 3) {
//                    MoengageManager.getInstance().trackSelectionEvent(ApplicationMain.getAppContext().getResources().getString(R.string.high));
//
//                }
//                GAManager.getInstance().setEvent(GAManager.SETTINGS, GAManager.UPDATE_STREAMING_SETTING, GAManager.EDIT_SETTINGS, GAManager.five);
//                pos = position;
//                new KsPreferenceKeys(ApplicationMain.getAppContext()).setQualityPosition(pos);
//                new KsPreferenceKeys(ApplicationMain.getAppContext()).setQualityName(list.get(position).getTrackName());
//                itemClickListener.onClick("", "");

        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final VideoQualityItemBinding notificationItemBinding;

        private SingleItemRowHolder(VideoQualityItemBinding binding) {
            super(binding.getRoot());
            this.notificationItemBinding = binding;

        }

    }

}

