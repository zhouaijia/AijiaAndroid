
package com.aijia.video.engine;

import com.aijia.video.player.source.Quality;

import java.io.Serializable;

public class AQualityConfig implements Serializable {
    public boolean enableStartupABR;
    public Quality defaultQuality;
    public Quality wifiMaxQuality;
    public Quality mobileMaxQuality;
    public Quality userSelectedQuality;
    public VolcDisplaySizeConfig displaySizeConfig;
    public boolean enableSupperResolutionDowngrade;

    public static class VolcDisplaySizeConfig {
        public int screenWidth;
        public int screenHeight;
        public int displayWidth;
        public int displayHeight;
    }
}
