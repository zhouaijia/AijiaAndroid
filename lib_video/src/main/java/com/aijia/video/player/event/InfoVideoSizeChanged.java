
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class InfoVideoSizeChanged extends Event {

    public int videoWidth;
    public int videoHeight;

    public InfoVideoSizeChanged() {
        super(PlayerEvent.Info.VIDEO_SIZE_CHANGED);
    }

    public InfoVideoSizeChanged init(int videoWidth, int videoHeight) {
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        videoWidth = 0;
        videoHeight = 0;
    }
}
