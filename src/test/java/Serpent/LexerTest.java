package Serpent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LexerTest {
    @Test
    void lexEmptyLine() {
        Lexer lexer = new Lexer("");
        Assertions.assertEquals(lexer.nextToken(), new SyntaxToken(0, TokenKind.EndOfFileToken, "\0", null));
    }

    @Test
    void lexKeywords() {
        Lexer lexer = new Lexer("true false");
        SyntaxToken[] tokens = new SyntaxToken[]{
                new SyntaxToken(0, TokenKind.TrueKeyword, "true", "true"),
                new SyntaxToken(4, TokenKind.WhitespaceToken, " ", null),
                new SyntaxToken(5, TokenKind.FalseKeyword, "false", "false"),
                new SyntaxToken(10, TokenKind.EndOfFileToken, "\0", null)
        };

        int i = 0;
        SyntaxToken token;
        do {
            token = lexer.nextToken();
            Assertions.assertEquals(token, tokens[i]);
            i++;
        } while (token.getKind() != TokenKind.EndOfFileToken);
    }

    @Test
    void lexArithmeticExpression() {
        Lexer lexer = new Lexer("12312 + 5 * 13 / 2");
        SyntaxToken[] tokens = new SyntaxToken[]{
                new SyntaxToken(0, TokenKind.NumberToken, "12312", 12312),
                new SyntaxToken(5, TokenKind.WhitespaceToken, " ", null),
                new SyntaxToken(6, TokenKind.PlusToken, "+", null),
                new SyntaxToken(7, TokenKind.WhitespaceToken, " ", null),
                new SyntaxToken(8, TokenKind.NumberToken, "5", 5),
                new SyntaxToken(9, TokenKind.WhitespaceToken, " ", null),
                new SyntaxToken(10, TokenKind.StarToken, "*", null),
                new SyntaxToken(11, TokenKind.WhitespaceToken, " ", null),
                new SyntaxToken(12, TokenKind.NumberToken, "13", 13),
                new SyntaxToken(14, TokenKind.WhitespaceToken, " ", null),
                new SyntaxToken(15, TokenKind.SlashToken, "/", null),
                new SyntaxToken(16, TokenKind.WhitespaceToken, " ", null),
                new SyntaxToken(17, TokenKind.NumberToken, "2", 2),
                new SyntaxToken(18, TokenKind.EndOfFileToken, "\0", null),
        };

        int i = 0;
        SyntaxToken token;
        do {
            token = lexer.nextToken();
            Assertions.assertEquals(tokens[i], token);
            i++;
        } while (token.getKind() != TokenKind.EndOfFileToken);
    }
}