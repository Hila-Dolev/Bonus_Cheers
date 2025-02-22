package entity;

public enum PriorityLevel {
	ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private final int value;

    // קונסטרקטור שמקבל את הערך
    PriorityLevel(int value) {
        this.value = value;
    }

    // מתודה שמחזירה את הערך
    public int getValue() {
        return value;
    }

}
