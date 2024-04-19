
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;

public class InfoSubtitleStateChanged extends Event {

    public boolean enabled;

    public InfoSubtitleStateChanged() {
        super(PlayerEvent.Info.SUBTITLE_STATE_CHANGED);
    }

    public InfoSubtitleStateChanged init(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        enabled = false;
    }
}
