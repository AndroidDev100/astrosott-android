package com.dialog.dialoggo.adapter.experiencemng;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonImages;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.callBacks.commonCallBacks.DetailRailClick;
import com.dialog.dialoggo.databinding.PosterlistingItemBinding;
import com.dialog.dialoggo.modelClasses.dmsResponse.MediaTypes;
import com.dialog.dialoggo.modelClasses.dmsResponse.ResponseDmsModel;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.ImageHelper;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.DoubleValue;
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

    public CommonPosterListingAdapter(Activity context,
                                      List<RailCommonData> itemsList, int type, String railName) {
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
                ImageHelper.getInstance(holder.potraitItemBinding.itemImage.getContext()).loadImageTo(holder.potraitItemBinding.itemImage, assetCommonImages.getImageUrl(),R.drawable.portrait);
            }
        } catch (Exception ignored) {

        }
        //holder.potraitItemBinding.setTile(singleItem);
        mediaTypeCondition(i, holder.potraitItemBinding);
    }

    private void mediaTypeCondition(int position, PosterlistingItemBinding potraitItemBinding) {
        if (Integer.parseInt(mediaTypes.getMovie()) == itemsList.get(0).getType()
                || Integer.parseInt(mediaTypes.getWebSeries()) == itemsList.get(0).getType()
                || Integer.parseInt(mediaTypes.getSpotlightSeries()) == itemsList.get(0).getType()) {
            getPremimumMark(position, potraitItemBinding);
            potraitItemBinding.exclusiveLayout.timmingLayout.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);

        } else if (Integer.parseInt(mediaTypes.getWebEpisode()) == itemsList.get(0).getType()
                || Integer.parseInt(mediaTypes.getSpotlightSeries()) == itemsList.get(0).getType()) {
            potraitItemBinding.mediaTypeLayout.lineOne.setText("E" + 1 + " | " + itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.GONE);
            if (itemsList.get(position).getUrls().size() > 0)
                potraitItemBinding.exclusiveLayout.durationTxt.setText(itemsList.get(position).getUrls().get(0).getDuration() + "");
            else
                potraitItemBinding.exclusiveLayout.durationTxt.setText("0 sec");


            episodeNumber(position, potraitItemBinding);
        } else if (Integer.parseInt(mediaTypes.getShortFilm()) == itemsList.get(0).getType()) {
            getPremimumMark(position, potraitItemBinding);
            if (itemsList.get(position).getUrls().size() > 0)
                potraitItemBinding.exclusiveLayout.durationTxt.setText(itemsList.get(position).getUrls().get(0).getDuration() + "");
            else
                potraitItemBinding.exclusiveLayout.durationTxt.setText("0 sec");


            PrintLogging.printLog("", itemsList.get(position).getUrls().get(0).getDuration() + "dnfjdjfdjfbdsf");

            potraitItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
        } else if (Integer.parseInt(mediaTypes.getLinear()) == itemsList.get(0).getType()
                || Integer.parseInt(mediaTypes.getTrailer()) == itemsList.get(0).getType()) {
            potraitItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
        } else if (Integer.parseInt(mediaTypes.getUGCCreator()) == itemsList.get(0).getType()) {
            potraitItemBinding.mediaTypeLayout.lineOne.setText("Creator Name" + " | " + itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.GONE);
            if (itemsList.get(position).getUrls().size() > 0)
                potraitItemBinding.exclusiveLayout.durationTxt.setText(itemsList.get(position).getUrls().get(0).getDuration() + "");
            else
                potraitItemBinding.exclusiveLayout.durationTxt.setText("0 sec");



        } else if (Integer.parseInt(mediaTypes.getUGCCreator()) == itemsList.get(0).getType()) {

            potraitItemBinding.mediaTypeLayout.lineOne.setText("Creator Name" + " | " + itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.timmingLayout.setVisibility(View.GONE);
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