package dev.jab125.configfixer.api.instruction;

import java.util.Map;

public interface Instruction<T extends Instruction<T>> {
    int FAILURE = -2;
    int PARTIAL_SUCCESS = -1;
    int SUCCESS = 0;

    int invoke();

    Map<String, String> diff();
}
