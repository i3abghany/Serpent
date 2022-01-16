package Serpent;

import Serpent.Binder.*;

import java.util.Objects;

public class Evaluator {
    private final DiagnosticList diagnostics = new DiagnosticList();
    private final BoundExpression rootExpression;

    public Evaluator(BoundExpression rootExpression) {
        this.rootExpression = rootExpression;
    }

    public Object evaluate() {
        return evaluateExpression(rootExpression);
    }

    private Object evaluateExpression(BoundExpression node) {
        if (node instanceof BoundLiteralExpression ble) {
            return ble.getValue();
        } else if (node instanceof BoundParenthesizedExpression bpe) {
            return evaluateExpression(bpe.getInnerExpression());
        } else if (node instanceof BoundUnaryExpression bue) {
            return switch (bue.getBoundOperator().getOperatorKind()) {
                case Identity -> (int) evaluateExpression(bue.getOperand());
                case Negation -> -(int) evaluateExpression(bue.getOperand());
                case LogicalNegation -> !(boolean) evaluateExpression(bue.getOperand());
            };
        } else if (node instanceof BoundBinaryExpression bbe) {
            Object left = evaluateExpression(bbe.getLeft());
            Object right = evaluateExpression(bbe.getRight());
            switch (bbe.getBoundOperator().getOperatorKind()) {
                case Addition -> {
                    return (int) left + (int) right;
                }
                case Subtraction -> {
                    return (int) left - (int) right;
                }
                case Division -> {
                    if ((int) right == 0) {
                        diagnostics.reportDivisionByZero();
                        return 0;
                    }
                    return (int) left / (int) right;
                }
                case Multiplication -> {
                    return (int) left * (int) right;
                }
                case Power -> {
                    return (int) Math.pow((int) left, (int) right);
                }
                case LogicalAnd -> {
                    return (boolean) left && (boolean) right;
                }
                case LogicalOr -> {
                    return (boolean) left || (boolean) right;
                } case Equals -> {
                    return Objects.equals(left, right);
                } case NotEquals -> {
                    return !Objects.equals(left, right);
                }
                default -> throw new IllegalStateException("Unexpected value: " + bbe.getBoundOperator().getOperatorKind());
            }
        }

        return 0;
    }

    public DiagnosticList getDiagnostics() {
        return diagnostics;
    }
}
