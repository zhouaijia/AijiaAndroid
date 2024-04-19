
package com.aijia.video.scene.ui.video.scene.shortvideo.layer;

import com.aijia.video.scene.ui.video.layer.SimpleProgressBarLayer;

public class ShortVideoProgressBarLayer extends SimpleProgressBarLayer {

    @Override
    public void show() {
        // PM: Only show progress bar when duration > 1 Min
        super.show();
    }
}
