package com.aijia.webview.bridge.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public final class JsonUtils {

    private static final byte TYPE_BOOLEAN     = 0x00;
    private static final byte TYPE_INT         = 0x01;
    private static final byte TYPE_LONG        = 0x02;
    private static final byte TYPE_DOUBLE      = 0x03;
    private static final byte TYPE_STRING      = 0x04;
    private static final byte TYPE_JSON_OBJECT = 0x05;
    private static final byte TYPE_JSON_ARRAY  = 0x06;

    private JsonUtils() {
        throw new UnsupportedOperationException("You can't instantiate me...");
    }


    /**
     * Checks if a given input is a JSONObject.
     *
     * @param input Anything.
     * @return true if it is a JSONObject.
     */
    public static <T> boolean isJSONObject(final T input) {
        Log.d("JsonUtils", "----->isJSONObject---->"+input);
        return input instanceof JSONObject;
    }

    /**
     * Checks if a given input is a JSONArray
     *
     * @param input Anything.
     * @return true if it is a JSONArray.
     */
    public static <T> boolean isJSONArray(final T input) {
        return input instanceof JSONArray;
    }

    public static boolean getBoolean(final JSONObject jsonObject,
                                     final String key) {
        return getBoolean(jsonObject, key, false);
    }

    public static boolean getBoolean(final JSONObject jsonObject,
                                     final String key,
                                     final boolean defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_BOOLEAN);
    }

    public static boolean getBoolean(final String json,
                                     final String key) {
        return getBoolean(json, key, false);
    }

    public static boolean getBoolean(final String json,
                                     final String key,
                                     final boolean defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_BOOLEAN);
    }

    public static int getInt(final JSONObject jsonObject,
                             final String key) {
        return getInt(jsonObject, key, -1);
    }

    public static int getInt(final JSONObject jsonObject,
                             final String key,
                             final int defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_INT);
    }

    public static int getInt(final String json,
                             final String key) {
        return getInt(json, key, -1);
    }

    public static int getInt(final String json,
                             final String key,
                             final int defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_INT);
    }

    public static long getLong(final JSONObject jsonObject,
                               final String key) {
        return getLong(jsonObject, key, -1);
    }

    public static long getLong(final JSONObject jsonObject,
                               final String key,
                               final long defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_LONG);
    }

    public static long getLong(final String json,
                               final String key) {
        return getLong(json, key, -1);
    }

    public static long getLong(final String json,
                               final String key,
                               final long defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_LONG);
    }

    public static double getDouble(final JSONObject jsonObject,
                                   final String key) {
        return getDouble(jsonObject, key, -1);
    }

    public static double getDouble(final JSONObject jsonObject,
                                   final String key,
                                   final double defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_DOUBLE);
    }

    public static double getDouble(final String json,
                                   final String key) {
        return getDouble(json, key, -1);
    }

    public static double getDouble(final String json,
                                   final String key,
                                   final double defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_DOUBLE);
    }

    public static String getString(final JSONObject jsonObject,
                                   final String key) {
        return getString(jsonObject, key, "");
    }

    public static String getString(final JSONObject jsonObject,
                                   final String key,
                                   final String defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_STRING);
    }

    public static String getString(final String json,
                                   final String key) {
        return getString(json, key, "");
    }

    public static String getString(final String json,
                                   final String key,
                                   final String defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_STRING);
    }

    public static JSONObject getJSONObject(final JSONObject jsonObject,
                                           final String key,
                                           final JSONObject defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_JSON_OBJECT);
    }

    public static JSONObject getJSONObject(final String json,
                                           final String key,
                                           final JSONObject defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_JSON_OBJECT);
    }

    public static JSONArray getJSONArray(final JSONObject jsonObject,
                                         final String key,
                                         final JSONArray defaultValue) {
        return getValueByType(jsonObject, key, defaultValue, TYPE_JSON_ARRAY);
    }

    public static JSONArray getJSONArray(final String json,
                                         final String key,
                                         final JSONArray defaultValue) {
        return getValueByType(json, key, defaultValue, TYPE_JSON_ARRAY);
    }

    private static <T> T getValueByType(final JSONObject jsonObject,
                                        final String key,
                                        final T defaultValue,
                                        final byte type) {
        if (jsonObject == null || key == null || key.length() == 0) {
            return defaultValue;
        }
        try {
            Object ret;
            if (type == TYPE_BOOLEAN) {
                ret = jsonObject.getBoolean(key);
            } else if (type == TYPE_INT) {
                ret = jsonObject.getInt(key);
            } else if (type == TYPE_LONG) {
                ret = jsonObject.getLong(key);
            } else if (type == TYPE_DOUBLE) {
                ret = jsonObject.getDouble(key);
            } else if (type == TYPE_STRING) {
                ret = jsonObject.getString(key);
            } else if (type == TYPE_JSON_OBJECT) {
                ret = jsonObject.getJSONObject(key);
            } else if (type == TYPE_JSON_ARRAY) {
                ret = jsonObject.getJSONArray(key);
            } else {
                return defaultValue;
            }
            //noinspection unchecked
            return (T) ret;
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    private static <T> T getValueByType(final String json,
                                        final String key,
                                        final T defaultValue,
                                        final byte type) {
        if (json == null || json.length() == 0
                || key == null || key.length() == 0) {
            return defaultValue;
        }
        try {
            return getValueByType(new JSONObject(json), key, defaultValue, type);
        } catch (JSONException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static String formatJson(final String json) {
        return formatJson(json, 4);
    }

    public static String formatJson(final String json, final int indentSpaces) {
        try {
            for (int i = 0, len = json.length(); i < len; i++) {
                char c = json.charAt(i);
                if (c == '{') {
                    return new JSONObject(json).toString(indentSpaces);
                } else if (c == '[') {
                    return new JSONArray(json).toString(indentSpaces);
                } else if (!Character.isWhitespace(c)) {
                    return json;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        Log.d("JsonUtils", "----->fromJson---->"+json);
        return new Gson().fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        Log.d("JsonUtils", "----->fromJson---->"+json);
        return new Gson().fromJson(json, typeToken.getType());
    }

    public static int getType(Object json) throws JSONException {
        int type = TYPE_STRING;
        //Object json = new JSONTokener(data).nextValue();

        if (json instanceof JSONObject) {
            type = TYPE_JSON_OBJECT;
        } else if (json instanceof JSONArray) {
            type = TYPE_JSON_ARRAY;
        } else if (json instanceof String) {
            type = TYPE_STRING;
        } else if (json instanceof Integer){
            type = TYPE_INT;
        } else if (json instanceof Long){
            type = TYPE_LONG;
        } else if (json instanceof Double){
            type = TYPE_DOUBLE;
        } else if (json instanceof Boolean){
            type = TYPE_BOOLEAN;
        }
        Log.d("JsonUtils", "----->getType---->"+type+", "+json);

        return type;
    }

    public static boolean isJSONObject(String data) throws JSONException {
        Object json = new JSONTokener(data).nextValue();
        return json instanceof JSONObject;
    }

    public static boolean isJSONArray(String data) throws JSONException {
        Object json = new JSONTokener(data).nextValue();
        return json instanceof JSONArray;
    }

    public static String toJsonString(Object o) {
        return new Gson().toJson(o);
    }
}
