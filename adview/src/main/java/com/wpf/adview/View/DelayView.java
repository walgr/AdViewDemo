package com.wpf.adview.View;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangpengfei on 2017/4/21.
 *
 */

public class DelayView extends View {

    private int delayTime = 5000;
    private String title = "跳过";
    private int colorTitle = Color.WHITE;
    private int colorProgressBar = Color.RED;
    private boolean showProgressBar = true;
    private boolean showTime = true;

    private int width;
    private Canvas canvas;
    private RectF rectFProgressBar;
    private float oldProgress = -1,progress = 0;
    private ValueAnimator animatorProgressBar;
    private TextPaint paintTitle = new TextPaint();
    private Paint paintProgressBar = new Paint();

    private OnTimeOverListener onTimeOverListener;

    public DelayView(Context context) {
        this(context,null);
    }

    public DelayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DelayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        paintTitle.setTextSize(width/3);
        paintTitle.setColor(colorTitle);

        paintProgressBar.setColor(colorProgressBar);
        paintProgressBar.setStrokeWidth(width/16);
        paintProgressBar.setAntiAlias(true);
        paintProgressBar.setStyle(Paint.Style.STROKE);

        rectFProgressBar = new RectF(paintProgressBar.getStrokeWidth(),
                paintProgressBar.getStrokeWidth(),
                width-paintProgressBar.getStrokeWidth(),
                width-paintProgressBar.getStrokeWidth());

        animatorProgressBar = ValueAnimator.ofInt(0, delayTime);
        animatorProgressBar.setDuration(delayTime);
        animatorProgressBar.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                progress = value*360/ delayTime;
                if(showTime) title = String.valueOf((delayTime - value)/1000+1);
                if(progress != oldProgress) {
                    oldProgress = progress;
                    invalidate();
                }
            }
        });
        animatorProgressBar.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                progress = 0;
                title = "";
                invalidate();
                if(onTimeOverListener != null) onTimeOverListener.timeOver();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!showProgressBar) return;
        if(width == 0) {
            width = getWidth();
            init();
            if(!animatorProgressBar.isStarted()) animatorProgressBar.start();
        }
        this.canvas = canvas;
        drawTitle();
        drawProgressBar();
    }

    private void drawTitle() {
        canvas.drawText(title,(width-getTextWidth(title))/2,(width+getTextHeight(paintTitle))/2,paintTitle);
    }

    private float getTextWidth(String str) {
        return paintTitle.measureText(str);
    }

    //文字高度
    private float getTextHeight(Paint paint) {
        Paint.FontMetrics fontMetrics= paint.getFontMetrics();
        return -fontMetrics.ascent-fontMetrics.descent;
    }

    private void drawProgressBar() {
        if(!showProgressBar) return;
        drawProgressBar(progress);
    }

    private void drawProgressBar(float progress) {
        canvas.drawArc(rectFProgressBar,-90,progress,false,paintProgressBar);
    }

    public void setShowProgressBar(boolean showProgressBar) {
        this.showProgressBar = showProgressBar;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setColorTitle(int colorTitle) {
        this.colorTitle = colorTitle;
    }

    public void setColorProgressBar(int colorProgressBar) {
        this.colorProgressBar = colorProgressBar;
    }

    public void setOnTimeOverListener(OnTimeOverListener onTimeOverListener) {
        this.onTimeOverListener = onTimeOverListener;
    }

    public void setShowTime(boolean showTime) {
        this.showTime = showTime;
    }

    public interface OnTimeOverListener {
        void timeOver();
    }
}
