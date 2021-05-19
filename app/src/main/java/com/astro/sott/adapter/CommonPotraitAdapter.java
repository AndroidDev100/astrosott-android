package com.astro.sott.adapter;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astro.sott.callBacks.commonCallBacks.MediaTypeCallBack;
import com.astro.sott.databinding.ExclusiveItemBinding;
import com.astro.sott.databinding.PotraitItemLargeBinding;
import com.astro.sott.databinding.PotraitItemSmallBinding;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.PotraitItemBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;
import com.enveu.enums.RailCardSize;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonPotraitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int layoutType;
    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private DetailRailClick detailRailClick;
    private long lastClickTime = 0;
    BaseCategory baseCategory;
    public CommonPotraitAdapter(Activity context,
                                List<RailCommonData> itemsList, int type, BaseCategory baseCat) {
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
                PotraitItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.potrait_item, parent, false);
                return new NormalHolder(binding);
            }
            else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                PotraitItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.potrait_item_small, parent, false);
                return new SmallHolder(binding);
            }
            else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                PotraitItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.potrait_item_large, parent, false);
                return new LargeHolder(binding);
            }
            else {
                PotraitItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.potrait_item, parent, false);
                return new NormalHolder(binding);
            }
        }else {
            PotraitItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.potrait_item, parent, false);
            return new NormalHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof NormalHolder) {
            setNormalValues(((NormalHolder) holder).potraitItemBinding,i);
        }
        else if (holder instanceof SmallHolder) {
            setSmallValues(((SmallHolder) holder).potraitItemBinding,i);
        }
        else if (holder instanceof LargeHolder) {
            setLargeValues(((LargeHolder) holder).potraitItemBinding,i);
        }

    }

    private void setLargeValues(PotraitItemLargeBinding potraitItemBinding, int i) {
        RailCommonData singleItem = itemsList.get(i);

        try {
            Log.w("liveassettype", singleItem.getProgress() + "");


            if (singleItem.getImages().size() > 0) {

//                singleItem.getType();
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);

                ImageHelper.getInstance(potraitItemBinding.itemImage.getContext()).loadImageToPotrait(potraitItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.ic_potrait_placeholder);

            } else {
                ImageHelper.getInstance(potraitItemBinding.itemImage.getContext()).loadImageToPlaceholder(potraitItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.ic_potrait_placeholder, potraitItemBinding.itemImage), R.drawable.ic_potrait_placeholder);

            }
            if (singleItem.getPosition() > 0) {
                potraitItemBinding.progressBar.setVisibility(View.VISIBLE);
                potraitItemBinding.progressBar.setProgress(singleItem.getPosition());
            } else {
                potraitItemBinding.progressBar.setVisibility(View.GONE);
            }

            try {
                setRecycler(potraitItemBinding.metas.recyclerView, singleItem.getObject().getTags());
                AppCommonMethods.setBillingUi(potraitItemBinding.metas.billingImage, singleItem.getObject().getTags(), singleItem.getObject().getType(), mContext);
                AppCommonMethods.handleTitleDesc(potraitItemBinding.titleLayout,potraitItemBinding.tvTitle,potraitItemBinding.tvDescription,baseCategory,itemsList.get(i),mContext);
                potraitItemBinding.tvTitle.setText(itemsList.get(i).getObject().getName());
                if (itemsList.get(i).getType()==MediaTypeConstant.getProgram(mContext)){
                    potraitItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                    potraitItemBinding.tvDescription.setText(AppCommonMethods.getProgramTimeDate(itemsList.get(i).getObject().getStartDate())+"");
                }else {
                    potraitItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.pale_gray));
                    potraitItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
                }

                //  potraitItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
            }catch (Exception ignored){

            }

            getPremimumMark(i, potraitItemBinding.exclusiveLayout);

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }

    private void setSmallValues(PotraitItemSmallBinding potraitItemBinding, int i) {
        RailCommonData singleItem = itemsList.get(i);

        try {
            Log.w("liveassettype", singleItem.getProgress() + "");



            if (singleItem.getImages().size() > 0) {

//                singleItem.getType();
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);

                ImageHelper.getInstance(potraitItemBinding.itemImage.getContext()).loadImageToPotrait(potraitItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.ic_potrait_placeholder);

            } else {
                ImageHelper.getInstance(potraitItemBinding.itemImage.getContext()).loadImageToPlaceholder(potraitItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.ic_potrait_placeholder, potraitItemBinding.itemImage), R.drawable.ic_potrait_placeholder);

            }
            if (singleItem.getPosition() > 0) {
                potraitItemBinding.progressBar.setVisibility(View.VISIBLE);
                potraitItemBinding.progressBar.setProgress(singleItem.getPosition());
            } else {
                potraitItemBinding.progressBar.setVisibility(View.GONE);
            }

            try {
                setRecycler(potraitItemBinding.metas.recyclerView, singleItem.getObject().getTags());
                AppCommonMethods.setBillingUi(potraitItemBinding.metas.billingImage, singleItem.getObject().getTags(), singleItem.getObject().getType(), mContext);
                AppCommonMethods.handleTitleDesc(potraitItemBinding.titleLayout,potraitItemBinding.tvTitle,potraitItemBinding.tvDescription,baseCategory,itemsList.get(i),mContext);
                potraitItemBinding.tvTitle.setText(itemsList.get(i).getObject().getName());
                //potraitItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
                if (itemsList.get(i).getType()==MediaTypeConstant.getProgram(mContext)){
                    potraitItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                    potraitItemBinding.tvDescription.setText(AppCommonMethods.getProgramTimeDate(itemsList.get(i).getObject().getStartDate())+"");
                }else {
                    potraitItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.pale_gray));
                    potraitItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
                }

            }catch (Exception ignored){

            }

            getPremimumMark(i, potraitItemBinding.exclusiveLayout);

        } catch (Exception e) {
            PrintLogging.printLog("Exception", "", "" + e);
        }
    }

    private void setNormalValues(PotraitItemBinding potraitItemBinding, int i) {
        RailCommonData singleItem = itemsList.get(i);

        try {
            Log.w("liveassettype", singleItem.getProgress() + "");

            if (singleItem.getImages().size() > 0) {

//                singleItem.getType();
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);

                ImageHelper.getInstance(potraitItemBinding.itemImage.getContext()).loadImageToPotrait(potraitItemBinding.itemImage, assetCommonImages.getImageUrl(), R.drawable.ic_potrait_placeholder);

            } else {
                ImageHelper.getInstance(potraitItemBinding.itemImage.getContext()).loadImageToPlaceholder(potraitItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.ic_potrait_placeholder, potraitItemBinding.itemImage), R.drawable.ic_potrait_placeholder);

            }

            try {
                setRecycler(potraitItemBinding.metas.recyclerView, singleItem.getObject().getTags());
                AppCommonMethods.setBillingUi(potraitItemBinding.metas.billingImage, singleItem.getObject().getTags(), singleItem.getObject().getType(), mContext);
                AppCommonMethods.handleTitleDesc(potraitItemBinding.titleLayout,potraitItemBinding.tvTitle,potraitItemBinding.tvDescription,baseCategory,itemsList.get(i),mContext);
                potraitItemBinding.tvTitle.setText(itemsList.get(i).getObject().getName());
               // potraitItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
                if (itemsList.get(i).getType()==MediaTypeConstant.getProgram(mContext)){
                    potraitItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                    potraitItemBinding.tvDescription.setText(AppCommonMethods.getProgramTimeDate(itemsList.get(i).getObject().getStartDate())+"");
                }else {
                    potraitItemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.pale_gray));
                    potraitItemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
                }

            }catch (Exception ignored){

            }

            if (singleItem.getPosition() > 0) {
                potraitItemBinding.progressBar.setVisibility(View.VISIBLE);
                potraitItemBinding.progressBar.setProgress(singleItem.getPosition());
            } else {
                potraitItemBinding.progressBar.setVisibility(View.GONE);
            }
            getPremimumMark(i, potraitItemBinding.exclusiveLayout);

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

        final PotraitItemBinding potraitItemBinding;

        private NormalHolder(PotraitItemBinding view) {
            super(view.getRoot());
            this.potraitItemBinding = view;
            final String name = mContext.getClass().getSimpleName();
            potraitItemBinding.getRoot().setOnClickListener(view1 -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }

                lastClickTime = SystemClock.elapsedRealtime();

                PrintLogging.printLog("Exception", "", "" + name);
                PrintLogging.printLog("Exception", "", "" +  itemsList.get(getLayoutPosition()).toString());
                PrintLogging.printLog("Exception", "", "" + layoutType);



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

        final PotraitItemSmallBinding potraitItemBinding;

        private SmallHolder(PotraitItemSmallBinding view) {
            super(view.getRoot());
            this.potraitItemBinding = view;
            final String name = mContext.getClass().getSimpleName();
            potraitItemBinding.getRoot().setOnClickListener(view1 -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }

                lastClickTime = SystemClock.elapsedRealtime();

                PrintLogging.printLog("Exception", "", "" + name);
                PrintLogging.printLog("Exception", "", "" +  itemsList.get(getLayoutPosition()).toString());
                PrintLogging.printLog("Exception", "", "" + layoutType);



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

        final PotraitItemLargeBinding potraitItemBinding;

        private LargeHolder(PotraitItemLargeBinding view) {
            super(view.getRoot());
            this.potraitItemBinding = view;
            final String name = mContext.getClass().getSimpleName();
            potraitItemBinding.getRoot().setOnClickListener(view1 -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }

                lastClickTime = SystemClock.elapsedRealtime();

                PrintLogging.printLog("Exception", "", "" + name);
                PrintLogging.printLog("Exception", "", "" +  itemsList.get(getLayoutPosition()).toString());
                PrintLogging.printLog("Exception", "", "" + layoutType);



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
