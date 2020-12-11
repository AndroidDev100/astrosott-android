package com.astro.sott.activities.myPlans.adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.astro.sott.beanModel.subscriptionmodel.SubscriptionModel;
import com.astro.sott.R;
import com.astro.sott.databinding.SubcriptionPackageListItemBinding;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyPlanAdapter extends RecyclerView.Adapter<MyPlanAdapter.SingleItemRowHolder> {

    private static final String TAG = "PlanAdapter";
    private List<SubscriptionModel> mPlanModelList;
    private int mCurrentSelectionPosition = -1;
    private final int DELAY_IN_MILLI_SEC_60 = 60;
    private PlanAdapterListener mListener;
    private long lastClickTime;
    private long mRenewalDate;
    private Context context;


    public MyPlanAdapter(List<SubscriptionModel> planModelList, PlanAdapterListener listener, Context myPlansActivity) {
        this.mListener = listener;
        this.mPlanModelList = planModelList;
        this.context = myPlansActivity;
    }

    @NonNull
    @Override
    public MyPlanAdapter.SingleItemRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubcriptionPackageListItemBinding itemBinding;
        itemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.subcription_package_list_item, parent, false);
        return new MyPlanAdapter.SingleItemRowHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPlanAdapter.SingleItemRowHolder holder, int position) {
        SubscriptionModel subscriptionModel = mPlanModelList.get(position);

        Long date = subscriptionModel.getSubscription().getEndDate();
        String date1 = getDateandTime(subscriptionModel.getNextrenewalDate());

       String time =  getDateCurrentTimeZone(subscriptionModel.getNextrenewalDate());
// <string name="you_are">you are subscribed to</string>
//    <string name="and_you_have_left">and you have left</string>
//    <string name="validity">validity of your pack.</string>

        try {
         if (subscriptionModel.isRenewableForPurchase()) {
             holder.itemBinding.activeIcon.setVisibility(View.VISIBLE);
             holder.itemBinding.btnCancel.setBackgroundResource(R.drawable.rounded_red_button);
             holder.itemBinding.btnCancel.setEnabled(true);
             SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy  HH:mm:ss");
             Date event_date = dateFormat.parse(time);
             Date current_date = new Date();
             long diff = event_date.getTime() - current_date.getTime();

             int Days = (int) (diff / (24 * 60 * 60 * 1000));
             if (Days < 0 || Days == 0 || Days == 1) {
                 holder.itemBinding.txtPackageDescription.setText("You have subscribed to" + " " + subscriptionModel.getSubscription().getName()+ " " + "with" + " " + "1 day" + " " + "validity."+" "+"Above price excludes tax.");
             } else {
                 holder.itemBinding.txtPackageDescription.setText("you have subscribed to" + " " + subscriptionModel.getSubscription().getName()+ " " + "with" + " " + Days +" Days"+ " " + "validity."+" "+"Above price excludes tax.");

             }
         }else {
             holder.itemBinding.btnCancel.setBackgroundResource(R.drawable.rounded_gray_button);
             holder.itemBinding.btnCancel.setAlpha(0.5f);
             holder.itemBinding.btnCancel.setEnabled(false);

             holder.itemBinding.activeIcon.setVisibility(View.GONE);
             holder.itemBinding.txtPackageDescription.setText("You have cancelled your subscription , but your package expires on"+" "+date1.concat(".") +" "+ "You can enjoy Live TV and 1000+ videos till then."+" "+"Above price excludes tax.");
         }

        }catch (ParseException e){

        }


        holder.itemBinding.txtTitle.setText(subscriptionModel.getSubscription().getName());

       // int price = subscriptionModel.getSubscription().getPrice().getPrice().getAmount().intValue();
        String finalPrice = new DecimalFormat("#").format(subscriptionModel.getSubscription().getPrice().getPrice().getAmount());
        holder.itemBinding.txtCharge.setText(subscriptionModel.getSubscription().getPrice().getPrice().getCurrency());
        holder.itemBinding.txtCharge1.setText(finalPrice);
        holder.itemBinding.txtExpiryDate.setText(date1);

        holder.itemBinding.txtViewAllChannel.setOnClickListener(v -> {
            mListener.openBottomSheet(true);
        });

        holder.itemBinding.btnCancel.setOnClickListener(v -> mListener.cancelSubscription(true,subscriptionModel.getSubscription().getId()));

    }

    private String getDateandTime(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time*1000);
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
        String time1 =DateFormat.format("hh:mm a",cal.getTimeInMillis()).toString();
       // String dateString = DateFormat.format("dd/MM/yyyy  HH:mm:ss", new Date(time*1000)).toString();
//        return date.concat(" ").concat("(").concat(time1).concat(")");
        return date.concat(" ").concat(time1);
    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time*1000);
        String date = DateFormat.format("dd/MM/yyyy", cal).toString();
      //  String time1 =DateFormat.format("HH:mm:ss",cal.getTimeInMillis()).toString();
        // String dateString = DateFormat.format("dd/MM/yyyy  HH:mm:ss", new Date(time*1000)).toString();
        return date;
    }


    @Override
    public int getItemCount() {
        if(mPlanModelList != null) {
            return mPlanModelList.size();
        }
        return 0;
    }

    public static class SingleItemRowHolder extends RecyclerView.ViewHolder {
        final SubcriptionPackageListItemBinding itemBinding;

        public SingleItemRowHolder(@NonNull SubcriptionPackageListItemBinding binding) {
            super(binding.getRoot());
            this.itemBinding = binding;
        }
    }

    public interface PlanAdapterListener {
        void openBottomSheet(boolean open);
        void cancelSubscription(boolean close, String id);
    }

    public static String getDateCurrentTimeZone(long timestamp) {
        try {
//            long time = timestamp / 1000;
//            Calendar calendar = Calendar.getInstance();
//            TimeZone tz = TimeZone.getTimeZone("GMT");
//            calendar.setTimeInMillis(time * 1000);
//            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy  HH:mm:ss");
//            Date currenTimeZone = (Date) calendar.getTime();
//            return sdf.format(currenTimeZone);

            String dateString = DateFormat.format("dd-MMM-yyyy  HH:mm:ss", new Date(timestamp*1000)).toString();
            return dateString;
        } catch (Exception e) {
        }
        return "";
    }
}
