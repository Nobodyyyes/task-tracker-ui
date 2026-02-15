package enums;

public enum HabitFrequency {
    DAILY("Ежедневно"),
    WEEKLY("Еженедельно");

    private final String description;

    HabitFrequency(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
