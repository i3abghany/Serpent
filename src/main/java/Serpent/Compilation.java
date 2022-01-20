package Serpent;

import Serpent.Binder.Binder;
import Serpent.Syntax.ExpressionSyntax;
import Serpent.Syntax.SyntaxTree;

public class Compilation {

    private SyntaxTree syntaxTree;

    public Compilation(SyntaxTree syntaxTree) {
        this.syntaxTree = syntaxTree;

    }

    public SyntaxTree getSyntaxTree() {
        return syntaxTree;
    }

    public EvaluationResult evaluate() {
        var diagnostics = syntaxTree.getDiagnostics();
        if (!diagnostics.isEmpty()) {
            return new EvaluationResult(null, diagnostics);
        }

        var binder = new Binder();
        var boundExpression = binder.bindExpression((ExpressionSyntax) syntaxTree.getRoot());
        diagnostics = binder.getDiagnostics();

        if (!diagnostics.isEmpty()) {
            return new EvaluationResult(null, diagnostics);
        }

        var evaluator = new Evaluator(boundExpression);
        var result = evaluator.evaluate();
        diagnostics = evaluator.getDiagnostics();

        if (!diagnostics.isEmpty()) {
            return new EvaluationResult(null, diagnostics);
        }

        return new EvaluationResult(result, diagnostics);
    }
}
