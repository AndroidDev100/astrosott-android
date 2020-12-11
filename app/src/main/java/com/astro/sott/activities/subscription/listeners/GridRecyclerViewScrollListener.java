package com.astro.sott.activities.subscription.listeners;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private GridLayoutManager gridLayoutManager;
    private DataLoader dataLoader;
    private int previousItemCount;
    private boolean loading;

    public GridRecyclerViewScrollListener(GridLayoutManager gridLayoutManager, DataLoader dataLoader) {
        this.gridLayoutManager = gridLayoutManager;
        this.dataLoader = dataLoader;
        reset();
    }


    @Override
    public void onScrolled(@NonNull RecyclerView view, int dx, int dy) {
        if (dy > 0) {
            int itemCount = gridLayoutManager.getItemCount();

            if (itemCount != previousItemCount) {
                loading = false;
            }

            if (!loading && gridLayoutManager.findLastVisibleItemPosition() >= itemCount - 1) {
                previousItemCount = itemCount;
                loading = dataLoader.onLoadMore();
            }
        }
    }

    public void reset() {
        this.loading = false;
        this.previousItemCount = -1;
    }

    public interface DataLoader {
        boolean onLoadMore();
    }
}
