
package com.aijia.video.scene.ui.video.layer;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aijia.video.player.Player;
import com.aijia.video.player.playback.PlaybackController;
import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.playback.VideoLayer;
import com.aijia.video.player.playback.VideoView;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.scene.VideoSettings;
import com.aijia.video.scene.ui.video.scene.PlayScene;

public class PlayerConfigLayer extends VideoLayer {
    @Nullable
    @Override
    public String tag() {
        return "player_config";
    }

    @Nullable
    @Override
    protected View createView(@NonNull ViewGroup parent) {
        return null;
    }

    @Override
    protected void onBindPlaybackController(@NonNull PlaybackController controller) {
        controller.addPlaybackListener(eventListener);
    }

    @Override
    protected void onUnbindPlaybackController(@NonNull PlaybackController controller) {
        controller.removePlaybackListener(eventListener);
    }

    final Dispatcher.EventListener eventListener = event -> {
        switch (event.code()) {
            case PlaybackEvent.State.BIND_PLAYER:
                VideoView videoView = videoView();
                if (videoView == null) return;
                syncConfigByScene(videoView.getPlayScene());
                break;
        }
    };

    @Override
    public void onVideoViewPlaySceneChanged(int fromScene, int toScene) {
        syncConfigByScene(toScene);
    }

    private void syncConfigByScene(int scene) {
        final Player player = player();
        if (player == null) return;
        if (scene == PlayScene.SCENE_FULLSCREEN) {
            player.setLooping(false);
        } else if (scene == PlayScene.SCENE_SHORT) {
            player.setLooping(VideoSettings.intValue(VideoSettings.SHORT_VIDEO_PLAYBACK_COMPLETE_ACTION) == 0 /* 0 循环播放 */);
        }
    }
}
