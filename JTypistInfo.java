
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JTypistInfo extends JPanel {
    private final JLabel name;
    private final JLabel accuracy;
    private final SwingTypist typist;
    public JTypistInfo(SwingTypist typist){
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.typist = typist;
        this.name = new JLabel();
        this.accuracy = new JLabel();
        this.add(this.name);
        this.add(this.accuracy);
    }
    public void update(){
        this.name.setText(this.typist.getName());
        this.name.setForeground(this.typist.getColor());
        this.accuracy.setText("Accuracy: " + (int)(100*this.typist.getAccuracy()) + "%");
    }
}
