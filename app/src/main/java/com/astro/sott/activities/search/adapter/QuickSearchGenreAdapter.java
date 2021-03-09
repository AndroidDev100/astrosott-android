package com.astro.sott.activities.search.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.astro.sott.R;
import com.astro.sott.databinding.GenreItemLayoutBinding;
import com.astro.sott.utils.helpers.ImageHelper;

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
       /* if (position%2==0){
            holder.binding.halfCircle.setVisibility(View.INVISIBLE);
            holder.binding.imageView.setVisibility(View.VISIBLE);
            ImageHelper.getInstance(holder.binding.imageView.getContext()).loadImageToLandscapeListingAdapter(holder.binding.imageView, "https://images.sgs1.ott.kaltura.com/service.svc/GetImage/p/3209/entry_id/cd0a956e5be4467eb4e6d120ca75530f/version/5", R.drawable.landscape);
        }else {
            holder.binding.halfCircle.setVisibility(View.VISIBLE);
            holder.binding.imageView.setVisibility(View.INVISIBLE);
            ImageHelper.getInstance(holder.binding.imageView.getContext()).loadImageToLandscapeListingAdapter(holder.binding.imageView, "https://images.sgs1.ott.kaltura.com/service.svc/GetImage/p/3209/entry_id/cd0a956e5be4467eb4e6d120ca75530f/version/5", R.drawable.landscape);
        }*/

        ImageHelper.getInstance(holder.binding.imageView.getContext()).loadImageToLandscapeListingAdapter(holder.binding.imageView, "https://images.sgs1.ott.kaltura.com/service.svc/GetImage/p/3209/entry_id/cd0a956e5be4467eb4e6d120ca75530f/version/5", R.drawable.landscape);

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
