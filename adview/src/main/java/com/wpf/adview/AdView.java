package com.wpf.adview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wpf.adview.Adapter.AdAdapter;
import com.wpf.adview.View.DelayControlView;
import com.wpf.adview.View.DelayView;
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

public class AdView extends FrameLayout implements
        DelayView.OnTimeOverListener {

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
            Gravity.TOP|Gravity.START,
            Gravity.TOP|Gravity.CENTER_HORIZONTAL,
            Gravity.TOP|Gravity.END,
            Gravity.CENTER_VERTICAL|Gravity.START,
            Gravity.CENTER,
            Gravity.CENTER_VERTICAL|Gravity.END,
            Gravity.BOTTOM|Gravity.START,
            Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL,
            Gravity.BOTTOM|Gravity.END,
    };
    private ViewPager viewPager;
    private TextView title;
    private DotView dotView;
    private DelayControlView delayControlView;
    private OnItemClickListener onItemClickListener;
    private OnPageChangeListener onPageChangeListener;
    private List<String> adUrlList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private StringBuilder sb = new StringBuilder();
    private boolean isStart,isTouched;
    private int curPosition = 0;

    //轮播图图片显示效果
    private ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_CENTER;
    //轮播图提示文字位置
    private int titleLocation;
    //轮播图小圆点位置
    private int dotViewLocation;
    //轮播图延迟按钮位置
    private int delayControlLocation;
    //轮播图提示文字大小
    private float titleSize;
    //轮播图提示文字颜色
    private int titleColor = Color.WHITE;
    //轮播图小圆点颜色
    private int dotColor = Color.WHITE;
    //轮播图显示比例
    private float scale = (float) 9/16;
    //轮播图自动跳转
    private boolean isAutoSkip = true;
    //是否无线循环
    private boolean isInfiniteLoop = true;
    //轮播图位置跳转动画
    private boolean showDotAnim = true;
    //显示进度条
    private boolean showProgressBar = true;
    //显示延时时间
    private boolean showDelayTime = true;
    //提示短语
    private String pointText = "";
    //提示短语文字大小
    private float pointTextSize;
    //轮播图跳转时间
    private int delayTime_ViewPager = 3000;
    //自动退出延时时间
    private int delayTime = 3000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x01) viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
        }
    };

    public AdView(Context context) {
        this(context,null);
    }

    public AdView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray =  context.obtainStyledAttributes(attrs, R.styleable.AdView,defStyleAttr,0);
        scale = typedArray.getFloat(R.styleable.AdView_scale, (float) 0.5625);
        scaleType = sScaleTypeArray[typedArray.getInt(R.styleable.AdView_scaleType,1)];
        isAutoSkip = typedArray.getBoolean(R.styleable.AdView_isAutoSkip,true);
        isInfiniteLoop = typedArray.getBoolean(R.styleable.AdView_isInfiniteLoop,true);
        delayTime_ViewPager = typedArray.getInt(R.styleable.AdView_delayTime_ViewPager,3000);
        titleColor = typedArray.getColor(R.styleable.AdView_titleColor,Color.WHITE);
        dotColor = typedArray.getColor(R.styleable.AdView_dotColor,Color.WHITE);
        titleSize = typedArray.getDimension(R.styleable.AdView_titleSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
                        getResources().getDisplayMetrics()));
        titleLocation = typedArray.getInt(R.styleable.AdView_titleLocation,7);
        dotViewLocation = typedArray.getInt(R.styleable.AdView_dotViewLocation,7);
        showDotAnim = typedArray.getBoolean(R.styleable.AdView_showDotAnim,true);
        delayControlLocation = typedArray.getInt(R.styleable.AdView_delayControlLocation,2);
        pointText = typedArray.getString(R.styleable.AdView_pointText);
        pointTextSize = typedArray.getDimension(R.styleable.AdView_pointTextSize,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
                getResources().getDisplayMetrics()));
        showProgressBar = typedArray.getBoolean(R.styleable.AdView_showProgressBar,false);
        showDelayTime = typedArray.getBoolean(R.styleable.AdView_showDelayTime,true);
        delayTime = typedArray.getInt(R.styleable.AdView_delayTime,3000);
        typedArray.recycle();
        if(pointText == null) pointText = "";
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.adview,this,false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        title = (TextView) view.findViewById(R.id.title);
        dotView = (DotView) view.findViewById(R.id.dorView);
        delayControlView = (DelayControlView) view.findViewById(R.id.delayView);
        addView(view);
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

        initViewPager();
        initDotView();
        initDelayControlView();

        if(isAutoSkip && delayTime_ViewPager != 0) {
            TimerTask timeTask = new TimerTask() {
                @Override
                public void run() {
                    if (isAutoSkip && !isTouched) handler.sendEmptyMessage(0x01);
                }
            };
            new Timer().schedule(timeTask, delayTime_ViewPager, delayTime_ViewPager);
        }
        isStart = true;
    }

    private void initViewPager() {
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
    }

    private void initDotView() {
        title.setTextColor(titleColor);
        title.setTextSize(TypedValue.COMPLEX_UNIT_PX,titleSize);
        title.setGravity(gGravityArray[titleLocation]);
        dotView.setPointNum(adUrlList.size());
        dotView.setCurPosition(curPosition);
        dotView.setDotColor(dotColor);
        dotView.setLocation(dotViewLocation);
        dotView.setShowDotAnim(showDotAnim);
        dotView.reView();
    }

    private void initDelayControlView() {
        delayControlView.setShowProgressBar(showProgressBar);
        if(!showProgressBar && !pointText.isEmpty())
            delayControlView.getView().setBackground(ContextCompat
                    .getDrawable(getContext(),R.drawable.pointtextback));
        delayControlView.setPointText(pointText);
        delayControlView.setOnTimeOverListener(this);
        delayControlView.setDelayTime(delayTime);
        delayControlView.setShowDelayTime(showDelayTime);
        delayControlView.setPointTextSize(pointTextSize);
        setDelayControlLocation(delayControlLocation);
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

    public void setShowDotAnim(boolean showDotAnim) {
        this.showDotAnim = showDotAnim;
    }

    public void setShowProgressBar(boolean showProgressBar) {
        this.showProgressBar = showProgressBar;
    }

    public void setDelayControlLocation(int delayControlLocation) {
        this.delayControlLocation = delayControlLocation;
        delayControlView.setGravity(gGravityArray[delayControlLocation]);
    }

    public void setDelayControlViewBack(Drawable back) {
        delayControlView.setBackground(back);
    }

    @Override
    public void timeOver() {
        delayControlView.getView().performClick();
    }

    public void setPointText(String pointText) {
        this.pointText = pointText;
        delayControlView.setPointText(pointText);
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
