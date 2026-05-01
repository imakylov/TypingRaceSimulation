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

    public static JTypistSelection fromAll(boolean multiselect){
        JTypistSelection selection = new JTypistSelection(null, multiselect);
        globalSelections.add(selection);
        return selection;
    }

    @Override
    public void removeOption(SwingTypist typist){
        if(!SwingTypist.getAll().remove(typist)) return;
        if(globalSelections.contains(this)){
            for(JTypistSelection selection : globalSelections) selection.removeOptionHere(typist);
        }else this.removeOptionHere(typist);
    }

    private void removeOptionHere(SwingTypist typist){
        super.removeOption(typist);
    }

    @Override
    public void addOption(SwingTypist typist){
        if(globalSelections.contains(this)){
            for(JTypistSelection selection : globalSelections) selection.addOptionHere(typist);
        }else this.addOptionHere(typist);
    }

    private void addOptionHere(SwingTypist typist){
        super.addOption(typist);
    }

    public void updateMe(){
        for(JTypistOption option : this.options){
            option.update();
        }
    }
    public static void updateAll(){
        for(JTypistSelection selection : globalSelections) selection.updateMe();
    }
}

/**
 * A class for holding and displaying option.
 */
class JTypistOption extends JMyOption<SwingTypist, JTypistInfo>{
    @Override
    int getMaxHeight(){return 80;}

    /**
     * @param typist to be represented with JTypistInfo
     * @return JTypistInfo object to represent the value
     */
    @Override
    JTypistInfo buildValue(SwingTypist typist, int width) {
        JTypistInfo info = new JTypistInfo(typist);
        info.setMinimumSize(new Dimension(width, getMaxHeight()));
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
        super(typist, 150);
    }

    public static JTypistOption makeOption(SwingTypist typist){
        return JMyOption.makeOption(() -> new JTypistOption(typist));
    }

    public void update(){
        this.rep.update();
    }
}