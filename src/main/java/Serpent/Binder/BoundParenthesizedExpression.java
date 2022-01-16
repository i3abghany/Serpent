package Serpent.Binder;

public class BoundParenthesizedExpression extends BoundExpression {
    private final BoundExpression innerExpression;

    public BoundParenthesizedExpression(BoundExpression innerExpression) {
        this.innerExpression = innerExpression;
    }

    public BoundExpression getInnerExpression() {
        return innerExpression;
    }

    @Override
    public Class<?> getValueType() {
        return innerExpression.getValueType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.ParenthesizedExpression;
    }
}
