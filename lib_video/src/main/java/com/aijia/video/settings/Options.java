
package com.aijia.video.settings;

import androidx.annotation.Nullable;

import java.util.List;


public interface Options {

    interface RemoteValues {
        @Nullable
        Object getValue(Option option);
    }

    interface UserValues {

        @Nullable
        Object getValue(Option option);

        void saveValue(Option option, @Nullable Object value);
    }

    Option option(String key);

    List<Option> options();

    UserValues userValues();

    RemoteValues remoteValues();
}
