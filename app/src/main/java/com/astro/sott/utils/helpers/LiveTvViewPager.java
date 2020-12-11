package com.astro.sott.utils.helpers;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class LiveTvViewPager extends ViewPager {

    private Boolean disable = false;
    private int[] heightArr = new int[3];  // allocating memory to array
    private int mCurrentPagePosition = 0;

    /**
     * Constructor
     *
     * @param context the context
     */
    public LiveTvViewPager(Context context) {
        super(context);
    }

    /**
     * Constructor
     *
     * @param context the context
     * @param attrs   the attribute set
     */
    public LiveTvViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return !disable && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !disable && super.onTouchEvent(event);
    }

    public void disableScroll(Boolean disable) {
        //When disable = true not work the scroll and when disble = false work the scroll
        this.disable = disable;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        try {

            int mode = MeasureSpec.getMode(heightMeasureSpec);
            if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
                // super has to be called in the beginning so the child views can be initialized.
                // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                int height = 0;
                int numOfElements = getChildCount();
                for (int i = 0; i < numOfElements; i++) {
                    View child = getChildAt(i);
                    child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    int h = child.getMeasuredHeight();
                    height = h;
                    //if (h > height) height = h;
                    heightArr[i] = height;
                }
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightArr[mCurrentPagePosition], MeasureSpec.EXACTLY);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }catch (Exception e){
            Log.d("ExceptionIs",e.getMessage());
        }
    }

    public void reMeasureCurrentPage(int position) {
        mCurrentPagePosition = position;
        requestLayout();
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @param view        the base view with already measured height
     * @return The height of the view, honoring constraints from measureSpec
     */
    private int measureHeight(int measureSpec, View view) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            // set the height from the base view if available
            if (view != null) {
                result = view.getMeasuredHeight();
            }
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

}
