package com.astro.sott.activities.search.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.databinding.LanguagePreferenceItemBinding;
import com.astro.sott.db.search.SearchedKeywords;
import com.astro.sott.modelClasses.dmsResponse.FilterLanguages;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;

import java.util.ArrayList;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.SingleItemHolder> {

    ArrayList<FilterLanguages> filterLanguageList;
    Context context;
    public LanguageAdapter(Activity cext, ArrayList<FilterLanguages> filterLangList) {
            this.filterLanguageList=filterLangList;
            this.context=cext;
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
        if (filterLanguageList.get(position).isSelected()){
            holder.binding.titleText.setText(filterLanguageList.get(position).getValue());
            holder.binding.titleText.setTextColor(Color.parseColor("#151024"));
            holder.binding.titleText.setBackgroundColor(Color.parseColor("#00e895"));

        }else {
            holder.binding.titleText.setText(filterLanguageList.get(position).getValue());
            holder.binding.titleText.setTextColor(context.getResources().getColor(R.color.grey_text));
            holder.binding.titleText.setBackgroundColor(context.getResources().getColor(R.color.edit_text_blue_bg));

        }

        holder.binding.titleText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filterLanguageList.get(position).isSelected()) {
                    filterLanguageList.get(position).setSelected(false);
                    notifyDataSetChanged();
                } else {
                    filterLanguageList.get(position).setSelected(true);
                    notifyDataSetChanged();
                }
                getSelectedValues(filterLanguageList);
            }
        });
    }

    String selectedGenre="";
    private void getSelectedValues(List<FilterLanguages> arrayList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i<arrayList.size(); i ++){
            if (arrayList.get(i).isSelected()){
                stringBuilder.append(AppLevelConstants.FILTER_LANGUAGE_CONSTANT +arrayList.get(i).getValue()+"'").append("  ");

            }
            if (stringBuilder.length() > 0) {
                selectedGenre = stringBuilder.toString();
                selectedGenre = selectedGenre.substring(0, selectedGenre.length() - 2);
            } else {
                selectedGenre = "";
            }
        }

        Log.w("selectedGenres-:",selectedGenre);

        KsPreferenceKey.getInstance(context).setFilterLanguage(selectedGenre);

    }


    @Override
    public int getItemCount() {
        return filterLanguageList.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        final LanguagePreferenceItemBinding binding;
        public SingleItemHolder(LanguagePreferenceItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding=itemBinding;
        }
    }
}
