import java.util.Iterator;
import java.util.Scanner;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/** Pure core logic: incrementally accumulates line lengths with no I/O dependency. Constant-space. */
class CumLineLengthObserverLogic {

    private final Iterator<String> lines;
    private final Consumer<Long> observer;
    private final BooleanSupplier shouldStop;

    CumLineLengthObserverLogic(final Iterator<String> lines, final Consumer<Long> observer, final BooleanSupplier shouldStop) {
        this.lines = lines;
        this.observer = observer;
        this.shouldStop = shouldStop;
    }

    /** Processes all lines, notifying the observer incrementally after each one.
     *  Stops early if shouldStop returns true (e.g. downstream pipe closed). */
    public void process() {
        var cumulative = 0L;
        while (lines.hasNext()) {
            cumulative += lines.next().length();
            observer.accept(cumulative);
            if (shouldStop.getAsBoolean()) break;
        }
    }
}

/** Read lines from standard input and accumulate their lengths. */
public class CumLineLengthObserver {
    public static void main(final String[] args) {
        // try-with-resources ensures scanner.close() is called on all exit paths
        try (final var scanner = new Scanner(System.in)) {
            final var logic = new CumLineLengthObserverLogic(scanner, System.out::println, System.out::checkError);
            logic.process();
        }
    }
}
