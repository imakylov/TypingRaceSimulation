
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
    private final SwingTypist typist;
    private final JLabel name = new JLabel();
    private final JLabel accuracy = new JLabel();
    private final JLabel WPM = new JLabel();
    private final JLabel bestWPM = new JLabel();
    private final JLabel mistypePercentage = new JLabel();

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
        this.add(this.name);
        this.add(this.accuracy);
        this.add(this.WPM);
        this.add(this.bestWPM);
        this.add(this.mistypePercentage);
    }

    /**
     * Updates the name, color and accuracy of the typist on the component
     */
    public void update(){
        this.name.setText(this.typist.getName());
        this.name.setForeground(this.typist.getColor());
        this.accuracy.setText("Accuracy: " + (int)(100*this.typist.getAccuracy()) + "%");
        this.WPM.setText("Global WPM: " + this.typist.getWPM());
        this.bestWPM.setText("best WPM: " + this.typist.getBestWPM());
        this.mistypePercentage.setText("Mistype percentage: " + (int) this.typist.getGlobalMistypePercentage() + "%");
    }
}
