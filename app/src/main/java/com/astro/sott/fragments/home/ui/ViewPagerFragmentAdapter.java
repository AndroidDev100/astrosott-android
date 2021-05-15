package com.astro.sott.fragments.home.ui;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.astro.sott.fragments.homenewtab.ui.HomeTabNew;
import com.astro.sott.fragments.livetv.ui.LiveTvFragment;
import com.astro.sott.fragments.sports.ui.SportsFragment;
import com.astro.sott.fragments.video.ui.VideoFragment;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {
    CharSequence Titles[];
    Fragment tvFragment, movieFragment, sportFragment, tabNew;

    public ViewPagerFragmentAdapter(@NonNull AppCompatActivity fragmentActivity, CharSequence mTitles[]) {
        super(fragmentActivity);
        this.Titles = mTitles;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                HomeTabNew homeTabNew = new HomeTabNew();
                tabNew = homeTabNew;
                return homeTabNew;
            case 1:
                HomeFragment homeFragment = new HomeFragment();
                tvFragment = homeFragment;
                return homeFragment;
            case 2:
                VideoFragment videoFragment = new VideoFragment();
                movieFragment = videoFragment;
                return videoFragment;
            case 3:
                SportsFragment sportsFragment = new SportsFragment();
                sportFragment = sportsFragment;
                return sportsFragment;
        }
        HomeFragment homeFragment = new HomeFragment();
        tvFragment = homeFragment;
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return Titles.length;
    }

    public Fragment getFragment(int pos) {
        if (pos == 0) {
            return tabNew;
        } else if (pos == 1) {
            return tvFragment;
        } else if (pos == 2) {
            return movieFragment;
        } else if (pos == 3) {
            return sportFragment;
        }
        return tvFragment;
    }

}