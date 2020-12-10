package com.dialog.dialoggo.adapter;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonImages;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.callBacks.commonCallBacks.ContinueWatchingRemove;
import com.dialog.dialoggo.callBacks.commonCallBacks.DetailRailClick;
import com.dialog.dialoggo.callBacks.commonCallBacks.MediaTypeCallBack;
import com.dialog.dialoggo.databinding.LandscapeItemBinding;
import com.dialog.dialoggo.modelClasses.dmsResponse.MediaTypes;
import com.dialog.dialoggo.modelClasses.dmsResponse.ResponseDmsModel;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.AssetContent;
import com.dialog.dialoggo.utils.helpers.ImageHelper;
import com.dialog.dialoggo.utils.helpers.MediaTypeConstant;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonLandscapeAdapter extends RecyclerView.Adapter<CommonLandscapeAdapter.SingleItemRowHolder> {

    private final int layoutType;
    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private long lastClickTime = 0;
    private DetailRailClick detailRailClick;



    public CommonLandscapeAdapter(Activity context, List<RailCommonData> itemsList, int type, ContinueWatchingRemove callBack, int cwIndex, String railName, boolean isContinueWatchingRail) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        try {
            this.detailRailClick = ((DetailRailClick) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    public CommonLandscapeAdapter(Activity context, List<RailCommonData> itemsList, int type) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        try {
            this.detailRailClick = ((DetailRailClick) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }


    ResponseDmsModel responseDmsModel;
    private int continueWatchingIndex = -1;
    private boolean isContinueWatchingRail;
    private String strRailName;
    MediaTypes mediaTypes;
    public CommonLandscapeAdapter(Activity context, List<RailCommonData> itemsList, int type, String railName) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        String name = mContext.getClass().getSimpleName();
        strRailName = railName;
        this.isContinueWatchingRail = false;
        try {
            responseDmsModel = AppCommonMethods.callpreference(mContext);
            mediaTypes = responseDmsModel.getParams().getMediaTypes();
            this.detailRailClick = ((DetailRailClick) context);


        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }


    @NonNull
    @Override
    public SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LandscapeItemBinding landscapeItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.landscape_item, parent, false);
        return new SingleItemRowHolder(landscapeItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemRowHolder holder, int i) {
        RailCommonData singleItem = itemsList.get(i);
        PrintLogging.printLog(CommonLandscapeAdapter.class,"",itemsList.get(0).getType()+"assettypeassest");
        try {

            boolean isProviderAvailable = AssetContent.getHungamaTag(singleItem.getObject().getTags());
            if (isProviderAvailable){
                holder.landscapeItemBinding.hungama.setVisibility(View.VISIBLE);
            }else {
                holder.landscapeItemBinding.hungama.setVisibility(View.GONE);
            }

            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                // holder.landscapeItemBinding.setImage(assetCommonImages);
                // Glide.with(mContext).load(assetCommonImages.getImageUrl()).into(holder.landscapeItemBinding.itemImage);
                ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageToLandscape(holder.landscapeItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.landscape);

            } else {
                ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageToPlaceholder(holder.landscapeItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.landscape, holder.landscapeItemBinding.itemImage), R.drawable.landscape);

            }
            if(itemsList.get(i).getType()==MediaTypeConstant.getProgram(mContext)){
                holder.landscapeItemBinding.livenowLay.setVisibility(View.VISIBLE);
            }else {
                holder.landscapeItemBinding.livenowLay.setVisibility(View.GONE);
            }
            getPremimumMark(i,holder.landscapeItemBinding);
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }

    }

    private void getPremimumMark(int position, LandscapeItemBinding landscapeItemBinding) {

        landscapeItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
        Map<String, Value> map = itemsList.get(position).getObject().getMetas();

        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;
        while (itr.hasNext()) {
            key = (String) itr.next();
            if (key.equalsIgnoreCase("Is Exclusive")) {
                landscapeItemBinding.exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                BooleanValue doubleValue = (BooleanValue) map.get(key);

                if (doubleValue.getValue()) {
                    landscapeItemBinding.exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                } else {
                    landscapeItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
                }

            }
        }
    }
    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final LandscapeItemBinding landscapeItemBinding;

        private SingleItemRowHolder(@NonNull LandscapeItemBinding itemView) {
            super(itemView.getRoot());
            this.landscapeItemBinding = itemView;

            final String name = mContext.getClass().getSimpleName();
            landscapeItemBinding.getRoot().setOnClickListener(view -> {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                new ActivityLauncher(mContext).railClickCondition("","",name, itemsList.get(getLayoutPosition()),getLayoutPosition(), layoutType,itemsList, new MediaTypeCallBack() {
                    @Override
                    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {
                        if (NetworkConnectivity.isOnline(mContext)) {
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
