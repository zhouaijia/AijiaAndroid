
package com.aijia.video.player.playback;

import androidx.annotation.NonNull;

import com.aijia.video.player.Player;
import com.aijia.video.player.source.MediaSource;

public interface PlayerPool {

    PlayerPool DEFAULT = new DefaultPlayerPool();

    @NonNull
    Player acquire(@NonNull MediaSource source, Player.Factory factory);

    Player get(@NonNull MediaSource source);

    void recycle(@NonNull Player player);
}
