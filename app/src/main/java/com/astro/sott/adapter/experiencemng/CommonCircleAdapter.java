package com.astro.sott.adapter.experiencemng;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.ContinueWatchingRemove;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.CircularItemBinding;
import com.astro.sott.modelClasses.dmsResponse.MediaTypes;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;

import java.util.List;
import java.util.Random;

public class CommonCircleAdapter extends RecyclerView.Adapter<CommonCircleAdapter.SingleItemRowHolder> {

    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private String strMenuNavigationName = "";
    private DetailRailClick detailRailClick;
    private final int layoutType;
    private long lastClickTime = 0;
    private String strRailName;

    private boolean isContinueWatchingRail;
    private ContinueWatchingRemove watchingRemove;
    private int continueWatchingIndex = -1;
    private int position;
    private ResponseDmsModel responseDmsModel;
    private MediaTypes mediaTypes;

    public CommonCircleAdapter(Activity context, List<RailCommonData> itemsList, int type, ContinueWatchingRemove callBack, int cwIndex, String railName, boolean isContinueWatchingRail) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        continueWatchingIndex = cwIndex;
        strRailName = railName;
        this.isContinueWatchingRail = isContinueWatchingRail;
        try {
            this.watchingRemove = callBack;
            this.detailRailClick = ((DetailRailClick) context);

            responseDmsModel = AppCommonMethods.callpreference(mContext);
            mediaTypes = responseDmsModel.getParams().getMediaTypes();

            Fragment f = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.content_frame);
            /*if (f instanceof HomeFragment) {
                strMenuNavigationName = mContext.getResources().getString(R.string.title_home);
            } else if (f instanceof ExclusiveFragment) {
                strMenuNavigationName = mContext.getResources().getString(R.string.title_exclusives);
            } else if (f instanceof SpotlightFragment) {
                strMenuNavigationName = mContext.getResources().getString(R.string.Live_Tv);
            } else if (f instanceof SunburnFragment) {
                strMenuNavigationName = mContext.getResources().getString(R.string.title_spotlight);
            }*/
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }


    public CommonCircleAdapter(Activity context, List<RailCommonData> itemsList, int type, String railName) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        strRailName = railName;
        this.isContinueWatchingRail = false;
        try {
            this.detailRailClick = ((DetailRailClick) context);

            Fragment f = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.content_frame);
            /*if (f instanceof HomeFragment) {
                strMenuNavigationName = mContext.getResources().getString(R.string.title_home);
            } else if (f instanceof ExclusiveFragment) {
                strMenuNavigationName = mContext.getResources().getString(R.string.title_exclusives);
            } else if (f instanceof SpotlightFragment) {
                strMenuNavigationName = mContext.getResources().getString(R.string.Live_Tv);
            } else if (f instanceof SunburnFragment) {
                strMenuNavigationName = mContext.getResources().getString(R.string.title_spotlight);
            }*/
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int i) {
        CircularItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.circular_item, parent, false);
        return new SingleItemRowHolder(binding);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        try {
            RailCommonData singleItem = itemsList.get(i);
            PrintLogging.printLog("circleAdapter ",singleItem.getObject().getId()+"");
            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                PrintLogging.printLog("", assetCommonImages.getImageUrl());
                // holder.circularItemBinding.setImage(assetCommonImages);
                ImageHelper.getInstance(holder.circularItemBinding.itemImage.getContext()).loadImageTo(holder.circularItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.square1);


               ImageHelper.getInstance(mContext).setImageDescription(holder.circularItemBinding.itemImage,assetCommonImages.getImageUrl());

            }else
            {


            }
            String name = singleItem.getCreatorName();
            if (name == null) {
                if (singleItem.getObject().getType() == MediaTypeConstant.getLinear(mContext)) {
                    holder.circularItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
                } else {

                }
                holder.circularItemBinding.setTile(singleItem);
                holder.circularItemBinding.itemImage.setVisibility(View.VISIBLE);
                holder.circularItemBinding.creatorLay.setVisibility(View.GONE);
                holder.circularItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(i).getName());
                holder.circularItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            } else {
                Drawable background = holder.circularItemBinding.creatorLay.getBackground();
                PrintLogging.printLog("", "circleviewww if" + "--->>");
                if (background instanceof GradientDrawable) {
                    PrintLogging.printLog("", "circleviewww else" + "--->>");
                    // cast to 'ShapeDrawable'
                    GradientDrawable shapeDrawable = (GradientDrawable) background;
                    Random random = new Random();

                    int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

                    shapeDrawable.setColor(color);
                }

                holder.circularItemBinding.creatorLay.setVisibility(View.VISIBLE);
                holder.circularItemBinding.mediaTypeLayout.lineOne.setText(AppCommonMethods.getCteatorNameCaps(itemsList.get(i).getName().trim()));
                holder.circularItemBinding.mediaTypeLayout.lineOne.setGravity(Gravity.CENTER);
                holder.circularItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
                holder.circularItemBinding.itemImage.setVisibility(View.GONE);
                holder.circularItemBinding.creatorName.setText(name);
            }

            if (isContinueWatchingRail) {
                checkContinueWatching(i, holder.circularItemBinding);
            }

        } catch (Exception e) {
            // Log.w("trendingImage", e.toString());
            holder.circularItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
        }

    }
    private void checkContinueWatching(int position, CircularItemBinding potraitItemBinding) {
        if (isContinueWatchingRail) {
            potraitItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.deleteIcon.setVisibility(View.VISIBLE);
            potraitItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.VISIBLE);
            potraitItemBinding.ivPlayVideo.setVisibility(View.VISIBLE);
            potraitItemBinding.progressBar.setVisibility(View.VISIBLE);
            potraitItemBinding.progressBar.setProgress(itemsList.get(position).getPosition());
        } else {
            potraitItemBinding.mediaTypeLayout.deleteIcon.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
            potraitItemBinding.progressBar.setVisibility(View.GONE);
            potraitItemBinding.ivPlayVideo.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }


    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final CircularItemBinding circularItemBinding;

        SingleItemRowHolder(CircularItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            circularItemBinding = circularItemBind;
            final String name = mContext.getClass().getSimpleName();
            circularItemBinding.getRoot().setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
               // GAManager.getInstance().setEvent(GAManager.BROWSING, GAManager.RAIL_ASSET_CLICKED, GAManager.RAIL_NAVIGATION, GAManager.zero);
                new ActivityLauncher(mContext).railClickCondition(strMenuNavigationName,strRailName, name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType,itemsList, (_url, position, type, commonData) -> detailRailClick.detailItemClicked(_url, position, type, commonData));


            });

        }

    }

}
