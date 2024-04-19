
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.source.SubtitleText;
import com.aijia.video.player.utils.event.Event;

public class InfoSubtitleTextUpdate extends Event {
    public SubtitleText subtitleText;

    public InfoSubtitleTextUpdate() {
        super(PlayerEvent.Info.SUBTITLE_TEXT_UPDATE);
    }

    public InfoSubtitleTextUpdate init(SubtitleText subtitleText) {
        this.subtitleText = subtitleText;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();

        this.subtitleText = null;
    }
}
