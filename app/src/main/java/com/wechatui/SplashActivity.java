package com.wechatui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.wechatui.fragment.SplashFragment;
import com.wechatui.view.transformer.ScaleTransformer;


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ViewPager mVpMain;
    private int[] mResIds = new int[]{
            R.drawable.start_page1,
            R.drawable.start_page2,
            R.drawable.start_page3,
            R.drawable.start_page4,
            R.drawable.start_page5,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mVpMain = findViewById(R.id.vp_main);

        mVpMain.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                return SplashFragment.newInstance(mResIds[position]);
            }

            @Override
            public int getCount() {
                return mResIds.length;
            }
        });

        mVpMain.setPageTransformer(true,new ScaleTransformer());

    }


}
