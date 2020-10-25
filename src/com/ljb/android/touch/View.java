package com.ljb.android.touch;

import com.ljb.android.touch.listener.OnClickListener;
import com.ljb.android.touch.listener.OnLongClickListener;
import com.ljb.android.touch.listener.OnTouchListener;

public class View {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int left, top, right, bottom;

    private OnTouchListener onTouchListener;
    private OnClickListener onClickListener;
    private OnLongClickListener onLongClickListener;


    public View() {
    }

    public View(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public boolean isContain(int x, int y) {
        return x > left && x < right && y > top && y < bottom;
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        System.out.println(name + " View.dispatchTouchEvent() ");

        boolean result = false;
        if (onTouchListener != null && onTouchListener.onTouch(this, event)) {
            result = true;
        }

        if (!result && onTouchEvent(event)) {
            result = true;
        }
        return result;
    }

    private boolean onTouchEvent(MotionEvent event) {
        System.out.println(name + " View.onTouchEvent() ");

        if (onLongClickListener != null && onLongClickListener.onLongClick(this)) {
            return true;
        }

        if (onClickListener != null) {
            onClickListener.onClick(this);
            return true;
        }
        return false;
    }


}
