package com.wechatui.view.transformer;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created on 2019/5/31 09:38
 *
 * @auther super果
 * @annotation
 */
public class RotateTransformer implements ViewPager.PageTransformer {

    private static final int MAX_ROTATE = 15;

    @Override
    public void transformPage(View page, float position) {
        //a->b
        //a,position:(0,-1)
        //b,postion:(1,0)

        //b->a
        //a,positon:(-1,0)
        //b,position:(0,1)

        //[负无穷,-1] 页面滑到外面的时候 会走
        if (position < -1) {
            page.setRotation(-MAX_ROTATE );
            page.setPivotY(page.getHeight());
            page.setPivotX(page.getWidth());
            //[-1,1]
        } else if (position <= 1) {
//针对左边的页面
            if (position < 0) {
                page.setPivotY(page.getHeight());
                //a->b
                //a,position:(0,-1)
                //0.5w,w
                page.setPivotX(0.5f * page.getWidth() + 0.5f * (-position) * page.getWidth());
//(0,-1)->0,-max
                page.setRotation(MAX_ROTATE * position);
            } else {//针对右边的页面
                //a->b
                //b,postion:(1,0)

                page.setPivotY(page.getHeight());
                //0,0.5w
                page.setPivotX( page.getWidth() * 0.5f * (1-position));

                page.setRotation(MAX_ROTATE * position);
            }

            //[1,正无穷]
        } else {
            page.setRotation(MAX_ROTATE );
            page.setPivotY(page.getHeight());
            page.setPivotX(0);
        }
    }
}
