/**
 * A class that holds useful functional interfaces
 *
 * @author Adil Akylov
 * @version 1.2
 */

public class FI {
    @FunctionalInterface
    public static interface SafeConsumer<T> {
        void accept(T t) throws RulesException;
    }
    @FunctionalInterface
    public static interface SafeRunnable {
        void run() throws RulesException;
    }
}
