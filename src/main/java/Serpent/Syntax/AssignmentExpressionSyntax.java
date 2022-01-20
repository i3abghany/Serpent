package Serpent.Syntax;

import java.util.List;

public class AssignmentExpressionSyntax extends ExpressionSyntax {

    private final SyntaxToken identifierToken;
    private final SyntaxToken equalsToken;
    private final ExpressionSyntax expression;

    public AssignmentExpressionSyntax(SyntaxToken identifierToken, SyntaxToken equalsToken, ExpressionSyntax expression) {

        this.identifierToken = identifierToken;
        this.equalsToken = equalsToken;
        this.expression = expression;
    }

    public ExpressionSyntax getExpression() {
        return expression;
    }

    public SyntaxToken getEqualsToken() {
        return equalsToken;
    }

    public SyntaxToken getIdentifierToken() {
        return identifierToken;
    }

    @Override
    public SyntaxKind getKind() {
        return SyntaxKind.AssignmentExpression;
    }

    @Override
    public List<SyntaxNode> getChildren() {
        return List.of(identifierToken, equalsToken, expression);
    }
}
