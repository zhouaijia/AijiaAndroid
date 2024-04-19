
package com.aijia.video.scene.utils;

import android.view.View;
import android.view.ViewGroup;

public class ViewUtils {

    public static ViewGroup removeFromParent(View view) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return parent;
    }
}
