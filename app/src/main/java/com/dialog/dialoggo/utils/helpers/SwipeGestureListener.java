package com.dialog.dialoggo.utils.helpers;

import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.dialog.dialoggo.callBacks.DoubleClickListener;
import com.dialog.dialoggo.callBacks.DragListner;
import com.google.android.material.bottomsheet.BottomSheetBehavior;


public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener implements View.OnClickListener{

    BottomSheetBehavior bottomSheetBehavior;
    DragListner dragListner;
    public SwipeGestureListener(BottomSheetBehavior bottomSheetBehavior, DragListner dragListner) {
       // this(doubleClickListener, 200L);
       this.bottomSheetBehavior=bottomSheetBehavior;
       this.dragListner = dragListner;

        DOUBLE_CLICK_INTERVAL = 200L; // default time to wait the second click.
    }

    public SwipeGestureListener(final long DOUBLE_CLICK_INTERVAL) {
       // this.doubleClickListener = doubleClickListener;
        this.DOUBLE_CLICK_INTERVAL = DOUBLE_CLICK_INTERVAL; // developer specified time to wait the second click.
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        // Grab two events located on the plane at e1=(x1, y1) and e2=(x2, y2)
        // Let e1 be the initial event
        // e2 can be located at 4 different positions, consider the following diagram
        // (Assume that lines are separated by 90 degrees.)
        //
        //
        //         \ A  /
        //          \  /
        //       D   e1   B
        //          /  \
        //         / C  \
        //
        // So if (x2,y2) falls in region:
        //  A => it's an UP swipe
        //  B => it's a RIGHT swipe
        //  C => it's a DOWN swipe
        //  D => it's a LEFT swipe
        //

        float x1 = e1.getX();
        float y1 = e1.getY();

        float x2 = e2.getX();
        float y2 = e2.getY();

        Direction direction = getDirection(x1,y1,x2,y2);
        return onSwipe(direction);
    }

    /** Override this method. The Direction enum will tell you how the user swiped. */
    public boolean onSwipe(Direction direction){
        Log.d("DirectionNameIs",direction.name());

        if (direction.name().equalsIgnoreCase(Direction.up.name())){
            dragListner.dragging(direction.name());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }else if (direction.name().equalsIgnoreCase(Direction.down.name())){
            dragListner.dragging(direction.name());
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        return false;
    }

    /**
     * Given two points in the plane p1=(x1, x2) and p2=(y1, y1), this method
     * returns the direction that an arrow pointing from p1 to p2 would have.
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the direction
     */
    public Direction getDirection(float x1, float y1, float x2, float y2){
        double angle = getAngle(x1, y1, x2, y2);

        PrintLogging.printLog("","directionValue--->>"+x1+""+y1+""+x2+""+y2);

        return Direction.fromAngle(angle);
    }

    /**
     *
     * Finds the angle between two points in the plane (x1,y1) and (x2, y2)
     * The angle is measured with 0/360 being the X-axis to the right, angles
     * increase counter clockwise.
     *
     * @param x1 the x position of the first point
     * @param y1 the y position of the first point
     * @param x2 the x position of the second point
     * @param y2 the y position of the second point
     * @return the angle between two points
     */
    public double getAngle(float x1, float y1, float x2, float y2) {

        double rad = Math.atan2(y1-y2,x2-x1) + Math.PI;
        return (rad*180/Math.PI + 180)%360;
    }


    public enum Direction{
        up,
        down,
        left,
        right;

        /**
         * Returns a direction given an angle.
         * Directions are defined as follows:
         *
         * Up: [45, 135]
         * Right: [0,45] and [315, 360]
         * Down: [225, 315]
         * Left: [135, 225]
         *
         * @param angle an angle from 0 to 360 - e
         * @return the direction of an angle
         */
        public static Direction fromAngle(double angle){
            if(inRange(angle, 45, 135)){
                return Direction.up;
            }
            else if(inRange(angle, 0,45) || inRange(angle, 315, 360)){
                return Direction.right;
            }
            else if(inRange(angle, 225, 315)){
                return Direction.down;
            }
            else{
                return Direction.left;
            }

        }

        /**
         * @param angle an angle
         * @param init the initial bound
         * @param end the final bound
         * @return returns true if the given angle is in the interval [init, end).
         */
        private static boolean inRange(double angle, float init, float end){
            return (angle >= init) && (angle < end);
        }
    }


    /*
     * Duration of click interval.
     * 200 milliseconds is a best fit to double click interval.
     */
    private long DOUBLE_CLICK_INTERVAL;  // Time to wait the second click.

    /*
     * Handler to process click event.
     */
    private final Handler mHandler = new Handler();

    /*
     * Click callback.
     */
    //private final DoubleClickListener doubleClickListener;

    /*
     * Number of clicks in @DOUBLE_CLICK_INTERVAL interval.
     */
    private int clicks;

    /*
     * Flag to check if click handler is busy.
     */
    private boolean isBusy = false;



    @Override
    public void onClick(final View view) {

        if (!isBusy) {
            //  Prevent multiple click in this short time
            isBusy = true;

            // Increase clicks count
            clicks++;

            mHandler.postDelayed(new Runnable() {
                public final void run() {

                    if (clicks >= 2) {  // Double tap.
                       // doubleClickListener.onDoubleClick(view);
                    }

                    if (clicks == 1) {  // Single tap
                       // doubleClickListener.onSingleClick(view);
                    }

                    // we need to  restore clicks count
                    clicks = 0;
                }
            }, DOUBLE_CLICK_INTERVAL);
            isBusy = false;
        }

    }

}