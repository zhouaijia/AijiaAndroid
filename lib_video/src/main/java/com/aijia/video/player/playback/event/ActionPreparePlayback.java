
package com.aijia.video.player.playback.event;

import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.utils.event.Event;


public class ActionPreparePlayback extends Event {

    public ActionPreparePlayback() {
        super(PlaybackEvent.Action.PREPARE_PLAYBACK);
    }
}
