package Serpent;

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
        if (node instanceof NumberExpression numberExpression) {
            return (int) numberExpression.getValue();
        } else if (node instanceof ParenthesizedExpression parenthesizedExpression) {
            return evaluateExpression(parenthesizedExpression.getExpression());
        } else if (node instanceof BinaryExpression binaryExpression) {
            int left = evaluateExpression(binaryExpression.getLeft());
            int right = evaluateExpression(binaryExpression.getRight());
            return switch (binaryExpression.getOperatorToken().getKind()) {
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
