package com.wpf.adview.View;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.wpf.adviewpager.R;

/**
 * Created by 王朋飞 on 2017/6/1.
 *
 * @Title 指示器界面
 * @Package com.wpf.adview.View
 * @Description: 用作显示轮播图位置和标题
 * @LastModifiedTime 2017/6/1
 */

public class IndicatorView extends FrameLayout {

    private View view;
    private TextView title;
    private DotView dotView;

    public IndicatorView(@NonNull Context context) {
        this(context,null);
    }

    public IndicatorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public IndicatorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        if(view == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.indicatorview,this,false);
            title = view.findViewById(R.id.title);
            dotView = view.findViewById(R.id.dotView);
            setLayoutParams(view.getLayoutParams());
            addView(view);
        }
    }

    public TextView getTitle() {
        return title;
    }

    public DotView getDotView() {
        return dotView;
    }
}
