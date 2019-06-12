package com.wechatui.view.transformer;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created on 2019/5/30 15:50
 *
 * @auther super果
 * @annotation
 */
public class ScaleTransformer implements ViewPager.PageTransformer {
    private static final String TAG = "ScaleTransformer";

    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_ALPHA = 0.5f;


    @Override
    public void transformPage(View page, float position) {
        Log.e(TAG, page.getTag() + ",position:" + position);


        //a->b
        //a,position:(0,-1)
        //b,postion:(1,0)

        //b->a
        //a,positon:(-1,0)
        //b,position:(0,1)

        //[负无穷,-1] 页面滑到外面的时候 会走
        if (position < -1) {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            page.setAlpha(MIN_ALPHA);
            //[-1,1]
        } else if (position <= 1) {
//针对左边的页面
            if (position < 0) {

                //a->b  依赖a,position:(0,-1)
                //[1,0.75f]
                float scaleA=MIN_SCALE+(1-MIN_SCALE)*(1+position);
                page.setScaleX(scaleA);
                page.setScaleY(scaleA);


                //[1，0.5f]
                float alphaA=MIN_ALPHA+(1-MIN_ALPHA)*(1+position);
                page.setAlpha(alphaA);


                //b->a  依赖a,positon:(-1,0)
                //[0.75f,1]

                //代码跟上面是一致的！


            } else {//针对右边的页面
                //a->b  依赖b,postion:(1,0)
                //[0.75f,1]
                float scaleB=MIN_SCALE+(1-MIN_SCALE)*(1-position);
                page.setScaleX(scaleB);
                page.setScaleY(scaleB);


                //[0.5f,1]
                float alphaB=MIN_ALPHA+(1-MIN_ALPHA)*(1-position);
                page.setAlpha(alphaB);



                //b->a  依赖b,position:(0,1)
                //[1,0.75f]
                //代码跟上面是一致的！
            }

            //[1,正无穷]
        } else {
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
            page.setAlpha(MIN_ALPHA);
        }
    }
}
