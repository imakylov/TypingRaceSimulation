import java.awt.*;
import java.util.ArrayList;
import java.util.function.Predicate;
import javax.swing.*;

/**
 * A class that holds and displays a place for selecting typists for any purposes.
 * if list of typists is needed outside set onAdd to add to your list and same with onRemove.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class JTypistsSelection extends JPanel {
    /**
     * @return default list of SwingTypists, //TODO might need to reconsider
     */
    public static ArrayList<SwingTypist> defaultRotation(){
        ArrayList<SwingTypist> typists = new ArrayList<>();
        typists.add(new SwingTypist("TURBOFINGERS", 0.85, Color.RED));
        typists.add(new SwingTypist("QWERTY_QUEEN",  0.60, Color.ORANGE));
        typists.add(new SwingTypist("HUNT_N_PECK",   0.30, Color.YELLOW));
        typists.add(new SwingTypist("Ivan",   0.50, Color.GREEN));
        typists.add(new SwingTypist("Alex",   0.51, Color.BLUE));
        typists.add(new SwingTypist("Monawar",   0.61, new Color(250, 0, 250)));
        typists.add(new SwingTypist("Oliver",   0.70, Color.PINK));
        return typists;
    }

    private final ArrayList<JTypistOption> options = new ArrayList<>();
    private Predicate<SwingTypist> onAdd;
    private Predicate<SwingTypist> onRemove;

    /**
     * Constructor for objects of class JTypistsSelection.
     * Creates a JPanel with JTypistOption for each typist and adds event listener to it to call onAdd or onRemove.
     * 
     * @param typists typist which will be available to choose from, if null defaultRotation will be
     */
    public JTypistsSelection(ArrayList<SwingTypist> typists){
        super();
        if(typists == null) typists = defaultRotation();
        this.setLayout(new GridLayout(0, 1, 0, 10));
        for(SwingTypist typist : typists){
            JTypistOption option = new JTypistOption(typist);
            option.addMouseListener(Utils.onLeftClick(() -> toggle(option)));
            this.options.add(option);
            this.add(option);
        }
    }

    /**
     * Gets JTypistOption object from the options based on typist. if not found null
     * 
     * @param typist typist to find amoung options
     * @return JTypistOption corresponding to typist
     */
    private JTypistOption getOption(SwingTypist typist){
        for(JTypistOption option : this.options){
            if(option.getTypist().equals(typist)){
                return option;
            }
        }return null;
    }

    /**
     * @param typist typist whose option to toggle
     */
    public void toggle(SwingTypist typist){
        this.toggle(getOption(typist));
    }

    /**
     * @param option option that needs to be toggled
     */
    private void toggle(JTypistOption option){
        if(option.isSelected()){
            this.unselect(option);
        }else this.select(option);
    }

    /**
     * calls the custom callback onAdd. If it returns false, doesn't proceed. otherwise selects option
     *
     * @param option to toggle
     */
    private void select(JTypistOption option){
        if(!this.onAdd.test(option.getTypist())) return;
        option.select();
    }

    /**
     * calls the custom callback onRemove. If it returns false, doesn't proceed. otherwise unselects option
     *
     * @param option
     */
    private void unselect(JTypistOption option){
        if(!this.onRemove.test(option.getTypist())) return;
        option.unselect();
    }

    /**
     * unselects all options with calling onRemove on all of them
     */
    public void unselectAll(){
        for(JTypistOption option : this.options){
            if(option.isSelected()) this.unselect(option);
        }
    }

    /**
     * @param f lambda that is called when option is selected.
     * false return is considered as failure, doesn't select
    */
    public void onAdd(Predicate<SwingTypist> f){
        this.onAdd = f;
    }

    /**
     * @param f lambda that is called when option is unselected.
     * false return is considered as failure, doesn't unselect
    */
    public void onRemove(Predicate<SwingTypist> f){
        this.onRemove = f;
    }
}

/**
 * A class for holding and displaying option.
 */
class JTypistOption extends JPanel {
    private final SwingTypist typist;
    private boolean selected;
    private JLabel indicator;

    /**
     * Constructor for objects of class JTypistOption.
     * Creates a JPanel with pointing cursor. and an indicator that shows when option is selected
     * 
     * @param typists typist which will be associated with this option
     */
    public JTypistOption(SwingTypist typist){
        super();
        this.setLayout(new FlowLayout());
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        this.typist = typist;
        this.selected = false;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.add(this.buildIndicator());
        JTypistInfo info = new JTypistInfo(typist);
        this.add(info);
        info.update();
    }

    /**
     * @return indicator for if option is selected
     */
    private JPanel buildIndicator(){
        JPanel panel = new JPanel(new BorderLayout());
        this.indicator = new JLabel();
        panel.add(this.indicator, SwingConstants.CENTER);
        return panel;
    }

    /**
     * @return typist this option represents
     */
    public SwingTypist getTypist(){
        return this.typist;
    }

    /**
     * @return is option is selected
     */
    public boolean isSelected(){
        return this.selected;
    }

    /**
     * Sets indicator and selected
     */
    public void select(){
        indicator.setText("●");
        this.selected = true;
    }
    
    /**
     * Sets indicator and selected
     */
    public void unselect(){
        indicator.setText("");
        this.selected = false;
    }
}
