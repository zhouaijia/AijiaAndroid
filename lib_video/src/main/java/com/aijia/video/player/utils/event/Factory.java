
package com.aijia.video.player.utils.event;

class Factory {
    static <T extends Event> T create(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException | InstantiationException |
                 NullPointerException e ) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }
}
