
package com.aijia.video.scene.strategy;

import androidx.annotation.Nullable;

import com.aijia.video.player.source.Subtitle;
import com.aijia.video.engine.SubtitleSelector;
import com.aijia.video.R;
import com.aijia.video.scene.utils.SceneUtils;

import java.util.List;

public class VideoSubtitle {
    public static final int LANGUAGE_ID_CN = 1;  // 简体中文
    public static final int LANGUAGE_ID_US = 2;  // 英语

    @Nullable
    public static String subtitle2String(Subtitle subtitle) {
        switch (subtitle.getLanguageId()) {
            case LANGUAGE_ID_CN:
                return SceneUtils.getApplication().getString(R.string.vevod_subtitle_language_cn);
            case LANGUAGE_ID_US:
                return SceneUtils.getApplication().getString(R.string.vevod_subtitle_language_english);
        }
        return null;
    }

    public static List<Integer> createLanguageIds() {
        return SubtitleSelector.DEFAULT_LANGUAGE_IDS;
    }
}
