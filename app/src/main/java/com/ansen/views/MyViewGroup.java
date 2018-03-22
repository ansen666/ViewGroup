package com.ansen.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获得此ViewGroup计算模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        //获取ViewGroup宽高
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        measureChildren(widthMeasureSpec,heightMeasureSpec);//测量所有子View的宽高

        if(getChildCount()<=0){//如果没有子View 当前ViewGroup的宽高直接设置为0
            setMeasuredDimension(0,0);
        }if(heightMode==MeasureSpec.AT_MOST&&widthMode==MeasureSpec.AT_MOST){//宽跟高是wrap_content
            int measuredWidth=0;//测量宽度
            int maxMeasuredHeigh=0;//测量高度子View最大的高度
            for(int i=0;i<getChildCount();i++){
                View child=getChildAt(i);


                MyLayoutParams lp= (MyLayoutParams) child.getLayoutParams();
                measuredWidth+=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;//加上左右边距

                if(child.getMeasuredHeight()>maxMeasuredHeigh){//如果当前的View大于之后View的高度
                    maxMeasuredHeigh=child.getMeasuredHeight();
                }
            }
            setMeasuredDimension(measuredWidth,maxMeasuredHeigh);
        }else{
            setMeasuredDimension(sizeWidth,sizeHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i("ansen","onLayout");
        int count=getChildCount();//获取子View数量
        int left=0;//上一个View到ViewGroup的宽度距离

        for(int i=0;i<count;i++){
            View child=getChildAt(i);

            MyLayoutParams lp= (MyLayoutParams) child.getLayoutParams();

            int childWidth=child.getMeasuredWidth();//获取子View宽度
            int childHeight=child.getMeasuredHeight();//获取子View高度

            if(lp.position==MyLayoutParams.POSITION_RIGHT){//当前子View显示在ViewGroup右边
                child.layout(getWidth()-childWidth,0,getWidth(),childHeight);//设置摆放位置
            }else if(lp.position==MyLayoutParams.POSITION_BOTTOM){////当前子View显示在ViewGroup底部
                child.layout(left+lp.leftMargin,getHeight()-childHeight,left+childWidth+lp.leftMargin,getHeight());
            }else{//没有设置位置的View
                child.layout(left+lp.leftMargin,0,left+childWidth+lp.leftMargin,child.getMeasuredHeight());
            }

            Log.i("ansen","left:"+left+" top:"+0+" right:"+(left+childWidth)+" bottom:"+childHeight);
            left+=childWidth+lp.leftMargin+lp.rightMargin;
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs){
        return new MyLayoutParams(getContext(), attrs);
    }

    public static class MyLayoutParams extends MarginLayoutParams {
        public static int POSITION_RIGHT = 1;//右边
        public static int POSITION_BOTTOM = 2;//底部

        public int position = -1;//

        public MyLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CustomLayoutLP);
            position = a.getInt(R.styleable.CustomLayoutLP_layout_position, position);
            a.recycle();
        }

        public MyLayoutParams(int width, int height) {
            super(width, height);
        }

        public MyLayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
