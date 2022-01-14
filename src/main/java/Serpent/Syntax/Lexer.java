package Serpent.Syntax;

import java.util.ArrayList;

public class Lexer {
    private final String text;
    private int position;
    private final ArrayList<String> diagnostics = new ArrayList<>();

    public Lexer(String text) {
        this.text = text;
        this.position = 0;
    }

    public ArrayList<String> getDiagnostics() {
        return diagnostics;
    }

    private void next() {
        position++;
    }

    private char peek(int offset) {
        int idx = position + offset;

        if (idx >= text.length()) return '\0';
        else return text.charAt(idx);
    }

    private char current() {
        return peek(0);
    }

    private char lookahead() {
        return peek(1);
    }

    public SyntaxToken nextToken() {
        if (position >= text.length())
            return new SyntaxToken(position, SyntaxKind.EndOfFileToken, "\0", null);

        if (Character.isDigit(current())) {
            return lexInteger();
        } else if (Character.isWhitespace(current())) {
            return lexWhitespace();
        } else if (Character.isLetter(current())) {
            return lexTextualToken();
        }

        SyntaxKind kind = switch (current()) {
            case '*' -> SyntaxKind.StarToken;
            case '+' -> SyntaxKind.PlusToken;
            case '-' -> SyntaxKind.MinusToken;
            case '/' -> SyntaxKind.SlashToken;
            case '^' -> SyntaxKind.CaretToken;
            case '(' -> SyntaxKind.OpenParenthesisToken;
            case ')' -> SyntaxKind.CloseParenthesisToken;
            default -> SyntaxKind.BadToken;
        };

        if (kind == SyntaxKind.BadToken) {
            diagnostics.add("[Lexer Error]: Unexpected token: " + current());
        }

        SyntaxToken ret = new SyntaxToken(position, kind, Character.toString(current()), null);
        position++;
        return ret;
    }

    private SyntaxToken lexTextualToken() {
        int start = position;
        while (Character.isLetter(current()))
            next();
        String tokenText = text.substring(start, position);
        SyntaxKind kind = SyntaxTraits.getTextualTokenKind(tokenText);
        return new SyntaxToken(start, kind, tokenText, tokenText);
    }

    private SyntaxToken lexWhitespace() {
        int start = position;
        while (Character.isWhitespace(current()))
            next();
        String tokenText = text.substring(start, position);
        return new SyntaxToken(start, SyntaxKind.WhitespaceToken, tokenText, null);
    }

    private SyntaxToken lexInteger() {
        int start = position;
        while (Character.isDigit(current()))
            next();
        String tokenText = text.substring(start, position);
        return new SyntaxToken(start, SyntaxKind.NumberToken, tokenText, Integer.parseInt(tokenText));
    }
}
