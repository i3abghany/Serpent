package Serpent;

import java.util.Objects;

public class SyntaxToken {
    private final int position;
    private final TokenKind kind;
    private final String text;
    private final Object value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyntaxToken that = (SyntaxToken) o;
        return position == that.position && kind == that.kind && text.equals(that.text) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, kind, text, value);
    }

    public SyntaxToken(int position, TokenKind kind, String text, Object value) {
        this.position = position;
        this.kind = kind;
        this.text = text;
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    public TokenKind getKind() {
        return kind;
    }

    public String getText() {
        return text;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "SyntaxToken{" +
                "position=" + position +
                ", kind=" + kind +
                ", text='" + text + '\'' +
                ", value=" + value +
                '}';
    }
}
