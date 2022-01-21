package Serpent;

import Serpent.Syntax.Parser;
import Serpent.Syntax.SyntaxTree;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    private final static String prompt = "> ";
    private final static String commandPrefix = "@";
    private static DiagnosticList diagnostics = new DiagnosticList();
    private static final HashMap<String, Object> variables = new HashMap<>();
    private static boolean printTree = false;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith(commandPrefix)) {
                handleCommand(line);
                printAndClearDiagnostics(line);
                continue;
            }

            Parser parser = new Parser(line);
            SyntaxTree ast = parser.parse();

            var evaluationResult = new Compilation(ast, variables).evaluate();
            diagnostics = evaluationResult.getDiagnostics();

            if (diagnostics.isEmpty()) {
                System.out.println(evaluationResult.getValue());
                continue;
            }

            printAndClearDiagnostics(line);
        }
    }

    private static void printAndClearDiagnostics(String line) {
        for (Diagnostic d : diagnostics) {
            printDiagnostic(d, line);
        }
        diagnostics.clear();
    }

    private static void handleCommand(String line) throws IOException {
        switch (line) {
            case "@exit":
                System.exit(0);
            case "@cls":
                if (System.getProperty("os.name").contains("Windows")) {
                    Runtime.getRuntime().exec("cls");
                }
                break;
            case "@printTree":
                printTree = !printTree;
                break;
            default:
                diagnostics.reportBadInternalCommand(line);
                break;
        }
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";

    private static void printDiagnostic(Diagnostic diagnostic, String line) {

        var span = diagnostic.span();
        System.out.println(diagnostic);

        if (span == null || span.end() > line.length()) {
            return;
        }

        var prefix = line.substring(0, span.start());
        var err = line.substring(span.start(), span.end());
        var suffix = line.substring(span.end());

        System.out.print(prefix);
        System.out.print(ANSI_RED + err + ANSI_RESET);
        System.out.println(suffix);
    }
}
