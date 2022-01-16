package Serpent;

import Serpent.Syntax.Lexer;
import Serpent.Syntax.SyntaxKind;
import Serpent.Syntax.SyntaxToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.ArgumentUtils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
                new SyntaxToken(expr.indexOf("+"), SyntaxKind.PlusToken, "+", "+"),
                new SyntaxToken(expr.indexOf("5"), SyntaxKind.NumberToken, "5", 5),
                new SyntaxToken(expr.indexOf("*"), SyntaxKind.StarToken, "*", "*"),
                new SyntaxToken(expr.indexOf("13"), SyntaxKind.NumberToken, "13", 13),
                new SyntaxToken(expr.indexOf("/"), SyntaxKind.SlashToken, "/", "/"),
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

    @Test
    void lexSingleTokens() {
        ArrayList<KindInfo> infos = getTokensWithText();

        for (KindInfo kindInfo : infos) {
            Lexer lexer = new Lexer(kindInfo.text);
            Assertions.assertEquals(new SyntaxToken(0, kindInfo.kind, kindInfo.text, kindInfo.value), lexer.nextToken());
        }

    }

    private record KindInfo(SyntaxKind kind, String text, Object value) {}

    private ArrayList<KindInfo> getTokensWithText() {
        ArrayList<KindInfo> kinds = new ArrayList<>();
        for (SyntaxKind kind : SyntaxKind.values()) {
            if (kind.text != null) {
                if (kind == SyntaxKind.TrueKeyword || kind == SyntaxKind.FalseKeyword) {
                    kinds.add(new KindInfo(kind, kind.text, kind == SyntaxKind.TrueKeyword));
                } else {
                    kinds.add(new KindInfo(kind, kind.text, kind.text));
                }
            }
        }

        ArrayList<KindInfo> dynamicKinds = new ArrayList<>(List.of(
                new KindInfo(SyntaxKind.NumberToken, "1", 1),
                new KindInfo(SyntaxKind.NumberToken, "0", 0),
                new KindInfo(SyntaxKind.NumberToken, "12345", 12345),
                new KindInfo(SyntaxKind.IdentifierToken, "a", "a"),
                new KindInfo(SyntaxKind.IdentifierToken, "ASD", "ASD"),
                new KindInfo(SyntaxKind.WhitespaceToken, " ", null),
                new KindInfo(SyntaxKind.WhitespaceToken, "\t", null),
                new KindInfo(SyntaxKind.WhitespaceToken, "\t ", null),
                new KindInfo(SyntaxKind.WhitespaceToken, "\n ", null),
                new KindInfo(SyntaxKind.WhitespaceToken, "\r\n ", null)
        ));

        kinds.addAll(dynamicKinds);
        return kinds;
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