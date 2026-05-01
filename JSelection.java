import java.awt.*;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.swing.*;

/**
 * A class that holds and displays a place for selecting typists for any purposes.
 * if list of typists is needed outside set onAdd to add to your list and same with onRemove.
 *
 * @author Adil Akylov
 * @version 1.2
 */
abstract public class JSelection<T, C extends JComponent, O extends JMyOption<T, C>> extends JPanel {
    protected final ArrayList<O> options = new ArrayList<>();
    private final boolean multiselect;
    protected ArrayList<T> values;
    protected Predicate<T> onAdd = t -> true;
    protected Predicate<T> onRemove = t -> true;
    protected Supplier<Boolean> onRemoveAll = () -> true;

    /**
     * @param value value associated with option to build
     * @return Option with given value
     */
    abstract protected O buildOption(T value);

    /**
     * Constructor for objects of class JSelection.
     * Creates a JPanel with JOption for each value given and adds event listener to it to call onAdd or onRemove.
     *
     * @param values values which will be available to choose from
     * @param multiselect true if multiple options can be selected at once. default is true
     */
    public JSelection(ArrayList<T> values, boolean multiselect){
        super();
        this.setLayout(new GridLayout(0, 1, 0, 10));
        this.values = values;
        this.buildOptions();
        this.multiselect = multiselect;
    }

    /**
     * @param values values to add as options to JSelection
     */
    private void buildOptions(){
        for(T value : this.values){
            this.addOption(value);
        }
    }

    /**
     * @param value value to build and add to selection
     */
    public void addOption(T value){
        O option = this.buildOption(value);
        option.addMouseListener(FI.onLeftClick(() -> toggle(option)));
        this.options.add(0, option);
        this.add(option, 0);
    }

    /**
     * Gets JMyOption object from the options based on value. if not found null
     * 
     * @param value value to find amoung options
     * @return JMyOption corresponding to value
     */
    private O getOption(T value){
        for(O option : this.options){
            if(option.getValue().equals(value)){
                return option;
            }
        }return null;
    }

    /**
     * @param value value whose option to toggle
     */
    public void toggle(T value){
        this.toggle(this.getOption(value));
    }

    /**
     * @param option option that needs to be toggled
     */
    private void toggle(O option){
        if(option.isSelected()){
            this.unselect(option);
        }else this.select(option);
    }

    /**
     * calls the custom callback onAdd. If it returns false, doesn't proceed. otherwise selects option
     *
     * @param option to select
     */
    protected void select(O option){
        if(!this.onAdd.test(option.getValue())) return;
        if(!this.multiselect) this.unselectAll();
        option.select();
    }

    /**
     * calls the custom callback onRemove. If it returns false, doesn't proceed. otherwise unselects option
     *
     * @param option to unselect
     */
    protected void unselect(O option){
        if(!this.onRemove.test(option.getValue())) return;
        option.unselect();
    }

    /**
     * unselects all options with calling onRemove on all of them
     */
    public void unselectAll(){
        for(O option : this.options){
            if(option.isSelected()) this.unselect(option);
        }
    }

    /**
     * @param tryAdding lambda that is called when option is selected.
     * false return is considered as failure, doesn't select
    */
    public void onTryAdd(Predicate<T> tryAdding){
        this.onAdd = tryAdding;
    }

    /**
     * @param adding lambda that is called when option is selected.
    */
    public void onAdd(Consumer<T> adding){
        this.onAdd = t -> {
            adding.accept(t);
            return true;
        };
    }

    /**
     * @param tryRemoving lambda that is called when option is unselected.
     * false return is considered as failure, doesn't unselect
    */
    public void onTryRemove(Predicate<T> tryRemoving){
        this.onRemove = tryRemoving;
    }

    /**
     * @param removing lambda that is called when option is unselected.
    */
    public void onRemove(Consumer<T> removing){
        this.onRemove = t -> {
            removing.accept(t);
            return true;
        };
    }
}