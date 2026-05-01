import java.awt.*;
import java.util.ArrayList;

/**
 * A class that holds and displays a place for selecting typists for any purposes.
 * if list of typists is needed outside set onAdd to add to your list and same with onRemove.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class JTypistSelection extends JSelection<SwingTypist, JTypistInfo, JTypistOption> {
    private static final ArrayList<JTypistSelection> globalSelections = new ArrayList<>();
    /**
     * @param typist typist associated with option to build
     * @return Option with given typist
     */
    @Override
    protected JTypistOption buildOption(SwingTypist typist) {
        return JTypistOption.makeOption(typist);
    }

    /**
     * Constructor for objects of class JTypistSelection.
     * Creates a JPanel with JTypistOption for each typist and adds event listener to it to call onAdd or onRemove.
     * 
     * @param typists typist which will be available to choose from, if null gets all typists
     * @param multiselect true if multiple options can be selected at once
     */
    public JTypistSelection(ArrayList<SwingTypist> typists, boolean multiselect) {
        if(typists == null) typists = SwingTypist.getAll();
        super(typists, multiselect);
    }
}

/**
 * A class for holding and displaying option.
 */
class JTypistOption extends JMyOption<SwingTypist, JTypistInfo>{
    @Override
    int getMaxHeight(){return 50;}

    /**
     * @param typist to be represented with JTypistInfo
     * @return JTypistInfo object to represent the value
     */
    @Override
    JTypistInfo buildValue(SwingTypist typist, int width) {
        JTypistInfo info = new JTypistInfo(typist);
        info.setMinimumSize(new Dimension(width, 0));
        info.update();
        return info;
    }

    /**
     * Constructor for objects of class JTypistOption.
     * Creates a JPanel with pointing cursor. and an indicator that shows when option is selected
     * 
     * @param typist typist which will be associated with this option
     */
    private JTypistOption(SwingTypist typist){
        super(typist, 100);
    }

    public static JTypistOption makeOption(SwingTypist typist){
        return JMyOption.makeOption(() -> new JTypistOption(typist));
    }
}