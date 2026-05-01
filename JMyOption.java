import java.awt.*;
import java.awt.event.MouseListener;
import java.util.function.Supplier;
import javax.swing.*;

/**
 * A class for holding and displaying option.
 */
abstract public class JMyOption<T, C extends JComponent> extends JPanel {
    protected final T value;
    protected final int width;
    protected C rep;
    private boolean selected;
    private JLabel indicator;

    abstract int getMaxHeight();
    /**
     * @param value value to be represented by component
     * @return component to represent the value
     */
    abstract C buildValue(T value, int width);

    /**
     * Constructor for objects of class JMyOption.
     * Creates a JPanel with pointing cursor. and an indicator that shows when option is selected
     * 
     * @param value value which will be associated with this option
     */
    protected JMyOption(T value, int width){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.value = value;
        this.width = width;
        this.selected = false;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.add(this.buildIndicator());
    }
    
    protected static <T, C extends JComponent, O extends JMyOption<T, C>> O makeOption(Supplier<O> constructor){
        O option = constructor.get();
        option.setMaximumSize(new Dimension(Integer.MAX_VALUE, option.getMaxHeight()));
        option.setPreferredSize(new Dimension(option.width+50, option.getMaxHeight()));
        option.rep = option.buildValue(option.value, option.width);
        option.add(option.rep);
        option.rep.setMaximumSize(new Dimension(option.width, Integer.MAX_VALUE));
        return option;
    }

    /**
     * adds and propogate mouse listener to it's child
     * @param l listener to propogate
     */
    @Override
    public void addMouseListener(MouseListener l){
        super.addMouseListener(l);
        this.rep.addMouseListener(l);
        this.indicator.addMouseListener(l);
    }

    /**
     * @return indicator for if option is selected
     */
    private JPanel buildIndicator(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(30, 30));
        this.indicator = new JLabel("", SwingConstants.CENTER);
        this.indicator.setForeground(Color.GREEN);
        panel.add(this.indicator);
        return panel;
    }

    /**
     * @return value this option represents
     */
    public T getValue(){
        return this.value;
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
        this.indicator.setText("●");
        this.selected = true;
    }

    /**
     * Sets indicator and selected
     */
    public void unselect(){
        this.indicator.setText("");
        this.selected = false;
    }
}
