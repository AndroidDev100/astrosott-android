package com.astro.sott.activities.liveChannel.adapter;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.astro.sott.beanModel.ksBeanmodel.RailCommonData;
import com.astro.sott.fragments.nowPlaying.NowPlaying;
import com.astro.sott.fragments.schedule.ui.Schedule;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.R;

public class LiveChannelPagerAdapter extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    //Constructor to the class
    private final RailCommonData railCommonData;
    private final Context mContext;

    public LiveChannelPagerAdapter(Context context,
                                   FragmentManager fm, RailCommonData railCommonData) {
        super(fm);
        //Initializing tab count
        this.mContext = context;
        this.railCommonData = railCommonData;

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            NowPlaying nowPlaying = new NowPlaying();
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railCommonData);
            nowPlaying.setArguments(bundle);
            return nowPlaying;
        } else {
            Schedule schedule = new Schedule();
            schedule.setUserVisibleHint(false);
            Bundle bundle = new Bundle();
            bundle.putParcelable(AppLevelConstants.RAIL_DATA_OBJECT, railCommonData);
            schedule.setArguments(bundle);
            return schedule;
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.now_playing);
            case 1:
                return mContext.getString(R.string.schedule);
            default:
                return null;
        }
    }
}