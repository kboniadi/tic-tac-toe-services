package io.github.coreutils.proj.enginedata;

/**
 * Enum used for the board tokens
 * @author Kord Boniadi
 */
public enum Token {
    X('X'),
    O('O'),
    BLANK(' ');

    private final char token;   // holds actual value of enum

    /**
     * Constructor
     * @param token value of specific enum
     * @author Kord Boniadi
     */
    Token(char token) {
        this.token = token;
    }

    /**
     * Checks if token is present
     * @return true or false
     */
    public boolean isBlank() {
        return this != BLANK;
    }

    /**
     * Gets the actual value of the enum
     * @return (char) value
     */
    public char getToken() {
        return this.token;
    }

    /**
     * String representation of value of the enum.
     * @return (String) representation
     * @author Kord Boniadi
     */
    @Override
    public String toString() {
        return String.valueOf(this.token);
    }
}
