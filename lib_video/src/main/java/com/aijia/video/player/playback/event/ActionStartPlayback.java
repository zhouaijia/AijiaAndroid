
package com.aijia.video.player.playback.event;

import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.utils.event.Event;


public class ActionStartPlayback extends Event {

    public ActionStartPlayback() {
        super(PlaybackEvent.Action.START_PLAYBACK);
    }
}
