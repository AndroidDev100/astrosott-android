package com.astro.sott.fragments.detailRailFragment.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.adapter.RibbonAdapter;
import com.astro.sott.beanModel.ksBeanmodel.AssetCommonImages;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.DetailRailClick;
import com.astro.sott.callBacks.commonCallBacks.MediaTypeCallBack;
import com.astro.sott.databinding.ExclusiveItemBinding;
import com.astro.sott.databinding.RelatedItemBinding;
import com.astro.sott.thirdParty.fcm.FirebaseEventManager;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.ActivityLauncher;
import com.astro.sott.utils.helpers.AssetContent;
import com.astro.sott.utils.helpers.ImageHelper;
import com.astro.sott.utils.helpers.NetworkConnectivity;
import com.astro.sott.utils.helpers.ToastHandler;
import com.kaltura.client.types.BooleanValue;
import com.kaltura.client.types.MultilingualStringValueArray;
import com.kaltura.client.types.Value;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.SingleItemViewHolder> {
    private List<RailCommonData> similarItemList;
    private Activity mContext;
    private DetailRailClick detailRailClick;


    public SimilarAdapter(Activity context, List<RailCommonData> loadedList) {
        similarItemList = loadedList;
        mContext = context;
        try {
            this.detailRailClick = ((DetailRailClick) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
    }

    @NonNull
    @Override
    public SimilarAdapter.SingleItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RelatedItemBinding landscapeItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.related_item, parent, false);
        return new SingleItemViewHolder(landscapeItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarAdapter.SingleItemViewHolder holder, int position) {
        RailCommonData singleItem = similarItemList.get(position);
        if (singleItem.getImages().size() > 0) {
            AssetCommonImages assetCommonImages = singleItem.getImages().get(0);
            ImageHelper.getInstance(holder.landscapeItemBinding.image.getContext()).loadImageToLandscape(holder.landscapeItemBinding.image, assetCommonImages.getImageUrl(), R.drawable.ic_landscape_placeholder);

        } else {
            ImageHelper.getInstance(holder.landscapeItemBinding.image.getContext()).loadImageToPlaceholder(holder.landscapeItemBinding.image, AppCommonMethods.getImageURI(R.drawable.ic_landscape_placeholder, holder.landscapeItemBinding.image), R.drawable.ic_landscape_placeholder);

        }
        try {
            setRecycler(holder.landscapeItemBinding.metas.recyclerView, singleItem.getObject().getTags());
            AppCommonMethods.setBillingUi(holder.landscapeItemBinding.billingImage, singleItem.getObject().getTags(), singleItem.getObject().getType(), mContext);

        } catch (Exception e) {

        }

        holder.landscapeItemBinding.lanscapeTitle.setText(singleItem.getName());

    }

    private void setRecycler(RecyclerView recyclerView, Map<String, MultilingualStringValueArray> tags) {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        RibbonAdapter ribbonAdapter = new RibbonAdapter(AssetContent.getRibbon(tags));
        recyclerView.setAdapter(ribbonAdapter);

    }

    @Override
    public int getItemCount() {
        return similarItemList.size();
    }

    private void getPremimumMark(int position, ExclusiveItemBinding exclusiveLayout) {

        exclusiveLayout.exclLay.setVisibility(View.GONE);
        Map<String, Value> map = similarItemList.get(position).getObject().getMetas();

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

    class SingleItemViewHolder extends RecyclerView.ViewHolder {
        RelatedItemBinding landscapeItemBinding;

        public SingleItemViewHolder(@NonNull RelatedItemBinding itemView) {
            super(itemView.getRoot());
            landscapeItemBinding = itemView;
            final String name = mContext.getClass().getSimpleName();


            landscapeItemBinding.cardView.setOnClickListener(v -> {
                new ActivityLauncher(mContext).railClickCondition("", "", name, similarItemList.get(getLayoutPosition()), getLayoutPosition(), AppConstants.Rail5, similarItemList, new MediaTypeCallBack() {
                    @Override
                    public void detailItemClicked(String _url, int position, int type, RailCommonData commonData) {
                        if (NetworkConnectivity.isOnline(mContext)) {
                            FirebaseEventManager.getFirebaseInstance(mContext).ralatedTabEvent(commonData.getObject().getName(),commonData.getObject(),mContext,"");
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
