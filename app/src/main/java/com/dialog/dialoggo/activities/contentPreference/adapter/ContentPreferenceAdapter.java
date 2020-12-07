package com.dialog.dialoggo.activities.contentPreference.adapter;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.baseModel.PrefrenceBean;
import com.dialog.dialoggo.databinding.UserPreferenceItemBinding;

import java.util.ArrayList;

public class ContentPreferenceAdapter extends RecyclerView.Adapter<ContentPreferenceAdapter.SingleItemRowHolder> {



    private final Activity activity;
    //    String type;
    private final ArrayList<PrefrenceBean> arrayList;
    private int count = 0;

    public ContentPreferenceAdapter(Activity ctx, ArrayList<PrefrenceBean> list/*, String type*/) {
        activity = ctx;
//        this.type = type;
        this.arrayList = list;
        // arrayList = new ArrayList<>();
        getGenreList();
    }


    public ArrayList<PrefrenceBean> getGenreList() {
        return getSelectedList(arrayList);
    }

    private ArrayList<PrefrenceBean> getSelectedList(ArrayList<PrefrenceBean> arrayList) {
        ArrayList<PrefrenceBean> selectedList = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getChecked()) {
                count++;
                selectedList.add(arrayList.get(i));
            }
        }
        return selectedList;
    }

    @NonNull
    @Override
    public ContentPreferenceAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        UserPreferenceItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.user_preference_item, viewGroup, false);

        return  new SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull final SingleItemRowHolder viewHolder, final int position) {

        if (arrayList.get(position).getChecked()) {
            viewHolder.genreItemBinding.titleText.setBackgroundResource(R.drawable.genre_selected);
            viewHolder.genreItemBinding.titleText.setTextColor(activity.getResources().getColor(R.color.white));
        } else {
            viewHolder.genreItemBinding.titleText.setBackgroundResource(R.drawable.genre_unselected);
            viewHolder.genreItemBinding.titleText.setTextColor(activity.getResources().getColor(R.color.black));
        }
        viewHolder.genreItemBinding.titleText.setText(arrayList.get(position).getName());
        viewHolder.genreItemBinding.titleText.setOnClickListener(view -> {
            if (count > 4) {
                if (arrayList.get(position).getChecked()) {
                    if (arrayList.get(position).getChecked()) {
                        count--;
                        arrayList.get(position).setChecked(false);
                        notifyDataSetChanged();
                    } else {
                        count++;
                        arrayList.get(position).setChecked(true);
                        notifyDataSetChanged();
                    }
                }

            } else {
                if (arrayList.get(position).getChecked()) {
                    count--;
                    arrayList.get(position).setChecked(false);
                    notifyDataSetChanged();
                } else {
                    count++;
                    arrayList.get(position).setChecked(true);
                    notifyDataSetChanged();
                }
            }

        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final UserPreferenceItemBinding genreItemBinding;

        private SingleItemRowHolder(UserPreferenceItemBinding binding) {
            super(binding.getRoot());
            this.genreItemBinding = binding;
        }

    }


}