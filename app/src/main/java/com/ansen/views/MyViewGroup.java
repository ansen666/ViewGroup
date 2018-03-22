package com.ansen.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ansen.viewgroup.R;

/**
 * 自定义ViewGroup
 * @author ansen
 * @create time 2018/3/22
 */
public class MyViewGroup extends ViewGroup{
    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count=getChildCount();
        Log.i("ansen","count:"+count);
        int left=0;

        for(int i=0;i<count;i++){
            View child=getChildAt(i);

            LayoutParams lp= (LayoutParams) child.getLayoutParams();

            int childWidth=child.getMeasuredWidth();
            int childHeight=child.getMeasuredHeight();

            if(lp.position==LayoutParams.POSITION_RIGHT){//当前子View显示在ViewGroup右边
                child.layout(getWidth()-childWidth,0,getWidth(),childHeight);
            }else if(lp.position==LayoutParams.POSITION_BOTTOM){////当前子View显示在ViewGroup底部
                child.layout(left+lp.leftMargin,getHeight()-childHeight,left+childWidth+lp.leftMargin,getHeight());
            }else{
                child.layout(left+lp.leftMargin,0,left+childWidth+lp.leftMargin,child.getMeasuredHeight());
            }

            Log.i("ansen","left:"+left+" top:"+0+" right:"+(left+childWidth)+" bottom:"+childHeight);

            left+=childWidth+lp.leftMargin+lp.rightMargin;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec,heightMeasureSpec);

        if(getChildCount()<=0){//如果没有子View 当前ViewGroup的宽高直接设置为0
            setMeasuredDimension(0,0);
        }if(heightMode==MeasureSpec.AT_MOST&&widthMode==MeasureSpec.AT_MOST){//宽或者高是wrap_content
            int measuredWidth=0;//测量宽度
            int maxMeasuredHeigh=0;//测量高度子View最大的高度
            for(int i=0;i<getChildCount();i++){
                View child=getChildAt(i);
                LayoutParams lp= (LayoutParams) child.getLayoutParams();
                measuredWidth+=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;

                if(child.getMeasuredHeight()>maxMeasuredHeigh){
                    maxMeasuredHeigh=child.getMeasuredHeight();
                }
            }
            setMeasuredDimension(measuredWidth,maxMeasuredHeigh);
        }else{
            Log.i("ansen","测量宽度:"+sizeWidth+" 测量高度:"+sizeHeight);
            setMeasuredDimension(sizeWidth,sizeHeight);
        }
    }
    
    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs){
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends MarginLayoutParams {
        public static int POSITION_RIGHT = 1;
        public static int POSITION_BOTTOM = 2;

        public int position = -1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CustomLayoutLP);
            position = a.getInt(R.styleable.CustomLayoutLP_layout_position, position);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
