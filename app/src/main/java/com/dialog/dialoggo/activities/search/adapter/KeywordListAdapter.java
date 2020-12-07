package com.dialog.dialoggo.activities.search.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.databinding.KeywordItemBinding;
import com.dialog.dialoggo.db.search.SearchedKeywords;
import com.dialog.dialoggo.utils.helpers.PrintLogging;

import java.util.List;


public class KeywordListAdapter extends RecyclerView.Adapter<KeywordListAdapter.KeywordItemHolder> {
    private List<SearchedKeywords> list;
    private final Context context;
    private final KeywordItemHolderListener listener;

    public KeywordListAdapter(Context context, List<SearchedKeywords> searchedKeywords, KeywordItemHolderListener listener) {

        this.context = context;
        this.list = searchedKeywords;
        this.listener = listener;
    }

    public void notifyKeywordAdapter(List<SearchedKeywords> searchedKeywords) {
        this.list = searchedKeywords;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public KeywordItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        KeywordItemBinding keywordItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.keyword_item, viewGroup, false);

        return new KeywordItemHolder(keywordItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull KeywordItemHolder viewHolder, int i) {
        try {
            final int pos = i;
            viewHolder.keywordItemBinding.setRowItem(list.get(i));
            viewHolder.keywordItemBinding.rootView.setOnClickListener(view -> listener.onItemClicked(list.get(pos)));
        } catch (Exception ex) {
            PrintLogging.printLog("Exception", "", "" + ex);
        }

    }

    @Override
    public int getItemCount() {

        int listLimit = 5;
        if (list.size() >= listLimit)
            return listLimit;
        else
            return list.size();
        ///return list.size();
    }


    public interface KeywordItemHolderListener {
        void onItemClicked(SearchedKeywords itemValue);
    }

    public class KeywordItemHolder extends RecyclerView.ViewHolder {

        final KeywordItemBinding keywordItemBinding;

        KeywordItemHolder(@NonNull KeywordItemBinding binding) {
            super(binding.getRoot());
            this.keywordItemBinding = binding;
        }

    }
}
