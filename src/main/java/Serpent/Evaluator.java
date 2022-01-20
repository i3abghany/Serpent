package Serpent;

import Serpent.Binder.*;

import java.util.Map;
import java.util.Objects;

public class Evaluator {
    private final DiagnosticList diagnostics = new DiagnosticList();
    private final BoundExpression rootExpression;
    private final Map<String, Object> variables;

    public Evaluator(BoundExpression rootExpression, Map<String, Object> variables) {
        this.rootExpression = rootExpression;
        this.variables = variables;
    }

    public Object evaluate() {
        return evaluateExpression(rootExpression);
    }

    private Object evaluateExpression(BoundExpression node) {
        if (node instanceof BoundLiteralExpression ble)
            return ble.getValue();
        else if (node instanceof BoundParenthesizedExpression bpe)
            return evaluateExpression(bpe.getInnerExpression());
        else if (node instanceof BoundVariableExpression bve)
            return variables.get(bve.getName());
        else if (node instanceof BoundAssignmentExpression bae) {
            var value = evaluateExpression(bae.getBoundExpression());
            variables.put(bae.getName(), value);
            return value;
        } else if (node instanceof BoundUnaryExpression bue) {
            var op = bue.getBoundOperator().getOperatorKind();
            return switch (op) {
                case Identity -> (int) evaluateExpression(bue.getOperand());
                case Negation -> -(int) evaluateExpression(bue.getOperand());
                case LogicalNegation -> !(boolean) evaluateExpression(bue.getOperand());
                default -> {
                    throw new IllegalStateException("Invalid unary operator " + op);
                }
            };
        } else if (node instanceof BoundBinaryExpression bbe) {
            var left = evaluateExpression(bbe.getLeft());
            var right = evaluateExpression(bbe.getRight());
            var op = bbe.getBoundOperator().getOperatorKind();

            switch (op) {
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
                }
                case Equals -> {
                    return Objects.equals(left, right);
                }
                case NotEquals -> {
                    return !Objects.equals(left, right);
                }
                default -> throw new IllegalStateException("Invalid binary operator" + op.getClass());
            }
        } else {
            throw new IllegalStateException("Invalid node type " + node.getClass());
        }
    }

    public DiagnosticList getDiagnostics() {
        return diagnostics;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}
