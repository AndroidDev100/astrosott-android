package com.astro.sott.player.swipe;

import android.view.MotionEvent;

public interface SwipeListener {
    void onSwipingLeft(final MotionEvent event);

    boolean onSwipedLeft(final MotionEvent event);

    void onSwipingRight(final MotionEvent event);

    boolean onSwipedRight(final MotionEvent event);

    void onSwipingUp(final MotionEvent event);

    boolean onSwipedUp(final MotionEvent event);

    void onSwipingDown(final MotionEvent event);

    boolean onSwipedDown(final MotionEvent event);
}