package com.skystudio.myapplication;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 喇叭视图   存在两种状态  播放状态play（）   默认静止状态也可通过stop()
 * Created by CreaterXv on 2017/4/10.
 */

public class HornView extends View {
    private int mWidth = 100;
    private int mHeight = 100;//默认为50像素

    private Paint mPaint;

    private RectF rectF;


    private Path path;

    private boolean isPlaying = false;

    private int i=4;

    public HornView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        rectF = new RectF(0, 0, mWidth, mHeight);
        path = new Path();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w, h;
        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);
        final int wMode = MeasureSpec.getMode(widthMeasureSpec);
        final int hMode = MeasureSpec.getMode(heightMeasureSpec);
        if (wMode == MeasureSpec.EXACTLY && hMode == MeasureSpec.EXACTLY) {
            mWidth = w;
            mHeight = h;
        }
        rectF.set(-mWidth, -mHeight / 2, mWidth, mHeight + mHeight / 2);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.RED);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        if (!isPlaying) {
            rectF.set(-mWidth + mWidth / 4, (-mHeight / 2) + mWidth / 4, mWidth - mWidth / 4, mHeight + mHeight / 2 - mHeight / 4);
            path.addArc(rectF, -30, 60);

            rectF.set(-mWidth + mWidth / 2, (-mHeight / 2) + mWidth / 2, mWidth - mWidth / 2, mHeight + mHeight / 2 - mHeight / 2);
            path.addArc(rectF, -30, 60);

            rectF.set(-mWidth + mWidth / 4 * 3, (-mHeight / 2) + mWidth / 4 * 3, mWidth - mWidth / 4 * 3, mHeight + mHeight / 2 - mHeight / 4 * 3);
            path.addArc(rectF, -30, 60);
        }else {
            if(i==4){
                path.reset();
            }
            else if (i==3) {
                path.reset();
                rectF.set(-mWidth + mWidth / 4 * 3, (-mHeight / 2) + mWidth / 4 * 3, mWidth - mWidth / 4 * 3, mHeight + mHeight / 2 - mHeight / 4 * 3);
                path.addArc(rectF, -30, 60);
            }else if(i==2){
                path.reset();
                rectF.set(-mWidth + mWidth / 2, (-mHeight / 2) + mWidth / 2, mWidth - mWidth / 2, mHeight + mHeight / 2 - mHeight / 2);
                path.addArc(rectF, -30, 60);

                rectF.set(-mWidth + mWidth / 4 * 3, (-mHeight / 2) + mWidth / 4 * 3, mWidth - mWidth / 4 * 3, mHeight + mHeight / 2 - mHeight / 4 * 3);
                path.addArc(rectF, -30, 60);
            }else if (i==1){
                path.reset();
                rectF.set(-mWidth + mWidth / 4, (-mHeight / 2) + mWidth / 4, mWidth - mWidth / 4, mHeight + mHeight / 2 - mHeight / 4);
                path.addArc(rectF, -30, 60);

                rectF.set(-mWidth + mWidth / 2, (-mHeight / 2) + mWidth / 2, mWidth - mWidth / 2, mHeight + mHeight / 2 - mHeight / 2);
                path.addArc(rectF, -30, 60);

                rectF.set(-mWidth + mWidth / 4 * 3, (-mHeight / 2) + mWidth / 4 * 3, mWidth - mWidth / 4 * 3, mHeight + mHeight / 2 - mHeight / 4 * 3);
                path.addArc(rectF, -30, 60);
            }
        }

        canvas.drawPath(path, mPaint);
    }


    public void setI(int i){
        this.i=i;
        invalidate();
    }


    private  ObjectAnimator animator;
    /**
     * 播放状态
     *
     * */
    public void play() {
        isPlaying=true;
        animator=ObjectAnimator.ofInt(this,"i",4,1);
        animator.setDuration(3000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();

    }
    /**
     * 停止播放
     *
     * */
    public void stop(){
        isPlaying=false;
        if (animator!=null){
            animator.cancel();
            animator=null;
        }
        invalidate();
    }
}
