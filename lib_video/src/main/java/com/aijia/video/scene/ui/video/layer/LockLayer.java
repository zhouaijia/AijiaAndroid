
package com.aijia.video.scene.ui.video.layer;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.aijia.video.player.Player;
import com.aijia.video.player.playback.VideoLayerHost;
import com.aijia.video.R;
import com.aijia.video.scene.ui.video.layer.base.AnimateLayer;
import com.aijia.video.scene.utils.UIUtils;

public class LockLayer extends AnimateLayer {

    private ImageView mImageView;

    public LockLayer() {
        setIgnoreLock(true);
    }

    @Override
    public String tag() {
        return "lock";
    }

    @Nullable
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        mImageView = new ImageView(parent.getContext());
        mImageView.setLayoutParams(new FrameLayout.LayoutParams(
                (int) UIUtils.dip2Px(context(), 44),
                (int) UIUtils.dip2Px(context(), 44),
                Gravity.CENTER_VERTICAL | Gravity.RIGHT));
        ((FrameLayout.LayoutParams) mImageView.getLayoutParams()).rightMargin = (int) UIUtils.dip2Px(context(), 60);
        mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mImageView.setImageDrawable(ResourcesCompat.getDrawable(parent.getResources(), R.drawable.lock_layer_ic_selector, null));
        mImageView.setOnClickListener(v -> {
            layerHost().setLocked(!layerHost().isLocked());
            // locked -> loop playback
            final Player player = player();
            if (player != null && player.isInPlaybackState()) {
                player.setLooping(layerHost().isLocked());
            }

            GestureLayer gestureLayer = layerHost().findLayer(GestureLayer.class);
            if (gestureLayer != null) {
                gestureLayer.showController();
            }
        });
        return mImageView;
    }

    @Override
    protected void onLayerHostLockStateChanged(boolean locked) {
        super.onLayerHostLockStateChanged(locked);
        syncLockedState(locked);
    }

    @Override
    public void show() {
        super.show();
        syncLockedState(isLayerHostLocked());
    }

    private boolean isLayerHostLocked() {
        VideoLayerHost layerHost = layerHost();
        return layerHost != null && layerHost.isLocked();
    }

    private void syncLockedState(boolean locked) {
        if (mImageView != null) {
            mImageView.setSelected(locked);
            if (locked) {
                mImageView.setTranslationY(0);
            } else {
                mImageView.setTranslationY((int) UIUtils.dip2Px(context(), -56));
            }
        }
    }
}
