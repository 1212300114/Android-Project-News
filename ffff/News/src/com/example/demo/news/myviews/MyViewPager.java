package com.example.demo.news.myviews;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.example.demo.news.activity.MainActivity;

public class MyViewPager extends ViewPager {
    //viewpager主要想处理事件分发然
    private MainActivity activity = null;

    public MyViewPager(Context context) {
        super(context);

        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
        }
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
        }
    }

    PointF downPoint = new PointF();
    OnSingleTouchListener onSingleTouchListener;

    @Override
    public boolean onTouchEvent(MotionEvent evt) {
        switch (evt.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录按下时�?�的坐标
                downPoint.x = evt.getX();
                downPoint.y = evt.getY();
                if (this.getChildCount() > 1) { // 有内容，多于1个时
                    // 通知其父控件，现在进行的是本控件的操作，不允许拦�?
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.getChildCount() > 1) { // 有内容，多于1个时
                    // 通知其父控件，现在进行的是本控件的操作，不允许拦�?
                }
                break;
            case MotionEvent.ACTION_UP:
                // 在up时判断是否按下和松手的坐标为�?个点
                break;
        }
        return super.onTouchEvent(evt);
    }


    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean dispatchDragEvent(DragEvent event) {
        return true;
    }

    public void onSingleTouch(View v) {
        if (onSingleTouchListener != null) {
            onSingleTouchListener.onSingleTouch(v);
        }
    }

    public interface OnSingleTouchListener {
        public void onSingleTouch(View v);
    }

    public void setOnSingleTouchListener(
            OnSingleTouchListener onSingleTouchListener) {
        this.onSingleTouchListener = onSingleTouchListener;
    }
}