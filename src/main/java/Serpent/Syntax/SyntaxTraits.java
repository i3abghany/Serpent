package Serpent.Syntax;

public final class SyntaxTraits {
    public static SyntaxKind getTextualTokenKind(String text) {
        return switch (text) {
            case "true" -> SyntaxKind.TrueKeyword;
            case "false" -> SyntaxKind.FalseKeyword;
            default -> SyntaxKind.IdentifierToken;
        };
    }

    public static int getBinaryOperatorPrecedence(SyntaxKind kind) {
        return switch (kind) {
            case StarToken, SlashToken -> 3;
            case CaretToken -> 2;
            case MinusToken, PlusToken -> 1;
            default -> 0;
        };
    }

    public static int getUnaryOperatorPrecedence(SyntaxKind kind) {
        return switch (kind) {
            case MinusToken, PlusToken -> 4;
            default -> 0;
        };
    }
}