
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class InfoSeekingStart extends Event {

    public long from;
    public long to;

    public InfoSeekingStart() {
        super(PlayerEvent.Info.SEEKING_START);
    }

    public InfoSeekingStart init(long from, long seekTo) {
        this.from = from;
        this.to = seekTo;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        from = 0;
        to = 0;
    }
}
