
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class ActionSetSpeed extends Event {
    public float speed;

    public ActionSetSpeed() {
        super(PlayerEvent.Action.SET_SPEED);
    }

    @Override
    public void recycle() {
        super.recycle();
        speed = 0;
    }

    public ActionSetSpeed init(float speed) {
        this.speed = speed;
        return this;
    }
}
