package com.astro.sott.fragments.detailRailFragment.adapter;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.astro.sott.beanModel.SponsoredTabData;
import com.astro.sott.fragments.episodeFrament.EpisodesFragment;
import com.astro.sott.fragments.sponsoredFragment.SponsoredTabFragment;
import com.astro.sott.utils.helpers.AppLevelConstants;
import com.astro.sott.utils.helpers.LiveTvViewPager;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SponsoredPagerAdapter  extends FragmentStatePagerAdapter {
    List<SponsoredTabData> sponsoredTabDataList;
    private int mCurrentPosition = -1;

    public SponsoredPagerAdapter(@NonNull @NotNull FragmentManager fm, List<SponsoredTabData> sponsoredTabData) {
        super(fm);
        this.sponsoredTabDataList=sponsoredTabData;
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

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        SponsoredTabFragment sponsoredTabFragment = new SponsoredTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString("COLLECTION_ID",sponsoredTabDataList.get(position).getCollectionId());
        sponsoredTabFragment.setArguments(bundle);
        return sponsoredTabFragment;
    }

    @Override
    public int getCount() {
        return sponsoredTabDataList.size();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return sponsoredTabDataList.get(position).getTitle();
    }
}
