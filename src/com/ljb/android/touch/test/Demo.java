package com.ljb.android.touch.test;

import com.ljb.android.touch.MotionEvent;
import com.ljb.android.touch.View;
import com.ljb.android.touch.ViewGroup;
import com.ljb.android.touch.listener.OnClickListener;
import com.ljb.android.touch.listener.OnLongClickListener;

public class Demo {

    public static void main(String[] args) {
        //初始化布局
        ViewGroup decorView = new ViewGroup(0, 0, 1080, 1920);
        decorView.setName("DecorView");
        ViewGroup layoutView = new ViewGroup(0, 0, 800, 800);
        layoutView.setName("LayoutView");
        View tvView = new View(0, 0, 100, 100);
        tvView.setName("TextView");
        View imgView = new View(100, 100, 200, 200);
        imgView.setName("ImageView");

        layoutView.addView(tvView);
        layoutView.addView(imgView);
        decorView.addView(layoutView);

        //消费事件测试
        tvView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println(view.getName() + " View.onLongClick() ");
                return false;
            }
        });

        tvView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(view.getName() + " View.onClick() ");
            }
        });

        imgView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(view.getName() + " View.onClick() ");
            }
        });

        //模拟点击事件,遵循Android事件传递规则，从外层往内传
        // MotionEvent event = new MotionEvent(50,50);
        MotionEvent event = new MotionEvent(150, 150);
        event.setAction(MotionEvent.ACTION_DOWN);
        decorView.dispatchTouchEvent(event);

    }
}
