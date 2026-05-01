import java.awt.Dimension;
import java.awt.Font;
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
        String[] words = passage.split(" |\n");
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
        area.setHighlighter(null);
        area.setFocusable(false);
        area.setFont(new Font("Monospace", 1, 15));
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

    public static void setStableSize(JComponent component, int width, int height){
        component.setMinimumSize(new Dimension(width, height));
        component.setPreferredSize(new Dimension(width, height));
        component.setMaximumSize(new Dimension(width, height));
    }
}
