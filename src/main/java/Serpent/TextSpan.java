package Serpent;

public record TextSpan(int start, int length) {
    public int end() {
        return start + length;
    }
}
