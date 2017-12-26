package com.hao.common.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class CenterDrawableTextView extends TextView {

	private boolean check = false;/* 选中的状态 */

	public CenterDrawableTextView(Context context) {
		super(context);
	}

	public CenterDrawableTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CenterDrawableTextView(Context context, AttributeSet attrs,
								  int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	/* 重绘，实现DrawableRight,DrawableLeft与文本一起居中显示 */
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawables[] = getCompoundDrawables();/* 得到资源数据 */
		if (drawables != null) {
			Drawable drawableRight = drawables[2];/* 得到drawableRight资源 */
			Drawable drawableLeft = drawables[0];/* 得到drawableLeft资源 */
			float textWidth = getPaint().measureText(getText().toString());/* 得到text的宽度 */
			int drawablePadding = getCompoundDrawablePadding();/* 得到填充的值 */
			if (drawableRight != null) {
				int drawableWidth = drawableRight.getIntrinsicWidth();/* 得到资源的宽度 */
				float bodyWidth = textWidth + drawableWidth + drawablePadding;
				setPadding(0, 0, (int) (getWidth() - bodyWidth), 0);/*
																	 * 设置控件内容的位置，
																	 * 关键方法
																	 */
				canvas.translate((getWidth() - bodyWidth) / 2, 0);/* 原点偏移的方法 */
			} else if (drawableLeft != null) {
				int drawableWidth = drawableLeft.getIntrinsicWidth();
				float bodyWidth = textWidth + drawableWidth + drawablePadding;
				setPadding(0, 0, (int) (getWidth() - bodyWidth), 0);/*
																	 * 设置控件内容的位置，
																	 * 关键方法
																	 */
				canvas.translate((getWidth() - bodyWidth) / 2, 0);
			}
		}
		super.onDraw(canvas);
	}

}
