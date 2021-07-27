package com.astro.sott.adapter.experiencemng;

import android.app.Activity;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.adapter.RibbonAdapter;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.ContinueWatchingRemove;
import com.astro.sott.databinding.PosterItemLargeBinding;
import com.astro.sott.databinding.PosterItemSmallBinding;
import com.astro.sott.modelClasses.dmsResponse.MediaTypes;
import com.astro.sott.modelClasses.dmsResponse.ResponseDmsModel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.MediaTypeConstant;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.utils.helpers.ToastHandler;
import com.astro.sott.R;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.databinding.PosterItemBinding;
import com.enveu.BaseCollection.BaseCategoryModel.BaseCategory;
import com.enveu.enums.RailCardSize;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonPosterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<RailCommonData> itemsList;
    private final Activity mContext;
    private String strMenuNavigationName = "";
    private DetailRailClick detailRailClick;
    private final int layoutType;
    private long lastClickTime = 0;
    private String strRailName;
    private ResponseDmsModel responseDmsModel;
    private MediaTypes mediaTypes;
    private boolean isContinueWatchingRail;
    private ContinueWatchingRemove watchingRemove;
    private int continueWatchingIndex = -1;
    private int position;
    BaseCategory baseCategory;
    public CommonPosterAdapter(Activity context, List<RailCommonData> itemsList, int type, ContinueWatchingRemove callBack, int cwIndex, String railName, boolean isContinueWatchingRail, BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        continueWatchingIndex = cwIndex;
        strRailName = railName;
        this.baseCategory=baseCat;
        this.isContinueWatchingRail = isContinueWatchingRail;
        try {
            this.watchingRemove = callBack;
            this.detailRailClick = ((DetailRailClick) context);

            responseDmsModel = AppCommonMethods.callpreference(mContext);
            mediaTypes = responseDmsModel.getParams().getMediaTypes();

            Fragment f = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.content_frame);
           /* if (f instanceof HomeFragment) {
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


    public CommonPosterAdapter(Activity context,
                               List<RailCommonData> itemsList, int type, String railName,BaseCategory baseCat) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        strRailName = railName;
        this.isContinueWatchingRail=false;
        this.baseCategory=baseCat;
        try {
            responseDmsModel = AppCommonMethods.callpreference(mContext);
            mediaTypes = responseDmsModel.getParams().getMediaTypes();
            this.detailRailClick = ((DetailRailClick) context);

            Fragment f = ((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.content_frame);
           /* if (f instanceof HomeFragment) {
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
                PosterItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.poster_item, parent, false);
                return new NormalHolder(binding);
            }
            else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.SMALL.name())) {
                PosterItemSmallBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.poster_item_small, parent, false);
                return new SmallHolder(binding);
            }
            else if (baseCategory.getRailCardSize().equalsIgnoreCase(RailCardSize.LARGE.name())) {
                PosterItemLargeBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.poster_item_large, parent, false);
                return new LargeHolder(binding);
            }
            else {
                PosterItemBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.poster_item, parent, false);
                return new NormalHolder(binding);
            }
        }else {
            PosterItemBinding binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.poster_item, parent, false);
            return new NormalHolder(binding);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof NormalHolder) {
            setNormalValues(((NormalHolder) holder).itemBinding,i);
        }
        else if (holder instanceof SmallHolder) {
            setSmallValues(((SmallHolder) holder).itemBinding,i);
        }
        else if (holder instanceof LargeHolder) {
            setLargeValues(((LargeHolder) holder).itemBinding,i);
        }


    }

    private void setLargeValues(PosterItemLargeBinding itemBinding, int i) {
        RailCommonData singleItem = itemsList.get(i);
        try {
            Log.w("listSizee-->>", "listSizee" + singleItem.getImages().size());
            if (singleItem.getImages().size() > 0) {
                Log.w("", "imageCommining" + singleItem.getImages().get(0).getImageUrl());
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                //holder.potraitItemBinding.setImage(assetCommonImages);

                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadImageTo(itemBinding.itemImage, assetCommonImages.getImageUrl(),R.drawable.ic_potrait_placeholder);
            } else {
                /*if (new KsPreferenceKeys(mContext).getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext()).loadImageTo(holder.itemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.shimmer_portrait, holder.itemBinding.itemImage));
                } else {
                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext()).loadImageTo(holder.itemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.portrait_dark, holder.itemBinding.itemImage));
                }*/

            }
        } catch (Exception ignored) {

        }

        try {
            itemBinding.metas.billingImage.setVisibility(View.GONE);
            setRecycler(itemBinding.metas.recyclerView, singleItem.getObject().getTags());
            AppCommonMethods.setBillingUi(itemBinding.billingImage, singleItem.getObject().getTags(), singleItem.getObject().getType(), mContext);
            AppCommonMethods.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory,itemsList.get(i),mContext);
            itemBinding.tvTitle.setText(itemsList.get(i).getObject().getName());
            if (itemsList.get(i).getType()==MediaTypeConstant.getProgram(mContext)){
                itemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                itemBinding.tvDescription.setText(AppCommonMethods.getProgramTimeDate(itemsList.get(i).getObject().getStartDate())+"");
            }
            else if (itemsList.get(i).getType()==MediaTypeConstant.getLinear(mContext)){
                    if (AssetContent.isLiveEvent(itemsList.get(i).getObject().getMetas())) {
                        String liveEventTime=AppCommonMethods.getLiveEventTime(itemsList.get(i).getObject());
                        itemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                        itemBinding.tvDescription.setText(liveEventTime);
                    }

                }
            else {
                itemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.pale_gray));
                itemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
                itemBinding.tvTitle.setMaxLines(2);
                itemBinding.tvTitle.setEllipsize(TextUtils.TruncateAt.END);
            }

            // itemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
        }catch (Exception ignored){

        }
        //holder.potraitItemBinding.setTile(singleItem);
       /* try {
            if (isContinueWatchingRail) {
                checkContinueWatching(i, holder.itemBinding);
            }else{
                mediaTypeCondition(i, holder.itemBinding);
            }
        } catch (Exception e) {
            holder.itemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
            holder.itemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);

        }*/
    }

    private void setSmallValues(PosterItemSmallBinding itemBinding, int i) {
        RailCommonData singleItem = itemsList.get(i);
        try {
            Log.w("listSizee-->>", "listSizee" + singleItem.getImages().size());
            if (singleItem.getImages().size() > 0) {
                Log.w("", "imageCommining" + singleItem.getImages().get(0).getImageUrl());
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                //holder.potraitItemBinding.setImage(assetCommonImages);

                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadImageTo(itemBinding.itemImage, assetCommonImages.getImageUrl(),R.drawable.ic_potrait_placeholder);
            } else {
                /*if (new KsPreferenceKeys(mContext).getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext()).loadImageTo(holder.itemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.shimmer_portrait, holder.itemBinding.itemImage));
                } else {
                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext()).loadImageTo(holder.itemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.portrait_dark, holder.itemBinding.itemImage));
                }*/

            }
        } catch (Exception ignored) {

        }

        try {
            itemBinding.metas.billingImage.setVisibility(View.GONE);
            setRecycler(itemBinding.metas.recyclerView, singleItem.getObject().getTags());
            AppCommonMethods.setBillingUi(itemBinding.billingImage, singleItem.getObject().getTags(), singleItem.getObject().getType(), mContext);

            AppCommonMethods.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory,itemsList.get(i),mContext);
            itemBinding.tvTitle.setText(itemsList.get(i).getObject().getName());
            if (itemsList.get(i).getType()==MediaTypeConstant.getProgram(mContext)){
                itemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                itemBinding.tvDescription.setText(AppCommonMethods.getProgramTimeDate(itemsList.get(i).getObject().getStartDate())+"");
            }
             else if (itemsList.get(i).getType()==MediaTypeConstant.getLinear(mContext)){
                    if (AssetContent.isLiveEvent(itemsList.get(i).getObject().getMetas())) {
                        String liveEventTime=AppCommonMethods.getLiveEventTime(itemsList.get(i).getObject());
                        itemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                        itemBinding.tvDescription.setText(liveEventTime);
                    }

                }
            else {
                itemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.pale_gray));
                itemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
                itemBinding.tvTitle.setMaxLines(2);
                itemBinding.tvTitle.setEllipsize(TextUtils.TruncateAt.END);
            }
            //itemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
        }catch (Exception ignored){

        }
        //holder.potraitItemBinding.setTile(singleItem);
       /* try {
            if (isContinueWatchingRail) {
                checkContinueWatching(i, holder.itemBinding);
            }else{
                mediaTypeCondition(i, holder.itemBinding);
            }
        } catch (Exception e) {
            holder.itemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
            holder.itemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);

        }*/
    }

    private void setNormalValues(PosterItemBinding itemBinding, int i) {
        RailCommonData singleItem = itemsList.get(i);
        try {
            Log.w("listSizee-->>", "listSizee" + singleItem.getImages().size());
            if (singleItem.getImages().size() > 0) {
                Log.w("", "imageCommining" + singleItem.getImages().get(0).getImageUrl());
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                //holder.potraitItemBinding.setImage(assetCommonImages);

                ImageHelper.getInstance(itemBinding.itemImage.getContext()).loadImageTo(itemBinding.itemImage, assetCommonImages.getImageUrl(),R.drawable.ic_potrait_placeholder);
            } else {
                /*if (new KsPreferenceKeys(mContext).getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext()).loadImageTo(holder.itemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.shimmer_portrait, holder.itemBinding.itemImage));
                } else {
                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext()).loadImageTo(holder.itemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.portrait_dark, holder.itemBinding.itemImage));
                }*/

            }
        } catch (Exception ignored) {

        }

        try {
            itemBinding.metas.billingImage.setVisibility(View.GONE);
            setRecycler(itemBinding.metas.recyclerView, singleItem.getObject().getTags());
            AppCommonMethods.setBillingUi(itemBinding.billingImage, singleItem.getObject().getTags(), singleItem.getObject().getType(), mContext);

            AppCommonMethods.handleTitleDesc(itemBinding.titleLayout,itemBinding.tvTitle,itemBinding.tvDescription,baseCategory,itemsList.get(i),mContext);
            itemBinding.tvTitle.setText(itemsList.get(i).getObject().getName());
            if (itemsList.get(i).getType()==MediaTypeConstant.getProgram(mContext)){
                itemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                itemBinding.tvDescription.setText(AppCommonMethods.getProgramTimeDate(itemsList.get(i).getObject().getStartDate())+"");
            }
            else if (itemsList.get(i).getType()==MediaTypeConstant.getLinear(mContext)){
                    if (AssetContent.isLiveEvent(itemsList.get(i).getObject().getMetas())) {
                        String liveEventTime=AppCommonMethods.getLiveEventTime(itemsList.get(i).getObject());
                        itemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.yellow_orange));
                        itemBinding.tvDescription.setText(liveEventTime);
                    }

                }
            else {
                itemBinding.tvDescription.setTextColor(mContext.getResources().getColor(R.color.pale_gray));
                itemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
                itemBinding.tvTitle.setMaxLines(2);
                itemBinding.tvTitle.setEllipsize(TextUtils.TruncateAt.END);
            }
           // itemBinding.tvDescription.setText(itemsList.get(i).getObject().getDescription());
        }catch (Exception ignored){

        }

        //holder.potraitItemBinding.setTile(singleItem);
       /* try {
            if (isContinueWatchingRail) {
                checkContinueWatching(i, holder.itemBinding);
            }else{
                mediaTypeCondition(i, holder.itemBinding);
            }
        } catch (Exception e) {
            holder.itemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
            holder.itemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);

        }*/

    }

    private void checkContinueWatching(int position, PosterItemBinding potraitItemBinding) {
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


    private void mediaTypeCondition(int position, PosterItemBinding potraitItemBinding) {
        if (MediaTypeConstant.getMovie(mContext) == itemsList.get(0).getType()
                || MediaTypeConstant.getWebSeries(mContext) == itemsList.get(0).getType()
                || MediaTypeConstant.getSpotlightSeries(mContext) == itemsList.get(0).getType()) {
            getPremimumMark(position, potraitItemBinding);
            potraitItemBinding.exclusiveLayout.timmingLayout.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);

        } else if (MediaTypeConstant.getWebEpisode(mContext) == itemsList.get(0).getType()
                || MediaTypeConstant.getSpotlightSeries(mContext) == itemsList.get(0).getType()) {
            potraitItemBinding.mediaTypeLayout.lineOne.setText("E" + 1 + " | " + itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.durationTxt.setText(itemsList.get(position).getUrls().get(0).getDuration() + "");
            episodeNumber(position, potraitItemBinding);
        } else if (MediaTypeConstant.getShortFilm(mContext) == itemsList.get(0).getType()) {
            getPremimumMark(position, potraitItemBinding);
            potraitItemBinding.exclusiveLayout.durationTxt.setText(itemsList.get(position).getUrls().get(0).getDuration() + "");
            potraitItemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);

        } else if (MediaTypeConstant.getLinear(mContext) == itemsList.get(0).getType()
                || MediaTypeConstant.getTrailer(mContext) == itemsList.get(0).getType()) {
            potraitItemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.lineOne.setText(itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
        } else if (MediaTypeConstant.getUGCCreator(mContext) == itemsList.get(0).getType()) {
            potraitItemBinding.mediaTypeLayout.lineOne.setText("Creator Name" + " | " + itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.durationTxt.setText(itemsList.get(position).getUrls().get(0).getDuration() + "");
        } else if (MediaTypeConstant.getUGCCreator(mContext) == itemsList.get(0).getType()) {
            potraitItemBinding.mediaTypeLayout.lineOne.setText("Creator Name" + " | " + itemsList.get(position).getName());
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.timmingLayout.setVisibility(View.GONE);
        }else{

            potraitItemBinding.mediaTypeLayout.lineOne.setVisibility(View.GONE);
            potraitItemBinding.mediaTypeLayout.lineTwo.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.flExclusive.setVisibility(View.GONE);
            potraitItemBinding.exclusiveLayout.timmingLayout.setVisibility(View.GONE);
        }
    }

    private void episodeNumber(int position, PosterItemBinding potraitItemBinding) {
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

    private void setRecycler(RecyclerView recyclerView, Map<String, MultilingualStringValueArray> tags) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        RibbonAdapter ribbonAdapter = new RibbonAdapter(AssetContent.getRibbon(tags));
        recyclerView.setAdapter(ribbonAdapter);

    }


    private void getPremimumMark(int position, PosterItemBinding potraitItemBinding) {
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

    public class NormalHolder extends RecyclerView.ViewHolder {

        final PosterItemBinding itemBinding;

        NormalHolder(PosterItemBinding potraitItemBind) {
            super(potraitItemBind.getRoot());
            itemBinding = potraitItemBind;
            final String name = mContext.getClass().getSimpleName();
            itemBinding.mediaTypeLayout.deleteIcon.setOnClickListener(view -> {
                position = getLayoutPosition();
               // showAlertDialog(mContext.getResources().getString(R.string.remove_continue_watching_item), mContext.getResources().getString(R.string.yes), mContext.getResources().getString(R.string.no));
            });


            itemBinding.getRoot().setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
               // GAManager.getInstance().setEvent(GAManager.BROWSING, GAManager.RAIL_ASSET_CLICKED, GAManager.RAIL_NAVIGATION, GAManager.zero);

                new ActivityLauncher(mContext).railClickCondition(strMenuNavigationName, strRailName, name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType,itemsList, (_url, position, type, commonData) -> {
                    if (NetworkConnectivity.isOnline(mContext)) {
                        detailRailClick.detailItemClicked(_url, position, type, commonData);
                    } else {
                        ToastHandler.show(mContext.getResources().getString(R.string.no_internet_connection), mContext);
                    }

                });


            });
        }

    }


    public class SmallHolder extends RecyclerView.ViewHolder {

        final PosterItemSmallBinding itemBinding;

        SmallHolder(PosterItemSmallBinding potraitItemBind) {
            super(potraitItemBind.getRoot());
            itemBinding = potraitItemBind;
            final String name = mContext.getClass().getSimpleName();
            itemBinding.mediaTypeLayout.deleteIcon.setOnClickListener(view -> {
                position = getLayoutPosition();
                // showAlertDialog(mContext.getResources().getString(R.string.remove_continue_watching_item), mContext.getResources().getString(R.string.yes), mContext.getResources().getString(R.string.no));
            });


            itemBinding.getRoot().setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                // GAManager.getInstance().setEvent(GAManager.BROWSING, GAManager.RAIL_ASSET_CLICKED, GAManager.RAIL_NAVIGATION, GAManager.zero);

                new ActivityLauncher(mContext).railClickCondition(strMenuNavigationName, strRailName, name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType,itemsList, (_url, position, type, commonData) -> {
                    if (NetworkConnectivity.isOnline(mContext)) {
                        detailRailClick.detailItemClicked(_url, position, type, commonData);
                    } else {
                        ToastHandler.show(mContext.getResources().getString(R.string.no_internet_connection), mContext);
                    }

                });


            });
        }

    }


    public class LargeHolder extends RecyclerView.ViewHolder {

        final PosterItemLargeBinding itemBinding;

        LargeHolder(PosterItemLargeBinding potraitItemBind) {
            super(potraitItemBind.getRoot());
            itemBinding = potraitItemBind;
            final String name = mContext.getClass().getSimpleName();
            itemBinding.mediaTypeLayout.deleteIcon.setOnClickListener(view -> {
                position = getLayoutPosition();
                // showAlertDialog(mContext.getResources().getString(R.string.remove_continue_watching_item), mContext.getResources().getString(R.string.yes), mContext.getResources().getString(R.string.no));
            });


            itemBinding.getRoot().setOnClickListener(view -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                // GAManager.getInstance().setEvent(GAManager.BROWSING, GAManager.RAIL_ASSET_CLICKED, GAManager.RAIL_NAVIGATION, GAManager.zero);

                new ActivityLauncher(mContext).railClickCondition(strMenuNavigationName, strRailName, name, itemsList.get(getLayoutPosition()), getLayoutPosition(), layoutType,itemsList, (_url, position, type, commonData) -> {
                    if (NetworkConnectivity.isOnline(mContext)) {
                        detailRailClick.detailItemClicked(_url, position, type, commonData);
                    } else {
                        ToastHandler.show(mContext.getResources().getString(R.string.no_internet_connection), mContext);
                    }

                });


            });
        }

    }



}
