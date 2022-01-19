package Serpent;

import Serpent.Syntax.SyntaxKind;

import java.util.ArrayList;

public class DiagnosticList extends ArrayList<Diagnostic> {
    public DiagnosticList() {
    }

    public void report(TextSpan span, String message) {
        this.add(new Diagnostic(message, span));
    }

    public void reportBadCharacterInput(int position, char character) {
        String msg = "Unexpected character '" + character + "'";
        TextSpan span = new TextSpan(position, 1);
        report(span, msg);
    }

    public void reportUndefinedBinaryOperator(TextSpan span, Class<?> leftType, SyntaxKind operatorKind, Class<?> rightType) {
        String msg = "Binary operator " + operatorKind + " is not defined for " + leftType + " and " + rightType;
        report(span, msg);
    }

    public void reportUndefinedUnaryOperator(TextSpan span, Class<?> operandType, SyntaxKind operatorKind) {
        String msg = "Unary operator " + operatorKind + " is not defined for " + operandType;
        report(span, msg);
    }

    public void reportInvalidExpressionKind(TextSpan span, SyntaxKind expressionKind) {
        String msg = "Invalid expression " + expressionKind;
        report(span, msg);
    }

    public void reportUnexpectedToken(TextSpan span, SyntaxKind found, SyntaxKind expected) {
        String msg = "Expected: " + expected + ", but found: " + found;
        report(span, msg);
    }

    public void reportDivisionByZero() {
        String msg = "Division by zero error.";
        report(null, msg);
    }

    public void reportBadInternalCommand(String command) {
        String msg = "Invalid command '" + command + "'";
        report(null, msg);
    }
}
