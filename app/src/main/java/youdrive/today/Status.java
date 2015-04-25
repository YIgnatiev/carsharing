package youdrive.today;

/**
 * Created by psuhoterin on 25.04.15.
 */
public enum Status {

    NORMAL("normal"),
    BOOKING("booking"),
    USAGE("usage"),
    PARKING("parking");

    private final String text;

    private Status(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
