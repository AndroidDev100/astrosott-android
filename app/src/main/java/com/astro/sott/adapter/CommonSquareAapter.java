package com.astro.sott.adapter;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;

import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.MediaTypeCallBack;
import com.astro.sott.databinding.ExclusiveItemBinding;
import com.astro.sott.databinding.SquareItemBinding;
import com.astro.sott.databinding.SquareItemLargeBinding;
import com.astro.sott.databinding.SquareItemSmallBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
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

public class CommonSquareAapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int layoutType;
    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private long lastClickTime = 0;
    private DetailRailClick detailRailClick;

    BaseCategory baseCategory;
    public CommonSquareAapter(Activity context, List<RailCommonData> itemsList, int type, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        this.baseCategory=baseCat;
        try {
            this.detailRailClick = ((DetailRailClick) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (baseCategory!=null && baseCategory.getRailCardSize()!=null) {
            if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.NORMAL.name())) {
                SquareItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.square_item, parent, false);
                return new NormalHolder(binding);
            }
            else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())){
                SquareItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.square_item_small, parent, false);
                return new SmallHolder(binding);
            }
            else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())){
                SquareItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.square_item_large, parent, false);
                return new LargeHolder(binding);
            }else {
                SquareItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.square_item, parent, false);
                return new NormalHolder(binding);
            }
        }else {
            SquareItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.square_item, parent, false);
            return new NormalHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof NormalHolder) {
            setNormalValues(((NormalHolder) holder).squareItemBinding,i);
        }
        else if (holder instanceof SmallHolder) {
            setSmallValues(((SmallHolder) holder).squareItemBinding,i);
        }
        else if (holder instanceof LargeHolder) {
            setLargeValues(((LargeHolder) holder).squareItemBinding,i);
        }

    }

    private void setLargeValues(SquareItemLargeBinding squareItemBinding, int i) {
        RailCommonData singleItem = itemsList.get(i);
        try {

            boolean isProviderAvailable = AssetContent.getHungamaTag(singleItem.getObject().getTags());
            if (isProviderAvailable){
                squareItemBinding.hungama.setVisibility(View.VISIBLE);
            }else {
                squareItemBinding.hungama.setVisibility(View.GONE);
            }

            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);

                PrintLogging.printLog(CommonSquareAapter.class, "", assetCommonImages.getImageUrl() + "assetassetaseetss");
                // holder.landscapeItemBinding.setImage(assetCommonImages);
                // Glide.with(mContext).load(assetCommonImages.getImageUrl()).into(holder.landscapeItemBinding.itemImage);
                ImageHelper.getInstance(squareItemBinding.itemImage.getContext()).loadImageTo(squareItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.square1);
            } else {
                ImageHelper.getInstance(squareItemBinding.itemImage.getContext()).loadImageTo(squareItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.square1, squareItemBinding.itemImage), R.drawable.square1);
            }
            if (singleItem.getProgress() > 0) {
                squareItemBinding.progressBar.setVisibility(View.VISIBLE);
                squareItemBinding.progressBar.setProgress(singleItem.getPosition());
            } else {
                squareItemBinding.progressBar.setVisibility(View.GONE);
            }

            try {
                setRecycler(squareItemBinding.metas.recyclerView, singleItem.getObject().getTags());
                AppCommonMethods.setBillingUi(squareItemBinding.metas.billingImage, singleItem.getObject().getTags());
                AppCommonMethods.handleTitleDesc(squareItemBinding.titleLayout,squareItemBinding.tvTitle,squareItemBinding.tvDescription,baseCategory);
                squareItemBinding.tvTitle.setText(itemsList.get(i).getObject().getName());
                squareItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
            }catch (Exception ignored){

            }

            getPremimumMark(i, squareItemBinding.exclusiveLayout);

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }

    private void setSmallValues(SquareItemSmallBinding squareItemBinding, int i) {
        RailCommonData singleItem = itemsList.get(i);
        try {

            boolean isProviderAvailable = AssetContent.getHungamaTag(singleItem.getObject().getTags());
            if (isProviderAvailable){
                squareItemBinding.hungama.setVisibility(View.VISIBLE);
            }else {
                squareItemBinding.hungama.setVisibility(View.GONE);
            }

            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);

                PrintLogging.printLog(CommonSquareAapter.class, "", assetCommonImages.getImageUrl() + "assetassetaseetss");
                // holder.landscapeItemBinding.setImage(assetCommonImages);
                // Glide.with(mContext).load(assetCommonImages.getImageUrl()).into(holder.landscapeItemBinding.itemImage);
                ImageHelper.getInstance(squareItemBinding.itemImage.getContext()).loadImageTo(squareItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.square1);
            } else {
                ImageHelper.getInstance(squareItemBinding.itemImage.getContext()).loadImageTo(squareItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.square1, squareItemBinding.itemImage), R.drawable.square1);
            }
            if (singleItem.getProgress() > 0) {
                squareItemBinding.progressBar.setVisibility(View.VISIBLE);
                squareItemBinding.progressBar.setProgress(singleItem.getPosition());
            } else {
                squareItemBinding.progressBar.setVisibility(View.GONE);
            }

            try {
                setRecycler(squareItemBinding.metas.recyclerView, singleItem.getObject().getTags());
                AppCommonMethods.setBillingUi(squareItemBinding.metas.billingImage, singleItem.getObject().getTags());
                AppCommonMethods.handleTitleDesc(squareItemBinding.titleLayout,squareItemBinding.tvTitle,squareItemBinding.tvDescription,baseCategory);
                squareItemBinding.tvTitle.setText(itemsList.get(i).getObject().getName());
                squareItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
            }catch (Exception ignored){

            }

            getPremimumMark(i, squareItemBinding.exclusiveLayout);

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }

    private void setNormalValues(SquareItemBinding squareItemBinding, int i) {
        RailCommonData singleItem = itemsList.get(i);
        try {

            boolean isProviderAvailable = AssetContent.getHungamaTag(singleItem.getObject().getTags());
            if (isProviderAvailable){
                squareItemBinding.hungama.setVisibility(View.VISIBLE);
            }else {
               squareItemBinding.hungama.setVisibility(View.GONE);
            }

            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);

                PrintLogging.printLog(CommonSquareAapter.class, "", assetCommonImages.getImageUrl() + "assetassetaseetss");
                // holder.landscapeItemBinding.setImage(assetCommonImages);
                // Glide.with(mContext).load(assetCommonImages.getImageUrl()).into(holder.landscapeItemBinding.itemImage);
                ImageHelper.getInstance(squareItemBinding.itemImage.getContext()).loadImageTo(squareItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.square1);
            } else {
                ImageHelper.getInstance(squareItemBinding.itemImage.getContext()).loadImageTo(squareItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.square1, squareItemBinding.itemImage), R.drawable.square1);
            }
            if (singleItem.getProgress() > 0) {
                squareItemBinding.progressBar.setVisibility(View.VISIBLE);
                squareItemBinding.progressBar.setProgress(singleItem.getPosition());
            } else {
                squareItemBinding.progressBar.setVisibility(View.GONE);
            }

            try {
                setRecycler(squareItemBinding.metas.recyclerView, singleItem.getObject().getTags());
                AppCommonMethods.setBillingUi(squareItemBinding.metas.billingImage, singleItem.getObject().getTags());
                AppCommonMethods.handleTitleDesc(squareItemBinding.titleLayout,squareItemBinding.tvTitle,squareItemBinding.tvDescription,baseCategory);
                squareItemBinding.tvTitle.setText(itemsList.get(i).getObject().getName());
                squareItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
            }catch (Exception ignored){

            }

            getPremimumMark(i, squareItemBinding.exclusiveLayout);

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
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class NormalHolder extends RecyclerView.ViewHolder {

        final SquareItemBinding squareItemBinding;

        private NormalHolder(SquareItemBinding view) {
            super(view.getRoot());
            this.squareItemBinding = view;
            final String name = mContext.getClass().getSimpleName();

            squareItemBinding.getRoot().setOnClickListener(view1 -> {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                new ActivityLauncher(mContext).railClickCondition("","",name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType,itemsList, new MediaTypeCallBack() {
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

        final SquareItemSmallBinding squareItemBinding;

        private SmallHolder(SquareItemSmallBinding view) {
            super(view.getRoot());
            this.squareItemBinding = view;
            final String name = mContext.getClass().getSimpleName();

            squareItemBinding.getRoot().setOnClickListener(view1 -> {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                new ActivityLauncher(mContext).railClickCondition("","",name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType,itemsList, new MediaTypeCallBack() {
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

        final SquareItemLargeBinding squareItemBinding;

        private LargeHolder(SquareItemLargeBinding view) {
            super(view.getRoot());
            this.squareItemBinding = view;
            final String name = mContext.getClass().getSimpleName();

            squareItemBinding.getRoot().setOnClickListener(view1 -> {

                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                new ActivityLauncher(mContext).railClickCondition("","",name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType,itemsList, new MediaTypeCallBack() {
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
