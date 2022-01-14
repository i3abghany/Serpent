package Serpent;

import Serpent.Syntax.*;

public class Evaluator {
    private final SyntaxTree ast;

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
            return switch (be.getOperatorToken().getKind()) {
                case PlusToken -> left + right;
                case MinusToken -> left - right;
                case SlashToken -> left / right;
                case StarToken -> left * right;
                case CaretToken -> (int) Math.pow(left, right);
                default -> 0;
            };
        }

        return 0;
    }
}
