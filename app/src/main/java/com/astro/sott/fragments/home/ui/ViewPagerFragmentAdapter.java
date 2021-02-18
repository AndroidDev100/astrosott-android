package com.astro.sott.fragments.home.ui;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.astro.sott.fragments.livetv.ui.LiveTvFragment;
import com.astro.sott.fragments.video.ui.VideoFragment;

public class ViewPagerFragmentAdapter extends FragmentStateAdapter {
    CharSequence Titles[];
    public ViewPagerFragmentAdapter(@NonNull AppCompatActivity fragmentActivity, CharSequence mTitles[]) {
        super(fragmentActivity);
        this.Titles = mTitles;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new VideoFragment();
            case 2:
                return new LiveTvFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return Titles.length;
    }
}