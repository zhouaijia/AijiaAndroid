
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class StatePrepared extends Event {

    public StatePrepared() {
        super(PlayerEvent.State.PREPARED);
    }
}
