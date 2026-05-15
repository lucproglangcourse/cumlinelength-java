import java.util.Iterator;
import java.util.Scanner;

/** Pure core logic: incrementally accumulates line lengths with no I/O dependency. Constant-space. */
class CumLineLengthIteratorLogic implements Iterator<Long> {

    private final Iterator<String> lines;
    private long cumulative = 0L;

    CumLineLengthIteratorLogic(final Iterator<String> lines) {
        this.lines = lines;
    }

    @Override
    public boolean hasNext() {
        return lines.hasNext();
    }

    @Override
    public Long next() {
        cumulative += lines.next().length();
        return cumulative;
    }
}

/** Read lines from standard input and accumulate their lengths. */
public class CumLineLengthIterator {
    public static void main(final String[] args) {
        // try-with-resources ensures scanner.close() is called on all exit paths
        try (final var input = new Scanner(System.in).useDelimiter("\\n")) {
            final var logic = new CumLineLengthIteratorLogic(input);
            // emit each cumulative length immediately as the line is read
            while (logic.hasNext()) {
                System.out.println(logic.next());
                // handle the case where downstream closed its end of the pipe
                if (System.out.checkError()) break;
            }
        }
    }
}
