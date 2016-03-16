package youdrive.today.models;

/**
 * Created by psuhoterin on 25.04.15.
 */
public enum Result {

    NEW("new"),
    PROCESSING("processing"),
    DONE("done"),
    ERROR("error");

    private final String text;

    Result(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static Result fromString(String text) {
        if (text != null) {
            for (Result result : Result.values()) {
                if (text.equalsIgnoreCase(result.text)) {
                    return result;
                }
            }
        }
        return null;
    }

}
