package com.astro.sott.adapter;

import android.app.Activity;

import androidx.databinding.DataBindingUtil;

import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.ContinueWatchingRemove;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.MediaTypeCallBack;
import com.astro.sott.databinding.ExclusiveItemBinding;
import com.astro.sott.databinding.LandscapeItemBinding;
import com.astro.sott.databinding.LandscapeItemLargeBinding;
import com.astro.sott.databinding.LandscapeItemSmallBinding;
import com.astro.sott.modelClasses.dmsResponse.MediaTypes;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.ToastHandler;
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;
import com.enveu.enums.RailCardSize;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonLandscapeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int layoutType;
    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private long lastClickTime = 0;
    private DetailRailClick detailRailClick;
    BaseCategory baseCategory;


    public CommonLandscapeAdapter(Activity context, List<RailCommonData> itemsList, int type, ContinueWatchingRemove callBack, int cwIndex, String railName, boolean isContinueWatchingRail, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        this.baseCategory = baseCat;
        try {
            this.detailRailClick = ((DetailRailClick) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    public CommonLandscapeAdapter(Activity context, List<RailCommonData> itemsList, int type, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        this.baseCategory = baseCat;
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

    public CommonLandscapeAdapter(Activity context, List<RailCommonData> itemsList, int type, String railName, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        String name = mContext.getClass().getSimpleName();
        strRailName = railName;
        this.isContinueWatchingRail = false;
        this.baseCategory = baseCat;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (baseCategory != null && baseCategory.getRailCardSize() != null) {
            if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.NORMAL.name())) {
                LandscapeItemBinding landscapeItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.landscape_item, parent, false);
                return new NormalHolder(landscapeItemBinding);
            } else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                LandscapeItemSmallBinding landscapeItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.landscape_item_small, parent, false);
                return new SmallHolder(landscapeItemBinding);
            } else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                LandscapeItemLargeBinding landscapeItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.landscape_item_large, parent, false);
                return new LargeHolder(landscapeItemBinding);
            } else {
                LandscapeItemBinding landscapeItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.landscape_item, parent, false);
                return new NormalHolder(landscapeItemBinding);
            }
        } else {
            LandscapeItemBinding landscapeItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.landscape_item, parent, false);
            return new NormalHolder(landscapeItemBinding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof NormalHolder) {
            setNormalValues(((NormalHolder) holder).landscapeItemBinding, i);


        } else if (holder instanceof SmallHolder) {

            setSmallValues(((SmallHolder) holder).circularItemBinding, i);
        } else if (holder instanceof LargeHolder) {

            setLargeValues(((LargeHolder) holder).circularItemBinding, i);
        }


    }


    private void setLargeValues(LandscapeItemLargeBinding landscapeItemBinding, int i) {
        RailCommonData singleItem = itemsList.get(i);
        PrintLogging.printLog(CommonLandscapeAdapter.class, "", itemsList.get(0).getType() + "assettypeassest");
        try {

            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                // holder.landscapeItemBinding.setImage(assetCommonImages);
                // Glide.with(mContext).load(assetCommonImages.getImageUrl()).into(holder.landscapeItemBinding.itemImage);
                ImageHelper.getInstance(landscapeItemBinding.itemImage.getContext()).loadImageToLandscape(landscapeItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.ic_landscape_placeholder);

            } else {
                ImageHelper.getInstance(landscapeItemBinding.itemImage.getContext()).loadImageToPlaceholder(landscapeItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.ic_landscape_placeholder, landscapeItemBinding.itemImage), R.drawable.ic_landscape_placeholder);

            }

            try {
                landscapeItemBinding.metas.billingImage.setVisibility(View.GONE);
                setRecycler(landscapeItemBinding.metas.recyclerView, singleItem.getObject().getTags());
                AppCommonMethods.setBillingUi(landscapeItemBinding.metas.billingImage, singleItem.getObject().getTags(), singleItem.getObject().getType(), mContext);
                AppCommonMethods.handleTitleDesc(landscapeItemBinding.titleLayout, landscapeItemBinding.tvTitle, landscapeItemBinding.tvDescription, baseCategory, itemsList.get(i), mContext);
                landscapeItemBinding.tvTitle.setText(itemsList.get(i).getObject().getName());
                if (itemsList.get(i).getType() == MediaTypeConstant.getProgram(mContext)) {
                    landscapeItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                    landscapeItemBinding.tvDescription.setText(AppCommonMethods.getProgramTimeDate(itemsList.get(i).getObject().getStartDate()) + " - " + AppCommonMethods.getEndTime(itemsList.get(i).getObject().getEndDate()));
                } else if (itemsList.get(i).getType() == MediaTypeConstant.getLinear(mContext)) {
                    if (AssetContent.isLiveEvent(itemsList.get(i).getObject().getMetas())) {
                        String liveEventTime = AppCommonMethods.getLiveEventTime(itemsList.get(i).getObject());
                        landscapeItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                        landscapeItemBinding.tvDescription.setText(liveEventTime);
                    }

                } else {
                    landscapeItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.pale_gray));
                    landscapeItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
                    landscapeItemBinding.tvTitle.setMaxLines(2);
                    landscapeItemBinding.tvTitle.setEllipsize(TextUtils.TruncateAt.END);


                }
                if (singleItem.getProgress() > 0) {
                    landscapeItemBinding.progressBar.setVisibility(View.VISIBLE);
                    landscapeItemBinding.progressBar.setProgress(singleItem.getPosition());
                } else {
                    landscapeItemBinding.progressBar.setVisibility(View.GONE);
                }
            } catch (Exception ignored) {
                landscapeItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
            }

            if (itemsList.get(i).getType() == MediaTypeConstant.getProgram(mContext)) {
                landscapeItemBinding.livenowLay.setVisibility(View.GONE);
            } else {
                landscapeItemBinding.livenowLay.setVisibility(View.GONE);
            }
            getPremimumMark(i, landscapeItemBinding.exclusiveLayout);
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }

    private void setSmallValues(LandscapeItemSmallBinding landscapeItemBinding, int i) {
        RailCommonData singleItem = itemsList.get(i);
        PrintLogging.printLog(CommonLandscapeAdapter.class, "", itemsList.get(0).getType() + "assettypeassest");
        try {


            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                // holder.landscapeItemBinding.setImage(assetCommonImages);
                // Glide.with(mContext).load(assetCommonImages.getImageUrl()).into(holder.landscapeItemBinding.itemImage);
                ImageHelper.getInstance(landscapeItemBinding.itemImage.getContext()).loadImageToLandscape(landscapeItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.ic_landscape_placeholder);

            } else {
                ImageHelper.getInstance(landscapeItemBinding.itemImage.getContext()).loadImageToPlaceholder(landscapeItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.ic_landscape_placeholder, landscapeItemBinding.itemImage), R.drawable.ic_landscape_placeholder);

            }

            try {
                landscapeItemBinding.metas.billingImage.setVisibility(View.GONE);
                setRecycler(landscapeItemBinding.metas.recyclerView, singleItem.getObject().getTags());
                AppCommonMethods.setBillingUi(landscapeItemBinding.billingImage, singleItem.getObject().getTags(), singleItem.getObject().getType(), mContext);
                AppCommonMethods.handleTitleDesc(landscapeItemBinding.titleLayout, landscapeItemBinding.tvTitle, landscapeItemBinding.tvDescription, baseCategory, itemsList.get(i), mContext);
                landscapeItemBinding.tvTitle.setText(itemsList.get(i).getObject().getName());
                if (itemsList.get(i).getType() == MediaTypeConstant.getProgram(mContext)) {
                    landscapeItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                    landscapeItemBinding.tvDescription.setText(AppCommonMethods.getProgramTimeDate(itemsList.get(i).getObject().getStartDate()) + " - " + AppCommonMethods.getEndTime(itemsList.get(i).getObject().getEndDate()));
                } else if (itemsList.get(i).getType() == MediaTypeConstant.getLinear(mContext)) {
                    if (AssetContent.isLiveEvent(itemsList.get(i).getObject().getMetas())) {
                        String liveEventTime = AppCommonMethods.getLiveEventTime(itemsList.get(i).getObject());
                        landscapeItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                        landscapeItemBinding.tvDescription.setText(liveEventTime);
                    }

                } else {
                    landscapeItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.pale_gray));
                    landscapeItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
                    landscapeItemBinding.tvTitle.setMaxLines(2);
                    landscapeItemBinding.tvTitle.setEllipsize(TextUtils.TruncateAt.END);


                }

                if (singleItem.getProgress() > 0) {
                    landscapeItemBinding.progressBar.setVisibility(View.VISIBLE);
                    landscapeItemBinding.progressBar.setProgress(singleItem.getPosition());
                } else {
                    landscapeItemBinding.progressBar.setVisibility(View.GONE);
                }
            } catch (Exception ignored) {

            }

            if (itemsList.get(i).getType() == MediaTypeConstant.getProgram(mContext)) {
                landscapeItemBinding.livenowLay.setVisibility(View.GONE);
            } else {
                landscapeItemBinding.livenowLay.setVisibility(View.GONE);
            }
            getPremimumMark(i, landscapeItemBinding.exclusiveLayout);
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }

    private void setNormalValues(LandscapeItemBinding landscapeItemBinding, int i) {
        RailCommonData singleItem = itemsList.get(i);
        PrintLogging.printLog(CommonLandscapeAdapter.class, "", itemsList.get(0).getType() + "assettypeassest");
        try {

            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                // holder.landscapeItemBinding.setImage(assetCommonImages);
                // Glide.with(mContext).load(assetCommonImages.getImageUrl()).into(holder.landscapeItemBinding.itemImage);
                ImageHelper.getInstance(landscapeItemBinding.itemImage.getContext()).loadImageToLandscape(landscapeItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.ic_landscape_placeholder);

            } else {
                ImageHelper.getInstance(landscapeItemBinding.itemImage.getContext()).loadImageToPlaceholder(landscapeItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.ic_landscape_placeholder, landscapeItemBinding.itemImage), R.drawable.ic_landscape_placeholder);

            }

            try {
                landscapeItemBinding.metas.billingImage.setVisibility(View.GONE);
                setRecycler(landscapeItemBinding.metas.recyclerView, singleItem.getObject().getTags());
                AppCommonMethods.setBillingUi(landscapeItemBinding.billingImage, singleItem.getObject().getTags(), singleItem.getObject().getType(), mContext);
                AppCommonMethods.handleTitleDesc(landscapeItemBinding.titleLayout, landscapeItemBinding.tvTitle, landscapeItemBinding.tvDescription, baseCategory, itemsList.get(i), mContext);
                landscapeItemBinding.tvTitle.setText(itemsList.get(i).getObject().getName());
                if (itemsList.get(i).getType() == MediaTypeConstant.getProgram(mContext)) {
                    landscapeItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                    landscapeItemBinding.tvDescription.setText(AppCommonMethods.getProgramTimeDate(itemsList.get(i).getObject().getStartDate()) + "-" + AppCommonMethods.getEndTime(itemsList.get(i).getObject().getEndDate()));
                } else if (itemsList.get(i).getType() == MediaTypeConstant.getLinear(mContext)) {
                    if (AssetContent.isLiveEvent(itemsList.get(i).getObject().getMetas())) {
                        String liveEventTime = AppCommonMethods.getLiveEventTime(itemsList.get(i).getObject());
                        landscapeItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                        landscapeItemBinding.tvDescription.setText(liveEventTime);
                    }

                } else {
                    landscapeItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.pale_gray));
                    landscapeItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
                    landscapeItemBinding.tvTitle.setMaxLines(2);
                    landscapeItemBinding.tvTitle.setEllipsize(TextUtils.TruncateAt.END);


                }

                if (singleItem.getProgress() > 0) {
                    landscapeItemBinding.progressBar.setVisibility(View.VISIBLE);
                    landscapeItemBinding.progressBar.setProgress(singleItem.getPosition());
                } else {
                    landscapeItemBinding.progressBar.setVisibility(View.GONE);
                }
            } catch (Exception ignored) {

            }

            if (itemsList.get(i).getType() == MediaTypeConstant.getProgram(mContext)) {
                landscapeItemBinding.livenowLay.setVisibility(View.GONE);
            } else {
                landscapeItemBinding.livenowLay.setVisibility(View.GONE);
            }
            getPremimumMark(i, landscapeItemBinding.exclusiveLayout);
        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }

    private void setRecycler(RecyclerView recyclerView, Map<String, MultilingualStringValueArray> tags) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        RibbonAdapter ribbonAdapter = new RibbonAdapter(AssetContent.getRibbon(tags));
        recyclerView.setAdapter(ribbonAdapter);

    }

    private void getPremimumMark(int position, ExclusiveItemBinding exclusiveLayout) {

        exclusiveLayout.exclLay.setVisibility(View.GONE);
        Map<String, Value> map = itemsList.get(position).getObject().getMetas();

        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;
        while (itr.hasNext()) {
            key = (String) itr.next();
            if (key.equalsIgnoreCase("Is Exclusive")) {
                exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                BooleanValue doubleValue = (BooleanValue) map.get(key);

                if (doubleValue.getValue()) {
                    exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                } else {
                    exclusiveLayout.exclLay.setVisibility(View.GONE);
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        if (itemsList.size() > 20 && itemsList.get(0).getRailDetail() != null && itemsList.get(0).getRailDetail().getDescription() != null) {
            if (itemsList.get(0).getRailDetail().getDescription().equalsIgnoreCase(AppLevelConstants.KEY_CONTINUE_WATCHING) || itemsList.get(0).getRailDetail().getDescription().equalsIgnoreCase(AppConstants.KEY_MY_WATCHLIST) || itemsList.get(0).getRailDetail().getDescription().equalsIgnoreCase(AppLevelConstants.LIVECHANNEL_RAIL) || itemsList.get(0).getRailDetail().getDescription().equalsIgnoreCase(AppLevelConstants.TRENDING)) {
                return 20;
            } else {
                return (null != itemsList ? itemsList.size() : 0);
            }
        } else {
            return (null != itemsList ? itemsList.size() : 0);
        }
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

                new ActivityLauncher(mContext).railClickCondition("", "", name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType, itemsList, new MediaTypeCallBack() {
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

    public class NormalHolder extends RecyclerView.ViewHolder {

        final LandscapeItemBinding landscapeItemBinding;

        private NormalHolder(@NonNull LandscapeItemBinding itemView) {
            super(itemView.getRoot());
            this.landscapeItemBinding = itemView;

            final String name = mContext.getClass().getSimpleName();
            landscapeItemBinding.getRoot().setOnClickListener(view -> {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();

                new ActivityLauncher(mContext).railClickCondition("", "", name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType, itemsList, new MediaTypeCallBack() {
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

    public class SmallHolder extends RecyclerView.ViewHolder {

        final LandscapeItemSmallBinding circularItemBinding;

        SmallHolder(LandscapeItemSmallBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;
            final String name = mContext.getClass().getSimpleName();

            circularItemBind.getRoot().setOnClickListener(view1 -> {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                new ActivityLauncher(mContext).railClickCondition("", "", name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType, itemsList, new MediaTypeCallBack() {
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

    public class LargeHolder extends RecyclerView.ViewHolder {

        final LandscapeItemLargeBinding circularItemBinding;

        LargeHolder(LandscapeItemLargeBinding circularItemBind) {
            super(circularItemBind.getRoot());
            this.circularItemBinding = circularItemBind;
            final String name = mContext.getClass().getSimpleName();

            circularItemBind.getRoot().setOnClickListener(view1 -> {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                new ActivityLauncher(mContext).railClickCondition("", "", name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType, itemsList, new MediaTypeCallBack() {
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
