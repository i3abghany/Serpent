package Serpent;

import java.util.List;

public class ParenthesizedExpression extends ExpressionSyntax {
    private final SyntaxToken openParen;
    private final SyntaxNode expression;
    private final SyntaxToken closeParen;

    public ParenthesizedExpression(SyntaxToken openParen, SyntaxNode expression, SyntaxToken closeParen) {
        this.openParen = openParen;
        this.expression = expression;
        this.closeParen = closeParen;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.ParenthesizedExpression;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(openParen, expression, closeParen);
    }

    public SyntaxToken getCloseParen() {
        return closeParen;
    }

    public SyntaxNode getExpression() {
        return expression;
    }

    public SyntaxToken getOpenParen() {
        return openParen;
    }
}
