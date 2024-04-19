
package com.aijia.video.engine;

import com.aijia.video.player.source.MediaSource;

public interface ConfigUpdater {
    void updateVolcConfig(MediaSource mediaSource);

    ConfigUpdater DEFAULT = new ConfigUpdater() {
        @Override
        public void updateVolcConfig(MediaSource mediaSource) {

        }
    };
}