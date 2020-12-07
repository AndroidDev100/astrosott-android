package com.dialog.dialoggo.adapter.shimmer;


import android.annotation.SuppressLint;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.databinding.ShimmerPotraitItemBinding;

 class ShimmerPortraitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public ShimmerPortraitAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        ShimmerPotraitItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.shimmer_potrait_item, parent, false);
        return new SingleItemRowHolder(binding);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        //holder.itemView.setBackgroundColor(R.color.gray);
    }


    @Override
    public int getItemCount() {
        return 0;
    }

     class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final ShimmerPotraitItemBinding shimmerPotraitItemBinding;

         SingleItemRowHolder(ShimmerPotraitItemBinding view) {
            super(view.getRoot());
            this.shimmerPotraitItemBinding = view;
        }

    }

}

