package kamysh.exceptions;

import lombok.Getter;

@Getter
public class ArgumentFormatException extends Exception {
    private final String argument;

    public ArgumentFormatException(String argument, String message) {
        super(message);
        this.argument = argument;
    }

    @Override
    public String toString() {
        return "EntryNotFound;" + argument + ";" + getMessage();
    }
}
