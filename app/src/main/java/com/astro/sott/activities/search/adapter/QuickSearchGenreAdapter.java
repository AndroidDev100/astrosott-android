package com.astro.sott.activities.search.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.callBacks.commonCallBacks.GenreSelectionCallBack;
import com.astro.sott.databinding.GenreItemLayoutBinding;
import com.astro.sott.utils.helpers.ImageHelper;

import java.util.ArrayList;
import java.util.List;

public class QuickSearchGenreAdapter extends RecyclerView.Adapter<QuickSearchGenreAdapter.SingleItemHolder>{
    private Fragment ctx;
    List<RailCommonData> dataList;
    GenreSelectionCallBack callBack;
    public QuickSearchGenreAdapter(Fragment activity, List<RailCommonData> data, GenreSelectionCallBack call) {
        this.ctx=activity;
        this.dataList=data;
        this.callBack=call;
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

        RailCommonData railCommonData=dataList.get(position);

//1571db
        if (railCommonData.isChecked()) {
            holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#1571db"));
            holder.binding.halfCircle.setVisibility(View.VISIBLE);
            holder.binding.imageView.setVisibility(View.GONE);
           // holder.binding.border.setVisibility(View.VISIBLE);
            holder.binding.border.setBackgroundColor(Color.parseColor("#13ff78"));

        } else {
            Log.w("selectedColor",railCommonData.getSelectedColor()+"  "+railCommonData.isChecked());
            if (railCommonData.getSelectedColor()!=null && !railCommonData.getSelectedColor().equalsIgnoreCase("")){
                if (railCommonData.getSelectedColor().equalsIgnoreCase("1")){
                    holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#1571db"));
                }else {
                    holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#303255"));
                }
            }
           // holder.binding.mainLayout.setBackgroundColor(Color.parseColor(railCommonData.getSelectedColor()));

            /*if (position%2==1){
                holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#303255"));
            }else {
                if (position!=0 && position+1%2==0){
                    holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#303255"));
                }else {
                    holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#1571db"));
                }
            }*/
           // holder.binding.mainLayout.setBackgroundColor(Color.parseColor("#303255"));
            holder.binding.halfCircle.setVisibility(View.GONE);
            holder.binding.imageView.setVisibility(View.VISIBLE);
           // holder.binding.border.setVisibility(View.GONE);
            holder.binding.border.setBackgroundColor(Color.parseColor("#191a2d"));
        }

        holder.binding.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (railCommonData.isChecked()) {
                    railCommonData.setChecked(false);
                    notifyDataSetChanged();
                } else {
                    railCommonData.setChecked(true);
                    notifyDataSetChanged();
                }

                callBack.onClick(position,dataList);
            }
        });

        holder.binding.textName.setText(railCommonData.getName());
        ImageHelper.getInstance(holder.binding.imageView.getContext()).loadImageOfGenre(holder.binding.imageView, "https://images.sgs1.ott.kaltura.com/service.svc/GetImage/p/3209/entry_id/cd0a956e5be4467eb4e6d120ca75530f/version/5", R.drawable.landscape);
        /*if (position%2==0){
            holder.binding.halfCircle.setVisibility(View.GONE);
            holder.binding.imageView.setVisibility(View.VISIBLE);
            ImageHelper.getInstance(holder.binding.imageView.getContext()).loadImageToLandscapeListingAdapter(holder.binding.imageView, "https://images.sgs1.ott.kaltura.com/service.svc/GetImage/p/3209/entry_id/cd0a956e5be4467eb4e6d120ca75530f/version/5", R.drawable.landscape);
        }else {
            holder.binding.halfCircle.setVisibility(View.VISIBLE);
            holder.binding.imageView.setVisibility(View.GONE);
            ImageHelper.getInstance(holder.binding.imageView.getContext()).loadImageToLandscapeListingAdapter(holder.binding.imageView, "https://images.sgs1.ott.kaltura.com/service.svc/GetImage/p/3209/entry_id/cd0a956e5be4467eb4e6d120ca75530f/version/5", R.drawable.landscape);
        }*/
        /*holder.binding.halfCircle.setVisibility(View.GONE);
        ImageHelper.getInstance(holder.binding.imageView.getContext()).loadImageOfGenre(holder.binding.imageView, "https://images.sgs1.ott.kaltura.com/service.svc/GetImage/p/3209/entry_id/cd0a956e5be4467eb4e6d120ca75530f/version/5", R.drawable.landscape);
*/
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        GenreItemLayoutBinding binding;
        public SingleItemHolder( GenreItemLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding=itemView;
        }
    }
}
