
package com.aijia.video.player.event;

import com.aijia.video.player.PlayerEvent.Info;
import com.aijia.video.player.utils.event.Event;

public class InfoCacheUpdate extends Event {

    public long cachedBytes;

    public InfoCacheUpdate() {
        super(Info.CACHE_UPDATE);
    }

    public InfoCacheUpdate init(long cachedBytes) {
        this.cachedBytes = cachedBytes;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        cachedBytes = 0;
    }
}
