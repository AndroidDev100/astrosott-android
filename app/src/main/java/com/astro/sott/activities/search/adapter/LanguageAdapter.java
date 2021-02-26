package com.astro.sott.activities.search.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.databinding.LanguagePreferenceItemBinding;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.SingleItemHolder> {


    public LanguageAdapter(Activity context) {

    }

    @NonNull
    @Override
    public LanguageAdapter.SingleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LanguagePreferenceItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.language_preference_item, parent, false);
        return  new SingleItemHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageAdapter.SingleItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {

        return 8;
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        final LanguagePreferenceItemBinding binding;
        public SingleItemHolder(LanguagePreferenceItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding=itemBinding;
        }
    }
}
