package Serpent;

import Serpent.Syntax.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

public class ParserTest {

    @ParameterizedTest
    @MethodSource("providesOperatorPairs")
    void parseWithPrecedence(SyntaxKind operator1, SyntaxKind operator2) {
        int precedence1 = SyntaxTraits.getBinaryOperatorPrecedence(operator1);
        int precedence2 = SyntaxTraits.getBinaryOperatorPrecedence(operator2);

        String expr = String.format("1 %s 2 %s 3", operator1.text, operator2.text);
        SyntaxNode parsedExpr = new Parser(expr).parse().getRoot();

        Assertions.assertInstanceOf(BinaryExpression.class, parsedExpr);

        List<SyntaxNode> children = parsedExpr.getChildren();
        Assertions.assertEquals(children.size(), 3);

        if (precedence1 >= precedence2) {
            Assertions.assertInstanceOf(BinaryExpression.class, children.get(0));
            Assertions.assertEquals(SyntaxKind.BinaryExpression, children.get(0).getKind());

            Assertions.assertInstanceOf(SyntaxToken.class, children.get(1));
            Assertions.assertEquals(operator2, children.get(1).getKind());

            Assertions.assertInstanceOf(LiteralExpression.class, children.get(2));
            Assertions.assertEquals(SyntaxKind.LiteralExpression, children.get(2).getKind());

            BinaryExpression left = (BinaryExpression) children.get(0);
            List<SyntaxNode> leftChildren = left.getChildren();
            Assertions.assertEquals(children.size(), 3);

            Assertions.assertInstanceOf(LiteralExpression.class, leftChildren.get(0));
            Assertions.assertInstanceOf(SyntaxToken.class, leftChildren.get(1));
            Assertions.assertInstanceOf(LiteralExpression.class, leftChildren.get(2));
        } else {
            Assertions.assertInstanceOf(LiteralExpression.class, children.get(0));
            Assertions.assertEquals(SyntaxKind.LiteralExpression, children.get(0).getKind());

            Assertions.assertInstanceOf(SyntaxToken.class, children.get(1));
            Assertions.assertEquals(operator1, children.get(1).getKind());

            Assertions.assertInstanceOf(BinaryExpression.class, children.get(2));
            Assertions.assertEquals(SyntaxKind.BinaryExpression, children.get(2).getKind());

            BinaryExpression right = (BinaryExpression) children.get(2);
            List<SyntaxNode> rightChildren = right.getChildren();
            Assertions.assertEquals(children.size(), 3);

            Assertions.assertInstanceOf(LiteralExpression.class, rightChildren.get(0));
            Assertions.assertInstanceOf(SyntaxToken.class, rightChildren.get(1));
            Assertions.assertInstanceOf(LiteralExpression.class, rightChildren.get(2));
        }
    }

    private static Stream<Arguments> providesOperatorPairs() {
        List<SyntaxKind> kinds = SyntaxTraits.getAllBinaryOperators();
        Stream<Arguments> s = Stream.empty();

        for (SyntaxKind kind1 : kinds) {
            for (SyntaxKind kind2 : kinds) {
                s = Stream.concat(s, Stream.of(Arguments.of(kind1, kind2)));
            }
        }

        return s;
    }

    @Test
    void testParenthesizedExpressionParsing() {
        String expr = "(true && false)";

        Parser parser = new Parser(expr);
        SyntaxTree ast = parser.parse();
        Assertions.assertTrue(ast.getDiagnostics().isEmpty());

        SyntaxNode root = ast.getRoot();
        Assertions.assertInstanceOf(ParenthesizedExpression.class, root);
        Assertions.assertEquals(root.getKind(), SyntaxKind.ParenthesizedExpression);

        List<SyntaxNode> children = root.getChildren();
        Assertions.assertInstanceOf(SyntaxToken.class, children.get(0));
        Assertions.assertInstanceOf(BinaryExpression.class, children.get(1));
        Assertions.assertInstanceOf(SyntaxToken.class, children.get(2));

        var pe = (ParenthesizedExpression) root;
        Assertions.assertEquals(pe.getOpenParen(), children.get(0));
        Assertions.assertEquals(pe.getExpression(), children.get(1));
        Assertions.assertEquals(pe.getCloseParen(), children.get(2));
    }

    @Test
    void testConsumedTokens() {
        String expr = "(true && false) || 123  +    456";
        var parser = new Parser(expr);
        var ast = parser.parse();

        var tokens = parser.getTokenArray();

        SyntaxToken[] expectedTokens = new SyntaxToken[] {
            new SyntaxToken(expr.indexOf('('), SyntaxKind.OpenParenthesisToken, "(", null),
            new SyntaxToken(expr.indexOf("true"), SyntaxKind.TrueKeyword, "true", true),
            new SyntaxToken(expr.indexOf("&&"), SyntaxKind.AmpersandAmpersandToken, "&&", null),
            new SyntaxToken(expr.indexOf("false"), SyntaxKind.FalseKeyword, "false", false),
            new SyntaxToken(expr.indexOf(")"), SyntaxKind.CloseParenthesisToken, ")", null),
            new SyntaxToken(expr.indexOf("||"), SyntaxKind.BarBarToken, "||", null),
            new SyntaxToken(expr.indexOf("123"), SyntaxKind.NumberToken, "123", 123),
            new SyntaxToken(expr.indexOf("+"), SyntaxKind.PlusToken, "+", null),
            new SyntaxToken(expr.indexOf("456"), SyntaxKind.NumberToken, "456", 456),
            new SyntaxToken(expr.length(), SyntaxKind.EndOfFileToken, "\0", null),
        };

        for (int i = 0; i < expectedTokens.length; i++) {
            Assertions.assertEquals(expectedTokens[i], tokens[i]);
        }

        Assertions.assertEquals(ast.getEofToken(), expectedTokens[expectedTokens.length - 1]);
    }
}
