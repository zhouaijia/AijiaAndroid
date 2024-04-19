
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;

public class InfoVideoRenderingStartBeforeStart extends Event {
    public InfoVideoRenderingStartBeforeStart() {
        super(PlayerEvent.Info.VIDEO_RENDERING_START_BEFORE_START);
    }
}
