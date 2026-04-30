
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A class that holds and displays info about the typist
 *
 * @author Adil Akylov
 * @version 1.2
 */
public class JTypistInfo extends JPanel {
    private final JLabel name;
    private final JLabel accuracy;
    private final SwingTypist typist;

    /**
     * Constructor for objects of class JTypistInfo.
     * Creates a JPanel with typist's name with their color and accuracy
     * 
     * @param typist typist whose info to display
     */
    public JTypistInfo(SwingTypist typist){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.typist = typist;
        this.name = new JLabel();
        this.accuracy = new JLabel();
        this.add(this.name);
        this.add(this.accuracy);
    }

    /**
     * Updates the name, color and accuracy of the typist on the component
     */
    public void update(){
        this.name.setText(this.typist.getName());
        this.name.setForeground(this.typist.getColor());
        this.accuracy.setText("Accuracy: " + (int)(100*this.typist.getAccuracy()) + "%");
    }
}
