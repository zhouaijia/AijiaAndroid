
package com.aijia.video.scene.ui.video.layer;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aijia.video.player.Player;
import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.playback.PlaybackController;
import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.player.utils.event.Event;
import com.aijia.video.R;
import com.aijia.video.scene.ui.video.layer.base.AnimateLayer;


public class PlayCompleteLayer extends AnimateLayer {

    @Override
    public String tag() {
        return "play_complete";
    }

    @Nullable
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        TextView textView = new TextView(parent.getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setText(R.string.vevod_play_complete_replay);
        textView.setTextColor(parent.getResources().getColor(android.R.color.white));
        textView.setBackgroundColor(textView.getResources().getColor(R.color.vevod_play_complete_background));
        textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        textView.setOnClickListener(v -> startPlayback());
        return textView;
    }

    @Override
    protected void onBindPlaybackController(@NonNull PlaybackController controller) {
        controller.addPlaybackListener(listener);
    }

    @Override
    protected void onUnbindPlaybackController(@NonNull PlaybackController controller) {
        controller.removePlaybackListener(listener);
    }

    private final Dispatcher.EventListener listener = new Dispatcher.EventListener() {

        @Override
        public void onEvent(Event event) {
            switch (event.code()) {
                case PlaybackEvent.Action.START_PLAYBACK:
                case PlaybackEvent.Action.STOP_PLAYBACK:
                    dismiss();
                    break;
                case PlayerEvent.State.COMPLETED:
                    Player player = event.owner(Player.class);
                    if (!player.isLooping()) {
                        animateShow(false);
                    }
                    break;

            }
        }
    };
}
