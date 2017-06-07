package com.example.administrator.myapplication;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


/**
 * 动态的表格chartview
 *
 * Created by Administrator on 2017/6/6.
 */

public class ChartView extends View {
    private int mDefHeight = 200;
    private int mDefwidth = 10;
    private int mHeight;
    private int mWidth;

    private int mRectWidth;
    private int mRectHeight;
    private int marginLeft;
    private int marginRight;
    private int marginTop;
    private int marginButtom;

    private Paint mPaint;
    private Paint mTextPaint;
    private int mColor = Color.BLACK;

    private float mPercent = 0;
    private float allPercent = 0;

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mTextPaint = new TextPaint();
        TypedArray ty=context.getTheme().obtainStyledAttributes(attrs,R.styleable.ChartView,defStyleAttr,0);
        allPercent=ty.getFloat(R.styleable.ChartView_percent,0.5f);
        ty.recycle();
        startAnimator();
    }

    private void startAnimator() {
        int endColor;
        if (allPercent>0.6f){
            endColor=Color.RED;
        }else {
            endColor=0XFF40FFC6;
        }
        ValueAnimator colorAnimator;
        if (Build.VERSION.SDK_INT >= 21)
            colorAnimator = ValueAnimator.ofArgb(Color.GREEN, endColor);
        else colorAnimator = ValueAnimator.ofInt(Color.GREEN,endColor);
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.RESTART);
        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mColor = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator highAnimator = ValueAnimator.ofFloat(0, allPercent);
        highAnimator.setRepeatCount(ValueAnimator.INFINITE);
        highAnimator.setRepeatMode(ValueAnimator.RESTART);
        highAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPercent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(3000);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.playTogether(colorAnimator, highAnimator);
        animSet.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = getDefaultSize(mDefwidth, widthMeasureSpec);
        mHeight = getDefaultSize(mDefHeight, heightMeasureSpec);
        mRectWidth = mWidth / 3 * 2;
        marginLeft = marginRight = mWidth / 6;
        mRectHeight = mHeight / 8 * 7;//矩形总高度的八分之七
        marginTop = mHeight / 8;
        marginButtom = 0;
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 获取默认宽高
     **/
    public static int getDefaultSize(int size, int measureSpec) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        textView.setText("75%");
//        textView.setTextColor(Color.RED);
//        textView.draw(canvas);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        if (Build.VERSION.SDK_INT >= 21)
            canvas.drawRoundRect(marginLeft, marginTop + (mRectHeight - mPercent * mRectHeight), marginLeft + mRectWidth, marginTop + mRectHeight, 5, 5, mPaint);
        else {
            canvas.drawRoundRect(new RectF(marginLeft, marginTop + (mRectHeight - mPercent * mRectHeight), marginLeft + mRectWidth, marginTop + mRectHeight), 5, 5, mPaint);
        }
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);
        int p = (int) (mPercent * 100);
        canvas.drawText(p + "%", mWidth / 2, marginTop + (mRectHeight - mPercent * mRectHeight) - 10, mTextPaint);

    }
}
