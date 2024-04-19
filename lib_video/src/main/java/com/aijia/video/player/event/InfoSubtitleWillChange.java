
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.source.Subtitle;
import com.aijia.video.player.utils.event.Event;

public class InfoSubtitleWillChange extends Event {

    public Subtitle current;
    public Subtitle target;

    public InfoSubtitleWillChange() {
        super(PlayerEvent.Info.SUBTITLE_WILL_CHANGE);
    }

    public InfoSubtitleWillChange init(Subtitle current, Subtitle target) {
        this.current = current;
        this.target = target;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        this.current = null;
        this.target = null;
    }
}
