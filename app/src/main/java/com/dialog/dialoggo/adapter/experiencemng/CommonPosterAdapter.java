package com.dialog.dialoggo.adapter.experiencemng;

import android.app.Activity;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dialog.dialoggo.ApplicationMain;
import com.dialog.dialoggo.R;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonImages;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.callBacks.commonCallBacks.ContinueWatchingRemove;
import com.dialog.dialoggo.callBacks.commonCallBacks.DetailRailClick;
import com.dialog.dialoggo.databinding.PosterItemBinding;
import com.dialog.dialoggo.modelClasses.dmsResponse.MediaTypes;
import com.dialog.dialoggo.modelClasses.dmsResponse.ResponseDmsModel;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.constants.AppConstants;
import com.dialog.dialoggo.utils.helpers.ActivityLauncher;
import com.dialog.dialoggo.utils.helpers.ImageHelper;
import com.dialog.dialoggo.utils.helpers.MediaTypeConstant;
import com.dialog.dialoggo.utils.helpers.NetworkConnectivity;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.ToastHandler;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.DoubleValue;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonPosterAdapter extends RecyclerView.Adapter<CommonPosterAdapter.SingleItemRowHolder>{

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

    public CommonPosterAdapter(Activity context, List<RailCommonData> itemsList, int type, ContinueWatchingRemove callBack, int cwIndex, String railName, boolean isContinueWatchingRail) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        continueWatchingIndex = cwIndex;
        strRailName = railName;
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
                               List<RailCommonData> itemsList, int type, String railName) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.layoutType = type;
        strRailName = railName;
        this.isContinueWatchingRail=false;
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
    public CommonPosterAdapter.SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int i) {
        PosterItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.poster_item, parent, false);
        return new CommonPosterAdapter.SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(CommonPosterAdapter.SingleItemRowHolder holder, int i) {

        RailCommonData singleItem = itemsList.get(i);
        try {
            if (singleItem.getImages().size() > 0) {
                PrintLogging.printLog("", "imageCommining" + singleItem.getImages().get(0).getImageUrl());
                AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
                //holder.potraitItemBinding.setImage(assetCommonImages);

                ImageHelper.getInstance(holder.itemBinding.itemImage.getContext()).loadImageTo(holder.itemBinding.itemImage, assetCommonImages.getImageUrl(),R.drawable.portrait);
            } else {
                /*if (new KsPreferenceKeys(mContext).getCurrentTheme().equalsIgnoreCase(AppConstants.LIGHT_THEME)) {
                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext()).loadImageTo(holder.itemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.shimmer_portrait, holder.itemBinding.itemImage));
                } else {
                    ImageHelper.getInstance(holder.itemBinding.itemImage.getContext()).loadImageTo(holder.itemBinding.itemImage, AppCommonMethods.getImageURI(R.drawable.portrait_dark, holder.itemBinding.itemImage));
                }*/

            }
        } catch (Exception ignored) {

        }
        //holder.potraitItemBinding.setTile(singleItem);
        try {
            if (isContinueWatchingRail) {
                checkContinueWatching(i, holder.itemBinding);
            }else{
                mediaTypeCondition(i, holder.itemBinding);
            }
        } catch (Exception e) {
            holder.itemBinding.mediaTypeLayout.metaLayout.setVisibility(View.GONE);
            holder.itemBinding.exclusiveLayout.exclLay.setVisibility(View.GONE);

        }

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

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final PosterItemBinding itemBinding;

        SingleItemRowHolder(PosterItemBinding potraitItemBind) {
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
