package Serpent.Syntax;

public enum SyntaxKind {
    BadToken,
    PlusToken,
    MinusToken,
    StarToken,
    SlashToken,
    OpenParenthesisToken,
    CloseParenthesisToken,
    NumberToken,
    IdentifierToken,
    WhitespaceToken,
    CaretToken,

    FalseKeyword,
    TrueKeyword,

    NumberExpression,
    BinaryExpression,
    ParenthesizedExpression,

    EndOfFileToken, UnaryExpression,
}