package com.dialog.dialoggo.fragments.schedule.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.forwardEPG.ForwardedEPGActivity;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.callBacks.SpecificAssetCallBack;
import com.dialog.dialoggo.callBacks.commonCallBacks.EpgNotAvailableCallBack;
import com.dialog.dialoggo.databinding.ScheduleItemBinding;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.AssetContent;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
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
        viewHolder.scheduleItemBinding.titleTxt.setText(commonData.getObject().getName());
        viewHolder.scheduleItemBinding.descriptionText.setText(commonData.getObject().getDescription());
        viewHolder.scheduleItemBinding.descriptionText.collapse();
        viewHolder.scheduleItemBinding.moreText.setText(R.string.more);
        String stTime = AppCommonMethods.getProgramTime(commonData.getObject(), 1);
        String endTime = AppCommonMethods.getProgramTime(commonData.getObject(), 2);
        viewHolder.scheduleItemBinding.descriptionText.post(() -> {
            int lineCount = viewHolder.scheduleItemBinding.descriptionText.getLineCount();
            if (lineCount > 0) {
                if (viewHolder.scheduleItemBinding.descriptionText.getLayout().getEllipsisCount(lineCount - 1) > 0) {
                    viewHolder.scheduleItemBinding.moreText.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.scheduleItemBinding.moreText.setVisibility(View.GONE);
                }
            }
        });






        String currentTime = AppCommonMethods.getCurrentTimeStamp();
        Long startTime = data.get(i).getObject().getStartDate();

        Boolean enable = ((ProgramAsset) data.get(i).getObject()).getEnableCatchUp();


        if (startTime > Long.valueOf(currentTime)) {
            value = new KsPreferenceKey(context).getReminderId(data.get(i).getObject().getId().toString());
            if(value == true){
                viewHolder.scheduleItemBinding.reminderIcon.setVisibility(View.VISIBLE);
                viewHolder.scheduleItemBinding.reminderIcon.setBackgroundResource(R.drawable.notificationiconred);
            } else {
                viewHolder.scheduleItemBinding.reminderIcon.setVisibility(View.VISIBLE);
                viewHolder.scheduleItemBinding.reminderIcon.setBackgroundResource(R.drawable.notificationicon);

            }

            //  viewHolder.scheduleItemBinding.playIcon.setVisibility(View.GONE);
        } else {
            viewHolder.scheduleItemBinding.reminderIcon.setVisibility(View.GONE);
            //   viewHolder.scheduleItemBinding.playIcon.setVisibility(View.VISIBLE);
        }


        if (enable) {
           // String currentTime = AppCommonMethods.getCurrentTimeStamp();
           // Long startTime = data.get(i).getObject().getStartDate();
            if (startTime > Long.valueOf(currentTime)) {
                 viewHolder.scheduleItemBinding.catchupIcon.setVisibility(View.GONE);
            } else {
                viewHolder.scheduleItemBinding.catchupIcon.setVisibility(View.VISIBLE);
            }
        } else {
            viewHolder.scheduleItemBinding.catchupIcon.setVisibility(View.GONE);
        }

        viewHolder.scheduleItemBinding.reminderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                value=new KsPreferenceKey(context).getReminderId(commonData.getObject().getId().toString());
                if (!data.get(i).isReminderEnabled() && value==false) {

                    callBack.getAsset(true,data.get(i).getObject());
                    data.get(i).setReminderEnabled(true);



                    ToastHandler.show(context.getResources().getString(R.string.reminder),context);

                    new KsPreferenceKey(context).setReminderId(commonData.getObject().getId().toString(),true);
                    viewHolder.scheduleItemBinding.reminderIcon.setBackgroundResource(R.drawable.notificationiconred);


                }else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                    builder.setTitle(context.getResources().getString(R.string.dialog));
                    if(context !=null) {
                        builder.setMessage(context.getResources().getString(R.string.remove_alarm))
                                .setCancelable(true)
                                .setPositiveButton(context.getString(R.string.yes), (dialog, id) -> {
                                    callBack.cancelReminder(data.get(i).getObject());
                                    Log.d("dataIsForRemin",data.get(i).getObject().getId().toString());
                                    data.get(i).setReminderEnabled(false);
                                    new KsPreferenceKey(context).setReminderId(commonData.getObject().getId().toString(),false);
                                    viewHolder.scheduleItemBinding.reminderIcon.setBackgroundResource(R.drawable.notificationicon);

                                })
                                .setNegativeButton(context.getString(R.string.no), (dialog, id) -> dialog.cancel());
                        AlertDialog alert = builder.create();
                        alert.show();
                        Button bn = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                        bn.setTextColor(ContextCompat.getColor(context, R.color.white));
                        Button bp = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                        bp.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    }


                }
            }
        });



        String totalDuration = AppCommonMethods.getProgramDurtion(endTime, stTime);

        viewHolder.scheduleItemBinding.moreText.setOnClickListener(view -> {
            viewHolder.scheduleItemBinding.moreText.setText(viewHolder.scheduleItemBinding.descriptionText.isExpanded() ? R.string.title_more : R.string.less);
            viewHolder.scheduleItemBinding.descriptionText.toggle();
        });

        viewHolder.scheduleItemBinding.start.setText(AppCommonMethods.setTime(commonData.getObject(), 1));
        viewHolder.scheduleItemBinding.end.setText(AppCommonMethods.setTime(commonData.getObject(), 2));
        viewHolder.scheduleItemBinding.totalDurationTxt.setText(totalDuration);

        if (AssetContent.getProgramGenre(commonData.getObject().getTags()).equals("")) {
            viewHolder.scheduleItemBinding.genreLayout.setVisibility(View.GONE);
        } else {
            viewHolder.scheduleItemBinding.genreLayout.setVisibility(View.VISIBLE);
        }


        viewHolder.scheduleItemBinding.genreTxt.setText(AssetContent.getProgramGenre(commonData.getObject().getTags()));
        if (mPosition == i) {
            liveNowIndex = i;
           // viewHolder.scheduleItemBinding.playIcon.setVisibility(View.GONE);
            viewHolder.scheduleItemBinding.mainLay.setBackgroundResource(R.color.search_toolbar_background);
            viewHolder.scheduleItemBinding.livenowLay.setVisibility(View.VISIBLE);
            viewHolder.scheduleItemBinding.catchupIcon.setVisibility(View.GONE);
            setTimeTextColor(viewHolder.scheduleItemBinding, 1);
        } else {
            viewHolder.scheduleItemBinding.livenowLay.setVisibility(View.GONE);
            viewHolder.scheduleItemBinding.mainLay.setBackgroundResource(R.color.fragment_background_color);
            setTimeTextColor(viewHolder.scheduleItemBinding, 2);

        }
    }


    private void setTimeTextColor(ScheduleItemBinding scheduleItemBinding, int type) {
        if (type == 1) {
            scheduleItemBinding.startHint.setTextColor(context.getResources().getColor(R.color.primary_blue));
            scheduleItemBinding.endHint.setTextColor(context.getResources().getColor(R.color.primary_blue));
            scheduleItemBinding.start.setTextColor(context.getResources().getColor(R.color.primary_blue));
            scheduleItemBinding.end.setTextColor(context.getResources().getColor(R.color.primary_blue));

        } else {
            scheduleItemBinding.startHint.setTextColor(context.getResources().getColor(R.color.white));
            scheduleItemBinding.endHint.setTextColor(context.getResources().getColor(R.color.white));
            scheduleItemBinding.start.setTextColor(context.getResources().getColor(R.color.white));
            scheduleItemBinding.end.setTextColor(context.getResources().getColor(R.color.white));

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

            itemView.getRoot().setOnClickListener(view -> {

                if (SystemClock.elapsedRealtime() - lastClickTime < 3000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                if (liveNowIndex == getLayoutPosition()) {

                } else {
                    if (liveNowIndex == -1) {
                        String currentTime = AppCommonMethods.getCurrentTimeStamp();
                        Long startTime = data.get(getLayoutPosition()).getObject().getStartDate();

                        if (startTime > Long.valueOf(currentTime)) {

                            new ActivityLauncher(context).forwardeEPGActivity(context, ForwardedEPGActivity.class, data.get(getLayoutPosition()));

                        } else {
                            if (((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp()) {
                                callBacks.itemClicked(((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp(),data.get(getLayoutPosition()));
                                // new ActivityLauncher(context).catchUpActivity(context, CatchupActivity.class, data.get(getLayoutPosition()));
                            } else {
                           // callBacks.itemClicked(((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp(),null);

                                callBacks.itemClicked(((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp(),data.get(getLayoutPosition()));
                            }

                        }
                    } else {
                        Long startTime = data.get(getLayoutPosition()).getObject().getStartDate();
                        Long liveNowStartTime = data.get(liveNowIndex).getObject().getStartDate();
                        if (startTime > liveNowStartTime) {
                          //  scheduleItemBinding.playIcon.setVisibility(View.GONE);

                            new ActivityLauncher(context).forwardeEPGActivity(context, ForwardedEPGActivity.class, data.get(getLayoutPosition()));

                        } else {
                            if (((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp()) {

                                callBacks.itemClicked(((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp(),data.get(getLayoutPosition()));

                                // new ActivityLauncher(context).catchUpActivity(context, CatchupActivity.class, data.get(getLayoutPosition()));
                            } else {
                              //  callBacks.itemClicked(((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp(),null);
                                callBacks.itemClicked(((ProgramAsset) data.get(getLayoutPosition()).getObject()).getEnableCatchUp(),data.get(getLayoutPosition()));

                            }

                        }
                    }

                }

            });

        }

    }
}
