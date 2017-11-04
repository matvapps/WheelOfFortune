package com.github.matvapps.wheeloffortune;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.wheeloffortune.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WOFView extends FrameLayout {

    private String TAG = WOFView.class.getSimpleName();

    // Circle View for display and animate sectors
    private CircleView circleView;
    // Image for display under circleView
    private ImageView backgroundImage;
    // Image for display over circleView
    private ImageView outerImage;

    // id of outerImage
    private int outerImageID = R.drawable.fortune_golden_outer;
    // id of backgroundImage
    private int backgroundImageID = R.drawable.fortune_back;
    // id of marker
    private int markerImageId = R.drawable.fortune_marker;

    private int viewWidth;
    // destination of image marker
    private Rect bitmapImgDest;

    public WOFView(Context context) {
        super(context);
        init();
    }

    public WOFView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getStyleableAttributes(context, attrs);
        init();
    }

    /**
     * Draw all element's of view at screen
     */
    private void draw() {

        Log.d(TAG, "draw: \t draw elements...");

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;

        backgroundImage.setLayoutParams(layoutParams);
        circleView.setLayoutParams(layoutParams);
        outerImage.setLayoutParams(layoutParams);

        Picasso.with(getContext()).load(backgroundImageID).into(backgroundImage);
        Picasso.with(getContext()).load(outerImageID).into(outerImage);

        addView(backgroundImage);
        addView(circleView);
        addView(outerImage);

        Log.d(TAG, "draw: \t draw done.");
    }

    /**
     * Init all of the view elements
     */
    private void init() {

        Log.d(TAG, "init: WOFView init...");

        setWillNotDraw(false);
        backgroundImage = new ImageView(getContext());
        circleView = new CircleView(getContext());
        outerImage = new ImageView(getContext());

        bitmapImgDest = new Rect();

        if (isInEditMode()) {
            for (int i = 0; i < 7; i++) {
                addSector(R.mipmap.ic_launcher);
            }
        }

        RotateOnTouchListener touchListener = new RotateOnTouchListener(
                new RotateOnTouchListener.SwipeActionListener() {
                    @Override
                    public void swipeStart(float x_start, float y_start) {

                    }

                    @Override
                    public void swipeEnd(float swipeSpeed) {
                        startRotate(swipeSpeed);
                    }
                });

        setOnTouchListener(touchListener);

        draw();

        Log.d(TAG, "init: WOFView initialized.");

    }

    private void getStyleableAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WOFView);

        backgroundImageID = typedArray.getResourceId(R.styleable.WOFView_wof_background, backgroundImageID);
        outerImageID = typedArray.getResourceId(R.styleable.WOFView_wof_outer, outerImageID);
        markerImageId = typedArray.getResourceId(R.styleable.WOFView_wof_marker, markerImageId);

        typedArray.recycle();

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        Log.d(TAG, "dispatchDraw: Start marker icon drawing...");

        BitmapDrawable icon_drawable = (BitmapDrawable) ContextCompat.getDrawable(getContext(), markerImageId);
        Bitmap markerIcon = icon_drawable.getBitmap();
        Rect bitmapImgSrc = new Rect(0, 0, markerIcon.getWidth(), markerIcon.getWidth());

        int markerSize = (int) (viewWidth / 2 * 0.42f);

        int posX = viewWidth / 2;
        int posY = markerSize / 2;

        int left = posX - markerSize / 2;
        int top = posY - markerSize / 2;
        int right = posX + markerSize / 2;
        int bottom = posY + markerSize / 2;

        bitmapImgDest.set(left, top, right, bottom);

        canvas.drawBitmap(markerIcon, bitmapImgSrc, bitmapImgDest, getFillPaint(Color.BLACK));

        Log.d(TAG, "dispatchDraw: Marker drawing done.");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (heightMeasureSpec > widthMeasureSpec) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * start rotate animation from circleView
     */
    public void startRotate() {
        circleView.startRotateAnimation();
    }

    public void startRotate(float speed) {
        circleView.startRotateAnimation(speed);
    }

    /**
     * @param fillColor:   sector color filling
     * @param strokeColor: sector color stroke
     * @param strokeSize:  sector color size
     * @param imageID:     sector image
     */
    public void addSector(int fillColor, int strokeColor, float strokeSize, int imageID) {
        circleView.addSector(fillColor, strokeColor, strokeSize, imageID);
    }

    /**
     * Add new sector to circleView
     *
     * @param imageID: sector image
     */
    public void addSector(int imageID) {
        circleView.addSector(imageID);
    }

    public ArrayList<SectorView> getSectorList() {
        return circleView.getSectors();
    }

    public int getRotateTime() {
        return circleView.getRotateTime();
    }

    /**
     * @param rotateTime: Rotating time for animation (millisecond)
     */
    public void setRotateTime(int rotateTime) {
        circleView.setRotateTime(rotateTime);
    }

    public float getRotateSpeed() {
        return circleView.getRotateSpeed();
    }

    /**
     * @param rotateSpeed: Rotating speed for animation (turnover per second)
     */
    public void setRotateSpeed(float rotateSpeed) {
        circleView.setRotateSpeed(rotateSpeed);
    }

    public int getMarkerImageId() {
        return markerImageId;
    }

    /**
     * @param markerImageId: marker image id
     */
    public void setMarkerImageId(int markerImageId) {
        this.markerImageId = markerImageId;
    }

    public int getOuterImageID() {
        return outerImageID;
    }

    /**
     * @param outerImageID: outer image id
     */
    public void setOuterImageID(int outerImageID) {
        this.outerImageID = outerImageID;
        outerImage.setImageResource(outerImageID);
    }

    public int getBackgroundImageID() {
        return backgroundImageID;
    }

    /**
     * @param backgroundImageID: background image id
     */
    public void setBackgroundImageID(int backgroundImageID) {
        this.backgroundImageID = backgroundImageID;
        backgroundImage.setImageResource(backgroundImageID);
    }

    /**
     * @param fillColors: array of colors for displaying circleView
     */
    public void setFillColors(int[] fillColors) {
        circleView.setFillColors(fillColors);
    }

    public int[] getFillColors() {
        return circleView.getFillColors();
    }

    /**
     * @param rotationListener: listener for rotation actions
     */
    public void setRotationListener(CircleView.RotationListener rotationListener) {
        circleView.setRotationListener(rotationListener);
    }

    private Paint getFillPaint(int color) {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);

        return paint;
    }

}
