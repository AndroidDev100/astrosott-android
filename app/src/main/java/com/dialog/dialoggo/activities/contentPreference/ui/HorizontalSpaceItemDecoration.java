package com.dialog.dialoggo.activities.contentPreference.ui;

import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {
    private final int horizontalItemSpace;

    public HorizontalSpaceItemDecoration(int horiontalItemSpace) {
        this.horizontalItemSpace = horiontalItemSpace;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        outRect.left = horizontalItemSpace;
    }
}
