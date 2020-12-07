package com.dialog.dialoggo.activities.movieDescription.adapter;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.adapter.CommonLandscapeAdapter;
import com.dialog.dialoggo.adapter.CommonPotraitAdapter;
import com.dialog.dialoggo.adapter.CommonSquareAapter;
import com.dialog.dialoggo.beanModel.ksBeanmodel.AssetCommonBean;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.databinding.ContinueWatchingRecycleritemBinding;
import com.dialog.dialoggo.databinding.LandscapeRecyclerItemBinding;
import com.dialog.dialoggo.databinding.PotraitRecyclerItemBinding;
import com.dialog.dialoggo.databinding.SquareRecyclerItemBinding;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.CustomLayoutManager;
import com.dialog.dialoggo.utils.helpers.GravitySnapHelper;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.dialog.dialoggo.utils.helpers.SpacingItemDecoration;
import com.dialog.dialoggo.utils.helpers.ToolBarHandler;

import java.util.List;

public class MovieDescriptionCommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //    private final int ITEM1 = AppLevelConstants.Rail2;
//    private final int ITEM4 = AppLevelConstants.Rail5;
//    private final int ITEM5 = AppLevelConstants.Rail6;
//    private final int ITEM6 = AppLevelConstants.Rail7;
    private final Activity activity;
    private final List<AssetCommonBean> dataList;

    public MovieDescriptionCommonAdapter(Activity activity, List<AssetCommonBean> demoList) {
        this.activity = activity;
        this.dataList = demoList;
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getRailType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int ITEM2 = AppLevelConstants.Rail3;
        int ITEM5 = AppLevelConstants.Rail6;
        int ITEM3 = AppLevelConstants.Rail4;
        if (viewType == ITEM2) {
            PotraitRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.potrait_recycler_item, parent, false);
            PotraitHolder potraitHolder = new PotraitHolder(binding);
            setRecyclerProperties(potraitHolder.potraitRecyclerItemBinding.recyclerViewList4);
            return potraitHolder;
        } else if (viewType == ITEM3) {
            SquareRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.square_recycler_item, parent, false);
            SquareHolder squareHolder = new SquareHolder(binding);
            setRecyclerProperties(squareHolder.squareRecyclerItemBinding.recyclerViewList4);
            return squareHolder;
        } else if (viewType == ITEM5) {
            ContinueWatchingRecycleritemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.continue_watching_recycleritem, parent, false);
            ContinueWatchingHolder childHolder = new ContinueWatchingHolder(binding);
            setRecyclerProperties(childHolder.landscapeRecyclerItemBinding.recyclerViewList4);
            return childHolder;
        } else {


            SquareRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.square_recycler_item, parent, false);
            SquareHolder landscapeHolder = new SquareHolder(binding);
            setRecyclerProperties(landscapeHolder.squareRecyclerItemBinding.recyclerViewList4);
            return landscapeHolder;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof SquareHolder) {

            try {
                squareDataLogic(((SquareHolder) holder), dataList, position);
            } catch (ClassCastException e) {
                PrintLogging.printLog("Exception", "", "" + e);
            }

        } else if (holder instanceof PotraitHolder) {

            try {
                potraitDataLogic(((PotraitHolder) holder), dataList, position);
            } catch (ClassCastException e) {
                PrintLogging.printLog("Exception", "", "" + e);
            }
        } else if (holder instanceof ContinueWatchingHolder) {
            try {
                continueWatchingDataLogic(((ContinueWatchingHolder) holder), dataList, position);
            } catch (ClassCastException e) {

            }
        } else if (holder instanceof LandscapeHolder) {

            try {
                landscapeDataLogic(((LandscapeHolder) holder), dataList, position);

            } catch (ClassCastException e) {
                PrintLogging.printLog("Exception", "", "" + e);
            }
        }

    }

    private void continueWatchingDataLogic(ContinueWatchingHolder holder, List<AssetCommonBean> dataList, int position) {
        /*int totalCount = dataList.get(position).getTotalCount();
        if (totalCount > 20) {*/
            new ToolBarHandler(activity).setContinueWatchingListener(((MovieDescriptionCommonAdapter.ContinueWatchingHolder) holder).landscapeRecyclerItemBinding.moreText, AppLevelConstants.TYPE4, dataList.get(position));
            holder.landscapeRecyclerItemBinding.moreText.setVisibility(View.VISIBLE);
       /* } else {
            holder.landscapeRecyclerItemBinding.moreText.setVisibility(View.GONE);
        }*/

        holder.landscapeRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());
        holder.landscapeRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        holder.landscapeRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        //holder.landscapeRecyclerItemBinding.headerTitleLayout.setVisibility(View.VISIBLE);
        List<RailCommonData> singleSectionItems = dataList.get(position).getRailAssetList();
        CommonPotraitAdapter commonSquareAapter = new CommonPotraitAdapter(activity, singleSectionItems, AppLevelConstants.Rail4);

        holder.landscapeRecyclerItemBinding.recyclerViewList4.setAdapter(commonSquareAapter);
    }

    private void landscapeDataLogic(LandscapeHolder holder, List<AssetCommonBean> dataList, int position) {
       /* int totalCount = dataList.get(position).getTotalCount();
        if (totalCount > 20) {*/
            new ToolBarHandler(activity).setDetailMoreListener(holder.landscapeRecyclerItemBinding.moreText, AppLevelConstants.TYPE5, dataList.get(position));
            holder.landscapeRecyclerItemBinding.moreText.setVisibility(View.VISIBLE);
        /*} else {
            holder.landscapeRecyclerItemBinding.moreText.setVisibility(View.GONE);
        }*/
        holder.landscapeRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());
        holder.landscapeRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        List<RailCommonData> singleSectionItems = dataList.get(position).getRailAssetList();
        CommonLandscapeAdapter commonLandscapeAdapter = new CommonLandscapeAdapter(activity, singleSectionItems, AppLevelConstants.Rail5);
        holder.landscapeRecyclerItemBinding.recyclerViewList4.setAdapter(commonLandscapeAdapter);
    }

    private void squareDataLogic(SquareHolder holder, List<AssetCommonBean> dataList, int position) {
       /* int totalCount = dataList.get(position).getTotalCount();
        if (totalCount > 20) {*/
            new ToolBarHandler(activity).setDetailMoreListener(holder.squareRecyclerItemBinding.moreText, AppLevelConstants.TYPE4, dataList.get(position));
            holder.squareRecyclerItemBinding.moreText.setVisibility(View.VISIBLE);
       /* } else {
            holder.squareRecyclerItemBinding.moreText.setVisibility(View.GONE);
        }*/
        holder.squareRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());
        holder.squareRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        List<RailCommonData> singleSectionItems = dataList.get(position).getRailAssetList();
        CommonSquareAapter commonSquareAapter = new CommonSquareAapter(activity, singleSectionItems, AppLevelConstants.Rail4);
        holder.squareRecyclerItemBinding.recyclerViewList4.setAdapter(commonSquareAapter);
    }

    private void potraitDataLogic(PotraitHolder holder, List<AssetCommonBean> dataList, int position) {
     //   int totalCount = dataList.get(position).getTotalCount();

        holder.potraitRecyclerItemBinding.headerTitle.setText(dataList.get(position).getTitle());
       /* if (totalCount > 20) {*/
            new ToolBarHandler(activity).setDetailMoreListener(holder.potraitRecyclerItemBinding.moreText, AppLevelConstants.TYPE3, dataList.get(position));
            holder.potraitRecyclerItemBinding.moreText.setVisibility(View.VISIBLE);
       /* } else {
            holder.potraitRecyclerItemBinding.moreText.setVisibility(View.GONE);
        }*/
        holder.potraitRecyclerItemBinding.shimmerTitleLayout.setVisibility(View.GONE);
        List<RailCommonData> singleSectionItems = dataList.get(position).getRailAssetList();
        CommonPotraitAdapter commonPotraitAdapter = new CommonPotraitAdapter(activity, singleSectionItems, AppLevelConstants.Rail3);
        holder.potraitRecyclerItemBinding.recyclerViewList4.setAdapter(commonPotraitAdapter);
    }

    private void setRecyclerProperties(RecyclerView rView) {
        rView.setNestedScrollingEnabled(false);
        rView.setHasFixedSize(true);
        rView.addItemDecoration(new SpacingItemDecoration(15, SpacingItemDecoration.HORIZONTAL));
       /* if (isDecoration) {
            rView.addItemDecoration(new SpacingItemDecoration(10, SpacingItemDecoration.HORIZONTAL));
        }*/
        rView.setLayoutManager(new CustomLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(rView);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private class SquareHolder extends RecyclerView.ViewHolder {

        final SquareRecyclerItemBinding squareRecyclerItemBinding;

        private SquareHolder(SquareRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            squareRecyclerItemBinding = flightItemLayoutBinding;

        }
    }

    private class LandscapeHolder extends RecyclerView.ViewHolder {

        final LandscapeRecyclerItemBinding landscapeRecyclerItemBinding;

        public LandscapeHolder(LandscapeRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            landscapeRecyclerItemBinding = flightItemLayoutBinding;

        }
    }

    private class PotraitHolder extends RecyclerView.ViewHolder {

        final PotraitRecyclerItemBinding potraitRecyclerItemBinding;

        private PotraitHolder(PotraitRecyclerItemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            potraitRecyclerItemBinding = flightItemLayoutBinding;

        }
    }

    public class ContinueWatchingHolder extends RecyclerView.ViewHolder {
        ContinueWatchingRecycleritemBinding landscapeRecyclerItemBinding;

        public ContinueWatchingHolder(ContinueWatchingRecycleritemBinding flightItemLayoutBinding) {
            super(flightItemLayoutBinding.getRoot());
            landscapeRecyclerItemBinding = flightItemLayoutBinding;
        }
    }
}
