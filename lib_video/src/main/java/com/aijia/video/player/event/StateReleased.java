
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class StateReleased extends Event {
    public StateReleased() {
        super(PlayerEvent.State.RELEASED);
    }
}
