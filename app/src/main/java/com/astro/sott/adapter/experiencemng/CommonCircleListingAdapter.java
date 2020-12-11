package com.astro.sott.adapter.experiencemng;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.CircularListingItemBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;

import java.util.List;
import java.util.Random;

public class CommonCircleListingAdapter extends RecyclerView.Adapter<  CommonCircleListingAdapter.SingleItemRowHolder> {


    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private final int layoutType;
    private  String strMenuNavigationName="";
    private DetailRailClick detailRailClick;
    private long lastClickTime = 0;
    private String strRailName;

    public CommonCircleListingAdapter(Activity context, List<RailCommonData> itemsList, int type, String railName) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        strRailName = railName;
        try {
            this.detailRailClick = ((DetailRailClick) context);

        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int i) {
        CircularListingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.circular_listing_item, parent, false);
        return new SingleItemRowHolder(binding);

    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        try {
            RailCommonData singleItem = itemsList.get(i);
            //PrintLogging.printLog("",singleItem.getObject().getId()+"fhjdsbfjsjfjsvfjh");
            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                PrintLogging.printLog("", assetCommonImages.getImageUrl());
                // holder.circularItemBinding.setImage(assetCommonImages);
//                ImageHelper.getInstance(holder.circularItemBinding.itemImage.getContext()).loadCircleImageTo(holder.circularItemBinding.itemImage, assetCommonImages.getImageUrl());


                /*if (new KsPreferenceKeys(mContext).getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {

                    Glide.with(mContext)
                            .load(assetCommonImages.getImageUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.shimmer_circle).error(R.drawable.shimmer_circle))
                            .into(holder.circularItemBinding.itemImage);

                    holder.circularItemBinding.itemImage.setBackground(ContextCompat.getDrawable(mContext,R.drawable.circle_drawable));
                } else {

//                    holder.circularItemBinding.itemImage.setBackgroundColor(R.color.transparent);

                    Glide.with(mContext)
                            .load(assetCommonImages.getImageUrl())
                            .apply(new RequestOptions().placeholder(R.drawable.circle_dark).error(R.drawable.circle_dark))
                            .into(holder.circularItemBinding.itemImage);
                }*/
              //  ImageHelper.getInstance(mContext).setImageDescription(holder.circularItemBinding.itemImage,"");

            }else {

                /*if (new KsPreferenceKeys(mContext).getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {

                    Glide.with(mContext)
                            .load("")
                            .apply(new RequestOptions().placeholder(R.drawable.shimmer_circle).error(R.drawable.shimmer_circle))
                            .into(holder.circularItemBinding.itemImage);
                }else{

                    Glide.with(mContext)
                        .load("")
                        .apply(new RequestOptions().placeholder(R.drawable.circle_dark).error(R.drawable.circle_dark))
                        .into(holder.circularItemBinding.itemImage);}
            }*/
            }
            String name = singleItem.getCreatorName();
            if (name == null) {
                if (singleItem.getObject().getType() == MediaTypeConstant.getLinear(mContext)) {
                    holder.circularItemBinding.metaLayout.setVisibility(View.GONE);
                } else {

                }
                holder.circularItemBinding.setTile(singleItem);
                holder.circularItemBinding.itemImage.setVisibility(View.VISIBLE);
                holder.circularItemBinding.creatorLay.setVisibility(View.GONE);
                holder.circularItemBinding.lineOne.setText(itemsList.get(i).getName());
                holder.circularItemBinding.lineTwo.setVisibility(View.GONE);
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
                holder.circularItemBinding.lineOne.setText(AppCommonMethods.getCteatorNameCaps(itemsList.get(i).getName()));
                holder.circularItemBinding.lineTwo.setVisibility(View.GONE);
                holder.circularItemBinding.itemImage.setVisibility(View.GONE);
                holder.circularItemBinding.creatorName.setText(name);
            }

       /*     holder.circularItemBinding.getRoot().setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                try {
                    lastClickTime = SystemClock.elapsedRealtime();
                    GAManager.getInstance().setEvent(GAManager.BROWSING, GAManager.RAIL_ASSET_CLICKED, GAManager.RAIL_NAVIGATION, GAManager.zero);
                    new ActivityLauncher(mContext).railClickCondition(strMenuNavigationName,strRailName, name, itemsList.get(i), i, layoutType, (_url, position, type, commonData) -> detailRailClick.detailItemClicked(_url, position, type, commonData));
                }catch (Exception ignored){
                    PrintLogging.printLog("",ignored.getMessage());
                }

            });*/



        } catch (Exception e) {
            // Log.w("trendingImage", e.toString());
            holder.circularItemBinding.metaLayout.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }


    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final CircularListingItemBinding circularItemBinding;

        SingleItemRowHolder(CircularListingItemBinding circularItemBind) {
            super(circularItemBind.getRoot());
            circularItemBinding = circularItemBind;
            final String name = mContext.getClass().getSimpleName();
           circularItemBinding.getRoot().setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                try {
                    lastClickTime = SystemClock.elapsedRealtime();
                    //GAManager.getInstance().setEvent(GAManager.BROWSING, GAManager.RAIL_ASSET_CLICKED, GAManager.RAIL_NAVIGATION, GAManager.zero);
                    new ActivityLauncher(mContext).railClickCondition(strMenuNavigationName,strRailName, name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType,itemsList, (_url, position, type, commonData) -> detailRailClick.detailItemClicked(_url, position, type, commonData));
                }catch (Exception ignored){
                    PrintLogging.printLog("",ignored.getMessage());
                }

            });

        }

    }
}
