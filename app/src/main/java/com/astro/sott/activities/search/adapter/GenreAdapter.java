package com.astro.sott.activities.search.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.databinding.LanguagePreferenceItemBinding;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.SingleItemHolder>  {
    public GenreAdapter(Activity context) {

    }

    @NonNull
    @Override
    public GenreAdapter.SingleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LanguagePreferenceItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.language_preference_item, parent, false);
        return  new SingleItemHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.SingleItemHolder holder, int position) {

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
