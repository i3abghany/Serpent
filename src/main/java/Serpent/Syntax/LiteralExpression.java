package Serpent.Syntax;

import java.util.List;

public class LiteralExpression extends ExpressionSyntax {
    private final SyntaxToken numberToken;
    private final Object value;

    public LiteralExpression(SyntaxToken numberToken, Object value) {
        this.numberToken = numberToken;
        this.value = value;
    }

    public LiteralExpression(SyntaxToken token) {
        this(token, token.getValue());
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.LiteralExpression;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(numberToken);
    }

    public Object getValue() {
        return value;
    }
}
