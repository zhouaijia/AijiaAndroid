
package com.aijia.video.player.playback;

import androidx.annotation.NonNull;

import com.aijia.video.player.Player;
import com.aijia.video.player.PlayerEvent;
import com.aijia.video.player.source.MediaSource;
import com.aijia.video.player.utils.L;
import com.aijia.video.player.utils.event.Dispatcher;
import com.aijia.video.player.utils.event.Event;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultPlayerPool implements PlayerPool {

    private final Map<String, Player> mAcquiredPlayers = Collections.synchronizedMap(new LinkedHashMap<>());

    @NonNull
    @Override
    public Player acquire(@NonNull MediaSource source, Player.Factory factory) {
        Player player = get(source);
        if (player != null) {
            if (player.isError() || player.isReleased()) {
                recycle(player);
                player = null;
            }
        }
        if (player == null) {
            player = create(source, factory);
        }
        L.d(this, "acquire", source, player);
        return player;
    }

    @NonNull
    private Player create(@NonNull MediaSource source, @NonNull Player.Factory factory) {
        Player player = factory.create(source);
        player.addPlayerListener(new Dispatcher.EventListener() {
            @Override
            public void onEvent(Event event) {
                if (event.code() == PlayerEvent.Action.RELEASE) {
                    Player p = event.owner(Player.class);
                    p.removePlayerListener(this);
                    recycle(source);
                }
            }
        });
        mAcquiredPlayers.put(key(source), player);
        return player;
    }

    @Override
    public Player get(@NonNull MediaSource source) {
        return mAcquiredPlayers.get(key(source));
    }

    @Override
    public void recycle(@NonNull Player player) {
        L.d(this, "recycle", player.getDataSource(), player);
        synchronized (mAcquiredPlayers) {
            Map<String, Player> copy = new LinkedHashMap<>(mAcquiredPlayers);
            for (Map.Entry<String, Player> entry : copy.entrySet()) {
                if (entry.getValue() == player) {
                    mAcquiredPlayers.remove(entry.getKey());
                }
            }
        }
        player.release();
    }

    private void recycle(@NonNull MediaSource source) {
        Player o = mAcquiredPlayers.remove(key(source));
        if (o != null) {
            L.d(this, "recycle by player", source, o);
        }
    }

    private String key(@NonNull MediaSource mediaSource) {
        return mediaSource.getUniqueId();
    }
}