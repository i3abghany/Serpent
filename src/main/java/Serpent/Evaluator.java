package Serpent;

import Serpent.Syntax.*;

import java.util.ArrayList;

public class Evaluator {
    private final SyntaxTree ast;
    private final ArrayList<String> diagnostics = new ArrayList<>();

    public Evaluator(SyntaxTree ast) {
        this.ast = ast;
    }

    public SyntaxTree getAst() {
        return ast;
    }

    public int evaluate() {
        SyntaxNode root = ast.getRoot();
        return evaluateExpression(root);
    }

    private int evaluateExpression(SyntaxNode node) {
        if (node instanceof NumberExpression ne) {
            return (int) ne.getValue();
        } else if (node instanceof ParenthesizedExpression pe) {
            return evaluateExpression(pe.getExpression());
        } else if (node instanceof UnaryExpression ue) {
            return switch (ue.getOperatorToken().getKind()) {
                case PlusToken -> evaluateExpression(ue.getExpression());
                case MinusToken -> -evaluateExpression(ue.getExpression());
                default -> 0;
            };
        } else if (node instanceof BinaryExpression be) {
            int left = evaluateExpression(be.getLeft());
            int right = evaluateExpression(be.getRight());
            switch (be.getOperatorToken().getKind()) {
                case PlusToken -> {
                    return left + right;
                }
                case MinusToken -> {
                    return left - right;
                }
                case SlashToken -> {
                    if (right == 0) {
                        diagnostics.add("[Eval. Error]: Division by zero.");
                        return 0;
                    }
                    return left / right;
                }
                case StarToken -> {
                    return left * right;
                }
                case CaretToken -> {
                    return (int) Math.pow(left, right);
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
