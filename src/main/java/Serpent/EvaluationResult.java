package Serpent;

public class EvaluationResult {
    private final Object value;
    private final DiagnosticList diagnostics;

    public EvaluationResult(Object value, DiagnosticList diagnostics) {
        this.value = value;
        this.diagnostics = diagnostics;
    }

    public DiagnosticList getDiagnostics() {
        return diagnostics;
    }

    public Object getValue() {
        return value;
    }
}
