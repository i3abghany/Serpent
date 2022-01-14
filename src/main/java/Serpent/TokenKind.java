package Serpent;

public enum TokenKind {
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
