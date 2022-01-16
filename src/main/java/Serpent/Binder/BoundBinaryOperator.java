package Serpent.Binder;

import Serpent.Syntax.SyntaxKind;

public class BoundBinaryOperator {
    private final SyntaxKind syntaxKind;
    private final BoundBinayOperatorKind operatorKind;
    private final Class<?> leftType;
    private final Class<?> rightType;
    private final Class<?> resultType;

    private BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinayOperatorKind operatorKind, Class<?> leftType, Class<?> rightType, Class<?> resultType) {
        this.syntaxKind = syntaxKind;
        this.operatorKind = operatorKind;
        this.leftType = leftType;
        this.rightType = rightType;
        this.resultType = resultType;
    }

    private BoundBinaryOperator(SyntaxKind kind, BoundBinayOperatorKind operatorKind, Class<?> type) {
        this(kind, operatorKind, type, type, type);
    }

    private BoundBinaryOperator(SyntaxKind kind, BoundBinayOperatorKind operatorKind, Class<?> operandsType, Class<?> resultType) {
        this(kind, operatorKind, operandsType, operandsType, resultType);
    }

    private static final BoundBinaryOperator[] ops = new BoundBinaryOperator[]{
            new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinayOperatorKind.Addition, Integer.class),
            new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinayOperatorKind.Subtraction, Integer.class),
            new BoundBinaryOperator(SyntaxKind.StarToken, BoundBinayOperatorKind.Multiplication, Integer.class),
            new BoundBinaryOperator(SyntaxKind.SlashToken, BoundBinayOperatorKind.Division, Integer.class),
            new BoundBinaryOperator(SyntaxKind.CaretToken, BoundBinayOperatorKind.Power, Integer.class),
            new BoundBinaryOperator(SyntaxKind.EqualsEqualsToken, BoundBinayOperatorKind.Equals, Integer.class, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinayOperatorKind.NotEquals, Integer.class, Boolean.class),

            new BoundBinaryOperator(SyntaxKind.AmpersandAmpersandToken, BoundBinayOperatorKind.LogicalAnd, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.BarBarToken, BoundBinayOperatorKind.LogicalOr, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.EqualsEqualsToken, BoundBinayOperatorKind.Equals, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinayOperatorKind.NotEquals, Boolean.class),
    };

    public static BoundBinaryOperator bind(SyntaxKind kind, Class<?> leftType, Class<?> rightType) {
        for (BoundBinaryOperator op : ops) {
            if (op.syntaxKind == kind && op.leftType.equals(leftType) && op.rightType.equals(rightType))
                return op;
        }

        return null;
    }

    public BoundBinayOperatorKind getOperatorKind() {
        return operatorKind;
    }

    public Class<?> getResultType() {
        return resultType;
    }
}
