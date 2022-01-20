package Serpent.Binder;

import Serpent.DiagnosticList;
import Serpent.Syntax.*;
import Serpent.TextSpan;

import java.util.Map;

public class Binder {
    private final DiagnosticList diagnostics = new DiagnosticList();
    private final Map<String, Object> variables;

    public Binder(Map<String, Object> variables) {
        this.variables = variables;
    }

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
            case NameExpression:
                return bindNameExpression((NameExpressionSyntax) syntax);
            case AssignmentExpression:
                return bindAssignmentExpression((AssignmentExpressionSyntax) syntax);
            default:
                diagnostics.reportInvalidExpressionKind(null, syntax.getKind());
                return null;
        }
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

    private BoundExpression bindParenthesizedExpression(ParenthesizedExpression syntax) {
        BoundExpression innerExpression = bindExpression(syntax.getExpression());
        return new BoundParenthesizedExpression(innerExpression);
    }

    private BoundExpression bindAssignmentExpression(AssignmentExpressionSyntax syntax) {
        var name = syntax.getIdentifierToken().getText();
        var boundExpression = bindExpression(syntax.getExpression());

        Object defaultValue;

        if (boundExpression.getValueType().equals(Integer.class)) {
            defaultValue = 0;
        } else if (boundExpression.getValueType().equals(Boolean.class)) {
            defaultValue = false;
        } else {
            throw new IllegalStateException("Unsupported variable type " + boundExpression.getValueType());
        }

        variables.put(name, defaultValue);
        return new BoundAssignmentExpression(name, boundExpression);
    }

    private BoundExpression bindNameExpression(NameExpressionSyntax syntax) {
        var name = syntax.getIdentifierToken().getText();

        if (!variables.containsKey(name)) {
            diagnostics.reportUndefinedName(syntax.getIdentifierToken().getSpan(), name);
            return new BoundLiteralExpression(0);
        }

        var value = variables.get(name);
        var type = value.getClass();
        return new BoundVariableExpression(name, type);
    }

    public DiagnosticList getDiagnostics() {
        return diagnostics;
    }
}
