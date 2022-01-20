package Serpent;

import Serpent.Binder.Binder;
import Serpent.Syntax.ExpressionSyntax;
import Serpent.Syntax.SyntaxTree;

import java.util.Map;

public class Compilation {

    private final SyntaxTree syntaxTree;
    private final Map<String, Object> variables;

    public Compilation(SyntaxTree syntaxTree, Map<String, Object> variables) {
        this.syntaxTree = syntaxTree;
        this.variables = variables;
    }

    public SyntaxTree getSyntaxTree() {
        return syntaxTree;
    }

    public EvaluationResult evaluate() {
        var diagnostics = syntaxTree.getDiagnostics();
        if (!diagnostics.isEmpty()) {
            return new EvaluationResult(null, diagnostics);
        }

        var binder = new Binder(variables);
        var boundExpression = binder.bindExpression((ExpressionSyntax) syntaxTree.getRoot());
        diagnostics = binder.getDiagnostics();

        if (!diagnostics.isEmpty()) {
            return new EvaluationResult(null, diagnostics);
        }

        var evaluator = new Evaluator(boundExpression, variables);
        var result = evaluator.evaluate();
        diagnostics = evaluator.getDiagnostics();

        if (!diagnostics.isEmpty()) {
            return new EvaluationResult(null, diagnostics);
        }

        return new EvaluationResult(result, diagnostics);
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}
