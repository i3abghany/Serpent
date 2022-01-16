package Serpent.Binder;

import Serpent.Syntax.BinaryExpression;
import Serpent.Syntax.ExpressionSyntax;
import Serpent.Syntax.LiteralExpression;
import Serpent.Syntax.ParenthesizedExpression;
import Serpent.Syntax.SyntaxKind;
import Serpent.Syntax.UnaryExpression;

import java.util.ArrayList;

public class Binder {
    private final ArrayList<String> diagnostics = new ArrayList<>();

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
                diagnostics.add("[Binder Error]: Invalid expression " + syntax.getKind());
                return null;
        }
    }

    private BoundExpression bindParenthesizedExpression(ParenthesizedExpression syntax) {
        BoundExpression innerExpression = bindExpression(syntax.getExpression());
        return new BoundParenthesizedExpression(innerExpression);
    }

    private BoundExpression bindUnaryExpression(UnaryExpression syntax) {
        BoundExpression boundOperand = bindExpression(syntax.getExpression());
        BoundUnaryOperatorKind boundOperatorKind = bindUnaryOperator(syntax.getOperatorToken().getKind(), boundOperand.getValueType());
        if (boundOperatorKind == null) {
            return boundOperand;
        }
        return new BoundUnaryExpression(boundOperatorKind, boundOperand);
    }

    private BoundUnaryOperatorKind bindUnaryOperator(SyntaxKind operatorKind, Class<?> operandType) {
        if (operandType.equals(Integer.class)) {
            switch (operatorKind) {
                case PlusToken:
                    return BoundUnaryOperatorKind.Identity;
                case MinusToken:
                    return BoundUnaryOperatorKind.Negation;
            }
        } else if (operandType.equals(Boolean.class)) {
            if (operatorKind == SyntaxKind.BangToken) {
                return BoundUnaryOperatorKind.LogicalNegation;
            }
        }

        diagnostics.add("[Binder Error]: Unary operator " + operatorKind + " is not defined for " + operandType);
        return null;
    }

    private BoundExpression bindBinaryExpression(BinaryExpression syntax) {
        BoundExpression boundLeft = bindExpression(syntax.getLeft());
        BoundExpression boundRight = bindExpression(syntax.getRight());
        BoundBinayOperatorKind boundOperator = bindBinaryOperator(syntax.getOperatorToken().getKind(), boundLeft.getValueType(), boundRight.getValueType());
        if (boundOperator == null) {
            return boundLeft;
        }
        return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);
    }

    private BoundBinayOperatorKind bindBinaryOperator(SyntaxKind operatorKind, Class<?> leftType, Class<?> rightType) {
        if (leftType.equals(Integer.class) && rightType.equals(Integer.class)) {
            switch (operatorKind) {
                case PlusToken:
                    return BoundBinayOperatorKind.Addition;
                case MinusToken:
                    return BoundBinayOperatorKind.Subtraction;
                case StarToken:
                    return BoundBinayOperatorKind.Multiplication;
                case SlashToken:
                    return BoundBinayOperatorKind.Division;
                case CaretToken:
                    return BoundBinayOperatorKind.Power;
            }
        } else if (leftType.equals(Boolean.class) && rightType.equals(Boolean.class)) {
            switch (operatorKind) {
                case AmpersandAmpersandToken:
                    return BoundBinayOperatorKind.LogicalAnd;
                case BarBarToken:
                    return BoundBinayOperatorKind.LogicalOr;
            }
        }

        diagnostics.add("[Binder Error]: Binary operator " + operatorKind + " is not defined for " + leftType + " and " + rightType);
        return null;
    }

    private BoundExpression bindLiteralExpression(LiteralExpression syntax) {
        Object value = syntax.getValue();
        if (value instanceof Integer || value instanceof Boolean) {
            return new BoundLiteralExpression(value);
        } else {
            diagnostics.add("[Binder Error]: Invalid literal value " + value.getClass());
            return new BoundLiteralExpression(0);
        }
    }

    public ArrayList<String> getDiagnostics() {
        return diagnostics;
    }
}
