package Serpent;

import Serpent.Binder.Binder;
import Serpent.Binder.BoundExpression;
import Serpent.Syntax.ExpressionSyntax;
import Serpent.Syntax.Parser;
import Serpent.Syntax.SyntaxTree;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class BinderTest {
    @ParameterizedTest
    @MethodSource("providesBinaryOperatorsAndWrongOperands")
    void binaryOperatorsWithWrongTypes(Object a, String op, Object b) {
        String expr = String.format(a + " %s " + b, op);
        SyntaxTree ast = new Parser(expr).parse();
        Binder binder = new Binder();
        BoundExpression illFormedBoundExpression = binder.bindExpression((ExpressionSyntax) ast.getRoot());
        Assertions.assertFalse(binder.getDiagnostics().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("providesUnaryOperatorsAndWrongOperand")
    void unaryOperatorsWithWrongType(String op, Object val) {
        String expr = String.format("%s" + val, op);
        SyntaxTree ast = new Parser(expr).parse();
        Binder binder = new Binder();
        BoundExpression illFormedBoundExpression = binder.bindExpression((ExpressionSyntax) ast.getRoot());
        Assertions.assertFalse(binder.getDiagnostics().isEmpty());
    }

    private static Stream<Arguments> providesBinaryOperatorsAndWrongOperands() {
        return Stream.of(
                Arguments.of(1, "+", true),
                Arguments.of(true, "+", 1),
                Arguments.of(2, "||", 1),
                Arguments.of(false, "||", 1),
                Arguments.of(true, "&&", 1),
                Arguments.of(2, "&&", false),
                Arguments.of(2, "==", false),
                Arguments.of(false, "==", 1),
                Arguments.of(true, "!=", 1),
                Arguments.of(4, "!=", true)
        );
    }

    private static Stream<Arguments> providesUnaryOperatorsAndWrongOperand() {
        return Stream.of(
                Arguments.of("-", true),
                Arguments.of("+", false),
                Arguments.of("!", 6543),
                Arguments.of("!", -6543),
                Arguments.of("!", 0)
        );
    }

}
