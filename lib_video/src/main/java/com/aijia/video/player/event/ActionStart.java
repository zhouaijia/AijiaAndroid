
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class ActionStart extends Event {
    public ActionStart() {
        super(PlayerEvent.Action.START);
    }
}
