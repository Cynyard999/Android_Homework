package com.bytedance.clockapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Clock extends View {

    private final static String TAG = "CLOCK";

    private static final int FULL_ANGLE = 360;

    private static final int CUSTOM_ALPHA = 140;

    private static final int FULL_ALPHA = 255;

    private int clockZone = 0;

    private static final int DEFAULT_PRIMARY_COLOR = Color.WHITE;

    private static final int DEFAULT_SECONDARY_COLOR = Color.LTGRAY;

    private static final float DEFAULT_DEGREE_STROKE_WIDTH = 0.010f;

    public final static int AM = 0;

    private static final int RIGHT_ANGLE = 90;

    private float PANEL_RADIUS = 200.0f;// 表盘半径

    private float HOUR_POINTER_LENGTH;// 指针长度
    
    private float MINUTE_POINTER_LENGTH;
    private float SECOND_POINTER_LENGTH;
    private float UNIT_DEGREE = (float) (6 * Math.PI / 180);// 一个小格的度数

    private int mWidth, mCenterX, mCenterY, mRadius;

    private int degreesColor;

    private Paint mNeedlePaint;

    //因为从正右边开始绘制
    private String[] hoursValues = {"3", "2", "1", "12", "11", "10", "9", "8", "7", "6", "5", "4"};

    public Clock(Context context,int clockZone) {
        super(context);
        this.clockZone = clockZone;
        init(context, null);
    }

    public Clock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Clock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();
        if (widthWithoutPadding > heightWithoutPadding) {
            size = heightWithoutPadding;
        } else {
            size = widthWithoutPadding;
        }
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(),
            size + getPaddingTop() + getPaddingBottom());
    }

    private void init(Context context, AttributeSet attrs) {
        this.degreesColor = DEFAULT_PRIMARY_COLOR;
        mNeedlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNeedlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mNeedlePaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getHeight() > getWidth() ? getWidth() : getHeight();
        int halfWidth = mWidth / 2;
        mCenterX = halfWidth;
        mCenterY = halfWidth;
        mRadius = halfWidth;
        PANEL_RADIUS = mRadius;
        HOUR_POINTER_LENGTH = PANEL_RADIUS - 400;
        MINUTE_POINTER_LENGTH = PANEL_RADIUS - 250;
        SECOND_POINTER_LENGTH = PANEL_RADIUS - 150;
        drawDegrees(canvas);
        drawHoursValues(canvas);
        drawNeedles(canvas);
        drawTimeBelow(canvas);
        //Log.d(TAG," "+System.currentTimeMillis());
        //试了很多方法 貌似只有这个会好一点点，浮动在1000上下，但是也会在某个时刻猛增
        postInvalidateDelayed(990);
    }

    private void drawDegrees(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH);
        paint.setColor(degreesColor);

        int rPadded = mCenterX - (int) (mWidth * 0.01f);
        int rEnd = mCenterX - (int) (mWidth * 0.05f);

        for (int i = 0; i < FULL_ANGLE; i += 6 /* Step */) {

            if ((i % RIGHT_ANGLE) != 0 && (i % 15) != 0) {
                paint.setAlpha(CUSTOM_ALPHA);
            } else {
                paint.setAlpha(FULL_ALPHA);
            }

            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));

            int stopX = (int) (mCenterX + rEnd * Math.cos(Math.toRadians(i)));
            int stopY = (int) (mCenterX - rEnd * Math.sin(Math.toRadians(i)));

            canvas.drawLine(startX, startY, stopX, stopY, paint);

        }
    }

    /**
     * Draw Hour Text Values, such as 1 2 3 ...
     *
     * @param canvas
     */
    private void drawHoursValues(Canvas canvas) {
        // Default Color:
        // - hoursValuesColor
        TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(degreesColor);
        textPaint.setTextSize(60f);
        textPaint.setStyle(TextPaint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        int rPadded = mCenterX - (int) (mWidth * 0.1f);
        for (int i = 0; i < FULL_ANGLE; i += 30) {
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float top = fontMetrics.top;
            float bottom = fontMetrics.bottom;
            int startX = (int) (mCenterX + rPadded * Math.cos(Math.toRadians(i)));
            int startY = (int) (mCenterX - rPadded * Math.sin(Math.toRadians(i)));
            int baseLine = (int) (startY - top / 2 - bottom / 2);
            canvas.drawText(hoursValues[i / 30], startX, baseLine, textPaint);
        }
    }

    /**
     * Draw hours, minutes needles Draw progress that indicates hours needle disposition.
     *
     * @param canvas
     */
    private void drawNeedles(final Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        int nowHours = now.getHours();
        int nowMinutes = now.getMinutes();
        int nowSeconds = now.getSeconds();
        // 画秒针
        drawPointer(canvas, 2, nowSeconds);
        // 画分针
        drawPointer(canvas, 1, nowMinutes);
        // 画时针,一小时五个小格
        int part = nowMinutes / 12;
        drawPointer(canvas, 0, 5 * nowHours + part);
    }

    private void drawTimeBelow(Canvas canvas) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mWidth * 0.1f);
        textPaint.setColor(Color.WHITE);
        textPaint.setAlpha(CUSTOM_ALPHA + 20);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Align.CENTER);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int amPm = calendar.get(Calendar.AM_PM);

        String time = String.format("%s:%s %s",
            String.format(Locale.getDefault(), "%d", hour),
            String.format(Locale.getDefault(), "%02d", minute),
            amPm == AM ? "AM" : "PM");
        canvas.drawText(time, mCenterX, mCenterY + mRadius * 0.5f, textPaint);
    }


    private void drawPointer(Canvas canvas, int pointerType, int value) {

        float degree;
        float[] pointerHeadXY = new float[2];
        float[] pointerReverseHeadXY = new float[2];
        switch (pointerType) {
            case 0:
                mNeedlePaint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH * 0.85f);
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.WHITE);
                pointerHeadXY = getPointerHeadXY(HOUR_POINTER_LENGTH, degree);
                pointerReverseHeadXY = getReversePointerHeadXY(HOUR_POINTER_LENGTH, degree);
                break;
            case 1:
                // todo 画分针，设置分针的颜色
                mNeedlePaint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH * 0.7f);
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.WHITE);
                pointerHeadXY = getPointerHeadXY(MINUTE_POINTER_LENGTH, degree);
                pointerReverseHeadXY = getReversePointerHeadXY(MINUTE_POINTER_LENGTH, degree);
                break;
            case 2:
                mNeedlePaint.setStrokeWidth(mWidth * DEFAULT_DEGREE_STROKE_WIDTH * 0.5f);
                degree = value * UNIT_DEGREE;
                mNeedlePaint.setColor(Color.WHITE);
                pointerHeadXY = getPointerHeadXY(SECOND_POINTER_LENGTH, degree);
                pointerReverseHeadXY = getReversePointerHeadXY(SECOND_POINTER_LENGTH, degree);
                break;
        }
        canvas.drawLine(mCenterX, mCenterY, pointerHeadXY[0], pointerHeadXY[1], mNeedlePaint);
        canvas.drawLine(mCenterX, mCenterY, pointerReverseHeadXY[0], pointerReverseHeadXY[1],
            mNeedlePaint);
    }

    private float[] getPointerHeadXY(float pointerLength, float degree) {
        float[] xy = new float[2];
        xy[0] = (float) (mCenterX + pointerLength * Math.sin(degree));
        xy[1] = (float) (mCenterY - pointerLength * Math.cos(degree));
        return xy;
    }

    //计算needle的尾巴坐标
    private float[] getReversePointerHeadXY(float pointerLength, float degree) {
        float[] xy = new float[2];
        xy[0] = (float) (mCenterX + 0.1f * pointerLength * Math.sin(degree - Math.PI));
        xy[1] = (float) (mCenterY - 0.1f * pointerLength * Math.cos(degree - Math.PI));
        return xy;
    }


}