
package com.aijia.video.scene.ui.video.layer;

import static com.aijia.video.scene.utils.UIUtils.dip2Px;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.playback.PlaybackController;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.player.utils.event.Event;
import com.aijia.video.R;
import com.aijia.video.scene.ui.video.layer.base.AnimateLayer;


public class TipsLayer extends AnimateLayer {

    @Override
    public String tag() {
        return "tips";
    }

    @Nullable
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setTextColor(Color.WHITE);
        textView.setBackground(ResourcesCompat.getDrawable(parent.getResources(),
                R.drawable.tips_layer_bg_shape,
                null));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.LEFT);
        lp.setMargins(
                (int) dip2Px(parent.getContext(), 20),
                0,
                0,
                (int) dip2Px(parent.getContext(), 20));
        textView.setLayoutParams(lp);
        int paddingV = (int) dip2Px(parent.getContext(), 2);
        int paddingH = (int) dip2Px(parent.getContext(), 8);
        textView.setPadding(paddingH, paddingV, paddingH, paddingV);
        textView.setShadowLayer(5, 0, 0, Color.BLACK);
        return textView;
    }

    public void show(CharSequence hintText) {
        animateShow(true);
        TextView text = getView();
        if (text != null) {
            text.setText(hintText);
        }
    }

    @Override
    protected void onBindPlaybackController(@NonNull PlaybackController controller) {
        controller.addPlaybackListener(mPlaybackListener);
    }

    @Override
    protected void onUnbindPlaybackController(@NonNull PlaybackController controller) {
        controller.removePlaybackListener(mPlaybackListener);
    }

    private final Dispatcher.EventListener mPlaybackListener = new Dispatcher.EventListener() {
        @Override
        public void onEvent(Event event) {
            switch (event.code()) {
                case PlayerEvent.Action.RELEASE:
                    dismiss();
                    break;
            }
        }
    };
}
