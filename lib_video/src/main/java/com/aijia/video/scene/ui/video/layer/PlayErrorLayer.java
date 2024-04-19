
package com.aijia.video.scene.ui.video.layer;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.PlayerException;
import com.aijia.video.player.event.StateError;
import com.aijia.video.player.playback.PlaybackController;
import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.player.utils.event.Event;
import com.aijia.video.R;
import com.aijia.video.scene.ui.video.layer.base.BaseLayer;

public class PlayErrorLayer extends BaseLayer {

    private PlayerException mException;

    @Override
    public String tag() {
        return "play_error";
    }

    @Nullable
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMarginStart(100);
        lp.setMarginEnd(100);
        textView.setLayoutParams(lp);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.LEFT);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlayback();
            }
        });
        return textView;
    }

    @Override
    public void show() {
        super.show();
        TextView textView = getView();
        if (mException != null && textView != null) {
            textView.setText(textView.getResources().getString(R.string.vevod_play_error_text) + "\n" + mException);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mException = null;
    }

    @Override
    protected void onBindPlaybackController(@NonNull PlaybackController controller) {
        controller.addPlaybackListener(mPlaybackListener);
    }

    @Override
    protected void onUnbindPlaybackController(@NonNull PlaybackController controller) {
        controller.removePlaybackListener(mPlaybackListener);
        dismiss();
    }

    private final Dispatcher.EventListener mPlaybackListener = new Dispatcher.EventListener() {

        @Override
        public void onEvent(Event event) {
            switch (event.code()) {
                case PlaybackEvent.Action.START_PLAYBACK:
                case PlaybackEvent.Action.STOP_PLAYBACK:
                    dismiss();
                    break;
                case PlayerEvent.State.ERROR: {
                    PlayErrorLayer.this.mException = event.cast(StateError.class).e;
                    show();
                    break;
                }
            }
        }
    };
}
