
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;

public class StateIDLE extends Event {

    public StateIDLE() {
        super(PlayerEvent.State.IDLE);
    }
}
