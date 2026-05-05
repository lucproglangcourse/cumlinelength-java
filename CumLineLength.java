import java.util.Scanner;

/** Read lines from standard input and accumulate their lengths. */
public class CumLineLength {
    public static void main(final String[] args) {
        // try-with-resources ensures scanner.close() is called on all exit paths
        try (final Scanner scanner = new Scanner(System.in)) {
            long cumulative = 0;
            // read lines until EOF
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                cumulative += line.length();
                System.out.println(cumulative);
                // handle the case where downstream closed its end of the pipe
                if (System.out.checkError()) break;
            }
        }
    }
}
