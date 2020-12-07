package com.dialog.dialoggo.activities.parentalControl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.callBacks.commonCallBacks.RestrictionClickListner;
import com.dialog.dialoggo.databinding.RestrictionItemBinding;
import com.dialog.dialoggo.db.search.SearchedKeywords;
import com.dialog.dialoggo.modelClasses.dmsResponse.ParentalDescription;
import com.dialog.dialoggo.modelClasses.dmsResponse.ParentalLevels;
import com.dialog.dialoggo.modelClasses.dmsResponse.ResponseDmsModel;
import com.dialog.dialoggo.utils.commonMethods.AppCommonMethods;
import com.dialog.dialoggo.utils.ksPreferenceKey.KsPreferenceKey;

import java.util.ArrayList;
import java.util.List;

public class RestrictionAdapter extends RecyclerView.Adapter<RestrictionAdapter.SingleItemRowHolder> {

    private final Context context;
    private ArrayList<ParentalDescription> arrayList;
    private String defaultParentalRating;
    private String userSelectedParentalRating = "";
    RestrictionClickListner restrictionClickListner;

    public RestrictionAdapter(Context context, ArrayList<ParentalDescription> restrictionArrayList, RestrictionClickListner clickListner) {
        this.context = context;
        this.arrayList = restrictionArrayList;
        this.restrictionClickListner = clickListner;
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        defaultParentalRating = responseDmsModel.getParams().getDefaultParentalLevel();
        userSelectedParentalRating = KsPreferenceKey.getInstance(context).getUserSelectedRating();
    }

    //Notify the Adapter
    public void notifyRestrictionAdapter(ArrayList<ParentalDescription> searchedKeywords) {
        this.arrayList = searchedKeywords;
        ResponseDmsModel responseDmsModel = AppCommonMethods.callpreference(context);
        defaultParentalRating = responseDmsModel.getParams().getDefaultParentalLevel();
        userSelectedParentalRating = KsPreferenceKey.getInstance(context).getUserSelectedRating();
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public RestrictionAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RestrictionItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.restriction_item, viewGroup, false);

        return new RestrictionAdapter.SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RestrictionAdapter.SingleItemRowHolder holder, int position) {


        holder.restrictionItemBinding.restrictionLevel.setText(arrayList.get(position).getKey());
        holder.restrictionItemBinding.restrictionText.setText(arrayList.get(position).getDescription());



        if(position==(getItemCount()-1)){
            holder.restrictionItemBinding.view.setVisibility(View.GONE);
            holder.restrictionItemBinding.radioSelector.setBackgroundResource(R.drawable.upper_line_unfilled);
        }else if(position == 0){
            holder.restrictionItemBinding.view.setVisibility(View.VISIBLE);
            holder.restrictionItemBinding.radioSelector.setBackgroundResource(R.drawable.lower_line_unfilled);
        }
        else {
            holder.restrictionItemBinding.view.setVisibility(View.VISIBLE);
            holder.restrictionItemBinding.radioSelector.setBackgroundResource(R.drawable.both_line_unfilled);
        }

        if(!userSelectedParentalRating.equalsIgnoreCase("")){
            if(userSelectedParentalRating.equalsIgnoreCase(arrayList.get(position).getKey())){
                if(position==(getItemCount()-1)){
                    holder.restrictionItemBinding.view.setVisibility(View.GONE);
                    holder.restrictionItemBinding.radioSelector.setBackgroundResource(R.drawable.upper_line_filled);
                    restrictionClickListner.onClick(false,arrayList.get(position).getKey());
                }else if(position == 0){
                    holder.restrictionItemBinding.view.setVisibility(View.VISIBLE);
                    holder.restrictionItemBinding.radioSelector.setBackgroundResource(R.drawable.lower_line_filled);
                    restrictionClickListner.onClick(false,arrayList.get(position).getKey());
                }
                else {
                    holder.restrictionItemBinding.view.setVisibility(View.VISIBLE);
                    holder.restrictionItemBinding.radioSelector.setBackgroundResource(R.drawable.both_line_filled);
                    restrictionClickListner.onClick(false,arrayList.get(position).getKey());
                }
            }

        } else {

            if (defaultParentalRating.equalsIgnoreCase(arrayList.get(position).getKey())) {
                if (position == (getItemCount() - 1)) {
                    holder.restrictionItemBinding.view.setVisibility(View.GONE);
                    holder.restrictionItemBinding.radioSelector.setBackgroundResource(R.drawable.upper_line_filled);
                    restrictionClickListner.onClick(false,arrayList.get(position).getKey());
                } else if (position == 0) {
                    holder.restrictionItemBinding.view.setVisibility(View.VISIBLE);
                    holder.restrictionItemBinding.radioSelector.setBackgroundResource(R.drawable.lower_line_filled);
                    restrictionClickListner.onClick(false,arrayList.get(position).getKey());
                } else {
                    holder.restrictionItemBinding.view.setVisibility(View.VISIBLE);
                    holder.restrictionItemBinding.radioSelector.setBackgroundResource(R.drawable.both_line_filled);
                    restrictionClickListner.onClick(false,arrayList.get(position).getKey());
                }
            }
        }


        holder.restrictionItemBinding.rootLayout.setOnClickListener(view -> {
            if(position==(getItemCount()-1)){
                holder.restrictionItemBinding.view.setVisibility(View.GONE);
                holder.restrictionItemBinding.radioSelector.setBackgroundResource(R.drawable.upper_line_filled);
                KsPreferenceKey.getInstance(context).setUserSelectedRating(arrayList.get(position).getKey());

            }else if(position == 0){
                holder.restrictionItemBinding.view.setVisibility(View.VISIBLE);
                holder.restrictionItemBinding.radioSelector.setBackgroundResource(R.drawable.lower_line_filled);
                KsPreferenceKey.getInstance(context).setUserSelectedRating(arrayList.get(position).getKey());

            }
            else {
                holder.restrictionItemBinding.view.setVisibility(View.VISIBLE);
                holder.restrictionItemBinding.radioSelector.setBackgroundResource(R.drawable.both_line_filled);
                KsPreferenceKey.getInstance(context).setUserSelectedRating(arrayList.get(position).getKey());


            }

            restrictionClickListner.onClick(true,arrayList.get(position).getKey());


        });


    }

    @Override
    public int getItemCount() {
        if(arrayList == null){
            return 0;
        }
        return arrayList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final RestrictionItemBinding restrictionItemBinding;

        SingleItemRowHolder(RestrictionItemBinding binding) {
            super(binding.getRoot());
            this.restrictionItemBinding = binding;
        }

    }

}
