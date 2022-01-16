package Serpent.Syntax;

import java.util.List;

public class ParenthesizedExpression extends ExpressionSyntax {
    private final SyntaxToken openParen;
    private final ExpressionSyntax expression;
    private final SyntaxToken closeParen;

    public ParenthesizedExpression(SyntaxToken openParen, ExpressionSyntax expression, SyntaxToken closeParen) {
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

    public ExpressionSyntax getExpression() {
        return expression;
    }

    public SyntaxToken getOpenParen() {
        return openParen;
    }
}
