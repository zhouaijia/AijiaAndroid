
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.PlayerException;
import com.aijia.video.player.utils.event.Event;


public class StateError extends Event {
    public PlayerException e;

    public StateError() {
        super(PlayerEvent.State.ERROR);
    }

    public StateError init(PlayerException e) {
        this.e = e;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        e = null;
    }
}
