
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class StateCompleted extends Event {
    public StateCompleted() {
        super(PlayerEvent.State.COMPLETED);
    }
}
