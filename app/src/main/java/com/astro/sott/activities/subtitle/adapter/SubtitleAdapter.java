package com.astro.sott.activities.subtitle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.R;
import com.astro.sott.databinding.AudioLangItemBinding;
import com.astro.sott.modelClasses.dmsResponse.AudioLanguages;
import com.astro.sott.modelClasses.dmsResponse.SubtitleLanguages;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;

import java.util.ArrayList;

public class SubtitleAdapter extends RecyclerView.Adapter<SubtitleAdapter.SingleItemHolder> {

    private ArrayList<SubtitleLanguages> audioLanguageList;
    private Context context;

    public SubtitleAdapter(ArrayList<SubtitleLanguages> audioLanguageList, Context context) {
        this.audioLanguageList = audioLanguageList;
        this.context = context;
    }

    @NonNull
    @Override
    public SubtitleAdapter.SingleItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AudioLangItemBinding audioLangItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.audio_lang_item, parent, false);
        return new SingleItemHolder(audioLangItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SubtitleAdapter.SingleItemHolder holder, int position) {
        holder.audioLangItemBinding.title.setText(audioLanguageList.get(position).getKey());
        String defaultLang = new KsPreferenceKey(context).getSubtitleLanguage();
        if (defaultLang.equalsIgnoreCase("")) {
            if (audioLanguageList.get(position).getKey().equalsIgnoreCase("English")) {
                holder.audioLangItemBinding.englishTick.setVisibility(View.VISIBLE);
            } else {
                holder.audioLangItemBinding.englishTick.setVisibility(View.GONE);

            }
        } else {
            if (defaultLang.equalsIgnoreCase(audioLanguageList.get(position).getKey())) {
                holder.audioLangItemBinding.englishTick.setVisibility(View.VISIBLE);

            } else {
                holder.audioLangItemBinding.englishTick.setVisibility(View.GONE);
            }
        }
        holder.audioLangItemBinding.mainLay.setOnClickListener(view -> {

        });

        holder.audioLangItemBinding.mainLay.setOnClickListener(view -> {
            new KsPreferenceKey(context).setSubtitleLanguage(audioLanguageList.get(position).getKey());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return audioLanguageList.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        final AudioLangItemBinding audioLangItemBinding;

        public SingleItemHolder(@NonNull AudioLangItemBinding itemView) {
            super(itemView.getRoot());
            audioLangItemBinding = itemView;
        }
    }
}
