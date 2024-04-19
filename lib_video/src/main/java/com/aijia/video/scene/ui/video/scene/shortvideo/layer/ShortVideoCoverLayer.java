
package com.aijia.video.scene.ui.video.scene.shortvideo.layer;

import android.view.Surface;

import androidx.annotation.NonNull;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.playback.PlaybackController;
import com.aijia.video.player.playback.VideoView;
import com.aijia.video.player.source.MediaSource;
import com.aijia.video.player.utils.L;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.player.utils.event.Event;
import com.aijia.video.scene.VideoSettings;
import com.aijia.video.scene.ui.video.layer.CoverLayer;
import com.aijia.video.scene.ui.video.scene.shortvideo.ShortVideoStrategy;

public class ShortVideoCoverLayer extends CoverLayer {

    @Override
    public String tag() {
        return "short_video_cover";
    }

    @Override
    public void onVideoViewBindDataSource(MediaSource dataSource) {
    }

    @Override
    public void onSurfaceAvailable(Surface surface, int width, int height) {
        final VideoView videoView = videoView();
        if (videoView == null) return;

        if (player() != null) {
            return;
        }

        final boolean rendered = ShortVideoStrategy.renderFrame(videoView);
        if (rendered) {
            L.d(this, "onSurfaceAvailable", videoView, surface, "preRender success");
            dismiss();
        } else {
            L.d(this, "onSurfaceAvailable", videoView, surface, "preRender failed");
            show();
        }
    }

    @Override
    protected void load() {

        if (!VideoSettings.booleanValue(VideoSettings.SHORT_VIDEO_ENABLE_IMAGE_COVER)) return;

        super.load();
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
                case PlayerEvent.Info.VIDEO_RENDERING_START: {
                    dismiss();
                    break;
                }
            }
        }
    };
}
