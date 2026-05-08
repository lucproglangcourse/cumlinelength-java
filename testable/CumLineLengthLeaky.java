
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/** Pure functional core: accumulates line lengths with no I/O dependency. */
class CumLineLengthLogic {

    private final Iterator<String> lines;

    public CumLineLengthLogic(final Iterator<String> lines) {
        this.lines = lines;
    }

    /** Processes the input and returns the cumulative lengths as an inspectable list. */
    public List<Long> process() {
        final List<Long> result = new ArrayList<>();
        long cumulative = 0;
        while (lines.hasNext()) {
            cumulative += lines.next().length();
            result.add(cumulative);
        }
        return result;
    }
}

/** Read lines from standard input and accumulate their lengths. */
public class CumLineLengthLeaky {
    public static void main(final String[] args) {
        // try-with-resources ensures scanner.close() is called on all exit paths
        try (final Scanner scanner = new Scanner(System.in)) {
            // delegate to pure functional core and print results
            for (final long value : new CumLineLengthLogic(scanner).process()) {
                System.out.println(value);
                // handle the case where downstream closed its end of the pipe
                if (System.out.checkError()) break;
            }
        }
    }
}
