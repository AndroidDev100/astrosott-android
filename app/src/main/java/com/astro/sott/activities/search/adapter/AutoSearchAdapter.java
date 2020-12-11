package com.astro.sott.activities.search.adapter;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.astro.sott.activities.search.viewModel.SearchViewModel;
import com.astro.sott.R;
import com.astro.sott.databinding.AutoCompleteItemBinding;
import com.kaltura.client.types.Asset;

import java.util.List;

public class AutoSearchAdapter extends RecyclerView.Adapter<AutoSearchAdapter.SingleRowHolder> {

    private final SearchNormalItemListener itemListener;
    FragmentActivity mcontext;
    List<Asset> massetList;
    private SearchViewModel searchViewModel;
    private String searchedtext;
    public AutoSearchAdapter(FragmentActivity context, List<Asset> assetList, SearchNormalItemListener listener, String searchedText) {
        this.massetList = assetList;
        itemListener = listener;
        this.mcontext = context;
        searchViewModel = ViewModelProviders.of(this.mcontext).get(SearchViewModel.class);
        this.searchedtext = searchedText;
    }

    @NonNull
    @Override
    public SingleRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        AutoCompleteItemBinding autoCompleteItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.auto_complete_item, parent, false);
        return new SingleRowHolder(autoCompleteItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleRowHolder singleRowHolder, int i) {

        singleRowHolder.autoCompleteItemBinding.textAuto.setText(massetList.get(i).getName());
        singleRowHolder.autoCompleteItemBinding.textAuto.setOnClickListener(view ->{
            itemListener.onItemClicked(massetList.get(i), 23);
            searchViewModel.insertRecentSearchKeywords(searchedtext);
        } );

    }

    @Override
    public int getItemCount() {
        return massetList.size();
    }

    public interface SearchNormalItemListener {
        void onItemClicked(Asset itemValue, int position);
    }

    class SingleRowHolder extends RecyclerView.ViewHolder {
        AutoCompleteItemBinding autoCompleteItemBinding;

        SingleRowHolder(@NonNull AutoCompleteItemBinding itemView) {
            super(itemView.getRoot());
            this.autoCompleteItemBinding = itemView;
        }
    }
}
