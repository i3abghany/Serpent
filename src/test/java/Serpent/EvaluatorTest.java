package Serpent;

import Serpent.Syntax.Parser;
import Serpent.Syntax.SyntaxKind;
import Serpent.Syntax.SyntaxTraits;
import Serpent.Syntax.SyntaxTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;


class EvaluatorTest {

    @ParameterizedTest
    @MethodSource("providesBinaryOperator")
    void evaluateSimpleBinaryExpressions(SyntaxKind operator, int a, int b) {
        String expr = String.format("%d %s %d", a, operator.text, b);
        SyntaxTree ast = new Parser(expr).parse();
        switch (operator) {
            case StarToken -> Assertions.assertEquals(a * b, new Evaluator(ast).evaluate());
            case SlashToken -> Assertions.assertEquals(a / b, new Evaluator(ast).evaluate());
            case CaretToken -> Assertions.assertEquals((int) Math.pow(a, b), new Evaluator(ast).evaluate());
            case MinusToken -> Assertions.assertEquals(a - b, new Evaluator(ast).evaluate());
            case PlusToken -> Assertions.assertEquals(a + b, new Evaluator(ast).evaluate());
            default -> Assertions.fail();
        }
    }

    private static Stream<Arguments> providesBinaryOperator() {
        List<SyntaxKind> ops = SyntaxTraits.getBinaryOperators();
        Stream<Arguments> s = Stream.empty();
        for (SyntaxKind op : ops) {
            for (int i = 0; i <= 4; i++) {
                for (int j = 1; j <= 5; j++) {
                    s = Stream.concat(s, Stream.of(Arguments.of(op, i, j)));
                }
            }
        }

        return s;
    }
}