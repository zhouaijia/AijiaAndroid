
package com.aijia.video.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OptionsDefault implements Options {
    private final Context mContext;
    private final List<Option> mOptions;
    private final Map<String, Option> mMap = new HashMap<>();
    private final Options.UserValues mUserProvider;
    private final Options.RemoteValues mRemoteGetter;

    public OptionsDefault(Context context, List<Option> options, Options.RemoteValues remoteGetter) {
        this(context, options, new InnerUserValues(context), remoteGetter);
    }

    public OptionsDefault(Context context, List<Option> options, Options.UserValues userProvider, Options.RemoteValues remoteGetter) {
        this.mContext = context;
        this.mUserProvider = userProvider;
        this.mRemoteGetter = remoteGetter;
        this.mOptions = options;
        for (Option option : options) {
            option.setup(userProvider, remoteGetter);
            mMap.put(option.key, option);
        }
    }

    @Override
    public Option option(@NonNull String key) {
        final Option option = mMap.get(key);
        if (option != null) return option;
        throw new IllegalArgumentException("item is not defined! " + key);
    }

    @Override
    public List<Option> options() {
        return mOptions;
    }

    @Override
    public Options.UserValues userValues() {
        return mUserProvider;
    }

    @Override
    public Options.RemoteValues remoteValues() {
        return mRemoteGetter;
    }

    static class InnerUserValues implements Options.UserValues {

        private final static String SHARED_PREF_SETTINGS = "shared_pref_vod_client_settings";
        private final SharedPreferences mSharedPref;

        InnerUserValues(Context context) {
            this.mSharedPref = context.getSharedPreferences(SHARED_PREF_SETTINGS, Context.MODE_PRIVATE);
        }

        @Nullable
        @Override
        public Object getValue(Option option) {
            final String value = mSharedPref.getString(option.key, null);
            return Option.string2Obj(value, option.clazz);
        }

        @Override
        public void saveValue(Option option, @Nullable Object value) {
            if (value == null) {
                mSharedPref.edit().remove(option.key).apply();
            } else {
                final String valueStr = Option.obj2String(value, option.clazz);
                mSharedPref.edit().putString(option.key, valueStr).apply();
            }
        }
    }
}
