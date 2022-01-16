package Serpent.Syntax;

import java.util.List;

public class BinaryExpression extends ExpressionSyntax {
    private final ExpressionSyntax left;
    private final SyntaxToken operatorToken;
    private final ExpressionSyntax right;

    public BinaryExpression(ExpressionSyntax left, SyntaxToken operatorToken, ExpressionSyntax right) {
        this.left = left;
        this.operatorToken = operatorToken;
        this.right = right;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.BinaryExpression;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(left, operatorToken, right);
    }

    public ExpressionSyntax getLeft() {
        return left;
    }

    public SyntaxToken getOperatorToken() {
        return operatorToken;
    }

    public ExpressionSyntax getRight() {
        return right;
    }
}
