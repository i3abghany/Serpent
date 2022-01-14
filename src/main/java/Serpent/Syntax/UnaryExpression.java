package Serpent.Syntax;

import java.util.List;

public class UnaryExpression extends ExpressionSyntax {
    private final SyntaxToken operatorToken;
    private final ExpressionSyntax expression;

    public UnaryExpression(SyntaxToken operatorToken, ExpressionSyntax expression) {
        this.operatorToken = operatorToken;
        this.expression = expression;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.UnaryExpression;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(operatorToken, expression);
    }

    public SyntaxToken getOperatorToken() {
        return operatorToken;
    }

    public ExpressionSyntax getExpression() {
        return expression;
    }
}
