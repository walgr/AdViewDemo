package com.wpf.adview.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.ImageView;

import com.wpf.adview.View.AdFragment;
import com.wpf.adview.AdView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王朋飞 on 7-7-0007.
 * 适配器
 */

public class AdAdapter extends FragmentPagerAdapter {

    public static int max = 10000001;
    private static boolean isInfiniteLoop = true;
    private ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_CENTER;
    private List<String> adUrlList = new ArrayList<>();
    private AdView.OnItemClickListener onItemClickListener;
    private AdView.OnResourceReady onResourceReady;

    public AdAdapter(FragmentManager fm, @NonNull List<String> adUrlList,
                     ImageView.ScaleType scaleType,boolean isInfiniteLoop) {
        super(fm);
        this.adUrlList = adUrlList;
        this.scaleType = scaleType;
        AdAdapter.isInfiniteLoop = isInfiniteLoop;
    }

    @Override
    public Fragment getItem(int position) {
        position = getCurPosition(position,adUrlList.size());
        return AdFragment.newInstance(position,adUrlList.get(position),scaleType)
                .setOnResourceReady(onResourceReady)
                .setOnItemClickListener(onItemClickListener);
    }

    public static int getCurPosition(int position, int adUrlListSize) {
        if(!isInfiniteLoop) return position;
        int curPosition;
        if((position-max/2)%adUrlListSize>=0) curPosition = (position-max/2)%adUrlListSize;
        else curPosition = adUrlListSize - (max/2-position)%adUrlListSize;
        return curPosition;
    }

    @Override
    public int getCount() {
        return isInfiniteLoop?max:adUrlList.size();
    }

    public AdAdapter setAdUrlList(List<String> adUrlList) {
        this.adUrlList = adUrlList;
        notifyDataSetChanged();
        return this;
    }

    public void setInfiniteLoop(boolean infiniteLoop) {
        isInfiniteLoop = infiniteLoop;
    }

    public AdAdapter setOnItemClickListener(AdView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public AdAdapter setOnResourceReady(AdView.OnResourceReady onResourceReady) {
        this.onResourceReady = onResourceReady;
        return this;
    }
}
