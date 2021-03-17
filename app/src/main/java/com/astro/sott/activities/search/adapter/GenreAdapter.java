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
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;

import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.SingleItemHolder>  {
    List<SearchedKeywords> filterLanguageList;
    Context context;
    public GenreAdapter(Activity ctx, List<SearchedKeywords> list) {
        this.filterLanguageList=list;
        this.context=ctx;
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
          // holder.binding.titleText.setText(filterLanguageList.get(position).getKeyWords());

        if (filterLanguageList.get(position).isSelected()){
            holder.binding.titleText.setText(filterLanguageList.get(position).getKeyWords());
            holder.binding.titleText.setTextColor(context.getResources().getColor(R.color.filter_text_selected_color));
            holder.binding.titleText.setBackgroundColor(context.getResources().getColor(R.color.filter_text_selected_bg));

        }else {
            holder.binding.titleText.setText(filterLanguageList.get(position).getKeyWords());
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
    String selectedGenre2="";
    private void getSelectedValues(List<SearchedKeywords> arrayList) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder1=new StringBuilder();
        for (int i = 0; i<arrayList.size(); i ++){
            if (arrayList.get(i).isSelected()){
                stringBuilder.append(AppLevelConstants.SEARCH_GENRE_CONSTATNT +arrayList.get(i).getKeyWords()+"'").append("  ");
                stringBuilder1.append(arrayList.get(i).getKeyWords()).append(", ");
            }
            if (stringBuilder.length() > 0) {
                selectedGenre = stringBuilder.toString();
                selectedGenre = selectedGenre.substring(0, selectedGenre.length() - 2);
            } else {
                selectedGenre = "";
            }

            if (stringBuilder1.length() > 0) {
                selectedGenre2 = stringBuilder1.toString();
                selectedGenre2 = selectedGenre2.substring(0, selectedGenre2.length() - 2);
            } else {
                selectedGenre2 = "";
            }
        }


        KsPreferenceKey.getInstance(context).setFilterGenre(selectedGenre);
        KsPreferenceKey.getInstance(context).setFilterGenreSelection(selectedGenre2);

        Log.w("selectedGenres-:",selectedGenre);

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
