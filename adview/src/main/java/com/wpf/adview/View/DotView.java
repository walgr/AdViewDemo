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
    private int location;
    private int dotRadius,curDotRadius,oldDotRadius;
    private int allPoint;
    private int curPosition,oldPosition,lastPosition;
    private int dotColor = Color.WHITE;
    private long delayTime = 100;
    private boolean isReady,isCanShow;
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
        mPaint.setColor(dotColor);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
    }

    public void reView() {
        init();
        invalidate();
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
                floats[0] = getDotLeft() + 2*(i-allPoint/2)*dotRadius + dotSpace * (i - allPoint/2);
                floats[1] = height /2;
                points.add(floats);
            }
            timer.schedule(timeTask,delayTime,delayTime);
        }
        drawDot(canvas);
    }

    private int getDotLeft() {
        if(location == 6) return 2*allPoint*dotRadius;
        else if(location == 7) return width/2;
        else if(location == 8) return width - 2*allPoint*dotRadius;
        else return width/2;
    }

    private Timer timer = new Timer();
    private TimerTask timeTask = new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(isReady?0x00:0x01);
        }
    };
    public void show() {
//        isReady = false;
        isReady = !isCanShow;
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
        this.curPosition = allPoint != 0?curPosition % allPoint:0;
        if(valueAnimator != null && allPoint != 1) valueAnimator.start();
        invalidate();
    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
        invalidate();
    }

    public void setLocation(int location) {
        //6左 7中 8右
        width = 0;
        this.location = location;
    }

    public void setCanShow(boolean canShow) {
        isCanShow = canShow;
    }
}
