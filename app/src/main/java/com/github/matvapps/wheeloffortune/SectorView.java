package com.github.matvapps.wheeloffortune;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.wheeloffortune.R;

public class SectorView extends View {

    private final String TAG = SectorView.class.getSimpleName();

    private int color_fill = Color.RED; // filling color
    private int color_stroke = Color.BLACK; // stroke color
    private float stroke_size = 3.0f; // stroke size
    private float angle_sector = 45.0f; // sector size
    private float angle_shift = 0f; // sector shift
    private Bitmap icon;
    private BitmapDrawable icon_drawable;

    private Rect bitmapImgSrc;
    private Rect bitmapImgDest;

    private RectF sectorRectDest;
    private int viewWidth;
    private int viewHeight;

    /**
     * @param context:      context
     * @param angle_sector: size of sector
     * @param angle_shift:  shifting(rotating) sector
     * @param color_fill:   color for filling sector
     * @param color_stroke: color for sector stroke
     * @param imageId:      inner icon id
     */
    public SectorView(Context context, float angle_sector, float angle_shift, int color_fill, int color_stroke, float stroke_size, int imageId) {
        super(context);

        this.angle_sector = angle_sector;
        this.color_fill = color_fill;
        this.icon_drawable = (BitmapDrawable) ContextCompat.getDrawable(context, imageId);
        this.angle_shift = angle_shift;
        this.color_stroke = color_stroke;
        this.stroke_size = stroke_size;

        init();

    }

    public SectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        getStyleableAttributes(context, attrs);
        init();
    }

    private void init() {
        sectorRectDest = new RectF();
        icon = icon_drawable.getBitmap();
        bitmapImgSrc = new Rect(0, 0, icon.getWidth(), icon.getWidth());
        bitmapImgDest = new Rect();
    }

    private void getStyleableAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SectorView);

        angle_sector = typedArray.getFloat(R.styleable.SectorView_sv_angle, angle_sector);
        color_fill = typedArray.getColor(R.styleable.SectorView_sv_color_fill, color_fill);
        icon_drawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.SectorView_sv_image);
        angle_shift = typedArray.getFloat(R.styleable.SectorView_sv_angle_shift, angle_shift);
        color_stroke = typedArray.getColor(R.styleable.SectorView_sv_color_stroke, color_stroke);
        stroke_size = typedArray.getFloat(R.styleable.SectorView_sv_stroke_size, stroke_size);

        typedArray.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Log.d(TAG, "onDraw: Start draw sector...");

        final int width = viewWidth;
        final int height = viewHeight;

        final int ox = width / 2; // center of view by horizontal
        final int oy = height / 2; // center of view by vertical

        int radius;
        if (width > height) {
            radius = (int) (height * 0.825f); // set radius to 82.5% of view height

        } else {
            radius = (int) (width * 0.825f); // set radius to 82.5% of view width
        }

        canvas.rotate(angle_shift, ox, oy); // rotate canvas by shift

        int imgSize = (int) ((radius * 0.28) - 10 * (360 / angle_sector)); // size of icon

        int left = ox - imgSize / 2; // image position left
        int top = (int) (oy / 2.5 - imgSize / 2); // image position top
        int right = ox + imgSize / 2; // image position right
        int bottom = (int) (oy / 2.5 + imgSize / 2); // image position bottom

        bitmapImgDest.set(left, top, right, bottom); // init image destination

        sectorRectDest.set(ox - radius / 2.0f, oy - radius / 2.0f,
                ox + radius / 2.0f, oy + radius / 2.0f); // init sector destination

        canvas.drawArc(sectorRectDest,
                -90 - angle_sector / 2, angle_sector,
                true,
                getFillPaint(color_fill)); // draw sector

        canvas.drawArc(sectorRectDest,
                -90 - angle_sector / 2,
                angle_sector,
                true,
                getStrokePaint(color_stroke, stroke_size)); // draw sector stroke

        canvas.drawBitmap(icon,
                bitmapImgSrc,
                bitmapImgDest,
                getFillPaint(color_fill)); // draw icon

        Log.d(TAG, "onDraw: Created sector: " +
                "\n\tSectorAngle=" + angle_sector +
                "\n\tSectorShift=" + angle_shift +
                "\n\tSectorColor=" + color_fill +
                "\n\tStroke color/size=" + color_stroke + "/" + stroke_size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // view already is a circle
        if (heightMeasureSpec > widthMeasureSpec)
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        else {
            super.onMeasure(heightMeasureSpec, heightMeasureSpec);
        }
    }

    private Paint getFillPaint(int color) {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);

        return paint;
    }

    private Paint getStrokePaint(int color, float size) {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);
        paint.setStrokeWidth(size);

        return paint;
    }

    public float getAngle_shift() {
        return angle_shift;
    }

    public void setAngle_shift(float angle_shift) {
        this.angle_shift = angle_shift;
    }

    public float getAngle_sector() {
        return angle_sector;
    }

    public void setAngle_sector(float angle_sector) {
        this.angle_sector = angle_sector;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}
