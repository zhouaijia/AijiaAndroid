
package com.aijia.video.scene.ui.video.layer.base;

import androidx.annotation.NonNull;

import com.aijia.video.player.playback.VideoLayer;

public abstract class BaseLayer extends VideoLayer {

    public void requestShow(@NonNull String reason) {
        show();
    }

    public void requestDismiss(@NonNull String reason) {
        dismiss();
    }

    public void requestHide(@NonNull String reason) {
        hide();
    }
}
