package com.wpf.adviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.wpf.adview.AdView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        AdView.OnPageChangeListener ,
        AdView.OnItemClickListener {

    private List<String> adUrlList = new ArrayList<String>(){{
        add("http://a.hiphotos.baidu.com/zhidao/pic/item/72f082025aafa40fa38bfc55a964034f79f019ec.jpg");
        add("http://photo.enterdesk.com/2011-2-16/enterdesk.com-1AA0C93EFFA51E6D7EFE1AE7B671951F.jpg");
        add("http://c.hiphotos.baidu.com/zhidao/pic/item/574e9258d109b3de56800b06cebf6c81800a4ca5.jpg");
        add("http://img.wallpapersking.com/d7/2012-8/2012081210374.jpg");
        add("http://pic.ilitu.com/y2/919_87426347492.jpg");
        add("http://pic32.nipic.com/20130812/3088286_192614263395_2.jpg");
        add("http://image16.360doc.com/DownloadImg/2010/10/2812/6422708_2.jpg");
        add("http://f.hiphotos.baidu.com/zhidao/pic/item/50da81cb39dbb6fd8fcb2a460d24ab18972b373a.jpg");
        add("http://pic27.nipic.com/20130128/11383425_184203698000_2.jpg");
        }};
    private List<String> titleList = new ArrayList<String>(){{
        add("第一张");add("第二张");add("第三张");
        add("第四张");add("第五张");add("第六张");
        add("第七张");add("第八张");add("第九张");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView adView = (AdView) findViewById(R.id.adView);
        adView.addOnPageChangeListener(this);
        adView.setOnItemClickListener(this);
        adView.setAdUrlList(adUrlList);
        adView.setTitleList(titleList);
        adView.start();
    }


    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onClick(int position) {
        Log.e("点击",""+position);
    }
}
