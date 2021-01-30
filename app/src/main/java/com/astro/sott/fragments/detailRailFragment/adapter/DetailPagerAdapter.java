package com.astro.sott.fragments.detailRailFragment.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.astro.sott.fragments.episodeFrament.EpisodesFragment;
import com.astro.sott.fragments.trailerFragment.TrailerFragment;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.R;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.fragments.moreLikeThisFragment.MoreLikeThisFragment;
import com.astro.sott.utils.helpers.MediaTypeConstant;

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

            if (railData.getObject().getType() == MediaTypeConstant.getSeries(mContext)) {
                EpisodesFragment episodesFragment = new EpisodesFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("ASSET_OBJ", railData.getObject());
                bundle.putInt("LAYOUT_TYPE", AppConstants.Rail5);
                bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railData);
                episodesFragment.setArguments(bundle);
                return episodesFragment;
            } else {
                TrailerFragment trailerFragment = new TrailerFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railData);
                trailerFragment.setArguments(bundle);
                return trailerFragment;
            }

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
                return mContext.getString(R.string.more_like_this);//mContext.getString(R.string.more_like_this);
            case 1:
                if (railData.getObject().getType() == MediaTypeConstant.getSeries(mContext)) {
                    return "Episodes";

                }else {
                    return mContext.getString(R.string.trailer_title);

                }
            default:
                return null;
        }
    }
}
