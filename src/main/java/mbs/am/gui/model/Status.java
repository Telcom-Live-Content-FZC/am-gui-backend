package mbs.am.gui.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Status {
    QUARANTINE("QUARANTINE"),
    LOCKED("LOCKED"),
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    SUSPENDED("SUSPENDED"),
    BLOCKED("BLOCKED"),
    DEFAULT("DEFAULT");

    @JsonValue
    private final String value;

    public static Status fromValue(String value) {
        return Arrays.stream(Status.values())
                .filter(s -> s.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(DEFAULT);
    }
}
