package com.aijia.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.BatteryManager;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

public class BatteryView extends View implements LifecycleObserver {
    // 电池方向
    private BatteryViewOrientation orientation;
    // 电池边框与内部电量的间隔
    private float borderPadding;
    // 电池边框宽度
    private float borderWidth;
    // 边框颜色
    private int borderColor;
    // 边框圆角
    private float radis;

    private float headerWidth;
    //电池头部与外边框之间的间距
    private float spaceHeaderAndBorder;

    // 电池电量及对应的颜色
    // 低电量 默认值 0%-10% 红色
    private int lowColor;
    private int lowValue;
    // 中等电量 默认值 11%-20% 黄色
    private int mediumColor;
    private int mediumValue;
    // 高电量 默认值 21%-100% 白色
    private int highColor;
    private int headerColor;
    private int currentPower = 60; // 当前电量

    // 未充电时高电量颜色
    private int noChargingHighColor;

    //是否正在充电
    private boolean isCharging;
    private int chargingSpeed;
    private boolean chargingAnim;

    private int width;
    private int height;
    private Context mContext;

    private Runnable runnable;

    private Lifecycle mLifecycle;
    private OnBatteryPowerListener mOnBatteryPowerListener;

    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        getAttrs(context, attrs);
        initPaints();
    }

    private void initBattery() {
        try {
            BatteryManager manager = (BatteryManager) mContext.getSystemService(Context.BATTERY_SERVICE);
            if(Build.VERSION.SDK_INT >= 21 && manager != null) {
//              int property_charge_counter = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
//              int property_current_average = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);
//              int property_current_now = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
                //当前电量百分比
                currentPower = manager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BatteryView);
        // 默认水平头朝右
        int orientationInt = ta.getInt(R.styleable.BatteryView_bv_orientation, 1);
        switch (orientationInt) {
            case 0:
                orientation = BatteryViewOrientation.HORIZONTAL_LEFT;
                break;
            case 1:
                orientation = BatteryViewOrientation.HORIZONTAL_RIGHT;
                break;
            case 2:
                orientation = BatteryViewOrientation.VERTICAL_TOP;
                break;
            case 3:
                orientation = BatteryViewOrientation.VERTICAL_BOTTOM;
                break;
        }
        // 电池内边距
        borderPadding = ta.getDimensionPixelSize(R.styleable.BatteryView_bv_border_padding, 2);
        // 电池边框厚度
        borderWidth = ta.getDimensionPixelSize(R.styleable.BatteryView_bv_border_width, 2);
        headerWidth = ta.getDimensionPixelSize(R.styleable.BatteryView_bv_header_width, 10);
        spaceHeaderAndBorder = borderWidth / 2;
        // 电池边框圆角
        radis = ta.getDimensionPixelSize(R.styleable.BatteryView_bv_radius, 2);
        // 电池边框颜色
        borderColor = ta.getColor(R.styleable.BatteryView_bv_border_color, Color.WHITE);

        // 电池实心部分
        lowColor = ta.getColor(R.styleable.BatteryView_bv_power_color_low, mContext.getResources().getColor(R.color.low));
        lowValue = ta.getInt(R.styleable.BatteryView_bv_power_value_low, 10);
        mediumColor = ta.getColor(R.styleable.BatteryView_bv_power_color_medium, mContext.getResources().getColor(R.color.medium));
        mediumValue = ta.getInt(R.styleable.BatteryView_bv_power_value_medium, 20);
        highColor = ta.getColor(R.styleable.BatteryView_bv_power_color_high, mContext.getResources().getColor(R.color.high));
        headerColor = ta.getColor(R.styleable.BatteryView_bv_header_color, Color.WHITE);
        noChargingHighColor = ta.getColor(R.styleable.BatteryView_bv_no_charging_color_high, mContext.getResources().getColor(R.color.high));
        chargingSpeed = ta.getInt(R.styleable.BatteryView_bv_charging_speed, 2) % 10;
        if(chargingSpeed == 0) chargingSpeed = 1;

        chargingAnim = ta.getBoolean(R.styleable.BatteryView_bv_charging_anim, true);

        ta.recycle();
    }

    private Paint borderPaint;
    private Paint powerPaint;
    private Paint headerPaint;

    // 初始化画笔
    private void initPaints() {
        //
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(borderColor);
        borderPaint.setStrokeWidth(borderWidth);
        // 电量的实心部分
        powerPaint = new Paint();
        powerPaint.setAntiAlias(true);
        powerPaint.setStyle(Paint.Style.FILL);
        powerPaint.setColor(highColor);
        powerPaint.setStrokeWidth(0);
        // 电池头部
        headerPaint = new Paint();
        headerPaint.setAntiAlias(true);
        headerPaint.setStyle(Paint.Style.FILL);
        headerPaint.setColor(headerColor);
        headerPaint.setStrokeWidth(0);
    }

    public void setLifecycleOwner(@NonNull LifecycleOwner owner) {
        if (mLifecycle != null) mLifecycle.removeObserver(this);
        mLifecycle = owner.getLifecycle();
        mLifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void registerPower() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mContext.registerReceiver(receiver, filter);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void unregisterPower() {
        mContext.unregisterReceiver(receiver);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    private RectF borderRf;
    private RectF powerRf;
    private RectF headerRf;

    @Override
    protected void onDraw(Canvas canvas) {
        if(orientation == BatteryViewOrientation.HORIZONTAL_LEFT){
            drawHorizontalLeft(canvas);
        }else if(orientation == BatteryViewOrientation.HORIZONTAL_RIGHT){
            drawHorizontalRight(canvas);
        }else if (orientation == BatteryViewOrientation.VERTICAL_TOP){
            drawVerticalTop(canvas);
        }else if (orientation == BatteryViewOrientation.VERTICAL_BOTTOM){
            drawVerticalBottom(canvas);
        }
    }

    // 朝左的电池
    private void drawHorizontalLeft(Canvas canvas) {
        // 绘制边框
        if (borderRf == null) {
            //因为绘制的边框是有宽度的，所以绘制的起点必须加上边框宽度，也就是borderWidth
            borderRf = new RectF(headerWidth + spaceHeaderAndBorder + borderWidth, borderWidth,
                    width - borderWidth, height - borderWidth);
        }
        canvas.drawRoundRect(borderRf, radis, radis, borderPaint);

        // 绘制实心区域
        if (powerRf == null) {
            initBattery();
            setPower(currentPower);
        }
        canvas.drawRoundRect(powerRf, radis, radis, powerPaint);

        //绘制充电时的闪电图标
        if (isCharging && !chargingAnim) {
            Bitmap bitmap = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.lightning)).getBitmap();
            float bitmapWidth = bitmap.getWidth();
            RectF dstRectF = new RectF();
            dstRectF.left = + headerWidth + (width - headerWidth - bitmapWidth) / 2.0f;
            dstRectF.right = dstRectF.left + bitmapWidth;
            dstRectF.top = borderRf.top;
            dstRectF.bottom = borderRf.bottom;
            canvas.drawBitmap(bitmap, null, dstRectF, powerPaint);
        }

        // 绘制电池头
        if (headerRf == null) {
            float headerHeight = height / 3f;
            headerRf = new RectF(0, headerHeight, headerWidth, headerHeight * 2);
        }
        canvas.drawRoundRect(headerRf, radis, radis, headerPaint);
    }
    // 朝右的电池
    private void drawHorizontalRight(Canvas canvas) {
        // 绘制边框
        if (borderRf == null) {
            borderRf = new RectF(borderWidth, borderWidth,
                    width - headerWidth - spaceHeaderAndBorder - borderWidth,
                    height - borderWidth);
        }
        canvas.drawRoundRect(borderRf, radis, radis, borderPaint);

        // 绘制实心区域
        if (powerRf == null) {
            initBattery();
            setPower(currentPower);
        }
        canvas.drawRoundRect(powerRf, radis, radis, powerPaint);

        //绘制充电时的闪电图标
        if (isCharging && !chargingAnim) {
            Bitmap bitmap = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.lightning)).getBitmap();
            float bitmapWidth = bitmap.getWidth();
            RectF dstRectF = new RectF();
            dstRectF.left = (width - headerWidth - bitmapWidth) / 2.0f;
            dstRectF.right = dstRectF.left + bitmapWidth;
            dstRectF.top = borderRf.top;
            dstRectF.bottom = borderRf.bottom;
            canvas.drawBitmap(bitmap, null, dstRectF, powerPaint);
        }

        // 绘制电池头
        if (headerRf == null) {
            float headerHeight = height / 3f;
            headerRf = new RectF(width - headerWidth, headerHeight, width, headerHeight * 2);
        }
        canvas.drawRoundRect(headerRf, radis, radis, headerPaint);
    }

    // 电池横向的宽度
    private float getHorizontalWidth(int power) {
        // 满电量宽度
        float fullWidth = width - borderWidth * 2 - borderPadding * 2 - borderPadding - spaceHeaderAndBorder;
        return fullWidth * power / 100f;
    }

    // 电池头朝上
    private void drawVerticalTop(Canvas canvas) {
        // 绘制边框
        if (borderRf == null) {
            borderRf = new RectF(borderWidth, headerWidth + spaceHeaderAndBorder + borderWidth,
                    width - borderWidth, height - borderWidth);
        }
        canvas.drawRoundRect(borderRf, radis, radis, borderPaint);

        // 绘制实心区域
        if (powerRf == null) {
            initBattery();
            setPower(currentPower);
        }
        canvas.drawRoundRect(powerRf, radis, radis, powerPaint);

        //绘制充电时的闪电图标
        if (isCharging && !chargingAnim) {
            Bitmap bitmap = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.lightning)).getBitmap();
            float bitmapHeight = bitmap.getHeight();
            RectF dstRectF = new RectF();
            dstRectF.left = borderRf.left;
            dstRectF.right = borderRf.right;
            dstRectF.top = (height - headerWidth - bitmapHeight) / 2.0f;
            dstRectF.bottom = dstRectF.top + bitmapHeight;
            canvas.drawBitmap(bitmap, null, dstRectF, powerPaint);
        }

        // 绘制电池头
        if (headerRf == null) {
            float headerWidth1 = width / 3f;
            headerRf = new RectF(headerWidth1, 0, headerWidth1 * 2, headerWidth);
        }
        canvas.drawRoundRect(headerRf, radis, radis, headerPaint);
    }

    // 电池头朝下
    private void drawVerticalBottom(Canvas canvas) {
        // 绘制边框
        if (borderRf == null) {
            borderRf = new RectF(borderWidth, borderWidth,
                    width - borderWidth,
                    height - headerWidth - spaceHeaderAndBorder - borderWidth);
        }
        canvas.drawRoundRect(borderRf, radis, radis, borderPaint);
        // 绘制实心区域
        if (powerRf == null) {
            initBattery();
            setPower(currentPower);
        }
        canvas.drawRoundRect(powerRf, radis, radis, powerPaint);

        //绘制充电时的闪电图标
        if (isCharging && !chargingAnim) {
            Bitmap bitmap = ((BitmapDrawable)mContext.getResources().getDrawable(R.mipmap.lightning)).getBitmap();
            float bitmapHeight = bitmap.getHeight();
            RectF dstRectF = new RectF();
            dstRectF.left = borderRf.left;
            dstRectF.right = borderRf.right;
            dstRectF.top = (height - headerWidth - spaceHeaderAndBorder - bitmapHeight) / 2.0f;
            dstRectF.bottom = dstRectF.top + bitmapHeight;
            canvas.drawBitmap(bitmap, null, dstRectF, powerPaint);
        }

        // 绘制电池头
        if (headerRf == null) {
            float headerWidth1 = width / 3f;
            headerRf = new RectF(headerWidth1, height - headerWidth,
                    headerWidth1 * 2, height);
        }
        canvas.drawRoundRect(headerRf, radis, radis, headerPaint);
    }

    private float getVerticalHeight(int power){
        // 满电量宽度
        float fullHeight = height - borderWidth * 2 - borderPadding * 2 - headerWidth - spaceHeaderAndBorder;
        return fullHeight * power / 100.0f;
    }

    public void setPower(int power) {
        if(mOnBatteryPowerListener != null) {
            mOnBatteryPowerListener.onPower(currentPower);
        }

        if (power <= lowValue) {
            powerPaint.setColor(lowColor);
        } else if (power < mediumValue) {
            powerPaint.setColor(mediumColor);
        } else {
            if(runnable == null) {
                powerPaint.setColor(noChargingHighColor);
            } else {
                powerPaint.setColor(highColor);
            }
        }

        if (orientation == BatteryViewOrientation.HORIZONTAL_RIGHT) {
            float realWidth = getHorizontalWidth(power);
            powerRf = new RectF(borderWidth + borderPadding, borderWidth + borderPadding,
                    borderWidth + borderPadding + realWidth, height - borderWidth - borderPadding);
        } else if (orientation == BatteryViewOrientation.HORIZONTAL_LEFT) {
            float realWidth = getHorizontalWidth(power);
            powerRf = new RectF(width-borderWidth-borderPadding-realWidth, borderWidth + borderPadding,
                    width - borderWidth - borderPadding, height - borderWidth - borderPadding);
        } else if (orientation == BatteryViewOrientation.VERTICAL_TOP) {
            float realHeight = getVerticalHeight(power);
            powerRf = new RectF(borderWidth + borderPadding, height - borderWidth - borderPadding - realHeight,
                    width - borderWidth - borderPadding, height - borderWidth - borderPadding);
        } else if (orientation == BatteryViewOrientation.VERTICAL_BOTTOM) {
            float realHeight = getVerticalHeight(power);
            powerRf = new RectF(borderWidth + borderPadding, borderWidth + borderPadding,
                    width - borderWidth - borderPadding, borderWidth + borderPadding + realHeight);
        }

        postInvalidate();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //电池剩余电量
            int power = intent.getIntExtra("level", 0);
            Log.i("电池剩余电量power", power + "");
            //获取电池状态
            int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
            if (status == BatteryManager.BATTERY_STATUS_CHARGING && power < 100) {
                isCharging = true;
                if (chargingAnim) {
                    startCharge();
                    currentPower = power;
                } else {
                    currentPower = 100;
                }
            } else {
                isCharging = false;
                stopCharge();
                currentPower = power;
            }

            if (powerRf != null && runnable == null) {
                setPower(currentPower);
            }

//          int scale = intent.getIntExtra("scale", 0);               //获取电池满电量数值
//          String technology = intent.getStringExtra("technology");  //获取电池技术支持
//          int plugged = intent.getIntExtra("plugged", 0);           //获取电源信息
//          int health = intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN); //获取电池健康度
//          int voltage = intent.getIntExtra("voltage", 0);           //获取电池电压
//          int temperature = intent.getIntExtra("temperature", 0);   //获取电池温度

        }
    };

    private int power;

    // 充电动态显示
    private void startCharge() {
        if (runnable != null) return;
        power = currentPower;
        runnable = new Runnable() {
            @Override
            public void run() {
                power %= 100;
                setPower(power);
                power += chargingSpeed;
                //延迟执行
                BatteryView.this.postDelayed(this, 200);
            }
        };
        post(runnable);
    }

    private void stopCharge() {
        if (runnable != null) {
            removeCallbacks(runnable);
            runnable = null;
        }
    }

    public void setOnBatteryPowerListener(OnBatteryPowerListener onBatteryPowerListener){
        mOnBatteryPowerListener = onBatteryPowerListener;
    }
    public void removeOnBatteryPowerListener(){
        mOnBatteryPowerListener = null;
    }

    /**
     * 充电动画速度
     * @param speed 1-9之前的数值
     */
    public void setChargingSpeed(int speed){
        this.chargingSpeed = speed;
    }

}
