package com.astro.sott.utils.helpers.carousel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.astro.sott.utils.helpers.carousel.indicators.SlideIndicatorsGroup;
import com.astro.sott.utils.helpers.carousel.model.Slide;
import com.astro.sott.R;
import com.astro.sott.beanModel.VIUChannel;
import com.astro.sott.utils.commonMethods.AppCommonMethods;
import com.astro.sott.utils.constants.AppConstants;
import com.astro.sott.utils.helpers.carousel.adapter.SliderAdapter;
import com.astro.sott.utils.helpers.carousel.indicators.IndicatorShape;
import com.enveu.Enum.CRIndicator;

import java.util.ArrayList;
import java.util.Random;

public class Slider extends FrameLayout implements ViewPager.OnPageChangeListener {

    private static final String TAG = "SLIDER";
    private LooperWrapViewPager viewPager;
    private AdapterView.OnItemClickListener itemClickListener;
    private Drawable selectedSlideIndicator;
    private Drawable unSelectedSlideIndicator;
    private int defaultIndicator;
    private int indicatorSize;
    private boolean mustAnimateIndicators;
    private boolean mustLoopSlides;
    private SlideIndicatorsGroup slideIndicatorsGroup;
    private int slideShowInterval = 3000;
    private final Handler handler = new Handler();
    private int slideCount;
    private int currentPageNumber;
    private boolean hideIndicators = false;

    public Slider(@NonNull Context context) {
        super(context);
    }

    AttributeSet attrset;
    public Slider(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        attrset=attrs;
    }

    public Slider(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attrset=attrs;
    }

    private void parseCustomAttributes(AttributeSet attributeSet) {
        try {
            if (attributeSet != null) {
                TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.BannerSlider);
                try {
                    indicatorSize = typedArray.getDimensionPixelSize(R.styleable.BannerSlider_indicatorSize, getResources().getDimensionPixelSize(R.dimen.indicator_size));
                    selectedSlideIndicator = typedArray.getDrawable(R.styleable.BannerSlider_selected_slideIndicator);
                    unSelectedSlideIndicator = typedArray.getDrawable(R.styleable.BannerSlider_unselected_slideIndicator);
                    defaultIndicator = typedArray.getInt(R.styleable.BannerSlider_defaultIndicators, IndicatorShape.CIRCLE);
                    mustAnimateIndicators = typedArray.getBoolean(R.styleable.BannerSlider_animateIndicators, true);
                    mustLoopSlides = typedArray.getBoolean(R.styleable.BannerSlider_loopSlides, true);
                    hideIndicators = typedArray.getBoolean(R.styleable.BannerSlider_hideIndicators, false);
                    int slideShowIntervalSecond = typedArray.getInt(R.styleable.BannerSlider_intervalSecond, 5);
                    slideShowInterval = slideShowIntervalSecond * 1000;

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

    boolean autoRotate=true;
    int autoRotateDuration=0;
    public void addSlides(ArrayList<Slide> slideList, int type, VIUChannel indicators, int position,boolean auto,int rotateDuration) {
        parseCustomAttributes(attrset);
        if (slideList == null || slideList.size() == 0)
            return;

        viewPager = new LooperWrapViewPager(getContext());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            viewPager.setId(View.generateViewId());
        } else {
            int id = Math.abs(new Random().nextInt((5000 - 1000) + 1) + 1000);
            viewPager.setId(id);
        }
        viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewPager.addOnPageChangeListener(Slider.this);
        addView(viewPager);
        SliderAdapter adapter = new SliderAdapter(getContext(), type, slideList, (adapterView, view, i, l) -> itemClickListener.onItemClick(adapterView, view, i, l), position);


        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        slideCount = slideList.size();
        viewPager.setCurrentItem(0);
        viewPager.setClipToPadding(false);
        int mMargin = indicatorSize * 3;


        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        autoRotate=auto;
        autoRotateDuration=rotateDuration;
        if (tabletSize) {
            viewPager.setPadding(getViewPagerPadding(type), 5, getViewPagerPadding(type), 10);
        } else {
            /*if (slideList.get(0).getType() == CAROUSEL_PR_POTRAIT) {
                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                //if you need three fix imageview in width
                int mWidth = ((displaymetrics.widthPixels - 80) * 10) / 15;
                viewPager.setPadding(mWidth / 2, 0, mWidth / 2, 0);
            } else*/
            viewPager.setPadding(0, 10, 0, 10);
        }
        viewPager.setPageMargin(20);

        if (indicators.getContentIndicator().equalsIgnoreCase(CRIndicator.HDN.name())) {
            hideIndicators = true;
        }

        if (!hideIndicators && slideCount > 1) {
            slideIndicatorsGroup = new SlideIndicatorsGroup(getContext(), selectedSlideIndicator, unSelectedSlideIndicator, defaultIndicator, indicatorSize, mustAnimateIndicators, indicators.getContentIndicator(), slideList.get(0).getType());
            addView(slideIndicatorsGroup);
            slideIndicatorsGroup.setSlides(slideCount);
            slideIndicatorsGroup.onSlideChange(0);
            slideIndicatorsGroup.setBackgroundColor(getContext().getResources().getColor(R.color.transparentColor));
        }

        if (slideCount > 1){
            if (autoRotate){
                setupTimer();
            }
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPageNumber = position;
        if (slideIndicatorsGroup != null && !hideIndicators) {
            if (position == 0) {
                slideIndicatorsGroup.onSlideChange(0);
            } else if (position == slideCount) {
                slideIndicatorsGroup.onSlideChange(slideCount);
            } else {
                slideIndicatorsGroup.onSlideChange(position);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                handler.removeCallbacksAndMessages(null);
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                if (autoRotate){
                    setupTimer();
                }

                break;
        }
    }

    private int getViewPagerPadding(int layoutType) {
        switch (layoutType) {
            case AppConstants
                    .CAROUSEL_LDS_LANDSCAPE:
                return (int) getResources().getDimension(R.dimen.carousal_landscape_padding);
            case AppConstants
                    .CAROUSEL_PR_POTRAIT:
                return (int) getResources().getDimension(R.dimen.carousal_potrait_padding);
            case AppConstants
                    .CAROUSEL_SQR_SQUARE:
                return (int) getResources().getDimension(R.dimen.carousal_square_padding);
            case AppConstants.CAROUSEL_CST_CUSTOM:
                return AppCommonMethods.convertDpToPixel(40);
            default:
                return 0;
        }
    }


    private void setupTimer() {
        try {
            if (mustLoopSlides) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (currentPageNumber <= slideCount)
                                currentPageNumber += 1;
                            else
                                currentPageNumber = 0;


                            if (currentPageNumber == 0) {
                                viewPager.setCurrentItem(currentPageNumber - 1, false);

                            } else {
                                viewPager.setCurrentItem(currentPageNumber - 1, true);

                            }


                            handler.removeCallbacksAndMessages(null);
                            handler.postDelayed(this, slideShowInterval);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, slideShowInterval);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // setters
    public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setHideIndicators(boolean hideIndicators) {
        this.hideIndicators = hideIndicators;
        try {
            if (hideIndicators)
                slideIndicatorsGroup.setVisibility(INVISIBLE);
            else
                slideIndicatorsGroup.setVisibility(VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}