package Serpent;

public class SyntaxTree {
    private final SyntaxNode root;

    public SyntaxTree(SyntaxNode root) {
        this.root = root;
    }

    public SyntaxNode getRoot() {
        return root;
    }
}
