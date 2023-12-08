package dev.jab125.configfixer.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.jab125.configfixer.api.Context;
import dev.jab125.configfixer.api.Result;
import dev.jab125.configfixer.api.instruction.Instruction;
import dev.jab125.configfixer.api.instruction.ModifyJsonInstruction;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static dev.jab125.configfixer.api.util.JsonUtil.*;

public class ContextImpl extends Context {
    private static final HashMap<String, BiFunction<Context, JsonObject, Instruction<?>>> INSTRUCTIONS = new HashMap<>();

    private final Path original;
    private final Path modified;
    private final Path cleaned;
    private BiConsumer<String, Instruction<?>> warn;

    public ContextImpl(Path original, Path modified, Path cleaned) {
        this.original = original;
        this.modified = modified;
        this.cleaned = cleaned;
        registerInstruction("c:modify_json_values", ModifyJsonInstruction::new);
    }

    public static void registerInstruction(String id, BiFunction<Context, JsonObject, Instruction<?>> instructionBiFunction) {
        INSTRUCTIONS.put(id, instructionBiFunction);
    }

    @Override
    public Path originalRoot() {
        return original;
    }

    @Override
    public Path modifiedRoot() {
        return modified;
    }

    @Override
    public Path cleanedRoot() {
        return cleaned;
    }

    @Override
    public Path originalPath(String path) {
        return original.resolve(path);
    }

    @Override
    public File originalFile(String path) {
        return original.resolve(path).toFile();
    }

    @Override
    public Path modifiedPath(String path) {
        return modified.resolve(path);
    }

    @Override
    public File modifiedFile(String path) {
        return modified.resolve(path).toFile();
    }

    @Override
    public Path cleanedPath(String path) {
        return cleaned.resolve(path);
    }

    @Override
    public File cleanedFile(String path) {
        return cleaned.resolve(path).toFile();
    }

    @Override
    public Result run(JsonObject object) {
        HashMap<String, String> diffs = new HashMap<>();
        ArrayList<Instruction<?>> instructions = new ArrayList<>();
        int schemaVersion = requiredInt(object, "schemaVersion");
        if (schemaVersion != 1) {
            throw new IllegalArgumentException("This version of config-fixer does not support schema version " + schemaVersion + "!");
        }
        String id = requiredString(object, "id");
        JsonArray jsonArray = requiredArray(object, "instructions");
        for (JsonElement jsonElement : jsonArray) {
            JsonObject object1 = requiredObject(jsonElement);
            String id1 = requiredString(object1, "id");
            BiFunction<Context, JsonObject, Instruction<?>> instruction = INSTRUCTIONS.getOrDefault(id1, null);
            if (instruction == null) {
                throw new IllegalArgumentException("This version of config-fixer does not support the " + id1 + " instruction!");
            }
            Instruction<?> apply = instruction.apply(this, object1);
            int returnValue;
            try {
                returnValue = apply.invoke();
            } catch (Throwable t) {
                throw new RuntimeException(); // treat as failure
            }
            if (returnValue == Instruction.FAILURE) throw new RuntimeException();
            if (returnValue == Instruction.PARTIAL_SUCCESS) {
                warn("Partial Success", apply);
            }
            instructions.add(apply);
        }
        for (Instruction<?> instruction : instructions) {
            diffs.putAll(instruction.diff());
        }
        return new ResultImpl(diffs);
    }

    @Override
    public void warn(String string, Instruction<?> instruction) {
        this.warn.accept(string, instruction);
    }

    @Override
    public BiConsumer<String, Instruction<?>> getWarn() {
        return this.warn;
    }

    @Override
    public void setWarn(BiConsumer<String, Instruction<?>> warn) {
        this.warn = warn;
    }
}
