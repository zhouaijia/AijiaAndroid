
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class InfoAudioRenderingStart extends Event {

    public InfoAudioRenderingStart() {
        super(PlayerEvent.Info.AUDIO_RENDERING_START);
    }
}
