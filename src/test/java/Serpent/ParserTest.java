package Serpent;

import Serpent.Syntax.*;
import org.junit.jupiter.api.Assertions;
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

        if (precedence1 >= precedence2) {
            List<SyntaxNode> children = parsedExpr.getChildren();
            Assertions.assertEquals(children.size(), 3);
            Assertions.assertInstanceOf(BinaryExpression.class, children.get(0));
            Assertions.assertEquals(SyntaxKind.BinaryExpression, children.get(0).getKind());

            Assertions.assertInstanceOf(SyntaxToken.class, children.get(1));
            Assertions.assertEquals(operator2, children.get(1).getKind());

            Assertions.assertInstanceOf(NumberExpression.class, children.get(2));
            Assertions.assertEquals(SyntaxKind.NumberExpression, children.get(2).getKind());

            BinaryExpression left = (BinaryExpression) children.get(0);
            List<SyntaxNode> leftChildren = left.getChildren();
            Assertions.assertEquals(children.size(), 3);

            Assertions.assertInstanceOf(NumberExpression.class, leftChildren.get(0));
            Assertions.assertInstanceOf(SyntaxToken.class, leftChildren.get(1));
            Assertions.assertInstanceOf(NumberExpression.class, leftChildren.get(2));
        } else {
            List<SyntaxNode> children = parsedExpr.getChildren();
            Assertions.assertEquals(children.size(), 3);

            Assertions.assertInstanceOf(NumberExpression.class, children.get(0));
            Assertions.assertEquals(SyntaxKind.NumberExpression, children.get(0).getKind());

            Assertions.assertInstanceOf(SyntaxToken.class, children.get(1));
            Assertions.assertEquals(operator1, children.get(1).getKind());

            Assertions.assertInstanceOf(BinaryExpression.class, children.get(2));
            Assertions.assertEquals(SyntaxKind.BinaryExpression, children.get(2).getKind());

            BinaryExpression right = (BinaryExpression) children.get(2);
            List<SyntaxNode> rightChildren = right.getChildren();
            Assertions.assertEquals(children.size(), 3);

            Assertions.assertInstanceOf(NumberExpression.class, rightChildren.get(0));
            Assertions.assertInstanceOf(SyntaxToken.class, rightChildren.get(1));
            Assertions.assertInstanceOf(NumberExpression.class, rightChildren.get(2));
        }
    }

    private static Stream<Arguments> providesOperatorPairs() {
        List<SyntaxKind> kinds = SyntaxTraits.getBinaryOperators();
        Stream<Arguments> s = Stream.empty();

        for (SyntaxKind kind1 : kinds) {
            for (SyntaxKind kind2 : kinds) {
                s = Stream.concat(s, Stream.of(Arguments.of(kind1, kind2)));
            }
        }

        return s;
    }
}
