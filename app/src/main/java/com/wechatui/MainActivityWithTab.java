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


import com.wechatui.fragment.TabFragment;
import com.wechatui.view.TabView;

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
 * FragmentStatePagerAdapter与FragmentPagerAdapter区别：通过打印log
 * FragmentPagerAdapter:适合用于tab数量比较少
 * FragmentStatePagerAdapter:适合用于tab数量比较多
 */
public class MainActivityWithTab extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ViewPager mVpMain;
    private List<String> mTitles = new ArrayList<>(Arrays.asList("微信", "通讯录", "发现", "我"));
    private TabView mBtnWeChat;
    private TabView mBtnFriend;
    private TabView mBtnFind;
    private TabView mBtnMine;
    //    private List<Fragment> mFragment=new ArrayList<>();
    private SparseArray<TabFragment> mFragment = new SparseArray<>();
    private List<TabView> mTabs = new ArrayList<>();
    private static final String BUNDLE_KEY_POS = "bundle_key_pos";
private int mCurTabPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        if (savedInstanceState != null) {
            mCurTabPos= savedInstanceState.getInt(BUNDLE_KEY_POS,0);
        }
        initViews();
        initViewPager();
        initListen();

    }

    private void initViews() {
        mVpMain = findViewById(R.id.vp_main);
        mBtnWeChat = findViewById(R.id.tab_wechat);
        mBtnFriend = findViewById(R.id.tab_friend);
        mBtnFind = findViewById(R.id.tab_find);
        mBtnMine = findViewById(R.id.tab_mine);
        mBtnWeChat.setIconAndText(R.drawable.menu_msg_default, R.drawable.menu_msg_selected, "微信");
        mBtnFriend.setIconAndText(R.drawable.menu_contact_default, R.drawable.menu_contact_selected, "通讯录");
        mBtnFind.setIconAndText(R.drawable.menu_find_default, R.drawable.menu_find_selected, "发现");
        mBtnMine.setIconAndText(R.drawable.menu_me_default, R.drawable.menu_me_selected, "我");
        mBtnWeChat.setProgress(1);
        //顺序不要出错了
        mTabs.add(mBtnWeChat);
        mTabs.add(mBtnFriend);
        mTabs.add(mBtnFind);
        mTabs.add(mBtnMine);



        setCurrentTab(mCurTabPos);


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
//                if (position == 0) {
//                    fragment.setOnTitleClickListener(new TabFragment.OnTitleClickListener() {
//                        @Override
//                        public void onClick(String title) {
//                            changeWeChatTab(title);
//                        }
//                    });
//                }
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
                if (positionOffset > 0) {

                    TabView left = mTabs.get(position);
                    TabView right = mTabs.get(position + 1);


                    left.setProgress(1 - positionOffset);
                    right.setProgress(positionOffset);
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
                    tabFragment.changeTitle("微信 change");
                }
            }
        });


        for (int i = 0; i < mTabs.size(); i++) {
            TabView tabView = mTabs.get(i);
            final int finalI = i;
            tabView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVpMain.setCurrentItem(finalI, false);
                    setCurrentTab(finalI);
                }
            });
        }
    }


    private void setCurrentTab(int pos) {
        for (int i = 0; i < mTabs.size(); i++) {
            TabView tabView = mTabs.get(i);
            if (i == pos) {
                tabView.setProgress(1);
            } else {
                tabView.setProgress(0);
            }
        }
    }

    //屏幕旋转处理  tab不能够记住状态
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_KEY_POS, mVpMain.getCurrentItem());
        super.onSaveInstanceState(outState);

    }
}
