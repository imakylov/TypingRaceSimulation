import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.*;
import javax.swing.*;


/**
 * A class that holds static methods and constants useful for different purposes.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class Utils {
    /**
     * takes some text and breakes it into lines between words to be at max limit width.
     * A line can only be more if there is a word that is longer then limit
     *
     * @param passage the text that needs to be broken
     * @param limit max width of a line
     * @return the passage with some spaces replace with new lines to be at max limit width
     */
    public static String breakWords(String passage, int limit){
        String result = "";
        String[] words = passage.split(" ");
        if(words.length > 0) result = words[0];
        if(words.length < 2) return result;
        int curCol = words[0].length();
        for(int i=1;i<words.length;i++){
            if(curCol + words[i].length() > limit){
                result += "\n";
                curCol = 0;
            }else{
                result += " ";
            }
            curCol += words[i].length();
            result += words[i];
        }
        return result;
    }

    /**
     * Creates a JPanel with BoxLayout on the set axis
     *
     * @param axis a static constant from BoxLayout like BoxLayout.X_AXIS or Y_AXIS
     * @return JPanel with BoxLayout layout manager
     */
    public static JPanel getBoxPanel(int axis){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, axis));
        return panel;
    }

    /**
     * Creates JTextArea that looks like a static JLabel, but breakes text properly into words
     *
     * @param text text that should be in the JTextArea
     * @return the static JTextArea
     */
    public static JTextArea getSeemlessTextArea(String text){
        JTextArea area = new JTextArea(text);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(false);
        area.setOpaque(false);
        area.setBorder(null);
        return area;
    }

    /**
     * @return current time in Milliseconds from System
     */
    public static long now(){
        return System.currentTimeMillis();
    }

    /**
     * Checks if given time in ms is before current time
     *
     * @param timeInMS time to compare to now
     * @return true if the time given has already passed
     */
    public static boolean isPast(long timeInMS){
        return timeInMS < Utils.now();
    }

    /**
     * takes a lambda that might throw rules exception and turns it into lambda that creates a toast
     * and returns false if RulesException is called.
     *
     * @param <T> input to consumer
     * @param consumer lambda that takes t argument and might throw RulesException
     * @return lambda that returns true if exception is not thrown and false otherwise
     */
    public static <T> Predicate<T> toastExceptions(FI.SafeConsumer<T> consumer){
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
    public static Supplier<Boolean> toastExceptions(FI.SafeRunnable runnable){
        return () -> toastExceptions(_ -> runnable.run()).test(0);
    }

    /**
     * takes a lambda that might throw rules exception and turns it into lambda that creates a toast on exception.
     *
     * @param runnable lambda that might throw RulesException
     * @return lambda that runs the argument and toasts if exception is thrown 
     */
    public static Runnable toastExceptionsIgnore(FI.SafeRunnable runnable){
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
