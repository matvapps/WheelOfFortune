package com.github.matvapps.wheeloffortune;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;

import com.github.matvapps.wofview.R;

import java.util.ArrayList;
import java.util.Random;

public class CircleView extends FrameLayout {

    private final String TAG = CircleView.class.getSimpleName();

    private ArrayList<SectorView> sectors; // list of sectors
    private RotationListener rotationListener; // listener for animation actions
    private int[] fillColors; // color list
    private int rotateTime; // milliseconds
    private float rotateSpeed; // turnover per seconds
    private float previousRotationAngle; // previous rotation angle
    private final int FULL_ROTATE = 360; // whole turnover


    public CircleView(@NonNull Context context) {
        super(context);
        init();
    }

    public CircleView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {

        Log.d(TAG, "init: CircleView init");

        sectors = new ArrayList<SectorView>();
        fillColors = getResources().getIntArray(R.array.fortune_colors);
        previousRotationAngle = 0;
        rotateSpeed = 2;
        rotateTime = 2000;

        Log.d(TAG, "init: CircleView initialized");
    }

    /**
     * Start CircleView rotating
     */
    public void startRotateAnimation() {
        rotateSpeed += new Random().nextFloat();

        final RotateAnimation animRotate = new RotateAnimation(previousRotationAngle, getRotationAngle(rotateTime, rotateSpeed),
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setInterpolator(new FastOutSlowInInterpolator());

        animRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rotationListener.rotationStart();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                float rotationAngle = getRotationAngle(rotateTime, rotateSpeed);

                int sectorIndex = getChoosingSectorNum(getSectorsCount(), rotationAngle);
                rotationListener.rotationEnd(sectorIndex);
                previousRotationAngle = rotationAngle - getFullTurnoversFrom(rotationAngle) * FULL_ROTATE;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animRotate.setStartOffset(0);
        animRotate.setDuration(rotateTime);
        animRotate.setFillAfter(true);

        startAnimation(animRotate);
    }

    public void startRotateAnimation(float speed) {
        final RotateAnimation animRotate = new RotateAnimation(previousRotationAngle, getRotationAngle(rotateTime, speed),
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        animRotate.setInterpolator(new FastOutSlowInInterpolator());

        final float rotSpeed = speed;

        animRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                rotationListener.rotationStart();
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                float rotationAngle = getRotationAngle(rotateTime, rotSpeed);

                int sectorIndex = getChoosingSectorNum(getSectorsCount(), rotationAngle);
                rotationListener.rotationEnd(sectorIndex);
                previousRotationAngle = rotationAngle - getFullTurnoversFrom(rotationAngle) * FULL_ROTATE;

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animRotate.setStartOffset(0);
        animRotate.setDuration(rotateTime);
        animRotate.setFillAfter(true);

        startAnimation(animRotate);
    }


    /**
     * @param millis: time, that used for rotate animation
     * @param speed:  speed (turnover per second)
     * @return angle
     */
    private float getRotationAngle(int millis, float speed) {
        float time = millis / 1000;

        float distance = time * speed;

        return distance * FULL_ROTATE + previousRotationAngle;

    }

    private int getFullTurnoversFrom(float angle) {
        return (int) (angle / FULL_ROTATE);
    }

    /**
     * @param sectorCount:   count of sectors in view
     * @param rotationAngle: angle, used for rotating view
     * @return index of sector
     */
    private int getChoosingSectorNum(int sectorCount, float rotationAngle) {

        // sub all full turnovers for getting simple angle of rotating
        rotationAngle = rotationAngle - (getFullTurnoversFrom(rotationAngle) * FULL_ROTATE);

        float sectorAngle = FULL_ROTATE / sectorCount; // each of sectors angle
        float sectorAngleShift = FULL_ROTATE / sectorCount / 2; // start sector shift
        float angleFromSectorStart = rotationAngle + sectorAngleShift; // scrolled angle

        int sectorShift = (int) (angleFromSectorStart / sectorAngle); // the count of scrolled sectors


        if (sectorShift == 0) // if angle shift < angle sector
            return sectorShift;


        int selectedSectorNum = sectorCount - sectorShift; // number of selected sector

        Log.d(TAG, "getChoosingSectorNum: selected sector num = " + selectedSectorNum);

        return selectedSectorNum;
    }

    public void addSector(int fillColor, int strokeColor, float strokeSize, int imageID) {

        Log.d(TAG, "addSector: Start adding new sector...");

        int sectorCount = sectors.size() + 1;

        float sectorAngle = 360.0f / sectorCount;

        sectors.add(new SectorView(getContext(),
                sectorAngle,
                sectorAngle * sectorCount,
                fillColor,
                strokeColor,
                strokeSize,
                imageID));

        Log.d(TAG, "addSector: Sector added.");

        removeAllViews();

        Log.d(TAG, "addSector: Update view drawing...");

        for (int i = 0; i < sectors.size(); i++) {

            sectors.get(i).setAngle_sector(sectorAngle);
            sectors.get(i).setAngle_shift(sectorAngle * i);

            addView(sectors.get(i));

            Log.d(TAG, "\taddSector: Update data of sector[" + i + "]");

        }

        Log.d(TAG, "addSector: Update done.");

    }

    public void addSector(int imageID) {
        addSector(getFillColor(sectors.size()),
                ContextCompat.getColor(getContext(), R.color.colorDivider),
                2.0f,
                imageID);
    }

    /**
     * @param index: color index in fillColorArr
     * @return color by index
     */
    private int getFillColor(int index) {

        int color_index = index % (fillColors.length);
        return fillColors[color_index];
    }

    public void getStyleableAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // view already is a circle
        if (heightMeasureSpec > widthMeasureSpec) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }

    public int getRotateTime() {
        return rotateTime;
    }

    public void setRotateTime(int rotateTime) {
        this.rotateTime = rotateTime;
    }

    public float getRotateSpeed() {
        return rotateSpeed;
    }

    public void setRotateSpeed(float rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }

    public int[] getFillColors() {
        return fillColors;
    }

    public void setFillColors(int[] newFillColors) {
        this.fillColors = newFillColors;
    }

    /**
     * @return get list of SectorView's
     */
    public ArrayList<SectorView> getSectors() {
        return sectors;
    }

    /**
     * @param index: sector index in list
     * @return SectorView by index
     */
    public SectorView getSector(int index) {
        return sectors.get(index);
    }

    /**
     * @return count of sectors
     */
    public int getSectorsCount() {
        return sectors.size();
    }

    /**
     * @param rotationListener: set listener to rotating animation
     */
    public void setRotationListener(RotationListener rotationListener) {
        this.rotationListener = rotationListener;
    }

    protected interface RotationListener {

        void rotationStart();

        void rotationEnd(int sectorIndex);

    }


}
