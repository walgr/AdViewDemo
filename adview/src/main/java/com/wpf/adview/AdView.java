package com.wpf.adview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wpf.adview.Adapter.AdAdapter;
import com.wpf.adview.View.DotView;
import com.wpf.adviewpager.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 王朋飞 on 7-7-0007.
 * 广告展示
 */

public class AdView extends FrameLayout {

    private ViewPager viewPager;
    private TextView title;
    private OnItemClickListener onItemClickListener;
    private OnPageChangeListener onPageChangeListener;
    private List<String> adUrlList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_CENTER;
    private StringBuilder sb = new StringBuilder();
    private int titleColor = Color.WHITE,dotColor = Color.WHITE;
    private int delayTime = 3000;
    private int curPosition = 0;
    private float scale = (float) 9/16;
    private int titleLocation,dotViewLocation;
    private float titleSize = 14;
    private boolean isInfiniteLoop = true,isStart,isCanShow = true;

    private static final ImageView.ScaleType[] sScaleTypeArray = {
            ImageView.ScaleType.MATRIX,
            ImageView.ScaleType.FIT_XY,
            ImageView.ScaleType.FIT_START,
            ImageView.ScaleType.FIT_CENTER,
            ImageView.ScaleType.FIT_END,
            ImageView.ScaleType.CENTER,
            ImageView.ScaleType.CENTER_CROP,
            ImageView.ScaleType.CENTER_INSIDE
    };

    private static final int[] gGravityArray = {
            Gravity.TOP|Gravity.LEFT,
            Gravity.TOP|Gravity.CENTER_HORIZONTAL,
            Gravity.TOP|Gravity.RIGHT,
            Gravity.CENTER_VERTICAL|Gravity.LEFT,
            Gravity.CENTER,
            Gravity.CENTER_VERTICAL|Gravity.RIGHT,
            Gravity.BOTTOM|Gravity.LEFT,
            Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,
            Gravity.BOTTOM|Gravity.RIGHT,
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x01) viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        }
    };
    private boolean isTouched;

    public AdView(Context context) {
        this(context,(AttributeSet)null);
    }

    public AdView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray =  context.obtainStyledAttributes(attrs, R.styleable.AdViewCustom,defStyleAttr,0);
        scale = typedArray.getFloat(R.styleable.AdViewCustom_scale, (float) 0.5625);
        scaleType = sScaleTypeArray[typedArray.getInt(R.styleable.AdViewCustom_scaleType,1)];
        isInfiniteLoop = typedArray.getBoolean(R.styleable.AdViewCustom_isInfiniteLoop,true);
        delayTime = isInfiniteLoop?typedArray.getInt(R.styleable.AdViewCustom_delayTime,3000):0;
        titleColor = typedArray.getColor(R.styleable.AdViewCustom_titleColor,Color.WHITE);
        dotColor = typedArray.getColor(R.styleable.AdViewCustom_dotColor,Color.WHITE);
        titleSize = typedArray.getFloat(R.styleable.AdViewCustom_titleSize,14);
        titleLocation = typedArray.getInt(R.styleable.AdViewCustom_titleLocation,7);
        dotViewLocation = typedArray.getInt(R.styleable.AdViewCustom_dotViewLocation,7);
        isCanShow = typedArray.getBoolean(R.styleable.AdViewCustom_showAnim,true);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(scale == 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize((int) (widthMeasureSpec*scale));
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,heightMode);
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    public void start() {
        if(isStart) return;
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.adview,this,false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        title = (TextView) view.findViewById(R.id.title);
        title.setTextColor(titleColor);
        title.setTextSize(titleSize);
        title.setGravity(gGravityArray[titleLocation]);
        final DotView dotView = (DotView) view.findViewById(R.id.dorView);
        dotView.setPointNum(adUrlList.size());
        dotView.setCurPosition(curPosition);
        dotView.setDotColor(dotColor);
        dotView.setLocation(dotViewLocation);
        dotView.setCanShow(isCanShow);
        dotView.reView();
        final AdAdapter adAdapter = new AdAdapter(((AppCompatActivity)getContext())
                        .getSupportFragmentManager(), adUrlList,scaleType,isInfiniteLoop);
        viewPager.setAdapter(adAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curPosition = AdAdapter.getCurPosition(position,adUrlList.size());
                dotView.setCurPosition(curPosition);
                dotView.setOldPosition(curPosition);
                if(!sb.toString().contains(String.valueOf(curPosition))) dotView.show(); else dotView.stopShow();
                title.setText(titleList.size()>curPosition?titleList.get(curPosition):"");
                if(onPageChangeListener != null) onPageChangeListener.onPageSelected(curPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isTouched = state!=0;
            }
        });
        if(isInfiniteLoop) viewPager.setCurrentItem(AdAdapter.max/2,false);
        else title.setText(titleList.get(curPosition));
        adAdapter.setOnItemClickListener(onItemClickListener);
        adAdapter.setOnResourceReady(new OnResourceReady() {
            @Override
            public void onResourceReady(int position) {
                if(!sb.toString().contains(String.valueOf(position))) sb.append(position);
                if(position == curPosition) dotView.stopShow();
            }
        });
        addView(view);
        if(delayTime != 0) {
            TimerTask timeTask = new TimerTask() {
                @Override
                public void run() {
                    if (!isTouched) handler.sendEmptyMessage(0x01);
                }
            };
            new Timer().schedule(timeTask,delayTime,delayTime);
        }
        isStart = true;
    }

    public AdView setAdUrlList(List<String> adUrlList) {
        this.adUrlList = adUrlList;
        return this;
    }

    public AdView setTitleList(List<String> titleList) {
        this.titleList = titleList;
        return this;
    }

    public AdView setTitleColor(int color) {
        titleColor = color;
        return this;
    }

    public AdView setTitleSize(float size) {
        titleSize = size;
        return this;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public AdView addOnPageChangeListener(AdView.OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        return this;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setDelayTime(int delayTime) {
        this.delayTime = delayTime;
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
    }

    public void setInfiniteLoop(boolean infiniteLoop) {
        isInfiniteLoop = infiniteLoop;
    }

    public void setCanShow(boolean canShow) {
        isCanShow = canShow;
    }

    public interface OnPageChangeListener {
        void onPageSelected(int position);
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public interface OnResourceReady {
        void onResourceReady(int position);
    }
}
