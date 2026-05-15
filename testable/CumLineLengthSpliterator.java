import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Spliterator that carries the running total; stateless lambdas can be used downstream. */
class CLLSpliterator implements Spliterator<Long> {

    private final Spliterator<String> source;
    private long cumulative = 0L;

    CLLSpliterator(final Spliterator<String> source) {
        this.source = source;
    }

    @Override
    public boolean tryAdvance(final Consumer<? super Long> action) {
        return source.tryAdvance(line -> action.accept(cumulative += line.length()));
    }

    @Override public Spliterator<Long> trySplit() { return null; } // sequential only
    @Override public long estimateSize() { return source.estimateSize(); }
    @Override public int characteristics() { return source.characteristics() & ~SORTED; }
}

/** Pure core logic: transforms a stream of lines into a lazy stream of cumulative lengths. Constant-space. */
class CumLineLengthLogic {

    /** Returns a lazy sequential stream of cumulative line lengths; no I/O dependency. */
    static Stream<Long> process(final Stream<String> lines) {
        return StreamSupport.stream(new CLLSpliterator(lines.spliterator()), false);
    }
}

/** Read lines from standard input and accumulate their lengths. */
public class CumLineLengthSpliterator {
    public static void main(final String[] args) throws Exception {
        // try-with-resources ensures reader.close() is called on all exit paths
        try (final var reader = new BufferedReader(new InputStreamReader(System.in))) {
            final var result = CumLineLengthLogic.process(reader.lines());
            result.takeWhile(_ -> !System.out.checkError())
                .forEach(System.out::println);
        }
    }
}
