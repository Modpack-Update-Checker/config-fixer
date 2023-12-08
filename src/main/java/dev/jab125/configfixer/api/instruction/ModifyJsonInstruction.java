package dev.jab125.configfixer.api.instruction;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import dev.jab125.configfixer.api.Context;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static dev.jab125.configfixer.api.util.JsonUtil.requiredObject;

public class ModifyJsonInstruction extends AbstractSingularFileInstruction<ModifyJsonInstruction> {

    protected final JsonObject override;
    protected JsonElement jsonFile;

    public ModifyJsonInstruction(Context context, JsonObject jsonObject) {
        super(context, jsonObject);
        override = requiredObject(jsonObject, "override");
    }

    @Override
    public int invoke() {
        jsonFile = readJsonFile(file);
        cleanJsonFile(jsonFile, file);
        GsonJsonProvider gsonJsonProvider = new GsonJsonProvider(GSON);
        ParseContext parseContext = JsonPath.using(Configuration.builder().jsonProvider(gsonJsonProvider).build());
        DocumentContext documentContext = parseContext.parse(jsonFile);
        for (Map.Entry<String, JsonElement> e : override.entrySet()) {
            documentContext.set(e.getKey(), e.getValue());
        }
        writeJsonFile(jsonFile, file);
        return SUCCESS;
    }

    private void writeJsonFile(JsonElement element, String file) {
        context.modifiedPath(file).toFile().getParentFile().mkdirs();
        shut(() -> Files.write(context.modifiedPath(file), GSON.toJson(element).getBytes(StandardCharsets.UTF_8)));
    }

    private void cleanJsonFile(JsonElement element, String file) {
        context.cleanedPath(file).toFile().getParentFile().mkdirs();
        shut(() -> Files.write(context.cleanedPath(file), GSON.toJson(element).getBytes(StandardCharsets.UTF_8)));
    }

    protected JsonElement readJsonFile(String file) {
        Path path = context.originalPath(file);
        return GSON.fromJson(new String(shut(() -> Files.readAllBytes(path))), JsonElement.class);
    }
}
