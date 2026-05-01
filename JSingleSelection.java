
import java.util.ArrayList;
import javax.swing.JComponent;

/**
 * A class that holds and displays a place for selecting one out of many options.
 *
 * @author Adil Akylov
 * @version 1.2
 */
abstract public class JSingleSelection<T, C extends JComponent> extends JSelection<T, C>{
    public JSingleSelection(ArrayList<T> optionsValues) {
        super(optionsValues);
    }
    /**
     * calls the custom callback onAdd. If it returns false, doesn't proceed.
     * unselects all options and then selects the given option
     *
     * @param option to select
     */
    @Override
    protected void select(JMyOption<T, C> option){
        if(!this.onAdd.test(option.getValue())) return;
        this.unselectAll();
        option.select();
    }
}
