package com.cxb.qqapp.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.cxb.qqapp.R;
import com.cxb.qqapp.utils.DisplayUtil;


/**
 * 侧滑菜单
 */

public class MySlidingMenu extends RelativeLayout {

    private Scroller mScroller;

    private boolean isOpen = false;//是否打开菜单
    private boolean haveShelter;//是否有遮蔽层

    private int mScreenWidth;//屏幕宽度
    private int mScreenHeight;//屏幕高度
    private int mMenuRightPadding;

    private View mMenu;//菜单view
    private View mContent;//主页view
    private View shelter;//遮蔽层

    private int mMenuWidth;
    private int mContentWidth;

    private final int spacePx = 75;//菜单右侧空位宽度
    private float shortTouchWidth = 5;//根据手机分辨率来决定 短触摸长度
    private final float backgroundAlpha = 0.6f;//遮蔽层透明度

    private int mLastX;//手指开始点击X坐标
    private int mLastY;//手指开始点击Y坐标
    private int dx = 0;

    private int mLastXIntercept = -1;
    private int mLastYIntercept = -1;

    public MySlidingMenu(Context context) {
        this(context, null, 0);
    }

    public MySlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MySlidingMenu);
        haveShelter = ta.getBoolean(R.styleable.MySlidingMenu_shelter, true);
        ta.recycle();

        shortTouchWidth = DisplayUtil.getMetrics(context).density;

        mScroller = new Scroller(context);

        mScreenWidth = DisplayUtil.getScreenWidth(context);
        mScreenHeight = DisplayUtil.getScreenHeight(context);
        //设置Menu距离屏幕右侧的距离，convertToDp是将代码中的100转换成100dp
        mMenuRightPadding = DisplayUtil.convertToDp(context, spacePx);

        if (haveShelter) {
            shelter = new View(context);
            shelter.setBackgroundResource(R.color.black);
            shelter.setAlpha(0f);
            shelter.setVisibility(GONE);
            shelter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeMenu();
                }
            });
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mMenu = getChildAt(0);
        mContent = getChildAt(1);
        mContent.setOnClickListener(null);

        mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth - mMenuRightPadding;
        mContentWidth = mContent.getLayoutParams().width = mScreenWidth;

        measureChild(mMenu, widthMeasureSpec, heightMeasureSpec);
        measureChild(mContent, widthMeasureSpec, heightMeasureSpec);

        if (haveShelter) {
            if (getChildCount() == 2) {
                addView(shelter);
            }
            shelter.getLayoutParams().width = mScreenWidth;
            shelter.getLayoutParams().height = mScreenHeight;
            measureChild(shelter, widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(mMenuWidth + mContentWidth, mScreenHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mMenu.layout(-mMenuWidth, 0, 0, mScreenHeight);
        mContent.layout(0, 0, mScreenWidth, mScreenHeight);
        if (haveShelter) {
            shelter.layout(0, 0, mScreenWidth, mScreenHeight);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int currentX = (int) event.getX();
                int currentY = (int) event.getY();
                dx = currentX - mLastX;

                if (dx < 0) {//向左滑动
                    if (getScrollX() + Math.abs(dx) >= 0) {
                        scrollTo(0, 0);
                    } else {
                        scrollBy(-dx, 0);
                    }
                } else {//向右滑动
                    if (getScrollX() - dx <= -mMenuWidth) {
                        scrollTo(-mMenuWidth, 0);
                    } else {
                        scrollBy(-dx, 0);
                    }
                }
                mLastX = currentX;
                mLastY = currentY;

                //设置页面交错偏移量
                mMenu.setTranslationX(2 * (mMenuWidth + getScrollX()) / 3);

//                Log.d("有个APP - dx", String.valueOf(dx));
//                Log.d("有个APP - scrollx", getScrollX() + "   " + (-mScreenWidth / 2));
                break;
            case MotionEvent.ACTION_UP:
                if (dx > shortTouchWidth) {
                    openMenu();
                    return true;
                } else if (dx < -shortTouchWidth) {
                    closeMenu();
                    return true;
                }

                if (getScrollX() < -mScreenWidth / 2) {
                    openMenu();
                } else {
                    closeMenu();
                }

                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (Math.abs(deltaX) > Math.abs(deltaY) * 2 && Math.abs(deltaX) > shortTouchWidth) {//横向滑动
                    intercept = true;
                } else {//纵向滑动
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        mLastX = x;
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;

//        Log.d("有个APP - intercept", intercept + "   ");
        return intercept;
    }

    @Override
    public void computeScroll() {
//        Log.d("有个APP - x", mScroller.getCurrX() + "   ");
//        Log.d("有个APP - getScrollX", getScrollX() + "   ");
//        Log.d("有个APP - Offset", mScroller.computeScrollOffset() + "   ");
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();

            //设置页面交错偏移量
            mMenu.setTranslationX(2 * (mMenuWidth + getScrollX()) / 3);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

//        Log.d("有个APP - x", l + "   ");
        if (haveShelter) {
            shelter.setAlpha(backgroundAlpha * Math.abs(l) / mContentWidth);
            if (l == 0) {
                shelter.setVisibility(GONE);
            } else {
                shelter.setVisibility(VISIBLE);
            }
        }
    }

    public void toggleMenu() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    private void closeMenu() {
//        Log.d("有个APP - closeMenu", getScrollX() + "   " + (-getScrollX()));
        mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 500);
        invalidate();
        isOpen = false;
    }

    private void openMenu() {
//        Log.d("有个APP - openMenu", getScrollX() + "   " + (-mMenuWidth - getScrollX()));
        if (haveShelter) {
            shelter.setVisibility(VISIBLE);
        }
        mScroller.startScroll(getScrollX(), 0, -mMenuWidth - getScrollX(), 0, 500);
        invalidate();
        isOpen = true;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
