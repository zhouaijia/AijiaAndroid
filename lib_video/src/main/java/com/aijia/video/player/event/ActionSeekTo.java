
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class ActionSeekTo extends Event {

    public long from;
    public long to;

    public ActionSeekTo() {
        super(PlayerEvent.Action.SEEK_TO);
    }

    public ActionSeekTo init(long from, long seekTo) {
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
