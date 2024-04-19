
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class ActionStop extends Event {
    public ActionStop() {
        super(PlayerEvent.Action.STOP);
    }
}
