
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class ActionRelease extends Event {

    public ActionRelease() {
        super(PlayerEvent.Action.RELEASE);
    }
}
