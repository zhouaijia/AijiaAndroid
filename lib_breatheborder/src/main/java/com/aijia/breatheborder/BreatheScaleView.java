package com.aijia.breatheborder;

import static android.animation.ValueAnimator.INFINITE;
import static android.animation.ValueAnimator.REVERSE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.Nullable;

/**
 * 呼吸阴影view

 * Author：Aijia
 * Date：2024.3.16
 */
final class BreatheScaleView extends View {
    private Paint mBorderPaint;
    private Path mPath;
    private float mBorderWidth = pt2px(2);
    private int mBorderColor = Color.WHITE;
    private int mBreatheDuration = 4000;
    private AnimatorSet mAnimatorSet;
    private boolean isBreathe = true;
    private boolean isBorder = true;
    private float mTopLeftRadius;
    private float mTopRightRadius;
    private float mBottomLeftRadius;
    private float mBottomRightRadius;

    public BreatheScaleView(Context context) {
        this(context, null);
    }

    public BreatheScaleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreatheScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 需禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPath = new Path();

        mBorderPaint = new Paint();
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setMaskFilter(new BlurMaskFilter(0.5f, BlurMaskFilter.Blur.NORMAL));
        setVisibility(GONE);
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();  // 假设我们想要设置的宽度为10dp
        int height = getMeasuredHeight(); // 假设我们想要设置的高度为10dp

        int padding = dpToPx(getContext(), 5);//5dp
        // 将期望的宽和高转换为像素
        int desiredWidth = width + padding;
        int desiredHeight = height + padding;

        // 设置最终的尺寸
        //setMeasuredDimension(desiredWidth, desiredHeight);
        setMeasuredDimension(resolveSize(desiredWidth, widthMeasureSpec), resolveSize(desiredHeight, heightMeasureSpec));
    }*/
    public static int dpToPx(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float)dp * density);
    }
    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        float padding = dpToPx(getContext(), 5);
        RectF shadowRectF = new RectF(padding, padding, width - padding, height - padding);
        final Path path = new Path();
        final float[] shadowRadius = new float[]{
                mTopLeftRadius, mTopLeftRadius,
                mTopRightRadius, mTopRightRadius,
                mBottomRightRadius, mBottomRightRadius,
                mBottomLeftRadius, mBottomLeftRadius};
        if (mTopLeftRadius != 0 || mTopRightRadius != 0 || mBottomLeftRadius != 0 || mBottomRightRadius != 0) {
            path.addRoundRect(shadowRectF, shadowRadius, Path.Direction.CW);
        } else {
            path.addRoundRect(shadowRectF, 0, 0, Path.Direction.CW);
        }
        mPath = path;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isBorder) {
            canvas.save();
            canvas.drawPath(mPath, mBorderPaint);
            canvas.restore();
        }
    }

    private void createAnimatorSet() {
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playSequentially(getScaleAnimator());
    }

    private ObjectAnimator getScaleAnimator() {
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(
                this,
                PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.04f, 1.0f),
                PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.05f, 1.0f)
        );
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(mBreatheDuration);
        animator.setRepeatMode(REVERSE);
        animator.setRepeatCount(INFINITE);
        //animator.start();
        return animator;
    }

    public void start() {
        setVisibility(VISIBLE);
        if (isBreathe) {
            if (null != mAnimatorSet) {
                mAnimatorSet.cancel();
            }
            createAnimatorSet();
            mAnimatorSet.start();
        } else {
            setAlpha(1f);
        }
    }

    public void stop() {
        setVisibility(GONE);
        if (isBreathe) {
            if (null != mAnimatorSet) {
                mAnimatorSet.cancel();
            }
        }
    }

    public void setBorderWidth(float mBorderWidth) {
        this.mBorderWidth = mBorderWidth;
        if (mBorderPaint != null) {
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }
    }

    public void setTopLeftRadius(float mTopLeftRadius) {
        this.mTopLeftRadius = mTopLeftRadius;
    }

    public void setTopRightRadius(float mTopRightRadius) {
        this.mTopRightRadius = mTopRightRadius;
    }

    public void setBottomLeftRadius(float mBottomLeftRadius) {
        this.mBottomLeftRadius = mBottomLeftRadius;
    }

    public void setBottomRightRadius(float mBottomRightRadius) {
        this.mBottomRightRadius = mBottomRightRadius;
    }

    public void setBorder(boolean border) {
        isBorder = border;
    }

    public void setBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
        if (mBorderPaint != null) {
            mBorderPaint.setColor(mBorderColor);
        }
    }

    public void setBreatheDuration(int mBreatheDuration) {
        this.mBreatheDuration = mBreatheDuration;
    }

    public void setBreathe(boolean breathe) {
        isBreathe = breathe;
    }

    private float pt2px(float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PT, value, getResources().getDisplayMetrics()) + 0.5f;
    }
}
