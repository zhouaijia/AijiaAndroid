
package com.aijia.video.player.playback.event;

import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.playback.VideoView;
import com.aijia.video.player.utils.event.Event;


public class StateUnbindVideoView extends Event {

    public VideoView videoView;

    public StateUnbindVideoView() {
        super(PlaybackEvent.State.UNBIND_VIDEO_VIEW);
    }

    public StateUnbindVideoView init(VideoView videoView) {
        this.videoView = videoView;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        videoView = null;
    }
}
