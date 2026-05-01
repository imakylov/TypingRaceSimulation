
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.swing.SwingUtilities;

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

    public static <T> Predicate<T> True(){return t -> true;}

    /**
     * takes a lambda that might throw rules exception and turns it into lambda that creates a toast
     * and returns false if RulesException is called.
     *
     * @param <T> input to consumer
     * @param consumer lambda that takes t argument and might throw RulesException
     * @return lambda that returns true if exception is not thrown and false otherwise
    */
    public static <T> Predicate<T> toastExceptions(SafeConsumer<T> consumer){
        return t -> {
            try{
                consumer.accept(t);
            }catch (RulesException e){
                ToastManager.get().push(e);
                return false;
            }
            return true;
        };
    }

    /**
     * takes a lambda that might throw rules exception and turns it into lambda that creates a toast
     * and returns false if RulesException is called.
     *
     * @param runnable lambda that might throw RulesException
     * @return lambda that returns true if exception is not thrown and false otherwise
    */
    public static Supplier<Boolean> toastExceptions(SafeRunnable runnable){
        return () -> toastExceptions(_ -> runnable.run()).test(0);
    }

    /**
     * takes a lambda that might throw rules exception and turns it into lambda that creates a toast on exception.
     *
     * @param runnable lambda that might throw RulesException
     * @return lambda that runs the argument and toasts if exception is thrown 
    */
    public static Runnable toastExceptionsIgnore(SafeRunnable runnable){
        return () -> toastExceptions(runnable).get();
    }

    /**
     * @param runnable lambda to run on left click
     * @return MouseAdapter that runs runnable when it gets left click
    */
    public static MouseAdapter onLeftClick(Runnable runnable) {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)){
                    runnable.run();
                }
            }
        };
    }
}
