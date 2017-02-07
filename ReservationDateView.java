package com.biohop.biohopnet.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.biohop.biohopnet.R;
import com.biohop.biohopnet.utils.DensityUtil;
import com.zhy.autolayout.utils.DimenUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 自定义预约日期    提供setStartDate()开始计算时间
 * setReservationTime(List<String> dates)提供可以预约的时间
 * setOnItemClickListener(ItemClickListener listener)
 * 以及点击监听事件ItemClickListener接口
 * Creater by CreaterXu at 2017/1/19 16:25
 */

public class ReservationDateView extends View {
    /*
         * 点击接口
         */
    public interface ItemClickListener {
        void clickReservation(String date);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    ItemClickListener itemClickListener;
    private int width;
    private int height;
    private int bigTextSize;
    private int smallTextSize;
    private int gridW;//格子宽
    private int gridH;//格子高

    public String getStartDate() {
        return startDate;
    }
    /**
     * 设置开始时间
     *
     * */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
        invalidate();
    }

    private String startDate = "2017-02-26";//默认开始日期
    private Paint mPaint;
    private TextPaint textPaint;

    private Point[] points = new Point[24];

    private List<Integer> canReservationPoint = new ArrayList<>();

    public ReservationDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BezierView);

    }

    /**
     * 设置预约时间   时间格式为2017-02-17：AM
     *
     * */
    public void setReservationTime(List<String> times) {
        for (String time : times) {
            String date = time.split(":")[0];
            String apm = time.split(":")[1];
            for (int i=0;i<7;i++){
                if(date.equals(getSpecifiedDayAfter(startDate)[i])){
                    if(apm.equals("AM")){
                        canReservationPoint.add(8+i+1);
                    }else {
                        canReservationPoint.add(16+i+1);
                    }
                    break;
                }
            }
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        bigTextSize = width / 18;
        smallTextSize = width / 50;

        Log.e("xv", "onMeasure: " + width + "height:" + height + "...." + widthMode + "heightMode:" + heightMode);
        mPaint = new Paint();
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("xv", "onDraw: ");
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        drawTitle(canvas, "2017-2-26");
        drawGrid(canvas);
        drawAPm(canvas);
        drawBottom(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 绘制底部说明
     */
    private void drawBottom(Canvas canvas) {
        textPaint.setTextSize(bigTextSize);
        canvas.drawText("注：绿色可以预约，灰色存在预约",width/2,height/20*19,textPaint);
    }

    /**
     * 绘制标题
     */
    private void drawTitle(Canvas canvas, String starDate) {
        int centerWidth = width / 2;
        int centerHeight = height / 10;
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(bigTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        sim.setLenient(false);
        Date startd = null;
        Date endd = null;
        Calendar s = Calendar.getInstance();
        Calendar e = Calendar.getInstance();
        try {
            startd = sim.parse(starDate);
            s.setTime(startd);
            endd = sim.parse(getSpecifiedDayAfter(starDate)[6]);
            e.setTime(endd);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        String text = (s.get(Calendar.MONTH) + 1) + "月" + s.get(Calendar.DAY_OF_MONTH) + "日至" + (e.get(Calendar.MONTH) + 1) + "月" + e.get(Calendar.DAY_OF_MONTH) + "日";
        canvas.drawText(text, (float) centerWidth, (float) centerHeight, textPaint);
    }

    /**
     * 绘制时段
     */
    private void drawAPm(Canvas canvas) {
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(smallTextSize);
        Path path = new Path();
        path.moveTo(points[8].x + gridW / 2, points[8].y);
        path.lineTo(points[8].x + gridW / 2, points[8].y + gridH);
        canvas.drawTextOnPath("8:00-12:00", path, 0, 0, textPaint);
        Path path1 = new Path();
        path1.moveTo(points[16].x + gridW / 2, points[16].y);
        path1.lineTo(points[16].x + gridW / 2, points[16].y + gridH);
        canvas.drawTextOnPath("13:00-17:00", path1, 0, 0, textPaint);
        for (int i = 1; i <= 7; i++) {
            String date = null;
            try {
                SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
                sim.setLenient(false);
                Date startd = null;
                startd = sim.parse(getSpecifiedDayAfter(startDate)[i - 1]);
                Calendar s = Calendar.getInstance();
                s.setTime(startd);
                date = (s.get(Calendar.MONTH) + 1) + "月" + s.get(Calendar.DAY_OF_MONTH) + "日";
            } catch (ParseException e) {
                e.printStackTrace();
            }
            canvas.drawText(date, points[i].x + gridW / 2, points[i].y + gridH / 2, textPaint);
        }
    }


    private void drawGrid(Canvas canvas) {
        gridW = width / 10;
        gridH = height / 5;
        int lineW = width / 70;
        float x = width / 2 - lineW * 3.5f - 4 * gridW;
        float y = height / 10 + height / 10;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#e2e2e2"));
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                mPaint.setColor(Color.parseColor("#e2e2e2"));
            } else {
                mPaint.setColor(Color.parseColor("#CD1E06"));
            }
            for (int j = 0; j < 8; j++) {
                if (j == 0) {
                    mPaint.setColor(Color.parseColor("#e2e2e2"));
                } else if (j != 0 && i != 0) {
                    mPaint.setColor(Color.parseColor("#CD1E06"));
                }
                Point point = new Point();
                point.set((int) x + j * (gridW + lineW), (int) y + i * (gridH + lineW));
                points[i * 8 + j] = point;

                boolean hasPoint=false;
                for(Integer num:canReservationPoint){
                    if((i*8+j)==num) {
                        mPaint.setColor(Color.parseColor("#008000"));
                        canvas.drawRect(x + j * (gridW + lineW), y + i * (gridH + lineW), x + j * (gridW + lineW) + gridW, y + i * (gridH + lineW) + gridH, mPaint);
                        hasPoint = true;
                        break;
                    }
                }
                if(!hasPoint){
                    canvas.drawRect(x + j * (gridW + lineW), y + i * (gridH + lineW), x + j * (gridW + lineW) + gridW, y + i * (gridH + lineW) + gridH, mPaint);
                }
            }
        }
    }

    /**
     * 获得指定日期的后7天
     *
     * @param specifiedDay
     * @return 七天数组
     */
    public static String[] getSpecifiedDayAfter(String specifiedDay) {
        String[] sevenDate = new String[7];
        sevenDate[0] = specifiedDay;
        for (int i = 0; i < 6; i++) {
            Calendar c = Calendar.getInstance();
            Date date = null;
            try {
                SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MM-dd");
                dataformat.setLenient(false);
                date = dataformat.parse(specifiedDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.setTime(date);
            int day = c.get(Calendar.DATE);
            c.set(Calendar.DATE, day + 1);
            specifiedDay = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
            sevenDate[i + 1] = specifiedDay;
        }
        return sevenDate;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_DOWN:
                //获取屏幕上点击的坐标
                float x=event.getX();
                float y = event.getY();
                for (Integer num:canReservationPoint
                     ) {
                    int minX=points[num].x;
                    int maxX=points[num].x+gridW;
                    int minY=points[num].y;
                    int maxY=points[num].y+gridH;
                    if(x>=minX&&x<=maxX&&y>minY&&y<=maxY){
                        String date=null;
                        if(num>16){
                            int day=num-16;
                            String d=getSpecifiedDayAfter(startDate)[day-1];
                            date=d+":PM";
                        }else {
                            int day=num-8;
                            String d=getSpecifiedDayAfter(startDate)[day-1];
                            date=d+":AM";
                        }
                        itemClickListener.clickReservation(date);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //点击抬起后，回复初始位置。
                invalidate();//更新视图
                return true;
        }
        return super.onTouchEvent(event);
    }
}
