package Serpent.Binder;

import Serpent.Syntax.SyntaxKind;

public class BoundBinaryOperator {
    private final SyntaxKind syntaxKind;
    private final BoundBinaryOperatorKind operatorKind;
    private final Class<?> leftType;
    private final Class<?> rightType;
    private final Class<?> resultType;

    private BoundBinaryOperator(SyntaxKind syntaxKind, BoundBinaryOperatorKind operatorKind, Class<?> leftType, Class<?> rightType, Class<?> resultType) {
        this.syntaxKind = syntaxKind;
        this.operatorKind = operatorKind;
        this.leftType = leftType;
        this.rightType = rightType;
        this.resultType = resultType;
    }

    private BoundBinaryOperator(SyntaxKind kind, BoundBinaryOperatorKind operatorKind, Class<?> type) {
        this(kind, operatorKind, type, type, type);
    }

    private BoundBinaryOperator(SyntaxKind kind, BoundBinaryOperatorKind operatorKind, Class<?> operandsType, Class<?> resultType) {
        this(kind, operatorKind, operandsType, operandsType, resultType);
    }

    private static final BoundBinaryOperator[] ops = new BoundBinaryOperator[]{
            new BoundBinaryOperator(SyntaxKind.PlusToken, BoundBinaryOperatorKind.Addition, Integer.class),
            new BoundBinaryOperator(SyntaxKind.MinusToken, BoundBinaryOperatorKind.Subtraction, Integer.class),
            new BoundBinaryOperator(SyntaxKind.StarToken, BoundBinaryOperatorKind.Multiplication, Integer.class),
            new BoundBinaryOperator(SyntaxKind.SlashToken, BoundBinaryOperatorKind.Division, Integer.class),
            new BoundBinaryOperator(SyntaxKind.CaretToken, BoundBinaryOperatorKind.Power, Integer.class),
            new BoundBinaryOperator(SyntaxKind.EqualsEqualsToken, BoundBinaryOperatorKind.Equals, Integer.class, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, Integer.class, Boolean.class),

            new BoundBinaryOperator(SyntaxKind.AmpersandAmpersandToken, BoundBinaryOperatorKind.LogicalAnd, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.BarBarToken, BoundBinaryOperatorKind.LogicalOr, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.EqualsEqualsToken, BoundBinaryOperatorKind.Equals, Boolean.class),
            new BoundBinaryOperator(SyntaxKind.BangEqualsToken, BoundBinaryOperatorKind.NotEquals, Boolean.class),
    };

    public static BoundBinaryOperator bind(SyntaxKind kind, Class<?> leftType, Class<?> rightType) {
        for (BoundBinaryOperator op : ops) {
            if (op.syntaxKind == kind && op.leftType.equals(leftType) && op.rightType.equals(rightType))
                return op;
        }

        return null;
    }

    public BoundBinaryOperatorKind getOperatorKind() {
        return operatorKind;
    }

    public Class<?> getResultType() {
        return resultType;
    }
}
