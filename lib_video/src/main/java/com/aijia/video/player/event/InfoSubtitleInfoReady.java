
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.source.Subtitle;
import com.aijia.video.player.utils.event.Event;

import java.util.List;

public class InfoSubtitleInfoReady extends Event {

    public List<Subtitle> subtitles;

    public InfoSubtitleInfoReady() {
        super(PlayerEvent.Info.SUBTITLE_LIST_INFO_READY);
    }

    public InfoSubtitleInfoReady init(List<Subtitle> subtitles) {
        this.subtitles = subtitles;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();

        subtitles = null;
    }
}
