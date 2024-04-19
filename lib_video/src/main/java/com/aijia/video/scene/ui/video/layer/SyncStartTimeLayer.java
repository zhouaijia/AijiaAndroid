
package com.aijia.video.scene.ui.video.layer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aijia.video.player.Player;
import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.playback.PlaybackController;
import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.playback.VideoLayerHost;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.player.utils.event.Event;
import com.aijia.video.R;
import com.aijia.video.scene.ui.video.layer.base.BaseLayer;
import com.aijia.video.scene.utils.TimeUtils;


public class SyncStartTimeLayer extends BaseLayer {

    @Override
    public String tag() {
        return "sync_start_time";
    }

    @Nullable
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        return null;
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
                case PlaybackEvent.Action.STOP_PLAYBACK:
                    dismiss();
                    break;
                case PlayerEvent.State.PREPARED: {
                    VideoLayerHost layerHost = layerHost();
                    if (layerHost == null) return;
                    Context context = context();
                    if (context == null) return;

                    Player player = event.owner(Player.class);
                    if (player.getStartTime() > 1000) {
                        TipsLayer tipsLayer = layerHost.findLayer(TipsLayer.class);
                        if (tipsLayer != null) {
                            tipsLayer.show(context.getString(R.string.vevod_tips_sync_start_time,
                                    TimeUtils.time2String(player.getStartTime())));
                        }
                    }
                    break;
                }
            }
        }
    };
}
