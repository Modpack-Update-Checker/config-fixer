package dev.jab125.configfixer.api;

import java.util.Map;

public interface Result {
    /**
     * Decorative, might not accurately represent what's changed.
     */
    Map<String, String> getDiffs();
}
