package youdrive.today.models;

/**
 * Created by psuhoterin on 25.04.15.
 */
public enum Status {

    NORMAL("normal"),
    BOOKING("booking"),
    USAGE("usage"),
    PARKING("parking");

    private final String text;

    Status(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static Status fromString(String text) {
        if (text != null) {
            for (Status status : Status.values()) {
                if (text.equalsIgnoreCase(status.text)) {
                    return status;
                }
            }
        }
        return null;
    }

}
