
package com.aijia.video.player.utils;

import org.json.JSONException;

public interface Parser<T> {

    T parse() throws JSONException;

}
