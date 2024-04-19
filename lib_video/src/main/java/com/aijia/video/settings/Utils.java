
package com.aijia.video.settings;

import android.content.Context;

public class Utils {
    public static float dip2Px(Context context, float dipValue) {
        if (context != null) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return dipValue * scale + 0.5f;
        }
        return 0;
    }
}
