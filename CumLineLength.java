import java.util.Scanner;

public class CumLineLength {
    public static void main(final String[] args) {
        final Scanner scanner = new Scanner(System.in);
        int cumulative = 0;
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            cumulative += line.length();
            System.out.println(cumulative);
            if (System.out.checkError()) break;
        }
        scanner.close();
    }
}
