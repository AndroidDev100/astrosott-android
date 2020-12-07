package com.dialog.dialoggo.activities.notification.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.callBacks.commonCallBacks.NotificationDeleteClickListener;
import com.dialog.dialoggo.callBacks.commonCallBacks.NotificationItemClickListner;
import com.dialog.dialoggo.databinding.NotificationItemBinding;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;
import com.dialog.dialoggo.utils.helpers.PrintLogging;
import com.kaltura.client.types.InboxMessage;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.SingleItemRowHolder> {

    private final List<InboxMessage> inboxMessages;
    private final NotificationItemClickListner itemClickListener;
    private final NotificationDeleteClickListener notificationDeleteClickListener;
    private final Context context;

    public NotificationAdapter(Context context, List<InboxMessage> itemsList, NotificationItemClickListner listener, NotificationDeleteClickListener notificationDeleteClickListener) {
        this.context = context;
        this.inboxMessages = itemsList;
        this.itemClickListener = listener;
        this.notificationDeleteClickListener = notificationDeleteClickListener;

    }

    @NonNull
    @Override
    public NotificationAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        NotificationItemBinding itemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.notification_item, viewGroup, false);

        return new NotificationAdapter.SingleItemRowHolder(itemBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.SingleItemRowHolder viewHolder, final int position) {
        String status = inboxMessages.get(position).getStatus().getValue();
        PrintLogging.printLog(this.getClass(), "", "notificationStatus" + inboxMessages.get(position).getStatus().toString());
        PrintLogging.printLog(this.getClass(), "", "notificationStatus" + inboxMessages.get(position).getMessage().toString());
        PrintLogging.printLog(this.getClass(), "", "notificationStatus" + inboxMessages.get(position).getUrl().toString());
        PrintLogging.printLog(this.getClass(), "", "notificationStatus" + inboxMessages.get(position));
        if (inboxMessages.get(position).getStatus().toString().equals(AppLevelConstants.STATUS_READ)) {
            viewHolder.notificationItemBinding.notificationrow.setBackgroundResource(R.color.gray);
        } else if (inboxMessages.get(position).getStatus().toString().equals(AppLevelConstants.STATUS_UNREAD)) {
            viewHolder.notificationItemBinding.notificationrow.setBackgroundResource(R.color.black);

        }
        viewHolder.notificationItemBinding.setTitle(inboxMessages.get(position));
        viewHolder.notificationItemBinding.notificationrow.setOnClickListener(view -> {
            String id = inboxMessages.get(position).getId();
            String status1 = AppLevelConstants.CHANGE_STATUS_READ;
            itemClickListener.onClick(id, status1);
            viewHolder.notificationItemBinding.notificationrow.setBackgroundResource(R.color.black);
        });

        viewHolder.notificationItemBinding.flDeleteNotification.setOnClickListener(v -> notificationDeleteClickListener.notificationDelete(position));
    }


    @Override
    public int getItemCount() {
        return inboxMessages.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        final NotificationItemBinding notificationItemBinding;

        SingleItemRowHolder(NotificationItemBinding binding) {
            super(binding.getRoot());
            this.notificationItemBinding = binding;
//            notificationItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                   PrintLogging.printLog(this.getClass(),"","notificationmessageis"+inboxMessages.get(getLayoutPosition().)));
//                }
//            });
        }

    }


}
