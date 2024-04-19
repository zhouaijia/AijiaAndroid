
package com.aijia.video.player.event;

import android.view.Surface;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class ActionSetSurface extends Event {

    public Surface surface;

    public ActionSetSurface() {
        super(PlayerEvent.Action.SET_SURFACE);
    }

    public ActionSetSurface init(Surface surface) {
        this.surface = surface;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        surface = null;
    }
}
