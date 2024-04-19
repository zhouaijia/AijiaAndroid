
package com.aijia.video.player.playback.event;

import com.aijia.video.player.Player;
import com.aijia.video.player.playback.PlaybackEvent;
import com.aijia.video.player.utils.event.Event;


public class StateBindPlayer extends Event {

    public Player player;

    public StateBindPlayer() {
        super(PlaybackEvent.State.BIND_PLAYER);
    }

    public StateBindPlayer init(Player player) {
        this.player = player;
        return this;
    }

    @Override
    public void recycle() {
        super.recycle();
        this.player = null;
    }
}
