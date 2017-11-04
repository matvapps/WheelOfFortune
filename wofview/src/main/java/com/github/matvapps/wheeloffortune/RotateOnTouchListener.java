package com.github.matvapps.wheeloffortune;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class RotateOnTouchListener implements View.OnTouchListener {

    private final String TAG = RotateOnTouchListener.class.getSimpleName();
    private final int INVALID_POINTER_ID = -1;

    public static final int HORIZONTAL_SWIPE = 100;
    public static final int VERTICAL_SWIPE = 101;

    private boolean ROTATE;
    private int activePointerID = INVALID_POINTER_ID;


    private float downTouchX;
    private float downTouchY;
    private float upTouchX;
    private float upTouchY;
    private long swipeTime;
    private float swipeSpeed;
    private float swipeLength;

    private SwipeActionListener swipeActionListener;

    public RotateOnTouchListener(SwipeActionListener swipeActionListener) {
        this.swipeActionListener = swipeActionListener;
    }

    public SwipeActionListener getSwipeActionListener() {
        return swipeActionListener;
    }

    public void setSwipeActionListener(SwipeActionListener swipeActionListener) {
        this.swipeActionListener = swipeActionListener;
    }

    public boolean isROTATE() {
        return ROTATE;
    }

    public void setROTATE(boolean ROTATE) {
        this.ROTATE = ROTATE;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                float x = 0;
                float y = 0;

                boolean success = false;

                activePointerID = event.getPointerId(0);

                try {
                    x = event.getX(activePointerID);
                    y = event.getY(activePointerID);

                    success = true;
                } catch (IllegalArgumentException ex) {
                    Log.w(TAG, "Exception in onTouch(view, event): ", ex);
                }

                if (success) {
                    // Remember we started
                    downTouchX = x;
                    downTouchY = y;

                    Log.d(TAG, "onTouch: downTouchX =" + downTouchX);
                    Log.d(TAG, "onTouch: downTouchY =" + downTouchY);

                    swipeTime = System.currentTimeMillis();

                }

                break;
            }
            case MotionEvent.ACTION_UP: {
                float x = 0;
                float y = 0;

                boolean success = false;

                try {
                    x = event.getX(activePointerID);
                    y = event.getY(activePointerID);

                    success = true;
                } catch (IllegalArgumentException ex) {
                    Log.w(TAG, "Exception in onTouch(view, event): ", ex);
                }

                if (success) {
                    upTouchX = x;
                    upTouchY = y;

                    Log.d(TAG, "onTouch: upTouchX =" + upTouchX);
                    Log.d(TAG, "onTouch: upTouchX =" + upTouchY);

                    swipeTime = System.currentTimeMillis() - swipeTime;
                    swipeLength = vectorLength(downTouchX, downTouchY, upTouchX, upTouchY);
                    swipeSpeed = swipeLength / swipeTime;

                    Log.d(TAG, "onTouch: swipeTime = " + swipeTime);
                    Log.d(TAG, "onTouch: swipeLength = " + swipeLength);
                    Log.d(TAG, "onTouch: swipeSpeed = " + swipeSpeed);

                    swipeActionListener.swipeEnd(swipeSpeed);

                }

            }
        }


        return true;
    }

    private float vectorLength(float x_start, float y_start, float x_end, float y_end) {
        return (float) Math.sqrt(Math.pow(x_end - x_start, 2) + Math.pow(y_end - y_start, 2));
    }

    private float getSpeedBy(long time, float length) {
        float speed;

        speed = length / time;

        return speed;
    }

    protected interface SwipeActionListener {

        void swipeStart(float x_start, float y_start);

        void swipeEnd(float swipeSpeed);

    }
}
