package Serpent.Syntax;

import Serpent.TextSpan;

import java.util.List;
import java.util.Objects;
import java.util.Collections;

public class SyntaxToken extends SyntaxNode {
    private final int position;
    private final SyntaxKind kind;
    private final String text;
    private final Object value;

    public TextSpan getSpan() {
        return new TextSpan(position, text.length());
    }

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

    public SyntaxToken(int position, SyntaxKind kind, String text, Object value) {
        this.position = position;
        this.kind = kind;
        this.text = text;
        this.value = value;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public SyntaxKind getKind() {
        return kind;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return Collections.emptyList();
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
