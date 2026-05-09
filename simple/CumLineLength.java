import java.util.Scanner;

/** Read lines from standard input and accumulate their lengths. */
// Java 25: unnamed main method (JEP 477) - no class wrapper or public static void main required
void main() {
    // try-with-resources ensures scanner.close() is called on all exit paths
    // Java 21+: var for local variable type inference
    try (var scanner = new Scanner(System.in)) {
        var cumulative = 0L;
        // read lines until EOF
        while (scanner.hasNextLine()) {
            var line = scanner.nextLine();
            cumulative += line.length();
            System.out.println(cumulative);
            // handle the case where downstream closed its end of the pipe
            if (System.out.checkError()) break;
        }
    }
}
