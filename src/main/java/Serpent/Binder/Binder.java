package Serpent.Binder;

import Serpent.Syntax.*;

import java.util.ArrayList;

public class Binder {
    private final ArrayList<String> diagnostics = new ArrayList<>();

    public BoundExpression bindExpression(ExpressionSyntax syntax) {
        switch (syntax.getKind()) {
            case BinaryExpression: return bindBinaryExpression((BinaryExpression) syntax);
            case LiteralExpression: return bindLiteralExpression((LiteralExpression) syntax);
            case UnaryExpression: return bindUnaryExpression((UnaryExpression) syntax);
            default: diagnostics.add("[Binder Error]: Invalid expression " + syntax.getKind()); return null;
        }
    }

    private BoundExpression bindUnaryExpression(UnaryExpression syntax) {
        BoundExpression boundOperand = bindExpression(syntax.getExpression());
        BoundUnaryOperatorKind boundOperatorKind = bindUnaryOperator(syntax.getOperatorToken().getKind(), boundOperand.getType());
        if (boundOperatorKind == null) {
            diagnostics.add("[Binder Error]: Invalid unary operator " + syntax.getOperatorToken().getKind());
            return boundOperand;
        }
        return new BoundUnaryExpression(boundOperatorKind, boundOperand);
    }

    private BoundUnaryOperatorKind bindUnaryOperator(SyntaxKind operatorKind, Class<?> operandType) {
        if (operandType.equals(Integer.class)) {
            switch (operatorKind) {
                case PlusToken: return BoundUnaryOperatorKind.Identity;
                case MinusToken: return BoundUnaryOperatorKind.Negation;
            }
        } else if (operandType.equals(Boolean.class)) {
            if (operatorKind == SyntaxKind.BangToken) {
                return BoundUnaryOperatorKind.LogicalNegation;
            }
        }

        return null;
    }

    private BoundExpression bindBinaryExpression(BinaryExpression syntax) {
        BoundExpression boundLeft = bindExpression(syntax.getLeft());
        BoundExpression boundRight = bindExpression(syntax.getRight());
        BoundBinayOperatorKind boundOperator = bindBinaryOperator(syntax.getOperatorToken().getKind(), boundLeft.getType(), boundRight.getType());
        if (boundOperator == null) {
            diagnostics.add("[Binder Error]: Invalid binary operator " + syntax.getOperatorToken().getKind());
            return boundLeft;
        }
        return new BoundBinaryExpression(boundLeft, boundOperator, boundRight);
    }

    private BoundBinayOperatorKind bindBinaryOperator(SyntaxKind operatorKind, Class<?> leftType, Class<?> rightType) {
        if (leftType.equals(Integer.class) && rightType.equals(Integer.class)) {
            return switch (operatorKind) {
                case PlusToken -> BoundBinayOperatorKind.Addition;
                case MinusToken -> BoundBinayOperatorKind.Subtraction;
                case StarToken -> BoundBinayOperatorKind.Multiplication;
                case SlashToken -> BoundBinayOperatorKind.Division;
                case CaretToken -> BoundBinayOperatorKind.Power;
                default -> null;
            };
        } else if (leftType.equals(Boolean.class) && rightType.equals(Boolean.class)) {
            return switch (operatorKind) {
                case AmpersandAmpersandToken -> BoundBinayOperatorKind.LogicalAnd;
                case BarBarToken -> BoundBinayOperatorKind.LogicalOr;
                default -> null;
            };
        } else {
            diagnostics.add("[Binder Error]: Binary operator " + operatorKind + " is not defined for " + leftType + " and " + rightType);
            return null;
        }
    }

    private BoundExpression bindLiteralExpression(LiteralExpression syntax) {
        Object value = syntax.getValue();
        if (value instanceof Integer || value instanceof Boolean) {
            return new BoundLiteralExpression(value);
        } else {
            return new BoundLiteralExpression(0);
        }
    }

    public ArrayList<String> getDiagnostics() {
        return diagnostics;
    }
}
