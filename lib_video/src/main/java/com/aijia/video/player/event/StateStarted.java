
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class StateStarted extends Event {
    public StateStarted() {
        super(PlayerEvent.State.STARTED);
    }
}
