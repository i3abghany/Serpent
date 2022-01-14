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

    FalseKeyword("false"),
    TrueKeyword("true"),

    BinaryExpression,
    NumberExpression,
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
