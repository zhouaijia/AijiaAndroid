
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class ActionSetLooping extends Event {

    public boolean isLooping;

    public ActionSetLooping() {
        super(PlayerEvent.Action.SET_LOOPING);
    }

    @Override
    public void recycle() {
        super.recycle();
        isLooping = false;
    }

    public ActionSetLooping init(boolean isLooping) {
        this.isLooping = isLooping;
        return this;
    }
}
