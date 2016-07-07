package com.wpf.adview.View;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wpf.adview.AdView;
import com.wpf.adviewpager.R;


/**
 * 广告View
 */

public class AdFragment extends Fragment implements
        View.OnClickListener {

    private int position;
    private String adUrl;
    private AdView.OnItemClickListener onItemClickListener;

    public static AdFragment newInstance(int position,String adUrl) {
        AdFragment adFragment = new AdFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Position",position);
        bundle.putString("AdUrl",adUrl);
        adFragment.setArguments(bundle);
        return adFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ad, container, false);
    }

    @SuppressLint("Assert")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        position = getArguments().getInt("Position");
        adUrl = getArguments().getString("AdUrl");
        assert adUrl != null && !adUrl.isEmpty();
        ImageView imageView = (ImageView) view;
        Glide.with(getActivity()).load(adUrl).into(imageView);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(onItemClickListener != null) onItemClickListener.onClick(position);
    }

    public AdFragment setOnItemClickListener(AdView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }
}
