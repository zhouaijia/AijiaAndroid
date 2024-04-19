
package com.aijia.video.player.playback.event;

import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.utils.event.Event;


public class ActionStopPlayback extends Event {
    public ActionStopPlayback() {
        super(PlaybackEvent.Action.STOP_PLAYBACK);
    }
}
