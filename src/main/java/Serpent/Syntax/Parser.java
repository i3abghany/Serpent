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
        ExpressionSyntax expressionSyntax = parseExpression(0);
        SyntaxToken eof = matchToken(SyntaxKind.EndOfFileToken);
        return new SyntaxTree(expressionSyntax, eof, diagnostics);
    }

    private ExpressionSyntax parsePrimaryExpression() {
        switch (current().getKind()) {
            case OpenParenthesisToken -> {
                SyntaxToken openParen = nextToken();
                ExpressionSyntax expr = parseExpression(0);
                SyntaxToken closedParen = nextToken();
                return new ParenthesizedExpression(openParen, expr, closedParen);
            }
            default -> {
                if (current().getKind() == SyntaxKind.NumberToken) {
                    SyntaxToken numberToken = matchToken(SyntaxKind.NumberToken);
                    return new LiteralExpression(numberToken);
                } else if (current().getKind() == SyntaxKind.TrueKeyword ||
                        current().getKind() == SyntaxKind.FalseKeyword) {
                    SyntaxToken booleanToken = nextToken();
                    return new LiteralExpression(booleanToken);
                } else {
                    return new LiteralExpression(matchToken(SyntaxKind.LiteralExpression));
                }
            }
        }
    }

    private ExpressionSyntax parseExpression(int parentPrecedence) {
        ExpressionSyntax left;

        int unaryOperatorPrecedence = SyntaxTraits.getUnaryOperatorPrecedence(current().getKind());
        if (unaryOperatorPrecedence > 0 && unaryOperatorPrecedence >= parentPrecedence) {
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax operand = parseExpression(unaryOperatorPrecedence);
            left = new UnaryExpression(operatorToken, operand);
        } else {
            left = parsePrimaryExpression();
        }

        while (true) {
            int operatorPrecedence = SyntaxTraits.getBinaryOperatorPrecedence(current().getKind());
            if (operatorPrecedence <= parentPrecedence || operatorPrecedence == 0)
                break;
            SyntaxToken operatorToken = nextToken();
            ExpressionSyntax right = parseExpression(operatorPrecedence);
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
