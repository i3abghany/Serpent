package Serpent.Syntax;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public String toString() {
        return getUnixTreeRepresentation(root, "", true);
    }

    private String getUnixTreeRepresentation(SyntaxNode node, String indent, boolean isLast) {
        if (node == null) {
            return "";
        }

        StringBuilder ret = new StringBuilder();
        String marker = isLast ? "└──" : "├──";

        ret.append(indent).append(marker).append(node.getKind());

        if (node instanceof SyntaxToken t && t.getValue() != null)
            ret.append(" ").append(t.getValue());

        ret.append("\n");

        List<SyntaxNode> children = node.getChildren();
        SyntaxNode lastChild = null;
        if (children.size() > 0) {
            lastChild = children.get(children.size() - 1);
        }

        indent += isLast ? "    " : "│   ";

        for (SyntaxNode child : children) {
            ret.append(getUnixTreeRepresentation(child, indent, child.equals(lastChild)));
        }

        return ret.toString();
    }
}
