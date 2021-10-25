package com.astro.sott.fragments.detailRailFragment.adapter;

import android.os.Bundle;

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

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SponsoredPagerAdapter  extends FragmentStatePagerAdapter {
    List<SponsoredTabData> sponsoredTabDataList;
    public SponsoredPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior,List<SponsoredTabData> sponsoredTabData) {
        super(fm, behavior);
        this.sponsoredTabDataList=sponsoredTabData;
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
