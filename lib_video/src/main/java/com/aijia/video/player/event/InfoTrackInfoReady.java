
package com.aijia.video.player.event;

import android.annotation.SuppressLint;

import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.source.Track;
import com.aijia.video.player.utils.event.Event;

import java.util.List;


public class InfoTrackInfoReady extends Event {
    @Track.TrackType
    public int trackType;
    public List<Track> tracks;

    public InfoTrackInfoReady() {
        super(PlayerEvent.Info.TRACK_INFO_READY);
    }

    public InfoTrackInfoReady init(@Track.TrackType int trackType, List<Track> tracks) {
        this.trackType = trackType;
        this.tracks = tracks;
        return this;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void recycle() {
        super.recycle();
        trackType = 0;
        tracks = null;
    }
}
