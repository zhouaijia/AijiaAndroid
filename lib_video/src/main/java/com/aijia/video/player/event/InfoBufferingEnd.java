
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class InfoBufferingEnd extends Event {

    public int bufferId;

    public InfoBufferingEnd() {
        super(PlayerEvent.Info.BUFFERING_END);
    }

    public InfoBufferingEnd init(int bufferId) {
        this.bufferId = bufferId;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        bufferId = 0;
    }
}
