package Serpent.Binder;

public class BoundVariableExpression extends BoundExpression {
    private final String name;
    private final Class<?> type;

    public BoundVariableExpression(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public Class<?> getValueType() {
        return type;
    }

    @Override
    public BoundNodeKind getKind() {
        return BoundNodeKind.VariableExpression;
    }

    public Object getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }
}
