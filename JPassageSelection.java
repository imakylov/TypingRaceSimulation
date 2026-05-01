
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * A class that holds and displays a place for selecting passages for any purposes.
 * if list of typists is needed outside set onAdd to add to your list and same with onRemove.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class JPassageSelection extends JSingleSelection<String, JScrollPane, JPassageOption> {
    /**
     * @return default list of SwingTypists
    */
    public static ArrayList<String> defaultRotation(){
        ArrayList<String> passages = new ArrayList<>();
        passages.add("The last man of Earth sat alone in a room. There was a knock on the door.");
        passages.add("In the rigor which is space, this spacesuit was designed by engineers to maintain your life in space, and can be called the smallest spaceship.");
        passages.add("When you become untouchable you're unable to touch.");
        passages.add("As a child, I considered such unknowns sinister. Now, though, I understand they bear no ill will. The universe is, and we are.");
        passages.add("The right man in the wrong place can make all the difference in the world. So, wake up, Mister Freeman. Wake up and... smell the ashes...");
        passages.add("It is sad to think people are no longer learning how to use the colon and semicolon, not least because, in this supreme QWERTY keyboard era, the little finger of the human right hand, deprived of its traditional function, may eventually dwindle and drop off from disuse.");
        return passages;
    }

    public JPassageSelection(ArrayList<String> passages) {
        if(passages == null) passages = defaultRotation();
        super(passages);
    }

    @Override
    protected JPassageOption buildOption(String passage) {
        return JPassageOption.makeOption(passage);
    }
}

class JPassageOption extends JMyOption<String, JScrollPane>{
    private JTextArea textArea;
    @Override
    int getMaxHeight() {return 70;}
    
    @Override
    JScrollPane buildValue(String passage, int width) {
        this.textArea = Utils.getSeemlessTextArea(Utils.breakWords(passage, 50));
        JScrollPane pane = new JScrollPane(this.textArea);
        Utils.setStableSize(pane, width, this.getMaxHeight());
        return pane;
    }

    private JPassageOption(String passage){
        super(passage, 500);
    }

    public static JPassageOption makeOption(String passage){
        return JMyOption.makeOption(() -> new JPassageOption(passage));
    }

    @Override
    public void addMouseListener(MouseListener l){
        super.addMouseListener(l);
        this.textArea.addMouseListener(l);
    }
}