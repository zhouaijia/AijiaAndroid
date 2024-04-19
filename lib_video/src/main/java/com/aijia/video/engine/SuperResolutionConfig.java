
package com.aijia.video.engine;

import java.io.Serializable;

public class SuperResolutionConfig implements Serializable {
    public static final String sSuperResolutionBinPath = "aijia/video/player/bmf";
    public boolean enableSuperResolutionAbility = true;
    public boolean enableSuperResolutionOnStartup = false;
    public boolean enableAsyncInitSuperResolution = false;
    public boolean enableSuperResolutionMaliGPUOpt = true;
    public int maxTextureWidth = 720;
    public int maxTextureHeight = 1280;
}
