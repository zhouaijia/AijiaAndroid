
package com.aijia.video.scene.strategy;

import androidx.annotation.NonNull;

import com.aijia.video.player.source.MediaSource;
import com.aijia.video.player.source.Quality;
import com.aijia.video.engine.Config;
import com.aijia.video.engine.AQuality;
import com.aijia.video.engine.AQualityConfig;
import com.aijia.video.engine.Scene;
import com.aijia.video.scene.VideoSettings;
import com.aijia.video.scene.data.model.VideoItem;
import com.aijia.video.scene.utils.SceneUtils;
import com.aijia.video.scene.utils.UIUtils;
import com.aijia.video.settings.Option;

import java.util.Arrays;
import java.util.List;

public class VideoQuality {
    public static final int VIDEO_QUALITY_DEFAULT = Quality.QUALITY_RES_720;

    public static String qualityDesc(int qualityRes) {
        if (qualityRes == 0) {
            return "未选择";
        }
        Quality quality = AQuality.quality(qualityRes);
        if (quality != null) {
            return quality.getQualityDesc();
        }
        return "UnKnown";
    }

    public static final List<Integer> QUALITY_RES_ARRAY_USER_SELECTED = Arrays.asList(
            Quality.QUALITY_RES_DEFAULT,
            Quality.QUALITY_RES_360,
            Quality.QUALITY_RES_480,
            Quality.QUALITY_RES_540,
            Quality.QUALITY_RES_720,
            Quality.QUALITY_RES_1080
    );

    public static final List<Integer> QUALITY_RES_ARRAY_DEFAULT = Arrays.asList(
            Quality.QUALITY_RES_360,
            Quality.QUALITY_RES_480,
            Quality.QUALITY_RES_540,
            Quality.QUALITY_RES_720,
            Quality.QUALITY_RES_1080
    );

    public static int getUserSelectedQualityRes(MediaSource mediaSource) {
        VideoItem videoItem = VideoItem.get(mediaSource);
        if (videoItem != null) {
            return VideoQuality.getUserSelectedQualityRes(videoItem.getPlayScene());
        }
        return Quality.QUALITY_RES_DEFAULT;
    }

    public static int getUserSelectedQualityRes(int playScene) {
        return VideoSettings.intValue(VideoSettings.QUALITY_VIDEO_QUALITY_USER_SELECTED);
    }

    public static void setUserSelectedQualityRes(int playScene, @Quality.QualityRes int qualityRes) {
        Option option = VideoSettings.option(VideoSettings.QUALITY_VIDEO_QUALITY_USER_SELECTED);
        option.userValues().saveValue(option, qualityRes);
    }

    //ToDo
    /*public static boolean isEnableStartupABR(MediaSource mediaSource) {
        return mediaSource != null &&
                (mediaSource.getSourceType() == MediaSource.SOURCE_TYPE_MODEL
                        || mediaSource.getSourceType() == MediaSource.SOURCE_TYPE_ID)
                && QualityStrategy.isEnableStartupABR(Config.get(mediaSource));
    }*/

    @NonNull
    public static AQualityConfig sceneGearConfig(int volcScene) {
        final AQualityConfig config = new AQualityConfig();
        config.enableStartupABR = VideoSettings.intValue(VideoSettings.QUALITY_ENABLE_STARTUP_ABR) >= 1;
        config.enableSupperResolutionDowngrade = VideoSettings.intValue(VideoSettings.QUALITY_ENABLE_STARTUP_ABR) == 2;
        switch (volcScene) {
            case Scene.SCENE_SHORT_VIDEO: {
                config.defaultQuality = AQuality.QUALITY_720P;
                config.wifiMaxQuality = AQuality.QUALITY_720P;
                config.mobileMaxQuality = AQuality.QUALITY_480P;

                final AQualityConfig.VolcDisplaySizeConfig displaySizeConfig = new AQualityConfig.VolcDisplaySizeConfig();
                config.displaySizeConfig = displaySizeConfig;

                final int screenWidth = UIUtils.getScreenWidth(SceneUtils.getApplication());
                final int screenHeight = UIUtils.getScreenHeight(SceneUtils.getApplication());

                displaySizeConfig.screenWidth = screenWidth;
                displaySizeConfig.screenHeight = screenHeight;
                displaySizeConfig.displayWidth = (int) (screenHeight / 16f * 9);
                displaySizeConfig.displayHeight = screenHeight;
                return config;
            }
            case Scene.SCENE_FULLSCREEN: {
                config.defaultQuality = AQuality.QUALITY_480P;
                config.wifiMaxQuality = AQuality.QUALITY_1080P;
                config.mobileMaxQuality = AQuality.QUALITY_720P;

                final AQualityConfig.VolcDisplaySizeConfig displaySizeConfig = new AQualityConfig.VolcDisplaySizeConfig();
                config.displaySizeConfig = displaySizeConfig;

                final int screenWidth = UIUtils.getScreenWidth(SceneUtils.getApplication());
                final int screenHeight = UIUtils.getScreenHeight(SceneUtils.getApplication());

                displaySizeConfig.screenWidth = screenWidth;
                displaySizeConfig.screenHeight = screenHeight;
                displaySizeConfig.displayWidth = Math.max(screenWidth, screenHeight);
                displaySizeConfig.displayHeight = (int) (Math.max(screenWidth, screenHeight) / 16f * 9);
                return config;
            }
            case Scene.SCENE_UNKNOWN:
            case Scene.SCENE_LONG_VIDEO:
            case Scene.SCENE_DETAIL_VIDEO:
            case Scene.SCENE_FEED_VIDEO:
            default: {
                config.defaultQuality = AQuality.QUALITY_480P;
                config.wifiMaxQuality = AQuality.QUALITY_540P;
                config.mobileMaxQuality = AQuality.QUALITY_360P;

                final AQualityConfig.VolcDisplaySizeConfig displaySizeConfig = new AQualityConfig.VolcDisplaySizeConfig();
                config.displaySizeConfig = displaySizeConfig;

                final int screenWidth = UIUtils.getScreenWidth(SceneUtils.getApplication());
                final int screenHeight = UIUtils.getScreenHeight(SceneUtils.getApplication());

                displaySizeConfig.screenWidth = screenWidth;
                displaySizeConfig.screenHeight = screenHeight;
                displaySizeConfig.displayWidth = Math.min(screenWidth, screenHeight);
                displaySizeConfig.displayHeight = (int) (Math.min(screenWidth, screenHeight) / 16f * 9);
                return config;
            }
        }
    }
}
