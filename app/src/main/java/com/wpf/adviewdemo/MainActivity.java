package com.wpf.adviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.socks.library.KLog;
import com.wpf.adview.AdView;
import com.wpf.adviewpdemo.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        AdView.OnPageChangeListener ,
        AdView.OnItemClickListener {

    private List<String> adUrlList = new ArrayList<String>(){{
        add("http://a.hiphotos.baidu.com/zhidao/pic/item/72f082025aafa40fa38bfc55a964034f79f019ec.jpg");
        add("http://photo.enterdesk.com/2011-2-16/enterdesk.com-1AA0C93EFFA51E6D7EFE1AE7B671951F.jpg");
        add("http://c.hiphotos.baidu.com/zhidao/pic/item/574e9258d109b3de56800b06cebf6c81800a4ca5.jpg");
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView adView = (AdView) findViewById(R.id.adView);
        adView.addOnPageChangeListener(this);
        adView.setOnItemClickListener(this);
        adView.setAdUrlList(adUrlList);
    }


    @Override
    public void onPageSelected(int position) {
        KLog.e(position);
    }

    @Override
    public void onClick(int position) {
        KLog.e(position);
    }
}
