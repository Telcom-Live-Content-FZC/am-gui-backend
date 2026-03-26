package mbs.am.gui.model;

public enum PolicyState {

    ARCHIVED(0),
    ACTIVE(1),
    PENDING(2),
    REJECTED(3);

    private final int code;

    PolicyState(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static PolicyState fromCode(Integer code) {
        if (code == null) {
            throw new IllegalArgumentException("State code cannot be null");
        }
        for (PolicyState state : values()) {
            if (state.getCode() == code) {
                return state;
            }
        }
        throw new IllegalArgumentException("Unknown PolicyState code: " + code);
    }
}
