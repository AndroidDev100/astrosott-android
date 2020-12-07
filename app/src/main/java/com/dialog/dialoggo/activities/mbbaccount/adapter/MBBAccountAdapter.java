package com.dialog.dialoggo.activities.mbbaccount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.mbbaccount.listener.MBBItemClickListner;
import com.dialog.dialoggo.callBacks.DTVItemClickListner;
import com.dialog.dialoggo.databinding.DtvItemBinding;

public class MBBAccountAdapter extends RecyclerView.Adapter<MBBAccountAdapter.SingleItemRowHolder> {

//    private final List<InboxMessage> inboxMessages;
    private MBBItemClickListner itemClickListener;
//    private final NotificationDeleteClickListener notificationDeleteClickListener;
    private final Context context;
    private String mbbAccountInfo;

    public MBBAccountAdapter(Context context, MBBItemClickListner mbbItemClickListner, String mbbAccount) {
        this.context = context;
        this.mbbAccountInfo = mbbAccount;
//        this.inboxMessages = itemsList;
        this.itemClickListener = mbbItemClickListner;
//        this.notificationDeleteClickListener = notificationDeleteClickListener;

    }

    @NonNull
    @Override
    public MBBAccountAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        DtvItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dtv_item, viewGroup, false);

        return new MBBAccountAdapter.SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull final MBBAccountAdapter.SingleItemRowHolder viewHolder, final int position) {

        viewHolder.dtvItemBinding.dtvAccNumber.setText(mbbAccountInfo+"");

        viewHolder.dtvItemBinding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick("Change",mbbAccountInfo);
            }
        });

        viewHolder.dtvItemBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick("Delete",mbbAccountInfo);
            }
        });
    }


    @Override
    public int getItemCount() {
        return 1;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final DtvItemBinding dtvItemBinding;

        SingleItemRowHolder(DtvItemBinding binding) {
            super(binding.getRoot());
            this.dtvItemBinding = binding;
//            notificationItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                   PrintLogging.printLog(this.getClass(),"","notificationmessageis"+inboxMessages.get(getLayoutPosition().)));
//                }
//            });
        }

    }


}
