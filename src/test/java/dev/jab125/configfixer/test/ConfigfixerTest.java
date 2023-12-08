package dev.jab125.configfixer.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.jab125.configfixer.api.Context;
import dev.jab125.configfixer.api.instruction.ModifyJsonInstruction;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class ConfigfixerTest {
    @Test
    public void testModifyJson() {
        System.out.println(Paths.get("original").toAbsolutePath());
        Context context = Context.createContext(Paths.get("build/resources/test/original"), Paths.get("build/resources/test/modified"), Paths.get("build/resources/test/clean"));
        new ModifyJsonInstruction(context, new Gson().fromJson("{\n" +
                "  \"file\":" +
                " \"seasons.json\",\n" +
                "  \"override\": {\n" +
                "    \"seasonLength\": 865000,\n" +
                "    \"seasonLock.isSeasonLocked\": true,\n" +
                "    \"isSeasonTiedWithSystemTime\": true,\n" +
                "    \"defaultCropConfig.winterModifier\": 0.1,\n" +
                "    \"doAnimalsBreedInWinter\": false\n" +
                "  }\n" +
                "}", JsonObject.class)).invoke();
    }
}
