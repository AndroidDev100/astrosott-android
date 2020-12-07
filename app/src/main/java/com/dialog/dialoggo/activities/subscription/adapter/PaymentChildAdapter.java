package com.dialog.dialoggo.activities.subscription.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.activities.subscription.model.BillPaymentDetails;
import com.dialog.dialoggo.callBacks.commonCallBacks.BillPaymentMethodCallBack;
import com.dialog.dialoggo.databinding.BillPaymentItemsItemBinding;

import java.util.List;

public class PaymentChildAdapter extends RecyclerView.Adapter<PaymentChildAdapter.MyViewHolder> {
    private List<BillPaymentDetails> mList;
    private Context mContext;
    private BillPaymentMethodCallBack billPaymentMethodCallBack;
    private int mSelectedItem = 0;



    public PaymentChildAdapter(List<BillPaymentDetails> billPaymentDetails, Context mcontext, BillPaymentMethodCallBack mListener) {
        this.mList = billPaymentDetails;
        this.mContext = mcontext;
        this.billPaymentMethodCallBack = mListener;
//
    }

    @Override
    public PaymentChildAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BillPaymentItemsItemBinding  billPaymentItemsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.bill_payment_items_item, parent, false);
        return new MyViewHolder(billPaymentItemsItemBinding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.billPaymentItemsItemBinding.txtPaymentMethod.setText(mList.get(position).getAccountType());
        holder.billPaymentItemsItemBinding.txtPaymentNumber.setText(mList.get(position).getAccountNumber());

        holder.billPaymentItemsItemBinding.lnpaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedItem = position;
                notifyDataSetChanged();
            }
        });
        if(position==mSelectedItem) {
            holder.billPaymentItemsItemBinding.radioPaymentSeletor.setChecked(true);
            if (billPaymentMethodCallBack != null) {
                billPaymentMethodCallBack.itemclicked(mList.get(position).getAccountType(), mList.get(position).getAccountNumber());
            }
        }else {
            holder.billPaymentItemsItemBinding.radioPaymentSeletor.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        BillPaymentItemsItemBinding billPaymentItemsItemBinding;
        public MyViewHolder(BillPaymentItemsItemBinding itemView) {
            super(itemView.getRoot());
            this.billPaymentItemsItemBinding = itemView;



        }
    }
}

