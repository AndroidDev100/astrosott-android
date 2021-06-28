package com.astro.sott.adapter.experiencemng;

import android.app.Activity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.adapter.RibbonAdapter;
import com.astro.sott.modelClasses.dmsResponse.MediaTypes;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.PrintLogging;
import com.bumptech.glide.Glide;
import com.astro.sott.databinding.LandscapeListingNewBinding;
import com.astro.sott.utils.constants.AppConstants;
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;
import com.kaltura.client.types.Asset;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Value;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.utils.commonMethods.AppCommonMethods;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonLandscapeListingAdapteNew extends RecyclerView.Adapter<CommonLandscapeListingAdapteNew.SingleItemRowHolder> {
    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private final int layoutType;
    ResponseDmsModel responseDmsModel;
    MediaTypes mediaTypes;
    private String strMenuNavigationName = "";
    private DetailRailClick detailRailClick;
    private String strRailName;
    DisplayMetrics displaymetrics;
    boolean isTablet = false;

    BaseCategory baseCategory;

    public CommonLandscapeListingAdapteNew(Activity context, List<RailCommonData> itemsList, int type, String railName, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        strRailName = railName;
        this.baseCategory = baseCat;
        try {
            displaymetrics = new DisplayMetrics();
            mContext.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            if (mContext.getResources().getBoolean(R.bool.isTablet))
                isTablet = true;

            this.detailRailClick = ((DetailRailClick) context);
            responseDmsModel = AppCommonMethods.callpreference(mContext);
            mediaTypes = responseDmsModel.getParams().getMediaTypes();

        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int i) {
        LandscapeListingNewBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.landscape_listing_new, parent, false);
        return new SingleItemRowHolder(binding);

    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {
        RailCommonData singleItem = itemsList.get(i);
        try {
            /*if (singleItem.getImages().size() > 0) {
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                // holder.landscapeItemBinding.setImage(assetCommonImages);
                PrintLogging.printLog("", "TRTWorld-->>" + singleItem.getImages().get(0));
                ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageTo(holder.landscapeItemBinding.itemImage, assetCommonImages.getImageUrl(),R.drawable.landscape);

            }*/
            try {
                holder.landscapeItemBinding.metas.billingImage.setVisibility(View.GONE);
                setRecycler(holder.landscapeItemBinding.metas.recyclerView, singleItem.getObject().getTags());
                AppCommonMethods.setBillingUi(holder.landscapeItemBinding.billingImage, singleItem.getObject().getTags(), singleItem.getObject().getType(), mContext);
                AppCommonMethods.handleTitleDesc(holder.landscapeItemBinding.mediaTypeLayout.metaLayout, holder.landscapeItemBinding.mediaTypeLayout.lineOne, holder.landscapeItemBinding.mediaTypeLayout.lineTwo, baseCategory, itemsList.get(i), mContext);
                holder.landscapeItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(i).getObject().getName());
                if (singleItem.getObject().getType() == MediaTypeConstant.getProgram(mContext)) {
                    holder.landscapeItemBinding.mediaTypeLayout.lineTwo.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                    holder.landscapeItemBinding.mediaTypeLayout.lineTwo.setText(AppCommonMethods.getProgramTimeDate(itemsList.get(i).getObject().getStartDate()) + "-" + AppCommonMethods.getEndTime(itemsList.get(i).getObject().getEndDate()));
                } else {
                    holder.landscapeItemBinding.mediaTypeLayout.lineTwo.setTextColor(mContext.getResources().getColor(R.color.pale_gray));
                    holder.landscapeItemBinding.mediaTypeLayout.lineTwo.setText(itemsList.get(i).getObject().getDescription());
                }
                //holder.landscapeItemBinding.mediaTypeLayout.lineTwo.setText(itemsList.get(i).getObject().getDescription());
            } catch (Exception ignored) {

            }

            if (singleItem.getObject().getImages().size() > 0) {
                setImageUrl(singleItem.getObject());
                if (landscapeUrl != null)
                    ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageTo(holder.landscapeItemBinding.itemImage, landscapeUrl, R.drawable.ic_landscape_placeholder);
                else if (potraitUrl != null)
                    ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageTo(holder.landscapeItemBinding.itemImage, potraitUrl, R.drawable.ic_landscape_placeholder);
            } else {
                /*if (new KsPreferenceKeys(mContext).getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
                    ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageTo(holder.landscapeItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.shimmer_landscape, holder.landscapeItemBinding.itemImage));
                } else {
                    ImageHelper.getInstance(holder.landscapeItemBinding.itemImage.getContext()).loadImageTo(holder.landscapeItemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.landscape_dark, holder.landscapeItemBinding.itemImage));
                }*/
            }

        } catch (Exception e) {

            PrintLogging.printLog("Exception", "" + e);
        }


        if (i == 0) {
            PrintLogging.printLog("", "valessIdd" + itemsList.get(0).getType() + "----" + layoutType);
        }

        holder.landscapeItemBinding.setTile(singleItem);

        try {
            // mediaTypeCondition(i, holder.landscapeItemBinding);
        } catch (Exception e) {
            holder.landscapeItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
            holder.landscapeItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);

        }

    }

    private void setRecycler(RecyclerView recyclerView, Map<String, MultilingualStringValueArray> tags) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        RibbonAdapter ribbonAdapter = new RibbonAdapter(AssetContent.getRibbon(tags));
        recyclerView.setAdapter(ribbonAdapter);

    }


    private String landscapeUrl;
    private String potraitUrl;
    private String squareUrl;
    private boolean isImage = true;

    public void setImageUrl(Asset asset) {

        if (asset.getImages().size() > 0) {
            isImage = true;
            for (int i = 0; i < asset.getImages().size(); i++) {
                if (asset.getImages().get(i).getRatio().equals("16x9")) {
                    String image_url = AppConstants.WEBP_URL + asset.getImages().get(i).getUrl();
                    landscapeUrl = image_url + AppConstants.WIDTH + (int) mContext.getResources().getDimension(R.dimen.landscape_image_width) + AppConstants.HEIGHT + (int) mContext.getResources().getDimension(R.dimen.landscape_image_height) + AppConstants.QUALITY;
                }
                if (asset.getImages().get(i).getRatio().equals("9:16")) {
                    String image_url = AppConstants.WEBP_URL + asset.getImages().get(i).getUrl();
                    potraitUrl = image_url + AppConstants.WIDTH + (int) mContext.getResources().getDimension(R.dimen.portrait_image_width) + AppConstants.HEIGHT + (int) mContext.getResources().getDimension(R.dimen.portrait_image_height) + AppConstants.QUALITY;
                }
                if (asset.getImages().get(i).getRatio().equals("1:1")) {
                    String image_url = AppConstants.WEBP_URL + asset.getImages().get(i).getUrl();
                    squareUrl = image_url + AppConstants.WIDTH + (int) mContext.getResources().getDimension(R.dimen.square_image_width) + AppConstants.HEIGHT + (int) mContext.getResources().getDimension(R.dimen.square_image_height) + AppConstants.QUALITY;
                }
            }
        } else {
            isImage = false;
        }


    }


    private void recommendedCondition(int position, LandscapeListingNewBinding landscapeItemBinding) {
        landscapeItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(position).getName());
        landscapeItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
        landscapeItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
    }

    private void mediaTypeCondition(int position, LandscapeListingNewBinding potraitItemBinding) {
        if (Integer.parseInt(mediaTypes.getMovie()) == itemsList.get(0).getType()
                || Integer.parseInt(mediaTypes.getSeries()) == itemsList.get(0).getType()
        ) {
            getPremimumMark(position, potraitItemBinding);
            potraitItemBinding.exclusiveLayout.timmingLayout.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);

        } else if (Integer.parseInt(mediaTypes.getEpisode()) == itemsList.get(0).getType()
        ) {
            getPremimumMark(position, potraitItemBinding);
            potraitItemBinding.mediaTypeLayout.lineOne.setText("E" + 1 + " | " + itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            //potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.GONE);
            if (itemsList.get(position).getUrls().size() > 0)
                potraitItemBinding.exclusiveLayout.durationTxt.setText(itemsList.get(position).getUrls().get(0).getDuration() + "");
            else
                potraitItemBinding.exclusiveLayout.durationTxt.setText("0 sec");

            episodeNumber(position, potraitItemBinding);
        } else if (Integer.parseInt(mediaTypes.getProgram()) == itemsList.get(0).getType()) {
            potraitItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.VISIBLE);
            potraitItemBinding.exclusiveLayout.exclLay.setVisibility(View.VISIBLE);
            potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.durationTxt.setText("Live");
            potraitItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.durationTxt.setBackgroundResource(R.color.exclusive_tag);

            RailCommonData commonData = itemsList.get(position);
            if (commonData != null) {
                for (int i = 0; i < commonData.getObject().getImages().size(); i++) {
                    if (commonData.getObject().getImages().get(i).getRatio().equalsIgnoreCase("1:1")) {
                        String channelLogoUrl = AppConstants.WEBP_URL + commonData.getObject().getImages().get(i).getUrl();
                        String finalUrl = channelLogoUrl + AppConstants.WIDTH + (int) mContext.getResources().getDimension(R.dimen.circle_image_width) + AppConstants.HEIGHT + (int) mContext.getResources().getDimension(R.dimen.circle_image_height) + AppConstants.QUALITY;

                        if (!TextUtils.isEmpty(channelLogoUrl)) {
                            potraitItemBinding.exclusiveLayout.ivChannelLogo.setVisibility(View.VISIBLE);
                            Glide.with(mContext).load(finalUrl).into(potraitItemBinding.exclusiveLayout.ivChannelLogo);
                            break;

                        }
                    } else {
                        potraitItemBinding.exclusiveLayout.ivChannelLogo.setVisibility(View.GONE);
                    }
                }
            } else {
                PrintLogging.printLog("rail data", "rail data has null");
            }


        } else if (itemsList.get(0).getType() == AppConstants.Rail7) {
            potraitItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(position).getName());
            /*potraitItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);*/
            getPremimumMark(position, potraitItemBinding);
            potraitItemBinding.exclusiveLayout.timmingLayout.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
        }
    }

    private void getPremimumMark(int position, LandscapeListingNewBinding potraitItemBinding) {
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

    private void episodeNumber(int position, LandscapeListingNewBinding potraitItemBinding) {
        Map<String, Value> map = itemsList.get(position).getObject().getMetas();

        Set keys = map.keySet();
        Iterator itr = keys.iterator();

        String key;
        while (itr.hasNext()) {
            key = (String) itr.next();
            if (key.equalsIgnoreCase("Episode number")) {
                DoubleValue doubleValue = (DoubleValue) map.get(key);
                PrintLogging.printLog("", "metavaluess--" + key + " - " + doubleValue.getValue());
                potraitItemBinding.mediaTypeLayout.lineOne.setText("E" + doubleValue.getValue().intValue() + " | " + itemsList.get(position).getName());
            }
        }

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        public final LandscapeListingNewBinding landscapeItemBinding;

        public SingleItemRowHolder(LandscapeListingNewBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            landscapeItemBinding = flightItemLayoutBinding;
            final String name = mContext.getClass().getSimpleName();
            PrintLogging.printLog("", "classsname" + name);

            landscapeItemBinding.getRoot().setOnClickListener(view -> {
                //GAManager.getInstance().setEvent(GAManager.BROWSING, GAManager.MORE_RESULT_CLICKED, GAManager.RAIL_NAVIGATION, GAManager.zero);
                try {
                    new ActivityLauncher(mContext).railClickCondition(strMenuNavigationName, strRailName, name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType, itemsList, (_url, position, type, commonData) -> detailRailClick.detailItemClicked(_url, position, type, commonData));
                } catch (Exception ignored) {

                }


            });
        }


    }

}
