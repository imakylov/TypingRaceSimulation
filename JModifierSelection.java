
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.*;

/**
 * A class that holds and displays a place for selecting passages for any purposes.
 * if list of typists is needed outside set onAdd to add to your list and same with onRemove.
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class JModifierSelection extends JSelection<String, JLabel, JModifierOption> {
    /**
     * @return default list of SwingTypists
    */

    public JModifierSelection() {
        ArrayList<String> modifiers = new ArrayList<>();
        modifiers.add("Autocorrect");
        modifiers.add("Caffeine Mode");
        modifiers.add("Night Shift");
        super(modifiers, true);
    }

    @Override
    protected JModifierOption buildOption(String passage) {
        return JModifierOption.makeOption(passage);
    }
}

class JModifierOption extends JMyOption<String, JLabel>{
    @Override
    int getMaxHeight() {return 70;}
    
    @Override
    JLabel buildValue(String modifier, int width) {
        JLabel mod = new JLabel(modifier);
        Utils.setStableSize(mod, width, this.getMaxHeight());
        return mod;
    }

    private JModifierOption(String modifier){
        super(modifier, 80);
    }

    public static JModifierOption makeOption(String passage){
        return JMyOption.makeOption(() -> new JModifierOption(passage));
    }
}