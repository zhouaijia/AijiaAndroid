
package com.aijia.video.scene.utils;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.view.OrientationEventListener;

import com.aijia.video.player.utils.L;

import java.lang.ref.WeakReference;


public class OrientationHelper extends OrientationEventListener {

    public static final int ORIENTATION_0 = 0;
    public static final int ORIENTATION_90 = 90;
    public static final int ORIENTATION_180 = 180;
    public static final int ORIENTATION_270 = 270;
    public static final int ORIENTATION_360 = 360;

    public static final int DEFAULT_ORIENTATION_DELTA = 15;

    private int mOrientationDelta = DEFAULT_ORIENTATION_DELTA;
    private final WeakReference<Activity> mActivityRef;
    private int mOrientation = -1;

    private final OrientationChangedListener mListener;
    private boolean mEnabled;

    public OrientationHelper(Activity activity, OrientationChangedListener listener) {
        super(activity);
        this.mActivityRef = new WeakReference<>(activity);
        this.mListener = listener;
    }

    public void setOrientationDelta(int orientationDelta) {
        this.mOrientationDelta = orientationDelta;
    }

    @Override
    public void onOrientationChanged(int orientation) {
        final Activity activity = mActivityRef.get();
        if (activity == null || activity.isFinishing()) return;

        int lastOrientation = mOrientation;
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            return;
        } else if (isPortraitRange(orientation) && mOrientation != ORIENTATION_0) {
            mOrientation = ORIENTATION_0;
        } else if (isReverseLandscapeRange(orientation) && mOrientation != ORIENTATION_90) {
            mOrientation = ORIENTATION_90;
        } else if (isLandScapeRange(orientation) && mOrientation != ORIENTATION_270) {
            mOrientation = ORIENTATION_270;
        }
        if (mListener != null
                && mOrientation != OrientationEventListener.ORIENTATION_UNKNOWN
                && lastOrientation != mOrientation) {
            mListener.orientationChanged(lastOrientation, mOrientation);
        }
    }

    @Override
    public void enable() {
        super.enable();
        if (!mEnabled) {
            mEnabled = true;
            L.v(this, "toggle", mEnabled);
        }
    }

    @Override
    public void disable() {
        super.disable();
        if (mEnabled) {
            mEnabled = false;
            L.v(this, "toggle", mEnabled);
        }
    }

    public boolean isEnabled() {
        return mEnabled;
    }

    public int getOrientation() {
        return mOrientation;
    }

    private boolean isPortraitRange(int orientation) {
        return Math.abs(ORIENTATION_0 - orientation) <= mOrientationDelta
                || Math.abs(ORIENTATION_360 - orientation) <= mOrientationDelta;
    }

    private boolean isLandScapeRange(int orientation) {
        return Math.abs(ORIENTATION_270 - orientation) <= mOrientationDelta;
    }


    private boolean isReverseLandscapeRange(int orientation) {
        return Math.abs(ORIENTATION_90 - orientation) <= mOrientationDelta;
    }

    public static boolean isSystemAutoOrientationEnabled(Context context) {
        int status = 0;
        try {
            status = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return status == 1;
    }

    public interface OrientationChangedListener {

        void orientationChanged(int last, int current);

    }
}
