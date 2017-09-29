package com.hao.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Package com.hao.common.widget
 * @作 用:VIBE自定义view
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年08月30日  11:46
 */


public class VibeProgressbarView extends View {
    private Paint mPaint;


    public VibeProgressbarView(Context context) {
        this(context, null);
    }

    public VibeProgressbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
