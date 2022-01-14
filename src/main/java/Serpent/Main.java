package Serpent;

import Serpent.Syntax.Parser;
import Serpent.Syntax.SyntaxTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private final static String prompt = "> ";
    private final static String commandPrefix = "@";
    private final static ArrayList<String> diagnostics = new ArrayList<>();

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

            if (!diagnostics.isEmpty()) {
                for (String d : diagnostics) {
                    System.out.println(d);
                }
                diagnostics.clear();
                continue;
            }

            Evaluator evaluator = new Evaluator(ast);
            System.out.println(evaluator.evaluate());
        }
    }

    private static void handleCommand(String line) throws IOException {
        if (line.equals("@exit")) {
            System.exit(0);
        } else if (line.equals("@cls")) {
            if (System.getProperty("os.name").contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            }
        } else {
            diagnostics.add("Unknown Command: " + line);
        }
    }
}
