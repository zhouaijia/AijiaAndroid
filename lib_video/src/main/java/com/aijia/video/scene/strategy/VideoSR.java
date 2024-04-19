
package com.aijia.video.scene.strategy;

import com.aijia.video.engine.SuperResolutionConfig;
import com.aijia.video.scene.VideoSettings;

public class VideoSR {
    public static SuperResolutionConfig createConfig(int playScene) {
        SuperResolutionConfig config = new SuperResolutionConfig();
        config.enableSuperResolutionOnStartup = VideoSettings.booleanValue(VideoSettings.COMMON_ENABLE_SUPER_RESOLUTION);
        return config;
    }
}
