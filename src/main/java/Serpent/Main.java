package Serpent;

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
            }

            if (!diagnostics.isEmpty()) {
                for (String d : diagnostics) {
                    System.out.println(d);
                }
                diagnostics.clear();
            }
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
            diagnostics.add("Unknown Command");
        }
    }
}
