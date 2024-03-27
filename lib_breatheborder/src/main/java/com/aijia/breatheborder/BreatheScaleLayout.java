package com.aijia.breatheborder;

import static android.graphics.Canvas.ALL_SAVE_FLAG;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * 结合compile 'me.jessyan:autosize:1.1.2'适配使用
 *
 * @author Aijia
 * @date 2024.03.18
 */
public class BreatheScaleLayout extends FrameLayout implements View.OnFocusChangeListener {
    protected long mAnimDuration = 300;
    private boolean mBringToFront = true;
    private boolean mIsParent = false;
    protected RectF mFrameRectF;
    private ViewTreeObserver.OnPreDrawListener startAnimationPreDrawListener;
    private BreatheScaleView scaleView;
    private float mRadius = 0;
    private float mShadowWidth = pt2px(10);
    private float mBorderWidth = pt2px(10);
    private int mBorderColor = Color.WHITE;
    private int mShadowColor = Color.WHITE;
    private int mBreatheDuration = 4000;
    private boolean mIsBreathe = true;
    private boolean mIsBorder = false;

    private float mTopLeftRadius;
    private float mTopRightRadius;
    private float mBottomLeftRadius;
    private float mBottomRightRadius;

    private boolean mIsDrawRound;
    private RectF mRefreshRectF;

    private boolean mIsDrawn;

    public BreatheScaleLayout(Context context) {
        this(context, null);
    }

    public BreatheScaleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreatheScaleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setWillNotDraw(false);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ShimmerShadowLayout, 0, 0);
        try {
            mAnimDuration = a.getInteger(R.styleable.ShimmerShadowLayout_mAnimDuration, 300);
            mBringToFront = a.getBoolean(R.styleable.ShimmerShadowLayout_mBringToFront, false);
            mIsParent = a.getBoolean(R.styleable.ShimmerShadowLayout_mIsParent, false);

            mShadowWidth = a.getDimension(R.styleable.ShimmerShadowLayout_mShadowWidth, pt2px(10));
            mBorderWidth = a.getDimension(R.styleable.ShimmerShadowLayout_mBorderWidth, pt2px(2));
            mShadowColor = a.getColor(R.styleable.ShimmerShadowLayout_mShadowColor, Color.WHITE);
            mBorderColor = a.getColor(R.styleable.ShimmerShadowLayout_mBorderColor, Color.WHITE);
            mBreatheDuration = a.getInteger(R.styleable.ShimmerShadowLayout_mBreatheDuration, 4000);
            mIsBreathe = a.getBoolean(R.styleable.ShimmerShadowLayout_mIsBreathe, true);
            mIsBorder = a.getBoolean(R.styleable.ShimmerShadowLayout_mIsBorder, true);

            mRadius = a.getDimension(R.styleable.ShimmerShadowLayout_mRadius, 0);
            mTopLeftRadius = a.getDimension(R.styleable.ShimmerShadowLayout_mTopLeftRadius, mRadius);
            mTopRightRadius = a.getDimension(R.styleable.ShimmerShadowLayout_mTopRightRadius, mRadius);
            mBottomLeftRadius = a.getDimension(R.styleable.ShimmerShadowLayout_mBottomLeftRadius, mRadius);
            mBottomRightRadius = a.getDimension(R.styleable.ShimmerShadowLayout_mBottomRightRadius, mRadius);
        } finally {
            a.recycle();
        }
        if (!mIsParent) {
            setOnFocusChangeListener(this);
        }
        //关闭硬件加速
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mFrameRectF = new RectF();
        mIsDrawRound = mTopLeftRadius != 0 || mTopRightRadius != 0 || mBottomLeftRadius != 0 || mBottomRightRadius != 0;

        postDelayed(new Runnable() {//test
            @Override
            public void run() {
                startAnimation();
            }
        }, 1000);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        int width = getMeasuredWidth();  // 假设我们想要设置的宽度为10dp
//        int height = getMeasuredHeight(); // 假设我们想要设置的高度为10dp
//
//        int padding = dpToPx(getContext(), 5);//5dp
//        // 将期望的宽和高转换为像素
//        int desiredWidth = width + padding;
//        int desiredHeight = height + padding;
//
//        // 设置最终的尺寸
//        //setMeasuredDimension(desiredWidth, desiredHeight);
//        setMeasuredDimension(resolveSize(desiredWidth, widthMeasureSpec), resolveSize(desiredHeight, heightMeasureSpec));
//    }

    public static int dpToPx(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        int newWidth = (int) (width + mBorderWidth*5);
        int newHeight = (int) (height + mBorderWidth*5);
        super.onSizeChanged(width, height, oldw, oldh);
        initBreatheScaleView(width, height);

        mFrameRectF.set(getPaddingLeft() + mShadowWidth + mBorderWidth / 2, getPaddingTop() + mShadowWidth + mBorderWidth / 2,
                width - getPaddingRight() - mShadowWidth - mBorderWidth / 2, height - getPaddingBottom() - mShadowWidth - mBorderWidth / 2);
        Path path = new Path();
        final float[] shimmerRadius = new float[]{
                mTopLeftRadius, mTopLeftRadius,
                mTopRightRadius, mTopRightRadius,
                mBottomRightRadius, mBottomRightRadius,
                mBottomLeftRadius, mBottomLeftRadius};
        if (mTopLeftRadius != 0 || mTopRightRadius != 0 || mBottomLeftRadius != 0 || mBottomRightRadius != 0) {
            path.addRoundRect(mFrameRectF, shimmerRadius, Path.Direction.CW);
        } else {
            path.addRoundRect(mFrameRectF, 0, 0, Path.Direction.CW);
        }
        if ((height != oldw || height != oldh) && mIsDrawRound) {
            mRefreshRectF = new RectF(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());
        }
    }

    private void initBreatheScaleView(int width, int height) {
        if (scaleView == null) {
            scaleView = new BreatheScaleView(getContext());
            scaleView.setBorderWidth(mBorderWidth);
            scaleView.setBorderColor(Color.BLUE);
            scaleView.setBreatheDuration(mBreatheDuration);
            scaleView.setBreathe(mIsBreathe);
            scaleView.setBorder(mIsBorder);
            scaleView.setTopLeftRadius(6);
            scaleView.setTopRightRadius(6);
            scaleView.setBottomLeftRadius(6);
            scaleView.setBottomRightRadius(6);
            LayoutParams layoutParams = new LayoutParams(width, height);
            addView(scaleView, layoutParams);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mIsDrawn || !mIsDrawRound) {
            super.dispatchDraw(canvas);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.saveLayer(mRefreshRectF, null);
            } else {
                canvas.saveLayer(mRefreshRectF, null, ALL_SAVE_FLAG);
            }
            super.dispatchDraw(canvas);
            canvas.restore();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (!mIsDrawRound) {
            super.draw(canvas);
        } else {
            mIsDrawn = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.saveLayer(mRefreshRectF, null);
            } else {
                canvas.saveLayer(mRefreshRectF, null, ALL_SAVE_FLAG);
            }
            super.draw(canvas);
            canvas.restore();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stopAnimation();
        super.onDetachedFromWindow();
    }

    public void startAnimation() {
        if (getWidth() == 0) {
            startAnimationPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    startAnimation();
                    return true;
                }
            };
            getViewTreeObserver().addOnPreDrawListener(startAnimationPreDrawListener);
            return;
        }
        if (scaleView != null) {
            scaleView.start();
        }
        setSelected(true);
    }

    public void stopAnimation() {
        if (startAnimationPreDrawListener != null) {
            getViewTreeObserver().removeOnPreDrawListener(startAnimationPreDrawListener);
        }
        if (scaleView != null) {
            scaleView.stop();
        }
        setSelected(false);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (mBringToFront) {
                v.bringToFront();
            }
            v.setSelected(true);
            startAnimation();
        } else {
            v.setSelected(false);
            stopAnimation();
        }
    }

    private float pt2px(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, getResources().getDisplayMetrics()) + 0.5f;
    }
}
