package Serpent.Binder;

public class BoundBinaryExpression extends BoundExpression {
    private final BoundExpression left;
    private final BoundBinayOperatorKind operatorKind;
    private final BoundExpression right;

    public BoundBinaryExpression(BoundExpression left, BoundBinayOperatorKind operatorKind, BoundExpression right) {
        this.left = left;
        this.operatorKind = operatorKind;
        this.right = right;
    }

    public BoundExpression getLeft() {
        return left;
    }

    public BoundBinayOperatorKind getOperatorKind() {
        return operatorKind;
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

