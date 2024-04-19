
package com.aijia.video.player.utils.concurrent;


public interface Cancelable {

    void cancel(boolean notify, boolean interrupt, String reason);

    boolean isCanceled();
}
