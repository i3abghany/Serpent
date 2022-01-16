package Serpent.Syntax;

public enum SyntaxKind {
    BadToken,
    PlusToken("+"),
    MinusToken("-"),
    StarToken("*"),
    SlashToken("/"),
    OpenParenthesisToken("("),
    CloseParenthesisToken(")"),
    NumberToken,
    IdentifierToken,
    WhitespaceToken,
    CaretToken("^"),
    BangToken("!"),

    FalseKeyword("false"),
    TrueKeyword("true"),

    BinaryExpression,
    LiteralExpression,
    ParenthesizedExpression,
    UnaryExpression,

    EndOfFileToken,
    ;

    public final String text;

    SyntaxKind(String text) {
        this.text = text;
    }

    SyntaxKind() {
        this(null);
    }
}
