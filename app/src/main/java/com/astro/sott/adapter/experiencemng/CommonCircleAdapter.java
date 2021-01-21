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
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.databinding.CircularItemLargeBinding;
import com.astro.sott.databinding.CircularItemSmallBinding;
import com.astro.sott.databinding.CwMediatypeItemBinding;
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
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;
import com.enveu.enums.RailCardSize;

import java.util.List;
import java.util.Random;

public class CommonCircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    public CommonCircleAdapter(Activity context, List<RailCommonData> itemsList, int type, ContinueWatchingRemove callBack, int cwIndex, String railName, boolean isContinueWatchingRail, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        continueWatchingIndex = cwIndex;
        strRailName = railName;
        this.isContinueWatchingRail = isContinueWatchingRail;
        this.baseCategory=baseCat;
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

    BaseCategory baseCategory;
    public CommonCircleAdapter(Activity context, List<RailCommonData> itemsList, int type, String railName, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        strRailName = railName;
        this.isContinueWatchingRail = false;
        this.baseCategory=baseCat;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        if (baseCategory!=null && baseCategory.getRailCardSize()!=null) {
            if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.NORMAL.name())) {
                CircularItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.circular_item, parent, false);
                return new NormalHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                CircularItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.circular_item_small, parent, false);
                return new SmallHolder(binding);
            }
            else  if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                CircularItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.circular_item_large, parent, false);
                return new LargeHolder(binding);
            }else {
                CircularItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.circular_item, parent, false);
                return new NormalHolder(binding);
            }

        }else {
            CircularItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.circular_item, parent, false);
            return new NormalHolder(binding);
        }


    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

            if (holder instanceof NormalHolder) {
                setNormalValues(((NormalHolder) holder).circularItemBinding,i);
            }
            else if (holder instanceof SmallHolder) {
                setSmallValues(((SmallHolder) holder).circularItemBinding,i);
            }
            else if (holder instanceof LargeHolder) {
                setLargeValues(((LargeHolder) holder).circularItemBinding,i);
            }

    }

    private void setLargeValues(CircularItemLargeBinding circularItemBinding, int i) {
        try {
            RailCommonData singleItem = itemsList.get(i);
            PrintLogging.printLog("circleAdapter ",singleItem.getObject().getId()+"");
            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                PrintLogging.printLog("", assetCommonImages.getImageUrl());
                // holder.circularItemBinding.setImage(assetCommonImages);
                ImageHelper.getInstance(circularItemBinding.itemImage.getContext()).loadImageTo(circularItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.square1);


                ImageHelper.getInstance(mContext).setImageDescription(circularItemBinding.itemImage,assetCommonImages.getImageUrl());

            }else
            {


            }
            String name = singleItem.getCreatorName();
            if (name == null) {
                if (singleItem.getObject().getType() == MediaTypeConstant.getLinear(mContext)) {
                    circularItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
                } else {

                }
                circularItemBinding.setTile(singleItem);
                circularItemBinding.itemImage.setVisibility(View.VISIBLE);
                circularItemBinding.creatorLay.setVisibility(View.GONE);
                circularItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(i).getName());
                circularItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            } else {
                Drawable background = circularItemBinding.creatorLay.getBackground();
                PrintLogging.printLog("", "circleviewww if" + "--->>");
                if (background instanceof GradientDrawable) {
                    PrintLogging.printLog("", "circleviewww else" + "--->>");
                    // cast to 'ShapeDrawable'
                    GradientDrawable shapeDrawable = (GradientDrawable) background;
                    Random random = new Random();

                    int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

                    shapeDrawable.setColor(color);
                }

                circularItemBinding.creatorLay.setVisibility(View.VISIBLE);
                circularItemBinding.mediaTypeLayout.lineOne.setText(AppCommonMethods.getCteatorNameCaps(itemsList.get(i).getName().trim()));
                circularItemBinding.mediaTypeLayout.lineOne.setGravity(Gravity.CENTER);
                circularItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
                circularItemBinding.itemImage.setVisibility(View.GONE);
                circularItemBinding.creatorName.setText(name);
            }

            if (isContinueWatchingRail) {
                checkContinueWatching(i, circularItemBinding.mediaTypeLayout, circularItemBinding.ivPlayVideo, circularItemBinding.progressBar);
            }

        } catch (Exception e) {
            // Log.w("trendingImage", e.toString());
            circularItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
        }

    }

    private void setSmallValues(CircularItemSmallBinding circularItemBinding, int i) {
        try {
            RailCommonData singleItem = itemsList.get(i);
            PrintLogging.printLog("circleAdapter ",singleItem.getObject().getId()+"");
            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                PrintLogging.printLog("", assetCommonImages.getImageUrl());
                // holder.circularItemBinding.setImage(assetCommonImages);
                ImageHelper.getInstance(circularItemBinding.itemImage.getContext()).loadImageTo(circularItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.square1);


                ImageHelper.getInstance(mContext).setImageDescription(circularItemBinding.itemImage,assetCommonImages.getImageUrl());

            }else
            {


            }
            String name = singleItem.getCreatorName();
            if (name == null) {
                if (singleItem.getObject().getType() == MediaTypeConstant.getLinear(mContext)) {
                    circularItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
                } else {

                }
                circularItemBinding.setTile(singleItem);
                circularItemBinding.itemImage.setVisibility(View.VISIBLE);
                circularItemBinding.creatorLay.setVisibility(View.GONE);
                circularItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(i).getName());
                circularItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            } else {
                Drawable background = circularItemBinding.creatorLay.getBackground();
                PrintLogging.printLog("", "circleviewww if" + "--->>");
                if (background instanceof GradientDrawable) {
                    PrintLogging.printLog("", "circleviewww else" + "--->>");
                    // cast to 'ShapeDrawable'
                    GradientDrawable shapeDrawable = (GradientDrawable) background;
                    Random random = new Random();

                    int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

                    shapeDrawable.setColor(color);
                }

                circularItemBinding.creatorLay.setVisibility(View.VISIBLE);
                circularItemBinding.mediaTypeLayout.lineOne.setText(AppCommonMethods.getCteatorNameCaps(itemsList.get(i).getName().trim()));
                circularItemBinding.mediaTypeLayout.lineOne.setGravity(Gravity.CENTER);
                circularItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
                circularItemBinding.itemImage.setVisibility(View.GONE);
                circularItemBinding.creatorName.setText(name);
            }

            if (isContinueWatchingRail) {
                checkContinueWatching(i, circularItemBinding.mediaTypeLayout, circularItemBinding.ivPlayVideo, circularItemBinding.progressBar);
            }

        } catch (Exception e) {
            // Log.w("trendingImage", e.toString());
            circularItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
        }

    }

    private void setNormalValues(CircularItemBinding circularItemBinding, int i) {
        try {
            RailCommonData singleItem = itemsList.get(i);
            PrintLogging.printLog("circleAdapter ",singleItem.getObject().getId()+"");
            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                PrintLogging.printLog("", assetCommonImages.getImageUrl());
                // holder.circularItemBinding.setImage(assetCommonImages);
                ImageHelper.getInstance(circularItemBinding.itemImage.getContext()).loadImageTo(circularItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.square1);


                ImageHelper.getInstance(mContext).setImageDescription(circularItemBinding.itemImage,assetCommonImages.getImageUrl());

            }else
            {


            }
            String name = singleItem.getCreatorName();
            if (name == null) {
                if (singleItem.getObject().getType() == MediaTypeConstant.getLinear(mContext)) {
                    circularItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
                } else {

                }
                circularItemBinding.setTile(singleItem);
                circularItemBinding.itemImage.setVisibility(View.VISIBLE);
                circularItemBinding.creatorLay.setVisibility(View.GONE);
                circularItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(i).getName());
                circularItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            } else {
                Drawable background = circularItemBinding.creatorLay.getBackground();
                PrintLogging.printLog("", "circleviewww if" + "--->>");
                if (background instanceof GradientDrawable) {
                    PrintLogging.printLog("", "circleviewww else" + "--->>");
                    // cast to 'ShapeDrawable'
                    GradientDrawable shapeDrawable = (GradientDrawable) background;
                    Random random = new Random();

                    int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

                    shapeDrawable.setColor(color);
                }

                circularItemBinding.creatorLay.setVisibility(View.VISIBLE);
                circularItemBinding.mediaTypeLayout.lineOne.setText(AppCommonMethods.getCteatorNameCaps(itemsList.get(i).getName().trim()));
                circularItemBinding.mediaTypeLayout.lineOne.setGravity(Gravity.CENTER);
                circularItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
                circularItemBinding.itemImage.setVisibility(View.GONE);
                circularItemBinding.creatorName.setText(name);
            }

            if (isContinueWatchingRail) {
                checkContinueWatching(i, circularItemBinding.mediaTypeLayout,circularItemBinding.ivPlayVideo,circularItemBinding.progressBar);
            }

        } catch (Exception e) {
            // Log.w("trendingImage", e.toString());
            circularItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
        }
    }

    private void checkContinueWatching(int position, CwMediatypeItemBinding mediaTypeLayout, ImageView ivPlayVideo, SeekBar progressBar) {
        if (isContinueWatchingRail) {
            mediaTypeLayout.lineOne.setText(itemsList.get(position).getName());
            mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            mediaTypeLayout.deleteIcon.setVisibility(View.VISIBLE);
            mediaTypeLayout.metaLayout.setVisibility(View.VISIBLE);
            ivPlayVideo.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(itemsList.get(position).getPosition());
        } else {
            mediaTypeLayout.deleteIcon.setVisibility(View.GONE);
            mediaTypeLayout.metaLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            ivPlayVideo.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }


    public class NormalHolder extends RecyclerView.ViewHolder {

        final CircularItemBinding circularItemBinding;

        NormalHolder(CircularItemBinding circularItemBind) {
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

    public class SmallHolder extends RecyclerView.ViewHolder {

        final CircularItemSmallBinding circularItemBinding;

        SmallHolder(CircularItemSmallBinding circularItemBind) {
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


    public class LargeHolder extends RecyclerView.ViewHolder {

        final CircularItemLargeBinding circularItemBinding;

        LargeHolder(CircularItemLargeBinding circularItemBind) {
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
