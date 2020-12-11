package com.astro.sott.utils.helpers.carousel.indicators;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.astro.sott.utils.helpers.carousel.listener.OnSlideChangeListener;
import com.astro.sott.R;
import com.enveu.Enum.CRIndicator;


import java.util.ArrayList;
import java.util.List;


public class SlideIndicatorsPotrait extends LinearLayout implements OnSlideChangeListener {
    private final Context context;
    private int slidesCount;
    private Drawable selectedSlideIndicator;
    private Drawable unselectedSlideIndicator;
    private int defaultIndicator;
    private final int indicatorSize;
    private boolean mustAnimateIndicators = true;
    private final List<IndicatorShape> indicatorShapes = new ArrayList<>();
    private final String indicatorPosition;
    private boolean isTab;

    public SlideIndicatorsPotrait(Context context, Drawable selectedSlideIndicator, Drawable unselectedSlideIndicator, int defaultIndicator, int indicatorSize, boolean mustAnimateIndicators, String indicatorPosition, boolean type) {
        super(context);
        this.context = context;
        this.selectedSlideIndicator = selectedSlideIndicator;
        this.unselectedSlideIndicator = unselectedSlideIndicator;
        this.defaultIndicator = defaultIndicator;
        this.indicatorSize = indicatorSize;
        this.indicatorPosition = indicatorPosition;
        this.mustAnimateIndicators = mustAnimateIndicators;
        this.isTab = type;
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
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            setBackground(selectedSlideIndicator);
                        } else {
                            setBackgroundDrawable(selectedSlideIndicator);
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            setBackground(unselectedSlideIndicator);
                        } else {
                            setBackgroundDrawable(unselectedSlideIndicator);
                        }
                    }
                }
            };
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                indicatorShape.setBackground(unselectedSlideIndicator);
            } else {
                indicatorShape.setBackgroundDrawable(unselectedSlideIndicator);
            }
            indicatorShapes.add(indicatorShape);
            addView(indicatorShape);

        } else {
            switch (defaultIndicator) {
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
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, indicatorSize * 2);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        if (indicatorPosition.equalsIgnoreCase(CRIndicator.TOP.name())) {
            if (isTab)
                layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.indicator_potrait_bottom_mobile));
            else
                layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.indicator_potrait_bottom_mobile));
        } else {
            layoutParams.setMargins(0, 0, 0, 5);
        }

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
