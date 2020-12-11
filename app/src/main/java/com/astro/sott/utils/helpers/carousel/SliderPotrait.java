package com.astro.sott.utils.helpers.carousel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.astro.sott.R;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.carousel.adapter.SliderAdapter;
import com.astro.sott.utils.helpers.carousel.indicators.IndicatorShape;
import com.astro.sott.utils.helpers.carousel.indicators.SlideIndicatorsPotrait;
import com.astro.sott.utils.helpers.carousel.model.Slide;
import com.enveu.Enum.CRIndicator;


import java.util.ArrayList;
import java.util.Random;


public class SliderPotrait extends RelativeLayout implements ViewPager.OnPageChangeListener/*, ViewPager.PageTransformer*/ {

    public static final String TAG = "SLIDER";
    private PortraitLooperWrapViewPager viewPager;
    private AdapterView.OnItemClickListener itemClickListener;


    private Drawable selectedSlideIndicator;
    private Drawable unSelectedSlideIndicator;
    private int defaultIndicator;
    private int indicatorSize;
    private boolean mustAnimateIndicators;
    private boolean mustLoopSlides;
    private SlideIndicatorsPotrait slideIndicatorsGroup;
    private int slideShowInterval = 4000;

    private final Handler handler = new Handler();
    private int slideCount;
    private int currentPageNumber;
    private boolean hideIndicators = false;
    private int maxTranslateOffsetX;

    public SliderPotrait(@NonNull Context context) {
        super(context);
    }

    public SliderPotrait(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parseCustomAttributes(attrs);
    }

    public SliderPotrait(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseCustomAttributes(attrs);
    }

    private void parseCustomAttributes(AttributeSet attributeSet) {
        try {
            if (attributeSet != null) {
                TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.BannerSlider);
                try {
                    indicatorSize = (int) typedArray.getDimension(R.styleable.BannerSlider_indicatorSize, getResources().getDimensionPixelSize(R.dimen.default_indicator_size));
                    selectedSlideIndicator = typedArray.getDrawable(R.styleable.BannerSlider_selected_slideIndicator);
                    unSelectedSlideIndicator = typedArray.getDrawable(R.styleable.BannerSlider_unselected_slideIndicator);
                    defaultIndicator = typedArray.getInt(R.styleable.BannerSlider_defaultIndicators, IndicatorShape.CIRCLE);
                    mustAnimateIndicators = typedArray.getBoolean(R.styleable.BannerSlider_animateIndicators, true);
                    mustLoopSlides = typedArray.getBoolean(R.styleable.BannerSlider_loopSlides, false);
                    hideIndicators = typedArray.getBoolean(R.styleable.BannerSlider_hideIndicators, false);
                    int slideShowIntervalSecond = typedArray.getInt(R.styleable.BannerSlider_intervalSecond, 3);
                    slideShowInterval = slideShowIntervalSecond * 1000;
                    this.maxTranslateOffsetX = dp2px(getContext(), 180);
                } catch (Exception e) {

                    e.printStackTrace();
                } finally {
                    typedArray.recycle();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    SliderAdapter adapter;
    public void addSlides(ArrayList<Slide> slideList, int type, VIUChannel indicators, int position, int width) {
        if (slideList == null || slideList.size() == 0)
            return;

        viewPager = new PortraitLooperWrapViewPager(getContext());
        viewPager.setBoundaryCaching(true);
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            viewPager.setId(View.generateViewId());
        } else {
            int id = Math.abs(new Random().nextInt((5000 - 1000) + 1) + 1000);
            viewPager.setId(id);
        }
        viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewPager.addOnPageChangeListener(SliderPotrait.this);
        addView(viewPager);
        adapter = new SliderAdapter(getContext(), type, slideList, (adapterView, view, i, l) -> itemClickListener.onItemClick(adapterView, view, i, l), position);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        adapter.notifyDataSetChanged();
        slideCount = slideList.size();
        viewPager.setCurrentItem(1);
        viewPager.setClipToPadding(false);
        int mMargin = indicatorSize * 3;

        int paddingVal = -1;

        if (tabletSize) {
            if (width > 0) {
                paddingVal = (width * 80) / 200;
            }
            if (paddingVal > 0)
                viewPager.setPadding(paddingVal, 5, paddingVal, 8 + AppConstants.INDICATOR_BOTTOM);
            viewPager.setPageMargin(15);
        } else {

            if (width > 0) {
                paddingVal = (width * 45) / 200;
            }
            if (paddingVal > 0)
                viewPager.setPadding(paddingVal, 5, paddingVal, 5 + AppConstants.INDICATOR_BOTTOM);
            viewPager.setPageMargin(AppCommonMethods.convertDpToPixel((int) (width * 2.5) / 100));
            // viewPager.setPageTransformer(true, this);

        }


        if (indicators.getContentIndicator().equalsIgnoreCase(CRIndicator.HDN.name())) {
            hideIndicators = true;
        }

        //hideIndicators = false;
        if (!hideIndicators && slideCount > 1) {
            slideIndicatorsGroup = new SlideIndicatorsPotrait(getContext(), selectedSlideIndicator, unSelectedSlideIndicator, defaultIndicator, indicatorSize, mustAnimateIndicators, indicators.getContentIndicator(), tabletSize);
            LayoutParams params = new LayoutParams(
                    slideIndicatorsGroup.getLayoutParams());
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            addView(slideIndicatorsGroup);
            slideIndicatorsGroup.setSlides(slideCount);
            slideIndicatorsGroup.onSlideChange(1);
            slideIndicatorsGroup.setBackgroundColor(getContext().getResources().getColor(R.color.transparentColor));
        }


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }



    @Override
    public void onPageSelected(int position) {
        currentPageNumber = position;
        if (slideIndicatorsGroup != null && !hideIndicators) {
            slideIndicatorsGroup.onSlideChange(position);
            /*if (position == 0) {
                slideIndicatorsGroup.onSlideChange(position);
            } else if (position == slideCount) {
                slideIndicatorsGroup.onSlideChange(0);
            } else {
                slideIndicatorsGroup.onSlideChange(position);
            }*/
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                handler.removeCallbacks(runnable);
                break;
            case ViewPager.SCROLL_STATE_SETTLING:
                handler.removeCallbacks(runnable);
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                //setupTimer2();
                //setupTimer();
                break;
        }
    }

    Runnable runnable;
    private void setupTimer() {
        try {


            handler.postDelayed(runnable, slideShowInterval);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // setters
    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }



    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;
    /*  @Override
      public void transformPage(@NonNull View view, float position) {
          if (viewPager == null) {
              return;
          }

         // handler.removeCallbacks(runnable);
        *//*  int pageWidth = view.getWidth();

        if (MAX_SCALE == 0.0f && position > 0.0f && position < 1.0f) {
            MAX_SCALE = position;
        }
        position = position - MAX_SCALE;
        float absolutePosition = Math.abs(position);
        if (position <= -1.0f || position >= 1.0f) {
            // Page is not visible -- stop any running animations

        } else if (position == 0.0f) {

            // Page is selected -- reset any views if necessary
            view.setScaleX((1 + MAX_SCALE));
            view.setScaleY((1 + MAX_SCALE));
            view.setAlpha(1);
        } else {
            view.setScaleX(1 + MAX_SCALE * (1 - absolutePosition));
            view.setScaleY(1 + MAX_SCALE * (1 - absolutePosition));
        }
*//*

     *//*  int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0f);

        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Scale the page down (between MIN_SCALE and 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Fade the page relative to its size.
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0f);
        }*//*
       if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.

        } else { // [-1,1]

            // Modify the default slide transition to shrink the page as well
            int leftInScreen = view.getLeft() - viewPager.getScrollX();
            int centerXInViewPager = leftInScreen + view.getMeasuredWidth() / 2;
            int offsetX = centerXInViewPager - viewPager.getMeasuredWidth() / 2;
            float offsetRate = (float) offsetX * 0.38f / viewPager.getMeasuredWidth();
            float scaleFactor = 1 - Math.abs(offsetRate);

            if (scaleFactor > 0) {
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setTranslationX(-maxTranslateOffsetX * offsetRate);
                //ViewCompat.setElevation(view, 0.0f);
            }
            currentPageNumber=viewPager.getCurrentItem();
            ViewCompat.setElevation(view, scaleFactor);
            viewPager.setBoundaryCaching(false);

        }

        *//*if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
        } else if (position < 0) { // [-1,0)
            // Left visible page fraction
        } else if (position == 0) {
            // One page centered

        } else if (position > 0 && position <= 1) { // (0, 1]
            // Right visible page fraction
        } else { // (1, Infinity]
            // This page is way off-screen to the right.
        }*//*


    }
*/
    private int dp2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5f);
    }


}