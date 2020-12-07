package com.dialog.dialoggo.fragments.detailRailFragment.adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dialog.dialoggo.R;
import com.dialog.dialoggo.beanModel.ksBeanmodel.RailCommonData;
import com.dialog.dialoggo.fragments.moreLikeThisFragment.MoreLikeThisFragment;
import com.dialog.dialoggo.fragments.trailerFragment.TrailerFragment;
import com.dialog.dialoggo.utils.helpers.AppLevelConstants;

public class DetailPagerAdapter extends FragmentStatePagerAdapter {
    private final Context mContext;
    private RailCommonData railData;
    private int adapterSize;

    public DetailPagerAdapter(FragmentManager fm, Context mContext, RailCommonData railCommonData, int size) {
        super(fm);
        this.mContext = mContext;
        this.railData = railCommonData;
        adapterSize = size;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            MoreLikeThisFragment moreLikeThisFragment = new MoreLikeThisFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railData);
            moreLikeThisFragment.setArguments(bundle);
            return moreLikeThisFragment;
        } else {
            TrailerFragment trailerFragment = new TrailerFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railData);
            trailerFragment.setArguments(bundle);
            return trailerFragment;
        }
    }

    @Override
    public int getCount() {
        return adapterSize;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return mContext.getString(R.string.more_like_this);
            case 1:
                return mContext.getString(R.string.trailer_title);
            default:
                return null;
        }
    }
}
