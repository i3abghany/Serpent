package Serpent;

import Serpent.Syntax.Lexer;
import Serpent.Syntax.SyntaxKind;
import Serpent.Syntax.SyntaxToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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
                new SyntaxToken(0, SyntaxKind.TrueKeyword, "true", true),
                new SyntaxToken(4, SyntaxKind.WhitespaceToken, " ", null),
                new SyntaxToken(5, SyntaxKind.FalseKeyword, "false", false),
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

    @ParameterizedTest
    @MethodSource("getTokensWithText")
    void lexSingleTokens(SyntaxKind kind, String text, Object value) {
        Lexer lexer = new Lexer(text);
        Assertions.assertEquals(new SyntaxToken(0, kind, text, value), lexer.nextToken());
    }

    private static Stream<Arguments> getTokensWithText() {
        Stream<Arguments> args = Stream.empty();
        for (SyntaxKind kind : SyntaxKind.values()) {
            if (kind.text != null) {
                if (kind == SyntaxKind.TrueKeyword || kind == SyntaxKind.FalseKeyword) {
                    args = Stream.concat(args, Stream.of(Arguments.of(kind, kind.text, kind == SyntaxKind.TrueKeyword)));
                } else {
                    args = Stream.concat(args, Stream.of(Arguments.of(kind, kind.text, null)));
                }
            }
        }

        args = Stream.concat(args, Stream.of(
                Arguments.of(SyntaxKind.NumberToken, "1", 1),
                Arguments.of(SyntaxKind.NumberToken, "0", 0),
                Arguments.of(SyntaxKind.NumberToken, "12345", 12345),
                Arguments.of(SyntaxKind.IdentifierToken, "a", "a"),
                Arguments.of(SyntaxKind.IdentifierToken, "ASD", "ASD"),
                Arguments.of(SyntaxKind.WhitespaceToken, " ", null),
                Arguments.of(SyntaxKind.WhitespaceToken, "\t", null),
                Arguments.of(SyntaxKind.WhitespaceToken, "\t ", null),
                Arguments.of(SyntaxKind.WhitespaceToken, "\n ", null),
                Arguments.of(SyntaxKind.WhitespaceToken, "\r\n ", null)
        ));

        return args;
    }

    @ParameterizedTest
    @MethodSource("providesBadTokens")
    void lexBadTokens(String badToken) {
        Lexer lexer = new Lexer(badToken);
        SyntaxToken token = lexer.nextToken();
        Assertions.assertEquals(SyntaxKind.BadToken, token.getKind());
        Assertions.assertFalse(lexer.getDiagnostics().isEmpty());
    }

    private static Stream<Arguments> providesBadTokens() {
        return Stream.of(
                Arguments.of("@"),
                Arguments.of("#"),
                Arguments.of("$"),
                Arguments.of("&"),
                Arguments.of("_"),
                Arguments.of("\\"),
                Arguments.of("×"),
                Arguments.of("ٍ"),
                Arguments.of("©")
        );
    }
}