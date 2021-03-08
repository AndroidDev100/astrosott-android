package com.astro.sott.activities.search.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.astro.sott.R;
import com.astro.sott.databinding.GenreItemLayoutBinding;

public class QuickSearchGenreAdapter extends RecyclerView.Adapter<QuickSearchGenreAdapter.SingleItemHolder>{
    private Fragment ctx;
    public QuickSearchGenreAdapter(Fragment activity) {
        this.ctx=activity;
    }

    @NonNull
    @Override
    public QuickSearchGenreAdapter.SingleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      GenreItemLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.genre_item_layout, parent, false);
      return new SingleItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuickSearchGenreAdapter.SingleItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 116;
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        GenreItemLayoutBinding binding;
        public SingleItemHolder( GenreItemLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding=itemView;
        }
    }
}
