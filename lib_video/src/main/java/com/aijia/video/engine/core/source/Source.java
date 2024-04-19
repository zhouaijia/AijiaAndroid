package com.aijia.video.engine.core.source;

public interface Source {
    int KEY_COST_SAVING_FIRST = 1;
    int KEY_HARDWARE_DECODE_FIRST = 2;

    Type type();

    String vid();

    int codecStrategy();

    Object tag();

    default boolean isCodecStrategyValid() {
        return isCodecStrategyValid(this.codecStrategy());
    }

    static boolean isCodecStrategyValid(int codecStrategy) {
        return codecStrategy == 1 || codecStrategy == 2;
    }

    public static final class EncodeType {
        public static final String H264 = "h264";
        public static final String h265 = "h265";
        public static final String h266 = "h266";

        public EncodeType() {
        }
    }

    public static enum Type {
        DIRECT_URL_SOURCE,
        VID_PLAY_AUTH_TOKEN_SOURCE,
        VIDEO_MODEL_SOURCE;

        private Type() {
        }
    }
}
