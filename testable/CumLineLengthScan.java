import java.util.Scanner;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

/** Pure core logic: transforms a stream of lines into a lazy stream of cumulative lengths. Constant-space. */
class CumLineLengthScanLogic {

    /** Returns a lazy sequential stream of cumulative line lengths; no I/O dependency. */
    static Stream<Long> process(final Stream<String> lines) {
        return lines
                .map(line -> (long) line.length())
                .gather(Gatherers.scan(() -> 0L, Long::sum));
    }
}

/** Read lines from standard input and accumulate their lengths. */
public class CumLineLengthScan {
    public static void main(final String[] args) {
        // try-with-resources ensures scanner.close() is called on all exit paths
        try (final var scanner = new Scanner(System.in).useDelimiter("\\R")) {
            final var result = CumLineLengthLogic.process(scanner.tokens());
            result.takeWhile(_ -> !System.out.checkError())
                .forEach(System.out::println);
        }
    }
}
