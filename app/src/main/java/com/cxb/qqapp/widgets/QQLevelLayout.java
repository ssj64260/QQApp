package com.cxb.qqapp.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cxb.qqapp.R;
import com.cxb.qqapp.utils.DisplayUtil;

/**
 * QQ等级控件
 */

public class QQLevelLayout extends LinearLayout {

    private static final int CROWN_LEVEL = 64;
    private static final int SUN_LEVEL = 16;
    private static final int MOON_LEVEL = 4;
    private static final int START_LEVEL = 1;

    private final int iconWidth = 14;
    private final int iconHeight = 14;

    private Context context;

    public QQLevelLayout(Context context) {
        this(context, null, 0);
    }

    public QQLevelLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQLevelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void setLevel(int level) {
        if (level < 1 || level > 255) {
            throw new IllegalAccessError("level must be (0,255]");
        }

        int curNum = level;

        int crownNum = curNum / CROWN_LEVEL;
        curNum = curNum % CROWN_LEVEL;

        int sunNum = curNum / SUN_LEVEL;
        curNum = curNum % SUN_LEVEL;

        int moonNum = curNum / MOON_LEVEL;
        curNum = curNum % MOON_LEVEL;

        int starNum = curNum;

        int width = DisplayUtil.dip2px(iconWidth);
        int height = DisplayUtil.dip2px(iconHeight);
        LayoutParams params = new LinearLayout.LayoutParams(width, height);

        for (int i = 0; i < crownNum; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.drawable.ic_qq_level_crown);
            imageView.setLayoutParams(params);
            addView(imageView);
        }

        for (int i = 0; i < sunNum; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.drawable.ic_qq_level_sun);
            imageView.setLayoutParams(params);
            addView(imageView);
        }

        for (int i = 0; i < moonNum; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.drawable.ic_qq_level_moon);
            imageView.setLayoutParams(params);
            addView(imageView);
        }

        for (int i = 0; i < starNum; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.drawable.ic_qq_level_star);
            imageView.setLayoutParams(params);
            addView(imageView);
        }

        invalidate();

    }

}
