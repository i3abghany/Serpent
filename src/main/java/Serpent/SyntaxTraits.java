package Serpent;

public final class SyntaxTraits {
    public static SyntaxKind getTextualTokenKind(String text) {
        return switch (text) {
            case "true" -> SyntaxKind.TrueKeyword;
            case "false" -> SyntaxKind.FalseKeyword;
            default -> SyntaxKind.IdentifierToken;
        };
    }
}
