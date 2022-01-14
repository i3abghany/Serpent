package Serpent;

public final class SyntaxTraits {
    public static TokenKind getTextualTokenKind(String text) {
        return switch (text) {
            case "true" -> TokenKind.TrueKeyword;
            case "false" -> TokenKind.FalseKeyword;
            default -> TokenKind.IdentifierToken;
        };
    }
}
