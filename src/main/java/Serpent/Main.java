package Serpent;

import Serpent.Binder.Binder;
import Serpent.Binder.BoundExpression;
import Serpent.Syntax.ExpressionSyntax;
import Serpent.Syntax.Parser;
import Serpent.Syntax.SyntaxTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private final static String prompt = "> ";
    private final static String commandPrefix = "@";
    private final static ArrayList<String> diagnostics = new ArrayList<>();
    private static boolean printTree = false;

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine();

            if (line.startsWith(commandPrefix)) {
                handleCommand(line);
                continue;
            }

            Parser parser = new Parser(line);
            SyntaxTree ast = parser.parse();
            diagnostics.addAll(ast.getDiagnostics());

            if (printTree) {
                System.out.println(ast.toString());
            }

            if (!diagnostics.isEmpty()) {
                for (String d : diagnostics) {
                    System.out.println(d);
                }
                diagnostics.clear();
                continue;
            }

            Binder binder = new Binder();
            BoundExpression rootExpression = binder.bindExpression((ExpressionSyntax) ast.getRoot());
            diagnostics.addAll(binder.getDiagnostics());

            if (!diagnostics.isEmpty()) {
                for (String d : diagnostics) {
                    System.out.println(d);
                }
                diagnostics.clear();
                continue;
            }

            Evaluator evaluator = new Evaluator(rootExpression);
            diagnostics.addAll(evaluator.getDiagnostics());

            if (!diagnostics.isEmpty()) {
                for (String d : diagnostics) {
                    System.out.println(d);
                }
                diagnostics.clear();
                continue;
            }

            System.out.println(evaluator.evaluate());
        }
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
                diagnostics.add("Unknown Command: " + line);
                break;
        }
    }
}
