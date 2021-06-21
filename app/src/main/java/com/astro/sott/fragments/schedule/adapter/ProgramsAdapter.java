package com.astro.sott.fragments.schedule.adapter;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.astro.sott.callBacks.SpecificAssetCallBack;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.R;
import com.astro.sott.activities.forwardEPG.ForwardedEPGActivity;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.EpgNotAvailableCallBack;
import com.astro.sott.databinding.ScheduleItemBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.kaltura.client.types.ProgramAsset;

import java.util.List;

public class ProgramsAdapter extends RecyclerView.Adapter<ProgramsAdapter.SingleItemRowHolder> {

    private final Activity context;
    private final List<RailCommonData> data;
    private int mPosition;
    private long lastClickTime = 0;
    EpgNotAvailableCallBack callBacks;
    private int liveNowIndex = -1;
    private SpecificAssetCallBack callBack;
    private boolean value;

    public ProgramsAdapter(Activity mContext,
                           List<RailCommonData> railData, int pos, EpgNotAvailableCallBack callBack, SpecificAssetCallBack specificAssetCallBack) {
        this.context = mContext;
        this.data = railData;
        this.callBacks = callBack;
        this.mPosition = pos;
        this.callBack = specificAssetCallBack;
    }


    @NonNull
    @Override
    public ProgramsAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ScheduleItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.schedule_item, viewGroup, false);
        return new SingleItemRowHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull final SingleItemRowHolder viewHolder, int i) {
        RailCommonData commonData = data.get(i);
        viewHolder.scheduleItemBinding.epgTitle.setText(commonData.getObject().getName());

        for (int count = 0; count < commonData.getObject().getImages().size(); count++) {
            if (commonData.getObject().getImages().get(count).getRatio().equals("16x9")) {
                String image_url = commonData.getObject().getImages().get(count).getUrl();
                String final_url = image_url + AppLevelConstants.WIDTH + (int) context.getResources().getDimension(R.dimen.detail_image_width) + AppLevelConstants.HEIGHT + (int) context.getResources().getDimension(R.dimen.carousel_image_height) + AppLevelConstants.QUALITY;
                ImageHelper.getInstance(viewHolder.scheduleItemBinding.image.getContext()).loadImageToPotrait(viewHolder.scheduleItemBinding.image, final_url, R.drawable.square1);
            }
        }
        viewHolder.scheduleItemBinding.startEndTime.setText(AppCommonMethods.setTime(commonData.getObject(), 1) + " - " + AppCommonMethods.setTime(commonData.getObject(), 0));
        viewHolder.scheduleItemBinding.descriptionText.setText(commonData.getObject().getDescription());
        //viewHolder.scheduleItemBinding.descriptionText.collapse();
        String stTime = AppCommonMethods.getProgramTime(commonData.getObject(), 1);
        String endTime = AppCommonMethods.getProgramTime(commonData.getObject(), 2);

        viewHolder.scheduleItemBinding.descriptionText.post(() -> {
            int lineCount = viewHolder.scheduleItemBinding.descriptionText.getLineCount();
            if (lineCount > 1) {
                viewHolder.scheduleItemBinding.descriptionText.setEllipsize(TextUtils.TruncateAt.END);
            }
        });

        viewHolder.scheduleItemBinding.moreButton.setOnClickListener(v -> {
            viewHolder.scheduleItemBinding.descriptionText.toggle();
            if (viewHolder.scheduleItemBinding.descriptionText.isExpanded()) {
                viewHolder.scheduleItemBinding.descriptionText.setEllipsize(null);
                viewHolder.scheduleItemBinding.moreButton.setText("See Less");
            } else {
                viewHolder.scheduleItemBinding.descriptionText.setEllipsize(TextUtils.TruncateAt.END);
                viewHolder.scheduleItemBinding.moreButton.setText("See More");

            }
        });
        viewHolder.scheduleItemBinding.share.setOnClickListener(v -> {
            try {
                FirebaseEventManager.getFirebaseInstance(context).shareEvent(commonData.getObject());
            } catch (Exception e) {

            }
            AppCommonMethods.openShareDialog(context, data.get(i).getObject(), context, "");
        });


        Long currentTime = AppCommonMethods.getCurrentTimeStampLong();
        Long startTime = data.get(i).getObject().getStartDate();
        Log.e("currentTime", String.valueOf(Long.valueOf(currentTime)));
        Log.e("startTime", String.valueOf(startTime));

        Boolean enable = ((ProgramAsset) data.get(i).getObject()).getEnableCatchUp();

        if (startTime > currentTime) {
            viewHolder.scheduleItemBinding.playIcon.setVisibility(View.GONE);

        }


    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateLiveChannelCount(int position) {
        this.mPosition = position;
    }


    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final ScheduleItemBinding scheduleItemBinding;

        private SingleItemRowHolder(ScheduleItemBinding itemView) {
            super(itemView.getRoot());

            scheduleItemBinding = itemView;

//            itemView.getRoot().setOnClickListener(view -> {
//
//                if (SystemClock.elapsedRealtime() - lastClickTime < 3000) {
//                    return;
//                }
//                lastClickTime = SystemClock.elapsedRealtime();
//
//                if (liveNowIndex == getLayoutPosition()) {
//
//                } else {
//                    if (liveNowIndex == -1) {
//                        String currentTime = AppCommonMethods.getCurrentTimeStamp();
//                        Long startTime = data.get(getLayoutPosition()).getObject().getStartDate();
//
//                        if (startTime > Long.valueOf(currentTime)) {
//
//                            // new ActivityLauncher(context).forwardeEPGActivity(context, ForwardedEPGActivity.class, data.get(getLayoutPosition()));
//
//                        } else {
//                            if (((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp()) {
//                                callBacks.itemClicked(((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp(), data.get(getLayoutPosition()));
//                                // new ActivityLauncher(context).catchUpActivity(context, CatchupActivity.class, data.get(getLayoutPosition()));
//                            } else {
//                                // callBacks.itemClicked(((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp(),null);
//
//                                callBacks.itemClicked(((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp(), data.get(getLayoutPosition()));
//                            }
//
//                        }
//                    } else {
//                        Long startTime = data.get(getLayoutPosition()).getObject().getStartDate();
//                        Long liveNowStartTime = data.get(liveNowIndex).getObject().getStartDate();
//                        if (startTime > liveNowStartTime) {
//                            //  scheduleItemBinding.playIcon.setVisibility(View.GONE);
//
//                            // new ActivityLauncher(context).forwardeEPGActivity(context, ForwardedEPGActivity.class, data.get(getLayoutPosition()));
//
//                        } else {
//                            if (((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp()) {
//
//                                callBacks.itemClicked(((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp(), data.get(getLayoutPosition()));
//
//                                // new ActivityLauncher(context).catchUpActivity(context, CatchupActivity.class, data.get(getLayoutPosition()));
//                            } else {
//                                //  callBacks.itemClicked(((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp(),null);
//                                callBacks.itemClicked(((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp(), data.get(getLayoutPosition()));
//
//                            }
//
//                        }
//                    }
//
//                }
//
//            });

        }

    }
}
