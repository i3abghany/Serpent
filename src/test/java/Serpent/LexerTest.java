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
    void lexWhitespaceSeparatedArithmeticExpression() {
        String expr = "12312 + 5 * 13 / 2";
        Lexer lexer = new Lexer(expr);
        SyntaxToken[] tokens = new SyntaxToken[]{
                new SyntaxToken(expr.indexOf("12312"), SyntaxKind.NumberToken, "12312", 12312),
                new SyntaxToken(expr.indexOf("+"), SyntaxKind.PlusToken, "+", null),
                new SyntaxToken(expr.indexOf("5"), SyntaxKind.NumberToken, "5", 5),
                new SyntaxToken(expr.indexOf("*"), SyntaxKind.StarToken, "*", null),
                new SyntaxToken(expr.indexOf("13"), SyntaxKind.NumberToken, "13", 13),
                new SyntaxToken(expr.indexOf("/"), SyntaxKind.SlashToken, "/", null),
                new SyntaxToken(expr.length() - 1, SyntaxKind.NumberToken, "2", 2),
        };

        int i = 0;
        boolean turn = false;
        SyntaxToken token = lexer.nextToken();
        while (token.getKind() != SyntaxKind.EndOfFileToken) {
            if (!turn) {
                Assertions.assertEquals(tokens[i], token);
                i++;
            } else {
                Assertions.assertEquals(SyntaxKind.WhitespaceToken, token.getKind());
                Assertions.assertEquals(" ", token.getText());
                Assertions.assertNull(token.getValue());
            }
            turn = !turn;
            token = lexer.nextToken();
        }
    }
}