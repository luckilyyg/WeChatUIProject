package com.wechatui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wechatui.R;


/**
 * Created on 2019/5/29 17:31
 *
 * @auther super果
 * @annotation
 */
public class TabFragment extends Fragment {
    private TextView mTvTitle;
    private String mTitle;
    private static final String BUNDLE_KEY_TITLE = "key_title";

    public static interface OnTitleClickListener {
        void onClick(String title);
    }

    private OnTitleClickListener mListener;

    //写一个对外公布的方法
    public void setOnTitleClickListener(OnTitleClickListener listener) {
        mListener = listener;
    }

    public static TabFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_TITLE, title);
        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mTitle = arguments.getString(BUNDLE_KEY_TITLE, "");
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvTitle.setText(mTitle);


        mTvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//获取activity对象
                //写法一：易奔溃
//                MainActivity activity= (MainActivity) getActivity();
//                activity.changeWeChatTab("微信 changed");

                //写法二：
//                Activity activity=getActivity();
//                if (activity instanceof  MainActivity){
//                    ((MainActivity)activity).changeWeChatTab("微信 changed");
//                }
                //问题在于：我们Fragment会触发一些事件，Activity去响应这些事件
                if (mListener != null) {
                    mListener.onClick("微信 changed");
                }

            }
        });

    }

    //activity调用fragment方法
    public void changeTitle(String title) {
//开发者容易忽略的一点
        if (!isAdded()) {
            return;
        }
        mTvTitle.setText(title);
    }
}
