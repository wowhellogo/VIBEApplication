package com.vibe.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;



/**
 * @Package com.hao.common.widget
 * @作 用:VIBE自定义view,y=Asin(1/ωx+φ)+k  A越大，振幅越大，ω越大，就越宽，φ控制左右平移，k上下平移
 * @创 建 人: linguoding 邮箱：linggoudingg@gmail.com
 * @日 期: 2017年08月30日  11:46
 */
public class VibeProgressbarView extends View {
    private Context mContext;
    private int type;//形态
    private final int DEFAULT_ABOVE_WAVE_COLOR = Color.BLUE;//高位波浪默认的颜色
    private final int DEFAULT_BLOW_WAVE_COLOR = Color.RED;//低位波浪默认的颜色
    private final int DEFAULT_ABOVE_RECT_COLOR = Color.YELLOW;//高位矩形默认的颜色
    private final int DEFAULT_BLOW_RECT_COLOR = Color.CYAN;//低位矩形默认的颜色
    private final int DEFAULT_NUM_TEXT_COLOR = Color.GRAY;//数字默认的颜色
    private final int DEFAULT_LINE_COLOR = Color.GRAY;//背景线默认的颜色
    private final int DEFAULT_PROGRESS = 5;
    protected static final int RECT = 1;//矩形
    protected static final int WAVE = 2;//波浪
    private Paint mNumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//数字的画笔
    private Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//背景线的画笔
    private Paint mAboveWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//高位波浪的画笔
    private Paint mBlowWavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//低位波浪的画笔
    private Paint mAboveRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//高位矩形画笔
    private Paint mBlowRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//低位矩形画笔
    private Path mPath = new Path();//x,y值路径
    private int mAboveWaveColor;//高位波浪颜色
    private int mBlowWaveColor;//低位波浪颜色
    private int mAboveRectColor;//高位矩形颜色
    private int mBlowRectColor;//低位矩形颜色

    private float mAboveHeight;//高位当前进度对应的高度

    private int mNumTextSize;//数字的字体大小
    private int mNumTextColor;//数字的字体颜色
    private int mNumTextWidth;//数字的字体宽度
    private int mNumTextHeight;//数字的字体高度
    private int mStartNum;//开始进度值
    private int mEndNum;//结束进度值

    private int mProgress;//进度值
    private int mMinProgress = 2;//最低进度值
    private float mMinProgressHeight;//最低进度对应的高度

    private int mMaxAmplitude = 100;//最大波浪振幅
    private int mAmplitude;//当前波浪振幅

    private int mPeakValleyCount = 4;//波浪的峰和谷数量
    private int mPeakValleyWidth;//波浪的峰和谷的宽度
    private int mIntervalWidth;//矩形间隔的宽度
    private int mIntervalHeight;//矩形间隔的高度
    private int mIntervalRow = 10;//行间隔线的条数


    private int mIntervalCol = 2;//列间隔线的条数
    private int mIntervalSize;//矩形间隔大小
    public OnChangNumProgressListener mOnChangNumProgressListener;
    private int mContentWidth;//控件有效内容宽度
    private int mContentHeight;//控件有效内容高度
    private int mPaddingLeft, mPaddingRight, mPaddingTop, mPaddingBottom;//内边距
    private int mLineColor;//背景线颜色

    public VibeProgressbarView(Context context) {
        super(context);
        init(null, 0);
    }

    public VibeProgressbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public VibeProgressbarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        this.mContext = getContext();
        final TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.VibeProgressbarView, defStyle, 0);
        mAboveWaveColor = attributes.getColor(R.styleable.VibeProgressbarView_aboveWaveColor, DEFAULT_ABOVE_WAVE_COLOR);
        mAboveRectColor = attributes.getColor(R.styleable.VibeProgressbarView_aboveRectColor, DEFAULT_ABOVE_RECT_COLOR);
        mBlowWaveColor = attributes.getColor(R.styleable.VibeProgressbarView_blowWaveColor, DEFAULT_BLOW_WAVE_COLOR);
        mBlowRectColor = attributes.getColor(R.styleable.VibeProgressbarView_blowRectColor, DEFAULT_BLOW_RECT_COLOR);
        mProgress = attributes.getInt(R.styleable.VibeProgressbarView_progress, DEFAULT_PROGRESS);
        mStartNum = attributes.getInt(R.styleable.VibeProgressbarView_startNum, 0);
        mEndNum = attributes.getInt(R.styleable.VibeProgressbarView_endNum, 100);
        mPeakValleyCount=attributes.getInt(R.styleable.VibeProgressbarView_peakValleyCount,4);
        type = attributes.getInt(R.styleable.VibeProgressbarView_type, WAVE);
        mNumTextSize = (int) attributes.getDimension(R.styleable.VibeProgressbarView_numTextSize, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        mNumTextColor = attributes.getColor(R.styleable.VibeProgressbarView_numTextColor, DEFAULT_NUM_TEXT_COLOR);
        mLineColor = attributes.getColor(R.styleable.VibeProgressbarView_lineColor, DEFAULT_LINE_COLOR);
        attributes.recycle();


        mNumPaint.setAntiAlias(true);
        mNumPaint.setTextSize(mNumTextSize);
        mNumPaint.setColor(mNumTextColor);

        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mLineColor);

        mAboveWavePaint.setAntiAlias(true);
        mAboveWavePaint.setColor(mAboveWaveColor);
        mAboveWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mBlowWavePaint.setAntiAlias(true);
        mBlowWavePaint.setColor(mBlowWaveColor);
        mBlowWavePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mAboveRectPaint.setAntiAlias(true);
        mAboveRectPaint.setColor(mAboveRectColor);
        mAboveRectPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mBlowRectPaint.setAntiAlias(true);
        mBlowRectPaint.setColor(mBlowRectColor);
        mBlowRectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mContentWidth == 0 || mContentHeight == 0) {
            updateAllValue();
        }
        drawBackgroundLine(canvas);
        if (type == WAVE) {
            drawAboveWave(canvas);
            drawBlowWave(canvas);
        } else if (type == RECT) {
            drawAboveRect(canvas);
            drawBlowWaveRect(canvas);
        }
        drawNumText(canvas);
    }

    /**
     * 画低位矩形
     *
     * @param canvas
     */
    private void drawBlowWaveRect(Canvas canvas) {
        mPath.reset();
        float moveY = mContentHeight - mMinProgressHeight - mNumTextHeight / 2;//计算从屏幕顶端开始移动的高度
        moveY = (mContentHeight - moveY) * 0.5f + moveY;//因为低位矩形比高位矩形高度要低，所以有：(mContentHeight - moveY) * 0.5f
        mPath.moveTo(0, moveY);
        int x = mPeakValleyWidth / 4;//矩形的宽度
        float dx = x * 0.2f;//高位矩形与低位矩形之间的距离
        float y = (mContentHeight - mAboveHeight) * 0.5f + mAboveHeight;
        for (int i = 1; i <= mPeakValleyCount * 2; i++) {
            int x1 = x * (i * 2 + 1);//计算线条开始的x坐标
            if (i % 2 == 1) {
                if (i == 1) {
                    mPath.lineTo(x + dx, moveY);
                    mPath.lineTo(x + dx, y);
                }
                mPath.lineTo(x1 - dx, y);
                mPath.lineTo(x1 - dx, moveY);
            } else {
                mPath.lineTo(x1 + dx, moveY);
                mPath.lineTo(x1 + dx, y);
            }
        }
//            RectF oval = new RectF(mPeakValleyWidth / 2, mAboveHeight, mPeakValleyWidth, mAboveHeight + 100);// 设置个新的长方形
//            mPath.arcTo(oval, 0, 360, false);

//            canvas.drawRoundRect(oval3, 20, 15, mAboveRectPaint);//第二个参数是x半径，第三个参数是y半径
//            mPath.quadTo(100, 0, 100, -100);

        mPath.lineTo(mContentWidth, mContentHeight);
        mPath.lineTo(0, mContentHeight);
        mPath.close();
        canvas.drawPath(mPath, mBlowRectPaint);
    }

    /**
     * 画高位矩形
     *
     * @param canvas
     */
    private void drawAboveRect(Canvas canvas) {
        mPath.reset();
        float moveY = mContentHeight - mMinProgressHeight - mNumTextHeight / 2;//计算从屏幕顶端开始移动的高度
        mPath.moveTo(0, moveY);
        int x = mPeakValleyWidth / 4;//矩形的宽度
        for (int i = 1; i <= mPeakValleyCount * 2; i++) {
            int x1 = x * (i * 2 + 1);//计算线条开始的x坐标
            if (i % 2 == 1) {
                if (i == 1) {
                    mPath.lineTo(x * i, moveY);
                    mPath.lineTo(x * i, mAboveHeight);
                }
                mPath.lineTo(x1, mAboveHeight);
                mPath.lineTo(x1, moveY);
            } else {
                mPath.lineTo(x1, moveY);
                mPath.lineTo(x1, mAboveHeight);
            }
        }
//            RectF oval = new RectF(mPeakValleyWidth / 2, mAboveHeight, mPeakValleyWidth, mAboveHeight + 100);// 设置个新的长方形
//            mPath.arcTo(oval, 0, 360, false);

//            canvas.drawRoundRect(oval3, 20, 15, mAboveRectPaint);//第二个参数是x半径，第三个参数是y半径
//            mPath.quadTo(100, 0, 100, -100);

        mPath.lineTo(mContentWidth, mContentHeight);
        mPath.lineTo(0, mContentHeight);
        mPath.close();
        canvas.drawPath(mPath, mAboveRectPaint);
    }

    /**
     * 画低位波浪
     *
     * @param canvas
     */
    private void drawBlowWave(Canvas canvas) {
        mPath.reset();
        float moveY = (mContentHeight - mAboveHeight) * 0.5f + mAboveHeight;//因为低位矩形比高位矩形高度要低，所以有：(mContentHeight - mAboveHeight) * 0.5f
        mPath.moveTo(0, moveY);
        for (int i = 0; i < mPeakValleyCount; i++) {
            int dy1 = i % 2 == 0 ? -mAmplitude : mAmplitude;//判断是峰还是谷
            mPath.rQuadTo(mPeakValleyWidth / 2, dy1, mPeakValleyWidth, 0);
        }
        mPath.lineTo(mContentWidth, mContentHeight);
        mPath.lineTo(0, mContentHeight);
        mPath.close();
        canvas.drawPath(mPath, mBlowWavePaint);
    }

    /**
     * 画高位波浪
     *
     * @param canvas
     */
    private void drawAboveWave(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(0, mAboveHeight);
        for (int i = 0; i < mPeakValleyCount; i++) {
            int dy1 = i % 2 == 0 ? -mAmplitude : mAmplitude;//判断是峰还是谷
            mPath.rQuadTo(mPeakValleyWidth / 2, dy1, mPeakValleyWidth, 0);
        }
        mPath.lineTo(mContentWidth, mContentHeight);
        mPath.lineTo(0, mContentHeight);
        mPath.close();
        canvas.drawPath(mPath, mAboveWavePaint);
    }

    /**
     * 画数字
     *
     * @param canvas
     */
    private void drawNumText(Canvas canvas) {
        for (int i = 0; i < mIntervalRow; i++) {
            String numText = String.valueOf((i + 1) * mIntervalSize);
            float textWidth = mNumPaint.measureText(numText);
            float x = (mPaddingLeft + 20 + mNumTextWidth) / 2 - textWidth / 2;
            float y = mPaddingTop + (mIntervalHeight * (mIntervalRow - i)) + mNumTextHeight * 2;
            canvas.drawText(numText, x, y, mNumPaint);
        }
    }

    /**
     * 画背景线
     *
     * @param canvas
     */
    private void drawBackgroundLine(Canvas canvas) {
        //画横线
        for (int i = 0; i < mIntervalRow; i++) {
            float x = mPaddingLeft + 20 + mNumTextWidth;
            float y = mPaddingTop + (mIntervalHeight * (mIntervalRow - i)) + mNumTextHeight;
            canvas.drawLine(x, y, mContentWidth - 20 - mNumTextWidth, y, mLinePaint);
        }

        //画竖线
        for (int i = 1; i <= mIntervalCol; i++) {
            float x = mPaddingLeft + mNumTextWidth + i * mIntervalWidth;
            float startY = mContentHeight - mIntervalHeight * mIntervalRow;
            canvas.drawLine(x, startY, x, mContentHeight, mLinePaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        updateAllValue();
    }

    /**
     * 计算有效内容的宽高
     */
    private void updateAllValue() {
        Log.i("VibeProgressbarView", "updateAllValue");
        mPaddingLeft = getPaddingLeft();
        mPaddingTop = getPaddingTop();
        mPaddingRight = getPaddingRight();
        mPaddingBottom = getPaddingBottom();

        mContentWidth = getWidth() - mPaddingLeft - mPaddingRight;
        mContentHeight = getHeight() - mPaddingTop - mPaddingBottom;

        mIntervalWidth = (mContentWidth - 20 - mNumTextWidth) / (mIntervalCol + 1);
        mIntervalHeight = mContentHeight / (mIntervalRow + 1);
        mIntervalSize = (mEndNum - mStartNum) / mIntervalRow;

        mPeakValleyWidth = mContentWidth / mPeakValleyCount;

        mAmplitude = mMaxAmplitude - (100 - mProgress);
        Log.i("VibeProgressbarView", "mMaxAmplitude=" + mAmplitude + "****mProgress=" + mProgress);

        float progressHeight = (float) mProgress * mIntervalHeight / (float) mIntervalRow;
        mAboveHeight = mContentHeight - progressHeight - mNumTextHeight / 2;
        if (type == WAVE) {
            //矩形图没有峰值，因此波浪图才加上当前波浪振幅
            mAboveHeight += mAmplitude / 2;
        }
        mNumTextWidth = (int) mNumPaint.measureText(String.valueOf(mEndNum));
        mNumTextHeight = (int) mNumPaint.getFontMetrics().bottom;

        mMinProgressHeight = mMinProgress * mIntervalHeight / (float) mIntervalRow;//计算最低进度对应的高度


        postInvalidate();
    }

    public int getStartNum() {
        return mStartNum;
    }

    public void setStartNum(int startNum) {
        this.mStartNum = startNum;
        updateAllValue();
    }

    public int getEndNum() {
        return mEndNum;
    }

    public void setEndNum(int endNum) {
        this.mEndNum = endNum;
        updateAllValue();
    }

    public int getProgress() {
        return mProgress;
    }

    public void setprogress(int progress) {
        this.mProgress = progress;
        updateAllValue();
    }

    private float downX;
    private float downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - downY;
                downX = x;
                downY = y;
                float progressHeight = mContentHeight - mAboveHeight - mNumTextHeight / 2;
                if (type == WAVE) {
                    //矩形图没有峰值，因此波浪图才加上当前波浪振幅
                    progressHeight += mAmplitude / 2;
                }
                progressHeight -= dy;
                int progress = (int) (progressHeight * mIntervalRow / mIntervalHeight);
                if (progress < mMinProgress || progress > 100) {
                    break;
                }
                mProgress = progress;
                float num = (mEndNum - mStartNum) * progress / 100f;
                if (mOnChangNumProgressListener != null) {
                    mOnChangNumProgressListener.onChangNumProgress(num, progress);
                }
                Log.i(this.getClass().getSimpleName(), "dy=" + dy + "***mProgress=" + mProgress + "*****num=" + num);
                updateAllValue();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    public void setOnChangNumProgressListener(OnChangNumProgressListener onChangNumProgressListener) {
        mOnChangNumProgressListener = onChangNumProgressListener;
    }

    /**
     * 监听进度的回调接口
     */
    public interface OnChangNumProgressListener {

        void onChangNumProgress(float num, int progress);
    }
}
