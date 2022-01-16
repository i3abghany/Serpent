package Serpent.Syntax;

import java.util.ArrayList;
import java.util.List;

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
            case StarToken, SlashToken -> 5;
            case CaretToken -> 4;
            case MinusToken, PlusToken -> 3;
            case AmpersandAmpersandToken -> 2;
            case BarBarToken -> 1;
            default -> 0;
        };
    }

    public static int getUnaryOperatorPrecedence(SyntaxKind kind) {
        return switch (kind) {
            case MinusToken, PlusToken, BangToken -> 6;
            default -> 0;
        };
    }

    public static List<SyntaxKind> getArithmeticBinaryOperators() {
        ArrayList<SyntaxKind> operatorTokens = new ArrayList<>();
        for (SyntaxKind kind : SyntaxKind.values()) {
            if (getBinaryOperatorPrecedence(kind) > 0 && !getLogicalBinaryOperators().contains(kind)) {
                operatorTokens.add(kind);
            }
        }
        return operatorTokens;
    }

    public static List<SyntaxKind> getLogicalBinaryOperators() {
        return List.of(SyntaxKind.AmpersandAmpersandToken, SyntaxKind.BarBarToken);
    }

    public static List<SyntaxKind> getAllBinaryOperators() {
        List<SyntaxKind> list = new ArrayList<>();
        list.addAll(getArithmeticBinaryOperators());
        list.addAll(getLogicalBinaryOperators());
        return list;
    }
}
