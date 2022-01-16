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
import java.util.stream.Stream;

class EvaluatorTest {

    @ParameterizedTest
    @MethodSource("providesBinaryExpressionOperandsAndOperator")
    void evaluateSimpleArithmeticExpressions(SyntaxKind operator, int a, int b) {
        String expr = String.format("%d %s %d", a, operator.text, b);
        SyntaxTree ast = new Parser(expr).parse();
        Binder binder = new Binder();
        BoundExpression rootExpression = binder.bindExpression((ExpressionSyntax) ast.getRoot());
        Assertions.assertTrue(binder.getDiagnostics().isEmpty());
        switch (operator) {
            case StarToken -> Assertions.assertEquals(a * b, new Evaluator(rootExpression).evaluate());
            case SlashToken -> {
                Evaluator evaluator = new Evaluator(rootExpression);
                if (b == 0) {
                    evaluator.evaluate();
                    Assertions.assertFalse(evaluator.getDiagnostics().isEmpty());
                } else {
                    Assertions.assertEquals(a / b, evaluator.evaluate());
                }
            }
            case CaretToken -> Assertions.assertEquals((int) Math.pow(a, b), new Evaluator(rootExpression).evaluate());
            case MinusToken -> Assertions.assertEquals(a - b, new Evaluator(rootExpression).evaluate());
            case PlusToken -> Assertions.assertEquals(a + b, new Evaluator(rootExpression).evaluate());
            default -> Assertions.fail();
        }
    }

    private static Stream<Arguments> providesBinaryExpressionOperandsAndOperator() {
        List<SyntaxKind> ops = SyntaxTraits.getArithmeticBinaryOperators();
        Stream<Arguments> s = Stream.empty();
        for (SyntaxKind op : ops) {
            for (int i = -3; i <= 3; i++) {
                for (int j = -60; j <= 150; j += 19) {
                    s = Stream.concat(s, Stream.of(Arguments.of(op, i, j)));
                }
            }
        }
        return s;
    }
}