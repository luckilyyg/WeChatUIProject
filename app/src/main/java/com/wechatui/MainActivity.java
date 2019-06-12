package com.wechatui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.wechatui.fragment.TabFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 仿微信的一个demo
 * 微信+通讯录+发现+我
 * <p>
 * activity调用fragment的方法
 * <p>
 * 发现问题：
 * 1、FragmentStatePagerAdapter与FragmentPagerAdapter区别：通过打印log
 * FragmentPagerAdapter:适合用于tab数量比较少
 * FragmentStatePagerAdapter:适合用于tab数量比较多
 *
 *
 *2、通讯
 * Activity->Fragment:拿到fragment.xxx
 * Fragment->Activity:换个思路...不是fragment要调用activity方法。而是，fragment对外提供自己的核心事件回调，activity自己选择是否监听。
 *
 *
 *
 * 3、如何正确的管理多个tab的viewpager
 * 出现的问题就是：屏幕旋转之后，onCreate会重新执行；getItem没有执行
 *
 *
 *
 *
 * 4、tab自定义控件
 * 1)纯绘制：效率高
 * 2)组合形式：稳定性高
 *
 *
 *
 *
 * 5、Viewpage的切换动画
 * setPageTransformer
 * view,position
 * scale view->(0.75f,1)
 * possiton->动画的区间
 * rotate
 * 注意旋转中心
 * 注意空间 要设置大点
 * banner
 * clipChildren
 * pageMargin(scale)
 *
 *
 *
 *
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ViewPager mVpMain;
    private List<String> mTitles = new ArrayList<>(Arrays.asList("微信", "通讯录", "发现", "我"));
    private Button mBtnWeChat;
    private Button mBtnFriend;
    private Button mBtnFind;
    private Button mBtnMine;
    //    private List<Fragment> mFragment=new ArrayList<>();
    private SparseArray<TabFragment> mFragment = new SparseArray<>();
    private List<Button> mTabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initViewPager();
        initListen();

    }

    private void initViews() {
        mVpMain = findViewById(R.id.vp_main);
        mBtnWeChat = findViewById(R.id.btn_wechat);
        mBtnFriend = findViewById(R.id.btn_friend);
        mBtnFind = findViewById(R.id.btn_find);
        mBtnMine = findViewById(R.id.btn_mine);
//顺序不要出错了
        mTabs.add(mBtnWeChat);
        mTabs.add(mBtnFriend);
        mTabs.add(mBtnFind);
        mTabs.add(mBtnMine);


    }

    private void initViewPager() {
        mVpMain.setOffscreenPageLimit(mTitles.size());
       /*
       屏幕旋转之后，onCreat会重新执行 getItem没有执行...
       但是出现这样的概率很小 因为开发者99%会在manifest的activity中设置 android：screenOrientation="portrait"
        mFragment.add(TabFragment.newInstance(""));
        mFragment.add(TabFragment.newInstance(""));
        mFragment.add(TabFragment.newInstance(""));
        mFragment.add(TabFragment.newInstance(""));

        解决方案：写一个合理的fragment管理，尤其是在多tab中
SparseArray<TabFragment> mFragment = new SparseArray<>();
代替
private List<Fragment> mFragment=new ArrayList<>();

        */

//        mVpMain.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//
//                return TabFragment.newInstance(mTitles.get(position));
//            }
//
//            @Override
//            public int getCount() {
//                return mTitles.size();
//            }
//        });

        mVpMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {//getItem相当于createfragment
                TabFragment fragment = TabFragment.newInstance(mTitles.get(position));
                //fragment与activity通讯
                if (position == 0) {
                    fragment.setOnTitleClickListener(new TabFragment.OnTitleClickListener() {
                        @Override
                        public void onClick(String title) {
                            changeWeChatTab(title);//由activity自由选择要不要调用fragment的方法
                        }
                    });
                }
                return fragment;
            }

            @Override
            public int getCount() {
                return mTitles.size();
            }


            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                TabFragment fragment = (TabFragment) super.instantiateItem(container, position);
                mFragment.put(position, fragment);
                return fragment;
            }


            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                mFragment.remove(position);
                super.destroyItem(container, position, object);
            }
        });


        mVpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e(TAG, "onPageScrolled:  position=" + position + ",positionOffset=" + positionOffset);


                //左->右  0->1,left pos,right pos+1,positionOffset 0-1 微信->通讯录
                //细化：left progress:1-0(绿色-灰色）（1-positionOffset）； right progress:0-1(灰色-绿色）（positionOffset）

                //右->左  1->0,left pos,right pos+1,positionOffset 1-0 通讯录->微信

                //细化：left progress:0-1(灰色-绿色）（1-positionOffset）； right progress:1-0(绿色-灰色）（positionOffset）



             //left tab
                //right tab
                if (positionOffset >0) {

                    Button left = mTabs.get(position);
                    Button right = mTabs.get(position + 1);


                    left.setText((1-positionOffset)+"");
                    right.setText(positionOffset+"");
                }


            }

            @Override
            public void onPageSelected(int position) {
                Log.e(TAG, "onPageSelected: position=" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void initListen() {

        mBtnWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TabFragment tabFragment = mFragment.get(0);
                if (tabFragment != null) {
                    tabFragment.changeTitle("微信 change");//调用fragment的方法
                }
            }
        });
    }


    public void changeWeChatTab(String title) {
        mBtnWeChat.setText(title);
    }

}
