
package com.aijia.video.scene.ui.video.scene.shortvideo;

import com.aijia.video.player.playback.DisplayModeHelper;
import com.aijia.video.player.playback.VideoView;
import com.aijia.video.engine.Scene;
import com.aijia.video.scene.VideoSettings;
import com.aijia.video.scene.data.model.VideoItem;

import java.util.List;

public class ShortVideoStrategy {

    public static void setEnabled(boolean enable) {
        if (!VideoSettings.booleanValue(VideoSettings.SHORT_VIDEO_ENABLE_STRATEGY)) return;

        VolcEngineStrategy.setEnabled(Scene.SCENE_SHORT_VIDEO, enable);
    }

    public static void setItems(List<VideoItem> videoItems) {
        if (!VideoSettings.booleanValue(VideoSettings.SHORT_VIDEO_ENABLE_STRATEGY)) return;

        if (videoItems == null) return;

        VolcEngineStrategy.setMediaSources(VideoItem.toMediaSources(videoItems, false));
    }

    public static void appendItems(List<VideoItem> videoItems) {
        if (!VideoSettings.booleanValue(VideoSettings.SHORT_VIDEO_ENABLE_STRATEGY)) return;

        if (videoItems == null) return;

        VolcEngineStrategy.addMediaSources(VideoItem.toMediaSources(videoItems, false));
    }

    public static boolean renderFrame(VideoView videoView) {
        if (!VideoSettings.booleanValue(VideoSettings.SHORT_VIDEO_ENABLE_STRATEGY)) return false;
        if (videoView == null) return false;

        int[] frameInfo = new int[2];
        VolcEngineStrategy.renderFrame(videoView.getDataSource(), videoView.getSurface(), frameInfo);
        int videoWidth = frameInfo[0];
        int videoHeight = frameInfo[1];
        if (videoWidth > 0 && videoHeight > 0) {
            videoView.setDisplayAspectRatio(DisplayModeHelper.calDisplayAspectRatio(videoWidth, videoHeight, 0));
            return true;
        }
        return false;
    }
}
