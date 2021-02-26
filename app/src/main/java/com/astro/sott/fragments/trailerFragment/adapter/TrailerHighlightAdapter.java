package com.astro.sott.fragments.trailerFragment.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerHighlightAdapter extends RecyclerView.Adapter<TrailerHighlightAdapter.SingleItemHolder> {
    @NonNull
    @Override
    public SingleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        public SingleItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
