package Serpent;

import Serpent.Binder.Binder;
import Serpent.Binder.BoundExpression;
import Serpent.Syntax.ExpressionSyntax;
import Serpent.Syntax.Parser;
import Serpent.Syntax.SyntaxKind;
import Serpent.Syntax.SyntaxTraits;
import Serpent.Syntax.SyntaxTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

class EvaluatorTest {

    @ParameterizedTest
    @MethodSource("providesBinaryExpressionOperandsAndOperator")
    void evaluateSimpleExpressions(SyntaxKind operator, Object a, Object b) {
        String expr = String.format(a + " %s " + b, operator.text);
        SyntaxTree ast = new Parser(expr).parse();
        Binder binder = new Binder();
        BoundExpression rootExpression = binder.bindExpression((ExpressionSyntax) ast.getRoot());
        Assertions.assertTrue(binder.getDiagnostics().isEmpty());

        Evaluator evaluator = new Evaluator(rootExpression);
        switch (operator) {
            case StarToken -> Assertions.assertEquals((int) a * (int) b, evaluator.evaluate());
            case SlashToken -> {
                if ((int) b == 0) {
                    evaluator.evaluate();
                    Assertions.assertFalse(evaluator.getDiagnostics().isEmpty());
                } else {
                    Assertions.assertEquals((int) a / (int) b, evaluator.evaluate());
                }
            }
            case CaretToken -> Assertions.assertEquals((int) Math.pow((int) a, (int) b), evaluator.evaluate());
            case MinusToken -> Assertions.assertEquals((int) a - (int) b, evaluator.evaluate());
            case PlusToken -> Assertions.assertEquals((int) a + (int) b, evaluator.evaluate());
            case AmpersandAmpersandToken -> Assertions.assertEquals((boolean) a && (boolean) b, evaluator.evaluate());
            case BarBarToken -> Assertions.assertEquals((boolean) a || (boolean) b, evaluator.evaluate());
            case EqualsEqualsToken -> Assertions.assertEquals(Objects.equals(a, b), evaluator.evaluate());
            case BangEqualsToken -> Assertions.assertEquals(!Objects.equals(a, b), evaluator.evaluate());
            default -> Assertions.fail();
        }
    }

    private static Stream<Arguments> providesBinaryExpressionOperandsAndOperator() {
        List<SyntaxKind> ops = SyntaxTraits.getArithmeticBinaryOperators();
        Stream<Arguments> s = Stream.empty();
        for (SyntaxKind op : ops) {
            for (int i = -3; i <= 3; i++) {
                for (int j = -76; j <= 150; j += 19) {
                    s = Stream.concat(s, Stream.of(Arguments.of(op, i, j)));
                }
            }
        }

        s = Stream.concat(s, Stream.of(
                Arguments.of(SyntaxKind.AmpersandAmpersandToken, false, false),
                Arguments.of(SyntaxKind.AmpersandAmpersandToken, true, false),
                Arguments.of(SyntaxKind.AmpersandAmpersandToken, true, true),
                Arguments.of(SyntaxKind.AmpersandAmpersandToken, false, true),
                Arguments.of(SyntaxKind.BarBarToken, false, false),
                Arguments.of(SyntaxKind.BarBarToken, true, false),
                Arguments.of(SyntaxKind.BarBarToken, true, true),
                Arguments.of(SyntaxKind.BarBarToken, false, true)
            )
        );

        return s;
    }
}