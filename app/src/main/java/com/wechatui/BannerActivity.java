package com.wechatui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.wechatui.view.transformer.RotateTransformer;


public class BannerActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ViewPager mVpMain;
    private int[] mResIds = new int[]{
            0xffff0000,
            0xff00ff00,
            0xff0000ff
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        mVpMain = findViewById(R.id.vp_main);
        mVpMain.setOffscreenPageLimit(3);
        mVpMain.setPageMargin(40);
        mVpMain.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mResIds.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = new View(container.getContext());

                view.setBackgroundColor(mResIds[position]);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
//                super.destroyItem(container, position, object);
                container.removeView((View) object);
            }
        });
//        mVpMain.setPageTransformer(true,new ScaleTransformer());
        mVpMain.setPageTransformer(true,new RotateTransformer());
    }


}
