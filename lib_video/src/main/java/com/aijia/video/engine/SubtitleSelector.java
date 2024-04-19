
package com.aijia.video.engine;

import androidx.annotation.NonNull;

import com.aijia.video.player.source.MediaSource;
import com.aijia.video.player.source.Subtitle;

import java.util.Arrays;
import java.util.List;

public class SubtitleSelector implements com.aijia.video.player.source.SubtitleSelector {
    public static final List<Integer> DEFAULT_LANGUAGE_IDS = Arrays.asList(5, 1, 2);

    @NonNull
    @Override
    public Subtitle selectSubtitle(@NonNull MediaSource mediaSource, @NonNull List<Subtitle> subtitles) {
        for (int languageId : DEFAULT_LANGUAGE_IDS) {
            for (Subtitle subtitle : subtitles) {
                if (subtitle.getLanguageId() == languageId) {
                    return subtitle;
                }
            }
        }
        return subtitles.get(0);
    }
}
