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
    private Paint mPaint_cur,mPaint_Other;

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
        mPaint_cur = new Paint();
        mPaint_cur.setStyle(Paint.Style.FILL);
        mPaint_cur.setColor(Color.GRAY);
        mPaint_cur.setAntiAlias(true);
        mPaint_cur.setStrokeWidth(1);

        mPaint_Other = new Paint();
        mPaint_Other.setStyle(Paint.Style.FILL);
        mPaint_Other.setColor(Color.WHITE);
        mPaint_Other.setAntiAlias(true);
        mPaint_Other.setStrokeWidth(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(width == 0) {
            width = getWidth();
            height = getHeight();
            dotRadius = height/3;
            if(dotRadius*(3*allPoint+1) > width)
                dotRadius = width/(3*allPoint+1);

            for(int i = 0;i<allPoint;++i) {
                float[] floats = new float[2];
                floats[0] = width /2 + 3*(i-allPoint/2)*dotRadius;
                floats[1] = height/2;
                points.add(floats);
            }
        }
        drawDot(canvas);
    }

    private void drawDot(Canvas canvas) {
        for(int i=0;i<allPoint;++i) {
            float[] point = points.get(i);
            canvas.drawCircle(point[0],point[1],dotRadius,(i==curPosition)?mPaint_cur:mPaint_Other);
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
