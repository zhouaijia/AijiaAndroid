package com.aijia.breatheborder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

public class BreathingButton extends Button {

    private Paint paint;
    private float borderWidth = 5f;
    private int borderColor = Color.BLUE;
    private float progress = 0f;
    private boolean isAnimating = false;

    public BreathingButton(Context context) {
        super(context);
        init();
    }

    public BreathingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BreathingButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                startBreathing();
            }
        }, 1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isAnimating) {
            progress += 0.05f;
            if (progress > 1f) {
                progress = 1f;
                isAnimating = false;
            }
            invalidate();
        }
        float strokeWidth = borderWidth * (1f - progress);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
    }

    public void startBreathing() {
        isAnimating = true;
        progress = 0f;
    }

    public void stopBreathing() {
        isAnimating = false;
        progress = 1f;
        invalidate();
    }
}
