
package com.aijia.video.player.playback.event;

import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.playback.VideoView;
import com.aijia.video.player.utils.event.Event;


public class StateBindVideoView extends Event {

    public VideoView videoView;

    public StateBindVideoView() {
        super(PlaybackEvent.State.BIND_VIDEO_VIEW);
    }

    public StateBindVideoView init(VideoView videoView) {
        this.videoView = videoView;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        this.videoView = null;
    }
}
