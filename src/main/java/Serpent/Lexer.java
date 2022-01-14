package Serpent;

public class Lexer {
    private final String text;
    private int position;

    public Lexer(String text) {
        this.text = text;
        this.position = 0;
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
            return new SyntaxToken(position, TokenKind.EndOfFileToken, "\0", null);

        if (Character.isDigit(current())) {
            return lexInteger();
        } else if (Character.isWhitespace(current())) {
            return lexWhitespace();
        } else if (Character.isLetter(current())) {
            return lexTextualToken();
        }

        TokenKind kind = switch (current()) {
            case '*' -> TokenKind.StarToken;
            case '+' -> TokenKind.PlusToken;
            case '-' -> TokenKind.MinusToken;
            case '/' -> TokenKind.SlashToken;
            case '^' -> TokenKind.CaretToken;
            case '(' -> TokenKind.OpenParenthesisToken;
            case ')' -> TokenKind.CloseParenthesisToken;
            default -> null;
        };

        if (kind != null) {
            SyntaxToken ret = new SyntaxToken(position, kind, Character.toString(current()), null);
            position++;
            return ret;
        }

        return null;
    }

    private SyntaxToken lexTextualToken() {
        int start = position;
        while (Character.isLetter(current()))
            next();
        String tokenText = text.substring(start, position);
        TokenKind kind = SyntaxTraits.getTextualTokenKind(tokenText);
        return new SyntaxToken(start, kind, tokenText, tokenText);
    }

    private SyntaxToken lexWhitespace() {
        int start = position;
        while (Character.isWhitespace(current()))
            next();
        String tokenText = text.substring(start, position);
        return new SyntaxToken(start, TokenKind.WhitespaceToken, tokenText, null);
    }

    private SyntaxToken lexInteger() {
        int start = position;
        while (Character.isDigit(current()))
            next();
        String tokenText = text.substring(start, position);
        return new SyntaxToken(start, TokenKind.NumberToken, tokenText, Integer.parseInt(tokenText));
    }
}
