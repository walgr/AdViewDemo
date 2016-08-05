package com.wpf.adview.View;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 王朋飞 on 7-7-0007.
 * 小圆点
 */

public class DotView extends View {

    private int width;
    private int dotRadius,curDotRadius,oldDotRadius;
    private int allPoint;
    private int curPosition,oldPosition,lastPosition;
    private long delayTime = 100;
    private boolean isReady,isRun;
    private List<float[]> points = new ArrayList<>();
    private Paint mPaint = new Paint();
    private ValueAnimator valueAnimator;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x01)
                setCurPosition(curPosition + 1);
            else if(msg.what == 0x02)
                setCurPosition(curPosition);
        }
    };

    public DotView(Context context) {
        this(context,null);
    }

    public DotView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if(width == 0) {
            width = getWidth();
            int height = getHeight();
            dotRadius = height /4;
            curDotRadius = dotRadius*3/2;
            oldDotRadius = dotRadius;
            valueAnimator = ValueAnimator.ofInt(dotRadius,dotRadius*3/2);
            valueAnimator.setDuration(200);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    curDotRadius = (int)valueAnimator.getAnimatedValue();
                    oldDotRadius = dotRadius*3/2 - (curDotRadius-dotRadius);
                    invalidate();
                }
            });
            if(dotRadius*(3*allPoint+1) > width)
                dotRadius = width/(3*allPoint+1);
            int dotSpace = 2 * dotRadius;
            for(int i = 0;i<allPoint;++i) {
                float[] floats = new float[2];
                floats[0] = width /2 + 2*(i-allPoint/2)*dotRadius + dotSpace * (i - allPoint/2);
                floats[1] = height /2;
                points.add(floats);
            }
            timer.schedule(timeTask,delayTime,delayTime);
        }
        drawDot(canvas);
    }

    private Timer timer = new Timer();
    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(isReady?0x00:0x01);
        }
    };
    public void show() {
        isReady = false;
        lastPosition = curPosition;
        oldPosition = curPosition;
    }

    public void stopShow() {
        isReady = true;
        setCurPosition(oldPosition);
    }

    private void drawDot(Canvas canvas) {
        for(int i = 0;i < allPoint; ++i) {
            float[] point = points.get(i);
            if(i == curPosition)
                canvas.drawCircle(point[0],point[1],curDotRadius, mPaint);
            else if(i == lastPosition)
                canvas.drawCircle(point[0],point[1],oldDotRadius, mPaint);
            else
                canvas.drawCircle(point[0],point[1],dotRadius, mPaint);
        }
    }

    public void setPointNum(int pointNum) {
        this.allPoint = pointNum;
        width = 0;
        invalidate();
    }

    public void setOldPosition(int oldPosition) {
        this.oldPosition = oldPosition;
    }

    public void setCurPosition(int curPosition) {
        lastPosition = this.curPosition;
        this.curPosition = curPosition % allPoint;
        if(valueAnimator != null && allPoint != 1) valueAnimator.start();
        invalidate();
    }
}
