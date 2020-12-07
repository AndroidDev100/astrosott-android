package com.dialog.dialoggo.adapter.shimmer;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.adapter.CommonAdapter;
import com.dialog.dialoggo.beanModel.SectionDataModel;
import com.dialog.dialoggo.utils.helpers.CustomLayoutManager;
import com.dialog.dialoggo.utils.helpers.GravitySnapHelper;
import com.dialog.dialoggo.utils.helpers.SpacingItemDecoration;
import com.dialog.dialoggo.utils.helpers.carousel.model.Slide;
import com.dialog.dialoggo.utils.helpers.shimmer.ShimmerHeaderAdapter;
import com.dialog.dialoggo.utils.helpers.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;

public class CustomShimmerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Activity activity;
    private final ArrayList<SectionDataModel> dataList;


    public CustomShimmerAdapter(Activity activity, ArrayList<SectionDataModel> demoList, ArrayList<Slide> slides) {
        this.activity = activity;
        this.dataList = demoList;
    }


    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int HEADER_ITEM = 0;
        View view;
        if (viewType == HEADER_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shimmer_crousel, parent, false);
            return new HeaderHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.potrait_recycler_item, parent, false);
            CustomShimmerAdapter.PortrateHolder childHolder = new CustomShimmerAdapter.PortrateHolder(view);
            setRecyclerProperties(childHolder.recycler_view_list1);
            return childHolder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        try {
            if (holder instanceof CommonAdapter.HeaderHolder) {
                ShimmerHeaderAdapter itemListDataAdapter1 = new ShimmerHeaderAdapter();
                CustomShimmerAdapter.HeaderHolder headerHolder = (HeaderHolder) holder;
                headerHolder.recycler_view_list1.setNestedScrollingEnabled(false);
                headerHolder.recycler_view_list1.setHasFixedSize(true);
                headerHolder.recycler_view_list1.showShimmerAdapter();
                headerHolder.recycler_view_list1.setDemoChildCount(1);
                headerHolder.recycler_view_list1.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
                headerHolder.recycler_view_list1.setAdapter(itemListDataAdapter1);
            } else if (holder instanceof CommonAdapter.PotraitHolder) {
                ShimmerPortraitAdapter itemListDataAdapter1 = new ShimmerPortraitAdapter();
                CustomShimmerAdapter.PortrateHolder portraitHolder = (PortrateHolder) holder;
                portraitHolder.recycler_view_list1.setNestedScrollingEnabled(false);
                portraitHolder.recycler_view_list1.setHasFixedSize(true);
                portraitHolder.recycler_view_list1.showShimmerAdapter();
                portraitHolder.recycler_view_list1.setAdapter(itemListDataAdapter1);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    private void setRecyclerProperties(RecyclerView rView) {
        rView.setNestedScrollingEnabled(false);
        rView.setHasFixedSize(true);
        rView.addItemDecoration(new SpacingItemDecoration(20, SpacingItemDecoration.HORIZONTAL));

        rView.setLayoutManager(new CustomLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(rView);


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    private class HeaderHolder extends RecyclerView.ViewHolder {

        final ShimmerRecyclerView recycler_view_list1;

        private HeaderHolder(View itemView) {
            super(itemView);
            recycler_view_list1 = itemView.findViewById(R.id.recycler_view_list1);
        }
    }


    private class PortrateHolder extends RecyclerView.ViewHolder {
        final ShimmerRecyclerView recycler_view_list1;

        private PortrateHolder(View itemView) {
            super(itemView);
            recycler_view_list1 = itemView.findViewById(R.id.recycler_view_list4);
        }
    }
}
