package enums;

public enum TaskStatus {
    TODO("Надо сделать"),
    IN_PROGRESS("В процессе"),
    DONE("Готово");

    private final String description;

    TaskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
