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
abstract public class JSelection<T, C extends JComponent> extends JPanel {
    private final ArrayList<JMyOption<T, C>> options = new ArrayList<>();
    protected Predicate<T> onAdd = FI.True();
    protected Predicate<T> onRemove = FI.True();

    /**
     * @param value value associated with option to build
     * @return Option with given value
     */
    abstract JMyOption<T, C> buildOption(T value);

    /**
     * Constructor for objects of class JSelection.
     * Creates a JPanel with JOption for each value given and adds event listener to it to call onAdd or onRemove.
     *
     * @param optionsValues values which will be available to choose from
     */
    public JSelection(ArrayList<T> optionsValues){
        super();
        this.setLayout(new GridLayout(0, 1, 0, 10));
        this.buildOptions(optionsValues);
    }

    /**
     * @param optionsValues values to add as options to JSelection
     */
    private void buildOptions(ArrayList<T> optionsValues){
        for(T optionValue : optionsValues){
            JMyOption<T, C> option = this.buildOption(optionValue);
            option.addMouseListener(Utils.onLeftClick(() -> toggle(option)));
            this.options.add(option);
            this.add(option);
        }
    }

    /**
     * Gets JMyOption object from the options based on value. if not found null
     * 
     * @param optionValue value to find amoung options
     * @return JMyOption corresponding to value
     */
    private JMyOption<T, C> getOption(T optionValue){
        for(JMyOption<T, C> option : this.options){
            if(option.getValue().equals(optionValue)){
                return option;
            }
        }return null;
    }

    /**
     * @param optionValue value whose option to toggle
     */
    public void toggle(T optionValue){
        this.toggle(this.getOption(optionValue));
    }

    /**
     * @param option option that needs to be toggled
     */
    private void toggle(JMyOption<T, C> option){
        if(option.isSelected()){
            this.unselect(option);
        }else this.select(option);
    }

    /**
     * calls the custom callback onAdd. If it returns false, doesn't proceed. otherwise selects option
     *
     * @param option to select
     */
    private void select(JMyOption<T, C> option){
        if(!this.onAdd.test(option.getValue())) return;
        option.select();
    }

    /**
     * calls the custom callback onRemove. If it returns false, doesn't proceed. otherwise unselects option
     *
     * @param option to unselect
     */
    private void unselect(JMyOption<T, C> option){
        if(!this.onRemove.test(option.getValue())) return;
        option.unselect();
    }

    /**
     * unselects all options with calling onRemove on all of them
     */
    public void unselectAll(){
        for(JMyOption<T, C> option : this.options){
            if(option.isSelected()) this.unselect(option);
        }
    }

    /**
     * @param tryAdding lambda that is called when option is selected.
     * false return is considered as failure, doesn't select
    */
    public void onAdd(Predicate<T> tryAdding){
        this.onAdd = tryAdding;
    }

    /**
     * @param tryRemoving lambda that is called when option is unselected.
     * false return is considered as failure, doesn't unselect
    */
    public void onRemove(Predicate<T> tryRemoving){
        this.onRemove = tryRemoving;
    }
}