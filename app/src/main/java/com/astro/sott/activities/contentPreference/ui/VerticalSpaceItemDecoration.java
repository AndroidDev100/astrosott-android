package com.astro.sott.activities.contentPreference.ui;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

 class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int verticalSpaceHeight;

    public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
        this.verticalSpaceHeight = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        outRect.bottom = verticalSpaceHeight;
    }
}
