
package com.aijia.video.engine;

import androidx.annotation.NonNull;

import com.aijia.video.engine.core.source.Source;
import com.aijia.video.player.Player;
import com.aijia.video.player.source.MediaSource;
import com.aijia.video.player.source.Track;

import java.io.Serializable;
import java.util.List;


public class Config implements Serializable {
    public static final Config DEFAULT = new Config();
    public static final String EXTRA_VOLC_CONFIG = "extra_volc_config";

    @NonNull
    public static Config get(MediaSource mediaSource) {
        if (mediaSource == null) return Config.DEFAULT;

        Config volcConfig = mediaSource.getExtra(Config.EXTRA_VOLC_CONFIG, Config.class);
        if (volcConfig == null) {
            return Config.DEFAULT;
        }
        return volcConfig;
    }

    public static void set(MediaSource mediaSource, Config volcConfig) {
        if (mediaSource == null) return;
        mediaSource.putExtra(EXTRA_VOLC_CONFIG, volcConfig);
    }


    public static final int CODEC_STRATEGY_DISABLE = 0;
    public static final int CODEC_STRATEGY_COST_SAVING_FIRST = Source.KEY_COST_SAVING_FIRST;
    public static final int CODEC_STRATEGY_HARDWARE_DECODE_FIRST = Source.KEY_HARDWARE_DECODE_FIRST;
    public int codecStrategyType;
    @Player.DecoderType
    public int playerDecoderType;
    @Track.EncoderType
    public int sourceEncodeType;
    public boolean enableAudioTrackVolume = false;

    //Todo
    public boolean enableHlsSeamlessSwitch = false;//Editions.isSupportHLSSeamLessSwitch();
    public boolean enableMP4SeamlessSwitch = false;//Editions.isSupportMp4SeamLessSwitch();
    public boolean enableDash = false;//Editions.isSupportDash();
    public boolean enableEngineLooper = false;//Editions.isSupportEngineLooper();
    public boolean enableSeekEnd = true;
    public boolean enableFrameUpdateCallback = false;
    public int firstFrameBufferingTimeoutMS = 0;
    public int playbackBufferingTimeoutMS = 0;
    public boolean enableECDN = false;
    public static String ECDN_FILE_KEY_REGULAR_EXPRESSION;
    public boolean enableTextureRender = true;//Editions.isSupportTextureRender();
    public boolean enableTextureRenderUsingNativeWindow = true;
    public SuperResolutionConfig superResolutionConfig = new SuperResolutionConfig();
    public AQualityConfig qualityConfig;
    public boolean enableSubtitle = false;
    public List<Integer> subtitleLanguageIds;
    public boolean enable403SourceRefreshStrategy;

    public String tag;
    public String subTag;
}
