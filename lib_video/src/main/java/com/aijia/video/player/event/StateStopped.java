
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class StateStopped extends Event {
    public StateStopped() {
        super(PlayerEvent.State.STOPPED);
    }
}
