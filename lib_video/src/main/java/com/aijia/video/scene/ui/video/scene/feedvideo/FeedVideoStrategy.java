
package com.aijia.video.scene.ui.video.scene.feedvideo;

import com.aijia.video.engine.Scene;
import com.aijia.video.scene.VideoSettings;
import com.aijia.video.scene.data.model.VideoItem;

import java.util.List;

public class FeedVideoStrategy {

    public static void setEnabled(boolean enable) {
        if (!VideoSettings.booleanValue(VideoSettings.FEED_VIDEO_ENABLE_PRELOAD)) return;

        VolcEngineStrategy.setEnabled(Scene.SCENE_FEED_VIDEO, enable);
    }

    public static void setItems(List<VideoItem> videoItems) {
        if (!VideoSettings.booleanValue(VideoSettings.FEED_VIDEO_ENABLE_PRELOAD)) return;

        if (videoItems == null) return;

        VolcEngineStrategy.setMediaSources(VideoItem.toMediaSources(videoItems, true));
    }

    public static void appendItems(List<VideoItem> videoItems) {
        if (!VideoSettings.booleanValue(VideoSettings.FEED_VIDEO_ENABLE_PRELOAD)) return;

        if (videoItems == null) return;

        VolcEngineStrategy.addMediaSources(VideoItem.toMediaSources(videoItems, true));
    }
}
