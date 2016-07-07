package com.wpf.adview.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
    private List<String> adUrlList = new ArrayList<>();
    private AdView.OnItemClickListener onItemClickListener;

    public AdAdapter(FragmentManager fm,@NonNull List<String> adUrlList) {
        super(fm);
        this.adUrlList = adUrlList;
    }

    @Override
    public Fragment getItem(int position) {
        position = getCurPosition(position,adUrlList.size());
        return AdFragment.newInstance(position,adUrlList.get(position)).setOnItemClickListener(onItemClickListener);
    }

    public static int getCurPosition(int position, int adUrlListSize) {
        int curPosition;
        if((position-max/2)%adUrlListSize>=0) curPosition = (position-max/2)%adUrlListSize;
        else curPosition = adUrlListSize - (max/2-position)%adUrlListSize;
        return curPosition;
    }

    @Override
    public int getCount() {
        return max;
    }

    public AdAdapter setAdUrlList(List<String> adUrlList) {
        this.adUrlList = adUrlList;
        notifyDataSetChanged();
        return this;
    }

    public AdAdapter setOnItemClickListener(AdView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }
}
