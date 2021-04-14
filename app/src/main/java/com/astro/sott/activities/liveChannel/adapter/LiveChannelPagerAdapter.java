package com.astro.sott.activities.liveChannel.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.astro.sott.baseModel.RecommendationRailFragment;
import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.fragments.nowPlaying.NowPlaying;
import com.astro.sott.fragments.schedule.ui.Schedule;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.R;
import com.astro.sott.utils.helpers.LiveTvViewPager;

public class LiveChannelPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    private int mCurrentPosition = -1;

    //Constructor to the class
    private final RailCommonData railCommonData;
    private final Context mContext;
    private int adapterSize;

    public LiveChannelPagerAdapter(Context context,
                                   FragmentManager fm, RailCommonData railCommonData, int adapterSize) {
        super(fm);
        //Initializing tab count
        this.mContext = context;
        this.railCommonData = railCommonData;
        this.adapterSize = adapterSize;

    }

    @Override
    public Fragment getItem(int position) {
        if (adapterSize == 2) {
            if (position == 1) {
                RecommendationRailFragment recommendationRailFragment = new RecommendationRailFragment();
                Bundle args = new Bundle();
                args.putInt("BUNDLE_TAB_ID", 0);
                args.putInt("MEDIA_TYPE", railCommonData.getObject().getType());
                args.putInt("LAYOUT_TYPE", AppLevelConstants.Rail5);
                args.putParcelable("ASSET_OBJ", railCommonData.getObject());
                recommendationRailFragment.setArguments(args);
                return recommendationRailFragment;
            } else {
                Schedule schedule = new Schedule();
                schedule.setUserVisibleHint(true);
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railCommonData);
                schedule.setArguments(bundle);
                return schedule;
            }
        } else {
            Schedule schedule = new Schedule();
            schedule.setUserVisibleHint(false);
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railCommonData);
            schedule.setArguments(bundle);
            return schedule;
        }
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);

        if (position != mCurrentPosition && container instanceof LiveTvViewPager) {
            Fragment fragment = (Fragment) object;
            LiveTvViewPager pager = (LiveTvViewPager) container;

            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return adapterSize;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        if (adapterSize == 2) {
            switch (position) {
                case 1:
                    return mContext.getString(R.string.related);
                case 0:
                    return mContext.getString(R.string.schedule);
                default:
                    return null;
            }
        } else {
            return mContext.getString(R.string.schedule);

        }
    }
}