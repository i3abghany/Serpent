package Serpent;

import Serpent.Syntax.Lexer;
import Serpent.Syntax.SyntaxKind;
import Serpent.Syntax.SyntaxToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LexerTest {
    @Test
    void lexEmptyLine() {
        Lexer lexer = new Lexer("");
        Assertions.assertEquals(lexer.nextToken(), new SyntaxToken(0, SyntaxKind.EndOfFileToken, "\0", null));
    }

    @Test
    void lexKeywords() {
        Lexer lexer = new Lexer("true false");
        SyntaxToken[] tokens = new SyntaxToken[]{
                new SyntaxToken(0, SyntaxKind.TrueKeyword, "true", "true"),
                new SyntaxToken(4, SyntaxKind.WhitespaceToken, " ", null),
                new SyntaxToken(5, SyntaxKind.FalseKeyword, "false", "false"),
                new SyntaxToken(10, SyntaxKind.EndOfFileToken, "\0", null)
        };

        int i = 0;
        SyntaxToken token;
        do {
            token = lexer.nextToken();
            Assertions.assertEquals(token, tokens[i]);
            i++;
        } while (token.getKind() != SyntaxKind.EndOfFileToken);
    }

    @Test
    void lexArithmeticExpression() {
        Lexer lexer = new Lexer("12312 + 5 * 13 / 2");
        SyntaxToken[] tokens = new SyntaxToken[]{
                new SyntaxToken(0, SyntaxKind.NumberToken, "12312", 12312),
                new SyntaxToken(5, SyntaxKind.WhitespaceToken, " ", null),
                new SyntaxToken(6, SyntaxKind.PlusToken, "+", null),
                new SyntaxToken(7, SyntaxKind.WhitespaceToken, " ", null),
                new SyntaxToken(8, SyntaxKind.NumberToken, "5", 5),
                new SyntaxToken(9, SyntaxKind.WhitespaceToken, " ", null),
                new SyntaxToken(10, SyntaxKind.StarToken, "*", null),
                new SyntaxToken(11, SyntaxKind.WhitespaceToken, " ", null),
                new SyntaxToken(12, SyntaxKind.NumberToken, "13", 13),
                new SyntaxToken(14, SyntaxKind.WhitespaceToken, " ", null),
                new SyntaxToken(15, SyntaxKind.SlashToken, "/", null),
                new SyntaxToken(16, SyntaxKind.WhitespaceToken, " ", null),
                new SyntaxToken(17, SyntaxKind.NumberToken, "2", 2),
                new SyntaxToken(18, SyntaxKind.EndOfFileToken, "\0", null),
        };

        int i = 0;
        SyntaxToken token;
        do {
            token = lexer.nextToken();
            Assertions.assertEquals(tokens[i], token);
            i++;
        } while (token.getKind() != SyntaxKind.EndOfFileToken);
    }
}