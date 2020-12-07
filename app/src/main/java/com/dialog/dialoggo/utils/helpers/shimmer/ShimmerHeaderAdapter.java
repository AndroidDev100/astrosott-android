package com.dialog.dialoggo.utils.helpers.shimmer;

import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.databinding.LayoutDemoGridBinding;

public class ShimmerHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private ArrayList<SingleItemModel> itemsList;
//    private Context mContext;

    public ShimmerHeaderAdapter() {
//        this.itemsList = itemsList;
//        this.mContext = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutDemoGridBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layout_demo_grid, parent, false);
        return new SingleItemRowHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final LayoutDemoGridBinding layoutDemoGridBinding;

        private SingleItemRowHolder(LayoutDemoGridBinding view) {
            super(view.getRoot());
            this.layoutDemoGridBinding = view;


        }
    }
}
