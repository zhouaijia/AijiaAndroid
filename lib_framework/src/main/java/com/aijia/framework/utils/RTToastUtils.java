package com.aijia.framework.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 可控制短时间显示大量的toast
 */
public class RTToastUtils {
    private static String mOldMsg;
    private static Toast mToast = null;
    private static long mOneTime = 0;
    private static long mTwoTime = 0;

    public static void showToast(Context context, final String s) {
        int toastMarginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                64, context.getResources().getDisplayMetrics());
        if (mToast == null) {
            mToast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.BOTTOM, 0, toastMarginBottom);
            mToast.show();
            mOneTime = System.currentTimeMillis();
        } else {
            mToast.setGravity(Gravity.BOTTOM, 0, toastMarginBottom);
            mTwoTime = System.currentTimeMillis();
            if (s.equals(mOldMsg)) {
                if (mTwoTime - mOneTime > Toast.LENGTH_SHORT) {
                    mToast.show();
                }
            } else {
                mOldMsg = s;
                mToast.setText(s);
                mToast.show();
            }
        }

        mOneTime = mTwoTime;
    }

    public static void showToast(Context context, final String s, int gravity) {
        if (mToast == null) {
            mToast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            mToast.setGravity(gravity, 0, 0);
            mToast.show();
            mOneTime = System.currentTimeMillis();
        } else {
            mToast.setGravity(gravity, 0, 0);
            mTwoTime = System.currentTimeMillis();
            if (s.equals(mOldMsg)) {
                if (mTwoTime - mOneTime > Toast.LENGTH_SHORT) {
                    mToast.show();
                }
            } else {
                mOldMsg = s;
                mToast.setText(s);
                mToast.show();
            }
        }

        mOneTime = mTwoTime;
    }

    public static void showToast(Context context, final int resId) {
        showToast(context, context.getString(resId));
    }
}
