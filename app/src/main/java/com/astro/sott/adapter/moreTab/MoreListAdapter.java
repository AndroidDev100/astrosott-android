package com.astro.sott.adapter.moreTab;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.astro.sott.utils.helpers.DrawableHelper;
import com.astro.sott.utils.ksPreferenceKey.KsPreferenceKey;
import com.astro.sott.R;
import com.astro.sott.databinding.MoreItemBinding;

import java.util.List;

public class MoreListAdapter extends RecyclerView.Adapter<MoreListAdapter.ViewHolder> {
    private final List<String> itemsList;
    private final Activity mContext;

    public MoreListAdapter(Activity context, List<String> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        MoreItemBinding moreItemBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.more_item, viewGroup, false);
        return new MoreListAdapter.ViewHolder(moreItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.moreItemBinding.moreListTitle.setText(itemsList.get(i));
        setIcons(holder.moreItemBinding.moreListIcon, i);

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    private void setIcons(ImageView v, int i) {
        if (KsPreferenceKey.getInstance(mContext).getUserActive()) {
            if (itemsList.size() == mContext.getResources().getStringArray(R.array.more_list).length) {
                switch (i) {
//                case 0:
//                    callDrawableHelper(mContext, R.color.more_icon_color_normal, R.drawable.nav_live_tv, v);
//                    break;
                    case 0:
                        callDrawableHelper(mContext, R.drawable.playlist_icon, v);
                        break;
                    case 1:
                        callDrawableHelper(mContext,  R.drawable.acc_settings, v);

                        break;
                    case 2:
                        callDrawableHelper(mContext,  R.drawable.app_setting, v);
                        break;


                    case 3:
                        callDrawableHelper(mContext,  R.drawable.devie_mang_icon, v);

                        break;

                    case 4:
                        callDrawableHelper(mContext,  R.drawable.term_cond_icon, v);
                        break;

                    case 5:
                        callDrawableHelper(mContext,  R.drawable.help, v);
                        break;

                    case 6:
                        callDrawableHelper(mContext,  R.drawable.logout, v);

                        break;
                    default:

                        break;
                }
            }
        } else {
            if (itemsList.size() == mContext.getResources().getStringArray(R.array.more_list_with_login).length) {
                switch (i) {
//                case 0:
//                    callDrawableHelper(mContext,  R.drawable.nav_live_tv, v);
//                    break;
                    case 0:
                        callDrawableHelper(mContext, R.drawable.playlist_icon, v);
                        break;
                    case 1:
                        callDrawableHelper(mContext, R.drawable.acc_settings, v);

                        break;
                    case 2:
                        callDrawableHelper(mContext, R.drawable.app_setting, v);
                        break;


                    case 3:
                        callDrawableHelper(mContext, R.drawable.devie_mang_icon, v);

                        break;

                    case 4:
                        callDrawableHelper(mContext, R.drawable.term_cond_icon, v);
                        break;

                    case 5:
                        callDrawableHelper(mContext, R.drawable.help, v);
                        break;

                    default:

                        break;
                }
            }
        }


    }

    private void callDrawableHelper(Context context, int mDrawable, ImageView imageView) {
        DrawableHelper
                .withContext(context)
                .withColor(R.color.more_icon_color_normal)
                .withDrawable(mDrawable)
                .tint()
                .applyTo(imageView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final MoreItemBinding moreItemBinding;

        private ViewHolder(@NonNull MoreItemBinding itemView) {
            super(itemView.getRoot());
            this.moreItemBinding = itemView;
        }
    }
}
