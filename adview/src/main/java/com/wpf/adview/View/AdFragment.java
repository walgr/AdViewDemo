package com.wpf.adview.View;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.wpf.adview.AdView;
import com.wpf.adviewpager.R;

import java.util.HashMap;


/**
 * 广告View
 */

@SuppressLint("ValidFragment")
public class AdFragment extends Fragment implements
        View.OnClickListener {

    private int position;
    private String adUrl;
    private ImageView.ScaleType scaleType = ImageView.ScaleType.FIT_CENTER;
    private AdView.OnItemClickListener onItemClickListener;
    private AdView.OnResourceReady onResourceReady;
    private View addView;

    public AdFragment(View view) {
        this.addView = view;
    }

    public static AdFragment newInstance(int position, String adUrl,
                                         ImageView.ScaleType scaleType,
                                         View addView) {
        AdFragment adFragment = new AdFragment(addView);
        Bundle bundle = new Bundle();
        bundle.putInt("Position",position);
        bundle.putString("AdUrl",adUrl);
        bundle.putSerializable("ScaleType",scaleType);
        adFragment.setArguments(bundle);
        return adFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad, container, false);
        FrameLayout frameLayout = new FrameLayout(container.getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        frameLayout.addView(view);
        return frameLayout;
    }

    @SuppressLint("Assert")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            position = bundle.getInt("Position");
            adUrl = bundle.getString("AdUrl");
            scaleType = (ImageView.ScaleType) bundle.getSerializable("ScaleType");
        }
        if(addView != null) {
            View parentView = (View) addView.getParent();
            if(parentView instanceof FrameLayout) {
                ((FrameLayout) parentView).removeView(addView);
            }
            ((FrameLayout)view).addView(addView);
        }
        assert adUrl != null && !adUrl.isEmpty();
        ImageView imageView = view.findViewById(R.id.image);
        imageView.setScaleType(scaleType);

        Glide.with(getActivity()).load(adUrl)
//                .placeholder(R.mipmap.loading)
//                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if(onResourceReady != null) onResourceReady.onResourceReady(position);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

        imageView.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onClick(View view) {
        if(onItemClickListener != null) onItemClickListener.onClick(position);
    }

    public AdFragment setOnItemClickListener(AdView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    public AdFragment setOnResourceReady(AdView.OnResourceReady onResourceReady) {
        this.onResourceReady = onResourceReady;
        return this;
    }
}
