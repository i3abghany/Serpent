package Serpent.Syntax;

import Serpent.DiagnosticList;

import java.util.ArrayList;

public class Lexer {
    private final String text;
    private int position;
    private final DiagnosticList diagnostics = new DiagnosticList();

    public Lexer(String text) {
        this.text = text;
        this.position = 0;
    }

    public DiagnosticList getDiagnostics() {
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

        String currentChar = Character.toString(current());
        switch (current()) {
            case '*':
                return new SyntaxToken(position++, SyntaxKind.StarToken, currentChar, null);
            case '+':
                return new SyntaxToken(position++, SyntaxKind.PlusToken, currentChar, null);
            case '-':
                return new SyntaxToken(position++, SyntaxKind.MinusToken, currentChar, null);
            case '/':
                return new SyntaxToken(position++, SyntaxKind.SlashToken, currentChar, null);
            case '^':
                return new SyntaxToken(position++, SyntaxKind.CaretToken, currentChar, null);
            case '(':
                return new SyntaxToken(position++, SyntaxKind.OpenParenthesisToken, currentChar, null);
            case ')':
                return new SyntaxToken(position++, SyntaxKind.CloseParenthesisToken, currentChar, null);
            case '!':
                return new SyntaxToken(position++, SyntaxKind.BangToken, currentChar, null);
            case '&': {
                if (lookahead() == '&') {
                    position += 2;
                    return new SyntaxToken(position - 2, SyntaxKind.AmpersandAmpersandToken, "&&", null);
                }
            }
            case '|': {
                if (lookahead() == '|') {
                    position += 2;
                    return new SyntaxToken(position - 2, SyntaxKind.BarBarToken, "||", null);
                }
            }
        }

        diagnostics.reportBadCharacterInput(position, current());
        return new SyntaxToken(position++, SyntaxKind.BadToken, currentChar, null);
    }

    private SyntaxToken lexTextualToken() {
        int start = position;
        while (Character.isLetter(current()))
            next();
        String tokenText = text.substring(start, position);
        return constructTextualToken(start, tokenText);
    }

    private SyntaxToken constructTextualToken(int start, String tokenText) {
        SyntaxKind kind = SyntaxTraits.getTextualTokenKind(tokenText);
        return switch (kind) {
            case IdentifierToken -> new SyntaxToken(start, kind, tokenText, tokenText);
            case TrueKeyword, FalseKeyword -> new SyntaxToken(start, kind, tokenText, kind == SyntaxKind.TrueKeyword);
            default -> null;
        };
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
