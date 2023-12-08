package dev.jab125.configfixer.api;

import com.google.gson.JsonObject;
import dev.jab125.configfixer.api.instruction.Instruction;
import dev.jab125.configfixer.impl.ContextImpl;

import java.io.File;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public abstract class Context {

    public static void registerInstruction(String id, BiFunction<Context, JsonObject, Instruction<?>> instruction) {
        ContextImpl.registerInstruction(id, instruction);
    }

    public static Context createContext(Path originalRoot, Path modifiedRoot, Path cleanedRoot) {
        return new ContextImpl(originalRoot, modifiedRoot, cleanedRoot);
    }

    public abstract Path originalRoot();

    public abstract Path modifiedRoot();

    public abstract Path cleanedRoot();

    public abstract Path originalPath(String path);

    public abstract File originalFile(String path);

    public abstract Path modifiedPath(String path);

    public abstract File modifiedFile(String path);

    public abstract Path cleanedPath(String path);

    public abstract File cleanedFile(String path);

    public abstract Result run(JsonObject object);

    public abstract void warn(String string, Instruction<?> instruction);

    public abstract BiConsumer<String, Instruction<?>> getWarn();

    public abstract void setWarn(BiConsumer<String, Instruction<?>> warn);
}
