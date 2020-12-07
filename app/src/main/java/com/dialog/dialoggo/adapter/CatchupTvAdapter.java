package com.dialog.dialoggo.adapter;

import android.content.Context;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.callBacks.commonCallBacks.CatchupCallBack;
import com.dialog.dialoggo.databinding.CatchUpItemBinding;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.ImageHelper;
import com.dialog.dialoggo.utils.helpers.shimmer.Constants;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;
import com.kaltura.client.types.Asset;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CatchupTvAdapter extends RecyclerView.Adapter<CatchupTvAdapter.SingleItemRowHolder> {

    private static final String TAG = "CatchupTvAdapter";
    private final Context context;
    private List<RailCommonData> commonData;
    private CatchupCallBack catchupCallBack;
    private long lastClickTime = 0;
    private Asset singleItem;
    private Boolean isLivePlayer;
    private String currentProgramId;
    private boolean value;

    public CatchupTvAdapter(Context context, List<RailCommonData> railCommonData, Boolean isLivePlayer, String currentProgramId, CatchupCallBack callBack) {
        this.context = context;
        this.commonData = railCommonData;
        this.catchupCallBack = callBack;
        this.isLivePlayer = isLivePlayer;
        this.currentProgramId = currentProgramId;
    }

    @NonNull
    @Override
    public CatchupTvAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        CatchUpItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.catch_up_item, viewGroup, false);

        return new CatchupTvAdapter.SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull final CatchupTvAdapter.SingleItemRowHolder viewHolder, final int position) {
        singleItem = commonData.get(position).getObject();
        // viewHolder.catchUpItemBinding
        String stTime = AppCommonMethods.getProgramTime(singleItem, 1);
        String endTime = AppCommonMethods.getProgramTime(singleItem, 2);
        String currentTime = getDateTimeStamp(System.currentTimeMillis());
        String duration = AppCommonMethods.getProgramDurtion(endTime, stTime);
        String title = commonData.get(position).getTilte();
        Long date = singleItem.getStartDate();
        String date1 = getDate(date);
        int id = Constants.currentProgramID;

        value = new KsPreferenceKey(context).getLiveCatchUpId(singleItem.getId().toString());

        if (value) {
            viewHolder.catchUpItemBinding.txtLive.setVisibility(View.VISIBLE);

        }else {
            viewHolder.catchUpItemBinding.txtLive.setVisibility(View.GONE);
            new KsPreferenceKey(context).setLiveCatchUpId(singleItem.getId().toString(), false);
        }

        try {
            if (singleItem.getImages().get(0).getRatio().equalsIgnoreCase("16:9")) {
                String imageUrl = singleItem.getImages().get(0).getUrl();

                viewHolder.catchUpItemBinding.txtTitle.setText(singleItem.getName());
                viewHolder.catchUpItemBinding.txtDate.setText(date1 + " " + "|" + " " + duration);

//                if (currentProgramId.equalsIgnoreCase(singleItem.getId().toString()) && id == singleItem.getId() && isLivePlayer) {
//                    viewHolder.catchUpItemBinding.txtLive.setVisibility(View.VISIBLE);
//                    viewHolder.catchUpItemBinding.catchupItem.setBackgroundResource(R.drawable.border_live_line);
//                    viewHolder.catchUpItemBinding.txtNowPlaying.setVisibility(View.VISIBLE);
//                }
                if (currentProgramId.equalsIgnoreCase(singleItem.getId().toString())) {
                    viewHolder.catchUpItemBinding.catchupItem.setBackgroundResource(R.drawable.border_live_line);
                    viewHolder.catchUpItemBinding.txtNowPlaying.setVisibility(View.VISIBLE);
                }

                if (imageUrl.contains("https")) {
                    ImageHelper.getInstance(viewHolder.catchUpItemBinding.itemImage.getContext()).loadImageTo(viewHolder.catchUpItemBinding.itemImage, imageUrl, R.drawable.landscape);
                }
            }


        } catch (Exception e) {

        }

        viewHolder.catchUpItemBinding.catchupItem.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                return;
            }
            lastClickTime = SystemClock.elapsedRealtime();
            catchupCallBack.catchupCallback("", commonData.get(position), commonData.get(position).getName());
        });
    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    private String getDateTimeStamp(Long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm a", Locale.US);
        return formatter.format(timeStamp);
    }

    @Override
    public int getItemCount() {
        return commonData.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final CatchUpItemBinding catchUpItemBinding;

        SingleItemRowHolder(CatchUpItemBinding binding) {
            super(binding.getRoot());
            this.catchUpItemBinding = binding;
        }

    }


}

