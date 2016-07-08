package com.wpf.adview.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王朋飞 on 7-7-0007.
 * 小圆点
 */

public class DotView extends View {

    private int width,height,dotRadius;
    private List<float[]> points = new ArrayList<>();
    private int allPoint,curPosition = 0;
    private Paint mPaint;

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
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(width == 0) {
            width = getWidth();
            height = getHeight();
            dotRadius = height/4;
            if(dotRadius*(3*allPoint+1) > width)
                dotRadius = width/(3*allPoint+1);
            int dotSpace = 2 * dotRadius;
            for(int i = 0;i<allPoint;++i) {
                float[] floats = new float[2];
                floats[0] = width /2 + 2*(i-allPoint/2)*dotRadius + dotSpace * (i - allPoint/2);
                floats[1] = height/2;
                points.add(floats);
            }
        }
        drawDot(canvas);
    }

    private void drawDot(Canvas canvas) {
        for(int i=0;i<allPoint;++i) {
            float[] point = points.get(i);
            canvas.drawCircle(point[0],point[1],(i==curPosition)?(int)(dotRadius*1.5):dotRadius,mPaint);
        }
    }

    public void setPointNum(int pointNum) {
        this.allPoint = pointNum;
        width = 0;
        invalidate();
    }

    public void setCurPosition(int curPosition) {
        this.curPosition = curPosition;
        invalidate();
    }
}
