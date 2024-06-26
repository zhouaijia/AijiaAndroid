
package com.aijia.video.scene.ui.video.layer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aijia.video.player.Player;
import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.playback.PlaybackController;
import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.playback.VideoView;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.player.utils.event.Event;
import com.aijia.video.R;
import com.aijia.video.scene.ui.video.layer.base.AnimateLayer;


public class PauseLayer extends AnimateLayer {

    private ObjectAnimator scaleXAnimator;
    private ObjectAnimator scaleYAnimator;

    @Override
    public String tag() {
        return "pause";
    }

    @Nullable
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pause_layer, parent, false);
        view.setOnClickListener(null);
        view.setClickable(false);
        return view;
    }

    @Override
    protected Animator createAnimator() {
        scaleXAnimator = new ObjectAnimator();
        scaleYAnimator = new ObjectAnimator();
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleXAnimator, scaleYAnimator);
        set.setInterpolator(new DecelerateInterpolator());
        set.setDuration(150);
        return set;
    }

    @Override
    protected void initAnimateDismissProperty(Animator animator) {
        if (scaleXAnimator != null) {
            // Using scaleX animator animate alpha instead create a new animator instance.
            scaleXAnimator.setPropertyName("alpha");
            scaleXAnimator.setFloatValues(1, 0);
        }
        if (scaleYAnimator != null) {
            scaleYAnimator.setPropertyName("scaleY");
            scaleYAnimator.setFloatValues(1, 1);
        }
    }

    @Override
    protected void initAnimateShowProperty(Animator animator) {
        if (scaleXAnimator != null) {
            scaleXAnimator.setPropertyName("scaleX");
            scaleXAnimator.setFloatValues(1, 1.5f, 1);
        }
        if (scaleYAnimator != null) {
            scaleYAnimator.setPropertyName("scaleY");
            scaleYAnimator.setFloatValues(1, 1.5f, 1);
        }
    }

    @Override
    protected void resetViewAnimateProperty() {
        View view = getView();
        if (view != null) {
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);
        }
    }

    @Override
    public void onVideoViewClick(VideoView videoView) {
        final Player player = player();
        if (player != null && player.isInPlaybackState()) {
            if (player.isPlaying()) {
                player.pause();
            } else {
                player.start();
            }
        } else {
            startPlayback();
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
                case PlaybackEvent.Action.START_PLAYBACK:
                case PlayerEvent.Action.START:
                case PlayerEvent.Info.VIDEO_RENDERING_START:
                    animateDismiss();
                    break;
                case PlayerEvent.Action.STOP:
                case PlayerEvent.Action.RELEASE:
                    dismiss();
                    break;
                case PlayerEvent.Action.PAUSE:
                    animateShow(false);
                    break;
            }
        }
    };
}
