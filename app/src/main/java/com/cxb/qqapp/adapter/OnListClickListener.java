package com.cxb.qqapp.adapter;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * RecyclerView Adapter 的点击回调
 */

public interface OnListClickListener {

    @IntDef({BUTTON, TEXTVIEW, LINEARLAYOUT, LONGCLICK})
    @Retention(RetentionPolicy.SOURCE)
    @interface ItemView {
    }

    int BUTTON = 0;
    int TEXTVIEW = 1;
    int LINEARLAYOUT = 2;
    int LONGCLICK = 3;

    //item点击事件
    void onItemClick(int position);

    //可根据tag来区分点击的是item内部哪个控件
    void onTagClick(@ItemView int tag, int position);
}
