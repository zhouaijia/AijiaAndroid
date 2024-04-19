
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.source.MediaSource;
import com.aijia.video.player.utils.event.Event;


public class ActionPrepare extends Event {

    public MediaSource mediaSource;

    public ActionPrepare() {
        super(PlayerEvent.Action.PREPARE);
    }

    public ActionPrepare init(MediaSource source) {
        this.mediaSource = source;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        mediaSource = null;
    }
}
