
package com.aijia.video.player.source;

import androidx.annotation.NonNull;

import java.util.List;

public interface SubtitleSelector {


    @NonNull
    Subtitle selectSubtitle(@NonNull MediaSource mediaSource, @NonNull List<Subtitle> subtitles);
}
