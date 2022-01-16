package Serpent.Binder;

import Serpent.Syntax.SyntaxKind;

public class BoundUnaryOperator {
    private final SyntaxKind syntaxKind;
    private final BoundUnaryOperatorKind operatorKind;
    private final Class<?> operandType;
    private final Class<?> resultType;

    private BoundUnaryOperator(SyntaxKind syntaxKind, BoundUnaryOperatorKind operatorKind, Class<?> operandType, Class<?> resultType) {
        this.syntaxKind = syntaxKind;
        this.operatorKind = operatorKind;
        this.operandType = operandType;
        this.resultType = resultType;
    }

    private BoundUnaryOperator(SyntaxKind syntaxKind, BoundUnaryOperatorKind operatorKind, Class<?> type) {
        this(syntaxKind, operatorKind, type, type);
    }

    private static final BoundUnaryOperator[] ops = new BoundUnaryOperator[]{
            new BoundUnaryOperator(SyntaxKind.PlusToken, BoundUnaryOperatorKind.Identity, Integer.class),
            new BoundUnaryOperator(SyntaxKind.MinusToken, BoundUnaryOperatorKind.Negation, Integer.class),

            new BoundUnaryOperator(SyntaxKind.BangToken, BoundUnaryOperatorKind.LogicalNegation, Boolean.class),
    };

    public static BoundUnaryOperator bind(SyntaxKind kind, Class<?> operandType) {
        for (var op : ops) {
            if (op.syntaxKind == kind && op.operandType == operandType)
                return op;
        }

        return null;
    }

    public BoundUnaryOperatorKind getOperatorKind() {
        return operatorKind;
    }
}
