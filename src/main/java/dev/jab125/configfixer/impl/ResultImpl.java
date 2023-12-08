package dev.jab125.configfixer.impl;

import dev.jab125.configfixer.api.Result;

import java.util.Map;

public class ResultImpl implements Result {

    private final Map<String, String> diffs;

    public ResultImpl(Map<String, String> diffs) {
        this.diffs = diffs;
    }

    @Override
    public Map<String, String> getDiffs() {
        return this.diffs;
    }
}
