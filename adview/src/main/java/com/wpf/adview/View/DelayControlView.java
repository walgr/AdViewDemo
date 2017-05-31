package com.wpf.adview.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wpf.adviewpager.R;

/**
 * Created by wangpengfei on 2017/4/11.
 * 延迟显示View
 */

public class DelayControlView extends LinearLayout implements
        View.OnClickListener {

    private View view;
    private TextView delayText;
    private DelayView delayView;

    public DelayControlView(Context context) {
        this(context,null);
    }

    public DelayControlView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DelayControlView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        if(view == null) {
            view = View.inflate(getContext(), R.layout.delayview, null);
            delayText = (TextView) view.findViewById(R.id.delayText);
            delayView = (DelayView) view.findViewById(R.id.delayView);
            view.setOnClickListener(this);
            addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        getActivity().finish();
    }

    private AppCompatActivity getActivity() {
        return (AppCompatActivity) getContext();
    }

    public View getView() {
        return view;
    }

    public void setOnTimeOverListener(DelayView.OnTimeOverListener onTimeOverListener) {
        delayView.setOnTimeOverListener(onTimeOverListener);
    }

    public void setPointText(String pointText) {
        delayText.setText(pointText);
        delayText.setVisibility(!pointText.isEmpty()?VISIBLE:GONE);
    }

    public void setShowProgressBar(boolean showProgressBar) {
        delayView.setVisibility(showProgressBar?VISIBLE:GONE);
    }

    public void setShowDelayTime(boolean showDelayTime) {
        delayView.setShowTime(showDelayTime);
    }

    public void setDelayTime(int delayTime) {
        delayView.setDelayTime(delayTime);
    }

    public void setPointTextSize(float pointTextSize) {
        delayText.setTextSize(TypedValue.COMPLEX_UNIT_PX,pointTextSize);
    }
}
