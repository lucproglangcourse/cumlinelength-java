import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/** Pure core logic: accumulates line lengths with no I/O dependency. */
record CumLineLengthLogic(Iterator<String> lines) {

    /** Processes the input and returns the cumulative lengths as an inspectable list. */
    public List<Long> process() {
        final var result = new ArrayList<Long>();
        var cumulative = 0L;
        while (lines.hasNext()) {
            cumulative += lines.next().length();
            result.add(cumulative);
        }
        return result;
    }
}

/** Read lines from standard input and accumulate their lengths. */
public class CumLineLengthLeaky {
    public static void main(String[] args) {
        // try-with-resources ensures scanner.close() is called on all exit paths
        try (var scanner = new Scanner(System.in)) {
            // delegate to pure functional core and print results
            for (final var value : new CumLineLengthLogic(scanner.useDelimiter("\\n")).process()) {
                System.out.println(value);
                // handle the case where downstream closed its end of the pipe
                if (System.out.checkError()) break;
            }
        }
    }
}
