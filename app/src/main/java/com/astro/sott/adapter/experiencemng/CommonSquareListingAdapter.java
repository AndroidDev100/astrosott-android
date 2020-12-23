package com.astro.sott.adapter.experiencemng;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.modelClasses.dmsResponse.MediaTypes;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.databinding.SquarelistingNewBinding;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.utils.commonMethods.AppCommonMethods;


public class CommonSquareListingAdapter extends RecyclerView.Adapter<CommonSquareListingAdapter.SingleItemRowHolder> {

    private String strMenuNavigationName = "";
    private DetailRailClick detailRailClick;
    private final int layoutType;
    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private String strRailName;
    ResponseDmsModel responseDmsModel;
    MediaTypes mediaTypes;

    public CommonSquareListingAdapter(Activity context, List<RailCommonData> itemsList, int type, String railName) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        strRailName = railName;
        try {
            this.detailRailClick = ((DetailRailClick) context);
            responseDmsModel = AppCommonMethods.callpreference(mContext);
            mediaTypes = responseDmsModel.getParams().getMediaTypes();

        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int i) {
        SquarelistingNewBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.squarelisting_new, parent, false);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        RailCommonData singleItem = itemsList.get(i);
        try {
            AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
            // holder.squareItemBinding.setImage(assetCommonImages);
            ImageHelper.getInstance(holder.squareItemBinding.itemImage.getContext()).loadImageTo(holder.squareItemBinding.itemImage, assetCommonImages.getImageUrl(),R.drawable.square1);
        } catch (Exception ignored) {

        }

        holder.squareItemBinding.setTile(singleItem);
        mediaTypeCondition(i, holder.squareItemBinding);
    }

    private void mediaTypeCondition(int position, SquarelistingNewBinding potraitItemBinding) {
        if (Integer.parseInt(mediaTypes.getMovie()) == itemsList.get(0).getType()
                || Integer.parseInt(mediaTypes.getSeries()) == itemsList.get(0).getType()
                ) {
            getPremimumMark(position, potraitItemBinding);
            potraitItemBinding.exclusiveLayout.timmingLayout.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);

        } else if (Integer.parseInt(mediaTypes.getEpisode()) == itemsList.get(0).getType()
                ) {
            potraitItemBinding.mediaTypeLayout.lineOne.setText("E" + 1 + " | " + itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.GONE);

            if (itemsList.get(position).getUrls().size() > 0) {
                potraitItemBinding.exclusiveLayout.durationTxt.setText(itemsList.get(position).getUrls().get(0).getDuration() + "");
            }
            else
             {
                potraitItemBinding.exclusiveLayout.durationTxt.setText("0 sec");
             }


            episodeNumber(position, potraitItemBinding);
        }  else if (Integer.parseInt(mediaTypes.getLinear()) == itemsList.get(0).getType()
                || Integer.parseInt(mediaTypes.getTrailer()) == itemsList.get(0).getType()) {
            potraitItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
        }
    }

    private void getPremimumMark(int position,   SquarelistingNewBinding potraitItemBinding) {
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

    private void episodeNumber(int position,   SquarelistingNewBinding potraitItemBinding) {
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


    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }


    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final SquarelistingNewBinding squareItemBinding;

        SingleItemRowHolder(SquarelistingNewBinding squareItemBind) {
            super(squareItemBind.getRoot());
            squareItemBinding = squareItemBind;
            final String name = mContext.getClass().getSimpleName();
            squareItemBinding.getRoot().setOnClickListener(view -> {
                //GAManager.getInstance().setEvent(GAManager.BROWSING, GAManager.MORE_RESULT_CLICKED, GAManager.RAIL_NAVIGATION, GAManager.zero);

                new ActivityLauncher(mContext).railClickCondition(strMenuNavigationName, strRailName, name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType,itemsList, (_url, position, type, commonData) -> detailRailClick.detailItemClicked(_url, position, type, commonData));
            });

        }

    }

}
