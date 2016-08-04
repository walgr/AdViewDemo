package com.wpf.adview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

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

    private List<String> adUrlList = new ArrayList<>();
    private int curPosition = 0;
    private ViewPager viewPager;
    private OnItemClickListener onItemClickListener;
    private AdView.OnPageChangeListener onPageChangeListener;
    private int delayTime = 3000;
    private Timer timer = new Timer();

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
        super(context, attrs);
    }

    public AdView(Context context, List<String> adUrlList) {
        super(context);
        setAdUrlList(adUrlList);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize*9/16,heightMode);
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    private void init() {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.adview,this,false);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        final DotView dotView = (DotView) view.findViewById(R.id.dorView);
        AdAdapter adAdapter = new AdAdapter(((AppCompatActivity)getContext())
                        .getSupportFragmentManager(), adUrlList);
        viewPager.setAdapter(adAdapter);
        viewPager.setCurrentItem(AdAdapter.max/2,false);
        adAdapter.setOnItemClickListener(onItemClickListener);

        dotView.setCurPosition(curPosition);
        dotView.setPointNum(adUrlList.size());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curPosition = AdAdapter.getCurPosition(position,adUrlList.size());
                dotView.setCurPosition(curPosition);
                if(onPageChangeListener != null) onPageChangeListener.onPageSelected(curPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isTouched = state!=0;
            }
        });
        addView(view);
        TimerTask timeTask = new TimerTask() {
            @Override
            public void run() {if(!isTouched) handler.sendEmptyMessage(0x01);
            }
        };
        timer.schedule(timeTask,delayTime,delayTime);
    }

    public AdView setAdUrlList(List<String> adUrlList) {
        this.adUrlList = adUrlList;
        init();
        return this;
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

    public interface OnPageChangeListener {
        void onPageSelected(int position);
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
