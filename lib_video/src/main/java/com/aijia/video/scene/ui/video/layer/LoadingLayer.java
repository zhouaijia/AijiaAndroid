
package com.aijia.video.scene.ui.video.layer;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aijia.video.player.Player;
import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.event.InfoBufferingStart;
import com.aijia.video.player.playback.PlaybackController;
import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.player.utils.event.Event;
import com.aijia.video.R;
import com.aijia.video.scene.ui.video.layer.base.AnimateLayer;

public class LoadingLayer extends AnimateLayer {

    private static Handler mHandler;

    public LoadingLayer() {
        if (mHandler == null) mHandler = new Handler();
    }

    @Override
    public String tag() {
        return "loading";
    }

    @Nullable
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        ProgressBar progressBar = (ProgressBar) LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_layer, parent, false);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) progressBar.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        return progressBar;
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
                case PlaybackEvent.Action.STOP_PLAYBACK: {
                    dismiss();
                    break;
                }
                case PlayerEvent.Action.PREPARE:
                case PlayerEvent.Action.START: {
                    showOpt();
                    break;
                }
                case PlayerEvent.Action.PAUSE:
                case PlayerEvent.Action.STOP:
                case PlayerEvent.Action.RELEASE: {
                    dismiss();
                    break;
                }
                case PlayerEvent.Action.SET_SURFACE: {
                    Player player = event.owner(Player.class);
                    if (player.isPlaying() && player.isBuffering()) {
                        showOpt();
                    } else if (player.isPreparing()) {
                        showOpt();
                    } else {
                        dismiss();
                    }
                    break;
                }
                case PlayerEvent.Info.VIDEO_RENDERING_START:
                case PlayerEvent.Info.VIDEO_RENDERING_START_BEFORE_START:
                case PlayerEvent.State.STARTED: {
                    Player player = event.owner(Player.class);
                    if (player.isPlaying() && player.isBuffering()) {
                        showOpt();
                    } else {
                        dismiss();
                    }
                    break;
                }
                case PlayerEvent.Info.BUFFERING_END:
                case PlayerEvent.State.COMPLETED:
                case PlayerEvent.State.ERROR: {
                    dismiss();
                    break;
                }
                case PlayerEvent.Info.BUFFERING_START: {
                    InfoBufferingStart e = event.cast(InfoBufferingStart.class);

                    int bufferNum = e.bufferId; // buffer 次数
                    int bufferType = e.bufferingType; // buffer 类型
                    int bufferStage = e.bufferingStage; // buffer 首帧前/后
                    int bufferReason = e.bufferingReason; // buffer 原因

                    Player player = event.owner(Player.class);
                    if (player.isPlaying()) {
                        showOpt();
                    }
                    break;
                }
            }
        }
    };

    @Override
    public void dismiss() {
        super.dismiss();
        mHandler.removeCallbacks(mShowRunnable);
    }

    private void showOpt() {
       showOpt(1000);
    }

    private void showOpt(long delayMills) {
        mHandler.removeCallbacks(mShowRunnable);
        mHandler.postDelayed(mShowRunnable, delayMills);
    }

    private final Runnable mShowRunnable = new Runnable() {
        @Override
        public void run() {
            animateShow(false);
        }
    };
}
