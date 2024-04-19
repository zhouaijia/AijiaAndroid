
package com.aijia.video.player.source;

import com.aijia.video.player.utils.L;

import java.io.Serializable;

public class SubtitleText implements Serializable {
    private long pts;
    private long duration;
    private String text;

    public long getPts() {
        return pts;
    }

    public void setPts(long pts) {
        this.pts = pts;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "SubtitleText{" +
                "pts=" + pts +
                ", duration=" + duration +
                ", text='" + text + '\'' +
                '}';
    }

    public static String dump(SubtitleText text) {
        if (!L.ENABLE_LOG) return null;
        if (text == null) return null;

        return text.pts + " " + text.duration + " " + text.text;
    }
}
