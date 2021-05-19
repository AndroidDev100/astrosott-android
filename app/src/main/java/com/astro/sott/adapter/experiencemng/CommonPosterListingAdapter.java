package com.astro.sott.adapter.experiencemng;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.adapter.RibbonAdapter;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.modelClasses.dmsResponse.MediaTypes;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.PosterlistingItemBinding;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonPosterListingAdapter extends RecyclerView.Adapter< CommonPosterListingAdapter.SingleItemRowHolder> {

    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private String strMenuNavigationName = "";
    private DetailRailClick detailRailClick;
    private final int layoutType;
    private String strRailName;
    ResponseDmsModel responseDmsModel;
    MediaTypes mediaTypes;
    BaseCategory baseCategory;
    public CommonPosterListingAdapter(Activity context,
                                      List<RailCommonData> itemsList, int type, String railName, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        strRailName = railName;
        this.baseCategory=baseCat;
        try {
            this.detailRailClick = ((DetailRailClick) context);
            responseDmsModel = AppCommonMethods.callpreference(mContext);
            mediaTypes = responseDmsModel.getParams().getMediaTypes();
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }

    }

    @Override
    public  CommonPosterListingAdapter.SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int i) {
        PosterlistingItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.posterlisting_item, parent, false);
        return new  CommonPosterListingAdapter.SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder( CommonPosterListingAdapter.SingleItemRowHolder holder, int i) {

        RailCommonData singleItem = itemsList.get(i);
        try {
            if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                //holder.potraitItemBinding.setImage(assetCommonImages);
                ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext()).loadImageTo(holder.potraitItemBinding.itemImage, assetCommonImages.getImageUrl(),R.drawable.ic_potrait_placeholder);
            }
        } catch (Exception ignored) {

        }

        try {
            holder.potraitItemBinding.metas.billingImage.setVisibility(View.GONE);
            setRecycler(holder.potraitItemBinding.metas.recyclerView, singleItem.getObject().getTags());
            AppCommonMethods.setBillingUi(holder.potraitItemBinding.billingImage, singleItem.getObject().getTags(), singleItem.getObject().getType(), mContext);
            AppCommonMethods.handleTitleDesc(holder.potraitItemBinding.mediaTypeLayout.metaLayout,holder.potraitItemBinding.mediaTypeLayout.lineOne,holder.potraitItemBinding.mediaTypeLayout.lineTwo,baseCategory,itemsList.get(i),mContext);
            holder.potraitItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(i).getObject().getName());
            if (itemsList.get(i).getType()== MediaTypeConstant.getProgram(mContext)){
                holder.potraitItemBinding.mediaTypeLayout.lineTwo.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                holder.potraitItemBinding.mediaTypeLayout.lineTwo.setText(AppCommonMethods.getProgramTimeDate(itemsList.get(i).getObject().getStartDate())+"");
            }else {
                holder.potraitItemBinding.mediaTypeLayout.lineTwo.setTextColor(mContext.getResources().getColor(R.color.pale_gray));
                holder.potraitItemBinding.mediaTypeLayout.lineTwo.setText(itemsList.get(i).getObject().getDescription());
            }
        }catch (Exception e){

        }


           // holder.potraitItemBinding.mediaTypeLayout.lineTwo.setText(itemsList.get(i).getObject().getDescription());


        //holder.potraitItemBinding.setTile(singleItem);
       // mediaTypeCondition(i, holder.potraitItemBinding);
    }

    private void setRecycler(RecyclerView recyclerView, Map<String, MultilingualStringValueArray> tags) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        RibbonAdapter ribbonAdapter = new RibbonAdapter(AssetContent.getRibbon(tags));
        recyclerView.setAdapter(ribbonAdapter);

    }


    private void mediaTypeCondition(int position, PosterlistingItemBinding potraitItemBinding) {
        if (Integer.parseInt(mediaTypes.getMovie()) == itemsList.get(0).getType()
                || Integer.parseInt(mediaTypes.getSeries()) == itemsList.get(0).getType()) {
            getPremimumMark(position, potraitItemBinding);
            potraitItemBinding.exclusiveLayout.timmingLayout.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);

        } else if (Integer.parseInt(mediaTypes.getEpisode()) == itemsList.get(0).getType()
               ) {
            potraitItemBinding.mediaTypeLayout.lineOne.setText("E" + 1 + " | " + itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.GONE);
            if (itemsList.get(position).getUrls().size() > 0)
                potraitItemBinding.exclusiveLayout.durationTxt.setText(itemsList.get(position).getUrls().get(0).getDuration() + "");
            else
                potraitItemBinding.exclusiveLayout.durationTxt.setText("0 sec");


            episodeNumber(position, potraitItemBinding);
        }  else if (Integer.parseInt(mediaTypes.getLinear()) == itemsList.get(0).getType()
                || Integer.parseInt(mediaTypes.getTrailer()) == itemsList.get(0).getType()) {
            potraitItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
        }
    }

    private void episodeNumber(int position, PosterlistingItemBinding potraitItemBinding) {
        Map<String, Value> map = itemsList.get(position).getObject().getMetas();

        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;
        while (itr.hasNext()) {
            key = (String) itr.next();
            if (key.equalsIgnoreCase("Episode number")) {
                DoubleValue doubleValue = (DoubleValue) map.get(key);
                PrintLogging.printLog("", "metavaluess--" + key + " - " + doubleValue.getValue());
                potraitItemBinding.mediaTypeLayout.lineOne.setText("E " + doubleValue.getValue().intValue() + " | " + itemsList.get(position).getName());
            }
        }

    }


    private void getPremimumMark(int position, PosterlistingItemBinding potraitItemBinding) {
        potraitItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
        Map<String, Value> map = itemsList.get(position).getObject().getMetas();

        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;
        while (itr.hasNext()) {
            key = (String) itr.next();
            if (key.equalsIgnoreCase("Is Exclusive")) {
                potraitItemBinding.exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
                BooleanValue doubleValue = (BooleanValue) map.get(key);

                PrintLogging.printLog("", "Is_Exclusive--" + key + " - " + doubleValue.getValue());
                if (doubleValue.getValue()) {
                    potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.VISIBLE);
                } else {
                    potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.GONE);
                }

            }
        }
    }


    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final PosterlistingItemBinding potraitItemBinding;

        SingleItemRowHolder(PosterlistingItemBinding potraitItemBind) {
            super(potraitItemBind.getRoot());
            potraitItemBinding = potraitItemBind;
            final String name = mContext.getClass().getSimpleName();
            potraitItemBinding.getRoot().setOnClickListener(view -> {
             //   GAManager.getInstance().setEvent(GAManager.BROWSING, GAManager.MORE_RESULT_CLICKED, GAManager.RAIL_NAVIGATION, GAManager.zero);

                try {
                    new ActivityLauncher(mContext).railClickCondition(strMenuNavigationName, strRailName, name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType,itemsList, (_url, position, type, commonData) -> detailRailClick.detailItemClicked(_url, position, type, commonData));
                } catch (Exception ignored) {

                }
            });
        }

    }

}