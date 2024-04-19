
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class InfoSeekComplete extends Event {
    public InfoSeekComplete() {
        super(PlayerEvent.Info.SEEK_COMPLETE);
    }
}
