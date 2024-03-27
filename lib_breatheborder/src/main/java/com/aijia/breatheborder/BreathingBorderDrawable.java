package com.aijia.breatheborder;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class BreathingBorderDrawable extends Drawable {
    private final Paint paint;
    private final float borderWidth;
    private final int centerColor;
    private final int edgeColor;
    private final float radius;
    private float currentAlpha = 255;

    public BreathingBorderDrawable(Context context, AttributeSet attrs) {
        Resources resources = context.getResources();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth = resources.getDisplayMetrics().density * 4);
        paint.setAntiAlias(true);
        centerColor = Color.TRANSPARENT;
        edgeColor = Color.RED; // 初始边框颜色

        // 假设TextView是正方形的，你可以根据需要调整
        radius = resources.getDisplayMetrics().density * 20;

    }

    @Override
    public void draw(Canvas canvas) {
        int width = getBounds().width();
        int height = getBounds().height();
        paint.setColor(Color.argb((int) currentAlpha, Color.red(edgeColor), Color.green(edgeColor), Color.blue(edgeColor)));
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);

        // 绘制内部透明圆形
        paint.setColor(centerColor);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        // 忽略alpha设置，因为我们自己管理透明度
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void startBreathingAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(255, 0, 255);
        animator.addUpdateListener(animation -> {
            currentAlpha = (int) animation.getAnimatedValue();
            invalidateSelf(); // 通知Drawable需要重新绘制
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(1000); // 设置动画持续时间，例如1秒
        animator.start();
    }
}
