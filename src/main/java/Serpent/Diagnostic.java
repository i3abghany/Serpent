package Serpent;

public final class Diagnostic {
    private final String message;
    private final TextSpan span;

    public Diagnostic(String message, TextSpan span) {
        this.message = message;
        this.span = span;
    }

    @Override
    public String toString() {
        return message;
    }

    public TextSpan getSpan() {
        return span;
    }
}
