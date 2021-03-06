package Serpent.Syntax;

import Serpent.DiagnosticList;

import java.util.ArrayList;

public class Parser {
    private SyntaxToken[] tokenArray;
    private final String text;
    private int position;
    private final DiagnosticList diagnostics = new DiagnosticList();

    public Parser(String text) {
        this.text = text;
    }

    public SyntaxTree parse() {
        consumeAllTokens();

        if (!diagnostics.isEmpty()) {
            return new SyntaxTree(null, null, diagnostics);
        }

        ExpressionSyntax expressionSyntax = parseExpression();
        SyntaxToken eof = matchToken(SyntaxKind.EndOfFileToken);
        return new SyntaxTree(expressionSyntax, eof, diagnostics);
    }

    private ExpressionSyntax parseAssignmentExpression() {
        if (current().getKind() == SyntaxKind.IdentifierToken &&
            peek(1).getKind() == SyntaxKind.EqualsToken) {
            var identifierToken = matchToken(SyntaxKind.IdentifierToken);
            var equalsToken = matchToken(SyntaxKind.EqualsToken);
            var right = parseAssignmentExpression();
            return new AssignmentExpressionSyntax(identifierToken, equalsToken, right);
        }

        return parseBinaryExpression(0);
    }

    private ExpressionSyntax parsePrimaryExpression() {
        switch (current().getKind()) {
            case OpenParenthesisToken -> {
                var openParen = matchToken(SyntaxKind.OpenParenthesisToken);
                var innerExpr = parseBinaryExpression(0);
                var closeParen = matchToken(SyntaxKind.CloseParenthesisToken);
                return new ParenthesizedExpression(openParen, innerExpr, closeParen);
            }
            case NumberToken -> {
                var numberToken = matchToken(SyntaxKind.NumberToken);
                return new LiteralExpression(numberToken);
            }
            case TrueKeyword, FalseKeyword -> {
                var booleanToken = nextToken();
                return new LiteralExpression(booleanToken);
            }
            case IdentifierToken -> {
                var identifierToken = matchToken(SyntaxKind.IdentifierToken);
                return new NameExpressionSyntax(identifierToken);
            }
            default -> {
                return new LiteralExpression(matchToken(SyntaxKind.LiteralExpression));
            }
        }
    }

    private ExpressionSyntax parseExpression() {
        return parseAssignmentExpression();
    }

    private ExpressionSyntax parseBinaryExpression(int parentPrecedence) {
        ExpressionSyntax left;

        int unaryOperatorPrecedence = SyntaxTraits.getUnaryOperatorPrecedence(current().getKind());
        if (unaryOperatorPrecedence > 0 && unaryOperatorPrecedence >= parentPrecedence) {
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax operand = parseBinaryExpression(unaryOperatorPrecedence);
            left = new UnaryExpression(operatorToken, operand);
        } else {
            left = parsePrimaryExpression();
        }

        while (true) {
            int operatorPrecedence = SyntaxTraits.getBinaryOperatorPrecedence(current().getKind());
            if (operatorPrecedence <= parentPrecedence || operatorPrecedence == 0)
                break;
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseBinaryExpression(operatorPrecedence);
            left = new BinaryExpression(left, operatorToken, right);
        }

        return left;
    }

    private void consumeAllTokens() {
        Lexer lexer = new Lexer(text);
        ArrayList<SyntaxToken> tokens = new ArrayList<>();

        SyntaxToken token;
        do {
            token = lexer.nextToken();
            if (token.getKind() != SyntaxKind.WhitespaceToken)
                tokens.add(token);
        } while (token.getKind() != SyntaxKind.EndOfFileToken);

        diagnostics.addAll(lexer.getDiagnostics());
        this.tokenArray = tokens.toArray(SyntaxToken[]::new);
    }

    public SyntaxToken[] getTokenArray() {
        return tokenArray;
    }

    private SyntaxToken current() {
        return peek(0);
    }

    private SyntaxToken peek(int offset) {
        int idx = offset + position;

        if (idx >= tokenArray.length)
            return tokenArray[tokenArray.length - 1];

        return tokenArray[idx];
    }

    private SyntaxToken nextToken() {
        SyntaxToken curr = current();
        position++;
        return curr;
    }

    private SyntaxToken matchToken(SyntaxKind kind) {
        if (current().getKind() == kind) {
            return nextToken();
        }

        diagnostics.reportUnexpectedToken(current().getSpan(), current().getKind(), kind);
        return new SyntaxToken(current().getPosition(), kind, null, null);
    }
}
