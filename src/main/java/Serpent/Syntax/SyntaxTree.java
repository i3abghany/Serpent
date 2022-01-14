package Serpent.Syntax;

import java.util.ArrayList;

public class SyntaxTree {
    private final SyntaxNode root;
    private final SyntaxToken eofToken;
    private final ArrayList<String> diagnostics;

    public SyntaxTree(SyntaxNode root, SyntaxToken eofToken, ArrayList<String> diagnostics) {
        this.root = root;
        this.eofToken = eofToken;
        this.diagnostics = diagnostics;
    }

    public SyntaxNode getRoot() {
        return root;
    }

    public SyntaxToken getEofToken() {
        return eofToken;
    }

    public ArrayList<String> getDiagnostics() {
        return diagnostics;
    }
}
