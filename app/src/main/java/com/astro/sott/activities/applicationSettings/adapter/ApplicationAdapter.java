package com.astro.sott.activities.applicationSettings.adapter;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.astro.sott.utils.helpers.DrawableHelper;
import com.astro.sott.utils.helpers.PrintLogging;
import com.astro.sott.R;
import com.astro.sott.databinding.ApplicationItemBinding;

import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {
    private final List<String> itemsList;
    private final Activity mContext;

    public ApplicationAdapter(Activity context, List<String> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;

    }

    @NonNull
    @Override
    public ApplicationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        ApplicationItemBinding applicationItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.application_item, viewGroup, false);
        return new ApplicationAdapter.ViewHolder(applicationItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.ViewHolder holder, int i) {
        holder.applicationItemBinding.moreListTitle.setText( itemsList.get(i));
        setIcons(holder.applicationItemBinding.moreListIcon, i);

    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    private void setIcons(ImageView v, int i) {


        if (itemsList.size() == mContext.getResources().getStringArray(R.array.application_setting_options).length) {
            switch (i) {
                case 0:
                    callDrawableHelper(mContext, v);
                    break;
                case 1:
                    callDrawableHelper(mContext, v);
                    break;
                case 2:
                    callDrawableHelper(mContext, v);
                    break;
                case 3:
                    callDrawableHelper(mContext, v);
                    break;
                default:
                    break;
            }

        }

    }

    private void callDrawableHelper(Context context, ImageView imageView) {
        DrawableHelper
                .withContext(context)
                .withColor(R.color.more_icon_color_normal)
                .withDrawable(R.drawable.nav_live_tv)
                .tint()
                .applyTo(imageView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ApplicationItemBinding applicationItemBinding;

        private ViewHolder(ApplicationItemBinding view) {
            super(view.getRoot());
            applicationItemBinding = view;
            //moreItemBinding.mRoot.setOnClickListener(this);
            applicationItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PrintLogging.printLog(this.getClass(), "", "valuessClicked" + itemsList.get(getLayoutPosition()));

//                    if (itemsList.get(getLayoutPosition()).toString().equalsIgnoreCase(AppConstants.LIVE_TV)){
//                        itemClickListener.onClick(AppConstants.LIVE_TV);
//                    }else if (itemsList.get(getLayoutPosition()).toString().equalsIgnoreCase(AppConstants.TRENDING_TALENT)){
//                        itemClickListener.onClick(AppConstants.TRENDING_TALENT);
//                    }
//                    else if (itemsList.get(getLayoutPosition()).toString().equalsIgnoreCase(mContext.getResources().getString(R.string.logout))){
//                        itemClickListener.onClick(mContext.getResources().getString(R.string.logout));
//                    }

                }
            });
        }

    }

}
