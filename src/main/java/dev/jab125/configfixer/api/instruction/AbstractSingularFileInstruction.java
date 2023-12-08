package dev.jab125.configfixer.api.instruction;

import codechicken.diffpatch.cli.DiffOperation;
import codechicken.diffpatch.util.OutputPath;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dev.jab125.configfixer.api.Context;
import dev.jab125.configfixer.impl.ThrowableSupplier;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static dev.jab125.configfixer.api.util.JsonUtil.requiredString;

public abstract class AbstractSingularFileInstruction<T extends AbstractSingularFileInstruction<T>> implements Instruction<T> {

    protected final JsonObject jsonObject;
    protected final String file;
    protected final Context context;
    protected final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public AbstractSingularFileInstruction(Context context, JsonObject jsonObject) {
        this.context = context;
        this.jsonObject = jsonObject;
        file = requiredString(this.jsonObject, "file");
    }

    protected static <U> U shut(ThrowableSupplier<U> o) {
        try {
            return o.get();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> diff() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        shut(() -> DiffOperation.builder().autoHeader(false).summary(false).aPath(context.cleanedPath(file)).bPath(context.modifiedPath(file)).outputPath(new OutputPath.PipePath(outputStream, null)).build().operate());
        HashMap<String, String> map = new HashMap<>();
        map.put(file, outputStream.toString());
        return map;
    }
}
