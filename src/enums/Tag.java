package enums;

public enum Tag {
    WORK("Работа"),
    STUDY("Учеба"),
    PERSONAL("Личное"),
    HEALTH("Здоровье"),
    FINANCE("Финансы"),
    DEFAULT("По умолчанию");

    private final String description;

    Tag(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
