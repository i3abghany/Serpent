package Serpent;

import java.util.List;

public class NumberExpression extends ExpressionSyntax {
    private final SyntaxToken numberToken;
    private final Object value;

    public NumberExpression(SyntaxToken numberToken, Object value) {
        this.numberToken = numberToken;
        this.value = value;
    }

    public NumberExpression(SyntaxToken token) {
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

    public Object getValue() {
        return value;
    }
}
