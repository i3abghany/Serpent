package Serpent.Binder;

public class BoundUnaryExpression extends BoundExpression {

    private final BoundUnaryOperator boundOperator;
    private final BoundExpression operand;

    public BoundUnaryExpression(BoundUnaryOperator boundOperator, BoundExpression operand) {
        this.boundOperator = boundOperator;
        this.operand = operand;
    }

    public BoundExpression getOperand() {
        return operand;
    }

    @Override
    public Class<?> getValueType() {
        return boundOperator.getResultType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.UnaryExpression;
    }

    public BoundUnaryOperator getBoundOperator() {
        return boundOperator;
    }
}
