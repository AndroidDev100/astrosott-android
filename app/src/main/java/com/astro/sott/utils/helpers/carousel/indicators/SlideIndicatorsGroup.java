package com.astro.sott.utils.helpers.carousel.indicators;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.astro.sott.R;
import com.astro.sott.utils.helpers.carousel.listener.OnSlideChangeListener;

import java.util.ArrayList;
import java.util.List;


/**
 * PRATIK RAO
 */

public class SlideIndicatorsGroup extends LinearLayout implements OnSlideChangeListener {
    private final Context context;
    private int slidesCount;
    private Drawable selectedSlideIndicator;
    private Drawable unselectedSlideIndicator;
    private int defaultIndicator;
    private final int indicatorSize;
    private boolean mustAnimateIndicators;
    private final List<IndicatorShape> indicatorShapes = new ArrayList<>();

    public SlideIndicatorsGroup(Context context, Drawable selectedSlideIndicator, Drawable unselectedSlideIndicator, int defaultIndicator, int indicatorSize, boolean mustAnimateIndicators, String indicatorPosition,int type) {
        super(context);
        this.context = context;
        this.selectedSlideIndicator = selectedSlideIndicator;
        this.unselectedSlideIndicator = unselectedSlideIndicator;
        this.defaultIndicator = defaultIndicator;
        this.indicatorSize = indicatorSize;
        this.mustAnimateIndicators = mustAnimateIndicators;
        setup();
    }

    public void setSlides(int slidesCount) {
        removeAllViews();
        indicatorShapes.clear();
        this.slidesCount = 0;
        for (int i = 0; i < slidesCount; i++) {
            onSlideAdd();
        }
        this.slidesCount = slidesCount;
    }

    private void onSlideAdd() {
        this.slidesCount += 1;
        addIndicator();
    }

    private void addIndicator() {
        IndicatorShape indicatorShape;
        if (selectedSlideIndicator != null && unselectedSlideIndicator != null) {
            indicatorShape = new IndicatorShape(context, indicatorSize, mustAnimateIndicators) {
                @Override
                public void onCheckedChange(boolean isChecked) {
                    super.onCheckedChange(isChecked);
                    if (isChecked) {
                        setBackground(selectedSlideIndicator);
                    } else {
                        setBackground(unselectedSlideIndicator);
                    }
                }
            };
            indicatorShape.setBackground(unselectedSlideIndicator);
            indicatorShapes.add(indicatorShape);
            addView(indicatorShape);

        } else {
            switch (defaultIndicator) {
               /* case IndicatorShape.SQUARE:
                    indicatorShape = new SquareIndicator(context, indicatorSize, mustAnimateIndicators);
                    indicatorShapes.add(indicatorShape);
                    addView(indicatorShape);
                    break;
                case IndicatorShape.ROUND_SQUARE:
                    indicatorShape = new RoundSquareIndicator(context, indicatorSize, mustAnimateIndicators);
                    indicatorShapes.add(indicatorShape);
                    addView(indicatorShape);
                    break;
                case IndicatorShape.DASH:
                    indicatorShape = new DashIndicator(context, indicatorSize, mustAnimateIndicators);
                    indicatorShapes.add(indicatorShape);
                    addView(indicatorShape);
                    break;*/

                case IndicatorShape.CIRCLE:
                    indicatorShape = new CircleIndicator(context, indicatorSize, mustAnimateIndicators);
                    indicatorShapes.add(indicatorShape);
                    addView(indicatorShape);
                    break;
                default:
                    break;
            }
        }
    }

    private void setup() {
        setOrientation(LinearLayout.HORIZONTAL);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, indicatorSize * 2);
        layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER;

        int margin = getResources().getDimensionPixelSize(R.dimen.default_indicator_margins) ;
        layoutParams.setMargins(0, margin, 0, 0);
        setLayoutParams(layoutParams);
    }

    @Override
    public void onSlideChange(int selectedSlidePosition) {
        for (int i = 0; i < indicatorShapes.size(); i++) {
            if (i == selectedSlidePosition) {
                indicatorShapes.get(i).onCheckedChange(true);
            } else {
                indicatorShapes.get(i).onCheckedChange(false);
            }
        }
    }

    public void changeIndicator(int defaultIndicator) {
        this.defaultIndicator = defaultIndicator;
        selectedSlideIndicator = null;
        unselectedSlideIndicator = null;
        setSlides(slidesCount);
    }

    public void changeIndicator(Drawable selectedSlideIndicator, Drawable unselectedSlideIndicator) {
        this.selectedSlideIndicator = selectedSlideIndicator;
        this.unselectedSlideIndicator = unselectedSlideIndicator;
        setSlides(slidesCount);
    }

    public void setMustAnimateIndicators(boolean mustAnimateIndicators) {
        this.mustAnimateIndicators = mustAnimateIndicators;
        for (IndicatorShape indicatorShape :
                indicatorShapes) {
            indicatorShape.setMustAnimateChange(mustAnimateIndicators);
        }
    }
}
