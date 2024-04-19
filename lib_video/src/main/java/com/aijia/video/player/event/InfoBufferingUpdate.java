
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class InfoBufferingUpdate extends Event {

    public int percent;

    public InfoBufferingUpdate() {
        super(PlayerEvent.Info.BUFFERING_UPDATE);
    }

    public InfoBufferingUpdate init(int percent) {
        this.percent = percent;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        percent = 0;
    }
}
