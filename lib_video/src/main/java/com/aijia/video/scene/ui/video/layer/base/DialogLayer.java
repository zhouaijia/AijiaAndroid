
package com.aijia.video.scene.ui.video.layer.base;

import static com.aijia.video.scene.ui.video.layer.Layers.VisibilityRequestReason.REQUEST_DISMISS_REASON_DIALOG_SHOW;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aijia.video.player.playback.VideoLayer;
import com.aijia.video.player.playback.VideoLayerHost;


public abstract class DialogLayer extends AnimateLayer implements VideoLayerHost.BackPressedHandler {

    @Nullable
    @Override
    protected final View createView(@NonNull ViewGroup parent) {
        return createDialogView(parent);
    }

    abstract protected View createDialogView(@NonNull ViewGroup parent);

    @Override
    public boolean onBackPressed() {
        if (isShowing()) {
            animateDismiss();
            return true;
        } else {
            return false;
        }
    }

    protected abstract int backPressedPriority();

    @CallSuper
    @Override
    protected void onBindLayerHost(@NonNull VideoLayerHost layerHost) {
        layerHost.registerBackPressedHandler(this, backPressedPriority());
    }

    @CallSuper
    @Override
    protected void onUnbindLayerHost(@NonNull VideoLayerHost layerHost) {
        layerHost.unregisterBackPressedHandler(this);
    }

    @Override
    public void show() {
        boolean isShowing = isShowing();
        super.show();
        if (!isShowing && isShowing()) {
            dismissLayers();
        }
    }

    private void dismissLayers() {
        for (int i = 0; i < layerHost().layerSize(); i++) {
            VideoLayer layer = layerHost().findLayer(i);
            if (layer instanceof AnimateLayer) {
                ((AnimateLayer) layer).requestAnimateDismiss(REQUEST_DISMISS_REASON_DIALOG_SHOW);
            } else if (layer instanceof BaseLayer) {
                ((BaseLayer) layer).requestDismiss(REQUEST_DISMISS_REASON_DIALOG_SHOW);
            }
        }
    }

    @Override
    public void requestDismiss(@NonNull String reason) {
        if (!TextUtils.equals(reason, REQUEST_DISMISS_REASON_DIALOG_SHOW)) {
            super.requestDismiss(reason);
        }
    }

    @Override
    public void requestHide(@NonNull String reason) {
        if (!TextUtils.equals(reason, REQUEST_DISMISS_REASON_DIALOG_SHOW)) {
            super.requestHide(reason);
        }
    }
}
