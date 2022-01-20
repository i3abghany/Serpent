package Serpent.Binder;

public class BoundAssignmentExpression extends BoundExpression {
    private final String name;
    private final BoundExpression boundExpression;

    public BoundAssignmentExpression(String name, BoundExpression boundExpression) {
        this.name = name;
        this.boundExpression = boundExpression;
    }

    public String getName() {
        return name;
    }

    public BoundExpression getBoundExpression() {
        return boundExpression;
    }

    @Override
    public Class<?> getValueType() {
        return boundExpression.getValueType();
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.AssignmentExpression;
    }
}
