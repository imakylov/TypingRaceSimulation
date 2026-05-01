
import java.util.ArrayList;
import javax.swing.JTextArea;

/**
 * A class that holds and displays a place for selecting passages for any purposes.
 * if list of typists is needed outside set onAdd to add to your list and same with onRemove.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class JPassageSelection extends JSingleSelection<String, JTextArea> {
    /**
     * @return default list of SwingTypists
    */
    public static ArrayList<String> defaultRotation(){
        ArrayList<String> passages = new ArrayList<>();
        passages.add("The last man of Earth sat alone in a room. There was a knock on the door.");
        passages.add("In the rigor which is space, this spacesuit was designed by engineers to maintain your life in space, and can be called the smallest spaceship.");
        // passages.add("");
        // passages.add("");
        // passages.add("");
        // passages.add("");
        // passages.add("");
        return passages;
    }

    public JPassageSelection(ArrayList<String> passages) {
        if(passages == null) passages = defaultRotation();
        super(passages);
    }

    @Override
    JMyOption<String, JTextArea> buildOption(String passage) {
        return new JPassageOption(passage);
    }
}

class JPassageOption extends JMyOption<String, JTextArea>{
    @Override
    int getMaxHeight() {return 60;}
    
    @Override
    JTextArea buildValue(String passage) {
        return Utils.getSeemlessTextArea(Utils.breakWords(passage, 60));
    }

    public JPassageOption(String passage){
        super(passage);
    }
}