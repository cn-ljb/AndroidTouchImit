package com.ljb.android.touch;

import java.util.ArrayList;
import java.util.List;

public class ViewGroup extends View {

    List<View> childList = new ArrayList<>();
    private View[] mChildren = new View[0];
    private TouchNode mFirstTouchNode = null;

    public ViewGroup(int left, int top, int right, int bottom) {
        super(left, top, right, bottom);
    }

    public void addView(View view) {
        if (view == null) return;
        childList.add(view);
        mChildren = childList.toArray(new View[childList.size()]);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        System.out.println(getName() + " ViewGroup.dispatchTouchEvent() ");

        int action = event.getAction();
        boolean intercept = onInterceptTouchEvent(event);
        boolean handle = false;
        TouchNode newTouchNode = null;
        if (action != MotionEvent.ACTION_CANCEL && !intercept) {
            if (action == MotionEvent.ACTION_DOWN) {
                final View[] children = mChildren;
                //源码中会确定Z轴，这里暂不考虑,简化为只考虑x,y
                for (int i = children.length - 1; i >= 0; i--) {
                    View child = children[i];
                    if (!child.isContain(event.getX(), event.getY())) {
                        continue;
                    }
                    //子View可接收事件，分发下去
                    if (dispatchTransformedTouchEvent(event, child)) {
                        handle = true;
                        newTouchNode = addTouchNode(child);
                        break;
                    }
                }
            }
        }
        //这个判断很重要，父容器传递子View之后，就靠这里往自己身上传，由于View是嵌套关系，所以达到了往上层传的目的
        if (newTouchNode == null) {
            //没有子View消费事件，那么就调用自身的onTouchEvent，看是否处理事件
            dispatchTransformedTouchEvent(event, null);
        }
        return handle;
    }

    /**
     *  这个函数用来表示事件继续向下分发，会出现2种情况
     *  1：存在子View传递事件到子View的dispatchTouchEvent里，继续分发
     *  2：已经没有子View可分发，向上传递到View类中dispatchTouchEvent()事件中
     */
    private boolean dispatchTransformedTouchEvent(MotionEvent event, View child) {
        boolean handled;
        if (child != null) {
            handled = child.dispatchTouchEvent(event);
        } else {
            handled = super.dispatchTouchEvent(event);
        }
        return handled;
    }

    private TouchNode addTouchNode(View child) {
        TouchNode node = TouchNode.obtain(child);
        node.next = mFirstTouchNode;
        mFirstTouchNode = node;
        return node;
    }

    private boolean onInterceptTouchEvent(MotionEvent event) {
        System.out.println(getName() + " ViewGroup.onInterceptTouchEvent() ");
        return false;
    }


    private boolean onTouchEvent(MotionEvent event) {
        System.out.println(getName() + " ViewGroup.onTouchEvent() ");
        return false;
    }

    //缓存节点（责任链设计模式）
    private static final class TouchNode {
        private static Object object = new Object[0];
        private static TouchNode sRecycleBin;
        private static int sRecycleSize;
        private View child;
        private TouchNode next;

        //从缓存中取一个节点使用
        public static TouchNode obtain(View child) {
            TouchNode node;
            synchronized (object) {
                if (sRecycleBin == null) {
                    node = new TouchNode();
                } else {
                    node = sRecycleBin;
                }
                sRecycleBin = node.next;
                node.next = null;
                sRecycleSize--;
            }
            node.child = child;
            return node;
        }

        //回收缓存节点
        public void recycle() {
            if (child == null) {
                throw new IllegalStateException("节点已回收过了");
            }
            synchronized (object) {
                if (sRecycleSize < 32) {
                    next = sRecycleBin;
                    sRecycleSize++;
                    sRecycleBin = this;
                } else {
                    next = null;
                }
                child = null;
            }
        }
    }
}
