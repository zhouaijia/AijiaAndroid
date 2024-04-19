
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class StatePaused extends Event {
    public StatePaused() {
        super(PlayerEvent.State.PAUSED);
    }
}
