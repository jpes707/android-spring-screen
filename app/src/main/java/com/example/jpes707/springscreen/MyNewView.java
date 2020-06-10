package com.example.jpes707.springscreen;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class MyNewView extends View {
    private final int FLOWER_COUNT = 50;
    private final int SUN_LINE_COUNT = 12;

    private int width;
    private int height;
    private int radius;
    private float additional_angle;
    private int[] colors, x_values, y_values;

    /* ------------------------*/
    /*    member variables     */

    private Paint mPaint;
    /* ------------------------*/
    /*    constructor          */

    public MyNewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        radius = 0;
        additional_angle = 0;
        setupPaint();

        Display screensize = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        @SuppressLint("DrawAllocation") Point size = new Point();
        screensize.getSize(size);
        width = size.x;
        height = size.y;

        setWillNotDraw(false);
        PropertyValuesHolder propertyRadius = PropertyValuesHolder.ofInt("radius", 175, 225);
        @SuppressLint("DrawAllocation") ValueAnimator animator = new ValueAnimator();
        animator.setValues(propertyRadius);
        animator.setDuration(5000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radius = (int) animation.getAnimatedValue("radius");
                invalidate();
            }
        });
        if (animator.isRunning()) {
            animator.end();
        }
        animator.start();
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);

        int[] color_choices = new int[]{Color.RED, Color.BLUE, Color.YELLOW, Color.MAGENTA, android.graphics.Color.rgb(255, 165, 0), android.graphics.Color.rgb(128, 0, 128)};
        colors = new int[FLOWER_COUNT];
        x_values = new int[FLOWER_COUNT];
        y_values = new int[FLOWER_COUNT];
        for(int k = 0; k < FLOWER_COUNT; k++) {
            colors[k] = color_choices[(int)(Math.random() * color_choices.length)];
            x_values[k] = (int)(Math.random() * (width - 25) + 25);
            y_values[k] = height - (int)(Math.random() * (height * 0.25) + 30);
        }
    }

    /* --------------------------------*/
    /*    protected draw operation     */

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(android.graphics.Color.rgb(135, 206, 235));
        mPaint.setColor(Color.YELLOW);
        canvas.drawCircle((float)width/2, (float)width/2, radius, mPaint);
        for (int k = 0; k < SUN_LINE_COUNT; k++) {
            drawSunLine(canvas, k);
        }
        additional_angle += 2 * Math.PI / 503;
        mPaint.setColor(android.graphics.Color.rgb(96, 128, 56));
        canvas.drawRect(0, (int) (height - (height * 0.25)), width, height, mPaint);
        for (int k = 0; k < FLOWER_COUNT; k++) {
            drawFlower(canvas, k);
        }
    }

    /* --------------------------------*/
    /*    custom operations methods    */

    private void setupPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.YELLOW);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(80);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
    }

    private void drawFlower(Canvas canvas, int index) {
        int x = x_values[index];
        int y = y_values[index];
        mPaint.setColor(Color.GREEN);
        canvas.drawRect(x-3, y, x+3, y+30, mPaint);
        mPaint.setColor(colors[index]);
        canvas.drawCircle(x, y, 10, mPaint);
    }

    private void drawSunLine(Canvas canvas, int count) {
        double angle = (double)count / SUN_LINE_COUNT * 2 * Math.PI + additional_angle;
        int bottom_r = radius + 20;
        int top_r = radius + 110 + (225 - radius);

        float x1 = bottom_r * (float)Math.cos(angle) + (float)width/2;
        float x2 = top_r * (float)Math.cos(angle) + (float)width/2;
        float y1 = bottom_r * (float)Math.sin(angle) + (float)width/2;
        float y2 = top_r * (float)Math.sin(angle) + (float)width/2;

        canvas.drawLine(x1, y1, x2, y2, mPaint);
    }
}