package com.hao.common.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hao.common.R;

/**
 * @author linguoding
 * @Title: TitleLinearLayout.java
 * @Package com.fz.community.widget
 * @Description: 带标题的linearLayout
 * @Company:广州分众信息科技有限公司
 * @date 2015-5-15 上午10:18:16
 * @version V1.0
 */
public class TitleLinearLayout extends LinearLayout {
	private TextView titleView;
	private String titleText;
	private int titleColor;

	public TitleLinearLayout(Context context) {
		super(context);
		initTitleView(context);
	}

	public TitleLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.titleLyaout);
		titleText = array.getString(R.styleable.titleLyaout_titleText);
		titleColor = array.getColor(R.styleable.titleLyaout_titleColor,
				getResources().getColor(R.color.textPrimary));
		array.recycle();// 释放资源
		initTitleView(context);
	}

	private void initTitleView(Context context) {
		titleView = new TextView(context);
		titleView.setText(titleText);
		titleView.setBackgroundColor(titleColor);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		titleView.setPadding(getpxFromPd(context, 5), getpxFromPd(context, 5),
				0, 0);
		this.addView(titleView, layoutParams);
	}

	/** 将输入的数据转化为dp */
	private static int getpxFromPd(Context context, float dip) {
		return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, context.getResources().getDisplayMetrics()) + 0.5f);
	}
}
