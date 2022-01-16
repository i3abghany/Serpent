package Serpent;

import Serpent.Binder.BoundBinaryExpression;
import Serpent.Binder.BoundExpression;
import Serpent.Binder.BoundLiteralExpression;
import Serpent.Binder.BoundUnaryExpression;

import java.util.ArrayList;

public class Evaluator {
    private final ArrayList<String> diagnostics = new ArrayList<>();
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
        } else if (node instanceof BoundUnaryExpression bue) {
            return switch (bue.getOperatorKind()) {
                case Identity -> (int) evaluateExpression(bue.getOperand());
                case Negation -> -(int) evaluateExpression(bue.getOperand());
            };
        } else if (node instanceof BoundBinaryExpression bbe) {
            Object left = evaluateExpression(bbe.getLeft());
            Object right = evaluateExpression(bbe.getRight());
            switch (bbe.getOperatorKind()) {
                case Addition -> {
                    return (int) left + (int) right;
                }
                case Subtraction -> {
                    return (int) left - (int) right;
                }
                case Division -> {
                    if ((int) right == 0) {
                        diagnostics.add("[Eval. Error]: Division by zero.");
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
                default -> {
                    diagnostics.add("[Eval. Error]: Unknown binary operator.");
                    return 0;
                }
            }
        }

        return 0;
    }

    public ArrayList<String> getDiagnostics() {
        return diagnostics;
    }
}
