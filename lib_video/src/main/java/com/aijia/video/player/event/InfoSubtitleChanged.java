
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.source.Subtitle;
import com.aijia.video.player.utils.event.Event;

public class InfoSubtitleChanged extends Event {

    public Subtitle pre;
    public Subtitle current;


    public InfoSubtitleChanged() {
        super(PlayerEvent.Info.SUBTITLE_CHANGED);
    }

    public InfoSubtitleChanged init(Subtitle pre, Subtitle current) {
        this.pre = pre;
        this.current = current;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        this.pre = null;
        this.current = null;
    }
}
