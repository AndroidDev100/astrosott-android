package com.astro.sott.activities.language.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.activities.search.adapter.GenreAdapter;
import com.astro.sott.databinding.ItemAppLanguageBinding;
import com.astro.sott.databinding.LanguagePreferenceItemBinding;

public class AppLanguageAdapter extends RecyclerView.Adapter<AppLanguageAdapter.SingleItemHolder> {
    @NonNull
    @Override
    public AppLanguageAdapter.SingleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppLanguageBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.language_preference_item, parent, false);
        return  new AppLanguageAdapter.SingleItemHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppLanguageAdapter.SingleItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        final ItemAppLanguageBinding binding;

        public SingleItemHolder(ItemAppLanguageBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
