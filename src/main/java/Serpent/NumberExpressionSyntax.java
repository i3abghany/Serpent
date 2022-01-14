package Serpent;

import java.util.List;

public class NumberExpressionSyntax extends ExpressionSyntax {
    private final SyntaxToken numberToken;
    private final Object value;

    public NumberExpressionSyntax(SyntaxToken numberToken, Object value) {
        this.numberToken = numberToken;
        this.value = value;
    }

    public NumberExpressionSyntax(SyntaxToken token) {
        this(token, token.getValue());
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.NumberExpression;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(numberToken);
    }
}
