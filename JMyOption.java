import java.awt.*;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 * A class for holding and displaying option.
 */
abstract class JMyOption<T, C extends JComponent> extends JPanel {
    private final T value;
    private final C rep;
    private boolean selected;
    private JLabel indicator;

    abstract int getMaxHeight();
    /**
     * @param value value to be represented by component
     * @return component to represent the value
     */
    abstract C buildValue(T value);

    /**
     * Constructor for objects of class JMyOption.
     * Creates a JPanel with pointing cursor. and an indicator that shows when option is selected
     * 
     * @param value value which will be associated with this option
     */
    public JMyOption(T value){
        super();
        this.setLayout(new FlowLayout());
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.getMaxHeight()));
        this.value = value;
        this.selected = false;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.add(this.buildIndicator());
        this.rep = this.buildValue(value);
        this.add(this.rep);
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
        this.indicator = new JLabel();
        panel.add(this.indicator, SwingConstants.CENTER);
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
