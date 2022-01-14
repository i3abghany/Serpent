package Serpent;

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

    EndOfFileToken,
}
