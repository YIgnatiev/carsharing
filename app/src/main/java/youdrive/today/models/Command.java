package youdrive.today.models;

/**
 * Created by psuhoterin on 26.04.15.
 */
public enum Command {

    OPEN("open"),
    CLOSE("close"),
    COMPLETE("complete");

    private final String text;

    Command(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static Command fromString(String text) {
        if (text != null) {
            for (Command command : Command.values()) {
                if (text.equalsIgnoreCase(command.text)) {
                    return command;
                }
            }
        }
        return null;
    }

}
