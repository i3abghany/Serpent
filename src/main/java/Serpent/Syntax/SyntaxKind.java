package Serpent.Syntax;

public enum SyntaxKind {
    BadToken,
    PlusToken("+"),
    MinusToken("-"),
    StarToken("*"),
    SlashToken("/"),
    OpenParenthesisToken("("),
    CloseParenthesisToken(")"),
    CaretToken("^"),
    BangToken("!"),
    EqualsToken("="),
    NumberToken,
    IdentifierToken,
    WhitespaceToken,

    FalseKeyword("false"),
    TrueKeyword("true"),

    BinaryExpression,
    LiteralExpression,
    ParenthesizedExpression,
    UnaryExpression,
    AssignmentExpression,
    NameExpression,

    AmpersandAmpersandToken("&&"),
    BarBarToken("||"),
    EqualsEqualsToken("=="),
    BangEqualsToken("!="),

    EndOfFileToken;

    public final String text;

    SyntaxKind(String text) {
        this.text = text;
    }

    SyntaxKind() {
        this(null);
    }
}
