package com.astro.sott.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.databinding.LandscapeItemBinding;
import com.astro.sott.databinding.RibbonItemBinding;

import java.util.List;

public class RibbonAdapter extends RecyclerView.Adapter<RibbonAdapter.SingleItem> {
    private List<String> ribbonList;

    public RibbonAdapter(List<String> ribbonList) {
        this.ribbonList = ribbonList;

    }

    @NonNull
    @Override
    public SingleItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RibbonItemBinding ribbonItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.ribbon_item, parent, false);
        return new SingleItem(ribbonItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItem holder, int position) {
        holder.ribbonItemBinding.ribbonText.setText(ribbonList.get(position));
    }

    @Override
    public int getItemCount() {
        return ribbonList.size();
    }

    public class SingleItem extends RecyclerView.ViewHolder {
        RibbonItemBinding ribbonItemBinding;

        public SingleItem(@NonNull RibbonItemBinding itemView) {
            super(itemView.getRoot());
            ribbonItemBinding = itemView;
        }
    }
}
