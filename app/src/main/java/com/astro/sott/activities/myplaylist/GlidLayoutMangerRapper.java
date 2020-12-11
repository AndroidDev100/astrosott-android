package com.astro.sott.activities.myplaylist;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

public class GlidLayoutMangerRapper extends GridLayoutManager {

    @SuppressWarnings("UnusedDeclaration")
    public GlidLayoutMangerRapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, RecyclerView view) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public GlidLayoutMangerRapper(Context context, int spanCount) {
        super(context, spanCount);

    }

    @SuppressWarnings("UnusedDeclaration")
    public GlidLayoutMangerRapper(Context context, int spanCount, int orientation, boolean reverseLayout, RecyclerView view) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {

        }
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}