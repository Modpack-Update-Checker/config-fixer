package dev.jab125.configfixer.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.jab125.configfixer.api.Context;
import dev.jab125.configfixer.api.instruction.ModifyJsonInstruction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class ConfigfixerTest {
    @Test
    public void testModifyJson() {
        Context context = Context.createContext(Paths.get("build/resources/test/original"), Paths.get("build/resources/test/modified"), Paths.get("build/resources/test/clean"));
        ModifyJsonInstruction modifyJsonInstruction = new ModifyJsonInstruction(context, new Gson().fromJson("{\n" +
                "  \"file\":" +
                " \"seasons.json\",\n" +
                "  \"override\": {\n" +
                "    \"seasonLength\": 865000,\n" +
                "    \"seasonLock.isSeasonLocked\": true,\n" +
                "    \"isSeasonTiedWithSystemTime\": true,\n" +
                "    \"defaultCropConfig.winterModifier\": 0.1,\n" +
                "    \"doAnimalsBreedInWinter\": false\n" +
                "  }\n" +
                "}", JsonObject.class));
        modifyJsonInstruction.invoke();
        Assertions.assertEquals( " {\n" +
                "-  \"seasonLength\": 672000,\n" +
                "+  \"seasonLength\": 865000,\n" +
                "   \"seasonLock\": {\n" +
                "-    \"isSeasonLocked\": false,\n" +
                "+    \"isSeasonLocked\": true,\n" +
                "     \"lockedSeason\": \"SPRING\"\n" +
                "   },\n" +
                "   \"dimensionWhitelist\": [\n" +
                "     \"minecraft:overworld\"\n" +
                "   ],\n" +
                "   \"doTemperatureChanges\": true,\n" +
                "-  \"isSeasonTiedWithSystemTime\": false,\n" +
                "+  \"isSeasonTiedWithSystemTime\": true,\n" +
                "   \"isInNorthHemisphere\": true,\n" +
                "   \"minecraftDefaultFoliage\": {\n" +
                "     \"springColor\": 4764952,\n" +
                "@@ -72,8 +72,8 @@\n" +
                "     \"springModifier\": 1.0,\n" +
                "     \"summerModifier\": 0.8,\n" +
                "     \"fallModifier\": 0.6,\n" +
                "-    \"winterModifier\": 0.0\n" +
                "+    \"winterModifier\": 0.1\n" +
                "   },\n" +
                "   \"cropConfigs\": [],\n" +
                "-  \"doAnimalsBreedInWinter\": true\n" +
                "+  \"doAnimalsBreedInWinter\": false\n" +
                " }\n",  modifyJsonInstruction.diff().get("seasons.json"));
    }
}
