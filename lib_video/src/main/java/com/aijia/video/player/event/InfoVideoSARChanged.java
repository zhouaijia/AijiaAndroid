
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.utils.event.Event;


public class InfoVideoSARChanged extends Event {

    public int num;
    public int den;

    public InfoVideoSARChanged() {
        super(PlayerEvent.Info.VIDEO_SAR_CHANGED);
    }


    public InfoVideoSARChanged init(int num, int den) {
        this.num = num;
        this.den = den;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        num = 0;
        den = 0;
    }
}
