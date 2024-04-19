
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class StatePreparing extends Event {
    public StatePreparing() {
        super(PlayerEvent.State.PREPARING);
    }
}
