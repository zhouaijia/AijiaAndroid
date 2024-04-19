
package com.aijia.video.player.event;

import android.annotation.SuppressLint;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.source.Track;
import com.aijia.video.player.utils.event.Event;


public class InfoTrackChanged extends Event {
    @Track.TrackType
    public int trackType;
    public Track pre;
    public Track current;

    public InfoTrackChanged() {
        super(PlayerEvent.Info.TRACK_CHANGED);
    }


    public InfoTrackChanged init(@Track.TrackType int trackType, Track pre, Track current) {
        this.trackType = trackType;
        this.pre = pre;
        this.current = current;
        return this;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void recycle() {
        super.recycle();
        trackType = Track.TRACK_TYPE_UNKNOWN;
        pre = null;
        current = null;
    }
}
