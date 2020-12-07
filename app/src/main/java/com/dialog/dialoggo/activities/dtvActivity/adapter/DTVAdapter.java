package com.dialog.dialoggo.activities.dtvActivity.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.callBacks.DTVItemClickListner;
import com.dialog.dialoggo.databinding.DtvItemBinding;

public class DTVAdapter extends RecyclerView.Adapter<DTVAdapter.SingleItemRowHolder> {

//    private final List<InboxMessage> inboxMessages;
    private DTVItemClickListner itemClickListener;
//    private final NotificationDeleteClickListener notificationDeleteClickListener;
    private final Context context;
    private String dtvAccountInfo;

    public DTVAdapter(Context context, DTVItemClickListner dtvItemClickListner, String dtvAccount) {
        this.context = context;
        this.dtvAccountInfo = dtvAccount;
//        this.inboxMessages = itemsList;
        this.itemClickListener = dtvItemClickListner;
//        this.notificationDeleteClickListener = notificationDeleteClickListener;

    }

    @NonNull
    @Override
    public DTVAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        DtvItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.dtv_item, viewGroup, false);

        return new DTVAdapter.SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull final DTVAdapter.SingleItemRowHolder viewHolder, final int position) {

        viewHolder.dtvItemBinding.dtvAccNumber.setText(dtvAccountInfo);

        viewHolder.dtvItemBinding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick("Change",dtvAccountInfo);
            }
        });

        viewHolder.dtvItemBinding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick("Delete",dtvAccountInfo);
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
