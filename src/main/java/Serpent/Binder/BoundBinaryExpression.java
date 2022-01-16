package Serpent.Binder;

public class BoundBinaryExpression extends BoundExpression {
    private final BoundExpression left;
    private final BoundBinaryOperator boundOperator;
    private final BoundExpression right;

    public BoundBinaryExpression(BoundExpression left, BoundBinaryOperator operatorKind, BoundExpression right) {
        this.left = left;
        this.boundOperator = operatorKind;
        this.right = right;
    }

    public BoundExpression getLeft() {
        return left;
    }

    public BoundBinaryOperator getBoundOperator() {
        return boundOperator;
    }

    public BoundExpression getRight() {
        return right;
    }

    @Override
    public Class<?> getValueType() {
        return left.getValueType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.BinaryExpression;
    }
}

