package mbs.am.gui.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ThreatAction {
    BLOCKED_DEVICE("BLOCK_DEVICE"),
    ALERT_ONLY("ALERT_ONLY");

    @JsonValue
    private final String value;

    public static ThreatAction fromValue(String value) {
        return Arrays.stream(ThreatAction.values())
                .filter(s -> s.value.equalsIgnoreCase(value))
                .findFirst()
                .orElse(BLOCKED_DEVICE);
    }
}
