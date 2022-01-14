package Serpent;

import java.util.List;

public class BinaryExpression extends ExpressionSyntax {
    private final SyntaxNode left;
    private final SyntaxToken operatorToken;
    private final SyntaxNode right;

    public BinaryExpression(SyntaxNode left, SyntaxToken operatorToken, SyntaxNode right) {
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

    public SyntaxNode getLeft() {
        return left;
    }

    public SyntaxToken getOperatorToken() {
        return operatorToken;
    }

    public SyntaxNode getRight() {
        return right;
    }
}
