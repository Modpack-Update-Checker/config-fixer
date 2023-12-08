package dev.jab125.configfixer.api.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtil {
    public static String requiredString(JsonObject jsonObject, String name) {
        if (!jsonObject.has(name)) throw new IllegalArgumentException();
        if (!jsonObject.get(name).isJsonPrimitive()) throw new IllegalArgumentException();
        if (!jsonObject.getAsJsonPrimitive(name).isString()) throw new IllegalArgumentException();
        return jsonObject.getAsJsonPrimitive(name).getAsString();
    }

    public static int requiredInt(JsonObject jsonObject, String name) {
        if (!jsonObject.has(name)) throw new IllegalArgumentException();
        if (!jsonObject.get(name).isJsonPrimitive()) throw new IllegalArgumentException();
        if (!jsonObject.getAsJsonPrimitive(name).isNumber()) throw new IllegalArgumentException();
        return jsonObject.getAsJsonPrimitive(name).getAsInt();
    }

    public static JsonObject requiredObject(JsonObject jsonObject, String name) {
        if (!jsonObject.has(name)) throw new IllegalArgumentException();
        if (!jsonObject.get(name).isJsonObject()) throw new IllegalArgumentException();
        return jsonObject.getAsJsonObject(name);
    }

    public static JsonObject requiredObject(JsonElement jsonElement) {
        if (!jsonElement.isJsonObject()) throw new IllegalArgumentException();
        return jsonElement.getAsJsonObject();
    }

    public static JsonArray requiredArray(JsonObject jsonObject, String name) {
        if (!jsonObject.has(name)) throw new IllegalArgumentException();
        if (!jsonObject.get(name).isJsonArray()) throw new IllegalArgumentException();
        return jsonObject.getAsJsonArray(name);
    }
}
