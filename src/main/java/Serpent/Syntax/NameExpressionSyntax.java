package Serpent.Syntax;

import java.util.List;

public class NameExpressionSyntax extends ExpressionSyntax {

    private final SyntaxToken identifierToken;

    public NameExpressionSyntax(SyntaxToken identifierToken) {
        this.identifierToken = identifierToken;
    }

    public SyntaxToken getIdentifierToken() {
        return identifierToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.NameExpression;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(identifierToken);
    }
}
