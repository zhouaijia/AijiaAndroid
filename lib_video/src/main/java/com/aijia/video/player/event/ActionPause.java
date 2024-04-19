
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class ActionPause extends Event {

    public ActionPause() {
        super(PlayerEvent.Action.PAUSE);
    }
}
