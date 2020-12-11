package com.astro.sott.activities.SelectAccount.adapter;

import android.app.Activity;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.astro.sott.R;
import com.astro.sott.activities.SelectAccount.SelectAccountModel.DetailListItem;
import com.astro.sott.callBacks.DTVItemClickListner;
import com.astro.sott.databinding.AccountItemBinding;
import com.astro.sott.utils.helpers.AppLevelConstants;

import java.util.List;

public class AccountItemAdapter extends RecyclerView.Adapter<AccountItemAdapter.SingleItemHolder> {
    private Activity activity;
    private List<DetailListItem> data;
    private DTVItemClickListner dtvItemClickListner;
    private int mSelectedPosition = 0;



    public AccountItemAdapter(Activity ctx, List<DetailListItem> channelList, DTVItemClickListner dtvItemClickListner) {
        activity = ctx;
        this.data = channelList;
        this.dtvItemClickListner = dtvItemClickListner;

    }
    private View view;

    @NonNull
    @Override
    public AccountItemAdapter.SingleItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        AccountItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.account_item, viewGroup, false);

        return new AccountItemAdapter.SingleItemHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleItemHolder singleItemHolder, int position) {

//        if(data.get(position).getLob().equalsIgnoreCase(AppLevelConstants.VOLTE)){
//            singleItemHolder.accountItemBinding.headerTitle.setText(AppLevelConstants.BROAD_BAND_HBB);
//            singleItemHolder.accountItemBinding.dtvAccNumber.setText(data.get(position).getRefAccount());
//        }else

        if(data.get(position).getLob().equalsIgnoreCase(AppLevelConstants.MBB)){
            singleItemHolder.accountItemBinding.headerTitle.setText(AppLevelConstants.MOBILE);
            singleItemHolder.accountItemBinding.dtvAccNumber.setText(data.get(position).getRefAccount());
        }else if(data.get(position).getLob().equalsIgnoreCase(AppLevelConstants.DTV)){
            singleItemHolder.accountItemBinding.headerTitle.setText(AppLevelConstants.DIALOG_TV);
            singleItemHolder.accountItemBinding.dtvAccNumber.setText(data.get(position).getRefAccount());
        }


        singleItemHolder.accountItemBinding.mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedPosition = position;
                notifyDataSetChanged();
                dtvItemClickListner.onClick(data.get(position).getLob(),data.get(position).getRefAccount());
            }
        });

        if (position == mSelectedPosition) {
            singleItemHolder.accountItemBinding.radioSelector.setChecked(true);


            //  singleItemHolder.multiplePlaylistItemBinding.radioSelector.setHighlightColor(Color.parseColor("#1E88E5"));
        } else {
            singleItemHolder.accountItemBinding.radioSelector.setChecked(false);
        }



    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SingleItemHolder extends RecyclerView.ViewHolder {
        RelativeLayout linearLayout;
        final AccountItemBinding accountItemBinding;
        SingleItemHolder(AccountItemBinding binding) {
            super(binding.getRoot());
            this.accountItemBinding = binding;


            //linearLayout = view.findViewById(R.id.item);

        }
    }




}