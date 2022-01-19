package Serpent;

public record Diagnostic(String message, TextSpan span) {

    @Override
    public String toString() {
        return message;
    }
}
