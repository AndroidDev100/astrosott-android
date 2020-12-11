package com.astro.sott.utils.helpers.carousel.adapter;


import android.os.Parcelable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

public class PotraitLoopPagerAdapterWrapper extends PagerAdapter {

    private final PagerAdapter mAdapter;

    private SparseArray<ToDestroy> mToDestroy = new SparseArray<ToDestroy>();

    private boolean mBoundaryCaching;

    public PotraitLoopPagerAdapterWrapper(PagerAdapter adapter) {
        this.mAdapter = adapter;
        this.mAdapter.notifyDataSetChanged();
    }

    public void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
    }

    @Override
    public void notifyDataSetChanged() {
        mToDestroy = new SparseArray<ToDestroy>();
        super.notifyDataSetChanged();
    }

    public int toRealPosition(int position) {
       /* int realCount = position;
        if (realCount == 0)
            return 0;
        int realPosition = (position - 1) % realCount;
        if (realPosition < 0)
            realPosition += realCount;*/

        return position;
    }

    public int toInnerPosition(int realPosition) {
        return (realPosition + 1);
    }

    private int getRealFirstPosition() {
        return 1;
    }

    private int getRealLastPosition() {
        return getRealFirstPosition() + getRealCount();
    }

    @Override
    public int getCount() {
        return mAdapter.getCount();
        /*if (mAdapter.getCount()==1){
            return 1;
        }else{
            return mAdapter.getCount() + 2;
        }*/

    }

    public int getRealCount() {
        return mAdapter.getCount();
    }

    public PagerAdapter getRealAdapter() {
        return mAdapter;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = (mAdapter instanceof FragmentPagerAdapter || mAdapter instanceof FragmentStatePagerAdapter)
                ? position
                : toRealPosition(position);

        if (mBoundaryCaching) {
            ToDestroy toDestroy = mToDestroy.get(position);
            if (toDestroy != null) {
                mToDestroy.remove(position);
                return toDestroy.object;
            }
        }
        return mAdapter.instantiateItem(container, realPosition);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        int realFirst = getRealFirstPosition();
        int realLast = getRealLastPosition();
        int realPosition = (mAdapter instanceof FragmentPagerAdapter || mAdapter instanceof FragmentStatePagerAdapter)
                ? position
                : toRealPosition(position);

        if (mBoundaryCaching && (position == realFirst || position == realLast)) {
            mToDestroy.put(position, new ToDestroy(container, realPosition,
                    object));
        } else {
            mAdapter.destroyItem(container, realPosition, object);
        }
    }

    /*
     * Delegate rest of methods directly to the inner adapter.
     */

    @Override
    public void finishUpdate(ViewGroup container) {
        mAdapter.finishUpdate(container);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return mAdapter.isViewFromObject(view, object);
    }

    @Override
    public void restoreState(Parcelable bundle, ClassLoader classLoader) {
        mAdapter.restoreState(bundle, classLoader);
    }

    @Override
    public Parcelable saveState() {
        return mAdapter.saveState();
    }

    @Override
    public void startUpdate(ViewGroup container) {
        mAdapter.startUpdate(container);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mAdapter.setPrimaryItem(container, position, object);
    }

    /*
     * End delegation
     */

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /**
     * Container class for caching the boundary views
     */
    static class ToDestroy {
        final ViewGroup container;
        final int position;
        final Object object;

        ToDestroy(ViewGroup container, int position, Object object) {
            this.container = container;
            this.position = position;
            this.object = object;
        }
    }


}