package Serpent.Binder;

import Serpent.DiagnosticList;
import Serpent.Syntax.BinaryExpression;
import Serpent.Syntax.ExpressionSyntax;
import Serpent.Syntax.LiteralExpression;
import Serpent.Syntax.ParenthesizedExpression;
import Serpent.Syntax.SyntaxKind;
import Serpent.Syntax.UnaryExpression;
import Serpent.TextSpan;

public class Binder {
    private final DiagnosticList diagnostics = new DiagnosticList();

    public BoundExpression bindExpression(ExpressionSyntax syntax) {
        switch (syntax.getKind()) {
            case BinaryExpression:
                return bindBinaryExpression((BinaryExpression) syntax);
            case LiteralExpression:
                return bindLiteralExpression((LiteralExpression) syntax);
            case UnaryExpression:
                return bindUnaryExpression((UnaryExpression) syntax);
            case ParenthesizedExpression:
                return bindParenthesizedExpression((ParenthesizedExpression) syntax);
            default:
                diagnostics.reportInvalidExpressionKind(null, syntax.getKind());
                return null;
        }
    }

    private BoundExpression bindParenthesizedExpression(ParenthesizedExpression syntax) {
        BoundExpression innerExpression = bindExpression(syntax.getExpression());
        return new BoundParenthesizedExpression(innerExpression);
    }

    private BoundExpression bindUnaryExpression(UnaryExpression syntax) {
        BoundExpression boundOperand = bindExpression(syntax.getExpression());
        SyntaxKind operatorKind = syntax.getOperatorToken().getKind();
        BoundUnaryOperator boundOperator = BoundUnaryOperator.bind(operatorKind, boundOperand.getValueType());
        if (boundOperator == null) {
            TextSpan span = syntax.getOperatorToken().getSpan();
            diagnostics.reportUndefinedUnaryOperator(span, boundOperand.getValueType(), operatorKind);
            return boundOperand;
        }
        return new BoundUnaryExpression(boundOperator, boundOperand);
    }

    private BoundExpression bindBinaryExpression(BinaryExpression syntax) {
        BoundExpression boundLeft = bindExpression(syntax.getLeft());
        BoundExpression boundRight = bindExpression(syntax.getRight());
        SyntaxKind operatorKind = syntax.getOperatorToken().getKind();
        BoundBinaryOperator boundOperator = BoundBinaryOperator.bind(operatorKind, boundLeft.getValueType(), boundRight.getValueType());
        if (boundOperator == null) {
            TextSpan opSpan = syntax.getOperatorToken().getSpan();
            diagnostics.reportUndefinedBinaryOperator(opSpan, boundLeft.getValueType(), operatorKind, boundRight.getValueType());
            return boundLeft;
        }
        return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);
    }

    private BoundExpression bindLiteralExpression(LiteralExpression syntax) {
        Object value = syntax.getValue();
        if (value instanceof Integer || value instanceof Boolean) {
            return new BoundLiteralExpression(value);
        } else {
            return new BoundLiteralExpression(0);
        }
    }

    public DiagnosticList getDiagnostics() {
        return diagnostics;
    }
}
