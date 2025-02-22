package entity;

public enum PriorityLevel {
	ZERO(0),
    LOW(1),
    MEDIUM(2),
    HIGH(3),
    URGENT(4),
    VERY_URGENT(5);

    private final int value;

    // קונסטרקטור שמקבל את הערך
    PriorityLevel(int value) {
        this.value = value;
    }

    // מתודה שמחזירה את הערך
    public int getValue() {
        return value;
    }
    
    public static PriorityLevel fromInt(int i) {
        for (PriorityLevel level : values()) {
            if (level.getValue() == i) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid priority level: " + i);
    }

}
